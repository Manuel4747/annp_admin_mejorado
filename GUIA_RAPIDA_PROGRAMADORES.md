# Guía Rápida para Programadores - ANNP Admin

## Inicio Rápido

### 1. Compilar el Proyecto

```bash
cd annp_admin_mejorado
mvn clean install
```

### 2. Desplegar en Servidor

```bash
# Para WildFly
mvn wildfly:deploy

# O copiar manualmente el WAR
cp target/annp_admin.war /ruta/servidor/deployments/
```

### 3. Acceder al Sistema

```
URL: http://localhost:8080/annp_admin/
Dashboard: http://localhost:8080/annp_admin/pages/tesoreria/index.xhtml
```

## Archivos Nuevos Agregados

### Controlador Java
```
src/main/java/py/com/annp/persistencia/controllers/TesoreriaController.java
```

### Vistas XHTML
```
src/main/webapp/pages/tesoreria/
├── dashboard.xhtml
└── index.xhtml
```

### Documentación
```
DOCUMENTACION_INTEGRACION.md
GUIA_RAPIDA_PROGRAMADORES.md
```

## Archivos Modificados

```
src/main/webapp/WEB-INF/sidebar.xhtml        # Agregado menú de tesorería
src/main/webapp/resources/css/styles.css     # Estilos consolidados
src/main/webapp/resources/scripts/layout.js  # JavaScript optimizado
```

## Conectar con Base de Datos Real

### Paso 1: Inyectar Dependencias

Editar `TesoreriaController.java`:

```java
@Inject
private FnPresupuestoController presupuestoController;

@Inject
private FnComprobantesDePagoController pagosController;

@PersistenceContext
private EntityManager em;
```

### Paso 2: Reemplazar Datos Mock

Buscar y reemplazar los comentarios `// TODO: Cargar datos reales`:

```java
private void loadDashboardData() {
    // Antes (mock):
    presupuestoTotal = new BigDecimal("5000000000");
    
    // Después (real):
    presupuestoTotal = (BigDecimal) em.createQuery(
        "SELECT SUM(p.monto) FROM FnPresupuesto p WHERE p.periodo = :periodo"
    ).setParameter("periodo", getCurrentPeriodo())
     .getSingleResult();
}
```

### Paso 3: Implementar Consultas

```java
private void createEjecucionMensualChart() {
    ejecucionMensualChart = new BarChartModel();
    ChartSeries pagos = new ChartSeries();
    pagos.setLabel("Pagos Realizados");

    // Consulta real
    List<Object[]> resultados = em.createQuery(
        "SELECT MONTH(p.fecha), SUM(p.monto) " +
        "FROM FnComprobantesDePago p " +
        "WHERE YEAR(p.fecha) = :anio " +
        "GROUP BY MONTH(p.fecha) " +
        "ORDER BY MONTH(p.fecha)"
    ).setParameter("anio", getCurrentYear())
     .getResultList();

    for (Object[] row : resultados) {
        Integer mes = (Integer) row[0];
        BigDecimal monto = (BigDecimal) row[1];
        pagos.set(getNombreMes(mes), monto.longValue());
    }

    ejecucionMensualChart.addSeries(pagos);
}
```

## Agregar Nuevas Funcionalidades

### Crear Nueva Vista XHTML

1. Crear carpeta en `/src/main/webapp/pages/nombre_modulo/`
2. Crear archivos siguiendo el patrón:
   - `index.xhtml` - Página principal
   - `List.xhtml` - Listado
   - `Create.xhtml` - Formulario de creación
   - `Edit.xhtml` - Formulario de edición
   - `View.xhtml` - Vista de detalle

3. Usar template existente:
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets">
    <ui:composition template="/WEB-INF/template.xhtml">
        <ui:define name="title">Título</ui:define>
        <ui:define name="body">
            <!-- Contenido aquí -->
        </ui:define>
    </ui:composition>
</html>
```

### Crear Nuevo Controlador

1. Crear clase en `/src/main/java/py/com/annp/persistencia/controllers/`
2. Extender `AbstractController` o implementar lógica propia:

```java
package py.com.annp.persistencia.controllers;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import py.com.startic.gestion.controllers.AbstractController;
import py.com.annp.persistencia.models.MiEntidad;

@Named(value = "miEntidadController")
@ViewScoped
public class MiEntidadController extends AbstractController<MiEntidad> {
    
    public MiEntidadController() {
        super(MiEntidad.class);
    }
    
    // Métodos personalizados aquí
}
```

### Agregar al Menú

Editar `/src/main/webapp/WEB-INF/sidebar.xhtml`:

```xml
<p:menuitem value="Mi Nuevo Módulo" 
            outcome="/pages/miModulo/index" 
            icon="ui-icon-star"/>
```

## Componentes PrimeFaces Útiles

### DataTable con Paginación
```xml
<p:dataTable id="tabla"
             value="#{controller.items}"
             var="item"
             paginator="true"
             rows="10"
             rowsPerPageTemplate="10,20,30,40,50">
    <p:column headerText="Columna">
        <h:outputText value="#{item.campo}"/>
    </p:column>
