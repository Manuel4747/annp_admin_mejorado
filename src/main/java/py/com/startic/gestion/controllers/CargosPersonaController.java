package py.com.startic.gestion.controllers;

import py.com.startic.gestion.models.CargosPersona;
import py.com.startic.gestion.models.Personas;
import java.util.Collection;
import py.com.startic.gestion.facades.CargosPersonaFacade;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "cargosPersonaController")
@ViewScoped
public class CargosPersonaController extends AbstractController<CargosPersona> {

    @Inject
    private EmpresasController empresaController;

    // Flags to indicate if child collections are empty
    private boolean isPersonasCollectionEmpty;

    public CargosPersonaController() {
        // Inform the Abstract parent controller of the concrete CargosPersona Entity
        super(CargosPersona.class);
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
        CargosPersona selected = this.getSelected();
        if (selected != null && empresaController.getSelected() == null) {
            empresaController.setSelected(selected.getEmpresa());
        }
    }

    public boolean getIsPersonasCollectionEmpty() {
        return this.isPersonasCollectionEmpty;
    }

}
