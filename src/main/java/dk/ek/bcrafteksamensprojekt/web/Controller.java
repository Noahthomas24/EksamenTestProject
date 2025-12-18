package dk.ek.bcrafteksamensprojekt.web;

import dk.ek.bcrafteksamensprojekt.service.UserAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    UserAuthenticationService userAuthenticationService;

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
    public String casesPage(HttpServletRequest request){
        if (!userAuthenticationService.validateLogin(request)){
            return "redirect:/login";
        }
        return "forward:/admin/cases.html";
    }

    @GetMapping("/customers")
    public String customersPage(HttpServletRequest request){
        if (!userAuthenticationService.validateLogin(request)){
            return "redirect:/login";
        }
        return "forward:/admin/customers.html";
    }

    @GetMapping("/dashboard")
    public String dashboardPage(HttpServletRequest request){
        if (!userAuthenticationService.validateLogin(request)){
            return "redirect:/login";
        }
        return "forward:/admin/dashboard.html";
    }

    @GetMapping("/materials")
    public String materialsPage(HttpServletRequest request){
        if (!userAuthenticationService.validateLogin(request)){
            return "redirect:/login";
        }
        return "forward:/admin/materials.html";
    }
    @GetMapping("/offers")
    public String offers(HttpServletRequest request) {
        if (!userAuthenticationService.validateLogin(request)){
            return "redirect:/login";
        }
        return "forward:/admin/offers.html";
    }
}
