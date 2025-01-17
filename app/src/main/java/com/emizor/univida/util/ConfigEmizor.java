package com.emizor.univida.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.emizor.univida.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by silisqui on 01/03/2017.
 */

public class ConfigEmizor {

    //tiempo de espera de 15 segundos en VOLLEY
    public static final int VOLLEY_TIME_MLS = 180000;
    // tiempo de espera para subir y esperar respuesta al remitir en VOLLEY
    public static final int VOLLEY_TIME_MLS_IMG = VOLLEY_TIME_MLS + 60000;
    // tiempo en milisegundos de espera en accion del usuario, si se pasa el tiempo de 5 minutos
    public static final int TIEMPO_ESPERA_CIERRE_SESION = 300000;// 5 minutos
//    public static final int TIEMPO_ESPERA_CIERRE_SESION = 1800000;// 30 minutos

    public static final long MILLSECS_POR_DAY = 24 * 60 * 60 * 1000;
    public static String VERSION = "Sin version";

    //tiempo de espera de 3 segundos en para intentar nuevamente un consumo API rest
    public static final int TIEMPO_ESPERA_INTENTO_MLS = 3000;
    // Tiempo minimo en milisegundos para captar las actualizaciones de la locacion
    // LOCATION
    public static final int TIEMPO_ESPERA_UPDATE_LOCATION = 600000;// 10 minutos
//    public static final int TIEMPO_ESPERA_UPDATE_LOCATION = 60000;// 1 minutos
    public static final long TIEMPO_MINIMO_ACTUALIZACION_LOCACION = 1000 * 60 * 1; // 1 minuto
    public static final float DISTACIA_ACTUALIZACION_LOCACION = 10; // 10 METROS

    public static void mostrarMensaje(Context context, LayoutInflater layoutInflater, String fonticon, String mensaje){
        mostrarMensaje(context,layoutInflater,fonticon,mensaje,1);
    }

    public static void mostrarMensaje(Context context, LayoutInflater layoutInflater, String fonticon, String mensaje, int duracion){

        try {

            if (context != null) {

                if (fonticon == null) {

                    fonticon = "fa_info_circle";

                }

                int duration = (duracion == 1) ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
                TypefaceProvider.registerDefaultIconSets();

                View layout = layoutInflater.inflate(R.layout.dialog_toast_custom, null);

                BootstrapLabel textToast = (BootstrapLabel) layout.findViewById(R.id.bslMensajeToast);
                BootstrapText.Builder builder = new BootstrapText.Builder(context);
                builder.addFontAwesomeIcon(fonticon);

                builder.addText(StringEscapeUtils.unescapeJava(mensaje));

                textToast.setBootstrapText(builder.build());

                Toast toast = new Toast(context);
                toast.setDuration(duration);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.setView(layout);
                toast.show();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void mostrarMensaje(Context context, LayoutInflater layoutInflater, String fonticon, String mensaje, int duracion, int tipo){

        try {

            if (context != null) {

                if (fonticon == null) {

                    fonticon = "fa_info_circle";

                }

                int duration = (duracion == 1) ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
                TypefaceProvider.registerDefaultIconSets();

                View layout = layoutInflater.inflate(R.layout.dialog_toast_custom, null);

                BootstrapLabel textToast = layout.findViewById(R.id.bslMensajeToast);
                BootstrapText.Builder builder = new BootstrapText.Builder(context);
                builder.addFontAwesomeIcon(fonticon);

                builder.addText(StringEscapeUtils.unescapeJava(mensaje));

                textToast.setBootstrapText(builder.build());

                switch (tipo){
                    case 1://success
                        textToast.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                        break;
                    case 2://danger
                        textToast.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
                        break;
                    case 3://info
                        textToast.setBootstrapBrand(DefaultBootstrapBrand.INFO);
                        break;
                    case 4://warning
                        textToast.setBootstrapBrand(DefaultBootstrapBrand.WARNING);
                        break;
                    case 5://primary
                        textToast.setBootstrapBrand(DefaultBootstrapBrand.PRIMARY);
                        break;
                    case 6://secondary
                        textToast.setBootstrapBrand(DefaultBootstrapBrand.SECONDARY);
                        break;
                    case 7://regular
                        textToast.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                        break;
                    case 8://primary uno
                        textToast.setBootstrapBrand(DefaultBootstrapBrand.PRIMARY_UNO);
                        break;
                }

                Toast toast = new Toast(context);
                toast.setDuration(duration);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.setView(layout);
                toast.show();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static long diasRestantes(Date fechaInicio, Date fechaFin){
        return ( fechaFin.getTime() - fechaInicio.getTime() )/ MILLSECS_POR_DAY;
    }

    public static void liberarMemoria(){

        Runtime garbage = Runtime.getRuntime();
        LogUtils.i("Config", "&quot;Memoria libre antes de limpieza: &quot;"+garbage.freeMemory());
        garbage.gc();
        LogUtils.i("Config", "&quot;Memoria libre tras la limpieza: &quot;" + garbage.freeMemory());

    }

    public static String fixEncoding(String response) {
        try {
            byte[] u = response.toString().getBytes(
                    "ISO-8859-1");
            response = new String(u, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        return response;
    }

    public static String fixEncoding2(String response) {
        try {
            byte[] u = response.toString().getBytes(
                    "utf-8");
            response = new String(u, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        return response;
    }

    public static void sinConexion(Context context, LayoutInflater layoutInflater) {

        mostrarMensaje(context, layoutInflater, null," No tiene conexión a internet.", 2);

    }

    public static String getImei(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static void mostrarMensaje(View vista, String mensaje){
        final Snackbar snackbar2 = Snackbar.make(vista, mensaje,Snackbar.LENGTH_INDEFINITE);
        snackbar2.setAction("Aceptar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                snackbar2.dismiss();
            }
        });

        snackbar2.show();
    }
}
