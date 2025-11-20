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
public class RolesPorUsuariosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "usuario")
    private int usuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rol")
    private int rol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "perfil")
    private int perfil;

    public RolesPorUsuariosPK() {
    }

    public RolesPorUsuariosPK(int usuario, int rol, int perfil) {
        this.usuario = usuario;
        this.rol = rol;
        this.perfil = perfil;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) usuario;
        hash += (int) rol;
        hash += (int) perfil;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolesPorUsuariosPK)) {
            return false;
        }
        RolesPorUsuariosPK other = (RolesPorUsuariosPK) object;
        if (this.usuario != other.usuario) {
            return false;
        }
        if (this.rol != other.rol) {
            return false;
        }
        if (this.perfil != other.perfil) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.RolesPorUsuariosPK[ usuario=" + usuario + ", rol=" + rol + ", perfil=" + perfil + " ]";
    }
    
}
