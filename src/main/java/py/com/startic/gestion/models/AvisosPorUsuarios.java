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
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "avisos_por_usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AvisosPorUsuarios.findAll", query = "SELECT r FROM AvisosPorUsuarios r")
    , @NamedQuery(name = "AvisosPorUsuarios.findByAvisoAndUsuario", query = "SELECT r FROM AvisosPorUsuarios r WHERE r.avisosPorUsuariosPK.aviso = :aviso AND r.avisosPorUsuariosPK.usuario = :usuario")})
public class AvisosPorUsuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AvisosPorUsuariosPK avisosPorUsuariosPK;
    @JoinColumn(name = "aviso", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Avisos avisos;
    @JoinColumn(name = "usuario", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuarios usuarios;
    @Basic(optional = true)
    @Column(name = "fecha_hora_visto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraVisto;
    @Basic(optional = true)
    @Column(name = "visto")
    private boolean visto;
    @Basic(optional = true)
    @Column(name = "programado")
    private boolean programado;
    

    public AvisosPorUsuarios() {
    }

    public AvisosPorUsuarios(AvisosPorUsuariosPK avisoesPorUsuariosPK) {
        this.avisosPorUsuariosPK = avisoesPorUsuariosPK;
    }

    public AvisosPorUsuarios(int usuario, int aviso) {
        this.avisosPorUsuariosPK = new AvisosPorUsuariosPK(usuario, aviso);
    }

    public AvisosPorUsuariosPK getAvisosPorUsuariosPK() {
        return avisosPorUsuariosPK;
    }

    public void setAvisosPorUsuariosPK(AvisosPorUsuariosPK avisoesPorUsuariosPK) {
        this.avisosPorUsuariosPK = avisoesPorUsuariosPK;
    }

    public Date getFechaHoraVisto() {
        return fechaHoraVisto;
    }

    public void setFechaHoraVisto(Date fechaHoraVisto) {
        this.fechaHoraVisto = fechaHoraVisto;
    }

    public boolean isVisto() {
        return visto;
    }

    public void setVisto(boolean visto) {
        this.visto = visto;
    }

    public boolean isProgramado() {
        return programado;
    }

    public void setProgramado(boolean programado) {
        this.programado = programado;
    }

    public Avisos getAvisos() {
        return avisos;
    }

    public void setAvisos(Avisos avisos) {
        this.avisos = avisos;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (avisosPorUsuariosPK != null ? avisosPorUsuariosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AvisosPorUsuarios)) {
            return false;
        }
        AvisosPorUsuarios other = (AvisosPorUsuarios) object;
        if ((this.avisosPorUsuariosPK == null && other.avisosPorUsuariosPK != null) || (this.avisosPorUsuariosPK != null && !this.avisosPorUsuariosPK.equals(other.avisosPorUsuariosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.AvisosPorUsuarios[ avisoesPorUsuariosPK=" + avisosPorUsuariosPK + " ]";
    }
    
}
