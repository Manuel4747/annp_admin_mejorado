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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
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
@Table(name = "observaciones_resoluciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObservacionesResoluciones.findAll", query = "SELECT o FROM ObservacionesResoluciones o")
    , @NamedQuery(name = "ObservacionesResoluciones.findOrdered", query = "SELECT o FROM ObservacionesResoluciones o ORDER BY o.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "ObservacionesResoluciones.findById", query = "SELECT o FROM ObservacionesResoluciones o WHERE o.id = :id")
    , @NamedQuery(name = "ObservacionesResoluciones.findByResolucion", query = "SELECT o FROM ObservacionesResoluciones o WHERE o.resolucion = :resolucion ORDER BY o.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "ObservacionesResoluciones.findByObservacion", query = "SELECT o FROM ObservacionesResoluciones o WHERE o.observacion = :observacion")
    , @NamedQuery(name = "ObservacionesResoluciones.findByFechaHoraAlta", query = "SELECT o FROM ObservacionesResoluciones o WHERE o.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "ObservacionesResoluciones.findByFechaHoraUltimoEstado", query = "SELECT o FROM ObservacionesResoluciones o WHERE o.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class ObservacionesResoluciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Size(max = 65535)
    @Column(name = "observacion")
    private String observacion;
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
    @JoinColumn(name = "resolucion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Resoluciones resolucion;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;

    public ObservacionesResoluciones() {
    }

    public ObservacionesResoluciones(Integer id) {
        this.id = id;
    }

    public ObservacionesResoluciones(Integer id, String observacion, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.observacion = observacion;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObservacion() {
        return observacion;
    }

    @XmlTransient
    public String getObservacionString() {
        return observacion.replace("\n", "<br />");
    }
    
    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public Resoluciones getResolucion() {
        return resolucion;
    }

    public void setResolucion(Resoluciones resolucion) {
        this.resolucion = resolucion;
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

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
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
        if (!(object instanceof ObservacionesResoluciones)) {
            return false;
        }
        ObservacionesResoluciones other = (ObservacionesResoluciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.ObservacionesResoluciones[ id=" + id + " ]";
    }
    
}
