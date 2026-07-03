package com.projJune.transaction.repo;

import com.projJune.transaction.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Product, Integer> {
}
