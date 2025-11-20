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
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "roles_por_usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RolesPorUsuarios.findAll", query = "SELECT r FROM RolesPorUsuarios r")
    , @NamedQuery(name = "RolesPorUsuarios.findByUsuario", query = "SELECT r FROM RolesPorUsuarios r WHERE r.rolesPorUsuariosPK.usuario = :usuario")
    , @NamedQuery(name = "RolesPorUsuarios.findByUsuarioRolFlujo", query = "SELECT r FROM RolesPorUsuarios r WHERE r.rolesPorUsuariosPK.usuario = :usuario and r.rolesPorUsuariosPK.rol < 0")
    , @NamedQuery(name = "RolesPorUsuarios.findRolFlujo", query = "SELECT r FROM RolesPorUsuarios r WHERE r.rolesPorUsuariosPK.rol < 0 AND r.rolesPorUsuariosPK.usuario = :usuario AND r.rolesPorUsuariosPK.rol IN (SELECT f.rolFinal.id FROM FlujosDocumento f WHERE f.flujosDocumentoPK.estadoDocumentoActual = :estadoDocumentoActual AND f.flujosDocumentoPK.tipoDocumento = :tipoDocumento)")
    , @NamedQuery(name = "RolesPorUsuarios.findRolFlujoAdministrativo", query = "SELECT r FROM RolesPorUsuarios r WHERE r.rolesPorUsuariosPK.rol < 0 AND r.rolesPorUsuariosPK.usuario = :usuario AND r.rolesPorUsuariosPK.rol IN (SELECT f.rolFinal.id FROM FlujosDocumentoAdministrativo f WHERE f.flujosDocumentoAdministrativoPK.estadoDocumentoActual = :estadoDocumentoActual AND f.flujosDocumentoAdministrativoPK.tipoDocumentoAdministrativo = :tipoDocumento)")
    , @NamedQuery(name = "RolesPorUsuarios.findByRol", query = "SELECT r FROM RolesPorUsuarios r WHERE r.rolesPorUsuariosPK.rol = :rol")
    , @NamedQuery(name = "RolesPorUsuarios.findByUsuarioRol", query = "SELECT r FROM RolesPorUsuarios r WHERE r.rolesPorUsuariosPK.rol = :rol AND r.rolesPorUsuariosPK.usuario = :usuario")
    , @NamedQuery(name = "RolesPorUsuarios.findByFechaHoraAlta", query = "SELECT r FROM RolesPorUsuarios r WHERE r.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "RolesPorUsuarios.findByFechaHoraUltimoEstado", query = "SELECT r FROM RolesPorUsuarios r WHERE r.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class RolesPorUsuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RolesPorUsuariosPK rolesPorUsuariosPK;
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
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Estados estado;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "rol", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Roles roles;
    @JoinColumn(name = "usuario", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuarios usuarios;
    @JoinColumn(name = "perfil", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Perfiles perfiles;

    public RolesPorUsuarios() {
    }

    public RolesPorUsuarios(RolesPorUsuariosPK rolesPorUsuariosPK) {
        this.rolesPorUsuariosPK = rolesPorUsuariosPK;
    }

    public RolesPorUsuarios(RolesPorUsuariosPK rolesPorUsuariosPK, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.rolesPorUsuariosPK = rolesPorUsuariosPK;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public RolesPorUsuarios(int usuario, int rol, int perfil) {
        this.rolesPorUsuariosPK = new RolesPorUsuariosPK(usuario, rol, perfil);
    }

    public RolesPorUsuariosPK getRolesPorUsuariosPK() {
        return rolesPorUsuariosPK;
    }

    public void setRolesPorUsuariosPK(RolesPorUsuariosPK rolesPorUsuariosPK) {
        this.rolesPorUsuariosPK = rolesPorUsuariosPK;
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

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
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

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    public Perfiles getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(Perfiles perfiles) {
        this.perfiles = perfiles;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolesPorUsuariosPK != null ? rolesPorUsuariosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolesPorUsuarios)) {
            return false;
        }
        RolesPorUsuarios other = (RolesPorUsuarios) object;
        if ((this.rolesPorUsuariosPK == null && other.rolesPorUsuariosPK != null) || (this.rolesPorUsuariosPK != null && !this.rolesPorUsuariosPK.equals(other.rolesPorUsuariosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.RolesPorUsuarios[ rolesPorUsuariosPK=" + rolesPorUsuariosPK + " ]";
    }
    
}
