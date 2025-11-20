package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.Beneficios;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "beneficiosController")
@ViewScoped
public class BeneficiosController extends AbstractController<Beneficios> {


    public BeneficiosController() {
        // Inform the Abstract parent controller of the concrete Beneficios Entity
        super(Beneficios.class);
    }
     
}
