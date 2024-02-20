
# Hobbyte API

## Funcionalidades comunes

#### Registrar Usuario

```http
  POST http://127.0.0.1:8080/api/registrar/usuario
```
```json
  {
  "nombre": "nombre",
  "rol": "usuario",
  "correo": "nombre@ejemplo.com",
  "contrasena": "12345"
}
```

#### Login Usuario

```http
  POST http://127.0.0.1:8080/api/login
```
```json
{
  "correo":"nombre@ejemplo.com",
  "contrasena": "12345"
}
```

A partir de aqui todas las rutas requieren tener un token para poder funcionar


## Funcionalidad Administrador

#### Agregar usuario desde el administrador 

```http
  POST http://127.0.0.1:8080/api/registrar/usuario
```
```json
  {
  "nombre": "nombre",
  "rol": "usuario",
  "correo": "nombre@ejemplo.com",
  "contrasena": "12345"
}
```

#### Obtener usuarios
```http
  GET http://127.0.0.1:8080/administrador/usuarios
```

#### Obtener usuario por ID
```http
  GET http://127.0.0.1:8080/administrador/usuario/{id}
```

#### Eliminar usuario desde el administrador
```http
  DELETE http://127.0.0.1:8080/administrador/eliminar/usuario/{id}
```


## Funcionalidad Usuarios

#### Generar tablero

```http
  POST http://127.0.0.1:8080/api/crear/tablero
```

#### Obtener partidas del usuario
```http
    GET http://127.0.0.1:8080/api/obtener/partidas/{id}
```

#### Obtener tablero del usuario por id  y numero tablero
```http
    GET http://127.0.0.1:8080/api/obtener/tablero/{id_usuario}/{id_tablero}
```

#### Jugar Tablero
```http
    PUT http://127.0.0.1:8080/api/actualizar/tablero/{id_usuario}/{id_tablero}
```

