package dk.ek.bcrafteksamensprojekt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "cases")
@Getter
@Setter
public class Case {
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate createdAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "case_materials",
            joinColumns = @JoinColumn(name = "case_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id"))
    private List<Material> materials = new ArrayList<>();

    public Case(Long id, String title, String description, LocalDate createdAt, Customer customer, List<Material> materials) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.customer = customer;
        this.materials = materials;
    }

    public Case() {
    }
}
