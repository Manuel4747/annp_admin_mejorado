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
 * @author eduardo
 */
@Entity
@Table(name = "rh_marcaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RhMarcaciones.findAll", query = "SELECT r FROM RhMarcaciones r")
    , @NamedQuery(name = "RhMarcaciones.findById", query = "SELECT r FROM RhMarcaciones r WHERE r.id = :id")
    , @NamedQuery(name = "RhMarcaciones.findByRangoFecha", query = "SELECT r FROM RhMarcaciones r WHERE r.fechaHoraMarcacion >= :fechaDesde AND r.fechaHoraMarcacion <= :fechaHasta ORDER BY r.fechaHoraMarcacion")
    , @NamedQuery(name = "RhMarcaciones.findByReloj", query = "SELECT r FROM RhMarcaciones r WHERE r.reloj = :reloj")
    , @NamedQuery(name = "RhMarcaciones.findByCodigoMarcacion", query = "SELECT r FROM RhMarcaciones r WHERE r.codigoUsuario = :codigoUsuario")
    , @NamedQuery(name = "RhMarcaciones.findByModoVerificacion", query = "SELECT r FROM RhMarcaciones r WHERE r.modoVerificacion = :modoVerificacion")
    , @NamedQuery(name = "RhMarcaciones.findByEntradaSalida", query = "SELECT r FROM RhMarcaciones r WHERE r.entradaSalida = :entradaSalida")
    , @NamedQuery(name = "RhMarcaciones.findByFechaHoraMarcacion", query = "SELECT r FROM RhMarcaciones r WHERE r.fechaHoraMarcacion = :fechaHoraMarcacion")
    , @NamedQuery(name = "RhMarcaciones.findByCodigoTrabajo", query = "SELECT r FROM RhMarcaciones r WHERE r.codigoTrabajo = :codigoTrabajo")
    , @NamedQuery(name = "RhMarcaciones.findByEmpresa", query = "SELECT r FROM RhMarcaciones r WHERE r.empresa = :empresa")})
public class RhMarcaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "reloj", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private RhRelojes reloj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "codigo_usuario")
    private String codigoUsuario;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuario;
    @JoinColumn(name = "modo_verificacion", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private RhModosVerificacion modoVerificacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "entrada_salida")
    private String entradaSalida;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_marcacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraMarcacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo_trabajo")
    private Integer codigoTrabajo;
    @JoinColumn(name = "modo_verificacion", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private RhModosVerificacion rhModosVerificacion;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;

    public RhMarcaciones() {
    }

    public RhMarcaciones(int id, RhRelojes reloj, String codigoUsuario, RhModosVerificacion modoVerificacion, String entradaSalida, Date fechaHoraMarcacion, int codigoTrabajo, Empresas empresa) {
        this.id = id;
        this.reloj = reloj;
        this.codigoUsuario = codigoUsuario;
        this.modoVerificacion = modoVerificacion;
        this.entradaSalida = entradaSalida;
        this.fechaHoraMarcacion = fechaHoraMarcacion;
        this.codigoTrabajo = codigoTrabajo;
        this.empresa = empresa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }
    
    public String getEntradaSalida() {
        return entradaSalida;
    }

    public void setEntradaSalida(String entradaSalida) {
        this.entradaSalida = entradaSalida;
    }

    public Date getFechaHoraMarcacion() {
        return fechaHoraMarcacion;
    }

    public void setFechaHoraMarcacion(Date fechaHoraMarcacion) {
        this.fechaHoraMarcacion = fechaHoraMarcacion;
    }

    public Integer getCodigoTrabajo() {
        return codigoTrabajo;
    }

    public void setCodigoTrabajo(Integer codigoTrabajo) {
        this.codigoTrabajo = codigoTrabajo;
    }

    public RhModosVerificacion getRhModosVerificacion() {
        return rhModosVerificacion;
    }

    public void setRhModosVerificacion(RhModosVerificacion rhModosVerificacion) {
        this.rhModosVerificacion = rhModosVerificacion;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public RhModosVerificacion getModoVerificacion() {
        return modoVerificacion;
    }

    public void setModoVerificacion(RhModosVerificacion modoVerificacion) {
        this.modoVerificacion = modoVerificacion;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public RhRelojes getReloj() {
        return reloj;
    }

    public void setReloj(RhRelojes reloj) {
        this.reloj = reloj;
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
        RhMarcaciones other = (RhMarcaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "py.com.startic.gestion.models18.RhMarcaciones[ id=" + id + " ]";
    }
    
}
