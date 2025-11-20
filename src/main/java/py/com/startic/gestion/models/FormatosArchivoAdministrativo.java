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
import jakarta.persistence.Lob;
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
@Table(name = "formatos_archivo_administrativo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FormatosArchivoAdministrativo.findAll", query = "SELECT t FROM FormatosArchivoAdministrativo t order by t.fechaHoraAlta desc")
    , @NamedQuery(name = "FormatosArchivoAdministrativo.findById", query = "SELECT t FROM FormatosArchivoAdministrativo t WHERE t.id = :id")
    , @NamedQuery(name = "FormatosArchivoAdministrativo.findByDescripcion", query = "SELECT t FROM FormatosArchivoAdministrativo t WHERE t.descripcion = :descripcion")
    , @NamedQuery(name = "FormatosArchivoAdministrativo.findByDepartamento", query = "SELECT t FROM FormatosArchivoAdministrativo t WHERE t.departamento = :departamento")
    , @NamedQuery(name = "FormatosArchivoAdministrativo.findByTipoArchivo", query = "SELECT t FROM FormatosArchivoAdministrativo t WHERE t.tipoArchivo = :tipoArchivo ORDER BY t.descripcion")
    , @NamedQuery(name = "FormatosArchivoAdministrativo.findByTipoArchivoANDDepartamento", query = "SELECT t FROM FormatosArchivoAdministrativo t WHERE t.tipoArchivo = :tipoArchivo AND t.departamento = :departamento ORDER BY t.descripcion")
    , @NamedQuery(name = "FormatosArchivoAdministrativo.findByIdTipoArchivo", query = "SELECT t FROM FormatosArchivoAdministrativo t WHERE t.tipoArchivo.id = :tipoArchivo ORDER BY t.descripcion")
    , @NamedQuery(name = "FormatosArchivoAdministrativo.findByFechaHoraAlta", query = "SELECT t FROM FormatosArchivoAdministrativo t WHERE t.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "FormatosArchivoAdministrativo.findByFechaHoraUltimoEstado", query = "SELECT t FROM FormatosArchivoAdministrativo t WHERE t.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class FormatosArchivoAdministrativo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "tipo_archivo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TiposArchivoAdministrativo tipoArchivo;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
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
    @Basic(optional = true)
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "usuario_borrado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioBorrado;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "texto")
    private String texto;

    public FormatosArchivoAdministrativo() {
    }

    public FormatosArchivoAdministrativo(Integer id) {
        this.id = id;
    }

    public FormatosArchivoAdministrativo(Integer id, String descripcion, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public TiposArchivoAdministrativo getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(TiposArchivoAdministrativo tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
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
    
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public Date getFechaHoraBorrado() {
        return fechaHoraBorrado;
    }

    public void setFechaHoraBorrado(Date fechaHoraBorrado) {
        this.fechaHoraBorrado = fechaHoraBorrado;
    }

    public Usuarios getUsuarioBorrado() {
        return usuarioBorrado;
    }

    public void setUsuarioBorrado(Usuarios usuarioBorrado) {
        this.usuarioBorrado = usuarioBorrado;
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
        if (!(object instanceof FormatosArchivoAdministrativo)) {
            return false;
        }
        FormatosArchivoAdministrativo other = (FormatosArchivoAdministrativo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (descripcion!=null)?descripcion:"";
    }
    
}
