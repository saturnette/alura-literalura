# LiterAlura

LiterAlura es una aplicación Java que permite gestionar libros y autores. La aplicación permite buscar libros por título, listar libros y autores registrados, listar autores vivos en un determinado año, listar libros por idioma, buscar autores por nombre, listar autores por rango de años de nacimiento, mostrar el top 10 de libros más descargados y mostrar estadísticas de libros.

## Requisitos

- Java 11 o superior
- Maven
- Base de datos compatible con JPA (por ejemplo, MySQL, PostgreSQL)

## Instalación

1. Clona el repositorio:

    ```bash
    git clone https://github.com/tu-usuario/literalura.git
    cd literalura
    ```

2. Configura la base de datos en el archivo `application.properties`:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/literalura
    spring.datasource.username=tu-usuario
    spring.datasource.password=tu-contraseña
    spring.jpa.hibernate.ddl-auto=update
    ```

3. Compila y ejecuta la aplicación:

    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

## Uso

Al ejecutar la aplicación, se mostrará un menú con las siguientes opciones:

1. Buscar Libro por Titulo
2. Listar Libros Registrados
3. Listar Autores Registrados
4. Listar Autores vivos en un determinado año
5. Listar Libros por Idioma
6. Buscar Autor por nombre
7. Listar Autores por rango de años de Nacimiento
8. Top 10 Libros mas descargados
9. Estadísticas
0. Salir

Selecciona una opción ingresando el número correspondiente y sigue las instrucciones en pantalla.

## Estructura del Proyecto

- [Principal.java](http://_vscodecontentref_/0): Clase principal que contiene el menú y la lógica de la aplicación.
- [model](http://_vscodecontentref_/1): Contiene las clases de modelo `Autor` y `Libro`.
- [repository](http://_vscodecontentref_/2): Contiene las interfaces de repositorio `AutorRepository` y `LibroRepository`.
- [service](http://_vscodecontentref_/3): Contiene las clases de servicio `ConsumoApi` y `ConvierteDatos`.
- `src/main/java/com/alura/literAlura/DTO/`: Contiene las clases DTO `AutorDTO` y `LibroDTO`.
