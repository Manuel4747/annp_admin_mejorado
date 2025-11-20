/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Collection;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "empresas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empresas.findAll", query = "SELECT e FROM Empresas e")
    , @NamedQuery(name = "Empresas.findById", query = "SELECT e FROM Empresas e WHERE e.id = :id")
    , @NamedQuery(name = "Empresas.findByRazonSocial", query = "SELECT e FROM Empresas e WHERE e.razonSocial = :razonSocial")
    , @NamedQuery(name = "Empresas.findByRuc", query = "SELECT e FROM Empresas e WHERE e.ruc = :ruc")
    , @NamedQuery(name = "Empresas.findByDireccion", query = "SELECT e FROM Empresas e WHERE e.direccion = :direccion")
    , @NamedQuery(name = "Empresas.findByTelefono", query = "SELECT e FROM Empresas e WHERE e.telefono = :telefono")})
public class Empresas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "razon_social")
    private String razonSocial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ruc")
    private String ruc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "telefono")
    private String telefono;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<Departamentos> departamentosCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<Roles> rolesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<Usuarios> usuariosCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<EstadosUsuario> estadosUsuarioCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<RolesPorUsuarios> rolesPorUsuariosCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<Sexos> sexosCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<PermisosPorRoles> permisosPorRolesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<Proveedores> proveedoresCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<TiposPersona> tiposPersonaCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<Pantallas> pantallasCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<MovimientosReparacionBienes> movimientosReparacionBienesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<TiposMovimiento> tiposMovimientoCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<Bienes> bienesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<Permisos> permisosCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<Estados> estadosCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<Indices> indicesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<MovimientosBienes> movimientosBienesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<EntradasDocumentosJudiciales> entradasDocumentosJudicialesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<ObservacionesDocumentosJudiciales> observacionesDocumentosJudicialesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<TiposResolucion> tiposResolucionCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "empresa")
    private Collection<Articulos> articulosCollection;

    public Empresas() {
    }

    public Empresas(Integer id) {
        this.id = id;
    }

    public Empresas(Integer id, String razonSocial, String ruc, String direccion, String telefono) {
        this.id = id;
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @XmlTransient
    public Collection<Departamentos> getDepartamentosCollection() {
        return departamentosCollection;
    }

    public void setDepartamentosCollection(Collection<Departamentos> departamentosCollection) {
        this.departamentosCollection = departamentosCollection;
    }

    @XmlTransient
    public Collection<Roles> getRolesCollection() {
        return rolesCollection;
    }

    public void setRolesCollection(Collection<Roles> rolesCollection) {
        this.rolesCollection = rolesCollection;
    }

    @XmlTransient
    public Collection<Usuarios> getUsuariosCollection() {
        return usuariosCollection;
    }

    public void setUsuariosCollection(Collection<Usuarios> usuariosCollection) {
        this.usuariosCollection = usuariosCollection;
    }

    @XmlTransient
    public Collection<EstadosUsuario> getEstadosUsuarioCollection() {
        return estadosUsuarioCollection;
    }

    public void setEstadosUsuarioCollection(Collection<EstadosUsuario> estadosUsuarioCollection) {
        this.estadosUsuarioCollection = estadosUsuarioCollection;
    }

    @XmlTransient
    public Collection<RolesPorUsuarios> getRolesPorUsuariosCollection() {
        return rolesPorUsuariosCollection;
    }

    public void setRolesPorUsuariosCollection(Collection<RolesPorUsuarios> rolesPorUsuariosCollection) {
        this.rolesPorUsuariosCollection = rolesPorUsuariosCollection;
    }

    @XmlTransient
    public Collection<Sexos> getSexosCollection() {
        return sexosCollection;
    }

    public void setSexosCollection(Collection<Sexos> sexosCollection) {
        this.sexosCollection = sexosCollection;
    }

    @XmlTransient
    public Collection<PermisosPorRoles> getPermisosPorRolesCollection() {
        return permisosPorRolesCollection;
    }

    public void setPermisosPorRolesCollection(Collection<PermisosPorRoles> permisosPorRolesCollection) {
        this.permisosPorRolesCollection = permisosPorRolesCollection;
    }
    
    @XmlTransient
    public Collection<Proveedores> getProveedoresCollection() {
        return proveedoresCollection;
    }

    public void setProveedoresCollection(Collection<Proveedores> proveedoresCollection) {
        this.proveedoresCollection = proveedoresCollection;
    }
    
    @XmlTransient
    public Collection<TiposPersona> getTiposPersonaCollection() {
        return tiposPersonaCollection;
    }

    public void setTiposPersonaCollection(Collection<TiposPersona> tiposPersonaCollection) {
        this.tiposPersonaCollection = tiposPersonaCollection;
    }

    @XmlTransient
    public Collection<Pantallas> getPantallasCollection() {
        return pantallasCollection;
    }

    public void setPantallasCollection(Collection<Pantallas> pantallasCollection) {
        this.pantallasCollection = pantallasCollection;
    }

    @XmlTransient
    public Collection<MovimientosReparacionBienes> getMovimientosReparacionBienesCollection() {
        return movimientosReparacionBienesCollection;
    }

    public void setMovimientosReparacionBienesCollection(Collection<MovimientosReparacionBienes> movimientosReparacionBienesCollection) {
        this.movimientosReparacionBienesCollection = movimientosReparacionBienesCollection;
    }

    @XmlTransient
    public Collection<TiposMovimiento> getTiposMovimientoCollection() {
        return tiposMovimientoCollection;
    }

    public void setTiposMovimientoCollection(Collection<TiposMovimiento> tiposMovimientoCollection) {
        this.tiposMovimientoCollection = tiposMovimientoCollection;
    }

    @XmlTransient
    public Collection<Bienes> getBienesCollection() {
        return bienesCollection;
    }

    public void setBienesCollection(Collection<Bienes> bienesCollection) {
        this.bienesCollection = bienesCollection;
    }

    @XmlTransient
    public Collection<Permisos> getPermisosCollection() {
        return permisosCollection;
    }

    public void setPermisosCollection(Collection<Permisos> permisosCollection) {
        this.permisosCollection = permisosCollection;
    }

    @XmlTransient
    public Collection<Estados> getEstadosCollection() {
        return estadosCollection;
    }

    public void setEstadosCollection(Collection<Estados> estadosCollection) {
        this.estadosCollection = estadosCollection;
    }

    @XmlTransient
    public Collection<Indices> getIndicesCollection() {
        return indicesCollection;
    }

    public void setIndicesCollection(Collection<Indices> indicesCollection) {
        this.indicesCollection = indicesCollection;
    }

    @XmlTransient
    public Collection<MovimientosBienes> getMovimientosBienesCollection() {
        return movimientosBienesCollection;
    }

    public void setMovimientosBienesCollection(Collection<MovimientosBienes> movimientosBienesCollection) {
        this.movimientosBienesCollection = movimientosBienesCollection;
    }

    @XmlTransient
    public Collection<EntradasDocumentosJudiciales> getEntradasDocumentosJudicialesCollection() {
        return entradasDocumentosJudicialesCollection;
    }

    public void setEntradasDocumentosJudicialesCollection(Collection<EntradasDocumentosJudiciales> entradasDocumentosJudicialesCollection) {
        this.entradasDocumentosJudicialesCollection = entradasDocumentosJudicialesCollection;
    }

    @XmlTransient
    public Collection<ObservacionesDocumentosJudiciales> getObservacionesDocumentosJudicialesCollection() {
        return observacionesDocumentosJudicialesCollection;
    }

    public void setObservacionesDocumentosJudicialesCollection(Collection<ObservacionesDocumentosJudiciales> observacionesDocumentosJudicialesCollection) {
        this.observacionesDocumentosJudicialesCollection = observacionesDocumentosJudicialesCollection;
    }

    @XmlTransient
    public Collection<TiposResolucion> getTiposResolucionCollection() {
        return tiposResolucionCollection;
    }

    public void setTiposResolucionCollection(Collection<TiposResolucion> tiposResolucionCollection) {
        this.tiposResolucionCollection = tiposResolucionCollection;
    }

    @XmlTransient
    public Collection<Articulos> getArticulosCollection() {
        return articulosCollection;
    }

    public void setArticulosCollection(Collection<Articulos> articulosCollection) {
        this.articulosCollection = articulosCollection;
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
        if (!(object instanceof Empresas)) {
            return false;
        }
        Empresas other = (Empresas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.Empresas[ id=" + id + " ]";
    }
    
}
