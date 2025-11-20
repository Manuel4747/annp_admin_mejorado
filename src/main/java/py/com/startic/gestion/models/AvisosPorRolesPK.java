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
public class AvisosPorRolesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "aviso")
    private int aviso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rol")
    private int rol;

    public AvisosPorRolesPK() {
    }

    public AvisosPorRolesPK(int aviso, int rol) {
        this.aviso = aviso;
        this.rol = rol;
    }

    public int getPermiso() {
        return aviso;
    }

    public void setPermiso(int aviso) {
        this.aviso = aviso;
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
        hash += (int) aviso;
        hash += (int) rol;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AvisosPorRolesPK)) {
            return false;
        }
        AvisosPorRolesPK other = (AvisosPorRolesPK) object;
        if (this.aviso != other.aviso) {
            return false;
        }
        if (this.rol != other.rol) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.AvisosPorRolesPK[ aviso=" + aviso + ", rol=" + rol + " ]";
    }
    
}
