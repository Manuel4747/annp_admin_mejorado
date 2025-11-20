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
@Table(name = "documentos_administrativos_autoguardados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentosAdministrativosAutoguardados.findAll", query = "SELECT d FROM DocumentosAdministrativosAutoguardados d")
    , @NamedQuery(name = "DocumentosAdministrativosAutoguardados.deleteByUsuarioAlta", query = "DELETE FROM DocumentosAdministrativosAutoguardados d WHERE d.usuarioAlta = :usuarioAlta")
    , @NamedQuery(name = "DocumentosAdministrativosAutoguardados.deleteByDocumentoAdministrativoOriginal", query = "DELETE FROM DocumentosAdministrativosAutoguardados d WHERE d.documentoAdministrativoOriginal = :documentoAdministrativoOriginal")
    , @NamedQuery(name = "DocumentosAdministrativosAutoguardados.findByUsuarioAlta", query = "SELECT d FROM DocumentosAdministrativosAutoguardados d WHERE d.usuarioAlta = :usuarioAlta")
    , @NamedQuery(name = "DocumentosAdministrativosAutoguardados.findById", query = "SELECT d FROM DocumentosAdministrativosAutoguardados d WHERE d.id = :id")})
public class DocumentosAdministrativosAutoguardados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "subcategoria_documento_administrativo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private SubcategoriasDocumentosAdministrativos subcategoriaDocumentoAdministrativo;
    @Basic(optional = true)
    @Column(name = "responder_a")
    private String responderA;
    @JoinColumn(name = "formato", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private FormatosArchivoAdministrativo formato;
    @ManyToOne(optional = true)
    @JoinColumn(name = "tipo_prioridad", referencedColumnName = "id")
    private TiposPrioridad tipoPrioridad;
    @Basic(optional = true)
    @Column(name = "asunto")
    private String asunto;
    @Basic(optional = true)
    @Column(name = "texto")
    private String texto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @ManyToOne(optional = true)
    @JoinColumn(name = "documento_administrativo_original", referencedColumnName = "id")
    private DocumentosAdministrativos documentoAdministrativoOriginal;
    @ManyToOne(optional = true)
    @JoinColumn(name = "documento_administrativo_padre", referencedColumnName = "id")
    private DocumentosAdministrativos documentoAdministrativoPadre;
    @Column(name = "responder")
    private boolean responder;

    public DocumentosAdministrativosAutoguardados() {
    }

    public DocumentosAdministrativosAutoguardados(SubcategoriasDocumentosAdministrativos subcategoriaDocumentoAdministrativo, String responderA, FormatosArchivoAdministrativo formato, TiposPrioridad tipoPrioridad, String asunto, String texto, Date fechaHoraAlta, Usuarios usuarioAlta, DocumentosAdministrativos documentoAdministrativoOriginal, DocumentosAdministrativos documentoAdministrativoPadre, boolean responder){
        
        this.subcategoriaDocumentoAdministrativo = subcategoriaDocumentoAdministrativo; 
        this.responderA = responderA; 
        this.formato = formato;
        this.tipoPrioridad = tipoPrioridad;
        this.asunto = asunto;
        this.texto = texto;
        this.fechaHoraAlta = fechaHoraAlta;
        this.usuarioAlta = usuarioAlta;
        this.documentoAdministrativoOriginal = documentoAdministrativoOriginal;
        this.documentoAdministrativoPadre = documentoAdministrativoPadre;
        this.responder = responder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SubcategoriasDocumentosAdministrativos getSubcategoriaDocumentoAdministrativo() {
        return subcategoriaDocumentoAdministrativo;
    }

    public void setSubcategoriaDocumentoAdministrativo(SubcategoriasDocumentosAdministrativos subcategoriaDocumentoAdministrativo) {
        this.subcategoriaDocumentoAdministrativo = subcategoriaDocumentoAdministrativo;
    }

    public String getResponderA() {
        return responderA;
    }

    public void setResponderA(String responderA) {
        this.responderA = responderA;
    }

    public FormatosArchivoAdministrativo getFormato() {
        return formato;
    }

    public void setFormato(FormatosArchivoAdministrativo formato) {
        this.formato = formato;
    }

    public TiposPrioridad getTipoPrioridad() {
        return tipoPrioridad;
    }

    public void setTipoPrioridad(TiposPrioridad tipoPrioridad) {
        this.tipoPrioridad = tipoPrioridad;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public DocumentosAdministrativos getDocumentoAdministrativoOriginal() {
        return documentoAdministrativoOriginal;
    }

    public void setDocumentoAdministrativoOriginal(DocumentosAdministrativos documentoAdministrativoOriginal) {
        this.documentoAdministrativoOriginal = documentoAdministrativoOriginal;
    }

    public DocumentosAdministrativos getDocumentoAdministrativoPadre() {
        return documentoAdministrativoPadre;
    }

    public void setDocumentoAdministrativoPadre(DocumentosAdministrativos documentoAdministrativoPadre) {
        this.documentoAdministrativoPadre = documentoAdministrativoPadre;
    }

    public boolean isResponder() {
        return responder;
    }

    public void setResponder(boolean responder) {
        this.responder = responder;
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
        if (!(object instanceof DocumentosAdministrativosAutoguardados)) {
            return false;
        }
        DocumentosAdministrativosAutoguardados other = (DocumentosAdministrativosAutoguardados) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return asunto;
    }
    
}
