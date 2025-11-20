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
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "usuarios_por_documentos_administrativos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuariosPorDocumentosAdministrativos.findAll", query = "SELECT c FROM UsuariosPorDocumentosAdministrativos c")
    , @NamedQuery(name = "UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativoANDTransferirNULL", query = "SELECT c FROM UsuariosPorDocumentosAdministrativos c WHERE c.documentoAdministrativo = :documentoAdministrativo AND c.transferenciaDocumentoAdministrativo IS NULL")
    , @NamedQuery(name = "UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativo", query = "SELECT c FROM UsuariosPorDocumentosAdministrativos c WHERE c.documentoAdministrativo = :documentoAdministrativo ORDER BY c.tipoEnvio.codigo, c.id")
    , @NamedQuery(name = "UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativoANDTipoEnvio", query = "SELECT c FROM UsuariosPorDocumentosAdministrativos c WHERE c.documentoAdministrativo = :documentoAdministrativo AND c.tipoEnvio = :tipoEnvio ORDER BY c.tipoEnvio.codigo, c.id")
    , @NamedQuery(name = "UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativo", query = "SELECT c FROM UsuariosPorDocumentosAdministrativos c WHERE c.transferenciaDocumentoAdministrativo = :transferenciaDocumentoAdministrativo ORDER BY c.tipoEnvio.codigo, c.id")
    , @NamedQuery(name = "UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", query = "SELECT c FROM UsuariosPorDocumentosAdministrativos c WHERE c.transferenciaDocumentoAdministrativo = :transferenciaDocumentoAdministrativo AND c.tipoEnvio = :tipoEnvio")
    , @NamedQuery(name = "UsuariosPorDocumentosAdministrativos.findById", query = "SELECT c FROM UsuariosPorDocumentosAdministrativos c WHERE c.id = :id")})
public class UsuariosPorDocumentosAdministrativos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuario;
    @JoinColumn(name = "documento_administrativo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosAdministrativos documentoAdministrativo;
    @JoinColumn(name = "tipo_envio", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TiposEnvio tipoEnvio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "usuario_borrado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioBorrado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @JoinColumn(name = "transferencia_documento_administrativo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TransferenciasDocumentoAdministrativo transferenciaDocumentoAdministrativo;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @Column(name = "incluido_automaticamente")
    private boolean incluidoAutomaticamente;

    public UsuariosPorDocumentosAdministrativos() {
    }

    public UsuariosPorDocumentosAdministrativos(Integer id) {
        this.id = id;
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

    public DocumentosAdministrativos getDocumentoAdministrativo() {
        return documentoAdministrativo;
    }

    public void setDocumentoAdministrativo(DocumentosAdministrativos documentoAdministrativo) {
        this.documentoAdministrativo = documentoAdministrativo;
    }

    public TiposEnvio getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(TiposEnvio tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public TransferenciasDocumentoAdministrativo getTransferenciaDocumentoAdministrativo() {
        return transferenciaDocumentoAdministrativo;
    }

    public void setTransferenciaDocumentoAdministrativo(TransferenciasDocumentoAdministrativo transferenciaDocumentoAdministrativo) {
        this.transferenciaDocumentoAdministrativo = transferenciaDocumentoAdministrativo;
    }

    public Usuarios getUsuarioBorrado() {
        return usuarioBorrado;
    }

    public void setUsuarioBorrado(Usuarios usuarioBorrado) {
        this.usuarioBorrado = usuarioBorrado;
    }

    public Date getFechaHoraBorrado() {
        return fechaHoraBorrado;
    }

    public void setFechaHoraBorrado(Date fechaHoraBorrado) {
        this.fechaHoraBorrado = fechaHoraBorrado;
    }

    public boolean isIncluidoAutomaticamente() {
        return incluidoAutomaticamente;
    }

    public void setIncluidoAutomaticamente(boolean incluidoAutomaticamente) {
        this.incluidoAutomaticamente = incluidoAutomaticamente;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
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
        if (!(object instanceof UsuariosPorDocumentosAdministrativos)) {
            return false;
        }
        UsuariosPorDocumentosAdministrativos other = (UsuariosPorDocumentosAdministrativos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.UsuariosPorDocumentosAdministrativos[ id=" + id + " ]";
    }
    
}
