package com.emizor.univida.imprime;

import android.content.Context;
import android.util.Log;

import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.soatc.EmiPolizaObtenerResponse;
import com.emizor.univida.modelo.dominio.univida.ventas.EfectivizarRespUnivida;

import java.util.Calendar;
import java.util.Date;

public class ImpresionManagerSoatc {
    private static final String TAG = "ImpresionManager";

    private Context context;
    private ImprimirFactura imprimirFactura;
    private User user;
    private int cantDocsImprimir;
    private int contadorImprimir;
    private Date fechaImpresion;
    private boolean errorImp;
    private ImpresionListener listener;

    public interface ImpresionListener {
        void onImpresionCompletada();
        void onErrorImpresion(String mensaje);
        void onTerminoDeImprimir();
        void onImpresionIniciada();
    }

    public ImpresionManagerSoatc(Context context, User user, ImpresionListener listener) {
        this.context = context;
        this.user = user;
        this.listener = listener;
        inicializarImpresora();
    }

    private void inicializarImpresora() {
        try {
            this.imprimirFactura = ImprimirFactura.obtenerImpresora(context);
            this.imprimirFactura.setAvisoListener(() -> {
                Log.i(TAG, "Impresión terminada");
                procesarTerminoImpresion();
            });
        } catch (Exception e) {
            Log.e(TAG, "Error al inicializar impresora", e);
            if (listener != null) {
                listener.onErrorImpresion("Error al inicializar la impresora");
            }
        }
    }

    public void imprimirFactura(EfectivizarRespUnivida respuesta) {
        if (respuesta == null) {
            notificarError("Los datos de impresión están vacíos");
            return;
        }

        try {
            configurarEstadoImpresion();
            if (listener != null) listener.onImpresionIniciada();
            imprimirFactura.prepararImpresionFactura(user, respuesta);
            iniciarImpresion();
        } catch (Exception ex) {
            notificarError("Error al preparar impresión: " + ex.getMessage());
        }
    }

    public void imprimirColilla(EfectivizarRespUnivida respuesta) {
        if (respuesta == null) {
            notificarError("Los datos para la colilla están vacíos");
            return;
        }

        try {
            configurarEstadoColilla();
            if (listener != null) listener.onImpresionIniciada();
            imprimirFactura.procesarColillaVentaSoat(user, respuesta, fechaImpresion);
            iniciarImpresion();
        } catch (Exception ex) {
            notificarError("Error al imprimir colilla: " + ex.getMessage());
        }
    }

    private void configurarEstadoImpresion() {
        this.cantDocsImprimir = 2;
        this.contadorImprimir = 0;
        this.fechaImpresion = Calendar.getInstance().getTime();
        this.errorImp = false;
    }

    private void configurarEstadoColilla() {
        this.cantDocsImprimir = 1;
        this.contadorImprimir = 0;
        this.fechaImpresion = Calendar.getInstance().getTime();
        this.errorImp = false;
    }

    private void iniciarImpresion() {
        contadorImprimir++;
        new Thread(() -> {
            try {
                imprimirFactura.imprimirFactura2();
            } catch (Exception e) {
                manejarErrorImpresion("Error al imprimir: " + e.getMessage(), e);
            }
        }).start();
    }

    private void manejarErrorImpresion(String mensaje, Exception e) {
        errorImp = true;
        contadorImprimir = cantDocsImprimir;
        notificarError(mensaje);
    }

    private void procesarTerminoImpresion() {
        if (errorImp) return;

        if (contadorImprimir < cantDocsImprimir) {
            iniciarImpresion();
        } else {
            if (listener != null) listener.onImpresionCompletada();
        }

        if (listener != null) listener.onTerminoDeImprimir();
    }

    private void notificarError(String mensaje) {
        if (listener != null) listener.onErrorImpresion(mensaje);
    }

    public void liberarRecursos() {
        if (imprimirFactura != null) {
            imprimirFactura.setAvisoListener(null);
        }
    }
    /**********************************/
    public void imprimirFacturaSoatc(EmiPolizaObtenerResponse respuesta) {
        if (respuesta == null) {
            notificarError("Los datos de impresión están vacíos");
            return;
        }

        try {
            configurarEstadoImpresion();
            if (listener != null) listener.onImpresionIniciada();
            imprimirFactura.prepararImpresionFacturaSoatc(user, respuesta);
            iniciarImpresion();
        } catch (Exception ex) {
            notificarError("Error al preparar impresión: " + ex.getMessage());
        }
    }

    public void imprimirColillaSoatc(EmiPolizaObtenerResponse respuesta) {
        if (respuesta == null) {
            notificarError("Los datos para la colilla están vacíos");
            return;
        }

        try {
            configurarEstadoColilla();
            if (listener != null) listener.onImpresionIniciada();
            imprimirFactura.procesarColillaVentaSoatc(user, respuesta, fechaImpresion);
            iniciarImpresion();
        } catch (Exception ex) {
            notificarError("Error al imprimir colilla: " + ex.getMessage());
        }
    }
}
