/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
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
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "usuarios_por_documentos_administrativos_autoguardados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuariosPorDocumentosAdministrativosAutoguardados.findAll", query = "SELECT c FROM UsuariosPorDocumentosAdministrativosAutoguardados c")
    , @NamedQuery(name = "UsuariosPorDocumentosAdministrativosAutoguardados.deleteByUsuarioAlta", query = "DELETE FROM UsuariosPorDocumentosAdministrativosAutoguardados c WHERE c.usuarioAlta = :usuarioAlta")
    , @NamedQuery(name = "UsuariosPorDocumentosAdministrativosAutoguardados.deleteByDocumentoAdministrativoOriginal", query = "DELETE FROM UsuariosPorDocumentosAdministrativosAutoguardados c WHERE c.documentoAdministrativoAutoguardado in (select d from DocumentosAdministrativosAutoguardados d WHERE d.documentoAdministrativoOriginal = :documentoAdministrativoOriginal)")
    , @NamedQuery(name = "UsuariosPorDocumentosAdministrativosAutoguardados.findByDocumentoAdministrativoAutoguardadoUsuarioAltaTipoEnvio", query = "SELECT c FROM UsuariosPorDocumentosAdministrativosAutoguardados c WHERE c.documentoAdministrativoAutoguardado = :documentoAdministrativoAutoguardado AND c.tipoEnvio = :tipoEnvio AND c.usuarioAlta = :usuarioAlta")
    , @NamedQuery(name = "UsuariosPorDocumentosAdministrativosAutoguardados.deleteByDocumentoAdministrativoAutoguardadoUsuarioAltaTipoEnvio", query = "DELETE FROM UsuariosPorDocumentosAdministrativosAutoguardados c WHERE c.documentoAdministrativoAutoguardado = :documentoAdministrativoAutoguardado AND c.tipoEnvio = :tipoEnvio AND c.usuarioAlta = :usuarioAlta")
    , @NamedQuery(name = "UsuariosPorDocumentosAdministrativosAutoguardados.findByDocumentoAdministrativoAutoguardado", query = "SELECT c FROM UsuariosPorDocumentosAdministrativosAutoguardados c WHERE c.documentoAdministrativoAutoguardado = :documentoAdministrativoAutoguardado")
    , @NamedQuery(name = "UsuariosPorDocumentosAdministrativosAutoguardados.findById", query = "SELECT c FROM UsuariosPorDocumentosAdministrativosAutoguardados c WHERE c.id = :id")})
public class UsuariosPorDocumentosAdministrativosAutoguardados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "documento_administrativo_autoguardado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosAdministrativosAutoguardados documentoAdministrativoAutoguardado;
    @JoinColumn(name = "tipo_envio", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TiposEnvio tipoEnvio;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuario;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;

    public UsuariosPorDocumentosAdministrativosAutoguardados() {
    }

    public UsuariosPorDocumentosAdministrativosAutoguardados(DocumentosAdministrativosAutoguardados documentoAdministrativoAutoguardado, TiposEnvio tipoEnvio, Usuarios usuario, Usuarios usuarioAlta) {
        this.documentoAdministrativoAutoguardado = documentoAdministrativoAutoguardado;
        this.tipoEnvio = tipoEnvio;
        this.usuario = usuario;
        this.usuarioAlta = usuarioAlta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public DocumentosAdministrativosAutoguardados getDocumentoAdministrativoAutoguardado() {
        return documentoAdministrativoAutoguardado;
    }

    public void setDocumentoAdministrativoAutoguardado(DocumentosAdministrativosAutoguardados documentoAdministrativoAutoguardado) {
        this.documentoAdministrativoAutoguardado = documentoAdministrativoAutoguardado;
    }

    public TiposEnvio getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(TiposEnvio tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
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
        if (!(object instanceof UsuariosPorDocumentosAdministrativosAutoguardados)) {
            return false;
        }
        UsuariosPorDocumentosAdministrativosAutoguardados other = (UsuariosPorDocumentosAdministrativosAutoguardados) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.UsuariosPorDocumentosAdministrativosAutoguardados[ id=" + id + " ]";
    }
    
}
