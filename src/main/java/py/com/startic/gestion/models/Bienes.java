/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "bienes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bienes.findAll", query = "SELECT b FROM Bienes b")
    , @NamedQuery(name = "Bienes.findReporte", query = "SELECT b FROM Bienes b ORDER BY b.especificacionSubcuenta.subcuenta.cuenta.descripcion, b.especificacionSubcuenta.subcuenta.descripcion, b.descripcion")
    , @NamedQuery(name = "Bienes.findOrdered", query = "SELECT b FROM Bienes b ORDER BY b.fechaHoraUltimoEstado DESC, b.descripcion")
    , @NamedQuery(name = "Bienes.findById", query = "SELECT b FROM Bienes b WHERE b.id = :id")
    , @NamedQuery(name = "Bienes.findPadres", query = "SELECT b FROM Bienes b ORDER BY b.descripcion")
    , @NamedQuery(name = "Bienes.findByDescripcion", query = "SELECT b FROM Bienes b WHERE b.descripcion = :descripcion")
    , @NamedQuery(name = "Bienes.findByOrigenBien", query = "SELECT b FROM Bienes b WHERE b.origenBien = :origenBien")
    , @NamedQuery(name = "Bienes.findByRotulado", query = "SELECT b FROM Bienes b WHERE b.rotulado = :rotulado")
    , @NamedQuery(name = "Bienes.findByFechaHoraAlta", query = "SELECT b FROM Bienes b WHERE b.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "Bienes.findByCostoUnitario", query = "SELECT b FROM Bienes b WHERE b.costoUnitario = :costoUnitario")
    , @NamedQuery(name = "Bienes.findByFechaHoraUltimoEstado", query = "SELECT b FROM Bienes b WHERE b.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")
    , @NamedQuery(name = "Bienes.findByMarca", query = "SELECT b FROM Bienes b WHERE b.marca = :marca")
    , @NamedQuery(name = "Bienes.findByModelo", query = "SELECT b FROM Bienes b WHERE b.modelo = :modelo")
    , @NamedQuery(name = "Bienes.findByNroSerie", query = "SELECT b FROM Bienes b WHERE b.nroSerie = :nroSerie")
    , @NamedQuery(name = "Bienes.findByNroChapa", query = "SELECT b FROM Bienes b WHERE b.nroChapa = :nroChapa")
    , @NamedQuery(name = "Bienes.findByNroChasis", query = "SELECT b FROM Bienes b WHERE b.nroChasis = :nroChasis")
    , @NamedQuery(name = "Bienes.findByNroFactura", query = "SELECT b FROM Bienes b WHERE b.nroFactura = :nroFactura")
    , @NamedQuery(name = "Bienes.findByEspecificacionSubcuenta", query = "SELECT b FROM Bienes b WHERE b.especificacionSubcuenta = :especificacionSubcuenta")
    , @NamedQuery(name = "Bienes.findByNroLlamado", query = "SELECT b FROM Bienes b WHERE b.nroLlamado = :nroLlamado")
    , @NamedQuery(name = "Bienes.findByNroResolucionBaja", query = "SELECT b FROM Bienes b WHERE b.nroResolucionBaja = :nroResolucionBaja")
    , @NamedQuery(name = "Bienes.findByFechaBaja", query = "SELECT b FROM Bienes b WHERE b.fechaBaja = :fechaBaja")})
