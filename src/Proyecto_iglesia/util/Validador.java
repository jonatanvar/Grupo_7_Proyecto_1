package Proyecto_iglesia.util;

import java.util.regex.Pattern;

public class Validador {

    // Regex simple para correo electrónico
    private static final String REGEX_CORREO = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    // Regex para teléfono (acepta formatos comunes como 9999-9999 o 99999999)
    private static final String REGEX_TELEFONO = "^[0-9]{4}-?[0-9]{4}$";

    /**
     * Verifica si un texto es nulo o está vacío.
     */
    public static boolean esTextoVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    /**
     * Valida formato de correo electrónico.
     */
    public static boolean esCorreoValido(String correo) {
        if (esTextoVacio(correo)) return false;
        return Pattern.matches(REGEX_CORREO, correo);
    }

    /**
     * Valida formato de teléfono (Honduras).
     */
    public static boolean esTelefonoValido(String telefono) {
        if (esTextoVacio(telefono)) return false;
        return Pattern.matches(REGEX_TELEFONO, telefono);
    }

    /**
     * Verifica si un número es positivo (para IDs o cantidades).
     */
    public static boolean esPositivo(int numero) {
        return numero > 0;
    }
}