package com.alura.literAlura.principal;

import java.util.*;

import com.alura.literAlura.base.LibroDTO;
import com.alura.literAlura.model.Autor;
import com.alura.literAlura.model.DatoLibro;
import com.alura.literAlura.model.Libro;
import com.alura.literAlura.repository.AutorRepository;
import com.alura.literAlura.repository.LibroRepository;
import com.alura.literAlura.service.ConsumoApi;
import com.alura.literAlura.service.ConvierteDatos;

public class Principal {
    private String url = "https://gutendex.com/books/";
    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi apiService = new ConsumoApi();
    private ConvierteDatos dataConverter = new ConvierteDatos();
    private LibroRepository libroRepo;
    private AutorRepository autorRepo;

    public Principal(LibroRepository libroRepo, AutorRepository autorRepo) {
        this.libroRepo = libroRepo;
        this.autorRepo = autorRepo;
    }

    public Principal() {
    }

    public void mostrarMenu() {
        String menu = """
                1 - Buscar Libro por Titulo
                2 - Listar Libros Registrados
                3 - Listar Autores Registrados
                4 - Listar Autores vivos en un determinado año
                5 - Listar Libros por Idioma
                6 - Buscar Autor por nombre
                7 - Listar Autores por rango de años de Nacimiento
                8 - Top 10 Libros mas descargados
                9 - Estadísticas
                0 - Salir
                """;
        int opcion = -1;
        while (opcion != 0) {
            System.out.println(menu);
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1 -> buscarLibroPorTitulo();
                    case 2 -> listarLibrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivosPorAnio();
                    case 5 -> listarLibrosPorIdioma();
                    case 6 -> buscarAutorPorNombre();
                    case 7 -> listarAutoresPorRangoAnioNacimiento();
                    case 8 -> top10Libros();
                    case 9 -> estadisticas();
                    case 0 -> System.out.println("Saliendo de la aplicacion");
                }
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public LibroDTO getDatosLibros() {
        System.out.println("Ingrese el nombre del libro a buscar:");
        String nombreLibro = scanner.nextLine();
        var json = apiService.obtenerDatos(url + "?search=" + nombreLibro.replace(" ", "+"));
        DatoLibro datos = dataConverter.ObtenerDatos(json, DatoLibro.class);
        return datos.results().stream()
                .filter(t -> t.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst()
                .orElse(null);
    }

    private void buscarLibroPorTitulo() {
        LibroDTO libroBuscado = getDatosLibros();
        if (libroBuscado == null) {
            System.out.println("LIBRO NO ENCONTRADO");
        } else {
            Optional<Libro> libroGuardado = libroRepo.findByTituloContainsIgnoreCase(libroBuscado.titulo());
            if (libroGuardado.isPresent()) {
                mostrarLibro(libroGuardado.get());
            } else {
                Libro nuevoLibro = new Libro(libroBuscado);
                Autor nuevoAutor = new Autor(libroBuscado.autores().get(0));
                autorRepo.save(nuevoAutor);
                nuevoLibro.setAutor(nuevoAutor);
                libroRepo.save(nuevoLibro);
                mostrarLibro(nuevoLibro);
            }
        }
    }

    private void mostrarLibro(Libro libro) {
        String subTitulo;
        String formatoLibro = """
                 Titulo: %s
                 Autor:  %s
                 N° Descargas: %s
                 Idioma: %s
                """;
        var lengthIdiomas = libro.getIdiomas().stream()
                .mapToInt(String::length)
                .sum();
        if (libro.getIdiomas().size() > 1)
            lengthIdiomas += libro.getIdiomas().size();

        if (libro.getTitulo().length() > 59) {
            subTitulo = libro.getTitulo().substring(0, 59);
        } else {
            subTitulo = libro.getTitulo();
        }

        System.out.printf(formatoLibro,
                subTitulo + " ".repeat(71 - subTitulo.length()),
                libro.getAutor().getNombre() + " ".repeat(71 - libro.getAutor().getNombre().length()),
                libro.getNumerosDescaargas() + " ".repeat(65 - (libro.getNumerosDescaargas() + "").length()),
                libro.getIdiomas() + " ".repeat(69 - lengthIdiomas));
    }

    private void listarLibrosRegistrados() {
        List<Libro> listaLibros = libroRepo.findAll();
        mostrarTitulo("LIBROS REGISTRADOS");
        listaLibros.forEach(this::mostrarLibro);
        System.out.println("presione una tecla para continuar...");
    }

    private void listarAutoresRegistrados() {
        List<Autor> listaAutores = autorRepo.findAll();
        mostrarTitulo("AUTORES REGISTRADOS");
        listaAutores.forEach(this::mostrarAutor);
        System.out.println("presione una tecla para continuar...");
    }

    private void mostrarAutor(Autor autor) {
        String formatoAutor = """
                 Nombre: %s
                 Nacimiento:  %s
                 Fallecimiento: %s
                """;
        System.out.printf(formatoAutor,
                autor.getNombre() + " ".repeat(61 - (autor.getNombre().length())),
                autor.getAnioNacimiento() + " ".repeat((56 - (autor.getAnioNacimiento() + "").length())),
                autor.getFechaMuerte() + " ".repeat((54 - (autor.getFechaMuerte() + "").length())));
    }

    public void listarAutoresVivosPorAnio() {
        mostrarTitulo("AUTORES VIVOS EN UN AÑO DETERMINADO");
        System.out.println("Ingrese el año: ");
        Integer anio = scanner.nextInt();
        List<Autor> listaAutoresVivosPorAnio = autorRepo.autoresVivosPorAnio(anio);
        if (listaAutoresVivosPorAnio != null) {
            System.out.printf("Autores que vivieron en %d \n", anio);
            listaAutoresVivosPorAnio.forEach(System.out::println);
        }
    }

    private void listarLibrosPorIdioma() {
        mostrarTitulo("LIBROS POR IDIOMAS");
        System.out.println("Ingrese las dos primeras iniciales del idioma ejemplo:");
        var idiomas = """
                es [Español]    en [English]
                fr [Frances]    ch [Chino]
                """;
        System.out.println(idiomas);
        var opc = scanner.nextLine();
        if (opc.length() == 0 || opc.length() > 2) {
            System.out.println("Debe ingresar solo dos letras, ejemplo: es");
        } else {
            List<Libro> librosPorIdiomas = libroRepo.findLibroByIdiomasContains(opc.toLowerCase());
            if (!librosPorIdiomas.isEmpty()) {
                librosPorIdiomas.forEach(this::mostrarLibro);
            } else {
                System.out.println("no hay libros guardados en ese idioma!");
            }
        }
    }

    private void buscarAutorPorNombre() {
        mostrarTitulo("AUTORES POR NOMBRE");
        System.out.println("Ingrese el nombre del autor: ");
        var nombreAutor = scanner.nextLine();
        List<Autor> autores = autorRepo.findAutorByNombreContainingIgnoreCase(nombreAutor);
        if (!autores.isEmpty()) {
            autores.forEach(this::mostrarAutor);
        } else {
            System.out.println("No se encontro autor!");
        }
    }

    private void listarAutoresPorRangoAnioNacimiento() {
        mostrarTitulo("AUTORES NACIDOS ENTRE UN RANGO DE AÑOS");
        System.out.println("Ingrese año nacimiento inicio:");
        var anioNacimiento = scanner.nextInt();
        System.out.println("Ingrese año nacimiento final");
        var anioNacimientoFinal = scanner.nextInt();
        if (anioNacimientoFinal < anioNacimiento) {
            System.out.println("Error: el segundo año no puede ser menor al primer año de nacimiento");
            return;
        }
        List<Autor> autores = autorRepo.findAutorByAnioNacimientoBetween(anioNacimiento, anioNacimientoFinal);
        if (!autores.isEmpty()) {
            autores.forEach(this::mostrarAutor);
        } else {
            System.out.println("No se encontraron autores nacidos entre esos años");
        }
    }

    private void top10Libros() {
        List<Libro> top10Libro = libroRepo.findTop10ByOrderByNumeroDescargas();
        mostrarTitulo("TOP 10 DE LIBROS");
        top10Libro.forEach(this::mostrarLibro);
    }

    private void estadisticas() {
        List<Libro> libros = libroRepo.findAll();
        IntSummaryStatistics stats = libros.stream()
                .mapToInt(Libro::getNumerosDescaargas)
                .summaryStatistics();
        mostrarTitulo("ESTADISTICAS DE LIBROS");
        var recuadro = """
                 Total de Descargas   : %s
                 Promedio de Descargas: %s
                 Minimo de Descargas  : %s
                 Maximo de Descargas  : %s
                 Numeros de Libros    : %s
                """;
        System.out.printf(recuadro,
                stats.getSum(),
                stats.getAverage(),
                stats.getMin(),
                stats.getMax(),
                stats.getCount());
    }

    private void mostrarTitulo(String titulo) {
        System.out.printf("""
                        %s
                        """, titulo);
    }
}