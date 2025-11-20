/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "observaciones_documentos_judiciales_preliminares")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObservacionesDocumentosJudicialesPreliminares.findAll", query = "SELECT o FROM ObservacionesDocumentosJudicialesPreliminares o")
    , @NamedQuery(name = "ObservacionesDocumentosJudicialesPreliminares.findOrdered", query = "SELECT o FROM ObservacionesDocumentosJudicialesPreliminares o ORDER BY o.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "ObservacionesDocumentosJudicialesPreliminares.findById", query = "SELECT o FROM ObservacionesDocumentosJudicialesPreliminares o WHERE o.id = :id")
    , @NamedQuery(name = "ObservacionesDocumentosJudicialesPreliminares.findByDocumentoJudicialPreliminar", query = "SELECT o FROM ObservacionesDocumentosJudicialesPreliminares o WHERE o.documentoJudicialPreliminar = :documentoJudicialPreliminar ORDER BY o.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "ObservacionesDocumentosJudicialesPreliminares.findByObservacion", query = "SELECT o FROM ObservacionesDocumentosJudicialesPreliminares o WHERE o.observacion = :observacion")
    , @NamedQuery(name = "ObservacionesDocumentosJudicialesPreliminares.findByFechaHoraAlta", query = "SELECT o FROM ObservacionesDocumentosJudicialesPreliminares o WHERE o.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "ObservacionesDocumentosJudicialesPreliminares.findByFechaHoraUltimoEstado", query = "SELECT o FROM ObservacionesDocumentosJudicialesPreliminares o WHERE o.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class ObservacionesDocumentosJudicialesPreliminares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Size(max = 65535)
    @Column(name = "observacion")
    private String observacion;
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
    @JoinColumn(name = "documento_judicial_preliminar", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DocumentosJudicialesPreliminares documentoJudicialPreliminar;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "observacionDocumentoJudicialPreliminar")
    private Collection<DocumentosJudicialesPreliminares> documentosJudicialesPreliminaresCollection;

    public ObservacionesDocumentosJudicialesPreliminares() {
    }

    public ObservacionesDocumentosJudicialesPreliminares(Integer id) {
        this.id = id;
    }

    public ObservacionesDocumentosJudicialesPreliminares(Integer id, String observacion, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.observacion = observacion;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObservacion() {
        return observacion;
    }

    @XmlTransient
    public String getObservacionString() {
        return observacion.replace("\n", "<br />");
    }
    
    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public DocumentosJudicialesPreliminares getDocumentoJudicialPreliminar() {
        return documentoJudicialPreliminar;
    }

    public void setDocumentoJudicialPreliminar(DocumentosJudicialesPreliminares documentoJudicialPreliminar) {
        this.documentoJudicialPreliminar = documentoJudicialPreliminar;
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

    @XmlTransient
    public Collection<DocumentosJudicialesPreliminares> getDocumentosJudicialesPreliminaresCollection() {
        return documentosJudicialesPreliminaresCollection;
    }

    public void setDocumentosJudicialesCollection(Collection<DocumentosJudicialesPreliminares> documentosJudicialesPreliminaresCollection) {
        this.documentosJudicialesPreliminaresCollection = documentosJudicialesPreliminaresCollection;
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
        if (!(object instanceof ObservacionesDocumentosJudicialesPreliminares)) {
            return false;
        }
        ObservacionesDocumentosJudicialesPreliminares other = (ObservacionesDocumentosJudicialesPreliminares) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.ObservacionesDocumentosJudicialesPreliminares[ id=" + id + " ]";
    }
    
}
