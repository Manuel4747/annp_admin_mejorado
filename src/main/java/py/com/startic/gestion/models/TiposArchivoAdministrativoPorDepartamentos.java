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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "tipos_archivo_administrativo_por_departamentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiposArchivoAdministrativoPorDepartamentos.findByTipoArchivoAdministrativoANDDepartamento", query = "SELECT t FROM TiposArchivoAdministrativoPorDepartamentos t WHERE t.tipoArchivoAdministrativo = :tipoArchivoAdministrativo AND t.departamento = :departamento")
    , @NamedQuery(name = "TiposArchivoAdministrativoPorDepartamentos.findByDepartamento", query = "SELECT t FROM TiposArchivoAdministrativoPorDepartamentos t WHERE t.departamento = :departamento ORDER BY t.tipoArchivoAdministrativo.descripcion")
    , @NamedQuery(name = "TiposArchivoAdministrativoPorDepartamentos.findById", query = "SELECT t FROM TiposArchivoAdministrativoPorDepartamentos t WHERE t.id = :id")})
public class TiposArchivoAdministrativoPorDepartamentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "tipo_archivo_administrativo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TiposArchivoAdministrativo tipoArchivoAdministrativo;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "secuencia")
    private String secuencia;
    @Transient
    private Integer nro;
    @Transient
    private String ano;
    @JoinColumn(name = "usuario_borrado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioBorrado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioAlta;
    @Basic(optional = true)
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioUltimoEstado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    
    public TiposArchivoAdministrativoPorDepartamentos() {
    }

    public TiposArchivoAdministrativoPorDepartamentos(Integer id) {
        this.id = id;
    }

    public TiposArchivoAdministrativoPorDepartamentos(TiposArchivoAdministrativo tipo, Departamentos dpto, String secuencia) {
        this.tipoArchivoAdministrativo = tipo;
        this.departamento = dpto;
        this.secuencia = secuencia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TiposArchivoAdministrativo getTipoArchivoAdministrativo() {
        return tipoArchivoAdministrativo;
    }

    public void setTipoArchivoAdministrativo(TiposArchivoAdministrativo tipoArchivoAdministrativo) {
        this.tipoArchivoAdministrativo = tipoArchivoAdministrativo;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public Usuarios getUsuarioBorrado() {
        return usuarioBorrado;
    }

    public void setUsuarioBorrado(Usuarios usuarioBorrado) {
        this.usuarioBorrado = usuarioBorrado;
    }

    public Date getFechaHoraBorrado() {
        return fechaHoraBorrado;
    }

    public void setFechaHoraBorrado(Date fechaHoraBorrado) {
        this.fechaHoraBorrado = fechaHoraBorrado;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public String getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Integer getNro() {
        return nro;
    }

    public void setNro(Integer nro) {
        this.nro = nro;
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
        if (!(object instanceof TiposArchivoAdministrativoPorDepartamentos)) {
            return false;
        }
        TiposArchivoAdministrativoPorDepartamentos other = (TiposArchivoAdministrativoPorDepartamentos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ((tipoArchivoAdministrativo != null)?tipoArchivoAdministrativo.getDescripcion():"") + ((departamento != null)?departamento.getNombre():"");
    }
    
}
