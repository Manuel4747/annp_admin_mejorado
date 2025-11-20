package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.AgDetallesAgendamiento;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "detallesAgendamientoController")
@ViewScoped
public class AgDetallesAgendamientoController extends AbstractController<AgDetallesAgendamiento> {

    @Inject
    private EmpresasController empresaController;

    public AgDetallesAgendamientoController() {
        // Inform the Abstract parent controller of the concrete AgDetallesAgendamiento Entity
        super(AgDetallesAgendamiento.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }


}
