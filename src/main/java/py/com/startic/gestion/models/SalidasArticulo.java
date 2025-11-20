/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
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
 * @author grecia
 */
@Entity
@Table(name = "salidas_articulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SalidasArticulo.findAll", query = "SELECT s FROM SalidasArticulo s ORDER BY s.fechaHoraAlta DESC")
    , @NamedQuery(name = "SalidasArticulo.findOrdered", query = "SELECT s FROM SalidasArticulo s WHERE s.fechaHoraAlta >= :fechaDesde AND s.fechaHoraAlta <= :fechaHasta ORDER BY s.fechaHoraAlta DESC")
    , @NamedQuery(name = "SalidasArticulo.findOrderedAsc", query = "SELECT s FROM SalidasArticulo s WHERE s.fechaHoraAlta >= :fechaDesde AND s.fechaHoraAlta <= :fechaHasta ORDER BY s.fechaHoraAlta ASC")
    , @NamedQuery(name = "SalidasArticulo.findFechaEntradaOrdered", query = "SELECT s FROM SalidasArticulo s WHERE s.fechaSalida >= :fechaDesde AND s.fechaSalida <= :fechaHasta ORDER BY s.fechaSalida DESC, s.fechaHoraAlta DESC")
    , @NamedQuery(name = "SalidasArticulo.findFechaEntradaOrderedAsc", query = "SELECT s FROM SalidasArticulo s WHERE s.fechaSalida >= :fechaDesde AND s.fechaSalida <= :fechaHasta ORDER BY s.fechaSalida DESC, s.fechaHoraAlta DESC")
    , @NamedQuery(name = "SalidasArticulo.findById", query = "SELECT s FROM SalidasArticulo s WHERE s.id = :id")
    , @NamedQuery(name = "SalidasArticulo.findByNroFormulario", query = "SELECT s FROM SalidasArticulo s WHERE s.nroFormulario = :nroFormulario")
    , @NamedQuery(name = "SalidasArticulo.findByFechaHoraAlta", query = "SELECT s FROM SalidasArticulo s WHERE s.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "SalidasArticulo.findByFechaHoraUltimoEstado", query = "SELECT s FROM SalidasArticulo s WHERE s.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class SalidasArticulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nro_formulario")
    private String nroFormulario;
    @Basic(optional = true)
    @Size(max = 500)
    @Column(name = "personas")
    private String personas;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_salida")
    @Temporal(TemporalType.DATE)
    private Date fechaSalida;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "salidaArticulo")
    private Collection<DetallesSalidaArticulo> detallesSalidaArticuloCollection;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios persona;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;

    public SalidasArticulo() {
    }

    public SalidasArticulo(Integer id) {
        this.id = id;
    }

    public SalidasArticulo(Integer id, String nroFormulario, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.nroFormulario = nroFormulario;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPersonas() {
        return personas;
    }

    public void setPersonas(String personas) {
        this.personas = personas;
    }

    public Inventarios getInventario() {
        return inventario;
    }

    public void setInventario(Inventarios inventario) {
        this.inventario = inventario;
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

    @XmlTransient
    public Collection<DetallesSalidaArticulo> getDetallesSalidaArticuloCollection() {
        return detallesSalidaArticuloCollection;
    }

    public void setDetallesSalidaArticuloCollection(Collection<DetallesSalidaArticulo> detallesSalidaArticuloCollection) {
        this.detallesSalidaArticuloCollection = detallesSalidaArticuloCollection;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }
    
    public Usuarios getPersona() {
        return persona;
    }

    public void setPersona(Usuarios persona) {
        this.persona = persona;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
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

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
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
        if (!(object instanceof SalidasArticulo)) {
            return false;
        }
        SalidasArticulo other = (SalidasArticulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.SalidasArticulo[ id=" + id + " ]";
    }
    
}
