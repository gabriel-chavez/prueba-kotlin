package com.emizor.univida.modelo.manejador;

import android.util.Base64;

import com.google.gson.Gson;
import com.emizor.univida.util.AesEncryptionData;
import com.emizor.univida.util.LogUtils;
import com.emizor.univida.util.MEncriptar;

/**
 * Created by posemizor on 08-12-17.
 */

public class UtilRest {

    private static final UtilRest ourInstance = new UtilRest();
    private static final String TAG = "UTILREST";

    public static UtilRest getInstance() {
        return ourInstance;
    }

    private UtilRest() {
    }

    public String procesarDatosInterno(String datosCadena, int tipo) {
        String respDatos = null;
        if (tipo == 1) {

            respDatos = datosCadena;

            try {

                String stringDec = new String(Base64.decode(datosCadena.getBytes(), Base64.DEFAULT));

                AesEncryptionData aesEncryptionData = new Gson().fromJson(stringDec, AesEncryptionData.class);

                respDatos = MEncriptar.decrypt("mDg?53@pd#63HH508xv7%jj/56QQ..=K".getBytes(), aesEncryptionData.iv, aesEncryptionData.value, aesEncryptionData.mac);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (tipo == 1000) {
            try {

                respDatos = MEncriptar.encrypt("mDg?53@pd#63HH508xv7%jj/56QQ..=K".getBytes(), datosCadena);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return respDatos;
    }
}
