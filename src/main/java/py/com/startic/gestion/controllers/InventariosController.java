package py.com.startic.gestion.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import jakarta.annotation.PostConstruct;

import py.com.startic.gestion.models.Inventarios;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.event.CellEditEvent;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Articulos;
import py.com.startic.gestion.models.DetallesInventario;
import py.com.startic.gestion.models.EstadosInventario;
import py.com.startic.gestion.models.ReportesInventario;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "inventariosController")
@ViewScoped
public class InventariosController extends AbstractController<Inventarios> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private DetallesInventarioController detallesInventarioController;
    @Inject
    private EstadosInventarioController estadoController;
    @Inject
    private ArticulosController articulosController;

    private Collection<DetallesInventario> detalles;

    private DetallesInventario detallesSelected;
    
    private Date fechaHasta;
    
    private Articulos articulo;

    public DetallesInventario getDetallesSelected() {
        return detallesSelected;
    }

    public void setDetallesSelected(DetallesInventario detallesSelected) {
        this.detallesSelected = detallesSelected;
    }

    public Collection<DetallesInventario> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<DetallesInventario> detalles) {
        this.detalles = detalles;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public InventariosController() {
        // Inform the Abstract parent controller of the concrete Inventarios Entity
        super(Inventarios.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        estadoController.setSelected(null);

        seleccionar();
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        setItems(ejbFacade.getEntityManager().createNamedQuery("Inventarios.findOrdered", Inventarios.class).getResultList());
        if (getItems2().size() > 0) {
            Inventarios art = getItems2().iterator().next();
            setSelected(art);
            
            agregarAInventario();

            seleccionar();
            // detalles = art.getDetallesInventarioCollection();
            // detallesSelected = null;
        } else {
            setSelected(null);
        }
    }

    private void seleccionar() {
        if (getSelected() != null) {
            detalles = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", getSelected()).getResultList();
        } else {
            detalles = null;
        }

        detallesSelected = null;

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

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        String key = event.getRowKey();

        if (key != null && !((Integer) oldValue).equals((Integer) newValue)) {
            detallesSelected = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findById", DetallesInventario.class).setParameter("id", Integer.valueOf(key)).getSingleResult();
            detallesSelected.setCantidad((Integer) newValue);
            detallesInventarioController.setSelected(detallesSelected);
            detallesInventarioController.save(null);
        }
    }

    @Override
    public Collection<Inventarios> getItems() {
        return super.getItems2();

    }

    @Override
    public void delete(ActionEvent event) {

        if (getSelected() != null) {
            if ("PR".equals(getSelected().getEstado().getCodigo())) {

                Collection<DetallesInventario> col = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", getSelected()).getResultList();

                for (DetallesInventario det : col) {
                    detallesInventarioController.setSelected(det);
                    detallesInventarioController.delete(event);
                }
                super.delete(event);

                detalles = null;
                detallesSelected = null;
                setItems(ejbFacade.getEntityManager().createNamedQuery("Inventarios.findOrdered", Inventarios.class).getResultList());

            } else {
                JsfUtil.addErrorMessage("Solo se puede borrar un inventario en proceso");
            }
        }
    }

    public void cerrarInventario() {
        if (getSelected() != null) {
            if ("PR".equals(getSelected().getEstado().getCodigo())) {

                try {
                    Inventarios inv = ejbFacade.getEntityManager().createNamedQuery("Inventarios.findVigente", Inventarios.class).getSingleResult();

                    inv.setEstado(ejbFacade.getEntityManager().createNamedQuery("EstadosInventario.findByCodigo", EstadosInventario.class).setParameter("codigo", "CE").getSingleResult());

                    Inventarios invTmp = getSelected();

                    setSelected(inv);

                    save(null);

                    setSelected(invTmp);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                getSelected().setEstado(ejbFacade.getEntityManager().createNamedQuery("EstadosInventario.findByCodigo", EstadosInventario.class).setParameter("codigo", "TE").getSingleResult());

                getSelected().setFecha(ejbFacade.getSystemDate());

                save(null);

                Articulos art = null;

                Collection<DetallesInventario> col = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", getSelected()).getResultList();

                for (DetallesInventario det : col) {
                    art = det.getArticulo();
                    art.setStock(det.getCantidad());

                    articulosController.setSelected(art);
                    articulosController.save(null);

                }

                setItems(ejbFacade.getEntityManager().createNamedQuery("Inventarios.findOrdered", Inventarios.class).getResultList());

            } else {
                JsfUtil.addErrorMessage("El inventario ya se encuentra finalizado");
            }
        }
    }

    public void nuevoInventario() {
        Inventarios invEnProceso = null;
        try {
            invEnProceso = ejbFacade.getEntityManager().createNamedQuery("Inventarios.findEnProceso", Inventarios.class).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (invEnProceso != null) {
            JsfUtil.addErrorMessage("Ya existe un inventario en proceso. Terminelo o borrelo antes de crear uno nuevo");
            return;
        }

        setSelected(prepareCreate(null));

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

        Date fecha = ejbFacade.getSystemDate();

        getSelected().setFechaHoraAlta(fecha);
        getSelected().setFechaHoraUltimoEstado(fecha);
        getSelected().setUsuarioAlta(usu);
        getSelected().setUsuarioUltimoEstado(usu);
        getSelected().setEmpresa(usu.getEmpresa());

        getSelected().setEstado(ejbFacade.getEntityManager().createNamedQuery("EstadosInventario.findByCodigo", EstadosInventario.class).setParameter("codigo", "PR").getSingleResult());

        saveNew(null);

        Collection<Articulos> col = ejbFacade.getEntityManager().createNamedQuery("Articulos.findAll", Articulos.class).getResultList();
        for (Articulos art : col) {
            DetallesInventario det = detallesInventarioController.prepareCreate(null);

            det.setArticulo(art);
            det.setCantidad(0);
            det.setEmpresa(usu.getEmpresa());
            det.setFechaHoraAlta(fecha);
            det.setFechaHoraUltimoEstado(fecha);
            det.setUsuarioAlta(usu);
            det.setUsuarioUltimoEstado(usu);
            det.setInventario(getSelected());

            detallesInventarioController.setSelected(det);

            detallesInventarioController.saveNew2(null);
        }

        setItems(ejbFacade.getEntityManager().createNamedQuery("Inventarios.findOrdered", Inventarios.class).getResultList());

        setSelected(ejbFacade.getEntityManager().createNamedQuery("Inventarios.findEnProceso", Inventarios.class).getSingleResult());

        detalles = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", getSelected()).getResultList();

    }

    public void agregarAInventario() {
        Inventarios invEnProceso = null;
        try {
            invEnProceso = ejbFacade.getEntityManager().createNamedQuery("Inventarios.findEnProceso", Inventarios.class).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (invEnProceso != null) {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            boolean encontro = false;
            Collection<Articulos> col = ejbFacade.getEntityManager().createNamedQuery("Articulos.findAll", Articulos.class).getResultList();
            for (Articulos art : col) {

                encontro = false;
                Collection<DetallesInventario> colInv = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", invEnProceso).getResultList();

                for (DetallesInventario det : colInv) {
                    if (det.getArticulo().equals(art)) {
                        encontro = true;
                        break;
                    }
                }

                if (!encontro) {
                    DetallesInventario det = detallesInventarioController.prepareCreate(null);

                    det.setArticulo(art);
                    det.setCantidad(0);
                    det.setEmpresa(usu.getEmpresa());
                    det.setFechaHoraAlta(fecha);
                    det.setFechaHoraUltimoEstado(fecha);
                    det.setUsuarioAlta(usu);
                    det.setUsuarioUltimoEstado(usu);
                    det.setInventario(invEnProceso);

                    detallesInventarioController.setSelected(det);

                    detallesInventarioController.saveNew2(null);
                }
            }
/*
            setItems(ejbFacade.getEntityManager().createNamedQuery("Inventarios.findOrdered", Inventarios.class).getResultList());

            setSelected(ejbFacade.getEntityManager().createNamedQuery("Inventarios.findEnProceso", Inventarios.class).getSingleResult());

            detalles = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", getSelected()).getResultList();
*/
        }

    }

    /**
     * Sets the "items" attribute with a collection of DetallesInventario
     * entities that are retrieved from Inventarios?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for DetallesInventario page
     */
    public String navigateDetallesInventarioCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DetallesInventario_items", this.getSelected().getDetallesInventarioCollection());
        }
        return "/pages5/detallesInventario/index";
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
    
    public void prepareReporteInventario() {
        fechaHasta = ejbFacade.getSystemDate();
        articulo = null;
    }
    

    public void pdf() {

        HttpServletResponse httpServletResponse = null;
        if (getSelected() != null) {
            try {
                JRBeanCollectionDataSource beanCollectionDataSource = null;
                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                // beanCollectionDataSource = new JRBeanCollectionDataSource(ejbFacade.getEntityManager().createNamedQuery("ReportesInventario.findByInventario", ReportesInventario.class).setParameter("inventario", getSelected().getId()).getResultList());

                
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                
                String articuloStr = "";
                if(articulo != null){
                    articuloStr = "and s.articulo = '" + articulo.getCodigo() + "' ";
                }
                
                jakarta.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                "select s.codigo, s.articulo, s.inventario, s.fecha, s.descripcion, s.simbolo, s.cant_inventario, s.cant_entrada, s.cant_salida, s.neto, s.stock " +
                "from (select @reporte_inventario_hasta := str_to_date('" + format2.format(fechaHasta) + "', '%d/%m/%Y')) a,  reportes_inventario2 s where " +
                "s.inventario = " + getSelected().getId() + " " +
                articuloStr +
                " order by s.descripcion"
                , ReportesInventario.class);
                
                
                beanCollectionDataSource = new JRBeanCollectionDataSource(query.getResultList());
                
                
                HashMap map = new HashMap();

                Date fecha = ejbFacade.getSystemDate();

                map.put("fecha", format.format(fecha));
                map.put("hasta", format2.format(fechaHasta));
                map.put("estado", getSelected().getEstado().getDescripcion());

                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteInventarios.jasper");
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

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
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

    public void pdf2() {

        HttpServletResponse httpServletResponse = null;
        if (getSelected() != null) {
            try {
                JRBeanCollectionDataSource beanCollectionDataSource = null;

                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                beanCollectionDataSource = new JRBeanCollectionDataSource(ejbFacade.getEntityManager().createNamedQuery("ReportesInventario.findByInventario", Inventarios.class).setParameter("inventario", getSelected().getId()).getResultList());

                HashMap map = new HashMap();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                Date fecha = ejbFacade.getSystemDate();

                map.put("fecha", format.format(fecha));
                map.put("hasta", format2.format(fecha));

                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteInventarioVacio.jasper");
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

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
                    } else {
                        httpServletResponse.setHeader("Content-disposition", "inline");
                    }

                }
                JsfUtil.addErrorMessage("No se pudo generar el reporte.");

            }
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

}
