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
@Table(name = "usuarios_asociados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuariosAsociados.findAll", query = "SELECT p FROM UsuariosAsociados p")
    , @NamedQuery(name = "UsuariosAsociados.findByUsuario", query = "SELECT p FROM UsuariosAsociados p WHERE p.usuariosAsociadosPK.usuario = :usuario")
    , @NamedQuery(name = "UsuariosAsociados.findByUsuarioAsociado", query = "SELECT p FROM UsuariosAsociados p WHERE p.usuariosAsociadosPK.usuarioAsociado = :usuarioAsociado AND p.usuario.estado.codigo = 'AC'")})
public class UsuariosAsociados implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsuariosAsociadosPK usuariosAsociadosPK;
    @JoinColumn(name = "usuario", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuarios usuario;
    @JoinColumn(name = "usuario_asociado", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuarios usuarioAsociado;

    public UsuariosAsociados() {
    }

    public UsuariosAsociados(UsuariosAsociadosPK usuariosAsociadosPK) {
        this.usuariosAsociadosPK = usuariosAsociadosPK;
    }

    public UsuariosAsociados(int persona, int usuarioAsociado) {
        this.usuariosAsociadosPK = new UsuariosAsociadosPK(persona, usuarioAsociado);
    }

    public UsuariosAsociadosPK getUsuariosAsociadosPK() {
        return usuariosAsociadosPK;
    }

    public void setUsuariosAsociadosPK(UsuariosAsociadosPK usuariosAsociadosPK) {
        this.usuariosAsociadosPK = usuariosAsociadosPK;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Usuarios getUsuarioAsociado() {
        return usuarioAsociado;
    }

    public void setUsuarioAsociado(Usuarios usuarioAsociado) {
        this.usuarioAsociado = usuarioAsociado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuariosAsociadosPK != null ? usuariosAsociadosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuariosAsociados)) {
            return false;
        }
        UsuariosAsociados other = (UsuariosAsociados) object;
        if ((this.usuariosAsociadosPK == null && other.usuariosAsociadosPK != null) || (this.usuariosAsociadosPK != null && !this.usuariosAsociadosPK.equals(other.usuariosAsociadosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.UsuariosAsociados[ usuariosAsociadasPK=" + usuariosAsociadosPK + " ]";
    }
    
}
