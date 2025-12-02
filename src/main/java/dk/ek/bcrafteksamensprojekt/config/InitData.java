package dk.ek.bcrafteksamensprojekt.config;

import dk.ek.bcrafteksamensprojekt.dto.Case.CaseMaterialRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Case.CaseRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Case.CaseResponseDTO;
import dk.ek.bcrafteksamensprojekt.dto.Material.MaterialRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Material.MaterialResponseDTO;
import dk.ek.bcrafteksamensprojekt.dto.OfferRequest.OfferRequestRequestDTO;
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

        // --- USERS ---
        userAuthenticationService.createUser("John", "NielsenEnjoyer3");
        userAuthenticationService.createUser("sofie", "oaklover2");


        // --- MATERIALS ---
        MaterialResponseDTO walnut = materialService.createMaterial(
                new MaterialRequestDTO("Black Walnut Hardwood", 975.0, "m2")
        );

        MaterialResponseDTO spruce = materialService.createMaterial(
                new MaterialRequestDTO("Spruce Timber", 280.0, "m")
        );

        MaterialResponseDTO plywood12 = materialService.createMaterial(
                new MaterialRequestDTO("Plywood 12", 1200.0, "m")
        );


        // --- CUSTOMERS ---
        Customer c1 = customerService.createCustomer(
                new Customer("Henrik", "Poulsen", "421123123", "henrik@example.com",
                        "Hasselvej 42", "7100", "Vejle")
        );

        Customer c2 = customerService.createCustomer(
                new Customer("Camilla", "Thomsen", "513123123", "camilla@example.com",
                        "Camilla 421", "7100", "Vejle")
        );


        // --- CASES ---
        CaseResponseDTO shoeRack = caseService.createCase(
                new CaseRequestDTO(
                        "Entryway Shoe Rack",
                        "Compact walnut shoe rack with two shelves.",
                        "Snedker",         // enum pretty print
                        c1.getId(),
                        List.of(
                                new CaseMaterialRequestDTO(
                                        "Walnut shelves (cut to size)",
                                        2,
                                        975.0,
                                        walnut.id()
                                ),
                                new CaseMaterialRequestDTO(
                                        "Walnut support rails",
                                        3,
                                        975.0,
                                        walnut.id()
                                )
                        )
                )
        );

        CaseResponseDTO diningBench = caseService.createCase(
                new CaseRequestDTO(
                        "Dining Room Bench",
                        "Long spruce bench with Scandinavian lines.",
                        "Snedker",
                        c2.getId(),
                        List.of(
                                new CaseMaterialRequestDTO(
                                        "Spruce planks for bench seat",
                                        4,
                                        280.0,
                                        spruce.id()
                                ),
                                new CaseMaterialRequestDTO(
                                        "12mm birch plywood underside supports",
                                        2,
                                        190.0,
                                        plywood12.id()
                                )
                        )
                )
        );


        // --- OFFER REQUESTS ---
        offerRequestService.create(
                new OfferRequestRequestDTO(
                        "Martin",
                        "Due",
                        "33445522",
                        "martin.d@example.com",
                        "Looking for a custom wall-mounted bookshelf in ash wood.",
                        "Snedker"
                )
        );

        offerRequestService.create(
                new OfferRequestRequestDTO(
                        "Kristina",
                        "Ravn",
                        "60607788",
                        "kristina.r@example.com",
                        "Request for a built-in wardrobe, white painted finish.",
                        "Snedker"
                )
        );
    }
}

