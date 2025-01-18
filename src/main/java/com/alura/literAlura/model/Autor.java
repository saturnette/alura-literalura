package com.alura.literAlura.model;

import jakarta.persistence.*;
import java.util.List;

import com.alura.literAlura.base.AutorDTO;

@Entity
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer anioNacimiento;
    private Integer fechaMuerte;
    private String nombre;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor() {
    }

    public Autor(AutorDTO d) {
        this.anioNacimiento = d.anioNacimiento();
        this.fechaMuerte = d.fechaMuerte();
        this.nombre = d.nombre();
    }

    public Autor(Autor autor) {
        this.anioNacimiento = autor.getAnioNacimiento();
        this.fechaMuerte = autor.getFechaMuerte();
        this.nombre = autor.getNombre();
    }

    public Integer getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(Integer anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public Integer getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(Integer fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return """
                  Nombre: %s
                  Año Naci: %s
                  Año Muerte: %s
                """.formatted(nombre, anioNacimiento, fechaMuerte);
    }
}