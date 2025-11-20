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
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "subcategorias_documentos_judiciales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubcategoriasDocumentosJudiciales.findAll", query = "SELECT s FROM SubcategoriasDocumentosJudiciales s")
    , @NamedQuery(name = "SubcategoriasDocumentosJudiciales.findById", query = "SELECT s FROM SubcategoriasDocumentosJudiciales s WHERE s.id = :id")
    , @NamedQuery(name = "SubcategoriasDocumentosJudiciales.findByDescripcion", query = "SELECT s FROM SubcategoriasDocumentosJudiciales s WHERE s.descripcion = :descripcion")
    , @NamedQuery(name = "SubcategoriasDocumentosJudiciales.findByTipoDocumentoJudicial", query = "SELECT s FROM SubcategoriasDocumentosJudiciales s WHERE s.tipoDocumentoJudicial = :tipoDocumentoJudicial ORDER BY s.descripcion")
    , @NamedQuery(name = "SubcategoriasDocumentosJudiciales.findByTipoDocumentoJudicialEstado", query = "SELECT s FROM SubcategoriasDocumentosJudiciales s WHERE s.tipoDocumentoJudicial = :tipoDocumentoJudicial AND s.estado = :estado ORDER BY s.descripcion")
    , @NamedQuery(name = "SubcategoriasDocumentosJudiciales.findByFechaHoraAlta", query = "SELECT s FROM SubcategoriasDocumentosJudiciales s WHERE s.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "SubcategoriasDocumentosJudiciales.findByFechaHoraUltimoEstado", query = "SELECT s FROM SubcategoriasDocumentosJudiciales s WHERE s.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class SubcategoriasDocumentosJudiciales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "tipo_documento_judicial", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TiposDocumentosJudiciales tipoDocumentoJudicial;
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
    @Basic(optional = true)
    @Size(max = 2)
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "tipo_archivo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private TiposArchivoAdministrativo tipoArchivo;

    public SubcategoriasDocumentosJudiciales() {
    }

    public SubcategoriasDocumentosJudiciales(Integer id) {
        this.id = id;
    }

    public SubcategoriasDocumentosJudiciales(Integer id, String descripcion, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TiposDocumentosJudiciales getTipoDocumentoJudicial() {
        return tipoDocumentoJudicial;
    }

    public void setTipoDocumentoJudicial(TiposDocumentosJudiciales tipoDocumentoJudicial) {
        this.tipoDocumentoJudicial = tipoDocumentoJudicial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TiposArchivoAdministrativo getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(TiposArchivoAdministrativo tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
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
        if (!(object instanceof SubcategoriasDocumentosJudiciales)) {
            return false;
        }
        SubcategoriasDocumentosJudiciales other = (SubcategoriasDocumentosJudiciales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.SubcategoriasDocumentosJudiciales[ id=" + id + " ]";
    }
    
}
