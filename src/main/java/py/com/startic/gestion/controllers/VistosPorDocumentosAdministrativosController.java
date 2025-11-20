package py.com.startic.gestion.controllers;

import java.util.Collection;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import py.com.startic.gestion.models.VistosPorDocumentosAdministrativos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.Personas;

@Named(value = "vistosPorDocumentosAdministrativosController")
@ViewScoped
public class VistosPorDocumentosAdministrativosController extends AbstractController<VistosPorDocumentosAdministrativos> {

    private HttpSession session;
    private String sessionId;
    private String endpoint;

    public VistosPorDocumentosAdministrativosController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(VistosPorDocumentosAdministrativos.class);
    }

    @Override
    public Collection<VistosPorDocumentosAdministrativos> getItems() {
        return super.getItems2();
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessionId = session.getId();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);
        String[] array = uri.split("/");
        endpoint = array[1];
    }
}
