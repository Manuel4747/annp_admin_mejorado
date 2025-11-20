# ANNP Admin - Proyecto Mejorado

## Descripción

Este proyecto es una aplicación web de administración desarrollada con **PrimeFaces 15**, **JDK 21** y **Jakarta EE 10**. Ha sido mejorado con un diseño completamente responsivo y estilos independientes de la librería manhattan-layout.

---

## Características Principales

### ✅ Diseño Responsivo
- **Desktop**: Menú lateral fijo a la izquierda, topbar fijo en la parte superior
- **Tablet/Móvil**: Menú hamburguesa que se despliega como overlay
- **Adaptativo**: Se ajusta automáticamente a cualquier tamaño de pantalla

### ✅ Mejoras Implementadas
- Eliminación de dependencias de manhattan-layout
- Estilos consolidados en un único archivo CSS
- JavaScript optimizado sin dependencias externas innecesarias
- Transiciones y animaciones suaves
- Código limpio y bien documentado

---

## Estructura del Proyecto

```
annp_admin_mejorado/
├── src/
│   ├── main/
│   │   ├── java/              # Código Java del backend
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── template.xhtml    # Plantilla principal
│   │       │   ├── topbar.xhtml      # Barra superior
│   │       │   ├── sidebar.xhtml     # Menú lateral
│   │       │   └── footer.xhtml      # Pie de página
│   │       └── resources/
│   │           ├── css/
│   │           │   └── styles.css    # Estilos principales
│   │           └── scripts/
│   │               ├── layout.js           # Lógica del layout
│   │               └── responsive-menu.js  # Menú responsivo
├── pom.xml                    # Configuración de Maven
├── MEJORAS_IMPLEMENTADAS.md   # Documentación de mejoras
├── MANUAL_INSTALACION.md      # Manual de instalación
└── MANUAL_USUARIO.md          # Manual de usuario
```

---

## Requisitos del Sistema

- **JDK**: 21 o superior
- **Maven**: 3.8 o superior
- **Servidor de Aplicaciones**: Compatible con Jakarta EE 10 (Payara, WildFly, GlassFish)
- **Base de Datos**: MySQL 8.0 o superior

---

## Instalación Rápida

1. **Clonar o extraer el proyecto**
   ```bash
   unzip annp_admin_mejorado_completo.zip
   cd annp_admin_mejorado
   ```

2. **Configurar la base de datos**
   - Crear una base de datos MySQL
   - Ajustar la configuración de conexión en `persistence.xml` o en el servidor de aplicaciones

3. **Compilar el proyecto**
   ```bash
   mvn clean install
   ```

4. **Desplegar en el servidor**
   - Copiar el archivo `.war` generado en `target/` al servidor de aplicaciones
   - O usar la consola de administración del servidor para desplegar

---

## Documentación

Este proyecto incluye documentación completa:

- **[MEJORAS_IMPLEMENTADAS.md](MEJORAS_IMPLEMENTADAS.md)**: Detalle de todas las mejoras y cambios realizados
- **[MANUAL_INSTALACION.md](MANUAL_INSTALACION.md)**: Guía paso a paso para instalar y configurar el proyecto
- **[MANUAL_USUARIO.md](MANUAL_USUARIO.md)**: Manual para usuarios finales del sistema

---

## Demo en Línea

Se ha creado una demostración funcional del nuevo diseño responsivo que puede visualizar en:

**URL de Demo**: [Ver Demo en Vivo](#)

La demo muestra todas las características del nuevo diseño sin necesidad de instalar el proyecto completo.

---

## Cambios Principales

### 1. Eliminación de Manhattan Layout
- Se extrajeron todos los estilos necesarios a `styles.css`
- Se eliminaron referencias a archivos CSS y JS de manhattan-layout
- El proyecto ahora es completamente independiente

### 2. Layout Mejorado
- Sidebar fijo a la izquierda (270px)
- Topbar fijo en la parte superior
- Contenido centrado y scrollable
- Sistema de flexbox moderno

### 3. Diseño Responsivo
- Breakpoints en 1280px, 768px y 480px
- Menú hamburguesa automático en pantallas pequeñas
- Overlay con máscara de fondo
- Transiciones suaves

---

## Soporte y Contacto

Para consultas o problemas relacionados con este proyecto, por favor contacte al equipo de desarrollo.

**Versión**: 2.0  
**Fecha**: Noviembre 2025  
**Tecnologías**: PrimeFaces 15, Jakarta EE 10, JDK 21

---

## Licencia

Este proyecto es propiedad de ANNP y está protegido por las leyes de derechos de autor aplicables.
