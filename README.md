# Objetivo

Aplicacion levanta un socket como servidor TCP/IP para poder recibir las transacciones ISO-8583 y generar la resputa

# VM Options

La aplicacion usa las siguientes variables

| VM Option | Descripcion                                                                  | Tipo de dato | valor por defecto |
|:---------:|:-----------------------------------------------------------------------------|:------------:|:-----------------:|
|   port    | Puerto de conexion                                                           |   Integer    |       8000        |

# Iniciar componente (ejemplos)

### Levantar servidor de emulacion ISO-8583 en el puerto 7000

    java -Dport=443 -jar emulador_trx-1.2.1.2.jar
