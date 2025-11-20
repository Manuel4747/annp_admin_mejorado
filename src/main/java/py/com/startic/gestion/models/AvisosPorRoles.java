/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "avisos_por_roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AvisosPorRoles.findAll", query = "SELECT p FROM AvisosPorRoles p")
    , @NamedQuery(name = "AvisosPorRoles.findByPermiso", query = "SELECT p FROM AvisosPorRoles p WHERE p.avisosPorRolesPK.aviso = :aviso")
    , @NamedQuery(name = "AvisosPorRoles.findByRol", query = "SELECT p FROM AvisosPorRoles p WHERE p.avisosPorRolesPK.rol = :rol")})
public class AvisosPorRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AvisosPorRolesPK avisosPorRolesPK;
    @JoinColumn(name = "aviso", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Avisos avisos;
    @JoinColumn(name = "rol", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Roles roles;

    public AvisosPorRoles() {
    }

    public AvisosPorRoles(AvisosPorRolesPK avisosPorRolesPK) {
        this.avisosPorRolesPK = avisosPorRolesPK;
    }

    public AvisosPorRoles(int aviso, int rol) {
        this.avisosPorRolesPK = new AvisosPorRolesPK(aviso, rol);
    }

    public AvisosPorRolesPK getAvisosPorRolesPK() {
        return avisosPorRolesPK;
    }

    public void setAvisosPorRolesPK(AvisosPorRolesPK avisosPorRolesPK) {
        this.avisosPorRolesPK = avisosPorRolesPK;
    }

    public Avisos getAvisos() {
        return avisos;
    }

    public void setAvisos(Avisos avisos) {
        this.avisos = avisos;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (avisosPorRolesPK != null ? avisosPorRolesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AvisosPorRoles)) {
            return false;
        }
        AvisosPorRoles other = (AvisosPorRoles) object;
        if ((this.avisosPorRolesPK == null && other.avisosPorRolesPK != null) || (this.avisosPorRolesPK != null && !this.avisosPorRolesPK.equals(other.avisosPorRolesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.AvisosPorRoles[ avisosPorRolesPK=" + avisosPorRolesPK + " ]";
    }
    
}
