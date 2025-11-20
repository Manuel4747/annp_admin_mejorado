package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Date;
import jakarta.annotation.PostConstruct;

import py.com.startic.gestion.models.ComponentesBienes;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.Bienes;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "componentesBienesController")
@ViewScoped
public class ComponentesBienesController extends AbstractController<ComponentesBienes> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresasController;
    @Inject
    private BienesController bienController;
    private Bienes bienOrigen;

    public Bienes getBienOrigen() {
        return bienOrigen;
    }

    public void setBienOrigen(Bienes bienOrigen) {
        this.bienOrigen = bienOrigen;
    }

    public ComponentesBienesController() {
        // Inform the Abstract parent controller of the concrete ComponentesBienes Entity
        super(ComponentesBienes.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresasController.setSelected(null);
        bienController.setSelected(null);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        bienOrigen = (Bienes) session.getAttribute("bien_origen");

        session.removeAttribute("bien_origen");

    }


    @Override
    public ComponentesBienes prepareCreate(ActionEvent event) {
        ComponentesBienes compBien = super.prepareCreate(event);

        if (bienOrigen != null) {
            compBien.setBien(bienOrigen);
        }
        
        return compBien;
    }
    
    /**
     * Sets the "items" attribute with a collection of MovimientosComponentes
     * entities that are retrieved from ComponentesBienes?cap_first and returns
     * the navigation outcome.
     *
     * @return navigation outcome for MovimientosComponentes page
     */
    public String navigateMovimientosComponentesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("componente_origen", getSelected());

            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosComponentes_items", ejbFacade.getEntityManager().createNamedQuery("MovimientosComponentes.findByComponente", ComponentesBienes.class).setParameter("componente", getSelected()).getResultList());
        }
        return "/pages/movimientosComponentes/index";
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
    public void prepareEmpresas(ActionEvent event) {
        if (this.getSelected() != null && empresasController.getSelected() == null) {
            empresasController.setSelected(this.getSelected().getEmpresa());
        }
    }

    /**
     * Sets the "selected" attribute of the Bienes controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareBien(ActionEvent event) {
        if (this.getSelected() != null && bienController.getSelected() == null) {
            bienController.setSelected(this.getSelected().getBien());
        }
    }
    
    @Override
    public Collection<ComponentesBienes> getItems() {
        return getItems2();
    }
    
    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            getSelected().setFechaHoraUltimoEstado(ejbFacade.getSystemDate());
            getSelected().setUsuarioUltimoEstado(usu);
        }

        super.save(event);
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

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usu);
            getSelected().setEmpresa(usu.getEmpresa());

            super.saveNew(event);
            
            setItems(ejbFacade.getEntityManager().createNamedQuery("ComponentesBienes.findByBien", ComponentesBienes.class).setParameter("bien", bienOrigen).getResultList());

        }
    }
    
    @Override
    public void delete(ActionEvent event) {
        super.delete(event);
        setItems(ejbFacade.getEntityManager().createNamedQuery("ComponentesBienes.findByBien", ComponentesBienes.class).setParameter("bien", bienOrigen).getResultList());
    }
}
