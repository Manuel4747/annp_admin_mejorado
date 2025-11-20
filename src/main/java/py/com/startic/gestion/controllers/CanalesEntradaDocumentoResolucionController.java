package py.com.startic.gestion.controllers;



import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import py.com.startic.gestion.models.CanalesEntradaDocumentoResolucion;

@Named(value = "canalesEntradaDocumentoResolucionController")
@ViewScoped
public class CanalesEntradaDocumentoResolucionController extends AbstractController<CanalesEntradaDocumentoResolucion> {

    @Inject
    private EmpresasController empresaController;

    public CanalesEntradaDocumentoResolucionController() {
        // Inform the Abstract parent controller of the concrete EstadosDocumento Entity
        super(CanalesEntradaDocumentoResolucion.class);
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
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
    }

}
