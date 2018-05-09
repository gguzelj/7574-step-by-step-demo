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
Quien cuente con un mysql ya instalado, puede cambiar esta dependencia de H2 por la de Mysql (Mysql jdbc driver), para poder manejar la conexión entre nuestra aplicación y la base de datos. Esto nos va a permitir ver luego de que manera hibernate hace el mapeo al esquema de la bas de datos.

El archivo que nos genera esta página es un demo.zip que contiene toda la estructura básica inicial del proyecto. Podemos correr los siguientes comandos para verificar que todo este bien:

```bash
unzip demo.zip
cd demo
mvn compile
```

El resultado debería ser algo similar a esto:

<img src="./images/mvn-compile.png" alt="maven compile">

### Configuración Mysql

En caso de estar usando Mysql en lugar de la base de datos in memory H2 será necesario definir la configuración de algunos parametros. Esto se hace modificando el archivo application.properties, agregando lo siguiente:

```yaml
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/demo
spring.datasource.username=dummy
spring.datasource.password=test
```
`spring.jpa.hibernate.ddl-auto` nos permite indicarle a hibernate si queremos que nos genere el esquema de tablas en la base de datos (en caso de create o create-drop), que lo actualice si es necesario (update), que lo valide (validate) entre otros.
Con `spring.datasource.driver-class-name` definimos el nombre de la clase que utilizaremos como drive para realizar las operaciones contra la base de datos.

### Hello world controller

Nuestro siguiente paso es armar un controller básico que nos permita testear los controllers que genera spring. Para esto primero importamos el proyecto (en el caso del intelliJ debería ser tan fácil como File > Open...).
El proyecto contiene estos archivos:

<img src="./images/intellij-open.png">

Creamos una nueva clase que será nuestro controller. Luego, solamente es necesario anotarla con la annotation `@RestController` para que spring la detecte como un controller de nuestra aplicación.
Si ademas queremos mapear esta clase a otra ruta, como por ejemplo '/api', deberíamos agregar la annotation `@RequestMapping`: 

```java 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloWorldController {

}
```

Ahora definimos un método que se encargué de manejar las request http con verbo GET, y devuelva un string con la palabra "hola mundo". Para esto solamente es necesario anotar al metodo con `@GetMapping`:
```java 
@GetMapping
public String helloWorldEndpoint() {
    return "hello world";
}
 ```
 
Creamos una configuración para poder levantar nuestra api desde la opción de 'Edit Configuration':

<img src="./images/edit-configuration.png">

Ahora levantamos la aplicación y probamos nuestro endpoint:
 
 <img src="./images/app-running.png">
 
 <img src="./images/postman-hello-world.png">
 
 
### Jpa & Hibernate

El siguiente paso es armar algún modelo en nuestro dominio y mapearlo a nuestra base de datos. Para este ejemplo vamos a crear la clase Customer, y agregarle algunas propiedades:
```java 

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;

    protected Customer() {}

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

}
 ```
 
 En este fragmento de código tenemos las siguientes annotations:
 - **@Entity:** Con esto le estamos indicando a hibernate que esta clase debe mapearse con alguna tabla de nuestro esquema.
 - **@Id:** Le indicamos que el atributo será quien contenga el id que lo identifica.
 - **@GeneratedValue:** Genera automáticamente un id cuando se está insertando una nueva instancia.
 
 Lo siguiente es definir un repositorio que nos permita realizar operaciones sobre esta entidad. Para esto contamos con los `CrudRepository`. CrudRepository es una interfaz que nos provee spring con las operaciones básicas (crud) para operar sobre nuestro dominio.
 
 Al levantar nuestra aplicación spring detecta que tenemos esta interfaz, e implementa los métodos necesarios para que funcione. Además, podemos crear nuevos métodos que sirvan para hacer queries sobre nuestra base de datos. Por ejemplo, si necesitamos buscar `Customer`'s por la propiedad lastName, podemos definir el método `List<Customer> findByLastName(String lastName)` y spring se encarga de implementarlo:
 
 ```java
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);

} 
  ```
  
### Customers Controller

Por último, vamos a crear un controller (`CustomerController`) que trabaje directamente con este repositorio para poder crear, buscar y eliminar customers.

```java
@RestController
@RequestMapping("api/customers")
public class CustomerController {

    private final CustomerRepository repository;

    @Autowired
    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }
}
```

En este caso podemos ver como spring se encarga de [inyectar](http://www.baeldung.com/inversion-control-and-dependency-injection-in-spring) una instancia de este repositorio con solamente agregar `@Autowired` .


#### Crear Customer
En nuestro controller agregamos el siguiente método:

 ```java
@PostMapping
public Customer createCustomer(@RequestBody Customer customer) {
    return this.repository.save(customer);
}
```
Con el `@PostMapping` indicamos que este método tiene que estar vinculado al verbo POST del protocolo HTTP. Por otro lado, con `@RequestBody` estamos indicando que el body de la request debe mapearse con una instancia del objeto Customer.


#### Leer todos los customer

 ```java
@GetMapping
public List<Customer> getAll() {
    return this.repository.findAll();
}
```

Este endpoint es muy similar al utilizado en el HelloWorld, con la diferencia que estamos usando el repositorio que habíamos creado. El repositorio original devuelve un `Iterable<Customer>` para el método findAll, pero podemos redefinirlo para que se ajuste a nuestra necesidad:
 ```java
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);
    
    List<Customer> findAll();

}
```
Nuevamente, spring se encarga de implementar este método.

#### Leer customer por id

 ```java
@GetMapping("/{customerId}")
public Customer findById(@PathVariable Long customerId) {
    return this.repository.findById(customerId).orElse(null);
}
```

Para este caso agregamos una nueva annotation, la `@PathVariable`. Con esto indicamos que debe vincularnos el parámetro customerId con parte del path de la request.


#### Borrar customer por id

 ```java
@DeleteMapping("/{customerId}")
public void deleteById(@PathVariable Long customerId) {
    this.repository.deleteById(customerId);
}
```
Este endpoint es muy similar al anterior, y no requiere mayor análisis.

#### Buscar customer por lastName

Por último, vamos a modificar nuestro endpoint que buscar todos los customers, para que acepte un query param que nos permita filtrar por lastName (aprovechando el método definido en nuestro repositorio).


```java
@GetMapping
public List<Customer> getAll(@RequestParam(required = false) String lastName) {
    if (Objects.isNull(lastName)) {
        return this.repository.findAll();
    }
    return this.repository.findByLastName(lastName);
}
```
`@RequestParam` se encarga de hacer el mapeo entre la request y nuestro parámetro (agregamos el required = false para hacerlo no obligatorio).

### Verificamos Api

**POST**

<img src="./images/post-example.png">

**GET**

<img src="./images/get-example.png">

**GET BY NAME**

<img src="./images/by-name-example.png">

