package py.com.startic.gestion.controllers;

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
import jakarta.faces.event.ActionEvent;
import jakarta.faces.validator.Validator;
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
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.DetallesSalidaArticulo;
import py.com.startic.gestion.models.DetallesSalidaArticuloCambios;
import py.com.startic.gestion.models.Inventarios;
import py.com.startic.gestion.models.SalidasArticuloCambios;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "salidasArticuloController")
@ViewScoped
public class SalidasArticuloController extends AbstractController<SalidasArticulo> {

    @Inject
    private UsuariosController personaController;
    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private ArticulosController articuloController;
    @Inject
    private ArticulosStockCriticoController articuloStockCriticoController;
    @Inject
    private SalidasArticuloCambiosController salidaArticuloCambioController;
    @Inject
    private SalidasArticuloController salidaArticuloController;
    @Inject
    private DetallesSalidaArticuloCambiosController detalleSalidaArticuloCambioController;
    @Inject
    private DetallesSalidaArticuloController detallesSalidaArticuloController;
    private Collection<DetallesSalidaArticulo> detalles;
    private Collection<DetallesSalidaArticulo> detallesEdit;
    private Collection<DetallesSalidaArticulo> detallesEditOri;
    private SalidasArticulo salidaArticuloOri;

    private DetallesSalidaArticulo detalleSelected;
    private DetallesSalidaArticulo detalleSelectedEdit;
    private Date fechaDesde;
    private Date fechaHasta;
    private Date fechaDesdeSalida;
    private Date fechaHastaSalida;

    private Articulos articulo;
    private Articulos articuloEdit;
    private Articulos articuloFiltro;
    private Departamentos departamentoFiltro;

    private Integer cantidad;
    private Integer cantidadEdit;

    private SalidasArticulo salidaArticulo;
    private String ordenarPor;
    private String tipoOrden;

    @PostConstruct
    @Override
    public void initParams() {
        salidaArticulo = prepareCreate(null);
        fechaDesdeSalida = ejbFacade.getSystemDateOnly(-30);
        fechaHastaSalida = ejbFacade.getSystemDateOnly();
        fechaDesde = null;
        fechaHasta = null;
        ordenarPor = "1";
        tipoOrden = "1";
        super.initParams();

        buscarPorFechaSalida();
    }

    @Override
    public SalidasArticulo prepareCreate(ActionEvent event) {

        detalles = null;
        detalleSelected = null;
        articulo = null;
        cantidad = 0;
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

        if (salidaArticulo != null) {
            salidaArticulo.setNroFormulario("");
            salidaArticulo.setPersona(null);
            salidaArticulo.setDepartamento(null);
            salidaArticulo.setInventario(inv);
        }

        return super.prepareCreate(event);
    }

    public void prepareEdit() {

        detallesEdit = null;
        detalleSelectedEdit = null;
        articuloEdit = null;
        cantidadEdit = 0;
        salidaArticuloOri = null;

        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Debe seleccionar un formulario");
            return;
        }

        salidaArticuloOri = new SalidasArticulo();

        salidaArticuloOri.setDepartamento(getSelected().getDepartamento());
        salidaArticuloOri.setEmpresa(getSelected().getEmpresa());
        salidaArticuloOri.setFechaHoraAlta(getSelected().getFechaHoraAlta());
        salidaArticuloOri.setFechaHoraUltimoEstado(getSelected().getFechaHoraUltimoEstado());
        salidaArticuloOri.setId(getSelected().getId());
        salidaArticuloOri.setInventario(getSelected().getInventario());
        salidaArticuloOri.setNroFormulario(getSelected().getNroFormulario());
        salidaArticuloOri.setPersona(getSelected().getPersona());
        salidaArticuloOri.setPersonas(getSelected().getPersonas());
        salidaArticuloOri.setUsuarioAlta(getSelected().getUsuarioAlta());
        salidaArticuloOri.setUsuarioUltimoEstado(getSelected().getUsuarioUltimoEstado());

