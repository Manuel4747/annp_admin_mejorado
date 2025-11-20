package py.com.startic.gestion.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import jakarta.annotation.PostConstruct;

import py.com.startic.gestion.models.SalidasArticulo;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.datasource.RepMemorandum;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.TiposDocumentosAdministrativos;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "memorandumController")
@ViewScoped
public class MemorandumController extends AbstractController<SalidasArticulo> {

    private Date fechaDesdeSalida;
    private Date fechaHastaSalida;

    
    private TiposDocumentosAdministrativos tipoDocumentoFiltro;
    private List<TiposDocumentosAdministrativos> listatipoDocumentoFiltro;
    private Departamentos origenFiltro;
    private List<Departamentos> listaorigenFiltro;
    private List<Departamentos> listaOrigen;
    private Departamentos destinoFiltro;
    private List<Departamentos> listaDestino;
    private List<Departamentos> listaDestinoFiltro;

    private ParametrosSistema par;
    private final FiltroURL filtroURL = new FiltroURL();
    private HttpSession session;
    private Usuarios usuario;

    public List<Departamentos> getListaOrigen() {
        return listaOrigen;
    }

    public void setListaOrigen(List<Departamentos> listaOrigen) {
        this.listaOrigen = listaOrigen;
    }

    public List<Departamentos> getListaDestino() {
        return listaDestino;
    }

    public void setListaDestino(List<Departamentos> listaDestino) {
        this.listaDestino = listaDestino;
    }

    public TiposDocumentosAdministrativos getTipoDocumentoFiltro() {
        return tipoDocumentoFiltro;
    }

    public void setTipoDocumentoFiltro(TiposDocumentosAdministrativos tipoDocumentoFiltro) {
        this.tipoDocumentoFiltro = tipoDocumentoFiltro;
    }

    public List<TiposDocumentosAdministrativos> getListatipoDocumentoFiltro() {
        return listatipoDocumentoFiltro;
    }

    public void setListatipoDocumentoFiltro(List<TiposDocumentosAdministrativos> listatipoDocumentoFiltro) {
        this.listatipoDocumentoFiltro = listatipoDocumentoFiltro;
    }

    public Departamentos getOrigenFiltro() {
        return origenFiltro;
    }

    public void setOrigenFiltro(Departamentos origenFiltro) {
        this.origenFiltro = origenFiltro;
    }

    public List<Departamentos> getListaorigenFiltro() {
        return listaorigenFiltro;
    }

    public void setListaorigenFiltro(List<Departamentos> listaorigenFiltro) {
        this.listaorigenFiltro = listaorigenFiltro;
    }

    public Departamentos getDestinoFiltro() {
        return destinoFiltro;
    }

    public void setDestinoFiltro(Departamentos destinoFiltro) {
        this.destinoFiltro = destinoFiltro;
    }

    public List<Departamentos> getListaDestinoFiltro() {
        return listaDestinoFiltro;
    }

    public void setListaDestinoFiltro(List<Departamentos> listaDestinoFiltro) {
        this.listaDestinoFiltro = listaDestinoFiltro;
    }

    @PostConstruct
    @Override
    public void initParams() {
        fechaDesdeSalida = ejbFacade.getSystemDateOnly(-30);
        fechaHastaSalida = ejbFacade.getSystemDateOnly();
        
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usuario = (Usuarios) session.getAttribute("Usuarios");
        
        String comando = "select * from departamentos where id in (select departamento_anterior from documentos_administrativos)";
        listaOrigen = ejbFacade.getEntityManager().createNativeQuery(comando, Departamentos.class).getResultList();
        
        
        comando = "select * from departamentos where id in (select departamento from documentos_administrativos)";
        listaDestino = ejbFacade.getEntityManager().createNativeQuery(comando, Departamentos.class).getResultList();
        
        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();

        super.initParams();
    }

    public MemorandumController() {
        // Inform the Abstract parent controller of the concrete SalidasArticulo Entity
        super(SalidasArticulo.class);
    }

    public Date getFechaDesdeSalida() {
        return fechaDesdeSalida;
    }

    public void setFechaDesdeSalida(Date fechaDesdeSalida) {
        this.fechaDesdeSalida = fechaDesdeSalida;
    }

