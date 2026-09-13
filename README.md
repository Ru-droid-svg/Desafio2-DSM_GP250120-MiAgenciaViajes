Agencia de Viajes — App Android

Alumno: Rudy Mauricio Gonzalez Pienda Carnet:GP250120 Materia: Desarrollo de Software para Móviles (DSM) Universidad: Universidad Don Bosco Desafío: Segundo Desafío Práctico [15%]

////////////////Descripción////////////////

Aplicación móvil para una Agencia de Viajes que permite gestionar un catálogo de destinos turísticos. Cuenta con sistema de autenticación de usuarios y un CRUD completo (Crear, Leer, Actualizar, Eliminar) conectado a Firebase.

 ////////////////Funcionalidades////////////////
Autenticación: Login y registro de agentes de viajes mediante Firebase Auth.
Catálogo (Read): Lista de destinos con foto, nombre, precio y descripción usando RecyclerView + CardView.
Registro de destinos (Create): Formulario con nombre, país (Spinner), precio, descripción y selección de imagen desde la galería.
Edición (Update): Permite modificar cualquier dato del destino, incluyendo la imagen.
Eliminación (Delete): Borrado de destinos con confirmación previa (AlertDialog).
Validaciones: Campos obligatorios, precio mayor a 0, imagen obligatoria, descripción mínima de 20 caracteres, con mensajes de error visibles en pantalla.

////////////////Tecnologías utilizadas////////////////
Lenguaje: Kotlin
IDE: Android Studio
Autenticación: Firebase Authentication (Email/Password)
Base de datos: Firebase Firestore
Almacenamiento de imágenes: Almacenamiento local (context.filesDir)
Carga de imágenes: Glide
Arquitectura: ConstraintLayout / LinearLayout + Repository pattern

////////////////Diseño////////////////
Ícono y nombre personalizados: diseñados con Android Asset Studio
Paleta de colores: obtenida de Material Palette

////////////////Estructura del proyecto////////////////
app/src/main/java/com/example/comrudygagenciaviajes/
├── LoginActivity.kt
├── RegisterActivity.kt
├── CatalogoActivity.kt
├── CrearDestinoActivity.kt
├── EditarDestinoActivity.kt
├── Destino.kt
├── DestinoAdapter.kt
└── FirestoreRepository.kt

 ////////////////Video de defensa////////////////
https://drive.google.com/drive/folders/1iqiUC_4o9Ihdft5egMM6K5f8LBj8rLXE?usp=drive_link


Ver video de defensa aquí
