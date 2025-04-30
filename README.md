# 📒 Kotlin Notes API

Una **API RESTful** desarrollada con **Kotlin** y **Spring Boot** que permite a los usuarios autenticarse y gestionar sus **notas personales** de forma segura. Cada nota está vinculada a un usuario autenticado.

---

## 🚀 Tecnologías utilizadas

- 🧠 **Kotlin**
- 🌐 **Spring Boot** (Web, Security, Data MongoDB)
- 🍃 **MongoDB**
- 🔐 **JWT (JSON Web Tokens)** para autenticación
- ⚙️ **Gradle** como sistema de construcción

---

## 🔐 Autenticación

El sistema de autenticación se basa en **JWT**, con un enfoque de **tokens de acceso de corta duración** y **tokens de refresco** para renovar sesiones de manera segura.

- Los tokens se generan al iniciar sesión y deben enviarse en el header `Authorization`:

  ```
  Authorization: Bearer <token>
  ```

- Los tokens de refresco se almacenan de forma segura y se invalidan después de su uso.

---

## 📌 Endpoints principales

### 🧾 Autenticación (`/auth`)

| Método | Endpoint         | Descripción                          |
|--------|------------------|--------------------------------------|
| POST   | `/auth/register` | Registra un nuevo usuario            |
| POST   | `/auth/login`    | Inicia sesión y devuelve los tokens |
| POST   | `/auth/refresh`  | Renueva los tokens expirados         |

### 📝 Notas (`/notes`) — *Requiere JWT válido*

| Método | Endpoint        | Descripción                          |
|--------|-----------------|--------------------------------------|
| POST   | `/notes`        | Crea una nueva nota                  |
| GET    | `/notes`        | Lista todas las notas del usuario   |
| GET    | `/notes/{id}`   | Obtiene una nota por su ID          | TODO
| PUT    | `/notes/{id}`   | Actualiza una nota existente         | TODO
| DELETE | `/notes/{id}`   | Elimina una nota                    |

---

## 🛠 Instalación local

1. Clona el repositorio:

   ```bash
   git clone https://github.com/BrandonFnts/spring_boot_kotlin.git
   cd kotlin-notes-api
   ```

2. Configura las variables de entorno necesarias (por ejemplo, conexión a MongoDB y claves JWT).

3. Ejecuta la aplicación:

   ```bash
   ./gradlew bootRun
   ```

---

## ✅ Pendientes

- [x] Registro e inicio de sesión de usuarios  
- [x] Tokens de acceso y refresco  
- [] CRUD de notas por usuario  
- [ ] Paginación de notas  
- [ ] Compartir notas con otros usuarios  
- [ ] Tests automatizados  

---

## 📄 Licencia

Este proyecto está licenciado bajo la **MIT License**.
