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
@Table(name = "permisos_por_roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PermisosPorRoles.findAll", query = "SELECT p FROM PermisosPorRoles p")
    , @NamedQuery(name = "PermisosPorRoles.findByPermiso", query = "SELECT p FROM PermisosPorRoles p WHERE p.permisosPorRolesPK.permiso = :permiso")
    , @NamedQuery(name = "PermisosPorRoles.findByRol", query = "SELECT p FROM PermisosPorRoles p WHERE p.permisosPorRolesPK.rol = :rol")})
public class PermisosPorRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PermisosPorRolesPK permisosPorRolesPK;
    @JoinColumn(name = "permiso", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Permisos permisos;
    @JoinColumn(name = "rol", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Roles roles;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Estados estado;

    public PermisosPorRoles() {
    }

    public PermisosPorRoles(PermisosPorRolesPK permisosPorRolesPK) {
        this.permisosPorRolesPK = permisosPorRolesPK;
    }

    public PermisosPorRoles(int permiso, int rol) {
        this.permisosPorRolesPK = new PermisosPorRolesPK(permiso, rol);
    }

    public PermisosPorRolesPK getPermisosPorRolesPK() {
        return permisosPorRolesPK;
    }

    public void setPermisosPorRolesPK(PermisosPorRolesPK permisosPorRolesPK) {
        this.permisosPorRolesPK = permisosPorRolesPK;
    }

    public Permisos getPermisos() {
        return permisos;
    }

    public void setPermisos(Permisos permisos) {
        this.permisos = permisos;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (permisosPorRolesPK != null ? permisosPorRolesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermisosPorRoles)) {
            return false;
        }
        PermisosPorRoles other = (PermisosPorRoles) object;
        if ((this.permisosPorRolesPK == null && other.permisosPorRolesPK != null) || (this.permisosPorRolesPK != null && !this.permisosPorRolesPK.equals(other.permisosPorRolesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.PermisosPorRoles[ permisosPorRolesPK=" + permisosPorRolesPK + " ]";
    }
    
}
