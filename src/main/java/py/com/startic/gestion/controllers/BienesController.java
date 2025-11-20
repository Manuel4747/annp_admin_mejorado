package py.com.startic.gestion.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import py.com.startic.gestion.models.Bienes;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.CambiosRotuladosBienes;
import py.com.startic.gestion.models.ComponentesBienes;
import py.com.startic.gestion.models.MotivosMovimientosBienes;
import py.com.startic.gestion.models.MovimientosBienes;
import py.com.startic.gestion.models.ObservacionesBienes;
import py.com.startic.gestion.models.RepBienes;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "bienesController")
@ViewScoped
public class BienesController extends AbstractController<Bienes> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private EstadosBienesController estadoController;
    @Inject
    private ProveedoresController proveedorController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private UsuariosController responsableController;
    @Inject
    private CambiosRotuladosBienesController cambiosRotuladosBienesController;
    @Inject
    private MovimientosBienesController movimientosBienesController;

    public BienesController() {
        // Inform the Abstract parent controller of the concrete Bienes Entity
        super(Bienes.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        estadoController.setSelected(null);
        proveedorController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        responsableController.setSelected(null);
    }

    @Override
    public Bienes prepareCreate(ActionEvent event) {
        Bienes bien = super.prepareCreate(event);
        bien.setFechaAdquisicion(ejbFacade.getSystemDate());

        return bien;
    }

    /**
     * Sets the "items" attribute with a collection of FotosBienes entities that
     * are retrieved from Bienes?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for FotosBienes page
     */
    public String navigateFotosBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FotosBienes_items", this.getSelected().getFotosBienesCollection());
        }
        return "/pages/fotosBienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of ComponentesBienes
     * entities that are retrieved from Bienes?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for CambiosRotuladosBienes page
     */
    public String navigateComponentesBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bien_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ComponentesBienes_items", ejbFacade.getEntityManager().createNamedQuery("ComponentesBienes.findByBien", ComponentesBienes.class).setParameter("bien", getSelected()).getResultList());
        }
        return "/pages/componentesBienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of CambiosRotuladosBienes
     * entities that are retrieved from Bienes?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for CambiosRotuladosBienes page
     */
    public String navigateCambiosRotuladosBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bien_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("CambiosRotuladosBienes_items", ejbFacade.getEntityManager().createNamedQuery("CambiosRotuladosBienes.findByBien", CambiosRotuladosBienes.class).setParameter("bien", getSelected()).getResultList());
        }
        return "/pages/cambiosRotuladosBienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of
     * MovimientosReparacionBienes entities that are retrieved from
     * Bienes?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for MovimientosReparacionBienes page
     */
    public String navigateMovimientosReparacionBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosReparacionBienes_items", this.getSelected().getMovimientosReparacionBienesCollection());
        }
        return "/pages/movimientosReparacionBienes/index";
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
     * Sets the "selected" attribute of the EstadosBienes controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEstado(ActionEvent event) {
        if (this.getSelected() != null && estadoController.getSelected() == null) {
            estadoController.setSelected(this.getSelected().getEstado());
        }
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
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareResponsable(ActionEvent event) {
        if (this.getSelected() != null && responsableController.getSelected() == null) {
            responsableController.setSelected(this.getSelected().getResponsable());
        }
    }

    /**
     * Sets the "items" attribute with a collection of ObservacionesBienes
     * entities that are retrieved from Bienes?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for ObservacionesBienes page
     */
    public String navigateObservacionesBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bien_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesBienes_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesBienes.findByBien", Bienes.class).setParameter("bien", getSelected()).getResultList());
        }
        return "/pages/observacionesBienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of Subcuentas entities that
     * are retrieved from Cuentas?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Subcuentas page
     */
    public String navigateSubcuentasCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bien_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Bienes_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesBienes.findByBien", ObservacionesBienes.class).setParameter("bien", getSelected()).getResultList());
        }
        return "/pages/subcuentas/index";
    }

    /**
     * Sets the "items" attribute with a collection of MovimientosBienes
     * entities that are retrieved from Bienes?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for MovimientosBienes page
     */
    public String navigateMovimientosBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bien_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosBienes_items", ejbFacade.getEntityManager().createNamedQuery("MovimientosBienes.findByBien", Bienes.class).setParameter("bien", getSelected()).getResultList());
        }
        return "/pages/movimientosBienes/index";
    }

    @Override
    public Collection<Bienes> getItems() {
        return this.ejbFacade.getEntityManager().createNamedQuery("Bienes.findOrdered", Bienes.class).getResultList();
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
            if (getSelected().getResponsable() != null) {
                getSelected().setDepartamento(getSelected().getResponsable().getDepartamento());
            }

            super.saveNew(event);
            
            CambiosRotuladosBienes rot = cambiosRotuladosBienesController.prepareCreate(event);
            
            rot.setBien(getSelected());
            
            rot.setFechaDesde(fecha);
            rot.setRotulado(getSelected().getRotulado());
            
            rot.setFechaHoraUltimoEstado(fecha);
            rot.setUsuarioUltimoEstado(usu);
            rot.setFechaHoraAlta(fecha);
            rot.setUsuarioAlta(usu);
            rot.setEmpresa(usu.getEmpresa());
            
            cambiosRotuladosBienesController.setSelected(rot);
            cambiosRotuladosBienesController.saveNewOnly(event);

            if (getSelected().getResponsable() != null) {
                MovimientosBienes mov = movimientosBienesController.prepareCreate(event);

                mov.setBien(getSelected());
                mov.setDepartamentoDestino(getSelected().getResponsable().getDepartamento());
                mov.setResponsableDestino(getSelected().getResponsable());

                mov.setFechaHoraUltimoEstado(fecha);
                mov.setUsuarioUltimoEstado(usu);
                mov.setFechaHoraAlta(fecha);
                mov.setUsuarioAlta(usu);
                mov.setEmpresa(usu.getEmpresa());
                
                mov.setMotivoMovimientoBien(ejbFacade.getEntityManager().createNamedQuery("MotivosMovimientosBienes.findByCodigo", MotivosMovimientosBienes.class).setParameter("codigo", "FA").getSingleResult());
                
                movimientosBienesController.setSelected(mov);
                movimientosBienesController.saveNewOnly(event);
            }

            // setSelected(ejbFacade.getEntityManager().createNamedQuery("Bienes.findById", Bienes.class).setParameter("id", getSelected().getId()).getSingleResult());
        }

    }

    public void saveBaja() {
        if (getSelected() != null) {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setFechaBaja(fecha);
            getSelected().setUsuarioBaja(usu);

            super.save(null);

            // setSelected(ejbFacade.getEntityManager().createNamedQuery("Bienes.findById", Bienes.class).setParameter("id", getSelected().getId()).getSingleResult());
        }

    }

    public void pdf() {

        HttpServletResponse httpServletResponse = null;
        try {
            Collection<Bienes> terminales = this.ejbFacade.getEntityManager().createNamedQuery("Bienes.findReporte", Bienes.class).getResultList();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("hh:mm:ss");

            Collection<RepBienes> repBienes = new ArrayList<>();
            RepBienes repBien = null;
            for (Bienes bien : terminales) {
                repBien = new RepBienes();
                repBien.setCostoUnitario(bien.getCostoUnitario());
                repBien.setCuenta(bien.getEspecificacionSubcuenta().getSubcuenta().getCuenta().getCodigo() + " " + bien.getEspecificacionSubcuenta().getSubcuenta().getCuenta().getDescripcion());
                repBien.setSubcuenta(bien.getEspecificacionSubcuenta().getSubcuenta().getCodigo() + " " + bien.getEspecificacionSubcuenta().getSubcuenta().getDescripcion());
                repBien.setFechaAdquisicion(format.format(bien.getFechaAdquisicion()));
                repBien.setObservacion(bien.getUltimaObservacionBien());
                repBien.setRotulado(bien.getRotulado());
                repBien.setDescripcion(bien.getDescripcion());
                repBien.setOrigen(bien.getOrigenBien().getDescripcion());
                repBien.setVidaUtil(String.valueOf(bien.getEspecificacionSubcuenta().getSubcuenta().getCuenta().getVidaUtil()));

                repBienes.add(repBien);
            }

            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(repBienes);
            HashMap map = new HashMap();

            Date fecha = ejbFacade.getSystemDate();
            map.put("fecha", format.format(fecha));
            map.put("hora", format.format(fecha));

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteBienes.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

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
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");

        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

    public void xls() {

        HttpServletResponse httpServletResponse = null;
        try {
            Collection<Bienes> terminales = this.ejbFacade.getEntityManager().createNamedQuery("Bienes.findReporte", Bienes.class).getResultList();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("hh:mm:ss");

            Collection<RepBienes> repBienes = new ArrayList<>();
            RepBienes repBien = null;
            for (Bienes bien : terminales) {
                repBien = new RepBienes();
                repBien.setCostoUnitario(bien.getCostoUnitario());
                repBien.setCuenta(bien.getEspecificacionSubcuenta().getSubcuenta().getCuenta().getCodigo() + " " + bien.getEspecificacionSubcuenta().getSubcuenta().getCuenta().getDescripcion());
                repBien.setSubcuenta(bien.getEspecificacionSubcuenta().getSubcuenta().getCodigo() + " " + bien.getEspecificacionSubcuenta().getSubcuenta().getDescripcion());
                repBien.setFechaAdquisicion(format.format(bien.getFechaAdquisicion()));
                repBien.setObservacion(bien.getUltimaObservacionBien());
                repBien.setRotulado(bien.getRotulado());
                repBien.setOrigen(bien.getOrigenBien().getDescripcion());
                repBien.setVidaUtil(String.valueOf(bien.getEspecificacionSubcuenta().getSubcuenta().getCuenta().getVidaUtil()));

                repBienes.add(repBien);
            }

            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(repBienes);
            HashMap map = new HashMap();

            Date fecha = ejbFacade.getSystemDate();
            map.put("fecha", format.format(fecha));
            map.put("hora", format.format(fecha));

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteBienes.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=reporte.xlsx");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            //JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            exporter.exportReport();

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");

        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

}
