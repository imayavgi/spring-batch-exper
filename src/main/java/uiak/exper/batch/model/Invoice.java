package uiak.exper.batch.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class Invoice {
    @Id
    public Integer invoice; // invoice
    public String date; // date

    @OneToOne
    public Person billTo;// bill-to

    @OneToOne
    public Person shipTo;// ship-to

    @OneToMany(targetEntity = Product.class)
    public List<Product> product;
    public Float tax;
    public Float total;
    public String comments;

    @Override
    public String toString() {
        return "Invoice: " + invoice + " - " + " date" + date;
    }

}

