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

    @OneToMany(mappedBy = "c", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CaseMaterial> caseMaterials = new ArrayList<>();

    public Case(Long id, String title, String description, LocalDate createdAt, Customer customer, List<CaseMaterial> caseMaterials) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.customer = customer;
        this.caseMaterials = caseMaterials;
    }

    public Case() {}

    // Sets the case on the caseMaterial and then adds the material to the caseMaterial-list
    public void addCaseMaterial(CaseMaterial caseMaterial) {
        if (caseMaterial == null) return;
        caseMaterial.setC(this);
        this.caseMaterials.add(caseMaterial);
    }

    // Removes the case on the caseMaterial and then removes the material from the caseMaterial-list
    public void removeCaseMaterial(CaseMaterial caseMaterial) {
        if (caseMaterial == null) return;
        caseMaterial.setC(null);
        this.caseMaterials.remove(caseMaterial);
    }

}
