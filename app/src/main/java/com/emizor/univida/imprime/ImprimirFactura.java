package com.emizor.univida.imprime;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.emizor.univida.R;
import com.emizor.univida.excepcion.ErrorPapelException;
import com.emizor.univida.excepcion.ImpresoraErrorException;
import com.emizor.univida.excepcion.NoHayPapelException;
import com.emizor.univida.excepcion.VoltageBajoException;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.DatosRcvResp;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.ObtenerRcvRespUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RcvListarVentaRespUnivida;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.soatc.EmiPolizaObtenerResponse;
import com.emizor.univida.modelo.dominio.univida.ventas.Datos;
import com.emizor.univida.modelo.dominio.univida.ventas.Detalle;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.EfectivizarRcvUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.EfectivizarRespUnivida;
import com.emizor.univida.util.LogUtils;
import com.emizor.univida.util.utilqr.AndroidBmpUtil;
import com.emizor.univida.util.utilqr.Contents;
import com.emizor.univida.util.utilqr.QRCodeEncoder;
import com.emizor.univida.utils.AidlUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.pax.dal.IPrinter;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.dal.exceptions.PrinterDevException;
import com.pax.neptunelite.api.NeptuneLiteUser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by silisqui on 3/03/17.
 */
public class ImprimirFactura {

    private final String TAG = "IMPRIMIRFACTURA";

    protected Context context;
    protected boolean estado;

    protected ImprimirAvisoListener avisoListener;

    protected DecimalFormat decimalFormat;
    protected DecimalFormat decimalFormatLleno;

    protected Vector<Object> pImp;
    protected final String linea = "------------------------------------------------------";
    protected final String lineaPaxNormal = "--------------------------------";

    public ImprimirFactura(Context context) {
        this.context = context;
        estado = false;

        decimalFormat = new DecimalFormat("##########0.00");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormatLleno = new DecimalFormat("000000000");
        pImp = new Vector<>();
    }

    public static ImprimirFactura obtenerImpresora(Context context){
        ImprimirFactura imprimirFactura;
        if (AidlUtil.getInstance().isConnect()) {
            imprimirFactura = new ImprimirFacturaSunmi(context);
        }else{
            imprimirFactura = new ImprimirFactura(context);
        }

        return imprimirFactura;
    }

    public void setAvisoListener(ImprimirAvisoListener avisoListener) {
        this.avisoListener = avisoListener;
    }

