/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 * @author eduardo
 */
@Embeddable
public class FlujosDocumentoAdministrativoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "estado_documento_actual")
    private String estadoDocumentoActual;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tipo_documento_administrativo")
    private String tipoDocumentoAdministrativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rol_final")
    private Integer rolFinal;

    public FlujosDocumentoAdministrativoPK() {
    }

    public FlujosDocumentoAdministrativoPK(String estadoDocumentoActual, String tipoDocumentoAdministrativo, Integer rolFinal) {
        this.estadoDocumentoActual = estadoDocumentoActual;
        this.tipoDocumentoAdministrativo = tipoDocumentoAdministrativo;
        this.rolFinal = rolFinal;
    }

    public String getEstadoDocumentoActual() {
        return estadoDocumentoActual;
    }

    public void setEstadoDocumentoActual(String estadoDocumentoActual) {
        this.estadoDocumentoActual = estadoDocumentoActual;
    }

    public String getTipoDocumentoAdministrativo() {
        return tipoDocumentoAdministrativo;
    }

    public void setTipoDocumentoAdministrativo(String tipoDocumentoAdministrativo) {
        this.tipoDocumentoAdministrativo = tipoDocumentoAdministrativo;
    }

    public Integer getRolFinal() {
        return rolFinal;
    }

    public void setRolFinal(Integer rolFinal) {
        this.rolFinal = rolFinal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estadoDocumentoActual != null ? estadoDocumentoActual.hashCode() : 0);
        hash += (tipoDocumentoAdministrativo != null ? tipoDocumentoAdministrativo.hashCode() : 0);
        hash += (rolFinal != null ? rolFinal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FlujosDocumentoAdministrativoPK)) {
            return false;
        }
        FlujosDocumentoAdministrativoPK other = (FlujosDocumentoAdministrativoPK) object;
        if ((this.estadoDocumentoActual == null && other.estadoDocumentoActual != null) || (this.estadoDocumentoActual != null && !this.estadoDocumentoActual.equals(other.estadoDocumentoActual))) {
            return false;
        }
        if ((this.tipoDocumentoAdministrativo == null && other.tipoDocumentoAdministrativo != null) || (this.tipoDocumentoAdministrativo != null && !this.tipoDocumentoAdministrativo.equals(other.tipoDocumentoAdministrativo))) {
            return false;
        }
        if ((this.rolFinal == null && other.rolFinal != null) || (this.rolFinal != null && !this.rolFinal.equals(other.rolFinal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.FlujosDocumentoPK[ estadoDocumentoActual=" + estadoDocumentoActual + ", tipoDocumentoAdministrativo=" + tipoDocumentoAdministrativo + ", rolFinal=" + rolFinal + " ]";
    }
    
}
