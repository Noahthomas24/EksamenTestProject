package dk.ek.bcrafteksamensprojekt.web;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {

    @GetMapping("/")
    public String homePage(){
        return "forward:/index.html";
    }

    @GetMapping("/kontakt")
    public String contactPage(){
        return "forward:/kontakt.html";
    }

    @GetMapping("/ydelser")
    public String servicesPage(){
        return "forward:/ydelser.html";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "forward:/admin/login.html";
    }

    @GetMapping("/cases")
    public String casesPage(){
        return "forward:/admin/cases.html";
    }

    @GetMapping("/customers")
    public String customersPage(){
        return "forward:/admin/customers.html";
    }

    @GetMapping("/dashboard")
    public String dashboardPage(){
        return "forward:/admin/dashboard.html";
    }

    @GetMapping("/materials")
    public String materialsPage(){
        return "forward:/admin/materials.html";
    }
}