    public synchronized void imprimirFactura2() throws ImpresoraErrorException, NoHayPapelException, VoltageBajoException, ErrorPapelException, PrinterDevException {
        Vector<Object> imprimir = pImp;
        while (estado) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        IPrinter iPrinter = null;
        try{
            iPrinter = NeptuneLiteUser.getInstance().getDal(context).getPrinter();

            //iniciamos los preparativos para la impresion
            iPrinter.init();
            //seteamos el tamaño del espacio entre caracteres y el espacio entre lineas
            iPrinter.spaceSet((byte)0,(byte)5);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        new VerificadorImpresora().verificarImpresora(iPrinter);

        estado = true;

        Integer valor = 0;

        int cont = 0;

        //un bucle para setear en el buffer de impresion los datso a imprimir
        for (Object objeto : imprimir) {

            try {
                // seteamos 500 para la impresion
                iPrinter.setGray(500);
                //reseteamos a los valores por defecto el doble alto
                iPrinter.doubleHeight(false, false);
                //reseteamos a los valores por defecto el doble ancho
                iPrinter.doubleWidth(false, false);
                if (objeto instanceof String) {

                    String linea = (String) objeto;
                    switch (valor) {
                        case 0:
                            //impresion por defecto
                            iPrinter.fontSet(EFontTypeAscii.FONT_8_16,EFontTypeExtCode.FONT_16_16);
                            iPrinter.setGray(500);
                            iPrinter.doubleHeight(false, false);
                            iPrinter.doubleWidth(false, false);
                            iPrinter.printStr(linea + "\n", "ISO-8859-1");
                            break;
                        case 1:
                            //impresion de tamaño chico
                            iPrinter.fontSet(EFontTypeAscii.FONT_8_16,EFontTypeExtCode.FONT_16_16);
                            iPrinter.setGray(500);
                            iPrinter.doubleHeight(false, false);
                            iPrinter.doubleWidth(false, false);

                            iPrinter.printStr(linea + "\n", "ISO-8859-1");

                            valor = 0;

                            break;
                        case 2:
                        case 3:
                            //impresion de una linea por una imagen
                            InputStream bm = context.getResources().openRawResource(R.raw.linea3);
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(bm);
                            Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                            iPrinter.printBitmap(bmp);

                            valor = 0;
                            break;
                        case 4:
                            // imprimimos con los colores invertidos o sea letras blancas y fondo negro por la cadena a imprimir
                            iPrinter.invert(true);
                            iPrinter.fontSet(EFontTypeAscii.FONT_8_16,EFontTypeExtCode.FONT_16_16);
                            iPrinter.setGray(500);
                            iPrinter.doubleHeight(true, true);
                            iPrinter.doubleWidth(true, true);

                            iPrinter.printStr(linea + "\n", "ISO-8859-1");
                            valor = 0;
                            iPrinter.invert(false);
                            iPrinter.doubleHeight(false, false);
                            iPrinter.doubleWidth(false, false);
                            break;
                        case 9:
                        case 5:
                            //impresion de letra a doble ancho y alto
                            iPrinter.fontSet(EFontTypeAscii.FONT_8_16,EFontTypeExtCode.FONT_16_16);
                            iPrinter.setGray(500);
                            iPrinter.doubleHeight(true, true);
                            iPrinter.doubleWidth(true, true);

                            iPrinter.printStr(linea + "\n", "ISO-8859-1");
                            valor = 0;
                            iPrinter.doubleHeight(false, false);
                            iPrinter.doubleWidth(false, false);
                            break;
                        case 6:
                            // impresion de una segunda linea
                            InputStream bm2 = context.getResources().openRawResource(R.raw.linea);
                            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(bm2);
                            Bitmap bmp2 = BitmapFactory.decodeStream(bufferedInputStream2);
                            iPrinter.printBitmap(bmp2);
                            valor = 0;
                            break;
                        case 7:
                            //impresion de imagen de pie de pagina
                            InputStream bmpp = context.getResources().openRawResource(R.raw.pie_de_pagina);
                            BufferedInputStream bufferedInputStreampp = new BufferedInputStream(bmpp);
                            Bitmap bmppp = BitmapFactory.decodeStream(bufferedInputStreampp);
                            iPrinter.printBitmap(bmppp);

                            valor = 0;
                            break;
                        case 8:
                            // impresion de imagen de la leyenda generica
                            InputStream bmlg = context.getResources().openRawResource(R.raw.leyenda_generica);
                            BufferedInputStream bufferedInputStreamlg = new BufferedInputStream(bmlg);
                            Bitmap bmplg = BitmapFactory.decodeStream(bufferedInputStreamlg);
                            iPrinter.printBitmap(bmplg);

                            valor = 0;
                            break;
//                        case 9:
//                            //
//                            iPrinter.fontSet(EFontTypeAscii.FONT_8_16,EFontTypeExtCode.FONT_16_16);
//                            iPrinter.setGray(500);
//                            iPrinter.doubleHeight(true, true);
//                            iPrinter.doubleWidth(true, true);
//                            iPrinter.printStr(linea + "\n", "ISO-8859-1");
//                            valor = 0;
//                            iPrinter.doubleHeight(false, false);
//                            iPrinter.doubleWidth(false, false);
//                            break;

                        case 10:
                            int idImagen = Integer.parseInt(linea);

                            InputStream bmid = context.getResources().openRawResource(idImagen);
                            BufferedInputStream bufferedInputStreamid = new BufferedInputStream(bmid);
                            Bitmap bmpid = BitmapFactory.decodeStream(bufferedInputStreamid);
                            iPrinter.printBitmap(bmpid);

                            valor = 0;
                            break;
                        case 11:
                            File archivo = new File(linea);
                            Uri uri = Uri.fromFile(archivo);
                            InputStream inputStream = context.getContentResolver().openInputStream(uri);

                            BufferedInputStream bufferedInputStreamid2 = new BufferedInputStream(inputStream);
                            Bitmap bmpid2 = BitmapFactory.decodeStream(bufferedInputStreamid2);
                            iPrinter.printBitmap(bmpid2);

                            valor = 0;
                            break;
                        case 100://letra pequenia con salto de linea
                            iPrinter.fontSet(EFontTypeAscii.FONT_8_16,EFontTypeExtCode.FONT_16_16);
                            iPrinter.printStr(linea + "\n", null);
                            valor = 0;
                            break;
                        case 101://letra pequenia sin salto de linea
                            iPrinter.fontSet(EFontTypeAscii.FONT_8_16,EFontTypeExtCode.FONT_16_16);
                            iPrinter.printStr(linea, null);
                            valor = 0;
                            break;
                        case 102://letra pequenia con doble with y height con salto de linea
                            iPrinter.fontSet(EFontTypeAscii.FONT_8_16,EFontTypeExtCode.FONT_16_16);
                            iPrinter.doubleHeight(true, true);
                            iPrinter.doubleWidth(true, true);
                            iPrinter.printStr(linea + "\n", null);
                            valor = 0;
                            break;
                        case 103://letra pequenia con doble with y height sin salto
                            iPrinter.fontSet(EFontTypeAscii.FONT_8_16,EFontTypeExtCode.FONT_16_16);
                            iPrinter.doubleHeight(true, true);
                            iPrinter.doubleWidth(true, true);
                            iPrinter.printStr(linea, null);
                            valor = 0;
                            break;
                        case 104://letra pequenia con salto de linea e invertido
                            iPrinter.fontSet(EFontTypeAscii.FONT_8_16,EFontTypeExtCode.FONT_16_16);
                            iPrinter.invert(true);
                            iPrinter.printStr(linea + "\n", null);
                            iPrinter.invert(false);
                            valor = 0;
                            break;
                        case 110:// letra normal con salto
//                            iPrinter.fontSet(EFontTypeAscii.FONT_16_24,EFontTypeExtCode.FONT_16_16);
                            iPrinter.fontSet(EFontTypeAscii.FONT_16_24,EFontTypeExtCode.FONT_24_24);
                            iPrinter.printStr(linea + "\n", null);
                            valor = 0;
                            break;
                        case 111:// letra normal
//                            iPrinter.fontSet(EFontTypeAscii.FONT_16_24,EFontTypeExtCode.FONT_16_16);
                            iPrinter.fontSet(EFontTypeAscii.FONT_16_24,EFontTypeExtCode.FONT_24_24);
                            iPrinter.printStr(linea, null);
                            valor = 0;
                            break;
                        case 112://letra normal con doble with y height con salto
//                            iPrinter.fontSet(EFontTypeAscii.FONT_16_24,EFontTypeExtCode.FONT_16_16);
                            iPrinter.fontSet(EFontTypeAscii.FONT_16_24,EFontTypeExtCode.FONT_24_24);
                            iPrinter.doubleHeight(true, true);
                            iPrinter.doubleWidth(true, true);
                            iPrinter.printStr(linea + "\n", null);
                            valor = 0;
                            break;
                        case 113://letra normal con doble with y height sin salto
//                            iPrinter.fontSet(EFontTypeAscii.FONT_16_24,EFontTypeExtCode.FONT_16_16);
                            iPrinter.fontSet(EFontTypeAscii.FONT_16_24,EFontTypeExtCode.FONT_24_24);
                            iPrinter.doubleHeight(true, true);
                            iPrinter.doubleWidth(true, true);
                            iPrinter.printStr(linea, null);
                            valor = 0;
                            break;

                    }

                } else if (objeto instanceof Integer) {

                   if (valor == 210) {
                       iPrinter.spaceSet((byte)0,((Integer) objeto).byteValue());
                       valor = 0;
                   }else{
                       valor = (Integer) objeto;
                    }

                } else if (objeto instanceof InputStream) {
                    InputStream bmob = (InputStream) objeto;
                    BufferedInputStream bufferedInputStreamob = new BufferedInputStream(bmob);
                    Bitmap bmpob = BitmapFactory.decodeStream(bufferedInputStreamob);
                    iPrinter.printBitmap(bmpob);

                    valor = 0;
                } else if (objeto instanceof Bitmap){

                    Bitmap bmpido = (Bitmap) objeto;
                    iPrinter.printBitmap(bmpido);

                    valor = 0;
                }

                cont++;

                int contadorPax = 0;

                if (cont == 600) {
                    //iniciamos la impresion que cargamos al buffer
                    iPrinter.start();
                    //verificamos el estado de la impresora
                    new VerificadorImpresora().verificarImpresora(iPrinter);
                    cont = 0;
                    try {
                        while (iPrinter.getStatus() == 1) {
                            try {

                                contadorPax ++;
                                Thread.sleep(10);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (contadorPax == 5){
                                break;
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();

                        try {

                            Thread.sleep(800);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                    iPrinter = null;

                    iPrinter = NeptuneLiteUser.getInstance().getDal(context).getPrinter();

                    iPrinter.init();
                    iPrinter.spaceSet((byte) 0, (byte) 5);
                    iPrinter.setGray(500);

                }

            }catch (Exception ex){
                ex.printStackTrace();
            }

        }

        int contadorEspera = 0;
        try {

            iPrinter.printStr("\f", null);
            iPrinter.start();
            try {

                Thread.sleep(1200);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (iPrinter.getStatus() != 0) {
                try {
                    Thread.sleep(500);
                    contadorEspera++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (contadorEspera == 10) {
                    break;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        estado = false;

        if (avisoListener != null){
            avisoListener.terminoDeImprimir();
        }
        new VerificadorImpresora().verificarImpresora(iPrinter);
        notify();

    }

    public void prepararImpresionRcv(User user, EfectivizarRcvUnivida efectivizarRcvUnivida, String secuencial, RcvListarVentaRespUnivida rcvListarVentaRespUnivida) {
        prepararImpresionRcvNormal(user,efectivizarRcvUnivida, secuencial, rcvListarVentaRespUnivida);
//        LogUtils.i(TAG, "PREPARAMOS FACTURA");
//
//        if (pImp != null){
//            pImp.clear();
//        }
//
//        Date dateRcv = null;
//        SimpleDateFormat simpleDateFormatr = new SimpleDateFormat("dd/MM/yyyy");
//        SimpleDateFormat simpleDateFormatRcv = new SimpleDateFormat("yyyy-MM-dd");
//        try{
//            dateRcv = simpleDateFormatRcv.parse(efectivizarRcvUnivida.getVentaFecha());
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//
//
//        pImp = new Vector<>();
//
//        pImp.add(3);
//        pImp.add(linea);
//        pImp.add(110);
//        pImp.add(formatearCentradoCadenaPequeniaDoblePax("REPORTE CIERRE DE VENTAS"));
//        pImp.add(3);
//        pImp.add(linea);
//        //pImp.add(101);
//        pImp.add("CODIGO RCV         : " + Double.valueOf(secuencial).intValue());
//        //pImp.add(101);
//        if (dateRcv != null) {
//            pImp.add("FECHA               : " + simpleDateFormatr.format(dateRcv));
//        }else{
//            pImp.add("FECHA               : " + efectivizarRcvUnivida.getVentaFecha());
//        }
//        pImp.add("IMPORTE             : " + efectivizarRcvUnivida.getRcvImporte());
//        pImp.add("CANTIDAD VALIDOS    : " + rcvListarVentaRespUnivida.getDatos().getCantidadValidos());
//        pImp.add("CANTIDAD REVERTIDOS : " + rcvListarVentaRespUnivida.getDatos().getCantidadRevertidos());
//        pImp.add("CANTIDAD ANULADOS   : " + rcvListarVentaRespUnivida.getDatos().getCantidadAnulados());
//        pImp.add("CANTIDAD TOTAL      : " + efectivizarRcvUnivida.getRcvCantidad());
//        pImp.add(3);
//        pImp.add(linea);
//        pImp.add("SUCURSAL : " + user.getDatosUsuario().getSucursalNombre());
//        pImp.add("USUARIO  : " + user.getDatosUsuario().getEmpleadoNombreCompleto());
//        pImp.add("CARGO    : " + user.getDatosUsuario().getEmpleadoCargo());
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        pImp.add("FECHA IMPRESIÓN : " + simpleDateFormat.format(new Date()));
//        pImp.add(3);
//        pImp.add(linea);

    }

    public void prepararImpresionRcvNormal(User user, EfectivizarRcvUnivida efectivizarRcvUnivida, String secuencial, RcvListarVentaRespUnivida rcvListarVentaRespUnivida) {

        LogUtils.i(TAG, "PREPARAMOS FACTURA");

        if (pImp != null){
            pImp.clear();
        }

        Date dateRcv = null;
        SimpleDateFormat simpleDateFormatr = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat simpleDateFormatRcv = new SimpleDateFormat("yyyy-MM-dd");
        try{
            dateRcv = simpleDateFormatRcv.parse(efectivizarRcvUnivida.getVentaFecha());
        }catch (Exception ex){
            ex.printStackTrace();
        }


        pImp = new Vector<>();

        pImp.add(3);
        pImp.add(linea);
        pImp.add(110);
        pImp.add(formatearCentradoCadenaPequeniaDoblePax("REPORTE CIERRE DE VENTAS"));
        pImp.add(3);
        pImp.add(linea);
        pImp.add(110);
        pImp.add("CÓDIGO RCV         : " + Double.valueOf(secuencial).intValue());
        pImp.add(110);
        if (dateRcv != null) {
            pImp.add("FECHA               : " + simpleDateFormatr.format(dateRcv));
        }else{
            pImp.add("FECHA               : " + efectivizarRcvUnivida.getVentaFecha());
        }
        pImp.add(110);
        pImp.add("IMPORTE             : " + efectivizarRcvUnivida.getRcvImporte());
        pImp.add(110);
        pImp.add("CANTIDAD VALIDOS    : " + rcvListarVentaRespUnivida.getDatos().getCantidadValidos());
        pImp.add(110);
        pImp.add("CANTIDAD REVERTIDOS : " + rcvListarVentaRespUnivida.getDatos().getCantidadRevertidos());
        pImp.add(110);
        pImp.add("CANTIDAD ANULADOS   : " + rcvListarVentaRespUnivida.getDatos().getCantidadAnulados());
        pImp.add(110);
        pImp.add("CANTIDAD TOTAL      : " + efectivizarRcvUnivida.getRcvCantidad());
        pImp.add(3);
        pImp.add(linea);
//        pImp.add(110);
//        pImp.add("SUCURSAL : " + user.getDatosUsuario().getSucursalNombre());
//        pImp.add(110);
//        pImp.add("USUARIO  : " + user.getDatosUsuario().getEmpleadoNombreCompleto());
//        pImp.add(110);
//        pImp.add("CARGO    : " + user.getDatosUsuario().getEmpleadoCargo());

        List<String> listaSucursal = formatearLineaCadena2("SUCURSAL : " + user.getDatosUsuario().getSucursalNombre());
        for(String strSucu : listaSucursal) {
            pImp.add(110);
            pImp.add(strSucu);
        }
        List<String> listaUsuario = formatearLineaCadena2("USUARIO  : " + user.getDatosUsuario().getEmpleadoNombreCompleto());
        for(String strUser : listaUsuario) {
            pImp.add(110);
            pImp.add(strUser);
        }
        List<String> listaCargo = formatearLineaCadena2("CARGO    : " + user.getDatosUsuario().getEmpleadoCargo());
        for(String strCargo : listaCargo) {
            pImp.add(110);
            pImp.add(strCargo);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        List<String> listaFecha = formatearLineaCadena2("FECHA IMPRESIÓN : " + simpleDateFormat.format(new Date()));
        for(String strFecha : listaFecha) {
            pImp.add(110);
            pImp.add(strFecha);
        }
//        pImp.add(110);
//        pImp.add("FECHA IMPRESIÓN : " + simpleDateFormat.format(new Date()));
        pImp.add(3);
        pImp.add(linea);

    }

    public void prepararImpresionRcv(User user, ObtenerRcvRespUnivida obtenerRcvRespUnivida) {
        prepararImpresionRcvNormal(user,obtenerRcvRespUnivida);
//        LogUtils.i(TAG, "PREPARAMOS RCV REPORTE");
//
//        if (pImp != null){
//            pImp.clear();
//        }
//
//        if (obtenerRcvRespUnivida != null){
//            DatosRcvResp datosRcvResp = obtenerRcvRespUnivida.getDatos();
//
//            if (datosRcvResp != null){
//                pImp = new Vector<>();
//
//                pImp.add(3);
//                pImp.add(linea);
//                pImp.add(110);
//                pImp.add(formatearCentradoCadenaPequeniaDoblePax("REPORTE CIERRE DE VENTAS"));
//                pImp.add(3);
//                pImp.add(linea);
//                //pImp.add(101);
//                pImp.add("CÓDIGO RCV         : " + datosRcvResp.getRcvSecuencial());
//                //pImp.add(101);
//                pImp.add("FECHA               : " + datosRcvResp.getRcvFormularioFecha());
//                pImp.add("IMPORTE             : " + datosRcvResp.getRcvFormularioImporte());
//                pImp.add("CANTIDAD VALIDOS    : " + datosRcvResp.getRcvCantidadValidos());
//                pImp.add("CANTIDAD REVERTIDOS : " + datosRcvResp.getRcvCantidadRevertidos());
//                pImp.add("CANTIDAD ANULADOS   : " + datosRcvResp.getRcvCantidadAnulados());
//                pImp.add("CANTIDAD TOTAL      : " + datosRcvResp.getRcvCantidad());
//                pImp.add(3);
//                pImp.add(linea);
//                pImp.add("SUCURSAL : " + user.getDatosUsuario().getSucursalNombre());
//                pImp.add("USUARIO  : " + user.getDatosUsuario().getEmpleadoNombreCompleto());
//                pImp.add("CARGO    : " + user.getDatosUsuario().getEmpleadoCargo());
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//                pImp.add("FECHA IMPRESIÓN : " + simpleDateFormat.format(new Date()));
//                pImp.add(3);
//                pImp.add(linea);
//            }
//
//
//        }


    }

    public void prepararImpresionRcvNormal(User user, ObtenerRcvRespUnivida obtenerRcvRespUnivida) {

        LogUtils.i(TAG, "PREPARAMOS RCV REPORTE");

        if (pImp != null){
            pImp.clear();
        }

        if (obtenerRcvRespUnivida != null){
            DatosRcvResp datosRcvResp = obtenerRcvRespUnivida.getDatos();

            if (datosRcvResp != null){
                pImp = new Vector<>();

                String linea = lineaPaxNormal;
                pImp.add(3);
                pImp.add(linea);
                pImp.add(110);
                pImp.add(formatearCentradoCadenaPequeniaDoblePax("REPORTE CIERRE DE VENTAS"));
                pImp.add(3);
                pImp.add(linea);
                pImp.add(110);
                pImp.add("CÓDIGO RCV         : " + datosRcvResp.getRcvSecuencial());
                pImp.add(110);
                pImp.add("FECHA               : " + datosRcvResp.getRcvFormularioFecha());
                pImp.add(110);
                pImp.add("IMPORTE             : " + datosRcvResp.getRcvFormularioImporte());
                pImp.add(110);
                pImp.add("CANTIDAD VALIDOS    : " + datosRcvResp.getRcvCantidadValidos());
                pImp.add(110);
                pImp.add("CANTIDAD REVERTIDOS : " + datosRcvResp.getRcvCantidadRevertidos());
                pImp.add(110);
                pImp.add("CANTIDAD ANULADOS   : " + datosRcvResp.getRcvCantidadAnulados());
                pImp.add(110);
                pImp.add("CANTIDAD TOTAL      : " + datosRcvResp.getRcvCantidad());
                pImp.add(3);
                pImp.add(linea);
                List<String> listaSucursal = formatearLineaCadena2("SUCURSAL : " + user.getDatosUsuario().getSucursalNombre());
                for(String strSucu : listaSucursal) {
                    pImp.add(110);
                    pImp.add(strSucu);
                }
                List<String> listaUsuario = formatearLineaCadena2("USUARIO  : " + user.getDatosUsuario().getEmpleadoNombreCompleto());
                for(String strUser : listaUsuario) {
                    pImp.add(110);
                    pImp.add(strUser);
                }
                List<String> listaCargo = formatearLineaCadena2("CARGO    : " + user.getDatosUsuario().getEmpleadoCargo());
                for(String strCargo : listaCargo) {
                    pImp.add(110);
                    pImp.add(strCargo);
                }
                pImp.add(110);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                List<String> listaFecha = formatearLineaCadena2("FECHA IMPRESIÓN : " + simpleDateFormat.format(new Date()));
                for(String strFecha : listaFecha) {
                    pImp.add(110);
                    pImp.add(strFecha);
                }
//                pImp.add(110);
//                pImp.add("FECHA IMPRESIÓN : " + simpleDateFormat.format(new Date()));
                pImp.add(3);
                pImp.add(linea);
            }


        }


    }
    public void prepararImpresionFacturaCompleto(User user, EfectivizarRespUnivida efectivizarRespUnivida) {
        prepararImpresionFactura2022FontNormal(user, efectivizarRespUnivida);
    }
    public void prepararImpresionFactura(User user, EfectivizarRespUnivida efectivizarRespUnivida) {

//        if (efectivizarRespUnivida.getDatos().getSoatGestionFk() >= 2022) {
        prepararImpresionFactura2022FontNormal(user, efectivizarRespUnivida);
//            return;
//        }
//        LogUtils.i(TAG, "PREPARAMOS FACTURA");
//
//        if (pImp != null){
//            pImp.clear();
//        }
//
//        pImp = new Vector<>();
//        Vector<Object> vectoDoc = new Vector<>();
//
//        Datos datos = efectivizarRespUnivida.getDatos();
//
//        List<String> nombreEmpresa = formatearLineaCadena2(datos.getFacturaMaestro().getRazonSocial());
//        List<String> strLey = formatearLineaCadenaPeque(datos.getFacturaMaestro().getLeyenda(), 0);
//        List<String> vector11 = formatearLineaCadenaPeque(datos.getFacturaMaestro().getDireccionSucursal() + " Teléfono: " + datos.getFacturaMaestro().getTelefonoSucursal(), 0);
//
//        BigDecimal bigDecimalAmount = new BigDecimal(datos.getFacturaMaestro().getImporteNumeral());
//
//        bigDecimalAmount = bigDecimalAmount.setScale(2, RoundingMode.HALF_EVEN);
//        StringBuilder stringBuilder = new StringBuilder("TOTAL: " + "Bs" + " " + bigDecimalAmount.toString());
//
//
//        Vector<Object> nnotes = new Vector<>();
//
//        List<String> literal = formatearLineaCadenaPeque("SON: " + datos.getFacturaMaestro().getImporteLiteral(),0);
//
//        List<Detalle> detalles = datos.getFacturaMaestro().getDetalle();
//
//        for (Detalle detalle : detalles){
//            String lineaDatos = construirFila(decimalFormat.format(detalle.getCantidad()), "", decimalFormat.format(detalle.getImporteUnitario()), decimalFormat.format(detalle.getImporteTotal()));
//            nnotes.add(lineaDatos);
//
//            nnotes.add(formatearLineaCadenaPeque(detalle.getLineaDetalle(), 0));
//
//        }
//
//        LogUtils.i("IMPRESION ", "TRY");
//
//        LogUtils.i("IMPRESION ", "logo");
//
//        vectoDoc.add(10);
//        vectoDoc.add(String.valueOf(R.raw.logop));
//
//        vectoDoc.add(formatearCentradoCadenaPequeniaPax("CASA MATRIZ"));
//
//        List<String> vector111 = formatearLineaCadenaPeque(datos.getFacturaMaestro().getDireccionEmpresa() + " Teléfono: " + datos.getFacturaMaestro().getTelefonosEmpresa(), 0);
//        for (String strDire2 : vector111){
//
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax(strDire2));
//
//        }
//
//        vectoDoc.add(formatearCentradoCadenaPequeniaPax(datos.getFacturaMaestro().getLugar()));
//        vectoDoc.add(" ");
//
//        if (datos.getFacturaMaestro().getNumeroSucursal() != 0) {
//
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax("SUCURSAL: " + datos.getFacturaMaestro().getNumeroSucursal()));
//
//            for (String strDire : vector11){
//
//                vectoDoc.add(formatearCentradoCadenaPequeniaPax(strDire));
//
//            }
//
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax(datos.getFacturaMaestro().getMunicipioDepartamento()));
//        }
//
//        vectoDoc.add(110);
//        vectoDoc.add(formatearCentradoCadenaPequeniaDoblePax("FACTURA"));
//
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//
//        vectoDoc.add(101);
//        vectoDoc.add("  NIT              : ");
//        vectoDoc.add(110);
//        vectoDoc.add(datos.getFacturaMaestro().getNitEmpresa());
//        vectoDoc.add(101);
//        vectoDoc.add("  FACTURA No.      : ");
//        vectoDoc.add(110);
//        vectoDoc.add(datos.getFacturaMaestro().getNumeroFactura().toString());
//        vectoDoc.add(101);
//        vectoDoc.add("  AUTORIZACIÓN No.: ");
//        vectoDoc.add(110);
//        vectoDoc.add(datos.getFacturaMaestro().getNumeroAutorizacion());
//
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//
//        List<String> activitys = formatearLineaCadenaPeque(datos.getFacturaMaestro().getActividadEconomica(), 0);
//
//        for (String strActividad : activitys){
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax(strActividad));
//        }
//
//        String fecha1 = datos.getFacturaMaestro().getFechaEmision();
//        fecha1 = fecha1.replace('-', '/');
//
//
//        vectoDoc.add("FECHA        : " + fecha1);
//        vectoDoc.add("NIT/CI       : " + datos.getFacturaMaestro().getNitCiCliente());
//        vectoDoc.addAll(formatearLineaCadenaPeque("RAZÓN SOCIAL: " + datos.getFacturaMaestro().getRazonSocialClient(), 10));
//
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//
//        vectoDoc.add(construirFila("Cant.", " ", "Precio", "Sub total"));
//
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//
//        for (int i = 0; i < nnotes.size(); i += 2) {
//
//            List<String> nnotes2 = (List<String>) nnotes.get(i + 1);
//
//            vectoDoc.addAll(nnotes2);
//
//            vectoDoc.add(String.valueOf(nnotes.get(i)));
//
//        }
//
//        //vectoDoc.add(getApplicationContext().getResources().openRawResource(R.raw.linea));
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//        // colocamos valor unao para imprimir cadenas al revez y con alineacion a la derecha
//        vectoDoc.add(102);
//        vectoDoc.add(stringBuilder.toString());
//
//        vectoDoc.add("MONTO A PAGAR: Bs " + decimalFormat.format(bigDecimalAmount));
//        vectoDoc.addAll(literal);
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//
//        vectoDoc.add(101);
//        vectoDoc.add("CÓDIGO DE CONTROL: ");
//        vectoDoc.add(110);
//        vectoDoc.add(datos.getFacturaMaestro().getCodigoControl());
//        vectoDoc.add("FECHA LÍMITE DE EMISIÓN: " + datos.getFacturaMaestro().getFechaLimiteEmision());
//
//
//        vectoDoc.add("USUARIO: " + user.getDatosUsuario().getEmpleadoNombreCompleto());
//
//        String informacionQr = datos.getFacturaMaestro().getCodigoQr();
//
//            vectoDoc.add(200);
//            vectoDoc.add(getQr(informacionQr));
//
//        for (String strLe : strLey) {
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax(strLe));
//        }
//
//        List<String> listaLeyenda = formatearLineaCadenaPeque("\"ESTA FACTURA CONTRIBUYE AL DESARROLLO DEL PAIS, EL USO ILÍCITO DE ESTA SERÁ SANCIONADO DE ACUERDO A LEY\"", 0);
//        for (String strLeyenda : listaLeyenda){
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax(strLeyenda));
//        }
//
//        vectoDoc.add(" ");
//        String corteAqui = ">>>--------------- CORTE AQUI ---------------<<<";
//        vectoDoc.add(corteAqui);
//        vectoDoc.add(" ");
//        vectoDoc.add(10);
//        vectoDoc.add(String.valueOf(R.raw.logop));
//
//        if (datos.getSoatQrContenido() != null) {
//            vectoDoc.add(200);
//            vectoDoc.add(getQr(datos.getSoatQrContenido().get(0)));
//        }
//
//        vectoDoc.add(110);
//        vectoDoc.add((formatearCentradoCadenaPequeniaDoblePax("COMPROBANTE SOAT " + datos.getSoatGestionFk())));
//
//        //SOAT
//        vectoDoc.add("Fecha emisión         : " + datos.getFacturaMaestro().getFechaEmision());
//        vectoDoc.add("Número de comprobante: " + decimalFormatLleno.format(datos.getSoatNumeroComprobante()));
//        vectoDoc.add("Número de roseta     : " + decimalFormatLleno.format(datos.getSoatRosetaNumero()));
//        vectoDoc.add("Número de factura    : " + decimalFormatLleno.format(datos.getFacturaMaestro().getNumeroFactura()));
//        vectoDoc.add("Placa                 : " +datos.getVehiculoPlaca());
//        vectoDoc.add("Tipo de vehículo      : " + datos.getSoatVehiculoTipoDescripcion());
//        vectoDoc.add("Tipo de uso           : " + datos.getSoatVehiculoUsoDescription());
//        vectoDoc.add(210);
//        vectoDoc.add(15);
//        vectoDoc.add("Plaza circulación     : " + datos.getSoatDepartamentoPcDescription());
//        //SOAT
//
//        vectoDoc.add(210);
//        vectoDoc.add(5);
//
//        //VEHICULO
//        vectoDoc.add("Marca                 : " + datos.getVehiculoMarca());
//        vectoDoc.add("Modelo                : " + datos.getVehiculoModelo());
//        vectoDoc.add("Color                 : " + datos.getVehiculoColor());
//        vectoDoc.add("Año                  : " + datos.getVehiculoAnio());
//        vectoDoc.add("Nro de Motor          : " + datos.getVehiculoMotor());
//        vectoDoc.add(210);
//        vectoDoc.add(15);
//        vectoDoc.add("Nro Chasis            : " + datos.getVehiculoChasis());
//        //VEHICULO
//
//        vectoDoc.add(210);
//        vectoDoc.add(5);
//
//        //TOMADOR
//        vectoDoc.addAll(formatearLineaCadenaPeque("Tomador               : " + datos.getPropTomador(), 10));
//        vectoDoc.add("CI                    : " + datos.getPropCi());
//        vectoDoc.add("NIT                   : " + datos.getPropNit());
//        vectoDoc.addAll(formatearLineaCadenaPeque("Dirección            : " + datos.getPropDireccion(), 10));
//        vectoDoc.add("Teléfono             : " + datos.getPropTelefono());
//        vectoDoc.add("Celular               : " + datos.getPropCelular());
//        //TOMADOR
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//        List<String> cadenaInfor = formatearLineaCadenaPeque("Para actualizar la información del presente formulario apersonese a nuestras oficinas o ingresar a la página web www.univida.bo", 0);
//        for (String strInfor : cadenaInfor){
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax(strInfor));
//        }
//
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//        vectoDoc.add("         Línea gratuita: 800-10-8444");
//        vectoDoc.add("    Central teléfonica: " + datos.getFacturaMaestro().getTelefonosEmpresa());
//        vectoDoc.add(formatearCentradoCadenaPequeniaPax("www.univida.bo"));
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//
//        if (datos.getSoatMensaje() != null) {
//            List<String> listaMensaje = formatearLineaCadenaPeque(String.valueOf(datos.getSoatMensaje()), 0);
//            for (String strMensaje : listaMensaje){
//                vectoDoc.add(formatearCentradoCadenaPequeniaPax(strMensaje));
//            }
//        }
//        vectoDoc.add(" ");
//        vectoDoc.add(formatearCentradoCadenaPequeniaPax("VIGENCIA DE LA COBERTURA SOAT " + datos.getSoatGestionFk()));
//        vectoDoc.add(formatearCentradoCadenaPequeniaPax("Del " + datos.getSoatFechaCoberturaInicio() + " hasta el " + datos.getSoatFechaCoberturaFin()));
//        vectoDoc.add(" ");
//        vectoDoc.add(formatearCentradoCadenaPequeniaPax("VIGENCIA COBERTURA"));
//
//        String fechaIniSoat = "01/01/" + datos.getSoatGestionFk();
//        if (datos.getSoatGestionFk() == 2021){
//            fechaIniSoat = "01/05/" + datos.getSoatGestionFk();
//        };
//
//        List<String> listaVigen = formatearLineaCadenaPeque("El SOAT " + datos.getSoatGestionFk() + " adquirido a partir del " + fechaIniSoat + " entra en vigor desde las 00:00 horas del dia siguiente de la adquisicion.", 0);
//
//        for (String strVigen : listaVigen){
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax(strVigen));
//        }
//
//        pImp.addAll(vectoDoc);

    }
    public void prepararImpresionFacturaSoatc(User user, EmiPolizaObtenerResponse efectivizarRespUnivida) {

    }

//    public void prepararImpresionFactura2022(User user, EfectivizarRespUnivida efectivizarRespUnivida) {
//
//        LogUtils.i(TAG, "PREPARAMOS FACTURA");
//
//        if (pImp != null){
//            pImp.clear();
//        }
//
//        pImp = new Vector<>();
//        Vector<Object> vectoDoc = new Vector<>();
//
//        Datos datos = efectivizarRespUnivida.getDatos();
//
////        List<String> nombreEmpresa = formatearLineaCadena2(datos.getFacturaMaestro().getRazonSocial());
//        List<String> strLey = formatearLineaCadenaPeque(datos.getFacturaMaestro().getLeyenda(), 0);
//        List<String> strTipoEmisionLeyenda = formatearLineaCadenaPeque(datos.getFacturaMaestro().getTipoEmisionLeyenda(),0);
//        List<String> vector11 = formatearLineaCadenaPeque(datos.getFacturaMaestro().getDireccionSucursal() + " Teléfono: " + datos.getFacturaMaestro().getTelefonoSucursal(), 0);
//
//        BigDecimal bigDecimalAmount = new BigDecimal(datos.getFacturaMaestro().getImporteNumeral());
//
//        bigDecimalAmount = bigDecimalAmount.setScale(2, RoundingMode.HALF_EVEN);
//        StringBuilder sbSubTotal = new StringBuilder("SUBTOTAL Bs" + " " + bigDecimalAmount.toString());
//        StringBuilder sbDescuento = new StringBuilder("DESCUENTO Bs" + " " + bigDecimalAmount.toString());
//        StringBuilder sbTotal = new StringBuilder("TOTAL Bs" + " " + bigDecimalAmount.toString());
//        StringBuilder sbMontoPagar = new StringBuilder("MONTO A PAGAR Bs" + " " + bigDecimalAmount.toString());
//        StringBuilder sbImporteBaseCreditoFiscal = new StringBuilder("IMPORTE BASE CRÉDITO FISCAL Bs" + " " + bigDecimalAmount.toString());
//
//
//        Vector<Object> nnotes = new Vector<>();
//
//        List<String> literal = formatearLineaCadenaPeque("SON: " + datos.getFacturaMaestro().getImporteLiteral(),0);
//
//        List<Detalle> detalles = datos.getFacturaMaestro().getDetalle();
//
//        for (Detalle detalle : detalles){
//            String lineaDatos = construirFila(decimalFormat.format(detalle.getCantidad()), "", decimalFormat.format(detalle.getImporteUnitario()), decimalFormat.format(detalle.getImporteTotal()));
////            String lineaDatos = construirFila(decimalFormat.format(detalle.getCantidad()), "", (detalle.getImporteUnitario()), (detalle.getImporteTotal()));
//            nnotes.add(lineaDatos);
//
//            nnotes.add(formatearLineaCadenaPeque(detalle.getLineaDetalle(), 0));
//
//        }
//
//        LogUtils.i("IMPRESION ", "TRY");
//
//        LogUtils.i("IMPRESION ", "logo");
//
//        vectoDoc.add(10);
//        vectoDoc.add(String.valueOf(R.raw.logop));
//
//        vectoDoc.add(formatearCentradoCadenaPequeniaPax("CASA MATRIZ"));
//
//        List<String> vector111 = formatearLineaCadenaPeque(datos.getFacturaMaestro().getDireccionEmpresa() + " Teléfono: " + datos.getFacturaMaestro().getTelefonosEmpresa(), 0);
//        for (String strDire2 : vector111){
//
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax(strDire2));
//
//        }
//
//        vectoDoc.add(formatearCentradoCadenaPequeniaPax(datos.getFacturaMaestro().getLugar()));
//        vectoDoc.add(" ");
//
//        if (datos.getFacturaMaestro().getNumeroSucursal() != 0) {
//
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax("SUCURSAL: " + datos.getFacturaMaestro().getNumeroSucursal()));
//
//            for (String strDire : vector11){
//
//                vectoDoc.add(formatearCentradoCadenaPequeniaPax(strDire));
//
//            }
//
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax(datos.getFacturaMaestro().getMunicipioDepartamento()));
//        }
//
//        vectoDoc.add(formatearCentradoCadenaPequeniaPax("PDV: " + datos.getFacturaMaestro().getPuntoVentaCodigo()));
//
//        vectoDoc.add(110);
//        vectoDoc.add(formatearCentradoCadenaPequeniaDoblePax("FACTURA"));
//        if(datos.getFacturaMaestro().getTipoEmision() != null && (! datos.getFacturaMaestro().getTipoEmision().isEmpty())){
//            List<String> tipoEmisionLista = formatearLineaCadenaPeque(datos.getFacturaMaestro().getTipoEmision(), 0);
//            for (String strTipoEmision : tipoEmisionLista){
//                vectoDoc.add(formatearCentradoCadenaPequeniaPax(strTipoEmision));
//            }
//        }
//
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//
//        vectoDoc.add(101);
//        vectoDoc.add("  NIT              : ");
//        vectoDoc.add(110);
//        vectoDoc.add(datos.getFacturaMaestro().getNitEmpresa());
//        vectoDoc.add(101);
//        vectoDoc.add("  FACTURA No.      : ");
//        vectoDoc.add(110);
//        vectoDoc.add(datos.getFacturaMaestro().getNumeroFactura().toString());
//        vectoDoc.add(101);
//        vectoDoc.add("  AUTORIZACIÓN No.: ");
//        vectoDoc.add(110);
//        vectoDoc.add(datos.getFacturaMaestro().getNumeroAutorizacion());
//
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//
//        List<String> activitys = formatearLineaCadenaPeque(datos.getFacturaMaestro().getActividadEconomica(), 0);
//
//        for (String strActividad : activitys){
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax(strActividad));
//        }
//
//        String fecha1 = datos.getFacturaMaestro().getFechaEmision();
//        fecha1 = fecha1.replace('-', '/');
//
//
//        vectoDoc.add("FECHA        : " + fecha1);
//        vectoDoc.add("NIT/CI       : " + datos.getFacturaMaestro().getNitCiCliente());
//        vectoDoc.addAll(formatearLineaCadenaPeque("RAZÓN SOCIAL: " + datos.getFacturaMaestro().getRazonSocialClient(), 10));
//
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//
//        vectoDoc.add(construirFila("Cant.", " ", "Precio", "Sub total"));
//
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//
//        for (int i = 0; i < nnotes.size(); i += 2) {
//
//            List<String> nnotes2 = (List<String>) nnotes.get(i + 1);
//
//            vectoDoc.addAll(nnotes2);
//
//            vectoDoc.add(String.valueOf(nnotes.get(i)));
//
//        }
//
//        //vectoDoc.add(getApplicationContext().getResources().openRawResource(R.raw.linea));
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//        // colocamos valor unao para imprimir cadenas al revez y con alineacion a la derecha
//        vectoDoc.add(102);
//        vectoDoc.add(102);
//        vectoDoc.add(sbSubTotal.toString());
//        vectoDoc.add(102);
//        vectoDoc.add(sbDescuento.toString());
//        vectoDoc.add(102);
//        vectoDoc.add(sbTotal.toString());
//        vectoDoc.add(102);
//        vectoDoc.add(sbMontoPagar.toString());
//        vectoDoc.add(102);
//        vectoDoc.add(sbImporteBaseCreditoFiscal.toString());
//
////        vectoDoc.add("MONTO A PAGAR: Bs " + decimalFormat.format(bigDecimalAmount));
//        vectoDoc.addAll(literal);
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//
////        vectoDoc.add(101);
////        vectoDoc.add("CÓDIGO DE CONTROL: ");
////        vectoDoc.add(110);
////        vectoDoc.add(datos.getFacturaMaestro().getCodigoControl());
////        vectoDoc.add("FECHA LÍMITE DE EMISIÓN: " + datos.getFacturaMaestro().getFechaLimiteEmision());
//
//
//        vectoDoc.add("USUARIO: " + user.getDatosUsuario().getEmpleadoNombreCompleto());
//
//        List<String> listaLeyenda = formatearLineaCadenaPeque("\"ESTA FACTURA CONTRIBUYE AL DESARROLLO DEL PAIS, EL USO ILÍCITO DE ESTA SERÁ SANCIONADO PENALMENTE DE ACUERDO A LEY\"", 0);
//        for (String strLeyenda : listaLeyenda){
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax(strLeyenda));
//        }
//
//        for (String strLe : strLey) {
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax(strLe));
//        }
//
//        for (String strTipoEmision : strTipoEmisionLeyenda) {
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax(strTipoEmision));
//        }
//
//        String informacionQr = datos.getFacturaMaestro().getCodigoQr();
//
////        vectoDoc.add(200);
//        vectoDoc.add(getQr(informacionQr));
//
//        vectoDoc.add(" ");
//        String corteAqui = ">>>--------------- CORTE AQUI ---------------<<<";
//        vectoDoc.add(corteAqui);
//        vectoDoc.add(" ");
//        vectoDoc.add(10);
//        vectoDoc.add(String.valueOf(R.raw.logop));
//
//        if (datos.getSoatQrContenido() != null) {
//            vectoDoc.add(200);
//            vectoDoc.add(getQr(datos.getSoatQrContenido().get(0)));
//        }
//
//        vectoDoc.add(110);
//        vectoDoc.add((formatearCentradoCadenaPequeniaDoblePax("COMPROBANTE SOAT " + datos.getSoatGestionFk())));
//
//        //SOAT
//        vectoDoc.add("Fecha emision         : " + datos.getFacturaMaestro().getFechaEmision());
//        vectoDoc.add("Número de comprobante: " + decimalFormatLleno.format(datos.getSoatNumeroComprobante()));
//        vectoDoc.add("Número de factura    : " + decimalFormatLleno.format(datos.getFacturaMaestro().getNumeroFactura()));
//        vectoDoc.add("Número de roseta     : " + decimalFormatLleno.format(datos.getSoatRosetaNumero()));
//        //SOAT
//        vectoDoc.add(" ");
////        vectoDoc.add(210);
////        vectoDoc.add(5);
//        //TOMADOR
//        vectoDoc.addAll(formatearLineaCadenaPeque("Comprador             : " + datos.getPropTomador(), 10));
//        vectoDoc.add("CI                    : " + datos.getPropCi());
//        vectoDoc.add("NIT                   : " + datos.getPropNit());
//        vectoDoc.addAll(formatearLineaCadenaPeque("Dirección            : " + datos.getPropDireccion(), 10));
//        vectoDoc.add("Teléfono             : " + datos.getPropTelefono());
//        vectoDoc.add("Celular               : " + datos.getPropCelular());
//        //TOMADOR
//        vectoDoc.add(" ");
////        vectoDoc.add(210);
////        vectoDoc.add(5);
//
//        //VEHICULO
//        vectoDoc.add("Placa                 : " +datos.getVehiculoPlaca());
//        vectoDoc.add("Marca                 : " + datos.getVehiculoMarca());
//        vectoDoc.add("Color                 : " + datos.getVehiculoColor());
//        vectoDoc.add("Tipo de vehiculo      : " + datos.getSoatVehiculoTipoDescripcion());
//        vectoDoc.add("Modelo                : " + datos.getVehiculoModelo());
//        vectoDoc.add("Año                  : " + datos.getVehiculoAnio());
//        vectoDoc.add("Cilindrada            : " + (datos.getVehiculoCilindrada() != null ? datos.getVehiculoCilindrada(): ""));
//        vectoDoc.add("Nro de Motor          : " + datos.getVehiculoMotor());
//        vectoDoc.add("Cap. de carga         : " + (datos.getVehiculoCapacidadCarga() != null ? datos.getVehiculoCapacidadCarga(): ""));
//        vectoDoc.add("Plaza circulacion     : " + datos.getSoatDepartamentoPcDescription());
//        vectoDoc.add("Nro Chasis            : " + datos.getVehiculoChasis());
//        vectoDoc.add("Tipo de uso           : " + datos.getSoatVehiculoUsoDescription());
//        //VEHICULO
//
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
////        vectoDoc.add(210);
////        vectoDoc.add(5);
//        // mensaje
//        if (datos.getSoatMensaje() != null) {
//            vectoDoc.add(" ");
//            List<String> listaMensaje = formatearLineaCadenaPeque(String.valueOf(datos.getSoatMensaje()), 0);
//            for (String strMensaje : listaMensaje){
//                vectoDoc.add(formatearCentradoCadenaPequeniaPax(strMensaje));
//            }
//            vectoDoc.add(" ");
//            vectoDoc.add(3);
//            vectoDoc.add(linea);
//        }
//        //mensaje
//        vectoDoc.add(" ");
//        vectoDoc.add(formatearCentradoCadenaPequeniaPax("VIGENCIA DE LA COBERTURA SOAT " + datos.getSoatGestionFk()));
//        vectoDoc.add(formatearCentradoCadenaPequeniaPax("Del " + datos.getSoatFechaCoberturaInicio() + " hasta el " + datos.getSoatFechaCoberturaFin()));
//        vectoDoc.add(" ");
//        vectoDoc.add(formatearCentradoCadenaPequeniaPax("VIGENCIA COBERTURA"));
//
//        String fechaIniSoat = "01/01/" + datos.getSoatGestionFk();
//        if (datos.getSoatGestionFk() == 2021){
//            fechaIniSoat = "01/05/" + datos.getSoatGestionFk();
//        };
//
//        List<String> listaVigen = formatearLineaCadenaPeque("El SOAT " + datos.getSoatGestionFk() + " adquirido a partir del " + fechaIniSoat + " entra en vigor desde las 00:00 horas del dia siguiente de la adquisicion.", 0);
//
//        for (String strVigen : listaVigen){
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax(strVigen));
//        }
//
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//        List<String> cadenaInfor = formatearLineaCadenaPeque("Para actualizar la informacion del presente formulario apersonese a nuestras oficinas o ingresar a la página web www.univida.bo", 0);
//        for (String strInfor : cadenaInfor){
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax(strInfor));
//        }
//
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//        vectoDoc.add("         Linea gratuita: 800-10-8444");
//        vectoDoc.add("    Central teléfonica: " + datos.getFacturaMaestro().getTelefonosEmpresa());
//        vectoDoc.add(formatearCentradoCadenaPequeniaPax("www.univida.bo"));
//        vectoDoc.add(3);
//        vectoDoc.add(linea);
//
//        if (datos.getSoatQrContenido().size() > 1) {
//            // extra 2022
//            vectoDoc.add(" ");
//            vectoDoc.add(corteAqui);
//            vectoDoc.add(" ");
//            vectoDoc.add(10);
//            vectoDoc.add(String.valueOf(R.raw.logop));
//
//            List<String> listaString01 = formatearLineaCadenaPeque("Para descargar tu Roseta Digital, escanea desde tu dispositivo móvil el siguiente código QR",0);
//            for (String str01 : listaString01){
//                vectoDoc.add(formatearCentradoCadenaPequeniaPax(str01));
//            }
//            if (datos.getSoatQrContenido() != null) {
//                vectoDoc.add(200);
//                vectoDoc.add(getQr(datos.getSoatQrContenido().get(1)));
//            }
//
////            vectoDoc.add(110);
//            vectoDoc.add(formatearCentradoCadenaPequeniaPax("Aclaraciones:"));
//
//            List<String> listaString02 = formatearLineaCadenaPeque("-La Roseta Dígital puede ser descargada en cualquier momento, sin afectar la vigencia de la cobertura del SOAT " + datos.getSoatGestionFk(), 0);
//            List<String> listaString03 = formatearLineaCadenaPeque("-No es necesario que la Roseta Digital sea impresa ni adherida al motorizado.",0);
//            for (String str02 : listaString02){
//                vectoDoc.add((str02));
//            }
//            for (String str03 : listaString03){
//                vectoDoc.add((str03));
//            }
//
//        }
//
//        pImp.addAll(vectoDoc);
//
//    }
    public void prepararImpresionFactura2022FontNormal(User user, EfectivizarRespUnivida efectivizarRespUnivida) {

        LogUtils.i(TAG, "PREPARAMOS FACTURA");

        if (pImp != null){
            pImp.clear();
        }

        pImp = new Vector<>();
        Vector<Object> vectoDoc = new Vector<>();

        Datos datos = efectivizarRespUnivida.getDatos();

//        List<String> nombreEmpresa = formatearLineaCadena2(datos.getFacturaMaestro().getRazonSocial());
        List<String> strLey = formatearLineaCadena2(datos.getFacturaMaestro().getLeyenda());
        List<String> strTipoEmisionLeyenda = formatearLineaCadena2(datos.getFacturaMaestro().getTipoEmisionLeyenda());
        List<String> vector11 = formatearLineaCadena2(datos.getFacturaMaestro().getDireccionSucursal() + " Teléfono: " + datos.getFacturaMaestro().getTelefonoSucursal());

//        BigDecimal bigDecimalAmount = new BigDecimal(datos.getFacturaMaestro().getImporteNumeral());

//        bigDecimalAmount = bigDecimalAmount.setScale(2, RoundingMode.HALF_EVEN);
        StringBuilder sbSubTotal = new StringBuilder("SUBTOTAL Bs" + " " + datos.getFacturaMaestro().getImporteSubtotalStr());
        StringBuilder sbDescuento = new StringBuilder("DESCUENTO Bs" + " " + datos.getFacturaMaestro().getImporteDescuentoStr());
        StringBuilder sbTotal = new StringBuilder("TOTAL Bs" + " " + datos.getFacturaMaestro().getImporteNumeralStr());
        StringBuilder sbMontoPagar = new StringBuilder("MONTO A PAGAR Bs" + " " + datos.getFacturaMaestro().getImporteNumeralStr());
        StringBuilder sbImporteBaseCreditoFiscal = new StringBuilder("IMPORTE BASE CRÉDITO FISCAL Bs" + " " + datos.getFacturaMaestro().getImporteBaseCreditoFiscalStr());


        Vector<Object> nnotes = new Vector<>();

        List<String> literal = formatearLineaCadena2("SON: " + datos.getFacturaMaestro().getImporteLiteral());

        List<Detalle> detalles = datos.getFacturaMaestro().getDetalle();

        for (Detalle detalle : detalles){
            nnotes.add(formatearLineaCadena2(detalle.getCatalogoCodigo() + " - " + detalle.getUnidadMedida() + "/ " + detalle.getLineaDetalle()));

            String lineaDatos = (decimalFormat.format(detalle.getCantidad()) + "x " + decimalFormat.format(detalle.getImporteUnitario()) + " - " + decimalFormat.format(detalle.getDescuento())+ "   " + decimalFormat.format(detalle.getImporteTotal()));
            nnotes.add(lineaDatos);

        }

        LogUtils.i("IMPRESION ", "TRY");

        LogUtils.i("IMPRESION ", "logo");

        vectoDoc.add(10);
        vectoDoc.add(String.valueOf(R.raw.logop));

        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena(datos.getFacturaMaestro().getTitulo()));
        if(datos.getFacturaMaestro().getSubtitulo() != null && (! datos.getFacturaMaestro().getSubtitulo().isEmpty())){
            List<String> tipoSubtitulo = formatearLineaCadena2(datos.getFacturaMaestro().getSubtitulo());
            for (String strTipoSubtitulo : tipoSubtitulo){
                vectoDoc.add(110);
                vectoDoc.add(formatearCentradoCadena(strTipoSubtitulo));
            }
        }

        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena("CASA MATRIZ"));

        List<String> vector111 = formatearLineaCadena2(datos.getFacturaMaestro().getDireccionEmpresa() + " Teléfono: " + datos.getFacturaMaestro().getTelefonosEmpresa());
        for (String strDire2 : vector111){
            vectoDoc.add(110);
            vectoDoc.add(formatearCentradoCadena(strDire2));

        }

        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena(datos.getFacturaMaestro().getLugar()));
        vectoDoc.add(" ");

        if (datos.getFacturaMaestro().getNumeroSucursal() != 0) {
            vectoDoc.add(110);
            vectoDoc.add(formatearCentradoCadena("SUCURSAL: " + datos.getFacturaMaestro().getNumeroSucursal()));

            for (String strDire : vector11){
                vectoDoc.add(110);
                vectoDoc.add(formatearCentradoCadena(strDire));

            }
            vectoDoc.add(110);
            vectoDoc.add(formatearCentradoCadena(datos.getFacturaMaestro().getMunicipioDepartamento()));
        }
        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena("PDV: " + datos.getFacturaMaestro().getPuntoVentaCodigo()));

        String linea = lineaPaxNormal;

        vectoDoc.add(3);
        vectoDoc.add(linea);

        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena("NIT"));
        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena(datos.getFacturaMaestro().getNitEmpresa()));
        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena("FACTURA No"));
        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena(datos.getFacturaMaestro().getNumeroFactura().toString()));
        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena("CÓD. AUTORIZACIÓN"));
        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena(datos.getFacturaMaestro().getCodigoControl()));

        vectoDoc.add(3);
        vectoDoc.add(linea);

