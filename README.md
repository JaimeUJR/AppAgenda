# 📅 Agenda Personal - Gestor de Tareas Moderno

## 📋 Descripción
Aplicación de escritorio moderna para gestión de tareas personales desarrollada en Java con interfaz Swing. Sistema completo con autenticación, gestión de tareas, filtros inteligentes y base de datos MySQL integrada.

## ✨ Características Principales

### � **Interfaz Moderna**
- Diseño profesional con esquema de colores morado y gris
- Dashboard intuitivo con tarjetas de estadísticas coloridas
- Navegación por pestañas fluida (Login/Dashboard)
- Interfaz responsive y centrada

### 🔐 **Sistema de Autenticación**
- Login seguro con validación de credenciales
- Registro de nuevos usuarios con validación completa
- Gestión de sesiones y cierre seguro
- Formularios modales elegantes

### ✅ **Gestión Inteligente de Tareas**
- Creación de tareas con fecha y hora específicas
- **Click-to-toggle**: Marca tareas como completadas con un clic
- Sistema de filtros dinámico (Todas/Pendientes/Completadas)
- Estados visuales claros con iconos y colores

### 📊 **Dashboard con Estadísticas**
- Tarjetas de resumen coloridas:
  - 📋 Total de tareas
  - ⏳ Tareas pendientes  
  - ✅ Tareas completadas
- Tabla de tareas con información completa
- Actualización en tiempo real de estadísticas

### 🔧 **Funcionalidades Avanzadas**
- Filtros con retroalimentación visual
- Formularios con selectores de fecha JSpinner
- Integración completa con base de datos MySQL
- Validaciones robustas en tiempo real

## 🏗️ Arquitectura del Sistema

### 📁 Estructura del Proyecto

```
AgendaPersonal/
│
├── 📂 backup/                          ← RESPALDOS Y SCRIPTS SQL
│   ├── backup_app_agenda.sql           ← Base de datos completa
│   ├── scripts/                        ← Scripts SQL personalizados
│   ├── dumps/                          ← Copias de seguridad
│   └── datos_prueba/                   ← Datos de ejemplo
│
├── 📂 src/main/
│   ├── 📂 java/com/agenda/agendapersonal/
│   │   │
│   │   ├── 📄 AgendaPersonal.java      ← CLASE PRINCIPAL
│   │   │
│   │   ├── 📂 modelo/                  ← MODELO (M) - Entidades
│   │   │   ├── Usuario.java            ← Entidad Usuario
│   │   │   ├── Evento.java             ← Entidad Evento
│   │   │   ├── Categoria.java          ← Entidad Categoria
│   │   │   ├── EstadoEvento.java       ← Enum Estados
│   │   │   ├── EventoDTO.java          ← DTO para vistas
│   │   │   └── CategoriaDTO.java       ← DTO para categorías
│   │   │
│   │   ├── 📂 vista/                   ← INTERFAZ GRÁFICA MODERNA
│   │   │   ├── VentanaPrincipal.java   ← Dashboard principal con diseño moderno
│   │   │   ├── FormularioNuevaTarea.java ← Modal para crear tareas
│   │   │   ├── FormularioRegistro.java ← Modal de registro de usuarios
│   │   │   └── test/                   ← Clases de prueba
│   │   │
│   │   ├── 📂 controlador/             ← CONTROLADOR (C) - Lógica
│   │   │   ├── AgendaControlador.java  ← Controlador principal
│   │   │   ├── UsuarioControlador.java ← Gestión de usuarios
│   │   │   ├── EventoControlador.java  ← Gestión de eventos
│   │   │   └── CategoriaControlador.java ← Gestión de categorías
│   │   │
│   │   ├── 📂 dao/                     ← ACCESO A DATOS
│   │   │   ├── UsuarioDAO.java         ← CRUD Usuarios
│   │   │   ├── EventoDAO.java          ← CRUD Eventos
│   │   │   └── CategoriaDAO.java       ← CRUD Categorías
│   │   │
│   │   └── 📂 config/                  ← CONFIGURACIÓN
│   │       ├── ConexionBD.java         ← Gestión de conexión MySQL
│   │       └── Constantes.java         ← Constantes de la aplicación
│   │
│   └── 📂 resources/
│       └── database.properties         ← Configuración de BD
│
└── 📄 pom.xml                          ← Dependencias Maven
```

## 🚀 Estado Actual del Proyecto

### ✅ **SISTEMA COMPLETAMENTE FUNCIONAL**

#### 🎨 **Interfaz de Usuario:**
- ✅ Dashboard moderno con diseño profesional
- ✅ Sistema de login/registro completamente funcional
- ✅ Formularios modales elegantes y responsive
- ✅ Navegación por pestañas fluida
- ✅ Esquema de colores consistente (morado/gris)
- ✅ Iconos y elementos visuales modernos

