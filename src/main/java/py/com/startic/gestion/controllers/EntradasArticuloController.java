package py.com.startic.gestion.controllers;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import jakarta.annotation.PostConstruct;

import py.com.startic.gestion.models.EntradasArticulo;
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
import org.primefaces.PrimeFaces;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Articulos;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.DetallesEntradaArticulo;
import py.com.startic.gestion.models.DetallesSalidaArticulo;
import py.com.startic.gestion.models.Inventarios;
import py.com.startic.gestion.models.Unidades;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "entradasArticuloController")
@ViewScoped
public class EntradasArticuloController extends AbstractController<EntradasArticulo> {

    @Inject
    private ProveedoresController proveedorController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private DetallesEntradaArticuloController detallesEntradaArticuloController;
    @Inject
    private ArticulosController articuloController;
    @Inject
    private ArticulosStockCriticoController articuloStockCriticoController;

    private Collection<DetallesEntradaArticulo> detalles;

    private DetallesEntradaArticulo detalleSelected;

    private Articulos articulo;

    private Unidades unidad;

    private Integer cantidad;

    private EntradasArticulo entradaArticulo;
    private Date fechaDesde;
    private Date fechaHasta;
    private Date fechaDesdeEntrada;
    private Date fechaHastaEntrada;
    private BigDecimal costoUnitario;
    private BigDecimal costoTotal;
    private Articulos articuloFiltro;
    private Departamentos departamentoFiltro;

    @PostConstruct
    @Override
    public void initParams() {
        entradaArticulo = prepareCreate(null);
        fechaDesdeEntrada = ejbFacade.getSystemDateOnly(-30);
        fechaHastaEntrada = ejbFacade.getSystemDateOnly();
        fechaDesde = null;
        fechaHasta = null;
        super.initParams();
        buscarPorFechaEntrada();
    }

    @Override
    public EntradasArticulo prepareCreate(ActionEvent event) {

        detalles = null;
        detalleSelected = null;
        articulo = null;
        cantidad = 0;
        costoUnitario = BigDecimal.ZERO;
        costoTotal = BigDecimal.ZERO;
        
        Inventarios inv = null;
        try {
            inv = ejbFacade.getEntityManager().createNamedQuery("Inventarios.findVigente", Inventarios.class).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (inv == null) {
            JsfUtil.addErrorMessage("Para ingresar un movimiento debe haber al menos un inventario vigente");
            return null;
        }

        if (entradaArticulo != null) {
            entradaArticulo.setNroFactura("");
            entradaArticulo.setNroLlamado("");
            entradaArticulo.setProveedor(null);
            entradaArticulo.setConcepto("");
            entradaArticulo.setInventario(inv);
            entradaArticulo.setFechaEntrada(ejbFacade.getSystemDate());
        }

        return super.prepareCreate(event);
    }

    public EntradasArticuloController() {
        // Inform the Abstract parent controller of the concrete EntradasArticulo Entity
        super(EntradasArticulo.class);
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public EntradasArticulo getEntradaArticulo() {
        return entradaArticulo;
    }

    public void setEntradaArticulo(EntradasArticulo entradaArticulo) {
        this.entradaArticulo = entradaArticulo;
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

    public Date getFechaDesdeEntrada() {
        return fechaDesdeEntrada;
    }

    public void setFechaDesdeEntrada(Date fechaDesdeEntrada) {
        this.fechaDesdeEntrada = fechaDesdeEntrada;
    }

    public Date getFechaHastaEntrada() {
        return fechaHastaEntrada;
    }

    public void setFechaHastaEntrada(Date fechaHastaEntrada) {
        this.fechaHastaEntrada = fechaHastaEntrada;
    }

    public Unidades getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidades unidad) {
        this.unidad = unidad;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public DetallesEntradaArticulo getDetalleSelected() {
        return detalleSelected;
    }

    public void setDetalleSelected(DetallesEntradaArticulo detalleSelected) {
        this.detalleSelected = detalleSelected;
    }

    public Collection<DetallesEntradaArticulo> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<DetallesEntradaArticulo> detalles) {
        this.detalles = detalles;
    }

    public Articulos getArticuloFiltro() {
        return articuloFiltro;
    }

    public void setArticuloFiltro(Articulos articuloFiltro) {
        this.articuloFiltro = articuloFiltro;
    }

    public Departamentos getDepartamentoFiltro() {
        return departamentoFiltro;
    }

    public void setDepartamentoFiltro(Departamentos departamentoFiltro) {
        this.departamentoFiltro = departamentoFiltro;
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        proveedorController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresaController.setSelected(null);

        seleccionar();
    }

    @Override
    public Collection<EntradasArticulo> getItems() {
        return super.getItems2();
    }

    @Override
    public void delete(ActionEvent event) {

        if (getSelected() != null) {
            Articulos art = null;

            Collection<DetallesEntradaArticulo> col = ejbFacade.getEntityManager().createNamedQuery("DetallesEntradaArticulo.findByEntradaArticulo", DetallesEntradaArticulo.class).setParameter("entradaArticulo", getSelected()).getResultList();

            for (DetallesEntradaArticulo det : col) {
                art = det.getArticulo();

                art.setStock(art.getStock() - det.getCantidad());

                articuloController.setSelected(art);

                articuloController.save(event);

                detallesEntradaArticuloController.setSelected(det);

                detallesEntradaArticuloController.delete(event);
            }

            super.delete(event);
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("EntradasArticulo.findOrdered", EntradasArticulo.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).getResultList());
            setSelected(null);

            detalleSelected = null;
            detalles = null;
        }
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

    public void resetFilters() {
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('wvArticulos').clearFilters()");
    }

    public void buscarPorFechaAlta() {
        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("EntradasArticulo.findOrdered", EntradasArticulo.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).getResultList());

            if (getItems2().size() > 0) {
                EntradasArticulo art = getItems2().iterator().next();
                setSelected(art);
                detalles = art.getDetallesEntradaArticuloCollection();
                detalleSelected = null;
            } else {
                detalles = null;
                detalleSelected = null;
                setSelected(null);
            }
        }
    }
    
