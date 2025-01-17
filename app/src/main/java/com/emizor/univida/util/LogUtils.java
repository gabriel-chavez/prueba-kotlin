package com.emizor.univida.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2015/7/10.
 */
public class LogUtils {

    private static boolean mDebug = obtenerAcceso();

    private LogUtils()
    {
        /* cannot be instantiated */
        //throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static boolean obtenerAcceso() {
        // metodo para setear el acceso a imprimir en los logs de android
        //return true;
        boolean estadoResp = false;
        //TODO: descomentar esto para produccion y estadoResp debe iniciar en falso
        try {
            Properties properties = new Properties();
            String direccionProp = Environment.getExternalStorageDirectory() + "/Download/";
            File archivo = new File(direccionProp, "activarPAXA920.properties");

            if (archivo.exists()) {

                properties.load(new FileInputStream(archivo));
                if (properties.containsKey("logcat")) {
                    estadoResp = properties.getProperty("logcat").equals("true");

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            estadoResp = false;
        }
        return estadoResp;
    }

    public static void setDebug(boolean debug){
        mDebug = debug;
    }

    public static void resetDebug(){
        // reiniciamos el valor de mDebug, dependiendo del metodo de obtenerAcceso
        setDebug(obtenerAcceso());
    }

    public static void v(String tag, String msg){
        if(mDebug){
            Log.v(tag,msg);
        }
    }
    public static void d(String tag, String msg){
        if(mDebug){
            Log.d(tag,msg);
        }
    }
    public static void i(String tag, String msg){
        if(mDebug){
            Log.i(tag,msg);
        }
    }
    public static void w(String tag, String msg){
        if(mDebug){
            Log.w(tag,msg);
        }
    }
    public static void e(String tag, String msg){
        if(mDebug){
            Log.e(tag,msg);
        }
    }

    public static void v(String tag, String msg, Throwable trw){
        if(mDebug){
            Log.v(tag,msg,trw);
        }
    }
    public static void d(String tag, String msg, Throwable trw){
        if(mDebug){
            Log.d(tag,msg,trw);
        }
    }
    public static void i(String tag, String msg, Throwable trw){
        if(mDebug){
            Log.i(tag,msg,trw);
        }
    }
    public static void w(String tag, String msg, Throwable trw){
        if(mDebug){
            Log.w(tag,msg,trw);
        }
    }
    public static void e(String tag, String msg, Throwable trw){
        if(mDebug){
            Log.e(tag,msg,trw);
        }
    }

}
