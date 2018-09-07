package uiak.exper.batch.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String sku;
    public Integer quantity;
    public String description;
    public Float price;

    @Override
    public String toString() {
        return "Product: " + sku;
    }
}