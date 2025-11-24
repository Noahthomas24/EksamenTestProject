package dk.ek.bcrafteksamensprojekt.model;


import jakarta.persistence.*;

@Entity
@Table(name = "materials")
public class Material {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double pricePerUnit;
    private String unit;

    public Material(Long id, String name, Double pricePerUnit, String unit) {
        this.id = id;
        this.name = name;
        this.pricePerUnit = pricePerUnit;
        this.unit = unit;
    }

    public Material() {}
}
