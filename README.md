
## API Reference

#### Registrar Usuario

- **Ruta:** `http://127.0.0.1:8080/api/registrar/usuario`
- **Descripcion** : Aqui podemos registrar un usuario
- **Metodo** : POST

```json
{
  "nombre": "usuario",
  "correo": "usuarioprueba@ejemplo.com",
  "contrasena": "123456"
}
```

#### Login

- **Ruta:** `http://127.0.0.1:8080/api/login`
- **Descripcion** : Iniciamos sesion
- **Metodo** : POST

```json
{
  "correo": "usuarioprueba@ejemplo.com",
  "contrasena": "123456"
}
```

#### Registo de personajes

- **Ruta:** `http://127.0.0.1:8080/api/crear/personajes`
- **Descripcion** : Registramos los personajes del usuario, siempre que estos no existan
- **Metodo** : POST


```json
{
  "correo": "usuarioprueba@ejemplo.com",
  "contrasena": "123456"
}
```

