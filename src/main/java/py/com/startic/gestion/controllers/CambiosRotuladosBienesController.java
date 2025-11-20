package py.com.startic.gestion.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.CambiosRotuladosBienes;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.Bienes;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "cambiosRotuladosBienesController")
@ViewScoped
public class CambiosRotuladosBienesController extends AbstractController<CambiosRotuladosBienes> {

    @Inject
    private BienesController bienController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;

    private Bienes bienOrigen;

    private CambiosRotuladosBienes ultimoCambioRotuladoBien;

    public CambiosRotuladosBienesController() {
        // Inform the Abstract parent controller of the concrete CambiosRotuladosBienes Entity
        super(CambiosRotuladosBienes.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        bienOrigen = (Bienes) session.getAttribute("bien_origen");

        session.removeAttribute("bien_origen");
        /*
        if (getItems2() != null) {
            if (getItems2().iterator().hasNext()) {
                ultimoCambioRotuladoBien = getItems2().iterator().next();
            }
        }
         */
        try {
            ultimoCambioRotuladoBien = ejbFacade.getEntityManager().createNamedQuery("CambiosRotuladosBienes.findByIdMax", CambiosRotuladosBienes.class).setParameter("bien", bienOrigen).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            ultimoCambioRotuladoBien = null;
        }
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        bienController.setSelected(null);
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
    }

    @Override
    public CambiosRotuladosBienes prepareCreate(ActionEvent event) {
        CambiosRotuladosBienes rotBien = super.prepareCreate(event);

        if (bienOrigen != null) {
            rotBien.setBien(bienOrigen);
        }
        
        
        rotBien.setFechaDesde(ejbFacade.getSystemDate());

        return rotBien;
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
            getSelected().setFechaDesde(fecha);

            super.saveNew(event);

            bienOrigen.setRotulado(getSelected().getRotulado());

            bienController.setSelected(bienOrigen);

            bienController.save(event);

            if (ultimoCambioRotuladoBien != null) {
                ultimoCambioRotuladoBien.setFechaHasta(fecha);

                setSelected(ultimoCambioRotuladoBien);

                save(event);

            }

            setItems(ejbFacade.getEntityManager().createNamedQuery("CambiosRotuladosBienes.findByBien", CambiosRotuladosBienes.class).setParameter("bien", bienOrigen).getResultList());
            /*
            if (getItems2().iterator().hasNext()) {
                ultimoCambioRotuladoBien = getItems2().iterator().next();
            }
             */
            ultimoCambioRotuladoBien = getSelected();
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
            getSelected().setFechaDesde(fecha);

            super.saveNew(event);

        }
    }

    @Override
    public void delete(ActionEvent event) {
        super.delete(event);
        setItems(ejbFacade.getEntityManager().createNamedQuery("CambiosRotuladosBienes.findByBien", CambiosRotuladosBienes.class).setParameter("bien", bienOrigen).getResultList());
    }
}
