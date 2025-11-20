# Documentación de Integración - Sistema ANNP Admin

## Resumen de Cambios

Este documento detalla las integraciones realizadas al proyecto original ANNP Admin para agregar las funcionalidades del Sistema de Presupuesto y Contabilidad, manteniendo toda la estructura original del proyecto JavaServer Faces.

## Estructura del Proyecto Original Mantenida

El proyecto mantiene **928 clases Java** con toda la arquitectura original:

```
annp_admin/
├── src/main/java/py/com/annp/
│   ├── persistencia/
│   │   ├── controllers/        # Controladores JSF (928 clases)
│   │   ├── models/             # Entidades JPA
│   │   └── util/               # Utilidades
│   └── ...
├── src/main/webapp/
│   ├── WEB-INF/
│   │   ├── template.xhtml      # Template principal
│   │   ├── topbar.xhtml        # Barra superior
│   │   ├── sidebar.xhtml       # Menú lateral (MODIFICADO)
│   │   └── ...
│   ├── pages/                  # Vistas XHTML organizadas por módulo
│   └── resources/
│       ├── css/
│       │   └── styles.css      # Estilos consolidados (MODIFICADO)
│       └── scripts/
│           └── layout.js       # JavaScript del layout (MODIFICADO)
└── pom.xml                     # Configuración Maven
```

## Archivos Nuevos Agregados

### 1. Controlador de Tesorería

**Ubicación:** `/src/main/java/py/com/annp/persistencia/controllers/TesoreriaController.java`

**Descripción:** Controlador JSF para el dashboard de tesorería con:
- Estadísticas principales (presupuesto, ejecutado, comprometido, disponible)
- Accesos rápidos a módulos relacionados
- Gráficos de ejecución (BarChartModel, PieChartModel de PrimeFaces)
- Listado de últimos pagos

**Características:**
- Anotado con `@Named` y `@ViewScoped`
- Utiliza PrimeFaces Charts
- Métodos de navegación a otros módulos
- Datos de ejemplo (TODO: conectar con base de datos real)

**Métodos principales:**
```java
- loadDashboardData()                    // Carga estadísticas
- createEjecucionMensualChart()          // Gráfico de barras mensual
- createDistribucionPorRubroChart()      // Gráfico de torta por rubro
- navigateToPagosPendientes()            // Navegación a pagos
- navigateToCDPs()                       // Navegación a CDPs
- navigateToCompromisos()                // Navegación a compromisos
```

### 2. Vistas XHTML de Tesorería

**Ubicación:** `/src/main/webapp/pages/tesoreria/`

#### dashboard.xhtml
Vista principal del dashboard con:
- Panel de estadísticas (4 tarjetas con totales)
- Accesos rápidos (4 enlaces a módulos)
- Gráficos de PrimeFaces (barra y torta)
- Tabla de últimos pagos con paginación

**Componentes utilizados:**
- `<p:panel>` - Contenedores
- `<p:chart>` - Gráficos
- `<p:dataTable>` - Tabla de datos
- `<p:commandLink>` - Enlaces de navegación
- `<h:outputText>` - Textos formateados con conversores de moneda

#### index.xhtml
Página de entrada que utiliza el template principal:
```xml
<ui:composition template="/WEB-INF/template.xhtml">
    <ui:define name="title">Dashboard de Tesorería</ui:define>
    <ui:define name="body">
        <ui:include src="dashboard.xhtml"/>
    </ui:define>
</ui:composition>
```

## Archivos Modificados

### 1. sidebar.xhtml

**Ubicación:** `/src/main/webapp/WEB-INF/sidebar.xhtml`

**Cambios realizados:**
- Agregado nuevo menuitem "Dashboard de Tesorería" después del Home
- Icono: `ui-icon-dashboard`
- Outcome: `/pages/tesoreria/index`

