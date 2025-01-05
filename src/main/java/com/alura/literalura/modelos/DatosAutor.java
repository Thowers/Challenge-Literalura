package com.alura.literalura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(@JsonAlias("birth_year") Integer fechaNacimiento,
                        @JsonAlias("death_year") Integer fechaDefuncion,
                        @JsonAlias("name") String nombreAutor) {

}
