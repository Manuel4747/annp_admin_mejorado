package py.com.startic.gestion.controllers;


import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import py.com.startic.gestion.models.DireccionGeneral;

@Named(value = "direccionGeneralController")
@ViewScoped
public class DireccionGeneralController extends AbstractController<DireccionGeneral> {


    public DireccionGeneralController() {
        // Inform the Abstract parent controller of the concrete DireccionGeneral Entity
        super(DireccionGeneral.class);
    }
    public void resetParents() {
      
    }

}
