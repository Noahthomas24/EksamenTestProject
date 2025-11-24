package dk.ek.bcrafteksamensprojekt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CaseMaterial {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private int quantity;
    private Double unitPrice;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case c;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    // This is made so the user can overwrite the materials price (if they want to give discount)
    public Double getEffectiveUnitPrice() {
        if (unitPrice != null) {
            return unitPrice;
        }

        if (material != null && material.getPricePerUnit() != null){
            return material.getPricePerUnit();
        }

        return 0.0;
    }

    public CaseMaterial(Long id, String description, int quantity, Double unitPrice, Case c, Material material) {
        this.id = id;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.c = c;
        this.material = material;
    }

    public CaseMaterial() {}
}
