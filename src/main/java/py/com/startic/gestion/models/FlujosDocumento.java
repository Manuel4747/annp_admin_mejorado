/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "flujos_documento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlujosDocumento.findAll", query = "SELECT f FROM FlujosDocumento f")
    , @NamedQuery(name = "FlujosDocumento.findByEstadoDocumentoActual", query = "SELECT f FROM FlujosDocumento f WHERE f.flujosDocumentoPK.estadoDocumentoActual = :estadoDocumentoActual AND f.flujosDocumentoPK.tipoDocumento = :tipoDocumento AND f.flujosDocumentoPK.rolFinal = 0")
    , @NamedQuery(name = "FlujosDocumento.findByEstadoDocumentoActualFin", query = "SELECT f FROM FlujosDocumento f WHERE f.flujosDocumentoPK.estadoDocumentoActual = :estadoDocumentoActual AND f.flujosDocumentoPK.tipoDocumento = :tipoDocumento AND f.flujosDocumentoPK.rolFinal = -999999")
    , @NamedQuery(name = "FlujosDocumento.findSgteEstado", query = "SELECT f FROM FlujosDocumento f WHERE f.flujosDocumentoPK.estadoDocumentoActual = :estadoDocumentoActual AND f.flujosDocumentoPK.tipoDocumento = :tipoDocumento")
    , @NamedQuery(name = "FlujosDocumento.findSgteEstadoSegunRol", query = "SELECT f FROM FlujosDocumento f WHERE f.flujosDocumentoPK.estadoDocumentoActual = :estadoDocumentoActual AND f.flujosDocumentoPK.tipoDocumento = :tipoDocumento AND f.rolFinal = :rolFinal")
    , @NamedQuery(name = "FlujosDocumento.findByTipoDocumento", query = "SELECT f FROM FlujosDocumento f WHERE f.flujosDocumentoPK.tipoDocumento = :tipoDocumento")
    , @NamedQuery(name = "FlujosDocumento.findByEmpresa", query = "SELECT f FROM FlujosDocumento f WHERE f.empresa = :empresa")})
public class FlujosDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FlujosDocumentoPK flujosDocumentoPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "empresa")
    private int empresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado_documento_actual")
    private String estadosDocumento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado_documento_final")
    private String estadoDocumentoFinal;
    @JoinColumn(name = "rol_final", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Roles rolFinal;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TiposDocumentosJudiciales tiposDocumentosJudiciales;

    public FlujosDocumento() {
    }

    public FlujosDocumento(FlujosDocumentoPK flujosDocumentoPK) {
        this.flujosDocumentoPK = flujosDocumentoPK;
    }

    public FlujosDocumento(FlujosDocumentoPK flujosDocumentoPK, int empresa) {
        this.flujosDocumentoPK = flujosDocumentoPK;
        this.empresa = empresa;
    }

    public FlujosDocumento(String estadoDocumentoActual, String tipoDocumento, Integer rolFinal) {
        this.flujosDocumentoPK = new FlujosDocumentoPK(estadoDocumentoActual, tipoDocumento, rolFinal);
    }

    public FlujosDocumentoPK getFlujosDocumentoPK() {
        return flujosDocumentoPK;
    }

    public void setFlujosDocumentoPK(FlujosDocumentoPK flujosDocumentoPK) {
        this.flujosDocumentoPK = flujosDocumentoPK;
    }

    public int getEmpresa() {
        return empresa;
    }
    
    public void setEmpresa(int empresa) {
        this.empresa = empresa;
    }

    public String getEstadosDocumento() {
        return estadosDocumento;
    }

    public void setEstadosDocumento(String estadosDocumento) {
        this.estadosDocumento = estadosDocumento;
    }

    public String getEstadoDocumentoFinal() {
        return estadoDocumentoFinal;
    }

    public void setEstadoDocumentoFinal(String estadoDocumentoFinal) {
        this.estadoDocumentoFinal = estadoDocumentoFinal;
    }

    public Roles getRolFinal() {
        return rolFinal;
    }

    public void setRolFinal(Roles rolFinal) {
        this.rolFinal = rolFinal;
    }

    public TiposDocumentosJudiciales getTiposDocumentosJudiciales() {
        return tiposDocumentosJudiciales;
    }

    public void setTiposDocumentosJudiciales(TiposDocumentosJudiciales tiposDocumentosJudiciales) {
        this.tiposDocumentosJudiciales = tiposDocumentosJudiciales;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flujosDocumentoPK != null ? flujosDocumentoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FlujosDocumento)) {
            return false;
        }
        FlujosDocumento other = (FlujosDocumento) object;
        if ((this.flujosDocumentoPK == null && other.flujosDocumentoPK != null) || (this.flujosDocumentoPK != null && !this.flujosDocumentoPK.equals(other.flujosDocumentoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models13.FlujosDocumento[ flujosDocumentoPK=" + flujosDocumentoPK + " ]";
    }
    
}
