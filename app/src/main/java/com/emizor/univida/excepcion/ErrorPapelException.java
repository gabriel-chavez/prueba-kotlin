package com.emizor.univida.excepcion;

/**
 * Created by root on 3/03/17.
 */
public class ErrorPapelException extends Exception {
    public ErrorPapelException() {
        this("Problemas con el papel, no se puede imprimir");
    }

    public ErrorPapelException(String detailMessage) {
        super(detailMessage);
    }
}
