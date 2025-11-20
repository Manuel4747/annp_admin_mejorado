# Manual de Instalación y Ajuste - ANNP Admin

## 1. Requisitos del Sistema

Para compilar y ejecutar este proyecto, necesitará el siguiente entorno de desarrollo:

- **Java Development Kit (JDK)**: Versión 21 o superior.
- **Apache Maven**: Versión 3.8 o superior.
- **Servidor de Aplicaciones**: Compatible con Jakarta EE 10 (por ejemplo, Payara, WildFly, GlassFish).
- **Base de Datos**: MySQL 8.0 o superior.

---

## 2. Configuración del Entorno

### 2.1. Variables de Entorno

Asegúrese de que las siguientes variables de entorno estén configuradas correctamente en su sistema:

- `JAVA_HOME`: Debe apuntar al directorio de instalación de su JDK 21.
- `MAVEN_HOME`: Debe apuntar al directorio de instalación de Maven.

Agregue los directorios `bin` de Java y Maven a la variable `PATH` de su sistema para poder ejecutar los comandos `java`, `javac` y `mvn` desde cualquier ubicación.

### 2.2. Configuración de la Base de Datos

1.  **Crear la Base de Datos**: Cree una nueva base de datos en su servidor MySQL.
2.  **Ejecutar Scripts SQL**: Si el proyecto incluye scripts de inicialización de base de datos, ejecútelos para crear las tablas y datos necesarios.
3.  **Configurar la Conexión**: La configuración de la conexión a la base de datos generalmente se encuentra en un archivo de persistencia (`persistence.xml`) o en la configuración del servidor de aplicaciones. Deberá ajustar los siguientes parámetros:

    - URL de la base de datos
    - Nombre de usuario
    - Contraseña

---

## 3. Compilación del Proyecto

Siga estos pasos para compilar el proyecto y generar el archivo WAR (Web Application Archive):

1.  **Navegar al Directorio del Proyecto**:
    Abra una terminal o línea de comandos y navegue a la raíz del proyecto `annp_admin_mejorado`.

    ```bash
    cd /ruta/a/su/proyecto/annp_admin_mejorado
    ```

2.  **Limpiar y Compilar con Maven**:
    Ejecute el siguiente comando de Maven. Esto limpiará compilaciones anteriores, compilará el código fuente y empaquetará la aplicación en un archivo `.war`.

    ```bash
    mvn clean install
    ```

3.  **Verificar el Archivo WAR**:
    Una vez que la compilación sea exitosa, encontrará el archivo `annp_pf15-1.0.war` (o un nombre similar) dentro del directorio `target/`.

---

## 4. Despliegue en el Servidor de Aplicaciones

El método de despliegue puede variar ligeramente dependiendo de su servidor de aplicaciones.

### Ejemplo con Payara Server:

1.  **Iniciar el Servidor Payara**.
2.  **Acceder a la Consola de Administración**:
    Abra su navegador y vaya a `http://localhost:4848` (el puerto por defecto de la consola de Payara).
3.  **Desplegar la Aplicación**:
    - En el menú de la izquierda, vaya a `Applications`.
    - Haga clic en el botón `Deploy...`.
    - Seleccione la opción "Packaged file to be uploaded to the server".
    - Haga clic en `Browse...` o `Choose File` y seleccione el archivo `.war` generado en el paso anterior (ubicado en el directorio `target/`).
    - Deje las opciones por defecto y haga clic en `OK`.

Una vez desplegada, la aplicación debería estar accesible en una URL similar a `http://localhost:8080/annp_pf15-1.0/`.

---

## 5. Manual de Ajustes y Personalización

### 5.1. Estilos Visuales (CSS)

Todos los estilos principales del layout se encuentran en un único archivo para facilitar la personalización:

- **Archivo Principal**: `src/main/webapp/resources/css/styles.css`

Dentro de este archivo, encontrará secciones comentadas que le permitirán modificar:
- Colores del `sidebar` y `topbar`.
- Tipografías y tamaños de fuente.
- Puntos de quiebre (breakpoints) para el diseño responsivo.
- Estilos de los componentes del menú.

### 5.2. Comportamiento del Layout (JavaScript)

La lógica para el menú responsivo y el comportamiento del layout se ha centralizado en dos archivos:

- **`layout.js`**: Contiene la lógica original del layout (adaptada).
- **`responsive-menu.js`**: Contiene la nueva lógica para el comportamiento responsivo, el menú hamburguesa y las animaciones.

Si necesita ajustar las animaciones o el comportamiento en dispositivos móviles, deberá modificar `responsive-menu.js`.

### 5.3. Estructura de la Plantilla (XHTML)

Los archivos principales que componen la estructura de la página son:

- `src/main/webapp/WEB-INF/template.xhtml`: La plantilla principal que une todas las partes.
- `src/main/webapp/WEB-INF/topbar.xhtml`: La barra de navegación superior.
- `src/main/webapp/WEB-INF/sidebar.xhtml`: El menú lateral.
- `src/main/webapp/WEB-INF/footer.xhtml`: El pie de página.

Para agregar o quitar elementos del menú, deberá editar `sidebar.xhtml`.

---

## 6. Solución de Problemas Comunes

- **Error de dependencias de Maven**: Si Maven no puede descargar las dependencias, verifique su conexión a internet y la configuración de su `settings.xml` de Maven.
- **La aplicación no se despliega**: Revise los logs del servidor de aplicaciones para obtener mensajes de error detallados. Las causas comunes incluyen una versión incorrecta de Java o problemas de conexión con la base de datos.
- **Los estilos no se cargan correctamente**: Limpie la caché de su navegador. Verifique que el archivo `.war` se haya desplegado correctamente y que las rutas a los archivos CSS en `template.xhtml` sean correctas.

---

**Fecha del Manual**: Noviembre 2025
