package com.emizor.univida.excepcion;

/**
 * Created by root on 3/03/17.
 */
public class VoltageBajoException extends Exception {
    public VoltageBajoException() {
        this("Bateria Baja, no se puede imprimir.");
    }

    public VoltageBajoException(String detailMessage) {
        super(detailMessage);
    }
}
