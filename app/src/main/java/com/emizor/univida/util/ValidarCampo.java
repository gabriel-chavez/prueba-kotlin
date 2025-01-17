package com.emizor.univida.util;

import android.text.TextUtils;
import android.util.Log;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by posemizor on 8/06/17.
 */

public class ValidarCampo {

    private static final String TAG = "VALIDARCAMPO";

    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PATTERN_DOMINIO = "[a-zA-Z0-9\\s]+[a-zA-Z0-9-\\s]+[a-zA-Z0-9\\s]+";
    private static final String PATTERN_NUMERO_DECIMAL = "[0-9]+([.][0-9]+)?$";

    private static final String PATTERN_PLACA1 = "^\\d{3}[a-zA-Z]{3}";
    private static final String PATTERN_PLACA2 = "^\\d{4}[a-zA-Z]{3}";
    private static final String PATTERN_PLACA3 = "^[1-9]{1}\\d{1}\\-[a-zA-Z]{2}\\-[1-9]{1}\\d{1}";

    public static boolean validarEmail(String strEmail){
        if (strEmail != null) {
            Pattern pattern = Pattern.compile(PATTERN_EMAIL);

            Matcher matcher = pattern.matcher(strEmail);
            return matcher.matches();
        }

        return false;
    }

    public static boolean validarTamanioCadena(String strCadena, int tamMin, int tamMax) {
        if (tamMin <= strCadena.length() && strCadena.length() <= tamMax){
            return true;
        }else{
            return false;
        }

    }

    public static boolean validarNumeroDecimal(String numeroDecimal){
        if (numeroDecimal != null) {
            Pattern pattern = Pattern.compile(PATTERN_NUMERO_DECIMAL);

            Matcher matcher = pattern.matcher(numeroDecimal);
            return matcher.matches();
        }

        return false;

    }

    public static boolean validarNumeroMayorACero(String numeroDecimal){
        boolean resp = false;
        try {
            Double numero = Double.valueOf(numeroDecimal);
            if (numero.doubleValue() > 0d){
                resp = true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            resp = false;
        }
        return resp;
    }

    public static boolean validarPlaca(String placa){
        Pattern pattern;
        if (placa.length() > 6) {
            pattern = Pattern.compile(PATTERN_PLACA2);
        }else{
            pattern = Pattern.compile(PATTERN_PLACA1);
        }

        Matcher matcher = pattern.matcher(placa);
        return matcher.matches();
    }

    public static boolean validarPlacaDiplomatica(String placa){
        Pattern pattern;

        pattern = Pattern.compile(PATTERN_PLACA3);


        Matcher matcher = pattern.matcher(placa);
        return matcher.matches();
    }

}