//        List<String> activitys = formatearLineaCadena2(datos.getFacturaMaestro().getActividadEconomica());
//
//        for (String strActividad : activitys){
//            vectoDoc.add(110);
//            vectoDoc.add(formatearCentradoCadena(strActividad));
//        }

        String fecha1 = datos.getFacturaMaestro().getFechaEmision();
        fecha1 = fecha1.replace('-', '/');

        vectoDoc.add(110);
        vectoDoc.add("Fecha        : " + fecha1);
        vectoDoc.add(110);
        vectoDoc.add("Código de Cliente: " + datos.getFacturaMaestro().getCodigoCliente());
        vectoDoc.add(110);
        List<String> razonSocial = formatearLineaCadena2("Nombre/Razón Social: " + datos.getFacturaMaestro().getRazonSocialClient());
        for (String cadena: razonSocial) {
            vectoDoc.add(110);
            vectoDoc.add(cadena);
        }
        vectoDoc.add(110);
        vectoDoc.add("NIT/CI/CEX   : " + datos.getFacturaMaestro().getNitCiCliente());

        vectoDoc.add(3);
        vectoDoc.add(linea);
        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena("DETALLE"));
//        vectoDoc.add(construirFilaNormal("Cant.", " ", "Precio", "Sub total"));

//        vectoDoc.add(3);
//        vectoDoc.add(linea);

        for (int i = 0; i < nnotes.size(); i += 2) {

            List<String> nnotes2 = (List<String>) nnotes.get(i);
            for(String strnnotes2: nnotes2) {
                vectoDoc.add(110);
                vectoDoc.add(strnnotes2);
            }
            vectoDoc.add(110);
            vectoDoc.add(String.valueOf(nnotes.get(i+1)));

        }

        //vectoDoc.add(getApplicationContext().getResources().openRawResource(R.raw.linea));
        vectoDoc.add(3);
        vectoDoc.add(linea);
        // colocamos valor unao para imprimir cadenas al revez y con alineacion a la derecha
        vectoDoc.add(110);
        vectoDoc.add(sbSubTotal.toString());
        vectoDoc.add(110);
        vectoDoc.add(sbDescuento.toString());
        vectoDoc.add(110);
        vectoDoc.add(sbTotal.toString());
        vectoDoc.add(110);
        vectoDoc.add(sbMontoPagar.toString());
        if (datos.getFacturaMaestro().getImporteBaseCreditoFiscal() != null && datos.getFacturaMaestro().getImporteBaseCreditoFiscal() > 0) {
            vectoDoc.add(110);
            vectoDoc.add(sbImporteBaseCreditoFiscal.toString());
        }
