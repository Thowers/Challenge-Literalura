package com.alura.literalura.repositorio;

import com.alura.literalura.modelos.Lenguaje;
import com.alura.literalura.modelos.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepositorioLibro extends JpaRepository<Libro, Long> {

    @Query("SELECT l FROM Libro l JOIN FETCH l.autor")
    List<Libro> findAllWithAutor();

    List<Libro> findByLenguaje(Lenguaje lenguaje);
    @Query("SELECT COUNT(l) FROM Libro l WHERE l.lenguaje = :lenguaje")
    Long contarLibrosPorLenguaje(@Param("lenguaje") Lenguaje lenguaje);
}
