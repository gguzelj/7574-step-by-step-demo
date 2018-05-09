# Demo paso a paso

Este documento prentende ser una guia rápida para poder armar una api rest con spring boot y jpa/hibernate en pocos pasos.


## Requisistos

Para poder armar el proyecto sera necesario tener instalado [java 8](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) y [maven](https://maven.apache.org/install.html). 
También es recomendable contar con algún [ide](https://www.jetbrains.com/idea/) que facilite la tarea del desarrollo.

## Pasos

### Set up del proyecto

Primero vamos a armar la estructura general del proyecto. En este caso, vamos a usar la herramienta [spring initializr](https://start.spring.io/) que nos permite seleccionar las dependencias que necesitamos para armar una pequeña api rest que se conecta con una base de datos. 

<img src="./images/spring-initilizr.png" alt="spring initilizr">

En este caso, las dependencias que vamos a agregar son: 

- **Web:** Un starter de spring boot que nos agrega las librerias necesarias para poder desarrollar nuestra api rest. Nos ayuda en la definición de controllers, el mapeo de parametros entre la request HTTP y la definición de nuestros métodos, herramientas de loggin, etc..
Se termina traduciendo en las siguientes lineas dentro de nuestro pom.xml:
```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>2.0.1.RELEASE</version>
    </dependency>
```

- **Jpa:** Al igual que la dependencia anterior, este starter de spring boot que nos agrega las librerias necesarias para poder hacer un mapeo entre nuestro modelo de objetos y un esquema de base de datos relacional.
Se termina traduciendo en las siguientes lineas dentro de nuestro pom.xml:
```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
        <version>2.0.1.RELEASE</version>
    </dependency>
```

- **H2:** Esta dependencia nos evita tener que hacer la instalación/configuración de un RDBMs. 
Se termina traduciendo en esta dependencia dentro del pom.xml: 
```xml
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
```
Quien cuente con un mysql ya instalado, puede cambiar esta dependencia de H2 por la de Mysql (Mysql jdbc driver), para poder manejar la conexión entre nuestra aplicación y la base de datos.

El archivo que nos genera esta página es un demo.zip que contiene toda la estructura básica inicial del proyecto. Podemos correr los siguientes comandos para verificar que todo este bien:

```bash
unzip demo.zip
cd demo
mvn compile
```

El resultado debería ser algo similar a esto:

<img src="./images/mvn-compile.png" alt="maven compile">