**Código agregado:**
```xml
<!-- Dashboard de Tesorería -->
<p:menuitem value="Dashboard de Tesorería" 
            outcome="/pages/tesoreria/index" 
            icon="ui-icon-dashboard"/>
```

### 2. styles.css

**Ubicación:** `/src/main/webapp/resources/css/styles.css`

**Cambios realizados:**
- Consolidación de estilos de manhattan-layout
- Estilos para el dashboard de tesorería
- Clases para tarjetas de estadísticas
- Estilos responsivos

**Clases nuevas agregadas:**
```css
.dashboard-stats { }          /* Contenedor de estadísticas */
.stat-card { }                /* Tarjeta de estadística */
.stat-value { }               /* Valor numérico grande */
.stat-percentage { }          /* Porcentaje */
.quick-access { }             /* Contenedor de accesos rápidos */
.quick-card { }               /* Tarjeta de acceso rápido */
.badge { }                    /* Badges de estado */
```

### 3. layout.js

**Ubicación:** `/src/main/webapp/resources/scripts/layout.js`

**Cambios realizados:**
- Eliminada dependencia de nanoScroller
- Uso de scroll nativo del navegador
- Mantenida funcionalidad de menú responsivo
- Compatibilidad con el layout existente

## Módulos del Sistema Original Utilizados

El sistema aprovecha los controladores y entidades ya existentes en el proyecto:

### Controladores Existentes Vinculados

1. **FnCertificadosDisponibilidadController** - Para CDPs
2. **FnComprobantesDePagoController** - Para pagos
3. **FnObjetosDeGastoController** - Para objetos del gasto
4. **FnFuentesDeFinanciamientoController** - Para fuentes de financiamiento
5. **FnPlanFinancieroController** - Para plan financiero
6. **FnPresupuestoController** - Para presupuesto
7. **FnReprogramacionesController** - Para reprogramaciones
8. **FnRetencionesController** - Para retenciones

### Navegación entre Módulos

El dashboard de tesorería incluye navegación directa a:

```java
/pages/fnComprobantesDePago/index      // Pagos pendientes
/pages/fnRetenciones/index             // Retenciones
/pages/fnCertificadosDisponibilidad/index  // CDPs
/pages/fnCompromisos/index             // Compromisos (si existe)
```

## Tecnologías Utilizadas

### Backend
- **Jakarta EE 10** - Plataforma empresarial
- **JavaServer Faces (JSF)** - Framework MVC
- **PrimeFaces 15** - Biblioteca de componentes UI
- **JPA** - Persistencia de datos
- **CDI** - Inyección de dependencias

### Frontend
- **XHTML** - Vistas
- **PrimeFaces Components** - UI rica
- **CSS3** - Estilos
- **JavaScript** - Interactividad

## Integración con Base de Datos

### Estado Actual
El controlador `TesoreriaController` utiliza datos de ejemplo (mock data) para demostración.

### Próximos Pasos para Integración Real

1. **Inyectar EntityManager:**
```java
@PersistenceContext
private EntityManager em;
```

2. **Crear consultas JPA/JPQL:**
```java
private void loadDashboardData() {
    presupuestoTotal = (BigDecimal) em.createQuery(
        "SELECT SUM(p.monto) FROM FnPresupuesto p WHERE p.periodo = :periodo"
    ).setParameter("periodo", getCurrentPeriodo()).getSingleResult();
    
    // ... más consultas
}
```

3. **Utilizar los controladores existentes:**
```java
@Inject
private FnPresupuestoController presupuestoController;

@Inject
private FnComprobantesDePagoController pagosController;
```

## Configuración de PrimeFaces Charts

Los gráficos utilizan la API de PrimeFaces Charts:

### Dependencias Requeridas (ya incluidas en pom.xml)
```xml
<dependency>
    <groupId>org.primefaces</groupId>
    <artifactId>primefaces</artifactId>
    <version>15.0.0</version>
</dependency>
```

### Configuración de Gráficos

