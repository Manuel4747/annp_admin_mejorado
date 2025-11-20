package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.Avisos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "avisosController")
@ViewScoped
public class AvisosController extends AbstractController<Avisos> {


    public AvisosController() {
        // Inform the Abstract parent controller of the concrete Avisos Entity
        super(Avisos.class);
    }
     
}
