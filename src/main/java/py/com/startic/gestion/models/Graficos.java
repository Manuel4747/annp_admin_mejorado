package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Base64;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "graficos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Graficos.findAll", query = "SELECT r FROM Graficos r"),
    @NamedQuery(name = "Graficos.findOrdered", query = "SELECT r FROM Graficos r ORDER BY r.nombre"),
    @NamedQuery(name = "Graficos.findById", query = "SELECT r FROM Graficos r WHERE r.id = :id")
})
public class Graficos implements Serializable  {

    private static final long serialVersionUID = 1L;
    @ Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "descripcion")
    private String descripcion;

    @Basic(optional = false)
    @Lob
    @Column(name = "imagen")
    private byte[] imagen;
    
    @Transient
    private String imagenBase64;
    
 public String getImagenBase64() {
        if (this.imagen != null && this.imagen.length > 0) {
            imagenBase64 = Base64.getEncoder().encodeToString(this.imagen).replace("?pfdrid_c=true", "");
            
            return imagenBase64;
        }
        return null;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

}
