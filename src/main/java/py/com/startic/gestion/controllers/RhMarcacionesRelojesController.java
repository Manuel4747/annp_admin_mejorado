package py.com.startic.gestion.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.RhMarcacionesRelojes;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.datasource.DateItem;
import py.com.startic.gestion.datasource.RepMarcacionesRelojes;
import py.com.startic.gestion.datasource.RhResumenMarcacionesReloj;
import py.com.startic.gestion.models.RhMarcaciones;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "rhMarcacionesRelojesController")
@ViewScoped
public class RhMarcacionesRelojesController extends AbstractController<RhMarcacionesRelojes> {

    @Inject
    private RhModosVerificacionController modoVerificacionController;
    @Inject
    private RhRelojesController relojController;
    @Inject
    private EmpresasController empresaController;
    private Date fechaDesde;
    private Date fechaHasta;
    private List<RepMarcacionesRelojes> listaMarcaciones;
    private HttpSession session;
    private Usuarios usuario;
    private Usuarios usuarioSeleccionado;
    private final FiltroURL filtroURL = new FiltroURL();
    private Date fecha23;
    private String ultimaActualizacion;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private String totalHorasMinutos;
    private long tardias;
    private long tolerancias;
    private Date fechamarcacion;
    private String ci;

    private List<RhResumenMarcacionesReloj> resumenMarcacionesReloj;
    private Long totalCantidadRegistrosProcesados;

    public Long getTotalCantidadRegistrosProcesados() {
        return totalCantidadRegistrosProcesados;
    }

    public void setTotalCantidadRegistrosProcesados(Long totalCantidadRegistrosProcesados) {
        this.totalCantidadRegistrosProcesados = totalCantidadRegistrosProcesados;
    }

    public long getTardias() {
        return tardias;
    }

    public void setTardias(long tardias) {
        this.tardias = tardias;
    }

    public long getTolerancias() {
        return tolerancias;
    }

    public void setTolerancias(long tolerancias) {
        this.tolerancias = tolerancias;
    }

    public String getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(String ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public Usuarios getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuarios usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public List<RepMarcacionesRelojes> getListaMarcaciones() {
        return listaMarcaciones;
    }

    public void setListaMarcaciones(List<RepMarcacionesRelojes> listaMarcaciones) {
        this.listaMarcaciones = listaMarcaciones;
    }

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

    public String getTotalHorasMinutos() {
        return totalHorasMinutos;
    }

    public void setTotalHorasMinutos(String totalHorasMinutos) {
        this.totalHorasMinutos = totalHorasMinutos;
    }

    public RhMarcacionesRelojesController() {
        // Inform the Abstract parent controller of the concrete RhMarcacionesRelojes Entity
        super(RhMarcacionesRelojes.class);
    }

    public List<RhResumenMarcacionesReloj> getResumenMarcacionesReloj() {
        return resumenMarcacionesReloj;
    }

    public void setResumenMarcacionesReloj(List<RhResumenMarcacionesReloj> resumenMarcacionesReloj) {
        this.resumenMarcacionesReloj = resumenMarcacionesReloj;
    }

    public Date getFechamarcacion() {
        return fechamarcacion;
    }

    public void setFechamarcacion(Date fechamarcacion) {
        this.fechamarcacion = fechamarcacion;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
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
        myCal.set(Calendar.YEAR, 2022);
        myCal.set(Calendar.MONTH, 4);
        myCal.set(Calendar.DAY_OF_MONTH, 23);
        myCal.set(Calendar.HOUR_OF_DAY, 0);
        myCal.set(Calendar.MINUTE, 0);
        myCal.set(Calendar.SECOND, 0);
        myCal.set(Calendar.MILLISECOND, 0);
        fecha23 = myCal.getTime();

        fechaDesde = ejbFacade.getSystemDateOnly(-9);
        fechaHasta = ejbFacade.getSystemDateOnly();

        verifFecha();

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");
        if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_MARCACIONES_RELOJES)) {
            usuarioSeleccionado = new Usuarios(0);
        } else {
            usuarioSeleccionado = usuario;
        }