    public Date getFechaHastaSalida() {
        return fechaHastaSalida;
    }

    public void setFechaHastaSalida(Date fechaHastaSalida) {
        this.fechaHastaSalida = fechaHastaSalida;
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<SalidasArticulo> getItems() {
        return super.getItems2();
    }

    public String datePattern() {
        return "dd/MM/yyyy HH:mm:ss";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    public String datePattern2() {
        return "dd/MM/yyyy";
    }

    public String customFormatDate2(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern2());
            return format.format(date);
        }
        return "";
    }
    
    public boolean deshabilitarOrigen(){
        return !(filtroURL.verifPermiso("adminMemorandums") || (!filtroURL.verifPermiso("adminMemorandums") && (listaDestinoFiltro==null?true:listaDestinoFiltro.isEmpty())));
    }
    
    public boolean deshabilitarDestino(){
        return !(filtroURL.verifPermiso("adminMemorandums") || (!filtroURL.verifPermiso("adminMemorandums") && (listaorigenFiltro==null?true:listaorigenFiltro.isEmpty())));
    }
    
    public void actualizarDependencias(){
        
        if(!filtroURL.verifPermiso("adminMemorandums")){
            if(listaorigenFiltro != null){
                if(!listaorigenFiltro.isEmpty()){
                    listaDestinoFiltro = new ArrayList<>();
                }
            }
            
            
            
            if(listaDestinoFiltro != null){
                if(!listaDestinoFiltro.isEmpty()){
                    listaorigenFiltro = new ArrayList<>();
                }
            }
        }

    }
    
