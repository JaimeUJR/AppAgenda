# 📅 Agenda Personal - Proyecto Java

## 📋 Descripción
Aplicación de escritorio para gestión de agenda personal desarrollada en Java con interfaz Swing. Permite gestionar eventos, citas y categorías con base de datos MySQL.

## 🏗️ Arquitectura del Proyecto

### 📁 Estructura de Carpetas Creadas

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
│   │   ├── 📂 vista/                   ← VISTA (V) - Interfaz gráfica
│   │   │   └── VentanaPrincipal.java   ← Ventana principal
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

## 🚀 Estado Actual del Desarrollo

### ✅ **COMPLETADO:**

#### 🔧 **Configuración:**
- ✅ Estructura de carpetas MVC
- ✅ Configuración Maven con dependencias
- ✅ Archivos de configuración de BD
- ✅ Base de datos MySQL con datos de prueba

#### 📦 **Modelo (Entidades):**
- ✅ Usuario.java - Gestión de usuarios
- ✅ Evento.java - Eventos/citas
- ✅ Categoria.java - Categorías para eventos
- ✅ EstadoEvento.java - Estados (pendiente, completado, cancelado)
- ✅ EventoDTO.java - Para vistas complejas
- ✅ CategoriaDTO.java - Para estadísticas

#### 🗄️ **DAO (Acceso a Datos):**
- ✅ ConexionBD.java - Gestión de conexión MySQL
- ✅ UsuarioDAO.java - CRUD completo usuarios
- ✅ EventoDAO.java - CRUD eventos con stored procedures
- ✅ CategoriaDAO.java - CRUD categorías

#### 🎮 **Controladores:**
- ✅ AgendaControlador.java - Coordinador principal
- ✅ UsuarioControlador.java - Autenticación y perfil
- ✅ EventoControlador.java - Gestión de eventos
- ✅ CategoriaControlador.java - Gestión de categorías

#### 🖥️ **Vista (Interfaz):**
- ✅ VentanaPrincipal.java - Ventana principal con login/dashboard
- ✅ Interfaz básica funcional

## 🔧 **Dependencias Configuradas (pom.xml)**

```xml
- MySQL Connector J 8.4.0        ← Conexión MySQL
- NetBeans AbsoluteLayout         ← Diseñador visual
- SLF4J + Logback                 ← Sistema de logging
- Java 17                         ← Versión estable LTS
```

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

## 🚀 **Cómo Ejecutar**

### **Opción 1: Desde NetBeans**
1. Abrir proyecto en NetBeans
2. Click derecho → "Run Project"
3. O ejecutar clase principal: `AgendaPersonal.java`

### **Opción 2: Desde línea de comandos**
```bash
# Compilar
mvn compile

# Ejecutar
mvn exec:java

# Crear JAR ejecutable
mvn package
```

### **Opción 3: JAR independiente**
```bash
# Después de mvn package
java -jar target/AgendaPersonal-1.0-SNAPSHOT.jar
```

## 🔍 **Funcionalidades Implementadas**

### **✅ Sistema de Autenticación:**
- Login/logout de usuarios
- Gestión de sesiones
- Validación de credenciales

### **✅ Dashboard Principal:**
- Panel de login intuitivo
- Dashboard con resumen de datos
- Botón de usuario de prueba

### **✅ Gestión de Base de Datos:**
- Conexión automática a MySQL
- Manejo de errores de conexión
- Stored procedures para operaciones complejas

### **✅ Arquitectura Robusta:**
- Patrón MVC implementado
- Separación clara de responsabilidades
- Validaciones en controladores
- Manejo de excepciones

## 🔄 **Próximos Pasos Sugeridos**

### **🎯 Funcionalidades por Implementar:**

1. **📅 Gestión de Eventos:**
   - Ventana crear/editar eventos
   - Lista de eventos con filtros
   - Calendario visual
   - Recordatorios

2. **📁 Gestión de Categorías:**
   - CRUD completo de categorías
   - Asignación a eventos
   - Estadísticas

3. **👤 Gestión de Usuario:**
   - Editar perfil
   - Cambiar contraseña
   - Registro de nuevos usuarios

4. **📊 Reportes y Estadísticas:**
   - Eventos por período
   - Estadísticas de categorías
   - Exportar datos

5. **🎨 Mejoras de UI:**
   - Iconos y gráficos
   - Temas de colores
   - Validaciones visuales

## 🛠️ **Herramientas y Tecnologías**

- **☕ Java 17** - Lenguaje principal
- **🖥️ Swing** - Interfaz gráfica
- **🗄️ MySQL** - Base de datos
- **📦 Maven** - Gestión de dependencias
- **🔧 NetBeans** - IDE de desarrollo
- **🏗️ MVC** - Patrón arquitectónico

## 📝 **Notas Importantes**

1. **Configuración BD:** Asegúrate de que MySQL esté ejecutándose y la BD importada
2. **Dependencias:** Maven descargará automáticamente las dependencias
3. **Estructura:** El proyecto sigue estrictamente el patrón MVC
4. **Extensibilidad:** La arquitectura permite agregar nuevas funcionalidades fácilmente
5. **Datos de Prueba:** Usa los usuarios de prueba para testing inicial

## 📞 **Soporte**

Si encuentras problemas:
1. Verifica la conexión a MySQL
2. Confirma que la BD esté importada
3. Revisa el archivo `database.properties`
4. Compila el proyecto con `mvn clean compile`

---

**🎉 ¡Proyecto listo para desarrollo adicional!** 

La base está completamente implementada con arquitectura sólida, conexión a BD funcional y interfaz básica operativa.