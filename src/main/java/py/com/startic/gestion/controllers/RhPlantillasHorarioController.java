package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.RhPlantillasHorario;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "rhPlantillasHorarioController")
@ViewScoped
public class RhPlantillasHorarioController extends AbstractController<RhPlantillasHorario> {

    @Inject
    private EmpresasController empresaController;

    public RhPlantillasHorarioController() {
        // Inform the Abstract parent controller of the concrete RhPlantillasHorario Entity
        super(RhPlantillasHorario.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of RhDetPlantillasHorario
     * entities that are retrieved from RhPlantillasHorario?cap_first and
     * returns the navigation outcome.
     *
     * @return navigation outcome for RhDetPlantillasHorario page
     */
    public String navigateRhDetPlantillasHorarioCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RhDetPlantillasHorario_items", this.getSelected().getRhDetPlantillasHorarioCollection());
        }
        return "/pages21/rhDetPlantillasHorario/index";
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
    }
}
