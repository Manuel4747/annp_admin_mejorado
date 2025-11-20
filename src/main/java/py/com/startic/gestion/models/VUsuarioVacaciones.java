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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "v_usuario_vacaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VUsuarioVacaciones.findAll", query = "SELECT v FROM VUsuarioVacaciones v"),
    @NamedQuery(name = "VUsuarioVacaciones.findById", query = "SELECT v FROM VUsuarioVacaciones v WHERE v.id = :id"),
    @NamedQuery(name = "VUsuarioVacaciones.findByUsuario", query = "SELECT v FROM VUsuarioVacaciones v WHERE v.usuario = :usuario"),
    @NamedQuery(name = "VUsuarioVacaciones.findByUsuarioNoCero", query = "SELECT v FROM VUsuarioVacaciones v WHERE v.usuario = :usuario and v.dias > v.cantidadUtilizada"),
    @NamedQuery(name = "VUsuarioVacaciones.findByUsuarioNoAcumuladoNoCero", query = "SELECT v FROM VUsuarioVacaciones v WHERE v.usuario = :usuario and v.tipo <> 'acumulado' and v.dias > v.cantidadUtilizada"),
    @NamedQuery(name = "VUsuarioVacaciones.findByUsuarioAnio", query = "SELECT v FROM VUsuarioVacaciones v WHERE v.id = :id AND v.anio = :anio")})
public class VUsuarioVacaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowKey")
    private String rowKey ;
    
    @Size(max = 15)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "usuario")
    private String usuario;

    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;

    @Size(min = 4, max = 4)
    @Column(name = "anio")
    private Integer anio;

    @Column(name = "antiguedad")
    private Integer antiguedad;

    @Column(name = "dias")
    private Integer dias;

    @Column(name = "cantidad_utilizada")
    private Integer cantidadUtilizada;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "id_documento")
    private Integer idDocumento;
    
    

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(Integer antiguedad) {
        this.antiguedad = antiguedad;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public Integer getCantidadUtilizada() {
        return cantidadUtilizada;
    }

    public void setCantidadUtilizada(Integer cantidadUtilizada) {
        this.cantidadUtilizada = cantidadUtilizada;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    
    
    public VUsuarioVacaciones() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

}
