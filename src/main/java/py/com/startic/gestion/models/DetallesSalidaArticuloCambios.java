/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author grecia
 */
@Entity
@Table(name = "detalles_salida_articulo_cambios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallesSalidaArticuloCambios.findAll", query = "SELECT d FROM DetallesSalidaArticulo d")
    , @NamedQuery(name = "DetallesSalidaArticuloCambios.findById", query = "SELECT d FROM DetallesSalidaArticuloCambios d WHERE d.id = :id")
    , @NamedQuery(name = "DetallesSalidaArticuloCambios.findByCantidad", query = "SELECT d FROM DetallesSalidaArticuloCambios d WHERE d.cantidad = :cantidad")
    , @NamedQuery(name = "DetallesSalidaArticuloCambios.findByNroFormulario", query = "SELECT d FROM DetallesSalidaArticuloCambios d WHERE d.nroFormulario = :nroFormulario")
    , @NamedQuery(name = "DetallesSalidaArticuloCambios.findBySalidaArticulo", query = "SELECT d FROM DetallesSalidaArticuloCambios d WHERE d.salidaArticulo = :salidaArticulo ORDER BY d.item")
    , @NamedQuery(name = "DetallesSalidaArticuloCambios.findByArticulo", query = "SELECT d FROM DetallesSalidaArticuloCambios d WHERE d.articulo = :articulo")
    , @NamedQuery(name = "DetallesSalidaArticuloCambios.findByArticuloFechaAlta", query = "SELECT d FROM DetallesSalidaArticuloCambios d WHERE d.fechaHoraAlta > :fechaAltaDesde AND d.fechaHoraAlta <= :fechaAltaHasta AND d.articulo = :articulo ORDER BY d.fechaHoraAlta")
    , @NamedQuery(name = "DetallesSalidaArticuloCambios.findByDptoFechaAlta", query = "SELECT d FROM DetallesSalidaArticuloCambios d WHERE d.fechaHoraAlta > :fechaAltaDesde AND d.fechaHoraAlta <= :fechaAltaHasta AND d.departamento = :departamento ORDER BY d.fechaHoraAlta")
    , @NamedQuery(name = "DetallesSalidaArticuloCambios.findByFechaHoraAlta", query = "SELECT d FROM DetallesSalidaArticuloCambios d WHERE d.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "DetallesSalidaArticuloCambios.findByFechaHoraUltimoEstado", query = "SELECT d FROM DetallesSalidaArticuloCambios d WHERE d.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class DetallesSalidaArticuloCambios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @Basic(optional = true)
    @Column(name = "item")
    private int item;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nro_formulario")
    private String nroFormulario;
    @JoinColumn(name = "inventario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Inventarios inventario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios persona;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "articulo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Articulos articulo;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "salida_articulo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SalidasArticulo salidaArticulo;
    @JoinColumn(name = "detalle_salida_articulo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DetallesSalidaArticulo detalleSalidaArticulo;
    @Basic(optional = true)
    @Column(name = "cantidad_nueva")
    private int cantidadNueva;
    @Basic(optional = true)
    @Column(name = "item_nuevo")
    private int itemNuevo;
    @Basic(optional = false)
    @Column(name = "nro_formulario_nuevo")
    private String nroFormularioNuevo;
    @JoinColumn(name = "inventario_nuevo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Inventarios inventarioNuevo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_cambio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraCambio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "cambio")
    private String cambio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "secuencia")
    private int secuencia;
    @JoinColumn(name = "persona_nueva", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios personaNueva;
    @JoinColumn(name = "usuario_cambio", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioCambio;
    @JoinColumn(name = "articulo_nuevo", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private Articulos articuloNuevo;
    @JoinColumn(name = "departamento_nuevo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamentoNuevo;
    @JoinColumn(name = "salida_articulo_cambio", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SalidasArticuloCambios salidaArticuloCambio;

    public DetallesSalidaArticuloCambios() {
    }

    public DetallesSalidaArticuloCambios(Integer id) {
        this.id = id;
    }

    public DetallesSalidaArticuloCambios(Integer id, int cantidad, String nroFormulario,Articulos articulo, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.cantidad = cantidad;
        this.nroFormulario = nroFormulario;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.articulo= articulo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public Inventarios getInventario() {
        return inventario;
    }

    public void setInventario(Inventarios inventario) {
        this.inventario = inventario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNroFormulario() {
        return nroFormulario;
    }

    public void setNroFormulario(String nroFormulario) {
        this.nroFormulario = nroFormulario;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Usuarios getPersona() {
        return persona;
    }

    public void setPersona(Usuarios persona) {
        this.persona = persona;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public SalidasArticulo getSalidaArticulo() {
        return salidaArticulo;
    }

    public void setSalidaArticulo(SalidasArticulo salidaArticulo) {
        this.salidaArticulo = salidaArticulo;
    }

    public int getCantidadNueva() {
        return cantidadNueva;
    }

    public void setCantidadNueva(int cantidadNueva) {
        this.cantidadNueva = cantidadNueva;
    }

    public int getItemNuevo() {
        return itemNuevo;
    }

    public void setItemNuevo(int itemNuevo) {
        this.itemNuevo = itemNuevo;
    }

    public String getNroFormularioNuevo() {
        return nroFormularioNuevo;
    }

    public void setNroFormularioNuevo(String nroFormularioNuevo) {
        this.nroFormularioNuevo = nroFormularioNuevo;
    }

    public Inventarios getInventarioNuevo() {
        return inventarioNuevo;
    }

    public void setInventarioNuevo(Inventarios inventarioNuevo) {
        this.inventarioNuevo = inventarioNuevo;
    }

    public Date getFechaHoraCambio() {
        return fechaHoraCambio;
    }

    public void setFechaHoraCambio(Date fechaHoraCambio) {
        this.fechaHoraCambio = fechaHoraCambio;
    }

    public Usuarios getPersonaNueva() {
        return personaNueva;
    }

    public void setPersonaNueva(Usuarios personaNueva) {
        this.personaNueva = personaNueva;
    }

    public Usuarios getUsuarioCambio() {
        return usuarioCambio;
    }

    public void setUsuarioCambio(Usuarios usuarioCambio) {
        this.usuarioCambio = usuarioCambio;
    }

    public Articulos getArticuloNuevo() {
        return articuloNuevo;
    }

    public void setArticuloNuevo(Articulos articuloNuevo) {
        this.articuloNuevo = articuloNuevo;
    }

    public Departamentos getDepartamentoNuevo() {
        return departamentoNuevo;
    }

    public void setDepartamentoNuevo(Departamentos departamentoNuevo) {
        this.departamentoNuevo = departamentoNuevo;
    }

    public SalidasArticuloCambios getSalidaArticuloCambio() {
        return salidaArticuloCambio;
    }

    public void setSalidaArticuloCambio(SalidasArticuloCambios salidaArticuloCambio) {
        this.salidaArticuloCambio = salidaArticuloCambio;
    }

    public String getCambio() {
        return cambio;
    }

    public void setCambio(String cambio) {
        this.cambio = cambio;
    }

    public int getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(int secuencia) {
        this.secuencia = secuencia;
    }

    public DetallesSalidaArticulo getDetalleSalidaArticulo() {
        return detalleSalidaArticulo;
    }

    public void setDetalleSalidaArticulo(DetallesSalidaArticulo detalleSalidaArticulo) {
        this.detalleSalidaArticulo = detalleSalidaArticulo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallesSalidaArticuloCambios)) {
            return false;
        }
        DetallesSalidaArticuloCambios other = (DetallesSalidaArticuloCambios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.DetallesSalidaArticuloCambios[ id=" + id + " ]";
    }
    
}
