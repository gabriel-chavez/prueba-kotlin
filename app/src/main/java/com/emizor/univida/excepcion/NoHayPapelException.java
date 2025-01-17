package com.emizor.univida.excepcion;

/**
 * Created by root on 3/03/17.
 */
public class NoHayPapelException extends Exception {
    public NoHayPapelException() {
        this("No se encuentra papel en la impresora.");
    }

    public NoHayPapelException(String detailMessage) {
        super(detailMessage);
    }
}
