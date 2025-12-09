package dk.ek.bcrafteksamensprojekt.config;

import dk.ek.bcrafteksamensprojekt.dto.Case.CaseMaterialRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Case.CaseRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Case.CaseResponseDTO;
import dk.ek.bcrafteksamensprojekt.dto.Customer.CustomerRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Customer.CustomerResponseDTO;
import dk.ek.bcrafteksamensprojekt.dto.Material.MaterialRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Material.MaterialResponseDTO;
import dk.ek.bcrafteksamensprojekt.dto.OfferRequest.OfferRequestRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Users.UserRequestDTO;
import dk.ek.bcrafteksamensprojekt.model.*;
import dk.ek.bcrafteksamensprojekt.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitData implements CommandLineRunner {

    private final MaterialService materialService;
    private final CustomerService customerService;
    private final CaseService caseService;
    private final OfferRequestService offerRequestService;
    private final UserAuthenticationService userAuthenticationService;


    @Override
    public void run(String... args) {

        createUsers();
        var materials = createMaterials();
        var customers = createCustomers();
        createCases(customers, materials);
        createOfferRequests();
    }

    // --------------------------------------------------------
    // USERS
    // --------------------------------------------------------

    private void createUsers() {
        userAuthenticationService.register(
                new UserRequestDTO("John", "NielsenEnjoyer3", "John", "Nielsen")
        );

        userAuthenticationService.register(
                new UserRequestDTO("sofie", "oaklover2", "Sofie", "Nørgaard")

        );
        userAuthenticationService.register(
                new UserRequestDTO("demo", "demo", "demo", "demo")
        );
    }

    // --------------------------------------------------------
    // MATERIALS
    // --------------------------------------------------------

    private record Materials(MaterialResponseDTO walnut, MaterialResponseDTO spruce, MaterialResponseDTO plywood12) {}

    private Materials createMaterials() {

        MaterialResponseDTO walnut = materialService.createMaterial(
                new MaterialRequestDTO("Black Walnut Hardwood", 975.0, "m2")
        );

        MaterialResponseDTO spruce = materialService.createMaterial(
                new MaterialRequestDTO("Spruce Timber", 280.0, "m")
        );

        MaterialResponseDTO plywood12 = materialService.createMaterial(
                new MaterialRequestDTO("Plywood 12", 1200.0, "m")
        );

        return new Materials(walnut, spruce, plywood12);
    }

    // --------------------------------------------------------
    // CUSTOMERS
    // --------------------------------------------------------

    private record Customers(CustomerResponseDTO c1, CustomerResponseDTO c2) {}

    private Customers createCustomers() {

        CustomerResponseDTO c1 = customerService.createCustomer(
                new CustomerRequestDTO(
                        "Henrik", "Poulsen", "henrik@example.com", "421123123",
                        "Hasselvej 42", "7100", "Vejle"
                )
        );

        CustomerResponseDTO c2 = customerService.createCustomer(
                new CustomerRequestDTO(
                        "Camilla", "Thomsen", "camilla@example.com", "513123123",
                        "Camilla 421", "7100", "Vejle"
                )
        );

        return new Customers(c1, c2);
    }

    // --------------------------------------------------------
    // CASES
    // --------------------------------------------------------

    private void createCases(Customers customers, Materials materials) {

        // Shoe rack
        caseService.createCase(
                new CaseRequestDTO(
                        "Entryway Shoe Rack",
                        "Compact walnut shoe rack with two shelves.",
                        Type.WOODCRAFT,
                        customers.c1.id(),
                        List.of(
                                new CaseMaterialRequestDTO(
                                        "Walnut shelves (cut to size)",
                                        2,
                                        975.0,
                                        materials.walnut().id()
                                ),
                                new CaseMaterialRequestDTO(
                                        "Walnut support rails",
                                        3,
                                        975.0,
                                        materials.walnut().id()
                                )
                        )
                )
        );

        // Dining bench
        caseService.createCase(
                new CaseRequestDTO(
                        "Dining Room Bench",
                        "Long spruce bench with Scandinavian lines.",
                        Type.WOODCRAFT,
                        customers.c2.id(),
                        List.of(
                                new CaseMaterialRequestDTO(
                                        "Spruce planks for bench seat",
                                        4,
                                        280.0,
                                        materials.spruce().id()
                                ),
                                new CaseMaterialRequestDTO(
                                        "12mm birch plywood underside supports",
                                        2,
                                        190.0,
                                        materials.plywood12().id()
                                )
                        )
                )
        );
    }

    // --------------------------------------------------------
    // OFFER REQUESTS
    // --------------------------------------------------------

    private void createOfferRequests() {

        offerRequestService.create(
                new OfferRequestRequestDTO(
                        "Martin", "Due", "33445522", "martin.d@example.com",
                        "Looking for a custom wall-mounted bookshelf in ash wood.",
                        Type.WOODCRAFT
                )
        );

        offerRequestService.create(
                new OfferRequestRequestDTO(
                        "Kristina", "Ravn", "60607788", "kristina.r@example.com",
                        "Request for a built-in wardrobe, white painted finish.",
                        Type.WOODCRAFT
                )
        );
    }
}

