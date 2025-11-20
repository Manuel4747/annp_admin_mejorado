package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.Profesiones;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "profesionesController")
@ViewScoped
public class ProfesionesController extends AbstractController<Profesiones> {

    @Inject
    private EmpresasController empresaController;

    public ProfesionesController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(Profesiones.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }


}