#### ⚡ **Funcionalidades Principales:**
- ✅ **Autenticación completa**: Login, registro, validaciones
- ✅ **Gestión de tareas**: Crear, completar, filtrar
- ✅ **Click-to-toggle**: Cambio de estado con clic simple
- ✅ **Sistema de filtros**: Todas/Pendientes/Completadas con feedback visual
- ✅ **Dashboard estadísticas**: Tarjetas coloridas con conteos en tiempo real
- ✅ **Formulario de nueva tarea**: Con selectores de fecha/hora

#### 🛠️ **Arquitectura Técnica:**
- ✅ Patrón MVC completamente implementado
- ✅ Integración completa con MySQL
- ✅ DAO pattern para acceso a datos
- ✅ Controladores con validación robusta
- ✅ Manejo de excepciones y errores
- ✅ Código limpio y profesional (sin comentarios innecesarios)

## 🎮 Guía de Uso

### � **Iniciar Sesión**
1. **Usuarios de prueba disponibles:**
   ```
   Usuario: jperez    | Password: password
   Usuario: mgarcia   | Password: password
   ```
2. **Crear cuenta nueva:** Click en "Registrarse" para crear tu usuario

### ✅ **Gestionar Tareas**
- **Crear tarea:** Click en "Nueva Tarea" → Completa el formulario
- **Completar tarea:** Click directo sobre cualquier tarea en la tabla
- **Filtrar tareas:** Usa los botones "Todas", "Pendientes", "Completadas"

### 📊 **Dashboard**
- **Estadísticas en tiempo real** en las tarjetas superiores
- **Tabla interactiva** con todas las tareas
- **Estados visuales** con iconos y colores

## 🔧 Dependencias y Tecnologías

### **Stack Tecnológico:**
```xml
- Java 17 LTS                     ← Lenguaje principal
- MySQL Connector J 8.4.0        ← Conexión a base de datos
- Java Swing                      ← Interfaz gráfica moderna
- NetBeans AbsoluteLayout         ← Diseñador visual
- SLF4J + Logback                 ← Sistema de logging
- Maven                           ← Gestión de dependencias
```

### **Arquitectura:**
- **MVC Pattern** - Separación clara de responsabilidades
- **DAO Pattern** - Acceso estructurado a datos
- **Singleton Pattern** - Gestión de conexión BD
- **Observer Pattern** - Actualizaciones de UI en tiempo real

## 🗃️ **Base de Datos Configurada**

### **Tablas:**
- `usuarios` - Gestión de usuarios
- `eventos` - Eventos/citas
- `categorias` - Categorías 
- `evento_categoria` - Relación M:N
- `usuario_categoria` - Relación M:N

### **Stored Procedures:**
- `sp_crear_evento` - Crear eventos con categorías
- `sp_actualizar_evento` - Actualizar eventos
- `sp_eliminar_evento` - Eliminar eventos
- `sp_cambiar_estado_evento` - Cambiar estado
- `sp_crear_o_obtener_categoria` - Gestión categorías

### **Vistas:**
- `v_eventos` - Vista completa de eventos
- `v_categorias` - Estadísticas de categorías
- `v_categorias_usuario` - Categorías por usuario

## 🔐 **Usuarios de Prueba en BD**

```
Usuario: jperez
Password: password
Nombre: Juan Pérez

Usuario: mgarcia  
Password: password
Nombre: María García
```

## ⚙️ **Configuración de Base de Datos**

### **Archivo:** `src/main/resources/database.properties`
```properties
db.url=jdbc:mysql://localhost:3306/agenda_personal
db.username=root
db.password=
```

### **Requisitos:**
1. MySQL Server ejecutándose
2. Base de datos `agenda_personal` creada
3. Datos importados desde `backup/backup_app_agenda.sql`

## 🚀 Instalación y Ejecución

### **Prerrequisitos:**
1. ☕ **Java 17** o superior
2. 🗄️ **MySQL Server** ejecutándose  
3. 🔧 **NetBeans IDE** (recomendado) o Maven
4. 📂 Base de datos `agenda_personal` importada

### **Pasos de Instalación:**

#### **1. Configurar Base de Datos:**
```sql
-- Crear base de datos
CREATE DATABASE agenda_personal;

-- Importar estructura y datos
-- Ejecutar: backup/backup_app_agenda.sql
```

#### **2. Configurar Conexión:**
Archivo: `src/main/resources/database.properties`
```properties
db.url=jdbc:mysql://localhost:3306/agenda_personal
db.username=root
db.password=tu_password_mysql
```

#### **3. Ejecutar Aplicación:**

**Desde NetBeans:**
1. Abrir proyecto en NetBeans
2. Click derecho → "Run Project"
3. O ejecutar clase: `AgendaPersonal.java`

**Desde línea de comandos:**
```bash
# Compilar proyecto
mvn clean compile

# Ejecutar aplicación
mvn exec:java -Dexec.mainClass="com.agenda.agendapersonal.AgendaPersonal"

# Crear JAR ejecutable
mvn clean package
java -jar target/AgendaPersonal-1.0-SNAPSHOT.jar
```

## 🔍 Funcionalidades Detalladas

