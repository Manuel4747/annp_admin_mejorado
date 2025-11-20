package py.com.startic.gestion.datasource;

import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;

/**
 *
 * @author DELL
 */
@Entity
public class RhResumenMarcacionesReloj {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "ci")
    private String ci;

    @Column(name = "minimo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date minimo;
    @Column(name = "maximo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date maximo;

    @Column(name = "Terminales")
    private String terminales;

    @Column(name = "cantidad")
    private Long cantidad;

    @Column(name = "diferencia")
    private Long diferencia;

    @Column(name = "transcurso")
    private String transcurso;

    public RhResumenMarcacionesReloj() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public Date getMinimo() {
        return minimo;
    }

    public void setMinimo(Date minimo) {
        this.minimo = minimo;
    }

    public Date getMaximo() {
        return maximo;
    }

    public void setMaximo(Date maximo) {
        this.maximo = maximo;
    }

    public String getTerminales() {
        return terminales;
    }

    public void setTerminales(String terminales) {
        this.terminales = terminales;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Long getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Long diferencia) {
        this.diferencia = diferencia;
    }

    public String getTranscurso() {
        return transcurso;
    }

    public void setTranscurso(String transcurso) {
        this.transcurso = transcurso;
    }

}