        detallesEdit = this.ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class).setParameter("salidaArticulo", getSelected()).getResultList();
        detallesEditOri = this.ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class).setParameter("salidaArticulo", getSelected()).getResultList();
        /*
        detallesEditOri = new ArrayList<>();

        for (DetallesSalidaArticulo det : detallesEdit) {
            
            detallesEditOri.add(det);
        }
         */
    }

    public SalidasArticuloController() {
        // Inform the Abstract parent controller of the concrete SalidasArticulo Entity
        super(SalidasArticulo.class);
    }

    public String getOrdenarPor() {
        return ordenarPor;
    }

    public void setOrdenarPor(String ordenarPor) {
        this.ordenarPor = ordenarPor;
    }

    public Collection<DetallesSalidaArticulo> getDetallesEdit() {
        return detallesEdit;
    }

    public void setDetallesEdit(Collection<DetallesSalidaArticulo> detallesEdit) {
        this.detallesEdit = detallesEdit;
    }

    public String getTipoOrden() {
        return tipoOrden;
    }

    public void setTipoOrden(String tipoOrden) {
        this.tipoOrden = tipoOrden;
    }

    public Departamentos getDepartamentoFiltro() {
        return departamentoFiltro;
    }

    public void setDepartamentoFiltro(Departamentos departamentoFiltro) {
        this.departamentoFiltro = departamentoFiltro;
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

    public DetallesSalidaArticulo getDetalleSelectedEdit() {
        return detalleSelectedEdit;
    }

    public void setDetalleSelectedEdit(DetallesSalidaArticulo detalleSelectedEdit) {
        this.detalleSelectedEdit = detalleSelectedEdit;
    }

    public Articulos getArticuloEdit() {
        return articuloEdit;
    }

    public void setArticuloEdit(Articulos articuloEdit) {
        this.articuloEdit = articuloEdit;
    }

    public Integer getCantidadEdit() {
        return cantidadEdit;
    }

    public void setCantidadEdit(Integer cantidadEdit) {
        this.cantidadEdit = cantidadEdit;
    }

    public Articulos getArticuloFiltro() {
        return articuloFiltro;
    }

    public void setArticuloFiltro(Articulos articuloFiltro) {
        this.articuloFiltro = articuloFiltro;
    }

    public Collection<DetallesSalidaArticulo> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<DetallesSalidaArticulo> detalles) {
        this.detalles = detalles;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public SalidasArticulo getSalidaArticulo() {
        return salidaArticulo;
    }

    public void setSalidaArticulo(SalidasArticulo salidaArticulo) {
        this.salidaArticulo = salidaArticulo;
    }

    public DetallesSalidaArticulo getDetalleSelected() {
        return detalleSelected;
    }

    public void setDetalleSelected(DetallesSalidaArticulo detalleSelected) {
        this.detalleSelected = detalleSelected;
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        personaController.setSelected(null);
        departamentoController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        /*
        if (this.getSelected() == null && this.getItems() != null) {
            if (!this.getItems().isEmpty()) {
                this.setSelected(getItems().iterator().next());
            }
        }
         */
        seleccionar();
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

    public void buscarPorFechaAlta() {
        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
            if ("1".equals(ordenarPor)) {
                if ("1".equals(tipoOrden)) {
                    setItems(this.ejbFacade.getEntityManager().createNativeQuery(
                            "select * from salidas_articulo where fecha_hora_alta >= ?1 AND fecha_hora_alta <= ?2 ORDER BY CONVERT(nro_formulario,signed) DESC;", SalidasArticulo.class).setParameter(1, fechaDesde).setParameter(2, nuevaFechaHasta).getResultList());

                } else {
                    setItems(this.ejbFacade.getEntityManager().createNativeQuery(
                            "select * from salidas_articulo where fecha_hora_alta >= ?1 AND fecha_hora_alta <= ?2 ORDER BY CONVERT(nro_formulario,signed) ASC;", SalidasArticulo.class).setParameter(1, fechaDesde).setParameter(2, nuevaFechaHasta).getResultList());
                }
                // setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrderedNroFormulario", SalidasArticulo.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("tipo", "signed").getResultList());
            } else {
                if ("1".equals(tipoOrden)) {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrdered", SalidasArticulo.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).getResultList());
                } else {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrderedAsc", SalidasArticulo.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).getResultList());
                }
            }

            if (getItems2().size() > 0) {
                SalidasArticulo art = getItems2().iterator().next();
                setSelected(art);
                // detalles = art.getDetallesSalidaArticuloCollection();
                detalles = this.ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class).setParameter("salidaArticulo", getSelected()).getResultList();
                detalleSelected = null;
            } else {
                detalles = null;
                detalleSelected = null;
                setSelected(null);
            }

        }
    }

    public void buscarPorFechaSalida() {
        if (fechaDesdeSalida == null || fechaHastaSalida == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHastaSalida);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHastaSalida = cal.getTime();
            if ("1".equals(ordenarPor)) {
                if ("1".equals(tipoOrden)) {
                    setItems(this.ejbFacade.getEntityManager().createNativeQuery(
                            "select * from salidas_articulo where fecha_salida >= ?1 AND fecha_salida <= ?2 ORDER BY CONVERT(nro_formulario,signed) DESC;", SalidasArticulo.class).setParameter(1, fechaDesdeSalida).setParameter(2, nuevaFechaHastaSalida).getResultList());

                } else {
                    setItems(this.ejbFacade.getEntityManager().createNativeQuery(
                            "select * from salidas_articulo where fecha_salida >= ?1 AND fecha_salida <= ?2 ORDER BY CONVERT(nro_formulario,signed) ASC;", SalidasArticulo.class).setParameter(1, fechaDesdeSalida).setParameter(2, nuevaFechaHastaSalida).getResultList());
                }
                // setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrderedNroFormulario", SalidasArticulo.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("tipo", "signed").getResultList());
            } else {
                if ("1".equals(tipoOrden)) {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrdered", SalidasArticulo.class).setParameter("fechaDesde", fechaDesdeSalida).setParameter("fechaHasta", nuevaFechaHastaSalida).getResultList());
                } else {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrderedAsc", SalidasArticulo.class).setParameter("fechaDesde", fechaDesdeSalida).setParameter("fechaHasta", nuevaFechaHastaSalida).getResultList());
                }
            }

            if (getItems2().size() > 0) {
                SalidasArticulo art = getItems2().iterator().next();
                setSelected(art);
                // detalles = art.getDetallesSalidaArticuloCollection();
                detalles = this.ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class).setParameter("salidaArticulo", getSelected()).getResultList();
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
            detalles = this.ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class).setParameter("salidaArticulo", getSelected()).getResultList();
        } else {
            detalles = null;
        }

    }

    /**
     * Sets the "items" attribute with a collection of DetallesSalidaArticulo
     * entities that are retrieved from SalidasArticulo?cap_first and returns
     * the navigation outcome.
     *
     * @return navigation outcome for DetallesSalidaArticulo page
     */
    public String navigateDetallesSalidaArticuloCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DetallesSalidaArticulo_items", this.getSelected().getDetallesSalidaArticuloCollection());
        }
        return "/pages/detallesSalidaArticulo/index";
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

    public void prepareEmpresas(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
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

    public void borrarDetalle() {
        if (detalles != null && detalleSelected != null) {
            for (DetallesSalidaArticulo det : detalles) {
                if (det.getArticulo().getCodigo().equals(detalleSelected.getArticulo().getCodigo())) {
                    detalles.remove(det);
                    break;
                }
            }
        }
    }

    public void borrarDetalleEdit() {
        if (detallesEdit != null && detalleSelectedEdit != null) {
            for (DetallesSalidaArticulo det : detallesEdit) {
                if (det.getArticulo().getCodigo().equals(detalleSelectedEdit.getArticulo().getCodigo())) {
                    detallesEdit.remove(det);
                    break;
                }
            }
        }
    }

    /*
    
    public void borrarDetalle() {
        
        
        Collection<DetallesSalidaArticulo> detallesNuevo = new ArrayList<>();
        
        boolean encontrado = false;
        
        if (detalles != null && detalleSelected != null) {
            for (DetallesSalidaArticulo det : detalles) {
                if (det.getArticulo().getCodigo().equals(detalleSelected.getArticulo().getCodigo())) {
                    //detalles.remove(det);
                    encontrado = true;
                }else{
                    if(!encontrado){
                        detallesNuevo.add(det);
                    }else{
                        DetallesSalidaArticulo dsa = detallesSalidaArticuloController.prepareCreate(null);

                        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

                        Date fecha = ejbFacade.getSystemDate();

                        dsa.setItem(det.getItem() - 1);
                        dsa.setCantidad(det.getCantidad());
                        dsa.setArticulo(det.getArticulo());
                        dsa.setId(Integer.valueOf(format.format(fecha)));
                        dsa.setInventario(det.getInventario());
                        detallesNuevo.add(dsa);
                    }
                }
            }
            
            detalles = detallesNuevo;
        }
    }
     */
    @Override
    public void delete(ActionEvent event) {

        Articulos art = null;

        Collection<DetallesSalidaArticulo> col = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class).setParameter("salidaArticulo", getSelected()).getResultList();

        for (DetallesSalidaArticulo det : col) {
            art = det.getArticulo();

            art.setStock(art.getStock() + det.getCantidad());

            articuloController.setSelected(art);

            articuloController.save(event);

            detallesSalidaArticuloController.setSelected(det);

            detallesSalidaArticuloController.delete(event);
        }

        super.delete(event);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaHasta);
        cal.add(Calendar.DATE, 1);
        Date nuevaFechaHasta = cal.getTime();
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrdered", SalidasArticulo.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).getResultList());
        setSelected(null);

        detalleSelected = null;
        detalles = null;
    }

    public void agregar() {

        if (articulo == null) {
            JsfUtil.addErrorMessage("Debe seleccionar el articulo");
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

        int contador = 0;
        for (DetallesSalidaArticulo det : detalles) {
            contador++;
            if (det.getArticulo().getCodigo().equals(articulo.getCodigo())) {

                if (articulo.getStock() < det.getCantidad() + cantidad) {
                    JsfUtil.addErrorMessage("La cantidad del articulo no debe supera el stock actual");
                    return;
                }

                detalles.remove(det);

                DetallesSalidaArticulo dsa = detallesSalidaArticuloController.prepareCreate(null);

                SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

                Date fecha = ejbFacade.getSystemDate();

                dsa.setItem(det.getItem());
                dsa.setCantidad(det.getCantidad() + cantidad);
                dsa.setArticulo(articulo);
                dsa.setId(Integer.valueOf(format.format(fecha)));
                dsa.setInventario(salidaArticulo.getInventario());
                detalles.add(dsa);
                encontro = true;
                break;
            }
        }

        if (!encontro) {
            DetallesSalidaArticulo dsa = detallesSalidaArticuloController.prepareCreate(null);

            SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

            Date fecha = ejbFacade.getSystemDate();

            if (articulo.getStock() < cantidad) {
                JsfUtil.addErrorMessage("La cantidad del articulo no debe supera el stock actual");
                return;
            }

            dsa.setItem(contador + 1);
            dsa.setCantidad(cantidad);
            dsa.setArticulo(articulo);
            dsa.setId(Integer.valueOf(format.format(fecha)));
            dsa.setInventario(salidaArticulo.getInventario());

            detalles.add(dsa);
        }
        
        // articulo = null;
        // cantidad = null;

    }

    public void agregarEdit() {

        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Debe seleccionar un formulario");
            return;
        }

        if (articuloEdit == null) {
            JsfUtil.addErrorMessage("Debe seleccionar el articulo");
            return;
        }

        if (cantidadEdit == null) {
            JsfUtil.addErrorMessage("Debe especificar la cantidad");
            return;
        } else if (cantidadEdit <= 0) {
            JsfUtil.addErrorMessage("La cantidad debe ser mayor a cero");
            return;
        }

        if (detallesEdit == null) {
            detallesEdit = new ArrayList<>();
        }

        boolean encontro = false;

        int cantidadEditOri;

        DetallesSalidaArticulo detOriEncontrado = null;
        int contador = 0;
        for (DetallesSalidaArticulo det : detallesEdit) {
            contador++;
            if (det.getArticulo().getCodigo().equals(articuloEdit.getCodigo())) {

                cantidadEditOri = 0;
                detOriEncontrado = null;
                for (DetallesSalidaArticulo detOri : detallesEditOri) {
                    if (detOri.getArticulo().getCodigo().equals(articuloEdit.getCodigo())) {
                        cantidadEditOri = detOri.getCantidad();
                        detOriEncontrado = detOri;
                        break;
                    }
                }

                if (articuloEdit.getStock() < (det.getCantidad() + cantidadEdit - cantidadEditOri)) {
                    JsfUtil.addErrorMessage("La diferencia entre la cantidad actual (" + cantidadEditOri + ") y la cantidad nueva (" + (det.getCantidad() + cantidadEdit) + ") es mayor que el stock actual. Diferencia (" + (det.getCantidad() + cantidadEdit - cantidadEditOri) + "), stock actual (" + articuloEdit.getStock() + ")");
                    return;
                }

                detallesEdit.remove(det);

                DetallesSalidaArticulo dsa = detallesSalidaArticuloController.prepareCreate(null);

                // SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

                // Date fecha = ejbFacade.getSystemDate();

                dsa.setItem(det.getItem());
                dsa.setCantidad(det.getCantidad() + cantidadEdit);
                dsa.setArticulo(det.getArticulo());
                //dsa.setId(Integer.valueOf(format.format(fecha)));
                dsa.setId(det.getId());
                dsa.setInventario(det.getInventario());

                dsa.setDepartamento(det.getDepartamento());
                dsa.setEmpresa(det.getEmpresa());
                dsa.setFechaHoraAlta(det.getFechaHoraAlta());
                dsa.setFechaHoraUltimoEstado(det.getFechaHoraUltimoEstado());
                dsa.setNroFormulario(det.getNroFormulario());
                dsa.setPersona(det.getPersona());
                dsa.setSalidaArticulo(det.getSalidaArticulo());
                dsa.setUsuarioAlta(det.getUsuarioAlta());
                dsa.setUsuarioUltimoEstado(det.getUsuarioUltimoEstado());
           
                
                detallesEdit.add(dsa);
                encontro = true;
                break;
            }
        }

        if (!encontro) {

            DetallesSalidaArticulo dsa = detallesSalidaArticuloController.prepareCreate(null);

            SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

            Date fecha = ejbFacade.getSystemDate();

            cantidadEditOri = 0;
            detOriEncontrado = null;
            for (DetallesSalidaArticulo detOri : detallesEditOri) {
                if (detOri.getArticulo().getCodigo().equals(articuloEdit.getCodigo())) {
                    cantidadEditOri = detOri.getCantidad();
                    detOriEncontrado = detOri;
                    break;
                }
            }

            if (articuloEdit.getStock() < cantidadEdit - cantidadEditOri) {
                JsfUtil.addErrorMessage("La diferencia entre la cantidad actual (" + cantidadEditOri + ") y la cantidad nueva (" + cantidadEdit + ") es mayor que el stock actual. Diferencia (" + (cantidadEdit - cantidadEditOri) + "), stock actual (" + articuloEdit.getStock() + ")");

                return;
            }

            if (detOriEncontrado == null) {
                dsa.setItem(contador + 1);
                dsa.setCantidad(cantidadEdit);
                dsa.setArticulo(articuloEdit);
                dsa.setId(Integer.valueOf(format.format(fecha)));
                dsa.setInventario(getSelected().getInventario());
                dsa.setSalidaArticulo(getSelected());
                dsa.setNroFormulario(getSelected().getNroFormulario());
                dsa.setPersona(getSelected().getPersona());
                dsa.setDepartamento(getSelected().getDepartamento());

            } else {
                dsa.setItem(contador + 1);
                dsa.setCantidad(cantidadEdit);
                dsa.setArticulo(articuloEdit);
                dsa.setId(detOriEncontrado.getId());
                dsa.setInventario(detOriEncontrado.getInventario());
                dsa.setSalidaArticulo(detOriEncontrado.getSalidaArticulo());
                dsa.setFechaHoraUltimoEstado(detOriEncontrado.getFechaHoraUltimoEstado());
                dsa.setUsuarioUltimoEstado(detOriEncontrado.getUsuarioUltimoEstado());
                dsa.setFechaHoraAlta(detOriEncontrado.getFechaHoraAlta());
                dsa.setUsuarioAlta(detOriEncontrado.getUsuarioAlta());
                dsa.setEmpresa(detOriEncontrado.getEmpresa());
                dsa.setNroFormulario(detOriEncontrado.getNroFormulario());
                dsa.setPersona(detOriEncontrado.getPersona());
                dsa.setDepartamento(detOriEncontrado.getDepartamento());
            }
            detallesEdit.add(dsa);
        }
        
        // cantidad = null;
        // articulo = null;

    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        String key = event.getRowKey();
        

        boolean encontro = false;
        if (key != null && !((Integer) oldValue).equals((Integer) newValue)) {
            int cantidadEditOri;

            for (DetallesSalidaArticulo det : detallesEdit) {
                if (det.getId().equals(Integer.valueOf(key))) {
                    encontro = true;
                    cantidadEditOri = 0;
                    
                    if(((Integer) newValue).intValue() <= 0){
                        JsfUtil.addErrorMessage("No se puede cambiar la cantidad a un valor igual o menor a cero");
                        det.setCantidad(cantidadEditOri);
                        break;
                    }
                    
                    for (DetallesSalidaArticulo detOri : detallesEditOri) {
                        if (detOri.getArticulo().getCodigo().equals(det.getArticulo().getCodigo())) {
                            cantidadEditOri = detOri.getCantidad();
                            break;
                        }
                    }

                    if (det.getArticulo().getStock() < (((Integer) newValue) - cantidadEditOri)) {
                        JsfUtil.addErrorMessage("La diferencia entre la cantidad actual (" + cantidadEditOri + ") y la cantidad nueva (" + newValue + ") es mayor que el stock actual. Diferencia (" + (((Integer) newValue) - cantidadEditOri) + "), stock actual (" + det.getArticulo().getStock() + ")");
                        det.setCantidad(cantidadEditOri);
                        return;
                    }

                    // det.setCantidad(((Integer) newValue));
                    /*
                    detallesEdit.remove(det);

                    DetallesSalidaArticulo dsa = detallesSalidaArticuloController.prepareCreate(null);

                    SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

                    Date fecha = ejbFacade.getSystemDate();

                    dsa.setId(det.getId());
                    dsa.setItem(det.getItem());
                    dsa.setCantidad(((Integer) newValue));
                    dsa.setArticulo(det.getArticulo());
                    dsa.setInventario(det.getInventario());
                    dsa.setDepartamento(det.getDepartamento());
                    dsa.setEmpresa(det.getEmpresa());
                    dsa.setFechaHoraAlta(det.getFechaHoraAlta());
                    dsa.setFechaHoraUltimoEstado(det.getFechaHoraUltimoEstado());
                    dsa.setNroFormulario(det.getNroFormulario());
                    dsa.setPersona(det.getPersona());
                    dsa.setSalidaArticulo(det.getSalidaArticulo());
                    dsa.setUsuarioAlta(det.getUsuarioAlta());
                    dsa.setUsuarioUltimoEstado(det.getUsuarioUltimoEstado());
                    detallesEdit.add(dsa);
                     */
                    break;
                }
            }
        }

        if (!encontro) {
            JsfUtil.addErrorMessage("Error: no se puede validar el stock. Cancelando");
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

            super.save(event);

            // Guardar registro de cambios
            SalidasArticuloCambios sac = salidaArticuloCambioController.prepareCreate(event);

            sac.setSalidaArticulo(getSelected());
            sac.setDepartamento(salidaArticuloOri.getDepartamento());
            sac.setEmpresa(salidaArticuloOri.getEmpresa());
            sac.setFechaHoraAlta(salidaArticuloOri.getFechaHoraAlta());
            sac.setFechaHoraUltimoEstado(salidaArticuloOri.getFechaHoraUltimoEstado());
            sac.setInventario(salidaArticuloOri.getInventario());
            sac.setNroFormulario(salidaArticuloOri.getNroFormulario());
            sac.setPersona(salidaArticuloOri.getPersona());
            sac.setPersonas(salidaArticuloOri.getPersonas());
            sac.setFechaHoraCambio(fecha);
            sac.setUsuarioAlta(salidaArticuloOri.getUsuarioAlta());
            sac.setUsuarioCambio(usu);
            sac.setUsuarioUltimoEstado(salidaArticuloOri.getUsuarioUltimoEstado());

            String cambio = "IGUAL";

            if (getSelected().getDepartamento() != null) {
                if (!getSelected().getDepartamento().equals(salidaArticuloOri.getDepartamento())) {
                    cambio = "MODIFICADO";
                }
            } else if (salidaArticuloOri.getDepartamento() != null) {
                cambio = "MODIFICADO";
            }

            sac.setDepartamentoNuevo(getSelected().getDepartamento());

            if (getSelected().getInventario() != null) {
                if (!getSelected().getInventario().equals(salidaArticuloOri.getInventario())) {
                    cambio = "MODIFICADO";
                }
            } else if (salidaArticuloOri.getInventario() != null) {
                cambio = "MODIFICADO";
            }

            sac.setInventarioNuevo(getSelected().getInventario());

            if (getSelected().getNroFormulario() != null) {
                if (!getSelected().getNroFormulario().equals(salidaArticuloOri.getNroFormulario())) {
                    cambio = "MODIFICADO";
                }
            } else if (salidaArticuloOri.getNroFormulario() != null) {
                cambio = "MODIFICADO";
            }

            sac.setNroFormularioNuevo(getSelected().getNroFormulario());

            if (getSelected().getPersona() != null) {
                if (!getSelected().getPersona().equals(salidaArticuloOri.getPersona())) {
                    cambio = "MODIFICADO";
                }
            } else if (salidaArticuloOri.getPersona() != null) {
                cambio = "MODIFICADO";
            }

            sac.setPersonaNueva(getSelected().getPersona());

            if (getSelected().getPersonas() != null) {
                if (!getSelected().getPersonas().equals(salidaArticuloOri.getPersonas())) {
                    cambio = "MODIFICADO";
                }
            } else if (salidaArticuloOri.getPersonas() != null) {
                cambio = "MODIFICADO";
            }

            sac.setPersonasNuevas(getSelected().getPersonas());

            sac.setCambio(cambio);

            jakarta.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                    "select ifnull(max(secuencia),0) + 1 as VALOR from salidas_articulo_cambios WHERE salida_articulo = " + getSelected().getId() + ";", NroSecuencia.class);

            NroSecuencia secuencia = (NroSecuencia) query.getSingleResult();

            sac.setSecuencia(secuencia.getSecuencia());

            salidaArticuloCambioController.setSelected(sac);
            salidaArticuloCambioController.saveNew(event);

            // Fin Guardar registro de cambios
            int cantidadNueva;
            boolean itero;
            boolean encontro;

            DetallesSalidaArticulo detGuardar = null;

            DetallesSalidaArticuloCambios dsac = null;

            for (DetallesSalidaArticulo det : detallesEdit) {

                itero = false;
                encontro = false;
                cantidadNueva = 0;
                for (DetallesSalidaArticulo detOri : detallesEditOri) {
                    itero = true;
                    if (det.getArticulo().getCodigo().equals(detOri.getArticulo().getCodigo())) {
                        cantidadNueva = det.getCantidad() - detOri.getCantidad();
                        detGuardar = detOri;
                        encontro = true;
                        break;
                    } else {
                        cantidadNueva = det.getCantidad();
                    }
                }

                if (!itero) {
                    cantidadNueva = det.getCantidad();
                }

                if (cantidadNueva != 0) {
                    Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", det.getArticulo().getCodigo()).getSingleResult();

                    art.setStock(art.getStock() - cantidadNueva);

                    articuloController.setSelected(art);

                    articuloController.save(event);

                    if (encontro) {
                        det.setFechaHoraUltimoEstado(fecha);
                        det.setUsuarioUltimoEstado(usu);
                        if (detGuardar != null) {
                            det.setUsuarioAlta(detGuardar.getUsuarioAlta());
                            det.setFechaHoraAlta(detGuardar.getFechaHoraAlta());
                            det.setEmpresa(detGuardar.getEmpresa());
                        }

                        detallesSalidaArticuloController.setSelected(det);
                        detallesSalidaArticuloController.save(event);

                        // Guardar registro del cambio
                        dsac = detalleSalidaArticuloCambioController.prepareCreate(event);
                        dsac.setArticulo(detGuardar.getArticulo());
                        dsac.setCantidad(detGuardar.getCantidad());
                        dsac.setDepartamento(detGuardar.getDepartamento());
                        dsac.setEmpresa(detGuardar.getEmpresa());
                        dsac.setFechaHoraAlta(detGuardar.getFechaHoraAlta());
                        dsac.setFechaHoraUltimoEstado(detGuardar.getFechaHoraUltimoEstado());
                        dsac.setFechaHoraCambio(fecha);
                        dsac.setUsuarioCambio(usu);
                        dsac.setUsuarioAlta(detGuardar.getUsuarioAlta());
                        dsac.setUsuarioUltimoEstado(detGuardar.getUsuarioUltimoEstado());
                        dsac.setInventario(detGuardar.getInventario());
                        dsac.setItem(detGuardar.getItem());
                        dsac.setNroFormulario(detGuardar.getNroFormulario());
                        dsac.setPersona(detGuardar.getPersona());
                        dsac.setSalidaArticulo(detGuardar.getSalidaArticulo());
                        dsac.setSalidaArticuloCambio(sac);
                        dsac.setDetalleSalidaArticulo(detGuardar);
                        dsac.setSalidaArticulo(getSelected());

                        cambio = "IGUAL";
                        if (detGuardar.getArticulo() != null) {
                            if (!detGuardar.getArticulo().equals(det.getArticulo())) {
                                cambio = "MODIFICADO";
                            }
                        } else if (det.getArticulo() != null) {
                            cambio = "MODIFICADO";
                        }
                        dsac.setArticuloNuevo(det.getArticulo());

                        if (detGuardar.getCantidad() != det.getCantidad()) {
                            cambio = "MODIFICADO";
                        }
                        dsac.setCantidadNueva(det.getCantidad());

                        if (detGuardar.getDepartamento() != null) {
                            if (!detGuardar.getDepartamento().equals(det.getDepartamento())) {
                                cambio = "MODIFICADO";
                            }
                        } else if (det.getDepartamento() != null) {
                            cambio = "MODIFICADO";
                        }
                        dsac.setDepartamentoNuevo(det.getDepartamento());

                        if (detGuardar.getInventario() != null) {
                            if (!detGuardar.getInventario().equals(det.getInventario())) {
                                cambio = "MODIFICADO";
                            }
                        } else if (det.getInventario() != null) {
                            cambio = "MODIFICADO";
                        }
                        dsac.setInventarioNuevo(det.getInventario());

                        if (detGuardar.getItem() != det.getItem()) {
                            cambio = "MODIFICADO";
                        }
                        dsac.setItemNuevo(det.getItem());

                        if (detGuardar.getNroFormulario() != null) {
                            if (!detGuardar.getNroFormulario().equals(det.getNroFormulario())) {
                                cambio = "MODIFICADO";
                            }
                        } else if (det.getNroFormulario() != null) {
                            cambio = "MODIFICADO";
                        }
                        dsac.setNroFormularioNuevo(det.getNroFormulario());

                        if (detGuardar.getPersona() != null) {
                            if (!detGuardar.getPersona().equals(det.getPersona())) {
                                cambio = "MODIFICADO";
                            }
                        } else if (det.getPersona() != null) {
                            cambio = "MODIFICADO";
                        }
                        dsac.setPersonaNueva(det.getPersona());

                        dsac.setCambio(cambio);

                        query = ejbFacade.getEntityManager().createNativeQuery(
                                "select ifnull(max(secuencia),0) + 1 as VALOR from detalles_salida_articulo_cambios WHERE detalle_salida_articulo = " + detGuardar.getId() + ";", NroSecuencia.class);

                        secuencia = (NroSecuencia) query.getSingleResult();

                        dsac.setSecuencia(secuencia.getSecuencia());
                        detalleSalidaArticuloCambioController.setSelected(dsac);
                        detalleSalidaArticuloCambioController.saveNew(event);

                        // Fin Guardar registro del cambio
                    } else {
                        det.setId(null);
                        det.setFechaHoraUltimoEstado(fecha);
                        det.setUsuarioUltimoEstado(usu);
                        det.setFechaHoraAlta(fecha);
                        det.setUsuarioAlta(usu);
                        det.setEmpresa(usu.getEmpresa());

                        detallesSalidaArticuloController.setSelected(det);
                        detallesSalidaArticuloController.saveNew(event);

                        // Guardar registro del cambio
                        dsac = detalleSalidaArticuloCambioController.prepareCreate(event);
                        dsac.setArticulo(det.getArticulo());
                        dsac.setCantidad(det.getCantidad());
                        dsac.setDepartamento(det.getDepartamento());
                        dsac.setEmpresa(det.getEmpresa());
                        dsac.setFechaHoraAlta(det.getFechaHoraAlta());
                        dsac.setFechaHoraUltimoEstado(det.getFechaHoraUltimoEstado());
                        dsac.setFechaHoraCambio(fecha);
                        dsac.setUsuarioCambio(usu);
                        dsac.setUsuarioAlta(det.getUsuarioAlta());
                        dsac.setUsuarioUltimoEstado(det.getUsuarioUltimoEstado());
                        dsac.setInventario(det.getInventario());
                        dsac.setItem(det.getItem());
                        dsac.setNroFormulario(det.getNroFormulario());
                        dsac.setPersona(det.getPersona());
                        dsac.setSalidaArticulo(det.getSalidaArticulo());
                        dsac.setSalidaArticuloCambio(sac);
                        dsac.setDetalleSalidaArticulo(null);
                        dsac.setSalidaArticulo(getSelected());

                        dsac.setCambio("NUEVO");

                        query = ejbFacade.getEntityManager().createNativeQuery(
                                "select ifnull(max(secuencia),0) + 1 as VALOR from detalles_salida_articulo_cambios WHERE detalle_salida_articulo = " + detGuardar.getId() + ";", NroSecuencia.class);

                        secuencia = (NroSecuencia) query.getSingleResult();

                        dsac.setSecuencia(secuencia.getSecuencia());
                        detalleSalidaArticuloCambioController.setSelected(dsac);
                        detalleSalidaArticuloCambioController.saveNew(event);

                        // Fin Guardar registro del cambio
                    }

                } else if (cantidad == 0 && encontro) {
                    // Guardar registro del cambio
                    dsac = detalleSalidaArticuloCambioController.prepareCreate(event);
                    dsac.setArticulo(detGuardar.getArticulo());
                    dsac.setArticuloNuevo(detGuardar.getArticulo());
                    dsac.setCantidad(detGuardar.getCantidad());
                    dsac.setCantidadNueva(detGuardar.getCantidad());
                    dsac.setDepartamento(detGuardar.getDepartamento());
                    dsac.setDepartamentoNuevo(detGuardar.getDepartamento());
                    dsac.setEmpresa(detGuardar.getEmpresa());
                    dsac.setFechaHoraAlta(detGuardar.getFechaHoraAlta());
                    dsac.setFechaHoraUltimoEstado(detGuardar.getFechaHoraUltimoEstado());
                    dsac.setFechaHoraCambio(fecha);
                    dsac.setUsuarioCambio(usu);
                    dsac.setUsuarioAlta(detGuardar.getUsuarioAlta());
                    dsac.setUsuarioUltimoEstado(detGuardar.getUsuarioUltimoEstado());
                    dsac.setInventario(detGuardar.getInventario());
                    dsac.setInventarioNuevo(detGuardar.getInventario());
                    dsac.setItem(detGuardar.getItem());
                    dsac.setItemNuevo(detGuardar.getItem());
                    dsac.setNroFormulario(detGuardar.getNroFormulario());
                    dsac.setNroFormularioNuevo(detGuardar.getNroFormulario());
                    dsac.setPersona(detGuardar.getPersona());
                    dsac.setPersonaNueva(detGuardar.getPersona());
                    dsac.setSalidaArticulo(detGuardar.getSalidaArticulo());
                    dsac.setSalidaArticuloCambio(sac);
                    dsac.setDetalleSalidaArticulo(detGuardar);
                    dsac.setSalidaArticulo(getSelected());

                    dsac.setCambio("IGUAL");

                    query = ejbFacade.getEntityManager().createNativeQuery(
                            "select ifnull(max(secuencia),0) + 1 as VALOR from detalles_salida_articulo_cambios WHERE detalle_salida_articulo = " + detGuardar.getId() + ";", NroSecuencia.class);

                    secuencia = (NroSecuencia) query.getSingleResult();

                    dsac.setSecuencia(secuencia.getSecuencia());
                    detalleSalidaArticuloCambioController.setSelected(dsac);
                    detalleSalidaArticuloCambioController.saveNew(event);
                    // Guardar registro del cambio
                }

            }

            // Ahora borrar los que ya no estan
            for (DetallesSalidaArticulo detOri : detallesEditOri) {
                encontro = false;
                itero = false;
                for (DetallesSalidaArticulo det : detallesEdit) {
                    itero = true;
                    if (det.getArticulo().getCodigo().equals(detOri.getArticulo().getCodigo())) {
                        cantidadNueva = det.getCantidad() - detOri.getCantidad();
                        detGuardar = detOri;
                        encontro = true;
                        break;
                    } else {
                        cantidadNueva = det.getCantidad();
                    }
                }

                if (!encontro) {
                    Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", detOri.getArticulo().getCodigo()).getSingleResult();

                    art.setStock(art.getStock() + detOri.getCantidad());

                    articuloController.setSelected(art);

                    articuloController.save(event);

                    // Guardar registro del cambio
                    dsac = detalleSalidaArticuloCambioController.prepareCreate(event);
                    dsac.setArticulo(detOri.getArticulo());
                    dsac.setCantidad(detOri.getCantidad());
                    dsac.setDepartamento(detOri.getDepartamento());
                    dsac.setEmpresa(detOri.getEmpresa());
                    dsac.setFechaHoraAlta(detOri.getFechaHoraAlta());
                    dsac.setFechaHoraUltimoEstado(detOri.getFechaHoraUltimoEstado());
                    dsac.setFechaHoraCambio(fecha);
                    dsac.setUsuarioCambio(usu);
                    dsac.setUsuarioAlta(detOri.getUsuarioAlta());
                    dsac.setUsuarioUltimoEstado(detOri.getUsuarioUltimoEstado());
                    dsac.setInventario(detOri.getInventario());
                    dsac.setItem(detOri.getItem());
                    dsac.setNroFormulario(detOri.getNroFormulario());
                    dsac.setPersona(detOri.getPersona());
                    dsac.setSalidaArticulo(detOri.getSalidaArticulo());
                    dsac.setSalidaArticuloCambio(sac);
                    dsac.setDetalleSalidaArticulo(detOri);
                    dsac.setSalidaArticulo(getSelected());

                    dsac.setCambio("BORRADO");

                    query = ejbFacade.getEntityManager().createNativeQuery(
                            "select ifnull(max(secuencia),0) + 1 as VALOR from detalles_salida_articulo_cambios WHERE detalle_salida_articulo = " + detOri.getId() + ";", NroSecuencia.class);

                    secuencia = (NroSecuencia) query.getSingleResult();

                    dsac.setSecuencia(secuencia.getSecuencia());
                    detalleSalidaArticuloCambioController.setSelected(dsac);
                    detalleSalidaArticuloCambioController.saveNew(event);
                    // Fin Guardar registro del cambio

                    detallesSalidaArticuloController.setSelected(detOri);
                    detallesSalidaArticuloController.delete(event);
                }
            }

            /*
            setItems(ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findAll", SalidasArticulo.class).getResultList());

            if (getItems2().size() > 0) {
                SalidasArticulo art = getItems2().iterator().next();
                setSelected(art);
                detalles = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class).setParameter("salidaArticulo", art).getResultList();

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

    public void saveNewOnly(ActionEvent event) {
        super.saveNew(event);
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

            salidaArticulo.setFechaHoraUltimoEstado(fecha);
            salidaArticulo.setUsuarioUltimoEstado(usu);
            salidaArticulo.setFechaHoraAlta(fecha);
            salidaArticulo.setUsuarioAlta(usu);
            salidaArticulo.setEmpresa(usu.getEmpresa());
            //salidaArticulo.setDepartamento(salidaArticulo.getPersona().getDepartamento());

            setSelected(salidaArticulo);

            super.saveNew(event);

            for (DetallesSalidaArticulo det : detalles) {
                det.setSalidaArticulo(getSelected());
                det.setFechaHoraUltimoEstado(fecha);
                det.setUsuarioUltimoEstado(usu);
                det.setFechaHoraAlta(fecha);
                det.setUsuarioAlta(usu);
                det.setEmpresa(usu.getEmpresa());
                det.setNroFormulario(getSelected().getNroFormulario());
                det.setPersona(getSelected().getPersona());
                det.setDepartamento(getSelected().getDepartamento());
                det.setId(null);

                Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", det.getArticulo().getCodigo()).getSingleResult();

                art.setStock(art.getStock() - det.getCantidad());

                articuloController.setSelected(art);

                articuloController.save(event);

                detallesSalidaArticuloController.setSelected(det);

                detallesSalidaArticuloController.saveNew(event);

            }
            /*
            setItems(ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findAll", SalidasArticulo.class).getResultList());

            if (getItems2().size() > 0) {
                SalidasArticulo art = getItems2().iterator().next();
                setSelected(art);
                detalles = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class).setParameter("salidaArticulo", art).getResultList();

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
                cal.setTime(fechaHastaSalida);
                cal.add(Calendar.DATE, 1);
                Date nuevaFechaHasta = cal.getTime();

                List<DetallesSalidaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findByArticuloFechaSalida", DetallesSalidaArticulo.class).setParameter("fechaSalidaDesde", fechaDesdeSalida).setParameter("fechaSalidaHasta", nuevaFechaHasta).setParameter("articulo", articuloFiltro).getResultList();
                for(DetallesSalidaArticulo det : lista){
                    det.setFechaSalida(det.getSalidaArticulo().getFechaSalida());
                }

                beanCollectionDataSource = new JRBeanCollectionDataSource(lista);

                HashMap map = new HashMap();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                
                Date fecha = ejbFacade.getSystemDate();

                map.put("fecha", format.format(fecha));
                map.put("fechaDesde", format2.format(fechaDesdeSalida));
                map.put("fechaHasta", format2.format(fechaHastaSalida));
                map.put("descArticulo", articuloFiltro.getDescripcion());

                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticulo.jasper");
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

    public void pdf2() {

        if (departamentoFiltro != null) {

            HttpServletResponse httpServletResponse = null;
            try {
                JRBeanCollectionDataSource beanCollectionDataSource = null;

                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                Calendar cal = Calendar.getInstance();
                cal.setTime(fechaHastaSalida);
                cal.add(Calendar.DATE, 1);
                Date nuevaFechaHasta = cal.getTime();
                
                List<DetallesSalidaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findByDptoFechaSalida", DetallesSalidaArticulo.class).setParameter("fechaSalidaDesde", fechaDesdeSalida).setParameter("fechaSalidaHasta", nuevaFechaHasta).setParameter("departamento", departamentoFiltro).getResultList();

                for(DetallesSalidaArticulo det : lista){
                    det.setFechaSalida(det.getSalidaArticulo().getFechaSalida());
                }

                beanCollectionDataSource = new JRBeanCollectionDataSource(lista);

                HashMap map = new HashMap();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                Date fecha = ejbFacade.getSystemDate();
                
                map.put("descDpto", departamentoFiltro.getNombre());
                map.put("fecha", format.format(fecha));
                map.put("fechaDesde", format2.format(fechaDesdeSalida));
                map.put("fechaHasta", format2.format(fechaHastaSalida));

                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticuloDpto.jasper");
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
            JsfUtil.addErrorMessage("Debe escojer un departamento.");
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }
}
