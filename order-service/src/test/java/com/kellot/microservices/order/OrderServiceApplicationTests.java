package com.kellot.microservices.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kellot.microservices.order.dto.OrderRequest;
import com.kellot.microservices.order.dto.OrderResponse;
import com.kellot.microservices.order.model.Order;
import com.kellot.microservices.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@Slf4j
class OrderServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;


    @Test
    void shouldCreateOrder() throws Exception {
        OrderRequest orderRequest = new OrderRequest("order-number-1", "sku-code-1", new BigDecimal("1000.00"), 1);
        String orderRequestString = objectMapper.writeValueAsString(orderRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestString))
                .andExpect(status().isCreated());

        Assertions.assertEquals(1, orderRepository.findAll().size());
    }

    @Test
    void shouldReturnOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOrderById() throws Exception {
        Order order = new Order();
        order.setOrderNumber("order-number-2");
        order.setSkuCode("sku-code-2");
        order.setPrice(new BigDecimal("2000.00"));
        order.setQuantity(2);
        orderRepository.save(order);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{id}", order.getId()))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        OrderResponse orderResponse = objectMapper.readValue(contentAsString, OrderResponse.class);

        Assertions.assertEquals(order.getId(), orderResponse.id());
        Assertions.assertEquals(order.getOrderNumber(), orderResponse.orderNumber());
        Assertions.assertEquals(order.getSkuCode(), orderResponse.skuCode());
        Assertions.assertEquals(order.getPrice().toEngineeringString(), orderResponse.price().toEngineeringString());
        Assertions.assertEquals(order.getQuantity(), orderResponse.quantity());
    }

}
