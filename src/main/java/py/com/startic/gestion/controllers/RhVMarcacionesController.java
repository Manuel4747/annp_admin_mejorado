package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.RhVMarcaciones;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;

@Named(value = "rhVMarcacionesController")
@ViewScoped
public class RhVMarcacionesController extends AbstractController<RhVMarcaciones> {


    public RhVMarcacionesController() {
        // Inform the Abstract parent controller of the concrete RhVMarcaciones Entity
        super(RhVMarcaciones.class);
    }

}
