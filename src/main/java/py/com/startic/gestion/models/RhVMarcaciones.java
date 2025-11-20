/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.math.BigDecimal;
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "rh_v_marcaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RhVMarcaciones.findAll", query = "SELECT r FROM RhVMarcaciones r")
    , @NamedQuery(name = "RhVMarcaciones.findById", query = "SELECT r FROM RhVMarcaciones r WHERE r.id = :id")
    , @NamedQuery(name = "RhVMarcaciones.findByRangoFecha", query = "SELECT r FROM RhVMarcaciones r WHERE r.fecha >= :fechaDesde AND r.fecha <= :fechaHasta ORDER BY r.fecha")
    , @NamedQuery(name = "RhVMarcaciones.findByUsuarioId", query = "SELECT r FROM RhVMarcaciones r WHERE r.usuarioId = :usuarioId")
    , @NamedQuery(name = "RhVMarcaciones.findByNombresApellidos", query = "SELECT r FROM RhVMarcaciones r WHERE r.nombresApellidos = :nombresApellidos")
    , @NamedQuery(name = "RhVMarcaciones.findByEntrada", query = "SELECT r FROM RhVMarcaciones r WHERE r.entrada = :entrada")
    , @NamedQuery(name = "RhVMarcaciones.findBySalida", query = "SELECT r FROM RhVMarcaciones r WHERE r.salida = :salida")})
public class RhVMarcaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usuario_id")
    private int usuarioId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombres_apellidos")
    private String nombresApellidos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ci")
    private String ci;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "entrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date entrada;
    @Column(name = "salida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date salida;
    @Column(name = "extra")
    @Temporal(TemporalType.TIME)
    private Date extra;
    @Column(name = "adicional")
    @Temporal(TemporalType.TIME)
    private Date adicional;
    @Column(name = "extra_mas_adicional")
    @Temporal(TemporalType.TIME)
    private Date extraMasAdicional;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "salario")
    private BigDecimal salario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porc_horas_extras")
    private long porcHorasExtras;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porc_horas_adicionales")
    private long porcHorasAdicionales;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_max_horas_extras")
    private long cantMaxHorasExtras;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_max_horas_adicionales")
    private long cantMaxHorasAdicionales;
    @Basic(optional = false)
    @NotNull
    @Column(name = "salario_70")
    private BigDecimal salario70;
    @Column(name = "salario_30")
    private BigDecimal salario30;
    @Column(name = "remuneracion_extra")
    private BigDecimal remuneracionExtra;
    @Column(name = "remuneracion_adicional")
    private BigDecimal remuneracionAdicional;
    @Column(name = "hora_entrada")
    @Temporal(TemporalType.TIME)
    private Date horaEntrada;
    @Column(name = "hora_salida")
    @Temporal(TemporalType.TIME)
    private Date horaSalida;
    @Column(name = "total_extra")
    private BigDecimal totalExtra;
    @Column(name = "total_adicional")
    private BigDecimal totalAdicional;
    @Column(name = "jubilacion_extra")
    private BigDecimal jubilacionExtra;
    @Column(name = "jubilacion_adicional")
    private BigDecimal jubilacionAdicional;
    @Column(name = "neto_extra")
    private BigDecimal netoExtra;
    @Column(name = "neto_adicional")
    private BigDecimal netoAdicional;

    public RhVMarcaciones() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public Date getExtra() {
        return extra;
    }

    public void setExtra(Date extra) {
        this.extra = extra;
    }

    public Date getExtraMasAdicional() {
        return extraMasAdicional;
    }

    public void setExtraMasAdicional(Date extraMasAdicional) {
        this.extraMasAdicional = extraMasAdicional;
    }

    public Date getAdicional() {
        return adicional;
    }

    public void setAdicional(Date adicional) {
        this.adicional = adicional;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public long getPorcHorasExtras() {
        return porcHorasExtras;
    }

    public void setPorcHorasExtras(long porcHorasExtras) {
        this.porcHorasExtras = porcHorasExtras;
    }

    public long getPorcHorasAdicionales() {
        return porcHorasAdicionales;
    }

    public void setPorcHorasAdicionales(long porcHorasAdicionales) {
        this.porcHorasAdicionales = porcHorasAdicionales;
    }

    public long getCantMaxHorasExtras() {
        return cantMaxHorasExtras;
    }

    public void setCantMaxHorasExtras(long cantMaxHorasExtras) {
        this.cantMaxHorasExtras = cantMaxHorasExtras;
    }

    public long getCantMaxHorasAdicionales() {
        return cantMaxHorasAdicionales;
    }

    public void setCantMaxHorasAdicionales(long cantMaxHorasAdicionales) {
        this.cantMaxHorasAdicionales = cantMaxHorasAdicionales;
    }

    public BigDecimal getSalario70() {
        return salario70;
    }

    public void setSalario70(BigDecimal salario70) {
        this.salario70 = salario70;
    }

    public BigDecimal getSalario30() {
        return salario30;
    }

    public void setSalario30(BigDecimal salario30) {
        this.salario30 = salario30;
    }

    public BigDecimal getRemuneracionExtra() {
        return remuneracionExtra;
    }

    public void setRemuneracionExtra(BigDecimal remuneracionExtra) {
        this.remuneracionExtra = remuneracionExtra;
    }

    public BigDecimal getRemuneracionAdicional() {
        return remuneracionAdicional;
    }

    public void setRemuneracionAdicional(BigDecimal remuneracionAdicional) {
        this.remuneracionAdicional = remuneracionAdicional;
    }

    public Date getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Date horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Date getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Date horaSalida) {
        this.horaSalida = horaSalida;
    }

    public BigDecimal getTotalExtra() {
        return totalExtra;
    }

    public void setTotalExtra(BigDecimal totalExtra) {
        this.totalExtra = totalExtra;
    }

    public BigDecimal getTotalAdicional() {
        return totalAdicional;
    }

    public void setTotalAdicional(BigDecimal totalAdicional) {
        this.totalAdicional = totalAdicional;
    }

    public BigDecimal getJubilacionExtra() {
        return jubilacionExtra;
    }

    public void setJubilacionExtra(BigDecimal jubilacionExtra) {
        this.jubilacionExtra = jubilacionExtra;
    }

    public BigDecimal getJubilacionAdicional() {
        return jubilacionAdicional;
    }

    public void setJubilacionAdicional(BigDecimal jubilacionAdicional) {
        this.jubilacionAdicional = jubilacionAdicional;
    }

    public BigDecimal getNetoExtra() {
        return netoExtra;
    }

    public void setNetoExtra(BigDecimal netoExtra) {
        this.netoExtra = netoExtra;
    }

    public BigDecimal getNetoAdicional() {
        return netoAdicional;
    }

    public void setNetoAdicional(BigDecimal netoAdicional) {
        this.netoAdicional = netoAdicional;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getEntrada() {
        return entrada;
    }

    public void setEntrada(Date entrada) {
        this.entrada = entrada;
    }

    public Date getSalida() {
        return salida;
    }

    public void setSalida(Date salida) {
        this.salida = salida;
    }
    
}
