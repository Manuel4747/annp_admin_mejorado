/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author eduardo
 */
@Embeddable
public class PermisosPorRolesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "permiso")
    private int permiso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rol")
    private int rol;

    public PermisosPorRolesPK() {
    }

    public PermisosPorRolesPK(int permiso, int rol) {
        this.permiso = permiso;
        this.rol = rol;
    }

    public int getPermiso() {
        return permiso;
    }

    public void setPermiso(int permiso) {
        this.permiso = permiso;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) permiso;
        hash += (int) rol;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermisosPorRolesPK)) {
            return false;
        }
        PermisosPorRolesPK other = (PermisosPorRolesPK) object;
        if (this.permiso != other.permiso) {
            return false;
        }
        if (this.rol != other.rol) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.PermisosPorRolesPK[ permiso=" + permiso + ", rol=" + rol + " ]";
    }
    
}
