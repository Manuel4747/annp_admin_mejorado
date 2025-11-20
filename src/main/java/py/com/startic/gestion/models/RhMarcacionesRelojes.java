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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "rh_marcaciones_relojes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RhMarcacionesRelojes.findAll", query = "SELECT r FROM RhMarcacionesRelojes r")
    , @NamedQuery(name = "RhMarcacionesRelojes.findById", query = "SELECT r FROM RhMarcacionesRelojes r WHERE r.id = :id")
    , @NamedQuery(name = "RhMarcacionesRelojes.findByFechaYCi", query = "SELECT r FROM RhMarcacionesRelojes r WHERE r.fechaAutenticacion = :fecha and r.nroEmpleado = :ci")})
public class RhMarcacionesRelojes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 100)
    @Column(name = "nro_empleado")
    private String nroEmpleado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_autenticacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAutenticacion;
    @Column(name = "fecha_autenticacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAutenticacion;
    @Column(name = "hora_autenticacion")
    @Temporal(TemporalType.TIME)
    private Date horaAutenticacion;
    @Basic(optional = true)
    @Size(max = 100)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = true)
    @Size(max = 100)
    @Column(name = "nombre_dispositivo")
    private String nombreDispositivo;
    @Basic(optional = true)
    @Size(max = 100)
    @Column(name = "nro_serie_dispositivo")
    private String nroSerieDispositivo;
    @Basic(optional = true)
    @Size(max = 100)
    @Column(name = "nombres_apellidos")
    private String nombresApellidos;
    @Basic(optional = true)
    @Size(max = 100)
    @Column(name = "nro_tarjeta")
    private String nroTarjeta;

    public RhMarcacionesRelojes() {
    }

    public RhMarcacionesRelojes(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNroEmpleado() {
        return nroEmpleado;
    }

    public void setNroEmpleado(String nroEmpleado) {
        this.nroEmpleado = nroEmpleado;
    }

    public Date getFechaHoraAutenticacion() {
        return fechaHoraAutenticacion;
    }

    public void setFechaHoraAutenticacion(Date fechaHoraAutenticacion) {
        this.fechaHoraAutenticacion = fechaHoraAutenticacion;
    }

    public Date getFechaAutenticacion() {
        return fechaAutenticacion;
    }

    public void setFechaAutenticacion(Date fechaAutenticacion) {
        this.fechaAutenticacion = fechaAutenticacion;
    }

    public Date getHoraAutenticacion() {
        return horaAutenticacion;
    }

    public void setHoraAutenticacion(Date horaAutenticacion) {
        this.horaAutenticacion = horaAutenticacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreDispositivo() {
        return nombreDispositivo;
    }

    public void setNombreDispositivo(String nombreDispositivo) {
        this.nombreDispositivo = nombreDispositivo;
    }

    public String getNroSerieDispositivo() {
        return nroSerieDispositivo;
    }

    public void setNroSerieDispositivo(String nroSerieDispositivo) {
        this.nroSerieDispositivo = nroSerieDispositivo;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getNroTarjeta() {
        return nroTarjeta;
    }

    public void setNroTarjeta(String nroTarjeta) {
        this.nroTarjeta = nroTarjeta;
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
        if (!(object instanceof SalidasArticulo)) {
            return false;
        }
        RhMarcacionesRelojes other = (RhMarcacionesRelojes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "py.com.startic.gestion.models18.RhMarcaciones[ id=" + id + " ]";
    }
    
}