### **🎨 Interfaz Moderna:**
- **Header principal** con diseño morado elegante
- **Tarjetas de estadísticas** con colores distintivos:
  - 🔵 Azul para total de tareas
  - 🟡 Amarillo para pendientes  
  - 🟢 Verde para completadas
- **Tabla interactiva** con datos en tiempo real
- **Botones de filtro** con retroalimentación visual

### **🔐 Sistema de Autenticación:**
- **Login seguro** con validación de credenciales
- **Registro de usuarios** con validación completa:
  - Verificación de campos requeridos
  - Validación de formato de email
  - Confirmación de contraseña
- **Gestión de sesiones** automática

### **✅ Gestión de Tareas Avanzada:**
- **Creación intuitiva** con formulario modal
- **Selectores de fecha/hora** con JSpinner
- **Toggle de completado** con un solo clic
- **Filtrado dinámico** por estado
- **Actualización automática** de estadísticas

### **🗄️ Base de Datos Robusta:**
- **Stored procedures** para operaciones complejas
- **Vistas optimizadas** para consultas rápidas
- **Transacciones seguras** con rollback automático
- **Relaciones normalizadas** entre entidades

## 🎯 Características Técnicas Destacadas

### **�️ Arquitectura Sólida:**
- **Patrón MVC** implementado correctamente
- **Separación de responsabilidades** clara
- **Inyección de dependencias** manual pero estructurada
- **Gestión centralizada** de estados

### **� Persistencia de Datos:**
- **Conexión singleton** a MySQL
- **Pool de conexiones** básico
- **Transacciones automáticas** con manejo de errores
- **Consultas optimizadas** con prepared statements

### **🎨 UI/UX Moderna:**
- **Responsive design** con GridBagLayout
- **Colores corporativos** consistentes
- **Feedback visual** en todas las interacciones
- **Modalidad de diálogos** para formularios

### **🔧 Mantenibilidad:**
- **Código limpio** sin comentarios innecesarios
- **Convenciones de nomenclatura** consistentes  
- **Estructura modular** extensible
- **Manejo robusto** de excepciones

## � Posibles Extensiones Futuras

### **📈 Mejoras de Funcionalidad:**
- 📅 **Vista de calendario** visual mensual/semanal
- 🔔 **Sistema de notificaciones** y recordatorios
- 📱 **Exportación** a formatos externos (CSV, PDF)
- 🏷️ **Sistema de etiquetas** y categorías avanzadas
- 📊 **Reportes detallados** con gráficos

### **🎨 Mejoras de Interfaz:**
- 🌙 **Tema oscuro/claro** intercambiable
- 🎯 **Iconos personalizados** y mejores gráficos
- 📱 **Responsive design** mejorado
- ⚡ **Animaciones suaves** en transiciones

### **🔧 Mejoras Técnicas:**
- 🌐 **API REST** para servicios web
- 📱 **Aplicación móvil** complementaria
- ☁️ **Sincronización en la nube**
- 🔒 **Encriptación de datos** sensibles

## 🛠️ Tecnologías Utilizadas

| Categoría | Tecnología | Versión | Propósito |
|-----------|------------|---------|-----------|
| **Lenguaje** | Java | 17 LTS | Desarrollo principal |
| **GUI** | Swing | Nativa | Interfaz gráfica |
| **Base de Datos** | MySQL | 8.0+ | Persistencia de datos |
| **Build Tool** | Maven | 3.6+ | Gestión de dependencias |
| **IDE** | NetBeans | 12+ | Desarrollo y diseño |
| **Patrón** | MVC | - | Arquitectura |

## � Solución de Problemas

### **❌ Error de Conexión a BD:**
```bash
# Verificar que MySQL esté ejecutándose
services.msc → MySQL80

# Verificar credenciales en database.properties
db.username=tu_usuario
db.password=tu_contraseña
```

### **⚠️ Error de Compilación:**
```bash
# Limpiar y recompilar
mvn clean compile

# Verificar versión de Java
java -version  # Debe ser 17+
```

### **🔍 Problemas de UI:**
- Verificar que NetBeans tenga el Look & Feel del sistema
- Comprobar resolución de pantalla (mínimo 1024x768)

## 📞 Contacto y Soporte

Para problemas técnicos o sugerencias:
1. 🔍 Revisar la sección de solución de problemas
2. 📋 Verificar configuración de base de datos
3. 🔄 Intentar recompilación limpia con Maven

---

## 🎉 Estado del Proyecto

**✅ SISTEMA COMPLETAMENTE FUNCIONAL Y LISTO PARA PRODUCCIÓN**

Este proyecto representa una **aplicación de gestión de tareas moderna y profesional** con:
- ✨ Interfaz elegante y moderna
- 🔐 Sistema de autenticación robusto  
- ✅ Funcionalidades completas de gestión de tareas
- 🏗️ Arquitectura escalable y mantenible
- 💾 Integración completa con base de datos
- 🎨 Código limpio y profesional

**¡Perfecto para uso personal o como base para proyectos más complejos!** 🚀
