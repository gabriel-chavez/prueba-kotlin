package com.emizor.univida.fragmento;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by posemizor on 26-10-17.
 */

public interface OnFragmentInteractionListener4 {

    // constantes del tipo de vista
    int VISTA_NUEVA_VENTA = 12;
    int VISTA_LISTA_VENTAS = 13;
    int VISTA_RCV_NUEVO = 14;
    int VISTA_RCV_LISTA =15;
    int VISTA_CAMBIAR_CONTRASENIA = 16;
    int VISTA_VACIA = 17;

    int ACCION_MENSAJE = 1019;
    int ACCION_PROGRESS = 1020;
    int ACCION_VISTA_EFECTIVIZAR = 1021;
    int ACCION_VISTA_EFECTIVIZAR_RCV = 1022;
    int ACCION_VISTA_REMITIR_RCV = 1023;

    int ACCION_INICIAR_CAPTURA_LOCACION = 1110;


    /**
     * Metodo que indica la accion y los parametros necesarios para realizarla a la actividad padre.
     *
     * @param  vista  la vista donde se invoco la llamada.
     * @param  accion accion que debe realizar la actividad
     * @param  parametros son los datos que se envia a la actividad para realizar la accion, los parametros son opcionales.
     */
    void onAccionFragment(View vista, int accion, Object parametros);

    /**
     * Metodo que registra el fragmento en la actividad padre.
     *
     * @param  fragment  es el fragmento que se visualiza y que vamos a registrar en la actividad padre
     * @param  tipoVista es la tipo de vista que se esta registrando.
     */
    void onRegisterFragment(Fragment fragment, int tipoVista);

}
