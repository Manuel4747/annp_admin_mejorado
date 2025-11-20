package py.com.startic.gestion.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.MovimientosComponentes;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.ComponentesBienes;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "movimientosComponentesController")
@ViewScoped
public class MovimientosComponentesController extends AbstractController<MovimientosComponentes> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private MotivosMovimientosComponentesController motivoMovimientoComponenteController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController responsableOrigenController;
    @Inject
    private UsuariosController responsableDestinoController;
    @Inject
    private ComponentesBienesController componenteController;
    @Inject
    private DepartamentosController departamentoOrigenController;
    @Inject
    private DepartamentosController departamentoDestinoController;
    private ComponentesBienes componenteOrigen;
   

    public ComponentesBienes getComponenteOrigen() {
        return componenteOrigen;
    }

    public void setComponenteOrigen(ComponentesBienes componenteOrigen) {
        this.componenteOrigen = componenteOrigen;
    }

    public MovimientosComponentesController() {
        // Inform the Abstract parent controller of the concrete MovimientosComponentes Entity
        super(MovimientosComponentes.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        componenteOrigen = (ComponentesBienes) session.getAttribute("componente_origen");

        session.removeAttribute("componente_origen");

    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        usuarioAltaController.setSelected(null);
        motivoMovimientoComponenteController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresaController.setSelected(null);
        responsableOrigenController.setSelected(null);
        responsableDestinoController.setSelected(null);
        componenteController.setSelected(null);
        departamentoOrigenController.setSelected(null);
        departamentoDestinoController.setSelected(null);
    }

    @Override
    public MovimientosComponentes prepareCreate(ActionEvent event) {
        MovimientosComponentes movComp = super.prepareCreate(event);

        if (componenteOrigen != null) {
            movComp.setComponente(componenteOrigen);
            movComp.setResponsableOrigen(componenteOrigen.getResponsable());
            movComp.setDepartamentoOrigen(componenteOrigen.getDepartamento());
        }
        
        movComp.setFechaMovimiento(ejbFacade.getSystemDate());
        

        return movComp;

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
     * Sets the "selected" attribute of the MotivosMovimientosComponentes
     * controller in order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareMotivoMovimientoComponente(ActionEvent event) {
        if (this.getSelected() != null && motivoMovimientoComponenteController.getSelected() == null) {
            motivoMovimientoComponenteController.setSelected(this.getSelected().getMotivoMovimientoComponente());
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

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareResponsableOrigen(ActionEvent event) {
        if (this.getSelected() != null && responsableOrigenController.getSelected() == null) {
            responsableOrigenController.setSelected(this.getSelected().getResponsableOrigen());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareResponsableDestino(ActionEvent event) {
        if (this.getSelected() != null && responsableDestinoController.getSelected() == null) {
            responsableDestinoController.setSelected(this.getSelected().getResponsableDestino());
        }
    }

    /**
     * Sets the "selected" attribute of the ComponentesBienes controller in
     * order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareComponente(ActionEvent event) {
        if (this.getSelected() != null && componenteController.getSelected() == null) {
            componenteController.setSelected(this.getSelected().getComponente());
        }
    }

    /**
     * Sets the "selected" attribute of the Departamentos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDepartamentoOrigen(ActionEvent event) {
        if (this.getSelected() != null && departamentoOrigenController.getSelected() == null) {
            departamentoOrigenController.setSelected(this.getSelected().getDepartamentoOrigen());
        }
    }

    /**
     * Sets the "selected" attribute of the Departamentos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDepartamentoDestino(ActionEvent event) {
        if (this.getSelected() != null && departamentoDestinoController.getSelected() == null) {
            departamentoDestinoController.setSelected(this.getSelected().getDepartamentoDestino());
        }
    }    
        
    public String datePattern() {
        return "dd/MM/yyyy";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    @Override
    public Collection<MovimientosComponentes> getItems() {
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
            
            if(getSelected().getResponsableDestino() != null){
                getSelected().setDepartamentoDestino(getSelected().getResponsableDestino().getDepartamento());
            }

            super.saveNew(event);
            
            componenteOrigen.setResponsable(getSelected().getResponsableDestino());
            componenteOrigen.setDepartamento(getSelected().getDepartamentoDestino());
            
            componenteController.setSelected(componenteOrigen);
            
            componenteController.save(event);
            
            setItems(ejbFacade.getEntityManager().createNamedQuery("MovimientosComponentes.findByComponente", MovimientosComponentes.class).setParameter("componente", componenteOrigen).getResultList());
        }
    }
    
    @Override
    public void delete(ActionEvent event) {
        super.delete(event);
        setItems(ejbFacade.getEntityManager().createNamedQuery("MovimientosComponentes.findByComponente", MovimientosComponentes.class).setParameter("componente", componenteOrigen).getResultList());
    }
}