        buscarMarcaciones();
    }

    public boolean renderedUsuario() {
        return filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_MARCACIONES_RELOJES);
    }

    /*
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
     */
    public void verifFecha() {

        if (fecha23.compareTo(fechaDesde) > 0) {
            JsfUtil.addErrorMessage("Fecha desde no puede ser anterior al 23/05/2022.");
            fechaDesde = fecha23;
        }

        if (fecha23.compareTo(fechaHasta) > 0) {
            JsfUtil.addErrorMessage("Fecha hasta no puede ser anterior al 23/05/2022.");
            fechaHasta = fecha23;
        }
    }

    public void verifFechaResumen() {
        if (fechamarcacion == null) {
            fechamarcacion = fecha23;
        } else {
            if (fecha23.compareTo(fechamarcacion) > 0) {
                JsfUtil.addErrorMessage("Fecha no puede ser anterior al 23/05/2022.");
                fechamarcacion = fecha23;
            }
        }
    }

    public void buscarMarcaciones() {

        this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

        verifFecha();

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(fechaDesde);
        cal2.add(Calendar.DATE, -1);
        Date fechaDesdeNueva = cal2.getTime();
        listaMarcaciones = this.ejbFacade.getEntityManager().createNativeQuery("select concat(date_format(u.selected_date, '%y%m%d'), u.id) as id, u.selected_date as fecha, u.ci, u.nombres_apellidos, m.minimo, m.maximo from (select f.selected_date, a.id, a.ci, a.nombres_apellidos from usuarios as a, (select * from \n"
                + "(select adddate('1970-01-01',t4.i*10000 + t3.i*1000 + t2.i*100 + t1.i*10 + t0.i) selected_date from\n"
                + " (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0,\n"
                + " (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1,\n"
                + " (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2,\n"
                + " (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3,\n"
                + " (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) v\n"
                + "where selected_date >= ?1 and selected_date < ?2) as f) as u left join (select nro_empleado, fecha_autenticacion, min(fecha_hora_autenticacion) as minimo, case when max(fecha_hora_autenticacion) = min(fecha_hora_autenticacion) then null else max(fecha_hora_autenticacion) end as maximo from rh_marcaciones_relojes group by nro_empleado, fecha_autenticacion) as m on (u.ci = m.nro_empleado and u.selected_date = m.fecha_autenticacion)\n"
                + "where id = ?3\n"
                + "order by u.ci, u.nombres_apellidos, u.selected_date", RepMarcacionesRelojes.class).setParameter(3, usuarioSeleccionado.getId()).setParameter(1, fechaDesdeNueva).setParameter(2, fechaHasta).getResultList();

        calcularTotalHorasMinutos(listaMarcaciones);
        DateItem dateItem = (DateItem) ejbFacade.getEntityManager().createNativeQuery("select max(fecha_hora_autenticacion) AS DATE_VALUE from rh_marcaciones_relojes", DateItem.class).getSingleResult();
        ultimaActualizacion = format.format(dateItem.getDate());
    }

    public static int horaEntrada = 7;

    public Long difFechasMinutos(Date fecha, Date entrada) {
        if (fecha != null && entrada != null) {
            long segs = difFechasSegundos(fecha, entrada, true);
            long minu = segs / 60;
            return minu;
        } else {
            return 0L;
        }
    }

    public Long difFechasSegundos(Date fecha, Date entrada, boolean setearhoraentrada) {
        if (fecha != null && entrada != null) {
            Date fechHorEntrada;
            if (setearhoraentrada) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(fecha);
                cal.set(Calendar.HOUR_OF_DAY, horaEntrada);
                fechHorEntrada = cal.getTime();
            } else {
                fechHorEntrada = new Date(fecha.getTime());

            }
            long mili = entrada.getTime() - fechHorEntrada.getTime();
            long segs = mili / 1000;
            return segs;
        } else {
            return 0L;
        }
    }

    public String tardiaClass(Date fecha, Date entrada) {
        Long dif = difFechasMinutos(fecha, entrada);
        if (dif > 0 && dif <= 15) {
            return "font-orange";
        }
        if (dif > 0) {
            return "font-red";
        }
        return "normal";
    }

    public void verificarAdvertenciaTolerancia(Long tol) {
        if (tol != null) {
            if (tol.equals(Long.valueOf(Constantes.CANTIDAD_DIAS_TOLERANCIA))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "LLegó a 3 tolerancias"));
            }
        }
    }

    private void calcularTotalHorasMinutos(List<RepMarcacionesRelojes> listaMarcaciones) {
        Long horas = Long.valueOf(0);
        Long minutos = Long.valueOf(0);
        tardias = 0L;
        tolerancias = 0L;

        for (RepMarcacionesRelojes rel : listaMarcaciones) {
            Long min = difFechasMinutos(rel.getFecha(), rel.getMinimo());
            if (min > 0) {
                tardias = tardias + 1;
            }
            if (min > 0 && min <= 15) {
                tolerancias = tolerancias + 1;
            }
            horas = horas + rel.getHoras();
            minutos = minutos + rel.getMinutos();
        }
        verificarAdvertenciaTolerancia(tolerancias);
        /*
        Long hh = minutos /60;
        
        int hint = Math.round(hh);
        horas = horas + hint;
        minutos = minutos + hh - hint;
         */
        if (minutos >= 60) {
            horas = horas + (minutos / 60);
            minutos = minutos % 60;
        }
        this.totalHorasMinutos = horas + ":" + minutos;
    }

    public void consultarResumenMarcacionesReloj() {
        List<RhResumenMarcacionesReloj> res = rhMarcacionesRelojes(fechamarcacion, ci);
        setResumenMarcacionesReloj(res);
    }

    public List<RhResumenMarcacionesReloj> rhMarcacionesRelojes(Date pFecha, String ci) {
        List<RhMarcacionesRelojes> result = ejbFacade.getEntityManager()
                .createNamedQuery("RhMarcacionesRelojes.findByFechaYCi", RhMarcacionesRelojes.class)
                .setParameter("fecha", pFecha)
                .setParameter("ci", ci)
                .getResultList();

        RhMarcacionesRelojes marcacionAnterior = null;
        boolean first = true;

        Long segundosConsiderados = 60L;
        List<String> dispositivos = new ArrayList<>();

        List<RhResumenMarcacionesReloj> resumen = new ArrayList<>();
        Integer index = 0;
        Long contador = 0L;

        RhResumenMarcacionesReloj current = null;

        for (RhMarcacionesRelojes res : result) {

            if (first) {
                current = new RhResumenMarcacionesReloj();
                index++;
                current.setId(index);
                current.setMinimo(res.getFechaHoraAutenticacion());
                current.setMaximo(res.getFechaHoraAutenticacion());
                current.setFecha(res.getFechaAutenticacion());
                if (!dispositivos.contains(res.getNombreDispositivo())) {
                    dispositivos.add(res.getNombreDispositivo());
                }
                current.setTerminales(concatenarStringList(dispositivos));
                first = false;
                contador++;
                marcacionAnterior = res;
                continue;
            }
            Long difSegundos = 0L;
            if (marcacionAnterior != null) {
                Date fechaAnt = new Date(marcacionAnterior.getFechaHoraAutenticacion().getTime());
                Date fehcaAut = new Date(res.getFechaHoraAutenticacion().getTime());
                difSegundos = difFechasSegundos(fechaAnt, fehcaAut, false);
            } else {
                marcacionAnterior = res;
                continue;
            }
            if (segundosConsiderados.compareTo(difSegundos) > 0) {
                contador++;
                if (!dispositivos.contains(res.getNombreDispositivo())) {
                    dispositivos.add(res.getNombreDispositivo());
                }
                String terms = concatenarStringList(dispositivos);
                Integer elId = current.getId();
                System.out.println("El id:" + elId);
                current.setTerminales(terms);
                current.setMaximo(res.getFechaHoraAutenticacion());
                current.setDiferencia(difSegundos);
                current.setFecha(res.getFechaAutenticacion());
                //current.setCantidad(contador);
                current.setTranscurso("00:00:" + padLeftZeros(String.valueOf(difSegundos), 2));
            } else {
                current.setCantidad(contador);
                resumen.add(current);
                dispositivos = new ArrayList<>();
                index++;
                contador = 1L;
                current = new RhResumenMarcacionesReloj();
                current.setId(index);
                dispositivos.add(res.getNombreDispositivo());
                current.setMinimo(res.getFechaHoraAutenticacion());
                current.setMaximo(res.getFechaHoraAutenticacion());
                current.setTerminales(concatenarStringList(dispositivos));
                current.setFecha(res.getFechaAutenticacion());

                Date fechaAnt = new Date(marcacionAnterior.getFechaHoraAutenticacion().getTime());
                Date fehcaAut = new Date(res.getFechaHoraAutenticacion().getTime());
                difSegundos = difFechasSegundos(fechaAnt, fehcaAut, false);

                current.setDiferencia(difSegundos);
                current.setCantidad(contador);
                if (difSegundos >= 3600) {
                    Long minutos = difSegundos / 60;
                    Long segundos = difSegundos % 60;
                    Long horas = 0L;
                    if (minutos >= 60) {
                        horas = minutos / 60;
                        minutos = minutos % 60;
                    }
                    current.setTranscurso(
                            padLeftZeros(String.valueOf(horas), 2) + ":"
                            + padLeftZeros(String.valueOf(minutos), 2) + ":"
                            + padLeftZeros(String.valueOf(segundos), 2));
                } else {
                    if (difSegundos >= 60) {
                        Long minutos = difSegundos / 60;
                        Long segundos = difSegundos % 60;
                        Long horas = 0L;
                        current.setTranscurso(
                                padLeftZeros(String.valueOf(horas), 2) + ":"
                                + padLeftZeros(String.valueOf(minutos), 2) + ":"
                                + padLeftZeros(String.valueOf(segundos), 2));
                    } else {
                        current.setTranscurso("00:00:" + padLeftZeros(String.valueOf(difSegundos), 2));
                    }
                }
            }
            marcacionAnterior = res;
        }
        if (current != null) {
            current.setCantidad(contador);
            resumen.add(current);
        }
        totalCantidadRegistrosProcesados = 0L;
        for (RhResumenMarcacionesReloj rr : resumen) {
            totalCantidadRegistrosProcesados = totalCantidadRegistrosProcesados + rr.getCantidad();
        }
        return resumen;
    }

    public String concatenarStringList(List<String> array) {
        String cad = "";
        if (array != null && !array.isEmpty()) {
            for (String aa : array) {
                if (cad.equals("")) {
                    cad = aa;
                } else {
                    cad = cad + ", " + aa;
                }
            }
        }
        return cad;
    }

    public String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }

    public void pdf() {

        HttpServletResponse httpServletResponse = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");

            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaMarcaciones);
            HashMap map = new HashMap();

            Date fecha = ejbFacade.getSystemDate();
            map.put("nombre", usuarioSeleccionado.getNombresApellidos());
            map.put("usuario", usuario.getNombresApellidos());
            map.put("fecha", format.format(fecha));
            map.put("hora", format2.format(fecha));
            map.put("fechaDesde", format.format(fechaDesde));
            map.put("fechaHasta", format.format(fechaHasta));
            map.put("totalHorasMinutos", totalHorasMinutos);

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteMarcacionesRelojes.jasper");
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
    /*
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
     */
}
