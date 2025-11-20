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
@Table(name = "flujos_documento_administrativo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlujosDocumentoAdministrativo.findAll", query = "SELECT f FROM FlujosDocumentoAdministrativo f")
    , @NamedQuery(name = "FlujosDocumentoAdministrativo.findByEstadoDocumentoActual", query = "SELECT f FROM FlujosDocumentoAdministrativo f WHERE f.flujosDocumentoAdministrativoPK.estadoDocumentoActual = :estadoDocumentoActual AND f.flujosDocumentoAdministrativoPK.tipoDocumentoAdministrativo = :tipoDocumento AND f.flujosDocumentoAdministrativoPK.rolFinal = 0")
    , @NamedQuery(name = "FlujosDocumentoAdministrativo.findByEstadoDocumentoActualFin", query = "SELECT f FROM FlujosDocumentoAdministrativo f WHERE f.flujosDocumentoAdministrativoPK.estadoDocumentoActual = :estadoDocumentoActual AND f.flujosDocumentoAdministrativoPK.tipoDocumentoAdministrativo = :tipoDocumento AND f.flujosDocumentoAdministrativoPK.rolFinal = -999999")
    , @NamedQuery(name = "FlujosDocumentoAdministrativo.findSgteEstado", query = "SELECT f FROM FlujosDocumentoAdministrativo f WHERE f.flujosDocumentoAdministrativoPK.estadoDocumentoActual = :estadoDocumentoActual AND f.flujosDocumentoAdministrativoPK.tipoDocumentoAdministrativo = :tipoDocumento")
    , @NamedQuery(name = "FlujosDocumentoAdministrativo.findSgteEstadoSegunRol", query = "SELECT f FROM FlujosDocumentoAdministrativo f WHERE f.flujosDocumentoAdministrativoPK.estadoDocumentoActual = :estadoDocumentoActual AND f.flujosDocumentoAdministrativoPK.tipoDocumentoAdministrativo = :tipoDocumento AND f.rolFinal = :rolFinal")
    , @NamedQuery(name = "FlujosDocumentoAdministrativo.findByTipoDocumento", query = "SELECT f FROM FlujosDocumentoAdministrativo f WHERE f.flujosDocumentoAdministrativoPK.tipoDocumentoAdministrativo = :tipoDocumento")
    , @NamedQuery(name = "FlujosDocumentoAdministrativo.findByEmpresa", query = "SELECT f FROM FlujosDocumentoAdministrativo f WHERE f.empresa = :empresa")})
public class FlujosDocumentoAdministrativo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FlujosDocumentoAdministrativoPK flujosDocumentoAdministrativoPK;
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
    @JoinColumn(name = "tipo_documento_administrativo", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TiposDocumentosAdministrativos tipoDocumentoAdministrativo;

    public FlujosDocumentoAdministrativo() {
    }

    public FlujosDocumentoAdministrativo(FlujosDocumentoAdministrativoPK flujosDocumentoAdministrativoPK) {
        this.flujosDocumentoAdministrativoPK = flujosDocumentoAdministrativoPK;
    }

    public FlujosDocumentoAdministrativo(FlujosDocumentoAdministrativoPK flujosDocumentoAdministrativoPK, int empresa) {
        this.flujosDocumentoAdministrativoPK = flujosDocumentoAdministrativoPK;
        this.empresa = empresa;
    }

    public FlujosDocumentoAdministrativo(String estadoDocumentoActual, String tipoDocumento, Integer rolFinal) {
        this.flujosDocumentoAdministrativoPK = new FlujosDocumentoAdministrativoPK(estadoDocumentoActual, tipoDocumento, rolFinal);
    }

    public FlujosDocumentoAdministrativoPK getFlujosDocumentoAdministrativoPK() {
        return flujosDocumentoAdministrativoPK;
    }

    public void setFlujosDocumentoAdministrativoPK(FlujosDocumentoAdministrativoPK flujosDocumentoAdministrativoPK) {
        this.flujosDocumentoAdministrativoPK = flujosDocumentoAdministrativoPK;
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

    public TiposDocumentosAdministrativos getTipoDocumentoAdministrativo() {
        return tipoDocumentoAdministrativo;
    }

    public void setTipoDocumentoAdministrativo(TiposDocumentosAdministrativos tipoDocumentoAdministrativo) {
        this.tipoDocumentoAdministrativo = tipoDocumentoAdministrativo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flujosDocumentoAdministrativoPK != null ? flujosDocumentoAdministrativoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FlujosDocumentoAdministrativo)) {
            return false;
        }
        FlujosDocumentoAdministrativo other = (FlujosDocumentoAdministrativo) object;
        if ((this.flujosDocumentoAdministrativoPK == null && other.flujosDocumentoAdministrativoPK != null) || (this.flujosDocumentoAdministrativoPK != null && !this.flujosDocumentoAdministrativoPK.equals(other.flujosDocumentoAdministrativoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.FlujosDocumentoAdministrativo[ flujosDocumentoAdministrativoPK=" + flujosDocumentoAdministrativoPK + " ]";
    }
    
}
