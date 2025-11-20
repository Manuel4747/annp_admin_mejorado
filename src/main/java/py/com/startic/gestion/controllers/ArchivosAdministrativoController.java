package py.com.startic.gestion.controllers;

import java.util.Collection;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.ArchivosAdministrativo;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "archivosAdministrativoController")
@ViewScoped
public class ArchivosAdministrativoController extends AbstractController<ArchivosAdministrativo> {

    public ArchivosAdministrativoController() {
        // Inform the Abstract parent controller of the concrete Archivos Entity
        super(ArchivosAdministrativo.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        Object paramItems = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("ArchivosAdministrativo_items");
        if (paramItems != null) {
            setItems((Collection<ArchivosAdministrativo>) paramItems);
            setLazyItems((Collection<ArchivosAdministrativo>) paramItems);
        }

    }
    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<ArchivosAdministrativo> getItems() {
        this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return this.ejbFacade.getEntityManager().createNamedQuery("ArchivosAdministrativo.findAll", ArchivosAdministrativo.class).getResultList();
    }
    
}