//        vectoDoc.add(110);
//        vectoDoc.add("MONTO A PAGAR: Bs " + decimalFormat.format(bigDecimalAmount));
        for(String strLietral : literal) {
            vectoDoc.add(110);
            vectoDoc.add(strLietral);
        }
        vectoDoc.add(3);
        vectoDoc.add(linea);

//        vectoDoc.add(110);
//        vectoDoc.add("CÓDIGO DE CONTROL: " + datos.getFacturaMaestro().getCodigoControl());
//        vectoDoc.add(110);
//        vectoDoc.add("FECHA LÍMITE DE EMISIÓN: \n" + datos.getFacturaMaestro().getFechaLimiteEmision());

        List<String> listLinesUser = formatearLineaCadena2("USUARIO: " + user.getDatosUsuario().getEmpleadoNombreCompleto());
        for(String strUsr: listLinesUser) {
            vectoDoc.add(110);
            vectoDoc.add(strUsr);
        }
        vectoDoc.add(" ");
        List<String> listaLeyenda = formatearLineaCadena2("\"ESTA FACTURA CONTRIBUYE AL DESARROLLO DEL PAIS, EL USO ILÍCITO DE ESTA SERÁ SANCIONADO PENALMENTE DE ACUERDO A LEY\"");
        for (String strLeyenda : listaLeyenda){
            vectoDoc.add(110);
            vectoDoc.add(formatearCentradoCadena(strLeyenda));
        }
        vectoDoc.add(" ");
        for (String strLe : strLey) {
            vectoDoc.add(110);
            vectoDoc.add(formatearCentradoCadena(strLe));
        }
        vectoDoc.add(" ");
        for (String strTipoEmision : strTipoEmisionLeyenda) {
            vectoDoc.add(110);
            vectoDoc.add(formatearCentradoCadena(strTipoEmision));
        }

        String informacionQr = datos.getFacturaMaestro().getCodigoQr();

        vectoDoc.add(200);
        vectoDoc.add(getQr(informacionQr));

        vectoDoc.add(" ");
        vectoDoc.add(110);
        String corteAqui = ">>>------- CORTE AQUI -------<<<";
        vectoDoc.add(corteAqui);
        vectoDoc.add(" ");
        vectoDoc.add(10);
        vectoDoc.add(String.valueOf(R.raw.logop));

        if (datos.getSoatQrContenido() != null) {
            vectoDoc.add(200);
            vectoDoc.add(getQr(datos.getSoatQrContenido().get(0)));
        }

        vectoDoc.add(110);
        vectoDoc.add((formatearCentradoCadena("COMPROBANTE SOAT " + datos.getSoatGestionFk())));

        //SOAT
        vectoDoc.add(110);
        vectoDoc.add("Fecha emisión    : " + datos.getFacturaMaestro().getFechaEmision());
        vectoDoc.add(110);
        vectoDoc.add("Nº de comprobante: " + decimalFormatLleno.format(datos.getSoatNumeroComprobante()));
        vectoDoc.add(110);
        vectoDoc.add("Nº de factura    : " + decimalFormatLleno.format(datos.getFacturaMaestro().getNumeroFactura()));
        vectoDoc.add(110);
        vectoDoc.add("Nº de roseta     : " + decimalFormatLleno.format(datos.getSoatRosetaNumero()));
        //SOAT
        vectoDoc.add(" ");
