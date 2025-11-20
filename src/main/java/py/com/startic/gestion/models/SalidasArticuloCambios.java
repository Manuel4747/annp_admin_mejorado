/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
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
@Table(name = "salidas_articulo_cambios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SalidasArticuloCambios.findAll", query = "SELECT s FROM SalidasArticuloCambios s ORDER BY s.fechaHoraAlta DESC")
    , @NamedQuery(name = "SalidasArticuloCambios.findOrdered", query = "SELECT s FROM SalidasArticuloCambios s WHERE s.fechaHoraAlta >= :fechaDesde AND s.fechaHoraAlta <= :fechaHasta ORDER BY s.fechaHoraAlta DESC")
    , @NamedQuery(name = "SalidasArticuloCambios.findOrderedAsc", query = "SELECT s FROM SalidasArticuloCambios s WHERE s.fechaHoraAlta >= :fechaDesde AND s.fechaHoraAlta <= :fechaHasta ORDER BY s.fechaHoraAlta ASC")
    , @NamedQuery(name = "SalidasArticuloCambios.findById", query = "SELECT s FROM SalidasArticuloCambios s WHERE s.id = :id")
    , @NamedQuery(name = "SalidasArticuloCambios.findByNroFormulario", query = "SELECT s FROM SalidasArticuloCambios s WHERE s.nroFormulario = :nroFormulario")
    , @NamedQuery(name = "SalidasArticuloCambios.findByFechaHoraAlta", query = "SELECT s FROM SalidasArticuloCambios s WHERE s.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "SalidasArticuloCambios.findByFechaHoraUltimoEstado", query = "SELECT s FROM SalidasArticuloCambios s WHERE s.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class SalidasArticuloCambios implements Serializable {

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
    @Basic(optional = false)
    @Column(name = "nro_formulario_nuevo")
    private String nroFormularioNuevo;
    @Basic(optional = true)
    @Size(max = 500)
    @Column(name = "personas_nuevas")
    private String personasNuevas;
    @JoinColumn(name = "inventario_nuevo", referencedColumnName = "id")
    @ManyToOne(optional = false)
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
    @JoinColumn(name = "departamento_nuevo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamentoNuevo;
    @JoinColumn(name = "usuario_cambio", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioCambio;
    @JoinColumn(name = "salida_articulo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private SalidasArticulo salidaArticulo;

    public SalidasArticuloCambios() {
    }

    public SalidasArticuloCambios(Integer id) {
        this.id = id;
    }

    public SalidasArticuloCambios(Integer id, String nroFormulario, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
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

    public String getNroFormularioNuevo() {
        return nroFormularioNuevo;
    }

    public void setNroFormularioNuevo(String nroFormularioNuevo) {
        this.nroFormularioNuevo = nroFormularioNuevo;
    }

    public String getPersonasNuevas() {
        return personasNuevas;
    }

    public void setPersonasNuevas(String personasNuevas) {
        this.personasNuevas = personasNuevas;
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

    public Departamentos getDepartamentoNuevo() {
        return departamentoNuevo;
    }

    public void setDepartamentoNuevo(Departamentos departamentoNuevo) {
        this.departamentoNuevo = departamentoNuevo;
    }

    public Usuarios getUsuarioCambio() {
        return usuarioCambio;
    }

    public void setUsuarioCambio(Usuarios usuarioCambio) {
        this.usuarioCambio = usuarioCambio;
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

    public SalidasArticulo getSalidaArticulo() {
        return salidaArticulo;
    }

    public void setSalidaArticulo(SalidasArticulo salidaArticulo) {
        this.salidaArticulo = salidaArticulo;
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
        if (!(object instanceof SalidasArticuloCambios)) {
            return false;
        }
        SalidasArticuloCambios other = (SalidasArticuloCambios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.SalidasArticuloCambios[ id=" + id + " ]";
    }
    
}
