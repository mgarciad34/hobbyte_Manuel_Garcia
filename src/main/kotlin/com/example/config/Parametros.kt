package com.example.config

object Parametros {
    val ip = "127.0.0.10"
    val port = 8090
    val secret = "algo"
    val issuer = "http://$ip:$port"
    val audience = "http://$ip:$port/rutasVarias"
    val realm = "Access to 'rutasVarias'"
}