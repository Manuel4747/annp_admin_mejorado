package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.DetallesEntradaArticulo;
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

@Named(value = "detallesEntradaArticuloController")
@ViewScoped
public class DetallesEntradaArticuloController extends AbstractController<DetallesEntradaArticulo> {

    @Inject
    private ProveedoresController proveedorController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private EntradasArticuloController entradaArticuloController;
    @Inject
    private ArticulosController articuloController;
    private Collection<DetallesEntradaArticulo> filtrado;

    public DetallesEntradaArticuloController() {
        // Inform the Abstract parent controller of the concrete DetallesEntradaArticulo Entity
        super(DetallesEntradaArticulo.class);
    }
 public Collection<DetallesEntradaArticulo> getFiltrado() {
        return filtrado;
    }

    public void setFiltrado(Collection<DetallesEntradaArticulo> filtrado) {
        this.filtrado = filtrado;
    }
    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        proveedorController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresaController.setSelected(null);
        entradaArticuloController.setSelected(null);
        articuloController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Proveedores controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareProveedor(ActionEvent event) {
        if (this.getSelected() != null && proveedorController.getSelected() == null) {
            proveedorController.setSelected(this.getSelected().getProveedor());
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
     public void prepareArticulos(ActionEvent event) {
        if (this.getSelected() != null && articuloController.getSelected() == null) {
            articuloController.setSelected(this.getSelected().getArticulo());
        }
    }

    /**
     * Sets the "selected" attribute of the EntradasArticulo controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEntradaArticulo(ActionEvent event) {
        if (this.getSelected() != null && entradaArticuloController.getSelected() == null) {
            entradaArticuloController.setSelected(this.getSelected().getEntradaArticulo());
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
     
    
    public void pdf() {

        HttpServletResponse httpServletResponse = null;
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = null;
            if (filtrado != null) {
                beanCollectionDataSource = new JRBeanCollectionDataSource(filtrado);
            } else {
                beanCollectionDataSource = new JRBeanCollectionDataSource(getItems());
            }
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteEntradaArticulo.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, new HashMap(), beanCollectionDataSource);
            
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=reporte.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
             
        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                }else{
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");

        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }


}










