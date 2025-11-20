package py.com.startic.gestion.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.MovimientosBienes;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.Bienes;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "movimientosBienesController")
@ViewScoped
public class MovimientosBienesController extends AbstractController<MovimientosBienes> {

    @Inject
    private BienesController bienController;
    @Inject
    private DepartamentosController departamentoOrigenController;
    @Inject
    private DepartamentosController departamentoDestinoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private MotivosMovimientosBienesController motivoMovimientoBienController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController responsableOrigenController;
    @Inject
    private UsuariosController responsableDestinoController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;

    private Bienes bienOrigen;

    public Bienes getBienOrigen() {
        return bienOrigen;
    }

    public void setBienOrigen(Bienes bienOrigen) {
        this.bienOrigen = bienOrigen;
    }

    public MovimientosBienesController() {
        // Inform the Abstract parent controller of the concrete MovimientosBienes Entity
        super(MovimientosBienes.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        bienOrigen = (Bienes) session.getAttribute("bien_origen");

        session.removeAttribute("bien_origen");

    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        bienController.setSelected(null);
        departamentoOrigenController.setSelected(null);
        departamentoDestinoController.setSelected(null);
        empresaController.setSelected(null);
        motivoMovimientoBienController.setSelected(null);
        usuarioAltaController.setSelected(null);
        responsableOrigenController.setSelected(null);
        responsableDestinoController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
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
     * Sets the "selected" attribute of the MotivosMovimientosBienes controller
     * in order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareMotivoMovimientoBien(ActionEvent event) {
        if (this.getSelected() != null && motivoMovimientoBienController.getSelected() == null) {
            motivoMovimientoBienController.setSelected(this.getSelected().getMotivoMovimientoBien());
        }
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

    @Override
    public MovimientosBienes prepareCreate(ActionEvent event) {
        MovimientosBienes movBien = super.prepareCreate(event);

        if (bienOrigen != null) {
            movBien.setBien(bienOrigen);
            movBien.setResponsableOrigen(bienOrigen.getResponsable());
            movBien.setDepartamentoOrigen(bienOrigen.getDepartamento());

        }

        movBien.setFechaMovimiento(ejbFacade.getSystemDate());

        return movBien;
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
    public Collection<MovimientosBienes> getItems() {
        return getItems2();
    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
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
            if (getSelected().getResponsableDestino() != null) {
                getSelected().setDepartamentoDestino(getSelected().getResponsableDestino().getDepartamento());
            }

            super.saveNew(event);

            bienOrigen.setResponsable(getSelected().getResponsableDestino());
            bienOrigen.setDepartamento(getSelected().getDepartamentoDestino());

            bienController.setSelected(bienOrigen);

            bienController.save(event);

            setItems(ejbFacade.getEntityManager().createNamedQuery("MovimientosBienes.findByBien", MovimientosBienes.class).setParameter("bien", bienOrigen).getResultList());

            // setSelected(ejbFacade.getEntityManager().createNamedQuery("ObservacionesBienes.findById", ObservacionesBienes.class).setParameter("id", getSelected().getId()).getSingleResult());
        }
    }

    public void saveNewOnly(ActionEvent event) {
        if (getSelected() != null) {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usu);
            getSelected().setEmpresa(usu.getEmpresa());
            if (getSelected().getResponsableDestino() != null) {
                getSelected().setDepartamentoDestino(getSelected().getResponsableDestino().getDepartamento());
            }

            super.saveNew(event);
        }
    }

    @Override
    public void delete(ActionEvent event) {
        super.delete(event);
        setItems(ejbFacade.getEntityManager().createNamedQuery("MovimientosBienes.findByBien", MovimientosBienes.class).setParameter("bien", bienOrigen).getResultList());
    }
}
