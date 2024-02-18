
# Hobbyte API


## API Reference

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

#### Generar tablero

```http
  POST http://127.0.0.1:8080/api/crear/tablero
```

#### Obtener tableros del usuario
```http
    GET http://127.0.0.1:8080/api/obtener/partidas/{id}
```



