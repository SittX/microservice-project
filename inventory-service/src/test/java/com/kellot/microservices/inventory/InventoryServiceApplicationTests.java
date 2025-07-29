package com.kellot.microservices.inventory;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kellot.microservices.inventory.model.Inventory;
import com.kellot.microservices.inventory.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@Import(com.kellot.microservices.inventory.TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class InventoryServiceApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldCreateNewInventory() throws Exception {
		Inventory inventory = Inventory.builder().skuCode("sku-code-1").quantity(10).build();
		String inventoryString = objectMapper.writeValueAsString(inventory);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/inventory")
						.contentType(MediaType.APPLICATION_JSON)
						.content(inventoryString))
				.andExpect(status().isCreated())
				.andReturn();


		String resultString = result.getResponse().getContentAsString();
		Inventory responseObj = objectMapper.readValue(resultString, Inventory.class);

		Inventory expectedInventory = inventoryRepository.findBySkuCode(inventory.getSkuCode()).orElseThrow(() -> new RuntimeException("Error finding sku code : " + inventory.getSkuCode()));

		org.junit.jupiter.api.Assertions.assertEquals(expectedInventory.getSkuCode(), responseObj.getSkuCode());
		org.junit.jupiter.api.Assertions.assertEquals(expectedInventory.getQuantity(), responseObj.getQuantity());
	}

	@Test
	void shouldReturnInventoryList() throws Exception {
		List<Inventory> inventoryList = List.of(
				Inventory.builder().skuCode("sku-code-1").quantity(10).build(),
				Inventory.builder().skuCode("sku-code-2").quantity(20).build(),
				Inventory.builder().skuCode("sku-code-3").quantity(30).build(),
				Inventory.builder().skuCode("sku-code-4").quantity(40).build()
		);

		inventoryRepository.saveAll(inventoryList);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory"))
				.andExpect(status().isOk())
				.andReturn();

		String resultString = result.getResponse().getContentAsString();
		List<Inventory> responseList = objectMapper.readValue(resultString, new TypeReference<>() {});

		// 2. Assert that the deserialized object is a list
		Assertions.assertThat(responseList).isInstanceOf(List.class);

		// 3. Assert that the list is not empty and has the correct size
		Assertions.assertThat(responseList).hasSize(4);

		// 4. Assert that each object in the list is an instance of Inventory
		responseList.forEach(item -> Assertions.assertThat(item).isInstanceOf(Inventory.class));
	}

}