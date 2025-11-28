package dk.ek.bcrafteksamensprojekt.config;

import dk.ek.bcrafteksamensprojekt.model.*;
import dk.ek.bcrafteksamensprojekt.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
    public void run(String... args) throws Exception {

        // --- USERS ---
        User u1 = userAuthenticationService.createUser("John", "NielsenEnjoyer3");
        u1.setFirstName("John");
        u1.setLastName("Nielsen");

        User u2 = userAuthenticationService.createUser("sofie", "oaklover2");
        u2.setFirstName("Sofie");
        u2.setLastName("Nørgaard");

        userAuthenticationService.createUser(u1.getUsername(), u1.getPassword());
        userAuthenticationService.createUser(u2.getUsername(), u2.getPassword());


        // --- MATERIALS ---
        Material walnut = new Material();
        walnut.setName("Black Walnut Hardwood");
        walnut.setPricePerUnit(975.0);
        walnut.setUnit("m²");

        Material spruce = new Material();
        spruce.setName("Spruce Timber");
        spruce.setPricePerUnit(280.0);
        spruce.setUnit("m");

        Material plywood12 = new Material();
        plywood12.setName("12mm Birch Plywood");
        plywood12.setPricePerUnit(190.0);
        plywood12.setUnit("m²");

        materialService.createMaterial(walnut);
        materialService.createMaterial(spruce);
        materialService.createMaterial(plywood12);


        // --- CUSTOMERS ---
        Customer c1 = new Customer();
        c1.setFirstName("Henrik");
        c1.setLastName("Poulsen");
        c1.setPhoneNumber("41223344");
        c1.setEmail("henrik.poulsen@example.com");
        c1.setAddress("Hasselvej 42");
        c1.setZipCode("7100");
        c1.setCity("Vejle");

        Customer c2 = new Customer();
        c2.setFirstName("Camilla");
        c2.setLastName("Thomsen");
        c2.setPhoneNumber("50554433");
        c2.setEmail("camilla.t@example.com");
        c2.setAddress("Kastanievej 9");
        c2.setZipCode("5000");
        c2.setCity("Odense");

        customerService.createCustomer(c1);
        customerService.createCustomer(c2);


        // --- CASES ---
        Case shoeRack = new Case();
        shoeRack.setTitle("Entryway Shoe Rack");
        shoeRack.setDescription("Compact walnut shoe rack with two shelves.");
        shoeRack.setCreatedAt(LocalDate.now().minusDays(14));
        shoeRack.setCustomer(c1);

        Case diningBench = new Case();
        diningBench.setTitle("Dining Room Bench");
        diningBench.setDescription("Long spruce bench with clean Scandinavian lines.");
        diningBench.setCreatedAt(LocalDate.now().minusDays(5));
        diningBench.setCustomer(c2);

        caseService.createCase(shoeRack.getId(), shoeRack);
        caseService.createCase(diningBench.getId(), diningBench);


        // --- CASE MATERIALS ---
        CaseMaterial scm1 = new CaseMaterial();
        scm1.setDescription("Walnut shelves (cut to size)");
        scm1.setQuantity(2);
        scm1.setUnitPrice(975.0);
        scm1.setMaterial(walnut);
        scm1.setC(shoeRack);

        CaseMaterial scm2 = new CaseMaterial();
        scm2.setDescription("Walnut support rails");
        scm2.setQuantity(3);
        scm2.setUnitPrice(975.0);
        scm2.setMaterial(walnut);
        scm2.setC(shoeRack);

        CaseMaterial dcm1 = new CaseMaterial();
        dcm1.setDescription("Spruce planks for bench seat");
        dcm1.setQuantity(4);
        dcm1.setUnitPrice(280.0);
        dcm1.setMaterial(spruce);
        dcm1.setC(diningBench);

        CaseMaterial dcm2 = new CaseMaterial();
        dcm2.setDescription("12mm birch plywood for underside supports");
        dcm2.setQuantity(2);
        dcm2.setUnitPrice(190.0);
        dcm2.setMaterial(plywood12);
        dcm2.setC(diningBench);

        materialService.createMaterial(scm1.getMaterial());
        materialService.createMaterial(scm2.getMaterial());
        materialService.createMaterial(dcm1.getMaterial());
        materialService.createMaterial(dcm2.getMaterial());

        shoeRack.setCaseMaterials(List.of(scm1, scm2));
        diningBench.setCaseMaterials(List.of(dcm1, dcm2));

        caseService.createCase(shoeRack.getId(), shoeRack);
        caseService.createCase(diningBench.getId(), diningBench);


        // --- OFFER REQUESTS ---
        OfferRequest or1 = new OfferRequest();
        or1.setFirstName("Martin");
        or1.setLastName("Due");
        or1.setPhoneNumber("33445522");
        or1.setEmail("martin.d@example.com");
        or1.setDescription("Looking for a custom wall-mounted bookshelf in ash wood.");

        OfferRequest or2 = new OfferRequest();
        or2.setFirstName("Kristina");
        or2.setLastName("Ravn");
        or2.setPhoneNumber("60607788");
        or2.setEmail("kristina.r@example.com");
        or2.setDescription("Request for a built-in wardrobe, white painted finish.");

        offerRequestService.createOfferRequest(or1);
        offerRequestService.createOfferRequest(or2);
    }
}
