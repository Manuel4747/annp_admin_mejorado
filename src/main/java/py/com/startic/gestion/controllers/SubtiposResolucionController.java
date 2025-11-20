package py.com.startic.gestion.controllers;

import java.util.Collection;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.SubtiposResolucion;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "subtiposResolucionController")
@ViewScoped
public class SubtiposResolucionController extends AbstractController<SubtiposResolucion> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;

    public SubtiposResolucionController() {
        // Inform the Abstract parent controller of the concrete SubtiposResolucion Entity
        super(SubtiposResolucion.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        Object paramItems = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("SubtiposResolucion_items");
        if (paramItems != null) {
            setItems((Collection<SubtiposResolucion>) paramItems);
            setLazyItems((Collection<SubtiposResolucion>) paramItems);
        }
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
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioAlta(ActionEvent event) {
        if (this.getSelected() != null && usuarioAltaController.getSelected() == null) {
            usuarioAltaController.setSelected(this.getSelected().getUsuarioAlta());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioUltimoEstado(ActionEvent event) {
        if (this.getSelected() != null && usuarioUltimoEstadoController.getSelected() == null) {
            usuarioUltimoEstadoController.setSelected(this.getSelected().getUsuarioUltimoEstado());
        }
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
    
    @Override
    public Collection<SubtiposResolucion> getItems() {
        this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return this.ejbFacade.getEntityManager().createNamedQuery("SubtiposResolucion.findAll", SubtiposResolucion.class).getResultList();
    }
}
