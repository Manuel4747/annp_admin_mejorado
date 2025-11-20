package py.com.startic.gestion.controllers;

import py.com.startic.gestion.models.EstadosProcesoPreliminar;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "estadosProcesoPreliminarController")
@ViewScoped
public class EstadosProcesoPreliminarController extends AbstractController<EstadosProcesoPreliminar> {

    @Inject
    private EmpresasController empresaController;

    public EstadosProcesoPreliminarController() {
        // Inform the Abstract parent controller of the concrete EstadosProceso Entity
        super(EstadosProcesoPreliminar.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        EstadosProcesoPreliminar selected = this.getSelected();
        if (selected != null && empresaController.getSelected() == null) {
            empresaController.setSelected(selected.getEmpresa());
        }
    }

}
