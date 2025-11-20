package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.DocumentosEscaneadosPreliminares;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "documentosEscaneadosPreliminaresController")
@ViewScoped
public class DocumentosEscaneadosPreliminaresController extends AbstractController<DocumentosEscaneadosPreliminares> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;

    public DocumentosEscaneadosPreliminaresController() {
        // Inform the Abstract parent controller of the concrete Cuentas Entity
        super(DocumentosEscaneadosPreliminares.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresaController.setSelected(null);
    }


    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to
     * the data layer
     */
    @Override
    public void saveNew(ActionEvent event) {
        if (getSelected() != null) {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");
            
            getSelected().setEmpresa(usu.getEmpresa());

            super.saveNew(event);

        }
    }
}