</p:dataTable>
```

### Formulario con Validación
```xml
<h:form id="formulario">
    <p:panel header="Título">
        <p:inputText value="#{controller.selected.campo}" 
                     required="true"
                     requiredMessage="Campo requerido"/>
        <p:commandButton value="Guardar" 
                         action="#{controller.save}"
                         update="@form"/>
    </p:panel>
</h:form>
```

### Gráficos
```xml
<p:chart type="bar" 
         model="#{controller.chartModel}" 
         style="height:300px"
         responsive="true"/>
```

## Debugging

### Habilitar Logs

Editar `src/main/resources/logging.properties`:

```properties
.level=INFO
py.com.annp.level=FINE
```

### Ver Logs en Desarrollo

```bash
tail -f /ruta/servidor/logs/server.log
```

### Debugging en IDE

1. Configurar remote debugging en el servidor
2. En IntelliJ IDEA: Run > Edit Configurations > Remote JVM Debug
3. Host: localhost, Port: 8787

## Testing

### Crear Test Unitario

```java
package py.com.annp.persistencia.controllers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TesoreriaControllerTest {
    
    @Test
    void testLoadDashboardData() {
        TesoreriaController controller = new TesoreriaController();
        controller.init();
        
        assertNotNull(controller.getPresupuestoTotal());
        assertTrue(controller.getPorcentajeEjecucion() > 0);
    }
}
```

## Optimización de Consultas

### Usar Named Queries

En la entidad:
```java
@NamedQuery(
    name = "FnPresupuesto.findByPeriodo",
    query = "SELECT p FROM FnPresupuesto p WHERE p.periodo = :periodo"
)
```

En el controlador:
```java
List<FnPresupuesto> presupuestos = em.createNamedQuery(
    "FnPresupuesto.findByPeriodo", FnPresupuesto.class
).setParameter("periodo", periodo).getResultList();
```

### Usar Fetch Joins

```java
em.createQuery(
    "SELECT p FROM FnPresupuesto p " +
    "LEFT JOIN FETCH p.objetoGasto " +
    "LEFT JOIN FETCH p.fuenteFinanciamiento " +
    "WHERE p.periodo = :periodo",
    FnPresupuesto.class
).setParameter("periodo", periodo).getResultList();
```

## Configuración de Permisos

### 1. Agregar Permiso en Base de Datos

```sql
INSERT INTO permisos (url, descripcion, activo) 
VALUES ('/pages/tesoreria/index.xhtml', 'Dashboard de Tesorería', true);
```

### 2. Asignar a Rol

```sql
INSERT INTO permisos_por_roles (id_rol, id_permiso)
SELECT r.id, p.id 
FROM roles r, permisos p
WHERE r.nombre = 'ADMINISTRADOR' 
  AND p.url = '/pages/tesoreria/index.xhtml';
```

### 3. Proteger Vista

```xml
<p:menuitem value="Dashboard de Tesorería" 
            outcome="/pages/tesoreria/index" 
            rendered="#{filtroURL.verifPermiso('/pages/tesoreria/index.xhtml')}"/>
```

## Comandos Maven Útiles

```bash
# Limpiar y compilar
mvn clean compile

# Empaquetar WAR
mvn package

# Ejecutar tests
mvn test

# Saltar tests
mvn package -DskipTests

# Ver dependencias
mvn dependency:tree

# Actualizar versiones
mvn versions:display-dependency-updates
```

## Estructura de Paquetes

```
py.com.annp.persistencia
├── controllers/     # Controladores JSF (@Named, @ViewScoped)
├── models/          # Entidades JPA (@Entity)
├── util/            # Utilidades
└── services/        # Servicios de negocio (opcional)
```

## Convenciones de Código

### Nombres de Clases
- Controladores: `NombreController.java`
- Entidades: `NombreEntidad.java`
- Utilidades: `NombreUtil.java`

### Nombres de Vistas
- Listado: `List.xhtml`
- Crear: `Create.xhtml`
- Editar: `Edit.xhtml`
- Ver: `View.xhtml`
- Índice: `index.xhtml`

### Nombres de Beans JSF
- Usar camelCase: `tesoreriaController`
- Coincidir con nombre de clase

## Recursos Adicionales

### Documentación Oficial
- Jakarta EE: https://jakarta.ee/
- PrimeFaces: https://www.primefaces.org/docs/
- JSF: https://jakarta.ee/specifications/faces/

### Archivos de Configuración
- `pom.xml` - Dependencias Maven
- `web.xml` - Configuración web
- `persistence.xml` - Configuración JPA

## Contacto y Soporte

Para consultas técnicas:
- Revisar `DOCUMENTACION_INTEGRACION.md` para detalles completos
- Consultar código existente como referencia
- Revisar logs del servidor para errores

---

**Última Actualización:** Noviembre 2025  
**Versión:** 1.0.0
