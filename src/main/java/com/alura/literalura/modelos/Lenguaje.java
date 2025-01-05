package com.alura.literalura.modelos;

public enum Lenguaje {
    ESPAÑOL("es","español"),
    INGLES ("en","ingles"),
    FRANCES("fr", "frances"),
    PORTUGUES("pt","portugues"),
    LATIN("la", "latin"),
    ALEMAN("de", "aleman"),
    ITALIANO("it", "italiano");

    private String lengLiter;
    private String lenguajeLiter;

    Lenguaje(String lengLiter, String lenguajeLiter) {
        this.lengLiter = lengLiter;
        this.lenguajeLiter = lenguajeLiter;
    }

    public static Lenguaje fromString(String text) {
        for (Lenguaje lenguaje : Lenguaje.values()) {
            if (lenguaje.lengLiter.equalsIgnoreCase(text)) {
                return lenguaje;
            }
        }
        throw new IllegalArgumentException();
    }

    public static Lenguaje fromTotalString(String text) {
        for (Lenguaje lenguaje : Lenguaje.values()) {
            if (lenguaje.lenguajeLiter.equalsIgnoreCase(text)) {
                return lenguaje;
            }
        }
        throw new IllegalArgumentException();
    }
}
