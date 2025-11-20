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
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "vistos_por_documentos_administrativos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VistosPorDocumentosAdministrativos.findAll", query = "SELECT d FROM VistosPorDocumentosAdministrativos d")
    , @NamedQuery(name = "VistosPorDocumentosAdministrativos.findById", query = "SELECT d FROM VistosPorDocumentosAdministrativos d WHERE d.id = :id")
    , @NamedQuery(name = "VistosPorDocumentosAdministrativos.findByUsuarioDocumentoAdministrativo", query = "SELECT d FROM VistosPorDocumentosAdministrativos d WHERE d.usuario = :usuario and d.documentoAdministrativo = :documentoAdministrativo")
    , @NamedQuery(name = "VistosPorDocumentosAdministrativos.findByDepartamentoDocumentoAdministrativo", query = "SELECT d FROM VistosPorDocumentosAdministrativos d WHERE d.departamento = :departamento and d.documentoAdministrativo = :documentoAdministrativo")})
public class VistosPorDocumentosAdministrativos implements Serializable {

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
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @Basic(optional = false)
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;

    public VistosPorDocumentosAdministrativos() {
    }

    public VistosPorDocumentosAdministrativos(Integer id) {
        this.id = id;
    }

    public VistosPorDocumentosAdministrativos(Integer id, Usuarios usuario, DocumentosAdministrativos documentoAdministrativo, Date fechaHoraAlta, Departamentos departamento) {
        this.id = id;
        this.usuario = usuario;
        this.documentoAdministrativo = documentoAdministrativo;
        this.fechaHoraAlta = fechaHoraAlta;
        this.departamento = departamento;
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

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
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
        if (!(object instanceof VistosPorDocumentosAdministrativos)) {
            return false;
        }
        VistosPorDocumentosAdministrativos other = (VistosPorDocumentosAdministrativos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.VistosPorDocumentosAdministrativos[ id=" + id + " ]";
    }
    
}
