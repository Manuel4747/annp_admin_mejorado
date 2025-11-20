package py.com.startic.gestion.controllers;

import java.util.Collection;
import jakarta.annotation.PostConstruct;
import py.com.startic.gestion.models.TiposPrioridad;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "tiposPrioridadController")
@ViewScoped
public class TiposPrioridadController extends AbstractController<TiposPrioridad> {

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("TiposPrioridad.findAll", TiposPrioridad.class).getResultList());
    }

    public TiposPrioridadController() {
        // Inform the Abstract parent controller of the concrete DespachosPersona Entity
        super(TiposPrioridad.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<TiposPrioridad> getItems() {
        return ejbFacade.getEntityManager().createNamedQuery("TiposPrioridad.findAll", TiposPrioridad.class).getResultList();
    }

}
