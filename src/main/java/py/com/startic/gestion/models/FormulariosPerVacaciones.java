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
import jakarta.persistence.FetchType;
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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "formularios_per_vacaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FormulariosPerVacaciones.findById", query = "SELECT t FROM FormulariosPerVacaciones t WHERE t.id = :id")
    , @NamedQuery(name = "FormulariosPerVacaciones.findByFormularioPermiso", query = "SELECT t FROM FormulariosPerVacaciones t WHERE t.formulariosPermiso = :formulariosPermiso")
    , @NamedQuery(name = "FormulariosPerVacaciones.findByFuncionario", query = "SELECT t FROM FormulariosPerVacaciones t WHERE t.funcionario = :funcionario")})
public class FormulariosPerVacaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @JoinColumn(name = "formulario_permiso")
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private FormulariosPermisos formulariosPermiso;

    @Size(min = 1, max = 4)
    @Column(name = "anio")
    private String anio;

    @JoinColumn(name = "funcionario")
    @ManyToOne(optional = false)
    private Usuarios funcionario;

    @Column(name = "cantidad_usada")
    private Integer cantidadUsada;

    @Column(name = "cantidad_acumulada")
    private Integer cantidadAcumulada;
  
    @Column(name = "fecha_hora_insert")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraInsert;
    
    public FormulariosPerVacaciones() {
    }

    public FormulariosPerVacaciones(Integer id) {
        this.id = id;
    }

    public FormulariosPermisos getFormularioPermiso() {
        return formulariosPermiso;
    }

    public void setFormularioPermiso(FormulariosPermisos formulariosPermiso) {
        this.formulariosPermiso = formulariosPermiso;
    }
    
    public FormulariosPerVacaciones(Integer id, String anio) {
        this.id = id;
        this.anio = anio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public Usuarios getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Usuarios funcionario) {
        this.funcionario = funcionario;
    }

    public Integer getCantidadUsada() {
        return cantidadUsada;
    }

    public void setCantidadUsada(Integer cantidadUsada) {
        this.cantidadUsada = cantidadUsada;
    }

    public Integer getCantidadAcumulada() {
        return cantidadAcumulada;
    }

    public void setCantidadAcumulada(Integer cantidadAcumulada) {
        this.cantidadAcumulada = cantidadAcumulada;
    }

    public Date getFechaHoraInsert() {
        return fechaHoraInsert;
    }

    public void setFechaHoraInsert(Date fechaHoraInsert) {
        this.fechaHoraInsert = fechaHoraInsert;
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
        if (!(object instanceof FormulariosPerVacaciones)) {
            return false;
        }
        FormulariosPerVacaciones other = (FormulariosPerVacaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.FormulariosPerVacaciones[ id=" + id + " ]";
    }
    
}
