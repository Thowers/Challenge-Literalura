
package com.alura.literalura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros(@JsonAlias("title") String titulo,
                         @JsonAlias("authors") List<DatosAutor> autor,
                         @JsonAlias("languages") List<String> lenguaje,
                         @JsonAlias("subjects") List<String> detalles,
                         @JsonAlias("download_count") Integer numeroDeDescargas) {
}