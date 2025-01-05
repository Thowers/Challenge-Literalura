package com.alura.literalura.repositorio;

import com.alura.literalura.modelos.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RepositorioAutor extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombreAutor(String nombreAutor);
    List<Autor> findByNombreAutorContainingIgnoreCase(String nombreAutor);

    @Query("SELECT DISTINCT a FROM Autor a LEFT JOIN FETCH a.libro")
    List<Autor> findAllAutorWithLibro();

    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :fecha AND a.fechaDefuncion >= :fecha")
    List<Autor> findAutoresByFecha(@Param("fecha") Integer fecha);

    @Query("SELECT a.id FROM Autor a WHERE a.nombreAutor ILIKE %:nombre%")
    Long findNombreAutor(String nombre);
}
