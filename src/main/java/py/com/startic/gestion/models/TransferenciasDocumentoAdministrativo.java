/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
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
@Table(name = "transferencias_documento_administrativo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransferenciasDocumentoAdministrativo.findAll", query = "SELECT m FROM TransferenciasDocumentoAdministrativo m")
    , @NamedQuery(name = "TransferenciasDocumentoAdministrativo.findByDocumentoAdministrativo", query = "SELECT m FROM TransferenciasDocumentoAdministrativo m WHERE m.documentoAdministrativo = :documentoAdministrativo ORDER BY m.fechaHoraAlta DESC, m.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "TransferenciasDocumentoAdministrativo.findByDocumentoAdministrativoANDEstados", query = "SELECT m FROM TransferenciasDocumentoAdministrativo m WHERE m.documentoAdministrativo = :documentoAdministrativo AND m.estado IN :estados ORDER BY m.fechaHoraAlta DESC, m.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "TransferenciasDocumentoAdministrativo.findById", query = "SELECT m FROM TransferenciasDocumentoAdministrativo m WHERE m.id = :id")
    , @NamedQuery(name = "TransferenciasDocumentoAdministrativo.findByFechaHoraAlta", query = "SELECT m FROM TransferenciasDocumentoAdministrativo m WHERE m.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "TransferenciasDocumentoAdministrativo.findByFechaHoraUltimoEstado", query = "SELECT m FROM TransferenciasDocumentoAdministrativo m WHERE m.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class TransferenciasDocumentoAdministrativo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
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
    @JoinColumn(name = "documento_administrativo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosAdministrativos documentoAdministrativo;
    @JoinColumn(name = "departamento_origen", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamentoOrigen;
    @JoinColumn(name = "departamento_destino", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamentoDestino;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "responsable_origen", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios responsableOrigen;
    @JoinColumn(name = "responsable_destino", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios responsableDestino;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "usuario_revisado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioRevisado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_revisado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraRevisado;
    @JoinColumn(name = "usuario_elaboracion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioElaboracion;
    @Basic(optional = true)
    @Column(name = "fecha_hora_elaboracion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraElaboracion;
    @JoinColumn(name = "usuario_borrado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioBorrado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_transferencia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraTransferencia;
    @Column(name = "revisado")
    private boolean revisado;
    @JoinColumn(name = "usuario_revision", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioRevision;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private EstadosTransferenciaDocumentoAdministrativo estado;
    @JoinColumn(name = "usuario_firmado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioFirmado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_firmado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraFirmado;
    @Column(name = "firmado")
    private boolean firmado;
    @JoinColumn(name = "usuario_firma", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioFirma;

    public TransferenciasDocumentoAdministrativo() {
    }

    public TransferenciasDocumentoAdministrativo(Integer id) {
        this.id = id;
    }

    public TransferenciasDocumentoAdministrativo(Integer id, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DocumentosAdministrativos getDocumentoAdministrativo() {
        return documentoAdministrativo;
    }

    public void setDocumentoAdministrativo(DocumentosAdministrativos documentoAdministrativo) {
        this.documentoAdministrativo = documentoAdministrativo;
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

    public Departamentos getDepartamentoOrigen() {
        return departamentoOrigen;
    }

    public void setDepartamentoOrigen(Departamentos departamentoOrigen) {
        this.departamentoOrigen = departamentoOrigen;
    }

    public Departamentos getDepartamentoDestino() {
        return departamentoDestino;
    }

    public void setDepartamentoDestino(Departamentos departamentoDestino) {
        this.departamentoDestino = departamentoDestino;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuarios getResponsableOrigen() {
        return responsableOrigen;
    }

    public void setResponsableOrigen(Usuarios responsableOrigen) {
        this.responsableOrigen = responsableOrigen;
    }

    public Usuarios getResponsableDestino() {
        return responsableDestino;
    }

    public void setResponsableDestino(Usuarios responsableDestino) {
        this.responsableDestino = responsableDestino;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public Usuarios getUsuarioRevisado() {
        return usuarioRevisado;
    }

    public void setUsuarioRevisado(Usuarios usuarioRevisado) {
        this.usuarioRevisado = usuarioRevisado;
    }

    public Date getFechaHoraRevisado() {
        return fechaHoraRevisado;
    }

    public void setFechaHoraRevisado(Date fechaHoraRevisado) {
        this.fechaHoraRevisado = fechaHoraRevisado;
    }

    public boolean isRevisado() {
        return revisado;
    }

    public void setRevisado(boolean revisado) {
        this.revisado = revisado;
    }

    public Usuarios getUsuarioRevision() {
        return usuarioRevision;
    }

    public void setUsuarioRevision(Usuarios usuarioRevision) {
        this.usuarioRevision = usuarioRevision;
    }

    public Date getFechaHoraTransferencia() {
        return fechaHoraTransferencia;
    }

    public void setFechaHoraTransferencia(Date fechaHoraTransferencia) {
        this.fechaHoraTransferencia = fechaHoraTransferencia;
    }

    public EstadosTransferenciaDocumentoAdministrativo getEstado() {
        return estado;
    }

    public void setEstado(EstadosTransferenciaDocumentoAdministrativo estado) {
        this.estado = estado;
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

    public Usuarios getUsuarioFirmado() {
        return usuarioFirmado;
    }

    public void setUsuarioFirmado(Usuarios usuarioFirmado) {
        this.usuarioFirmado = usuarioFirmado;
    }

    public Date getFechaHoraFirmado() {
        return fechaHoraFirmado;
    }

    public void setFechaHoraFirmado(Date fechaHoraFirmado) {
        this.fechaHoraFirmado = fechaHoraFirmado;
    }

    public boolean isFirmado() {
        return firmado;
    }

    public void setFirmado(boolean firmado) {
        this.firmado = firmado;
    }

    public Usuarios getUsuarioFirma() {
        return usuarioFirma;
    }

    public void setUsuarioFirma(Usuarios usuarioFirma) {
        this.usuarioFirma = usuarioFirma;
    }

    public Usuarios getUsuarioElaboracion() {
        return usuarioElaboracion;
    }

    public void setUsuarioElaboracion(Usuarios usuarioElaboracion) {
        this.usuarioElaboracion = usuarioElaboracion;
    }

    public Date getFechaHoraElaboracion() {
        return fechaHoraElaboracion;
    }

    public void setFechaHoraElaboracion(Date fechaHoraElaboracion) {
        this.fechaHoraElaboracion = fechaHoraElaboracion;
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
        if (!(object instanceof TransferenciasDocumentoAdministrativo)) {
            return false;
        }
        TransferenciasDocumentoAdministrativo other = (TransferenciasDocumentoAdministrativo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.TransferenciasDocumentoAdministrativo[ id=" + id + " ]";
    }
    
}
