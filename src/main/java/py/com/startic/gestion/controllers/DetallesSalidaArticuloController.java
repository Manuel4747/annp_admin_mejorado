package py.com.startic.gestion.controllers;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.DetallesSalidaArticulo;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "detallesSalidaArticuloController")
@ViewScoped
public class DetallesSalidaArticuloController extends AbstractController<DetallesSalidaArticulo> {

    @Inject
    private UsuariosController personaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private ArticulosController articuloController;
    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private SalidasArticuloController salidaArticuloController;
   private Collection<DetallesSalidaArticulo> filtrado;

    public DetallesSalidaArticuloController() {
        // Inform the Abstract parent controller of the concrete DetallesSalidaArticulo Entity
        super(DetallesSalidaArticulo.class);
    }
    
    public Collection<DetallesSalidaArticulo> getFiltrado() {
        return filtrado;
    }

    public void setFiltrado(Collection<DetallesSalidaArticulo> filtrado) {
        this.filtrado = filtrado;
    }
    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        personaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        articuloController.setSelected(null);
        departamentoController.setSelected(null);
        empresaController.setSelected(null);
        salidaArticuloController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void preparePersona(ActionEvent event) {
        if (this.getSelected() != null && personaController.getSelected() == null) {
            personaController.setSelected(this.getSelected().getPersona());
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

    /**
     * Sets the "selected" attribute of the Articulos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareArticulo(ActionEvent event) {
        if (this.getSelected() != null && articuloController.getSelected() == null) {
            articuloController.setSelected(this.getSelected().getArticulo());
        }
    }

    /**
     * Sets the "selected" attribute of the Departamentos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDepartamento(ActionEvent event) {
        if (this.getSelected() != null && departamentoController.getSelected() == null) {
            departamentoController.setSelected(this.getSelected().getDepartamento());
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
     * Sets the "selected" attribute of the SalidasArticulo controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSalidaArticulo(ActionEvent event) {
        if (this.getSelected() != null && salidaArticuloController.getSelected() == null) {
            salidaArticuloController.setSelected(this.getSelected().getSalidaArticulo());
        }
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

                    super.saveNew(event);

        }

    }
}