//        vectoDoc.add(210);
//        vectoDoc.add(5);
        //TOMADOR
        List<String> listComprador=formatearLineaCadena2("Comprador         : " + datos.getPropTomador());
        for(String strCompra: listComprador) {
            vectoDoc.add(110);
            vectoDoc.add(strCompra);
        }
        vectoDoc.add(110);
        vectoDoc.add("CI                : " + datos.getPropCi());
        vectoDoc.add(110);
        vectoDoc.add("NIT               : " + datos.getPropNit());
        List<String> listaDireccion = formatearLineaCadena2("Dirección        : " + datos.getPropDireccion());
        for(String strDirec: listaDireccion) {
            vectoDoc.add(110);
            vectoDoc.add(strDirec);
        }
        vectoDoc.add(110);
        vectoDoc.add("Teléfono         : " + datos.getPropTelefono());
        vectoDoc.add(110);
        vectoDoc.add("Celular           : " + datos.getPropCelular());
        //TOMADOR
        vectoDoc.add(" ");
//        vectoDoc.add(210);
//        vectoDoc.add(5);

        //VEHICULO
        vectoDoc.add(110);
        vectoDoc.add("Placa             : " +datos.getVehiculoPlaca());
        vectoDoc.add(110);
        vectoDoc.add("Marca             : " + datos.getVehiculoMarca());
        vectoDoc.add(110);
        vectoDoc.add("Color             : " + datos.getVehiculoColor());
        List<String> listTipoVehiculos=formatearLineaCadena2("Tipo de vehículo :" + datos.getSoatVehiculoTipoDescripcion());
        for(String strTipoVehi: listTipoVehiculos) {
            vectoDoc.add(110);
            vectoDoc.add(strTipoVehi);
        }
