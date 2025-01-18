package com.alura.literAlura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alura.literAlura.model.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro,Integer> {

    public Optional<Libro> findByTituloContainsIgnoreCase(String titulo);

    List<Libro> findLibroByIdiomasContains(String idioma);

    @Query("SELECT l FROM Libro l ORDER BY l.numerosDescaargas DESC limit 10")
        List<Libro> findTop10ByOrderByNumeroDescargas();


}
