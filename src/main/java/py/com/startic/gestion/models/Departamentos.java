/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import py.com.startic.gestion.controllers.Utils;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "departamentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Departamentos.findAll", query = "SELECT d FROM Departamentos d ORDER BY d.nombre")
    , @NamedQuery(name = "Departamentos.findById", query = "SELECT d FROM Departamentos d WHERE d.id = :id")
    , @NamedQuery(name = "Departamentos.findByEstado", query = "SELECT d FROM Departamentos d WHERE d.estado = :estado ORDER BY d.nombre")
    , @NamedQuery(name = "Departamentos.findByEstadoANDEsMaximoNivel", query = "SELECT d FROM Departamentos d WHERE d.estado = :estado and d.esMaximoNivel = :esMaximoNivel ORDER BY d.nombre")
    , @NamedQuery(name = "Departamentos.findByEstados", query = "SELECT d FROM Departamentos d WHERE d.estado IN :estados ORDER BY d.nombre")
    , @NamedQuery(name = "Departamentos.findByDepartamentoPadreISNULL", query = "SELECT d FROM Departamentos d WHERE d.departamentoPadre is null and d.estado = :estado ORDER BY d.nombre")
    , @NamedQuery(name = "Departamentos.findByDepartamentoPadre", query = "SELECT d FROM Departamentos d WHERE d.departamentoPadre = :departamentoPadre and d.estado = :estado ORDER BY d.nombre")
    , @NamedQuery(name = "Departamentos.findByNombre", query = "SELECT d FROM Departamentos d WHERE d.nombre = :nombre")
    , @NamedQuery(name = "Departamentos.findByFechaHoraAlta", query = "SELECT d FROM Departamentos d WHERE d.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "Departamentos.findByFechaHoraUltimoEstado", query = "SELECT d FROM Departamentos d WHERE d.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class Departamentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
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
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "departamento_padre", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamentoPadre;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Estados estado;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "departamento")
    private Collection<Usuarios> usuariosCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "departamentoOrigen")
    private Collection<MovimientosBienes> movimientosBienesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "departamentoDestino")
    private Collection<MovimientosBienes> movimientosBienesCollection1;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "departamento")
    private Collection<DocumentosJudiciales> documentosJudicialesCollection;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "nomenclatura_memo")
    private String nomenclaturaMemo;
    @Column(name = "es_maximo_nivel")
    private boolean esMaximoNivel;

    public Departamentos() {
    }

    public Departamentos(Integer id) {
        this.id = id;
    }

    public Departamentos(Integer id, String nombre, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.nombre = nombre;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isEsMaximoNivel() {
        return esMaximoNivel;
    }

    public void setEsMaximoNivel(boolean esMaximoNivel) {
        this.esMaximoNivel = esMaximoNivel;
    }

    public String getNombre() {
        /*
        if(nombre != null){
            return Utils.capitalizeString(nombre);
        }
        return null;
*/
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public Departamentos getDepartamentoPadre() {
        return departamentoPadre;
    }

    public void setDepartamentoPadre(Departamentos departamentoPadre) {
        this.departamentoPadre = departamentoPadre;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<Usuarios> getUsuariosCollection() {
        return usuariosCollection;
    }

    public void setUsuariosCollection(Collection<Usuarios> usuariosCollection) {
        this.usuariosCollection = usuariosCollection;
    }
    
    @XmlTransient
    public Collection<DocumentosJudiciales> getDocumentosJudicialesCollection() {
        return documentosJudicialesCollection;
    }

    public void setDocumentosJudicialesCollection(Collection<DocumentosJudiciales> documentosJudicialesCollection) {
        this.documentosJudicialesCollection = documentosJudicialesCollection;
    }

    @XmlTransient
    public Collection<MovimientosBienes> getMovimientosBienesCollection() {
        return movimientosBienesCollection;
    }

    public void setMovimientosBienesCollection(Collection<MovimientosBienes> movimientosBienesCollection) {
        this.movimientosBienesCollection = movimientosBienesCollection;
    }

    @XmlTransient
    public Collection<MovimientosBienes> getMovimientosBienesCollection1() {
        return movimientosBienesCollection1;
    }

    public void setMovimientosBienesCollection1(Collection<MovimientosBienes> movimientosBienesCollection1) {
        this.movimientosBienesCollection1 = movimientosBienesCollection1;
    }

    public String getNomenclaturaMemo() {
        return nomenclaturaMemo;
    }

    public void setNomenclaturaMemo(String nomenclaturaMemo) {
        this.nomenclaturaMemo = nomenclaturaMemo;
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
        if (!(object instanceof Departamentos)) {
            return false;
        }
        Departamentos other = (Departamentos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
}
