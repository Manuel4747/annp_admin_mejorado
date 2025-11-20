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
@Table(name = "entradas_documentos_administrativos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EntradasDocumentosAdministrativos.findAll", query = "SELECT e FROM EntradasDocumentosAdministrativos e")
    , @NamedQuery(name = "EntradasDocumentosAdministrativos.findById", query = "SELECT e FROM EntradasDocumentosAdministrativos e WHERE e.id = :id")
    , @NamedQuery(name = "EntradasDocumentosAdministrativos.findByFechaHoraAlta", query = "SELECT e FROM EntradasDocumentosAdministrativos e WHERE e.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "EntradasDocumentosAdministrativos.findByNroMesaEntrada", query = "SELECT e FROM EntradasDocumentosAdministrativos e WHERE e.nroMesaEntrada = :nroMesaEntrada")
    , @NamedQuery(name = "EntradasDocumentosAdministrativos.findByFechaHoraUltimoEstado", query = "SELECT e FROM EntradasDocumentosAdministrativos e WHERE e.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class EntradasDocumentosAdministrativos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Size(max = 200)
    @Column(name = "nro_mesa_entrada")
    private String nroMesaEntrada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;

    public EntradasDocumentosAdministrativos() {
    }

    public EntradasDocumentosAdministrativos(Integer id) {
        this.id = id;
    }

    public EntradasDocumentosAdministrativos(Integer id, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNroMesaEntrada() {
        return nroMesaEntrada;
    }

    public void setNroMesaEntrada(String nroMesaEntrada) {
        this.nroMesaEntrada = nroMesaEntrada;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntradasDocumentosAdministrativos)) {
            return false;
        }
        EntradasDocumentosAdministrativos other = (EntradasDocumentosAdministrativos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nroMesaEntrada;
    }
    
}
