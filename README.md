<div align="center">

# 🎭 Desfile San Pedrito Manager

### Aplicación móvil para la gestión de participantes del desfile de San Pedrito

[![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Material Design](https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=material-design&logoColor=white)](https://material.io/)

---

**Desarrollado por:** [Ilan Angeles Rodriguez](https://www.linkedin.com/in/ilan-angeles-rodriguez)  
**Universidad:** Universidad Nacional del Santa  
**Curso:** Aplicaciones Móviles

</div>

## 📱 Capturas de Pantalla

<div align="center">

### 📊 Panel de Estadísticas
<img src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-9elZsM1tISsAhpa4jEuWxbeE2VdAK3.png" alt="Estadísticas del Desfile" width="300"/>

*Vista completa de métricas y distribuciones de participantes*

### 👥 Lista de Participantes
<img src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-fkdMU8MWr7zXZYAXOBGK31V4G0eRkd.png" alt="Lista de Participantes" width="300"/>

*Gestión completa de participantes con información detallada*

### ✍️ Registro de Participantes
<img src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-DWNaAWuuxHW02zXQ74dEp1IqQCij6P.png" alt="Formulario de Registro" width="300"/>

*Formulario intuitivo para el registro de nuevos participantes*

</div>

## 🚀 Características Principales

### 📝 **Registro Intuitivo**
- Formulario completo con validación de datos
- Campos para información personal y de contacto
- Selección de tipo de participante y categoría
- Interfaz amigable y fácil de usar

### 📋 **Gestión Avanzada**
- **Listado completo** de participantes activos e inactivos
- **Búsqueda inteligente** por nombre, email o teléfono
- **Filtros automáticos** por tipo y categoría
- **Ordenamiento flexible** por múltiples criterios

<div align="center">
<img src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-SlSrcEr2PXmA7eqBvmSQXninrjxgmG.png" alt="Opciones de Ordenamiento" width="300"/>
</div>

### 📊 **Estadísticas Detalladas**
- Total de participantes registrados
- Conteo de participantes activos
- Edad promedio de participantes
- Distribución por tipo de participante
- Distribución por categoría
- Análisis por rango de edad
- Registros recientes

### ⚡ **Control de Estado**
- Activación/desactivación individual de participantes
- Confirmación de eliminación con modal de seguridad
- Gestión de estado en tiempo real

<div align="center">
<img src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-PxMT3EHmpSnwgLxJzOuHT7DT3mcWij.png" alt="Gestión de Estado" width="300"/>
<img src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-6jB0lsuh6bf9jUdgom0HqSf5XBdirR.png" alt="Confirmación de Eliminación" width="300"/>
</div>

## 🏗️ Arquitectura del Proyecto

```
📦 Desfile San Pedrito Manager
├── 🎯 domain/
│   ├── Person.kt                    # Modelo principal de participante
│   ├── TypePerson.kt               # Tipos de participante
│   ├── ParticipantCategory.kt      # Categorías de participación
│   └── Statistics.kt               # Modelo de estadísticas
├── 💾 data/
│   └── PersonRepository.kt         # Repositorio en memoria
├── 🖥️ presentation/
│   ├── screens/
│   │   ├── RegistrationScreen.kt   # Pantalla de registro
│   │   ├── ParticipantListScreen.kt # Lista de participantes
│   │   └── StatisticsScreen.kt     # Panel de estadísticas
│   ├── components/
│   │   └── BottomNavigationBar.kt  # Navegación inferior
│   └── viewmodel/
│       └── PersonViewModel.kt      # Lógica de negocio
└── 🎨 ui/theme/
    ├── Color.kt                    # Paleta de colores
    ├── Theme.kt                    # Tema de la aplicación
    └── Type.kt                     # Tipografía
```

## 🛠️ Tecnologías Utilizadas

| Tecnología | Propósito |
|------------|-----------|
| **Kotlin** | Lenguaje de programación principal |
| **Jetpack Compose** | Framework de UI moderna y declarativa |
| **Material 3** | Sistema de diseño y componentes |
| **Navigation Compose** | Navegación entre pantallas |
| **ViewModel** | Gestión de estado y lógica de negocio |
| **StateFlow** | Manejo reactivo de datos |

## 📋 Requisitos del Sistema

- **Android 8.1** (API nivel 27) o superior
- **Android Studio** Arctic Fox o posterior
- **Kotlin** 1.8.0 o superior

## 🚀 Instalación y Configuración

### 1️⃣ Clonar el Repositorio
```bash
git clone https://github.com/tu-usuario/desfile-san-pedrito-manager.git
cd desfile-san-pedrito-manager
```

### 2️⃣ Abrir en Android Studio
- Abre Android Studio
- Selecciona "Open an existing project"
- Navega hasta la carpeta del proyecto

### 3️⃣ Sincronizar Dependencias
- Android Studio sincronizará automáticamente las dependencias de Gradle
- Espera a que termine el proceso de sincronización

### 4️⃣ Ejecutar la Aplicación
- Conecta un dispositivo Android o inicia un emulador
- Haz clic en el botón "Run" o presiona `Shift + F10`

## 📖 Guía de Uso

### 📝 **Registrar Participante**
1. Navega a la pestaña "Registro"
2. Completa todos los campos obligatorios
3. Selecciona el tipo de participante y categoría
4. Presiona "Registrar Participante"

### 👥 **Gestionar Participantes**
1. Ve a la pestaña "Participantes"
2. Usa la barra de búsqueda para encontrar participantes específicos
3. Aplica filtros por tipo o categoría
4. Ordena la lista según tus preferencias
5. Activa/desactiva participantes según sea necesario

### 📊 **Ver Estadísticas**
1. Accede a la pestaña "Estadísticas"
2. Revisa las métricas generales
3. Analiza las distribuciones por tipo, categoría y edad
4. Consulta los registros más recientes

## 🎨 Características de Diseño

- **Material Design 3** con colores personalizados
- **Interfaz oscura** optimizada para mejor experiencia visual
- **Componentes responsivos** que se adaptan a diferentes tamaños de pantalla
- **Animaciones fluidas** para transiciones entre estados
- **Iconografía consistente** en toda la aplicación

## 📄 Licencia

Este proyecto ha sido desarrollado con fines **educativos** para el curso de Aplicaciones Móviles de la Universidad Nacional del Santa.

---

<div align="center">

### 👨‍💻 Desarrollado con ❤️ por Ilan Angeles Rodriguez

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/ilan-angeles-rodriguez)
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:ilanangelesrodriguez@gmail.com)

**Universidad Nacional del Santa** | **Aplicaciones Móviles**
