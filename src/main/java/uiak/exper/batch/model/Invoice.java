package uiak.exper.batch.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public Integer invoice; // invoice
    public String date; // date

    @OneToOne(cascade = CascadeType.ALL)
    public Person billTo;// bill-to

    @OneToOne(cascade = CascadeType.ALL)
    public Person shipTo;// ship-to

    @OneToMany(targetEntity = Product.class, cascade = CascadeType.ALL)
    public List<Product> product;
    public Float tax;
    public Float total;
    public String comments;

    @Override
    public String toString() {
        return "Invoice: " + invoice + " - " + " date" + date;
    }

}

