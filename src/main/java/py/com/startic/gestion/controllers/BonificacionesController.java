package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.Bonificaciones;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "bonificacionesController")
@ViewScoped
public class BonificacionesController extends AbstractController<Bonificaciones> {


    public BonificacionesController() {
        // Inform the Abstract parent controller of the concrete Bonificaciones Entity
        super(Bonificaciones.class);
    }
     
}
