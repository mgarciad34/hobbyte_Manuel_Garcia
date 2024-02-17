
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