public class Bienes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "origen_bien", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private OrigenesBienes origenBien;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "rotulado")
    private String rotulado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = true)
    @Column(name = "costo_unitario")
    private BigDecimal costoUnitario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @Size(max = 200)
    @Column(name = "marca")
    private String marca;
    @Size(max = 200)
    @Column(name = "garantia")
    private String garantia;
    @Size(max = 200)
    @Column(name = "modelo")
    private String modelo;
    @Size(max = 40)
    @Column(name = "nro_serie")
    private String nroSerie;
    @Size(max = 40)
    @Column(name = "nro_chapa")
    private String nroChapa;
    @Size(max = 40)
    @Column(name = "nro_chasis")
    private String nroChasis;
    @Size(max = 200)
    @Column(name = "ano")
    private String ano;
    @Size(max = 200)
    @Column(name = "rasp")
    private String rasp;
    @Size(max = 40)
    @Column(name = "nro_factura")
    private String nroFactura;
    @JoinColumn(name = "especificacion_subcuenta", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private EspecificacionesSubcuenta especificacionSubcuenta;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamento;
    @Size(max = 40)
    @Column(name = "nro_llamado")
    private String nroLlamado;
    @Size(max = 20)
    @Column(name = "nro_resolucion_baja")
    private String nroResolucionBaja;
    @Column(name = "fecha_baja")
    @Temporal(TemporalType.DATE)
    private Date fechaBaja;
    @Column(name = "fecha_adquisicion")
    @Temporal(TemporalType.DATE)
    private Date fechaAdquisicion;
    @Lob
    @Column(name = "foto")
    private byte[] foto;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "bien")
    private Collection<FotosBienes> fotosBienesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "bien")
    private Collection<CambiosRotuladosBienes> cambiosRotuladosBienesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "bien")
    private Collection<MovimientosReparacionBienes> movimientosReparacionBienesCollection;
    @JoinColumn(name = "observacion_bien", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private ObservacionesBienes observacionBien;
    @Basic(optional = true)
    @Size(max = 1000)
    @Column(name = "ultima_observacion_bien")
    private String ultimaObservacionBien;
    @Basic(optional = true)
    @Column(name = "fecha_ultima_observacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltimoObservacion;
    @JoinColumn(name = "usuario_ultima_observacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimaObservacion;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private EstadosBienes estado;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Proveedores proveedor;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_baja", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioBaja;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios responsable;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "bien")
    private Collection<ObservacionesBienes> observacionesBienesCollection;
    @OneToMany(mappedBy = "bien")
    private Collection<MovimientosBienes> movimientosBienesCollection;

    public Bienes() {
    }

    public Bienes(Integer id) {
        this.id = id;
    }

    public Bienes(Integer id, OrigenesBienes origenBien, String rotulado, Date fechaHoraAlta, BigDecimal costoUnitario, Date fechaHoraUltimoEstado, EspecificacionesSubcuenta especificacionSubcuenta) {
        this.id = id;
        this.origenBien = origenBien;
        this.rotulado = rotulado;
        this.fechaHoraAlta = fechaHoraAlta;
        this.costoUnitario = costoUnitario;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.especificacionSubcuenta = especificacionSubcuenta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGarantia() {
        return garantia;
    }

    public void setGarantia(String garantia) {
        this.garantia = garantia;
    }

    public Date getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(Date fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getRasp() {
        return rasp;
    }

    public void setRasp(String rasp) {
        this.rasp = rasp;
    }

    public Usuarios getUsuarioBaja() {
        return usuarioBaja;
    }

    public void setUsuarioBaja(Usuarios usuarioBaja) {
        this.usuarioBaja = usuarioBaja;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public ObservacionesBienes getObservacionBien() {
        return observacionBien;
    }

    public void setObservacionBien(ObservacionesBienes observacionBien) {
        this.observacionBien = observacionBien;
    }
    
    @XmlTransient
    public String getEstadoSistema(){
        if( nroResolucionBaja != null){
            return "De Baja";
        }
        
        return "Activo";
    }

    public String getUltimaObservacionBien() {
        return ultimaObservacionBien;
    }

    public void setUltimaObservacionBien(String ultimaObservacionBien) {
        this.ultimaObservacionBien = ultimaObservacionBien;
    }

    public Date getFechaUltimoObservacion() {
        return fechaUltimoObservacion;
    }

    public void setFechaUltimoObservacion(Date fechaUltimoObservacion) {
        this.fechaUltimoObservacion = fechaUltimoObservacion;
    }

    public Usuarios getUsuarioUltimaObservacion() {
        return usuarioUltimaObservacion;
    }

    public void setUsuarioUltimaObservacion(Usuarios usuarioUltimaObservacion) {
        this.usuarioUltimaObservacion = usuarioUltimaObservacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public OrigenesBienes getOrigenBien() {
        return origenBien;
    }

    public void setOrigenBien(OrigenesBienes origenBien) {
        this.origenBien = origenBien;
    }

    public String getRotulado() {
        return rotulado;
    }

    public void setRotulado(String rotulado) {
        this.rotulado = rotulado;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNroSerie() {
        return nroSerie;
    }

    public void setNroSerie(String nroSerie) {
        this.nroSerie = nroSerie;
    }

    public String getNroChapa() {
        return nroChapa;
    }

    public void setNroChapa(String nroChapa) {
        this.nroChapa = nroChapa;
    }

    public String getNroChasis() {
        return nroChasis;
    }

    public void setNroChasis(String nroChasis) {
        this.nroChasis = nroChasis;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public EspecificacionesSubcuenta getEspecificacionSubcuenta() {
        return especificacionSubcuenta;
    }

    public void setEspecificacionSubcuenta(EspecificacionesSubcuenta especificacionSubcuenta) {
        this.especificacionSubcuenta = especificacionSubcuenta;
    }

    public String getNroLlamado() {
        return nroLlamado;
    }

    public void setNroLlamado(String nroLlamado) {
        this.nroLlamado = nroLlamado;
    }

    public String getNroResolucionBaja() {
        return nroResolucionBaja;
    }

    public void setNroResolucionBaja(String nroResolucionBaja) {
        this.nroResolucionBaja = nroResolucionBaja;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    @XmlTransient
    public Collection<FotosBienes> getFotosBienesCollection() {
        return fotosBienesCollection;
    }

    public void setFotosBienesCollection(Collection<FotosBienes> fotosBienesCollection) {
        this.fotosBienesCollection = fotosBienesCollection;
    }

    @XmlTransient
    public Collection<CambiosRotuladosBienes> getCambiosRotuladosBienesCollection() {
        return cambiosRotuladosBienesCollection;
    }

    public void setCambiosRotuladosBienesCollection(Collection<CambiosRotuladosBienes> cambiosRotuladosBienesCollection) {
        this.cambiosRotuladosBienesCollection = cambiosRotuladosBienesCollection;
    }

    @XmlTransient
    public Collection<MovimientosReparacionBienes> getMovimientosReparacionBienesCollection() {
        return movimientosReparacionBienesCollection;
    }

    public void setMovimientosReparacionBienesCollection(Collection<MovimientosReparacionBienes> movimientosReparacionBienesCollection) {
        this.movimientosReparacionBienesCollection = movimientosReparacionBienesCollection;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public EstadosBienes getEstado() {
        return estado;
    }

    public void setEstado(EstadosBienes estado) {
        this.estado = estado;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
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

    public Usuarios getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuarios responsable) {
        this.responsable = responsable;
    }

    @XmlTransient
    public Collection<ObservacionesBienes> getObservacionesBienesCollection() {
        return observacionesBienesCollection;
    }

    public void setObservacionesBienesCollection(Collection<ObservacionesBienes> observacionesBienesCollection) {
        this.observacionesBienesCollection = observacionesBienesCollection;
    }

    @XmlTransient
    public Collection<MovimientosBienes> getMovimientosBienesCollection() {
        return movimientosBienesCollection;
    }

    public void setMovimientosBienesCollection(Collection<MovimientosBienes> movimientosBienesCollection) {
        this.movimientosBienesCollection = movimientosBienesCollection;
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
        if (!(object instanceof Bienes)) {
            return false;
        }
        Bienes other = (Bienes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descripcion;
    }
    
}
