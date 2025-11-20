package py.com.startic.gestion.controllers;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.RhMarcaciones;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.RhVMarcaciones;
import py.com.startic.gestion.models.RhVMarcacionesSuma;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "rhMarcacionesController")
@ViewScoped
public class RhMarcacionesController extends AbstractController<RhMarcaciones> {

    @Inject
    private RhModosVerificacionController modoVerificacionController;
    @Inject
    private RhRelojesController relojController;
    @Inject
    private EmpresasController empresaController;
    private Date fechaDesde;
    private Date fechaHasta;

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public RhMarcacionesController() {
        // Inform the Abstract parent controller of the concrete RhMarcaciones Entity
        super(RhMarcaciones.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        modoVerificacionController.setSelected(null);
        relojController.setSelected(null);
        empresaController.setSelected(null);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2018);
        myCal.set(Calendar.MONTH, 1);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        fechaDesde = myCal.getTime();
        fechaHasta = ejbFacade.getSystemDateOnly();

        buscarMarcaciones();
    }

    public void reporteCalculoRERA() {

        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar fecha desde y fecha hasta");
            return;
        }

        HttpServletResponse httpServletResponse = null;
        try {

            List<RhVMarcacionesSuma> marcaciones = ejbFacade.getEntityManager().createNamedQuery("RhVMarcacionesSuma.findByRangoFecha", RhVMarcacionesSuma.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList();

            HashMap map = new HashMap();

            map.put("fechaDesde", fechaDesde);
            map.put("fechaHasta", fechaHasta);

            JRBeanCollectionDataSource beanCollectionDataSource1 = new JRBeanCollectionDataSource(marcaciones);
            JRBeanCollectionDataSource beanCollectionDataSource2 = new JRBeanCollectionDataSource(marcaciones);

            map.put("datasource1", beanCollectionDataSource1);
            map.put("datasource2", beanCollectionDataSource2);

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteCalculoRARE.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource1);

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=reporte.xlsx");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
            exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.FALSE);
            exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            exporter.exportReport();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            //JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
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

    }

    public void buscarMarcaciones() {
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("RhMarcaciones.findByRangoFecha", RhMarcaciones.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList());

    }

    public void reporteRERA() {

        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar fecha desde y fecha hasta");
            return;
        }

        HttpServletResponse httpServletResponse = null;
        try {

            List<RhVMarcaciones> marcaciones = ejbFacade.getEntityManager().createNamedQuery("RhVMarcaciones.findByRangoFecha", RhVMarcaciones.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList();

            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(marcaciones);

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteRERAExcelSinForm.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, new HashMap(), beanCollectionDataSource);

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=reporte.xlsx");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            exporter.exportReport();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            //JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
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

    }

}