//        vectoDoc.add(110);
//        vectoDoc.add("Tipo de vehículo : " + datos.getSoatVehiculoTipoDescripcion());
        vectoDoc.add(110);
        vectoDoc.add("Modelo            : " + datos.getVehiculoModelo());
        vectoDoc.add(110);
        vectoDoc.add("Año              : " + datos.getVehiculoAnio());
        vectoDoc.add(110);
        vectoDoc.add("Cilindrada        : " + (datos.getVehiculoCilindrada() != null ? datos.getVehiculoCilindrada(): ""));
        vectoDoc.add(110);
        vectoDoc.add("Nro de Motor      : " + datos.getVehiculoMotor());
        vectoDoc.add(110);
        vectoDoc.add("Cap. de carga     : " + (datos.getVehiculoCapacidadCarga() != null ? datos.getVehiculoCapacidadCarga(): ""));
        vectoDoc.add(110);
        vectoDoc.add("Plaza circulación: " + datos.getSoatDepartamentoPcDescription());
        vectoDoc.add(110);
        vectoDoc.add("Nro Chasis        : " + datos.getVehiculoChasis());
        vectoDoc.add(110);
        vectoDoc.add("Tipo de uso       : " + datos.getSoatVehiculoUsoDescription());
        //VEHICULO

        vectoDoc.add(3);
        vectoDoc.add(linea);
