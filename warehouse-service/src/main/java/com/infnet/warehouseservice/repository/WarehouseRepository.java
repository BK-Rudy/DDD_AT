package com.infnet.warehouseservice.repository;

import com.infnet.warehouseservice.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Warehouse findByProductId(Long productId);
}
