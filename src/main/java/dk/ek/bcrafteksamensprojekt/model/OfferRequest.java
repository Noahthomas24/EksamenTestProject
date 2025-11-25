package dk.ek.bcrafteksamensprojekt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="offer_requests")
@Getter
@Setter

// Offer request is the form that comes in when a customer fills out the contact form on the website
public class OfferRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    private String description;

    public OfferRequest(Long id, String firstName, String lastName, String phoneNumber, String email, String description) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.description = description;
    }

    public OfferRequest(){}
}