//        vectoDoc.add(210);
//        vectoDoc.add(5);
        // mensaje
        if (datos.getSoatMensaje() != null) {
            vectoDoc.add(" ");
            List<String> listaMensaje = formatearLineaCadena2(String.valueOf(datos.getSoatMensaje()));
            for (String strMensaje : listaMensaje){
                vectoDoc.add(110);
                vectoDoc.add(formatearCentradoCadena(strMensaje));
            }
            vectoDoc.add(" ");
            vectoDoc.add(3);
            vectoDoc.add(linea);
        }
        //mensaje
        vectoDoc.add(" ");
        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena("VIGENCIA DE LA COBERTURA SOAT"));
        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena("" + datos.getSoatGestionFk()));
        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena("Del " + datos.getSoatFechaCoberturaInicio() + " hasta el "));
        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena("" + datos.getSoatFechaCoberturaFin()));
        vectoDoc.add(" ");
        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena("VIGENCIA COBERTURA"));

        String fechaIniSoat = "01/01/" + datos.getSoatGestionFk();
        if (datos.getSoatGestionFk() == 2021){
            fechaIniSoat = "01/05/" + datos.getSoatGestionFk();
        };

        List<String> listaVigen = formatearLineaCadena2("El SOAT " + datos.getSoatGestionFk() + " adquirido a partir del " + fechaIniSoat + " entra en vigor desde las 00:00 horas del dia siguiente de la adquisicion.");

        for (String strVigen : listaVigen){
            vectoDoc.add(110);
            vectoDoc.add(formatearCentradoCadena(strVigen));
        }

        vectoDoc.add(3);
        vectoDoc.add(linea);
        List<String> cadenaInfor = formatearLineaCadena2("Para actualizar la información del presente formulario apersonese a nuestras oficinas o ingresar a la página web www.univida.bo");
        for (String strInfor : cadenaInfor){
            vectoDoc.add(110);
            vectoDoc.add(formatearCentradoCadena(strInfor));
        }

        vectoDoc.add(3);
        vectoDoc.add(linea);
        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena("Línea gratuita: 800-10-8444"));
        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena("Central teléfonica: " + datos.getFacturaMaestro().getTelefonosEmpresa()));
        vectoDoc.add(110);
        vectoDoc.add(formatearCentradoCadena("www.univida.bo"));
        vectoDoc.add(3);
        vectoDoc.add(linea);

        if (datos.getSoatQrContenido().size() > 1) {
            // extra 2022
            vectoDoc.add(" ");
            vectoDoc.add(110);
            vectoDoc.add(corteAqui);
            vectoDoc.add(" ");
            vectoDoc.add(10);
            vectoDoc.add(String.valueOf(R.raw.logop));

            List<String> listaString01 = formatearLineaCadena2("Para descargar tu Roseta Digital, escanea desde tu dispositivo móvil el siguiente código QR");
            for (String str01 : listaString01){
                vectoDoc.add(110);
                vectoDoc.add(formatearCentradoCadena(str01));
            }
            if (datos.getSoatQrContenido() != null) {
                vectoDoc.add(200);
                vectoDoc.add(getQr(datos.getSoatQrContenido().get(1)));
            }

            vectoDoc.add(110);
            vectoDoc.add(formatearCentradoCadena("Aclaraciones:"));

            List<String> listaString02 = formatearLineaCadena2("-La Roseta Dígital puede ser descargada en cualquier momento, sin afectar la vigencia de la cobertura del SOAT " + datos.getSoatGestionFk());
            List<String> listaString03 = formatearLineaCadena2("-No es necesario que la Roseta Digital sea impresa ni adherida al motorizado.");
            for (String str02 : listaString02){
                vectoDoc.add(110);
                vectoDoc.add((str02));
            }
            for (String str03 : listaString03){
                vectoDoc.add(110);
                vectoDoc.add((str03));
            }

        }

        pImp.addAll(vectoDoc);

    }

    public void procesarColillaVentaSoat(User user, EfectivizarRespUnivida efectivizarRespUnivida, Date fecha){
        procesarColillaVentaSoatNormal(user,efectivizarRespUnivida,fecha);
//        if (pImp != null){
//            pImp.clear();
//        }
//        Datos datos = efectivizarRespUnivida.getDatos();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//
//
//        pImp = new Vector<>();
//
//
//        Vector<Object> vectoColilla = new Vector<>();
//        vectoColilla.add("PLACA                : " +datos.getVehiculoPlaca());
//        vectoColilla.add("SOAT No. COMPROBANTE : " + datos.getSoatNumeroComprobante());
//        vectoColilla.add("Fecha de emision     : " + datos.getFacturaMaestro().getFechaEmision());
//        vectoColilla.add("NIT                  : " + datos.getFacturaMaestro().getNitEmpresa());
//        vectoColilla.add("No. AUTORIZACION     : " + datos.getFacturaMaestro().getNumeroAutorizacion());
//        vectoColilla.add("No. FACTURA          : " + decimalFormatLleno. format(datos.getFacturaMaestro().getNumeroFactura()));
//        vectoColilla.add("NIT/CI               : " + datos.getFacturaMaestro().getNitCiCliente());
//        vectoColilla.addAll(formatearLineaCadenaPeque("Nombre/Razón Social : " + datos.getFacturaMaestro().getRazonSocialClient(), 10));
//        vectoColilla.add("Monto Total Bs       : " + datos.getFacturaMaestro().getImporteNumeral());
//
//        vectoColilla.add("FECHA/HORA Impresión: " + simpleDateFormat.format(fecha));
//        vectoColilla.addAll(formatearLineaCadenaPeque("CODIGO               : " + user.getDatosUsuario().getEmpleadoSecuencial(), 10));
//        vectoColilla.addAll(formatearLineaCadenaPeque("USUARIO              : " + user.getDatosUsuario().getEmpleadoNombreCompleto(), 10));
//        vectoColilla.addAll(formatearLineaCadenaPeque("CARGO                : " + user.getDatosUsuario().getEmpleadoCargo(), 10));
//
//        pImp.addAll(vectoColilla);
    }
    public void procesarColillaVentaSoatc(User user, EmiPolizaObtenerResponse efectivizarRespUnivida, Date fecha){
    }

    public void procesarColillaVentaSoatNormal(User user, EfectivizarRespUnivida efectivizarRespUnivida, Date fecha){
        if (pImp != null){
            pImp.clear();
        }
        Datos datos = efectivizarRespUnivida.getDatos();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


        pImp = new Vector<>();


        Vector<Object> vectoColilla = new Vector<>();
        vectoColilla.add(110);
        vectoColilla.add("PLACA              : " +datos.getVehiculoPlaca());
        vectoColilla.add(110);
        vectoColilla.add("ROSETA            : " +datos.getSoatRosetaNumero());
        vectoColilla.add(110);
        vectoColilla.add("SOAT No. COMPROBANTE: " + datos.getSoatNumeroComprobante());
        vectoColilla.add(110);
        vectoColilla.add("Fecha de emisión  : " + datos.getFacturaMaestro().getFechaEmision());
        vectoColilla.add(110);
        vectoColilla.add("NIT                : " + datos.getFacturaMaestro().getNitEmpresa());
        vectoColilla.add(110);
        vectoColilla.add("No. AUTORIZACIÓN  : " + datos.getFacturaMaestro().getNumeroAutorizacion());
        vectoColilla.add(110);
        vectoColilla.add("No. FACTURA        : " + decimalFormatLleno. format(datos.getFacturaMaestro().getNumeroFactura()));
        vectoColilla.add(110);
        vectoColilla.add("NIT/CI             : " + datos.getFacturaMaestro().getNitCiCliente());
        List<String> listacliente = formatearLineaCadena2("Nombre/Razón Social: " + datos.getFacturaMaestro().getRazonSocialClient());
//        vectoColilla.addAll(formatearLineaCadenaPeque("Nombre/Razón Social : " + datos.getFacturaMaestro().getRazonSocialClient(), 10));
        for (String strCliente : listacliente){
            vectoColilla.add(110);
            vectoColilla.add(strCliente);
        }
        vectoColilla.add(110);
        vectoColilla.add("Monto Total Bs     : " + datos.getFacturaMaestro().getImporteNumeral());
        List<String> listaFecha = formatearLineaCadena2("FECHA/HORA Impresión: " + simpleDateFormat.format(fecha));
        for(String strFecha : listaFecha) {
            vectoColilla.add(110);
            vectoColilla.add(strFecha);
        }
//        vectoColilla.add(110);
//        vectoColilla.add("FECHA/HORA Impresión: " + simpleDateFormat.format(fecha));
        List<String> listaCodigos = formatearLineaCadena2("CÓDIGO            : " + user.getDatosUsuario().getEmpleadoSecuencial());
        for (String strCodigo : listaCodigos) {
            vectoColilla.add(110);
            vectoColilla.add(strCodigo);
//            vectoColilla.addAll(formatearLineaCadenaPeque("CODIGO               : " + user.getDatosUsuario().getEmpleadoSecuencial(), 10));
        }
        List<String> listaUsuario = formatearLineaCadena2("USUARIO            : " + user.getDatosUsuario().getEmpleadoNombreCompleto());
        for (String strUser : listaUsuario) {
            vectoColilla.add(110);
            vectoColilla.add(strUser);
//            vectoColilla.addAll(formatearLineaCadenaPeque("USUARIO              : " + user.getDatosUsuario().getEmpleadoNombreCompleto(), 10));
        }
        List<String> listaCargo = formatearLineaCadena2("CARGO              : " + user.getDatosUsuario().getEmpleadoCargo());
        for (String strCargo : listaCargo) {
            vectoColilla.add(110);
            vectoColilla.add(strCargo);
//            vectoColilla.addAll(formatearLineaCadenaPeque("CARGO                : " + user.getDatosUsuario().getEmpleadoCargo(), 10));
        }

        pImp.addAll(vectoColilla);
    }

    private boolean sonMayusculas(String cadena) {
        String mayus = cadena.toUpperCase();
        return mayus.equals(cadena);
    }

    private boolean sonMinisculas(String cadena) {
        String minus = cadena.toLowerCase();
        return minus.equals(cadena);
    }

    public List<String> formatearLineaCadenaPeque(String cadena, int tipo) {
        List<String> resp = new ArrayList<>();
        if (cadena == null){
            return resp;
        }
        int tamanio_Total = 46;

        String cadena2 = cadena + "";

        String vacio = cleanString(cadena2);

        if (! cadena.equals(vacio)){
            tamanio_Total = 46;
        }

        String[] splitCaduno = cadena.split(":");

        StringBuilder linea = new StringBuilder();

        if (tipo == 10) {

            cadena = "";

            if (splitCaduno.length > 1) {
                linea.append(splitCaduno[0]).append(":");
                StringBuilder cadenaBuilder = new StringBuilder(cadena);
                for (int i = 1; i < splitCaduno.length; i++) {
                    if (i == 1) {
                        cadenaBuilder = new StringBuilder(splitCaduno[i].trim());
                    } else {
                        cadenaBuilder.append(" ").append(splitCaduno[i].trim());
                    }

                }
                cadena = cadenaBuilder.toString().trim();
            } else {
                cadena = splitCaduno[0].trim();
            }
        }

        String[] cadenas = cadena.split(" ");

        String linea2;

        for (String cadena1 : cadenas) {
            linea2 = cadena1;

            if ((linea2.length() + linea.length() + 1) > tamanio_Total) {
                resp.add(linea + "");

                linea = new StringBuilder(linea2);
            } else {

                if (linea.length() == 0) {
                    linea.append(linea2);
                } else {
                    linea.append(" ").append(linea2);
                }
            }
        }

        if (linea.length() > 0) {
            resp.add(linea.toString());
        }

        return resp;
    }

    public List<String> formatearLineaCadenaPequeDoble(String cadena) {
        List<String> resp = new ArrayList<>();
        int tamanio_Total = 32;

        String cadena2 = cadena + "";

        String vacio = cleanString(cadena2);

        if (! cadena.equals(vacio)){
            tamanio_Total -= 2;
        }

        String[] cadenas = cadena.split(" ");

        StringBuilder linea = new StringBuilder();
        String linea2;

        for (String cadena1 : cadenas) {
            linea2 = cadena1;

            if ((linea2.length() + linea.length() + 1) > tamanio_Total) {
                resp.add(linea + "");

                linea = new StringBuilder(linea2);
            } else {

                if (linea.length() == 0) {
                    linea.append(linea2);
                } else {
                    linea.append(" ").append(linea2);
                }
            }
        }

        if (linea.length() > 0) {
            resp.add(linea.toString());
        }

        return resp;
    }

    public static String cleanString(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }

    public List<String> formatearLineaCadena2(String cadena) {
        List<String> resp = new ArrayList<>();
        int tamanio_Total = 0;


        if (sonMayusculas(cadena)) {
            tamanio_Total = 30;
        } else if (sonMinisculas(cadena)) {
            tamanio_Total = 31;
        } else {
            tamanio_Total = 31;
        }

        String[] cadenas = cadena.split(" ");

        StringBuilder linea = new StringBuilder();
        String linea2 = "";

        for (int j = 0; j < cadenas.length; j++) {
            linea2 = cadenas[j];

            if ((linea2.length() + linea.length() + 1) > tamanio_Total) {
                resp.add(linea + "");
                //LogUtils.i("iforlinea-1",linea + " tamanio " + linea.length());
                linea = new StringBuilder(linea2);
            } else {

                if (linea.length() == 0) {
                    linea.append(linea2);
                } else {
                    linea.append(" ").append(linea2);
                }
            }
        }

        if (linea.length() > 0) {
            resp.add(linea.toString());
            //LogUtils.i("fforlinea-1",linea + " tamanio " + linea.length());
        }

        return resp;
    }
    public String formatearCentradoCadena(String cadena) {
        int tamanio_Total;


        if (sonMayusculas(cadena)) {
            tamanio_Total = 30;
        } else if (sonMinisculas(cadena)) {
            tamanio_Total = 31;
        } else {
            tamanio_Total = 31;
        }



        String linea3 = cadena;

        int tamdos = (tamanio_Total - linea3.length()) / 2;

        for (int g = 0; g < tamdos; g++) {
            linea3 = " " + linea3;
        }
        //LogUtils.i("adic", " cadena con espacios |" + linea3);

        return linea3;
    }

    public String formatearCentradoCadenaPequeniaPax(String cadena) {
        int tamanio_Total = 45;

        String linea3 = cadena;

        int tamdos = (tamanio_Total - linea3.length()) / 2;

        for (int g = 0; g < tamdos; g++) {
            linea3 = " " + linea3;
        }

        return linea3;
    }

    public String formatearCentradoCadenaPequeniaDoblePax(String cadena) {
        int tamanio_Total = 31;

        String linea3 = cadena;

        int tamdos = (tamanio_Total - linea3.length()) / 2;

        for (int g = 0; g < tamdos; g++) {
            linea3 = " " + linea3;
        }

        return linea3;
    }

    private String construirFila(String cantidad, String detalle, String concepto, String monto) {

        int valorInicial1 = 0, valorInicial2 = 0, valorInicial3 = 0;

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1 && Build.BRAND.toUpperCase().equals("MOBIWIRE") && Build.MODEL.toUpperCase().equals("MOBIPRINT")) {
            valorInicial1 = 8;
            valorInicial2 = 12;
            valorInicial3 = 15;
        }else{
            valorInicial1 = 10;
            valorInicial2 = 15;
            valorInicial3 = 10;
        }

        int espCan = valorInicial1 - cantidad.length();
        int espDet = valorInicial2 - detalle.length();
        int espCon = valorInicial3 - concepto.length();

        StringBuilder linea = new StringBuilder("" + cantidad);
        String espacio = " ";

        for (int i = 0; i < espCan; i++) {
            linea.append(espacio);
        }

        linea.append(detalle);
        for (int i1 = 0; i1 < espDet; i1++) {
            linea.append(espacio);
        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1 && Build.BRAND.toUpperCase().equals("MOBIWIRE") && Build.MODEL.toUpperCase().equals("MOBIPRINT")) {
            if (espDet == valorInicial2) {
                linea.append("    ");
            }
        }

        linea.append(concepto);
        for (int i2 = 0; i2 < espCon; i2++) {
            linea.append(espacio);
        }

        linea.append(monto);


        return linea.toString();
    }

    private String construirFilaNormal(String cantidad, String detalle, String concepto, String monto) {

        int valorInicial1 = 0, valorInicial2 = 0, valorInicial3 = 0;

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1 && Build.BRAND.toUpperCase().equals("MOBIWIRE") && Build.MODEL.toUpperCase().equals("MOBIPRINT")) {
            valorInicial1 = 8;
            valorInicial2 = 12;
            valorInicial3 = 15;
        }else{
            valorInicial1 = 6;
            valorInicial2 = 9;
            valorInicial3 = 8;
        }

        if (AidlUtil.getInstance().isConnect()) {
            valorInicial1 = 6;
            valorInicial2 = 7;
            valorInicial3 = 10;
        }

        int espCan = valorInicial1 - cantidad.length();
        int espDet = valorInicial2 - detalle.length();
        int espCon = valorInicial3 - concepto.length();

        StringBuilder linea = new StringBuilder("" + cantidad);
        String espacio = " ";

        for (int i = 0; i < espCan; i++) {
            linea.append(espacio);
        }

        linea.append(detalle);
        for (int i1 = 0; i1 < espDet; i1++) {
            linea.append(espacio);
        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1 && Build.BRAND.toUpperCase().equals("MOBIWIRE") && Build.MODEL.toUpperCase().equals("MOBIPRINT")) {
            if (espDet == valorInicial2) {
                linea.append("    ");
            }
        }

        linea.append(concepto);
        for (int i2 = 0; i2 < espCon; i2++) {
            linea.append(espacio);
        }

        linea.append(monto);


        return linea.toString();
    }

    private Bitmap getQr(String texto) {
        //String qrInputText = texto;
        LogUtils.i("______QrTExt ", texto);
        Bitmap bitmap = null;
        int width = 330;
        int height = 480;

        LogUtils.i("______QrTExt ", "width " + width + " height " + height);
        int smallerDimension = width;
        smallerDimension = smallerDimension * 3 / 4;

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(texto,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1 && Build.BRAND.toUpperCase().equals("MOBIWIRE") && Build.MODEL.toUpperCase().equals("MOBIPRINT")) {

                bitmap = qrCodeEncoder.encodeAsBitmap();

            }else{

                bitmap = qrCodeEncoder.encodeAsBitmap2();

            }

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
