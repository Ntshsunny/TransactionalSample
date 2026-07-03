package com.projJune.transaction.handler;

import com.projJune.transaction.entity.Product;
import com.projJune.transaction.repo.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryHandler {

    private final InventoryRepository inventoryRepository;

    public InventoryHandler(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Product updateProductDetails(Product product){
        //forecefully throwing the exception to stimualte Transaction
        if(product.getPrice()>5000){
            throw new RuntimeException("Db Crashed.....");
        }
        return inventoryRepository.save(product);
    }

    public Product getProduct(int id){
        return inventoryRepository.findById(id)
                .orElseThrow(
                        ()-> new RuntimeException("Product not found"));
    }
}
