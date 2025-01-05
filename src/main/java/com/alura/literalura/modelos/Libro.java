package com.alura.literalura.modelos;

import jakarta.persistence.*;

@Entity
@Table(name="libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Lenguaje lenguaje;
    private String tema;
    private Integer numeroDeDescarga;
    private String detalles;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libro() {
    }

    public Libro(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.autor = autor;

        if (!datosLibros.lenguaje().isEmpty()) {
            this.lenguaje = Lenguaje.fromString(datosLibros.lenguaje().get(0));
        } else {
            throw new IllegalArgumentException("Idioma invalido.");
        }
        this.detalles = String.join(",", datosLibros.detalles());
        this.numeroDeDescarga = datosLibros.numeroDeDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Lenguaje getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(Lenguaje lenguaje) {
        this.lenguaje = lenguaje;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public Integer getNumeroDeDescarga() {
        return numeroDeDescarga;
    }

    public void setNumeroDeDescarga(Integer numeroDeDescarga) {
        this.numeroDeDescarga = numeroDeDescarga;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return  "titulo='" + titulo +
                ", autor=" + autor +
                ", lenguaje=" + lenguaje;
    }
}
