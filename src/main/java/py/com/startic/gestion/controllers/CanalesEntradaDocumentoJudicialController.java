package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.CanalesEntradaDocumentoJudicial;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "canalesEntradaDocumentoJudicialController")
@ViewScoped
public class CanalesEntradaDocumentoJudicialController extends AbstractController<CanalesEntradaDocumentoJudicial> {

    @Inject
    private EmpresasController empresaController;

    public CanalesEntradaDocumentoJudicialController() {
        // Inform the Abstract parent controller of the concrete EstadosDocumento Entity
        super(CanalesEntradaDocumentoJudicial.class);
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
