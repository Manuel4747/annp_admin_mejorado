package py.com.startic.gestion.controllers;


import java.util.Collection;
import py.com.startic.gestion.models.TiposPermisos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "tiposPermisosController")
@ViewScoped
public class TiposPermisosController extends AbstractController<TiposPermisos> {

    public TiposPermisosController() {
        // Inform the Abstract parent controller of the concrete TiposPermisos Entity
        super(TiposPermisos.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<TiposPermisos> getItems() {
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("TiposPermisos.findAll", TiposPermisos.class).getResultList());
        return super.getItems2();
    }


}