    public void verDoc(String nombreArchivo, String extension) {

        HttpServletResponse httpServletResponse = null;
        try {
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if("pdf".equals(extension)){
                httpServletResponse.setContentType("application/pdf");
                httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");
            }else if("xlsx".equals(extension)){
                httpServletResponse.addHeader("Content-disposition", "filename=documento.xlsx");
            }else{
                JsfUtil.addErrorMessage("Extension de reporte no reconocida");
                return;
            }
            
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

            byte[] fileByte = null;

            try {
                fileByte = Files.readAllBytes(new File(par.getRutaSolicitudes() + "/" + nombreArchivo + "." + extension).toPath());
            } catch (IOException ex) {
                JsfUtil.addErrorMessage("No tiene documento adjunto");
                return;
            }

            servletOutputStream.write(fileByte);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.verdoc", "true", Collections.<String, Object>emptyMap());
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

    public void reporteMemorandum() {

        HttpServletResponse httpServletResponse = null;
        boolean origenAgregado = false;
        boolean destinoAgregado = false;
        try {
            
            if(fechaDesdeSalida == null || fechaHastaSalida == null){
                JsfUtil.addErrorMessage("Debe elegir fecha desde y fecha hasta");
                return;
            }
            
            if((listatipoDocumentoFiltro == null?true:listatipoDocumentoFiltro.isEmpty()) && (listaorigenFiltro == null?true:listaorigenFiltro.isEmpty()) && (listaDestinoFiltro == null?true:listaDestinoFiltro.isEmpty())){
                JsfUtil.addErrorMessage("Debe seleccionar al menos uno de los criterios ademas del rango de fecha");
                return;
            }
            
            if(!filtroURL.verifPermiso("adminMemorandums")){
                if(listaorigenFiltro == null){
                    origenAgregado = true;
                    listaorigenFiltro = new ArrayList<>();
                }
                
                if(listaorigenFiltro.isEmpty()){
                    origenAgregado = true;
                    listaorigenFiltro.add(usuario.getDepartamento());
                }
                
                if(listaDestinoFiltro == null){
                    destinoAgregado = true;
                    listaDestinoFiltro = new ArrayList<>();
                }
                
                if(listaDestinoFiltro.isEmpty()){
                    destinoAgregado = true;
                    listaDestinoFiltro.add(usuario.getDepartamento());
                }
                
            }
            
            JRBeanCollectionDataSource beanCollectionDataSource = null;

            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

            Calendar cal = Calendar.getInstance();

            SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");

            cal.setTime(fechaHastaSalida);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
            
            String filtroTipoDoc = "";
            TiposDocumentosAdministrativos tipo = null;
            int i = 0;
            for(; i < listatipoDocumentoFiltro.size(); i++){
                tipo = listatipoDocumentoFiltro.get(i);
                if(!"".equals(filtroTipoDoc)){
                    filtroTipoDoc += ", ";
                }
                
                filtroTipoDoc += "'" + tipo.getCodigo() + "'";
            }
            
            if(i == 0){
                filtroTipoDoc = "-1";
            }
            
            String filtroOrigen = "";
            Departamentos origen = null;
            i = 0;
            for(; i < listaorigenFiltro.size(); i++){
                origen = listaorigenFiltro.get(i);
                if(!"".equals(filtroOrigen)){
                    filtroOrigen += ", ";
                }
                
                filtroOrigen += origen.getId();
            }
            
            if(i == 0){
                filtroOrigen = "-1";
            }
            
            String filtroDestino = "";
            Departamentos destino = null;
            i = 0;
            for(; i < listaDestinoFiltro.size(); i++){
                destino = listaDestinoFiltro.get(i);
                if(!"".equals(filtroDestino)){
                    filtroDestino += ", ";
                }
                
                filtroDestino += destino.getId();
            }
            
            if(i == 0){
                filtroDestino = "-1";
            }
            
            
            if(!"-1".equals(filtroTipoDoc) || !"-1".equals(filtroDestino) || !"-1".equals(filtroOrigen)){
                if("-1".equals(filtroTipoDoc)){
                    filtroTipoDoc = "";
                }
                if("-1".equals(filtroDestino)){
                    filtroDestino = "";
                }
                if("-1".equals(filtroOrigen)){
                    filtroOrigen = "";
                }
            }
            
            String parametros = "";
            
            if(!"".equals(filtroTipoDoc)){
                parametros += " and d.tipo_documento_administrativo in (" + filtroTipoDoc + ")";
            }
            
            if(!"".equals(filtroOrigen)){
                parametros += " and d.departamento_anterior in (" + filtroOrigen + ")";
            }
            
            if(!"".equals(filtroDestino)){
                parametros += " and d.departamento in (" + filtroDestino + ")";
            }
            
            String comando = "select concat(t.descripcion, e.nombre, p.nombre, d.estado) as codigo, t.descripcion as tipoDocumento, case when d.estado = 'AC' then 'Memos en bandeja de entrada' when d.estado = 'AR' then 'Memos en bandeja de archivados' end as Bandeja, e.nombre as origen, p.nombre as destino, count(*) as cantidad from documentos_administrativos as d, departamentos as e, departamentos as p, tipos_documentos_administrativos as t where d.departamento = p.id and t.codigo = d.tipo_documento_administrativo and d.departamento_anterior = e.id " + parametros + " and d.fecha_presentacion >= ?1 and d.fecha_presentacion <= ?2 group by t.descripcion, e.nombre, p.nombre, d.estado order by t.descripcion, d.estado, e.nombre, p.nombre";

            
            List<RepMemorandum> listarM = ejbFacade.getEntityManager().createNativeQuery(comando, RepMemorandum.class).setParameter(1, format3.format(fechaDesdeSalida)).setParameter(2, format3.format(nuevaFechaHasta)).getResultList();

            beanCollectionDataSource = new JRBeanCollectionDataSource(listarM);

            HashMap map = new HashMap();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Date fecha = ejbFacade.getSystemDate();

            map.put("fecha", format.format(fecha));
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            map.put("fechaDesde", format2.format(fechaDesdeSalida));
            map.put("fechaHasta", format2.format(fechaHastaSalida));

            JasperPrint jasperPrint = null;
            ServletOutputStream servletOutputStream = null;

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteMemorandum.jasper");
            jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

            servletOutputStream = httpServletResponse.getOutputStream();

            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            exporter.exportReport();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
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

        }finally{
            
            if(!filtroURL.verifPermiso("adminMemorandums")){
                if(origenAgregado){
                    listaorigenFiltro = new ArrayList<>();
                }
                if(destinoAgregado){
                    listaDestinoFiltro = new ArrayList<>();
                }
            }
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }
       
    
}
