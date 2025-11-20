package py.com.startic.gestion.controllers;

import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Acciones;
import py.com.startic.gestion.models.CambiosValor;
import py.com.startic.gestion.models.DetallesPlanEstrategicas;
import py.com.startic.gestion.models.PlanEstrategicas;
import py.com.startic.gestion.models.Programacion;
import py.com.startic.gestion.models.TiposObjetivos;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "programacionController")
@ViewScoped
public class ProgramacionController extends AbstractController<Programacion> {

    @Inject
    private CambiosValorController cambiosValorController;
    @Inject
    private ProgramacionController programacionController;
    private List<Acciones> listaAcciones;
    private Acciones accion;
    private TiposObjetivos tiposObjetivos;
    private HttpSession session;
    private Usuarios usuario;
    private String menu;
    private String pantalla;
    private String updateCerrar;
    private Programacion programacionSelected;
    private DetallesPlanEstrategicas detallesPlan;
    private double nuevaVariable;
    private double valorVariable;

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");

        menu = Constantes.NO;
        setItems(ejbFacade.getEntityManager().createNamedQuery("Programacion.findOrdered", Programacion.class).getResultList());
        if (!getItems2().isEmpty()) {
            Programacion art = getItems2().iterator().next();
            setSelected(art);

        } else {
            setSelected(null);
        }

    }

    public Programacion prepareCreate() {
        Programacion prog = super.prepareCreate(null);
        tiposObjetivos = null;
        return prog;
    }

    public Programacion prepareEdit(ActionEvent event) {
        programacionSelected = null;
        return programacionSelected;
    }

    public List<Acciones> getListaAcciones() {
        return listaAcciones;
    }

    public void setListaAcciones(List<Acciones> listaAcciones) {
        this.listaAcciones = listaAcciones;
    }

    public Acciones getAccion() {
        return accion;
    }

    public void setAccion(Acciones accion) {
        this.accion = accion;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getPantalla() {
        return pantalla;
    }

    public void setPantalla(String pantalla) {
        this.pantalla = pantalla;
    }

    public double getNuevaVariable() {
        return nuevaVariable;
    }

    public void setNuevaVariable(double nuevaVariable) {
        this.nuevaVariable = nuevaVariable;
    }

    public double getValorVariable() {
        return valorVariable;
    }

    public void setValorVariable(double valorVariable) {
        this.valorVariable = valorVariable;
    }
    

    public Programacion getProgramacionSelected() {
        return programacionSelected;
    }

    public void setProgramacionSelected(Programacion programacionSelected) {
        this.programacionSelected = programacionSelected;
    }

    public TiposObjetivos getTiposObjetivos() {
        if (session.getAttribute("tipoSelected") != null) {
            tiposObjetivos = (TiposObjetivos) session.getAttribute("tipoSelected");
            session.removeAttribute("tipoSelected");
        }
        return tiposObjetivos;
    }

    public void setTiposObjetivos(TiposObjetivos tiposObjetivos) {
        this.tiposObjetivos = tiposObjetivos;
    }

    public DetallesPlanEstrategicas getDetallesPlan() {
        return detallesPlan;
    }

    public void setDetallesPlan(DetallesPlanEstrategicas detallesPlan) {
        this.detallesPlan = detallesPlan;
    }
    

    public ProgramacionController() {
        // Inform the Abstract parent controller of the concrete FormPermisosPorRoles Entity
        super(Programacion.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        if (Constantes.SI.equals(pantalla)) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("programacionSelected", programacionSelected);
        }
    }

    public String abrirCreate(String desde, String updateCerrar) {
        menu = Constantes.NO;
        pantalla = Constantes.NO;
        if (Constantes.ABIERTO_DESDE_MENU.equals(desde)) {
            menu = Constantes.SI;
        } else if (Constantes.ABIERTO_DESDE_PANTALLA.equals(desde)) {
            pantalla = Constantes.SI;
            this.updateCerrar = updateCerrar;
        }

        prepareEdit(null);

        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('ProgramacionEditDialog').show();");
        return "";
    }

    public String updateCerrar() {
        return updateCerrar;
    }

   
    public void guardarEditValor() {

        if (programacionSelected != null) {

            if (programacionSelected.getValorVariable() != nuevaVariable) {
                CambiosValor cambio = cambiosValorController.prepareCreate(null);

                cambio.setCantidadOriginal(programacionSelected.getValorVariable());
                cambio.setCantidadFinal(nuevaVariable);
                cambio.setProgramacion(programacionSelected);
    

                programacionSelected.setValorVariable(nuevaVariable);
                programacionController.setSelected(programacionSelected);
                programacionController.save(null);
                
                cambiosValorController.setSelected(cambio);
                cambiosValorController.saveNew(null);
                // resetParents();
            } else {
                JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
            }
        }
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
            getSelected().setTiposObjetivos(tiposObjetivos);
            super.saveNew(event);
        }

    }

    private List<Acciones> obtenerListaTiposObjetivos(TiposObjetivos tiposObjetivos) {
        if (tiposObjetivos != null) {
            return this.ejbFacade.getEntityManager().createNamedQuery("Acciones.findByTiposObjetivos", Acciones.class
            ).setParameter("tiposObjetivos", tiposObjetivos).getResultList();
        }
        return null;
    }

    public void actualizarListas(TiposObjetivos tiposObjetivos) {
        listaAcciones = obtenerListaTiposObjetivos(tiposObjetivos);
    }

    public void resetearListas(TiposObjetivos tiposObjetivos) {
        accion = null;

        if (getSelected() != null) {
            getSelected().setAccion(null);

        }
        actualizarListas(tiposObjetivos);
    }
}
