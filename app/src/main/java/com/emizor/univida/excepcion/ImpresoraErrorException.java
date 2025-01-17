package com.emizor.univida.excepcion;

/**
 * Created by root on 3/03/17.
 */
public class ImpresoraErrorException extends Exception {
    public ImpresoraErrorException() {
        this("No se puede imprimir por un error en la impresora");
    }

    public ImpresoraErrorException(String detailMessage) {
        super(detailMessage);
    }
}
