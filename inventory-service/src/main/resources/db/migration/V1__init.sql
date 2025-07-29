CREATE TABLE `inventory` (
    id BIGINT(22) NOT NULL AUTO_INCREMENT,
    sku_code VARCHAR(255) NOT NULL,
    quantity INTEGER,
    primary key(id)
)