**Gráfico de Barras (Ejecución Mensual):**
```java
BarChartModel model = new BarChartModel();
ChartSeries series = new ChartSeries();
series.set("Enero", 250000000);
// ... más datos
model.addSeries(series);
model.setTitle("Evolución Mensual");
```

**Gráfico de Torta (Distribución por Rubro):**
```java
PieChartModel model = new PieChartModel();
model.set("Servicios Personales", 1800000000);
// ... más datos
model.setTitle("Distribución por Rubro");
```

## Permisos y Seguridad

### Integración con Sistema de Permisos Existente

Para agregar control de acceso al dashboard de tesorería:

1. **Agregar permiso en base de datos:**
```sql
INSERT INTO permisos (url, descripcion) 
VALUES ('/pages/tesoreria/index.xhtml', 'Dashboard de Tesorería');
```

2. **Modificar sidebar.xhtml:**
```xml
<p:menuitem value="Dashboard de Tesorería" 
            outcome="/pages/tesoreria/index" 
            icon="ui-icon-dashboard"
            rendered="#{filtroURL.verifPermiso('/pages/tesoreria/index.xhtml')}"/>
```

## Compilación y Despliegue

### Requisitos
- JDK 21
- Maven 3.8+
- Servidor de aplicaciones compatible con Jakarta EE 10 (WildFly, Payara, etc.)

### Comandos Maven

**Compilar el proyecto:**
```bash
mvn clean compile
```

**Empaquetar WAR:**
```bash
mvn clean package
```

**Desplegar en servidor:**
```bash
mvn wildfly:deploy
```

## Pruebas

### Verificar la Integración

1. **Compilar el proyecto:**
   ```bash
   cd annp_admin_mejorado
   mvn clean install
   ```

2. **Desplegar en servidor de aplicaciones**

3. **Acceder al dashboard:**
   ```
   http://localhost:8080/annp_admin/pages/tesoreria/index.xhtml
   ```

4. **Verificar:**
   - Estadísticas se muestran correctamente
   - Gráficos se renderizan
   - Enlaces de navegación funcionan
   - Tabla de pagos se muestra con paginación

## Personalización

### Modificar Datos del Dashboard

Editar el método `loadDashboardData()` en `TesoreriaController.java`:

```java
private void loadDashboardData() {
    // Reemplazar con consultas reales a la base de datos
    presupuestoTotal = obtenerPresupuestoTotalDesdeDB();
    totalEjecutado = obtenerTotalEjecutadoDesdeDB();
    // ...
}
```

### Agregar Nuevos Gráficos

1. Crear el modelo del gráfico en el controlador
2. Agregar el componente `<p:chart>` en dashboard.xhtml
3. Configurar el tipo y los datos

### Modificar Estilos

Editar `/src/main/webapp/resources/css/styles.css`:

```css
.stat-card {
    background: #ffffff;
    border-radius: 8px;
    padding: 20px;
    /* Personalizar aquí */
}
```

## Soporte y Mantenimiento

### Archivos Clave para el Equipo de Desarrollo

1. **TesoreriaController.java** - Lógica del dashboard
2. **dashboard.xhtml** - Vista del dashboard
3. **sidebar.xhtml** - Menú de navegación
4. **styles.css** - Estilos personalizados

### Tareas Pendientes (TODO)

- [ ] Conectar con base de datos real
- [ ] Implementar consultas JPA para estadísticas
- [ ] Agregar filtros por periodo/fecha
- [ ] Implementar exportación de reportes
- [ ] Agregar más gráficos según necesidades
- [ ] Configurar permisos de acceso
- [ ] Crear tests unitarios
- [ ] Optimizar consultas de base de datos

## Contacto

Para consultas sobre la integración, contactar al equipo de desarrollo.

---

**Desarrollado por:** TIC © 2025  
**Versión del Proyecto:** 1.0.0  
**Última Actualización:** Noviembre 2025
