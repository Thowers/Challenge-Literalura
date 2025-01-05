package com.alura.literalura.principal;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.alura.literalura.modelos.*;
import com.alura.literalura.repositorio.RepositorioAutor;
import com.alura.literalura.repositorio.RepositorioLibro;
import com.alura.literalura.servicio.ConsumoAPI;
import com.alura.literalura.servicio.ConvierteDatos;
import org.hibernate.Hibernate;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos convertir = new ConvierteDatos();
    private List<Libro> Libros;
    private List<Autor> autores;

    private RepositorioLibro repositorioLibro;
    private RepositorioAutor repositorioAutor;

    public Principal(RepositorioLibro repositorioLibro, RepositorioAutor repositorioAutor) {
        this.repositorioLibro = repositorioLibro;
        this.repositorioAutor = repositorioAutor;
    }

    public void muestraMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                                              MENU DE OPCIONES
                    1. Busca el libro por titulo.
                    2. Consulta los libros.
                    3. Consulta los autores.
                    4. Consulta los autores segun el año
                    5. Consulta de libros por idioma.                    
                    0. Salir.
                    """;

            System.out.println(menu);
            var Opcion = teclado.nextLine();
            try {
                opcion = Integer.parseInt(Opcion.trim());

                switch (opcion) {
                    case 1:
                        registraLibro();
                        break;
                    case 2:
                        consultaLIbro();
                        break;
                    case 3:
                        consultaAutores();
                        break;
                    case 4:
                        fechaAutores();
                        break;
                    case 5:
                        buscaIdioma();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("--------------------------------OPCION INCORRECTA--------------------------------");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("--------------------------------VALOR NO NUMERICO--------------------------------");
            }
        }
    }

    private DatosLibros getDatosLibros() {
        System.out.println("Digite el nombre del libro que desea buscar");
        var buscado = teclado.nextLine();
        var comprobado = buscado;

        boolean caracterEscpecial = caracteresEspeciales(buscado);

        if (caracterEscpecial == true) {
            comprobado = "npsel";
        }

        String json = consumoApi.obtenerDatos(URL_BASE + "?search=" + comprobado.replace(" ", "+"));
        System.out.println(json);
        var busqueda = convertir.obtenerDatos(json, DatosBiblioteca.class);
        return busqueda.listaLibros().stream()
                .filter(datosLibros -> datosLibros.titulo().toUpperCase().contains(buscado.toUpperCase()))
                .findFirst()
                .orElse(null);
    }
    private boolean caracteresEspeciales(String texto){
        Pattern pattern = Pattern.compile( "«\\¿+|\\?+|\\°+|\\¬+|\\|+|\\!+|\\#+|\\$+|\\%+|\\&+|\\+|\\=+|\\’+|\\¡+|\\++|\\*+|\\~+|\\[+|\\]+|\\{+|\\}+|\\^+|\\<+|\\>»" );
        Matcher matcher = pattern.matcher(texto);
        boolean caracteresEspeciales = matcher.find();
        return(caracteresEspeciales);
    }

    public void registraLibro() {
        DatosLibros datos = getDatosLibros();

        System.out.println("DATOS DE LIBROS ---->" + datos);

        if (datos == null) {
            System.out.println("El libro no existe en Gutendex. Por favor revise que el nombre no tenga caracteres especiales ");
            return;
        }
        List<Autor> autores = datos.autor().stream()
                .map(datosAutor -> repositorioAutor.findByNombreAutor(datosAutor.nombreAutor())
                        .orElseGet(() -> {

                            Autor nuevoAutor = new Autor();
                            nuevoAutor.setNombreAutor(datosAutor.nombreAutor());
                            nuevoAutor.setFechaNacimiento(datosAutor.fechaNacimiento());
                            nuevoAutor.setFechaDefuncion(datosAutor.fechaDefuncion());
                            repositorioAutor.save(nuevoAutor);
                            return nuevoAutor;
                        })
                ).collect(Collectors.toList());

        try {
            Libro libro = new Libro(datos);
            libro.setAutor(autores.get(0));
            repositorioLibro.save(libro);
            System.out.println("Libro guardado: " + datos);


        } catch(DataIntegrityViolationException e) {
            System.out.println("El libro ya se encuentra registrado");
        }
    }

    private void consultaLIbro(){

        List<Libro> todosLosLibros = repositorioLibro.findAllWithAutor();
        todosLosLibros.forEach(libro -> Hibernate.initialize(libro.getAutor()));

        System.out.println("\n\n LIBROS DE LA BASE DE DATOS: \n");
        todosLosLibros.forEach(s -> System.out.println(
                """ 
                TÍTULO : %s - %s-Idiomas: %s  -Descargas %s  -Descripción:  %s   
                """.formatted( s.getTitulo(), s.getAutor(), s.getLenguaje(), s.getNumeroDeDescarga(), s.getDetalles()
                )));
    }

    private void consultaAutores() {
        List<Autor> autores = repositorioAutor.findAllAutorWithLibro();
        System.out.println("Autores guardados");
        autores.stream().sorted(Comparator.comparing(Autor::toString)).forEach(System.out::println);
    }

    private void fechaAutores() {
        System.out.println(" Para ver los autores vivos, por favor digita el año:  ");

        var fecha = teclado.nextLine();

        try{
            int fechaBuscada = Integer.parseInt(fecha.trim());
            List<Autor> autoresVivos= repositorioAutor.findAutoresByFecha(fechaBuscada);

            System.out.println("\n\nAUTORES VIVOS EN "+ fechaBuscada);
            autoresVivos.forEach(s-> System.out.println(
                    """
                     %s - Fecha nacimiento: %s  - Fecha fallecimiento: %s
                    """
                            .formatted(s.getNombreAutor(), s.getFechaNacimiento(), s.getFechaDefuncion())
            ));

        } catch (NumberFormatException e){
            System.out.println("\nEL VALOR INGRESADO NO ES UN NÚMERO VÁLIDO. Por favor, intente de nuevo\n\n");
        }
    }

    private void buscaIdioma() {
        try {
            System.out.println("Por favor introduzca el idioma de los libros que desea buscar");
            var idioma = teclado.nextLine();
            var lenguaje = Lenguaje.fromTotalString(idioma.toLowerCase());
            Long cantidadLibros = repositorioLibro.contarLibrosPorLenguaje(lenguaje);
            System.out.println("La cantidad de lbros en " + idioma + " es de un total de " + cantidadLibros);
            List<Libro> idiomaLibro = repositorioLibro.findByLenguaje(lenguaje);
            System.out.println("Titulos de los libros en " + idioma + "\n");
            idiomaLibro.forEach(libro -> System.out.println(libro.getTitulo()));
        } catch (IllegalArgumentException e) {
            System.out.println("No se encuentra el idioma digitado.");
        }
    }



}
