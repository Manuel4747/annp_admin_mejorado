package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "tiposDocumentosJudicialesController")
@ViewScoped
public class TiposDocumentosJudicialesController extends AbstractController<TiposDocumentosJudiciales> {

    @Inject
    private EmpresasController empresaController;

    public TiposDocumentosJudicialesController() {
        // Inform the Abstract parent controller of the concrete EstadosDocumento Entity
        super(TiposDocumentosJudiciales.class);
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

    /**
     * Sets the "items" attribute with a collection of DocumentosJudiciales
     * entities that are retrieved from EstadosDocumento?cap_first and returns
     * the navigation outcome.
     *
     * @return navigation outcome for DocumentosJudiciales page
     */
    public String navigateDocumentosJudicialesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DocumentosJudiciales_items", this.getSelected().getDocumentosJudicialesCollection());
        }
        return "/pages/documentosJudiciales/index";
    }

}
