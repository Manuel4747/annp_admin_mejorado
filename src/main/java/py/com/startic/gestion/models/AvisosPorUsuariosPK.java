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
public class AvisosPorUsuariosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "usuario")
    private int usuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aviso")
    private int aviso;

    public AvisosPorUsuariosPK() {
    }

    public AvisosPorUsuariosPK(int usuario, int aviso) {
        this.usuario = usuario;
        this.aviso = aviso;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public int getAviso() {
        return aviso;
    }

    public void setAviso(int aviso) {
        this.aviso = aviso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) usuario;
        hash += (int) aviso;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AvisosPorUsuariosPK)) {
            return false;
        }
        AvisosPorUsuariosPK other = (AvisosPorUsuariosPK) object;
        if (this.usuario != other.usuario) {
            return false;
        }
        if (this.aviso != other.aviso) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.AvisosPorUsuariosPK[ usuario=" + usuario + ", aviso=" + aviso + " ]";
    }
    
}
