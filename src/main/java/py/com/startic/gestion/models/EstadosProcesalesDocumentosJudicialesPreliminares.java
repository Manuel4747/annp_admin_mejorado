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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "estados_procesales_documentos_judiciales_preliminares")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadosProcesalesDocumentosJudicialesPreliminares.findAll", query = "SELECT e FROM EstadosProcesalesDocumentosJudicialesPreliminares e")
    , @NamedQuery(name = "EstadosProcesalesDocumentosJudicialesPreliminares.findById", query = "SELECT e FROM EstadosProcesalesDocumentosJudicialesPreliminares e WHERE e.id = :id")
    , @NamedQuery(name = "EstadosProcesalesDocumentosJudicialesPreliminares.findByDocumentoJudicialPreliminar", query = "SELECT e FROM EstadosProcesalesDocumentosJudicialesPreliminares e WHERE e.documentoJudicialPreliminar = :documentoJudicialPreliminar ORDER BY e.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "EstadosProcesalesDocumentosJudicialesPreliminares.findByFechaHoraAlta", query = "SELECT e FROM EstadosProcesalesDocumentosJudicialesPreliminares e WHERE e.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "EstadosProcesalesDocumentosJudicialesPreliminares.findByFechaHoraUltimoEstado", query = "SELECT e FROM EstadosProcesalesDocumentosJudicialesPreliminares e WHERE e.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class EstadosProcesalesDocumentosJudicialesPreliminares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "documento_judicial_preliminar", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DocumentosJudicialesPreliminares documentoJudicialPreliminar;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "estado_procesal")
    private String estadoProcesal;
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
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;

    public EstadosProcesalesDocumentosJudicialesPreliminares() {
    }

    public EstadosProcesalesDocumentosJudicialesPreliminares(Integer id) {
        this.id = id;
    }

    public EstadosProcesalesDocumentosJudicialesPreliminares(Integer id, String estadoProcesal, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.estadoProcesal = estadoProcesal;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DocumentosJudicialesPreliminares getDocumentoJudicialPreliminar() {
        return documentoJudicialPreliminar;
    }

    public void setDocumentoJudicialPreliminar(DocumentosJudicialesPreliminares documentoJudicialPreliminar) {
        this.documentoJudicialPreliminar = documentoJudicialPreliminar;
    }

    public String getEstadoProcesal() {
        return estadoProcesal;
    }

    public void setEstadoProcesal(String estadoProcesal) {
        this.estadoProcesal = estadoProcesal;
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

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
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
        if (!(object instanceof EstadosProcesalesDocumentosJudicialesPreliminares)) {
            return false;
        }
        EstadosProcesalesDocumentosJudicialesPreliminares other = (EstadosProcesalesDocumentosJudicialesPreliminares) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.EstadosProcesalesDocumentosJudicialesPreliminares[ id=" + id + " ]";
    }
    
}
