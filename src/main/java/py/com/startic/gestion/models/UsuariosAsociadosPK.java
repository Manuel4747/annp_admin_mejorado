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
public class UsuariosAsociadosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "usuario")
    private int usuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usuario_asociado")
    private int usuarioAsociado;

    public UsuariosAsociadosPK() {
    }

    public UsuariosAsociadosPK(int usuario, int usuarioAsociado) {
        this.usuario = usuario;
        this.usuarioAsociado = usuarioAsociado;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public int getUsuarioAsociado() {
        return usuarioAsociado;
    }

    public void setUsuarioAsociado(int usuarioAsociado) {
        this.usuarioAsociado = usuarioAsociado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) usuario;
        hash += (int) usuarioAsociado;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuariosAsociadosPK)) {
            return false;
        }
        UsuariosAsociadosPK other = (UsuariosAsociadosPK) object;
        if (this.usuario != other.usuario) {
            return false;
        }
        if (this.usuarioAsociado != other.usuarioAsociado) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.UsuariosAsociadosPK[ usuario=" + usuario + ", usuarioAsociado=" + usuarioAsociado + " ]";
    }
    
}
