package py.com.startic.gestion.controllers;

import java.util.Collection;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.ControlarPermisos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "controlarPermisosController")
@ViewScoped
public class ControlarPermisosController extends AbstractController<ControlarPermisos> {

    @Inject
    private UsuariosController funcionariosController;
    @Inject
    private TiposPermisosController tiposPermisosController;

    public ControlarPermisosController() {
        // Inform the Abstract parent controller of the concrete ControlarPermisos Entity
        super(ControlarPermisos.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        Object paramItems = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("ControlarPermisos_items");
        if (paramItems != null) {
            setItems((Collection<ControlarPermisos>) paramItems);
            setLazyItems((Collection<ControlarPermisos>) paramItems);
        }
    }

    public void resetParents() {
        funcionariosController.setSelected(null);
        tiposPermisosController.setSelected(null);

    }

    public void prepareUsuarioAlta(ActionEvent event) {
        if (this.getSelected() != null && funcionariosController.getSelected() == null) {
            funcionariosController.setSelected(this.getSelected().getFuncionario());
        }
    }

    public void prepareUsuarioUltimoEstado(ActionEvent event) {
        if (this.getSelected() != null && tiposPermisosController.getSelected() == null) {
            tiposPermisosController.setSelected(this.getSelected().getTipoPermiso());
        }
    }

    @Override
    public Collection<ControlarPermisos> getItems() {
        this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return this.ejbFacade.getEntityManager().createNamedQuery("ControlarPermisos.findAll", ControlarPermisos.class).getResultList();
    }
}
