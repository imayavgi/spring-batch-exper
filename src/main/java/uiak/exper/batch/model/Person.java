package uiak.exper.batch.model;

import javax.persistence.*;

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String given;
    public String family;

    @OneToOne
    public Address address;
}