    public void buscarPorFechaEntrada() {
        if (fechaDesdeEntrada == null || fechaHastaEntrada == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHastaEntrada);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHastaEntrada = cal.getTime();
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("EntradasArticulo.findFechaEntradaOrdered", EntradasArticulo.class).setParameter("fechaDesde", fechaDesdeEntrada).setParameter("fechaHasta", nuevaFechaHastaEntrada).getResultList());

            if (getItems2().size() > 0) {
                EntradasArticulo art = getItems2().iterator().next();
                setSelected(art);
                detalles = art.getDetallesEntradaArticuloCollection();
                detalleSelected = null;
            } else {
                detalles = null;
                detalleSelected = null;
                setSelected(null);
            }
        }
    }

    private void seleccionar() {
        if (getSelected() != null) {
            detalles = this.ejbFacade.getEntityManager().createNamedQuery("DetallesEntradaArticulo.findByEntradaArticulo", DetallesEntradaArticulo.class).setParameter("entradaArticulo", getSelected()).getResultList();
        } else {
            detalles = null;
        }

    }

    /**
     * Sets the "items" attribute with a collection of DetallesEntradaArticulo
     * entities that are retrieved from EntradasArticulo?cap_first and returns
     * the navigation outcome.
     *
     * @return navigation outcome for DetallesEntradaArticulo page
     */
    public String navigateDetallesEntradaArticuloCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DetallesEntradaArticulo_items", this.getSelected().getDetallesEntradaArticuloCollection());
        }
        return "/pages/detallesEntradaArticulo/index";
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

    public void borrarDetalle() {
        if (detalles != null && detalleSelected != null) {
            for (DetallesEntradaArticulo det : detalles) {
                if (det.getArticulo().getCodigo().equals(detalleSelected.getArticulo().getCodigo())) {
                    detalles.remove(det);
                    break;
                }
            }
        }
    }

    public void agregar() {
        if (articulo == null) {
            JsfUtil.addErrorMessage("Debe seleccionar el articulo");
            return;
        }

        if (costoUnitario == null) {
            JsfUtil.addErrorMessage("Debe especificar un costo unitario");
            return;
        } else if (costoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            JsfUtil.addErrorMessage("El costo unitario debe ser mayor a cero");
            return;
        }

        if (cantidad == null) {
            JsfUtil.addErrorMessage("Debe especificar la cantidad");
            return;
        } else if (cantidad <= 0) {
            JsfUtil.addErrorMessage("La cantidad debe ser mayor a cero");
            return;
        }

        if (detalles == null) {
            detalles = new ArrayList<>();
        }

        boolean encontro = false;

        for (DetallesEntradaArticulo det : detalles) {
            if (det.getArticulo().getCodigo().equals(articulo.getCodigo())) {

                detalles.remove(det);

                DetallesEntradaArticulo dea = detallesEntradaArticuloController.prepareCreate(null);

                SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

                Date fecha = ejbFacade.getSystemDate();

                dea.setCantidad(det.getCantidad() + cantidad);
                dea.setArticulo(articulo);
                dea.setCostoUnitario(costoUnitario);
                dea.setCostoTotal(costoUnitario.multiply(new BigDecimal(dea.getCantidad())));
                dea.setId(Integer.valueOf(format.format(fecha)));
                detalles.add(dea);
                encontro = true;
                break;
            }
        }

        if (!encontro) {
            DetallesEntradaArticulo dea = detallesEntradaArticuloController.prepareCreate(null);

            SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

            Date fecha = ejbFacade.getSystemDate();

            dea.setCantidad(cantidad);
            dea.setArticulo(articulo);
            dea.setCostoUnitario(costoUnitario);
            dea.setCostoTotal(costoUnitario.multiply(new BigDecimal(dea.getCantidad())));
            dea.setId(Integer.valueOf(format.format(fecha)));
            dea.setInventario(entradaArticulo.getInventario());
            detalles.add(dea);
        }
        
        // RequestContext requestContext = RequestContext.getCurrentInstance();
        // requestContext.execute("PF('wvArticulos').clearFilters()");

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

            entradaArticulo.setFechaHoraUltimoEstado(fecha);
            entradaArticulo.setUsuarioUltimoEstado(usu);
            entradaArticulo.setFechaHoraAlta(fecha);
            entradaArticulo.setUsuarioAlta(usu);
            entradaArticulo.setEmpresa(usu.getEmpresa());

            setSelected(entradaArticulo);

            super.saveNew(event);

            for (DetallesEntradaArticulo det : detalles) {
                det.setEntradaArticulo(getSelected());
                det.setFechaHoraUltimoEstado(fecha);
                det.setUsuarioUltimoEstado(usu);
                det.setFechaHoraAlta(fecha);
                det.setUsuarioAlta(usu);
                det.setEmpresa(usu.getEmpresa());
                det.setNroFactura(getSelected().getNroFactura());
                det.setNroLlamado(getSelected().getNroLlamado());
                det.setProveedor(getSelected().getProveedor());
                det.setId(null);

                Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", det.getArticulo().getCodigo()).getSingleResult();

                art.setStock(art.getStock() + det.getCantidad());

                articuloController.setSelected(art);

                articuloController.save(event);

                detallesEntradaArticuloController.setSelected(det);

                detallesEntradaArticuloController.saveNew(event);

            }
            /*
            setItems(ejbFacade.getEntityManager().createNamedQuery("EntradasArticulo.findAll", EntradasArticulo.class).getResultList());

            if (getItems2().size() > 0) {
                EntradasArticulo art = getItems2().iterator().next();
                setSelected(art);
                detalles = ejbFacade.getEntityManager().createNamedQuery("DetallesEntradaArticulo.findByEntradaArticulo", DetallesEntradaArticulo.class).setParameter("entradaArticulo", art).getResultList();
                detalleSelected = null;
            } else {
                setSelected(null);
            }
             */

            if (fechaDesde == null) {
                fechaDesde = ejbFacade.getSystemDateOnly(-30);
            }
            if (fechaHasta == null) {
                fechaHasta = ejbFacade.getSystemDateOnly();
            }

            buscarPorFechaAlta();

            articuloStockCriticoController.verifArticulosStockCritico();
        }

    }
    
    public void pdf() {

        if (articuloFiltro != null) {

            HttpServletResponse httpServletResponse = null;
            try {
                JRBeanCollectionDataSource beanCollectionDataSource = null;

                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                Calendar cal = Calendar.getInstance();
                cal.setTime(fechaHastaEntrada);
                cal.add(Calendar.DATE, 1);
                Date nuevaFechaHasta = cal.getTime();

                List<DetallesEntradaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesEntradaArticulo.findByArticuloFechaEntrada", DetallesEntradaArticulo.class).setParameter("fechaEntradaDesde", fechaDesdeEntrada).setParameter("fechaEntradaHasta", nuevaFechaHasta).setParameter("articulo", articuloFiltro).getResultList();
                for(DetallesEntradaArticulo det : lista){
                    det.setFechaEntrada(det.getEntradaArticulo().getFechaEntrada());
                }

                beanCollectionDataSource = new JRBeanCollectionDataSource(lista);

                HashMap map = new HashMap();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                
                Date fecha = ejbFacade.getSystemDate();

                map.put("fecha", format.format(fecha));
                map.put("fechaDesde", format2.format(fechaDesdeEntrada));
                map.put("fechaHasta", format2.format(fechaHastaEntrada));
                map.put("descArticulo", articuloFiltro.getDescripcion());

                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteEntradaArticulo.jasper");
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
        } else {
            JsfUtil.addErrorMessage("Debe escojer un articulo.");
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

}
