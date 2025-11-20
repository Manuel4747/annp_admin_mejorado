/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "reportes_inventario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReportesInventario.findAll", query = "SELECT r FROM ReportesInventario r")
    , @NamedQuery(name = "ReportesInventario.findByCodigo", query = "SELECT r FROM ReportesInventario r WHERE r.codigo = :codigo")
    , @NamedQuery(name = "ReportesInventario.findByArticulo", query = "SELECT r FROM ReportesInventario r WHERE r.articulo = :articulo")
    , @NamedQuery(name = "ReportesInventario.findByInventario", query = "SELECT r FROM ReportesInventario r WHERE r.inventario = :inventario ORDER BY r.descripcion")
    , @NamedQuery(name = "ReportesInventario.findByFecha", query = "SELECT r FROM ReportesInventario r WHERE r.fecha = :fecha")
    , @NamedQuery(name = "ReportesInventario.findByDescripcion", query = "SELECT r FROM ReportesInventario r WHERE r.descripcion = :descripcion")
    , @NamedQuery(name = "ReportesInventario.findBySimbolo", query = "SELECT r FROM ReportesInventario r WHERE r.simbolo = :simbolo")
    , @NamedQuery(name = "ReportesInventario.findByCantInventario", query = "SELECT r FROM ReportesInventario r WHERE r.cantInventario = :cantInventario")
    , @NamedQuery(name = "ReportesInventario.findByCantEntrada", query = "SELECT r FROM ReportesInventario r WHERE r.cantEntrada = :cantEntrada")
    , @NamedQuery(name = "ReportesInventario.findByCantSalida", query = "SELECT r FROM ReportesInventario r WHERE r.cantSalida = :cantSalida")
    , @NamedQuery(name = "ReportesInventario.findByNeto", query = "SELECT r FROM ReportesInventario r WHERE r.neto = :neto")
    , @NamedQuery(name = "ReportesInventario.findByStock", query = "SELECT r FROM ReportesInventario r WHERE r.stock = :stock")})
public class ReportesInventario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 22)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "articulo")
    private String articulo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inventario")
    private int inventario;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "simbolo")
    private String simbolo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_inventario")
    private BigInteger cantInventario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_entrada")
    private BigInteger cantEntrada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_salida")
    private BigInteger cantSalida;
    @Basic(optional = false)
    @NotNull
    @Column(name = "neto")
    private BigInteger neto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stock")
    private long stock;

    public ReportesInventario() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public int getInventario() {
        return inventario;
    }

    public void setInventario(int inventario) {
        this.inventario = inventario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public BigInteger getCantInventario() {
        return cantInventario;
    }

    public void setCantInventario(BigInteger cantInventario) {
        this.cantInventario = cantInventario;
    }

    public BigInteger getCantEntrada() {
        return cantEntrada;
    }

    public void setCantEntrada(BigInteger cantEntrada) {
        this.cantEntrada = cantEntrada;
    }

    public BigInteger getCantSalida() {
        return cantSalida;
    }

    public void setCantSalida(BigInteger cantSalida) {
        this.cantSalida = cantSalida;
    }

    public BigInteger getNeto() {
        return neto;
    }

    public void setNeto(BigInteger neto) {
        this.neto = neto;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }
    
}
