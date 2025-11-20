package py.com.startic.gestion.controllers;

import py.com.startic.gestion.models.Resuelve;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "resuelveController")
@ViewScoped
public class ResuelveController extends AbstractController<Resuelve> {

    @Inject
    private EmpresasController empresaController;

    public ResuelveController() {
        // Inform the Abstract parent controller of the concrete Sentencia Entity
        super(Resuelve.class);
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
        Resuelve selected = this.getSelected();
        if (selected != null && empresaController.getSelected() == null) {
            empresaController.setSelected(selected.getEmpresa());
        }
    }

}
