/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "contactos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contactos.findAll", query = "SELECT r FROM Contactos r")
    , @NamedQuery(name = "Contactos.findById", query = "SELECT r FROM Contactos r WHERE r.id = :id")
    , @NamedQuery(name = "Contactos.findByUsuario", query = "SELECT r FROM Contactos r WHERE r.usuario = :usuario ORDER BY r.nombresApellidos")
    , @NamedQuery(name = "Contactos.findByNombresApellidos", query = "SELECT r FROM Contactos r WHERE r.nombresApellidos = :nombresApellidos")})
public class Contactos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "nombres_apellidos")
    private String nombresApellidos;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "telefono")
    private String telefono;
    @JoinColumn(name = "vinculo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Vinculos vinculo;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuario;
    
    public Contactos() {
    }

    public Contactos(Integer id) {
        this.id = id;
    }

    public Contactos(Integer id, String nombresApellidos, String telefono, Vinculos vinculo, Usuarios usuario) {
        this.id = id;
        this.nombresApellidos = nombresApellidos;
        this.telefono = telefono;
        this.vinculo = vinculo;
        this.usuario = usuario;
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

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Vinculos getVinculo() {
        return vinculo;
    }

    public void setVinculo(Vinculos vinculo) {
        this.vinculo = vinculo;
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
        if (!(object instanceof Contactos)) {
            return false;
        }
        Contactos other = (Contactos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.Roles[ id=" + id + " ]";
    }
    
}
