package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.UsuariosAsociados;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "usuariosAsociadosController")
@ViewScoped
public class UsuariosAsociadosController extends AbstractController<UsuariosAsociados> {

    public UsuariosAsociadosController() {
        // Inform the Abstract parent controller of the concrete FormPermisosPorRoles Entity
        super(UsuariosAsociados.class);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getUsuariosAsociadosPK().setUsuario(this.getSelected().getUsuario().getId());
        this.getSelected().getUsuariosAsociadosPK().setUsuarioAsociado(this.getSelected().getUsuarioAsociado().getId());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setUsuariosAsociadosPK(new py.com.startic.gestion.models.UsuariosAsociadosPK());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
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
            super.saveNew(event);
        }

    }
}
