package py.com.startic.gestion.controllers;

import java.util.Collection;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.AgDepartamentosPorAgendamientos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "departamentosPorAgendamientosController")
@ViewScoped
public class AgDepartamentosPorAgendamientosController extends AbstractController<AgDepartamentosPorAgendamientos> {

    public AgDepartamentosPorAgendamientosController() {
        // Inform the Abstract parent controller of the concrete Archivos Entity
        super(AgDepartamentosPorAgendamientos.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        Object paramItems = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("AgDepartamentosPorAgendamientos_items");
        if (paramItems != null) {
            setItems((Collection<AgDepartamentosPorAgendamientos>) paramItems);
            setLazyItems((Collection<AgDepartamentosPorAgendamientos>) paramItems);
        }

    }
    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<AgDepartamentosPorAgendamientos> getItems() {
        this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return this.ejbFacade.getEntityManager().createNamedQuery("AgDepartamentosPorAgendamientos.findAll", AgDepartamentosPorAgendamientos.class).getResultList();
    }
    
}
