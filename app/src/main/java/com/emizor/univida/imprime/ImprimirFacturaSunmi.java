package com.emizor.univida.imprime;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import com.emizor.univida.R;
import com.emizor.univida.excepcion.ErrorPapelException;
import com.emizor.univida.excepcion.ImpresoraErrorException;
import com.emizor.univida.excepcion.NoHayPapelException;
import com.emizor.univida.excepcion.VoltageBajoException;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.DatosRcvResp;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.EfectivizarRcvUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.ObtenerRcvRespUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RcvListarVentaRespUnivida;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.soatc.EmiPolizaObtenerResponse;
import com.emizor.univida.modelo.dominio.univida.ventas.Datos;
import com.emizor.univida.modelo.dominio.univida.ventas.Detalle;
import com.emizor.univida.modelo.dominio.univida.ventas.EfectivizarRespUnivida;
import com.emizor.univida.util.LogUtils;
import com.emizor.univida.util.utilqr.Contents;
import com.emizor.univida.util.utilqr.QRCodeEncoder;
import com.emizor.univida.utils.AidlUtil;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.pax.dal.IPrinter;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.dal.exceptions.PrinterDevException;
import com.pax.neptunelite.api.NeptuneLiteUser;

import java.io.BufferedInputStream;
import java.io.File;
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
public class ImprimirFacturaSunmi extends ImprimirFactura{

    private final String TAG = "PRINTFACTURASUNMI";
    private final int TAMANIO_NORMAL_3 = 21;
    private final int TAMANIO_NORMAL_2 = 21;
    private final int TAMANIO_NORMAL = 23;
    private final int TAMANIO_GRANDE = 31;

    protected final String linea = "--------------------------------";

    public ImprimirFacturaSunmi(Context context) {
        super(context);
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

        estado = true;

        Integer valor = 0;

        int cont = 0;

        //un bucle para setear en el buffer de impresion los datso a imprimir
        for (Object objeto : imprimir) {

            try {
                if (objeto instanceof String) {

                    String linea = (String) objeto;
                    switch (valor) {
                        case -4:
                            AidlUtil.getInstance().printText(linea + "\n",TAMANIO_NORMAL_3,false, false, 0);
                            valor = 0;
                            break;
                        case -3:
                            AidlUtil.getInstance().printText(linea + "\n",TAMANIO_NORMAL_3,false, false, 1);
                            valor = 0;
                            break;
                        case -2:
                            AidlUtil.getInstance().printText(linea + "\n",TAMANIO_NORMAL,false, false, 1);
                            valor = 0;
                            break;
                        case -1:
                            AidlUtil.getInstance().printText(linea + "\n",TAMANIO_NORMAL_2,false, false);
                            valor = 0;
                            break;
                        case 0:
                            //impresion por defecto
                            AidlUtil.getInstance().printText(linea + "\n",TAMANIO_NORMAL,false, false);
                            break;
                        case 1:
                            AidlUtil.getInstance().printText(linea,TAMANIO_NORMAL,false, false);

                            valor = 0;

                            break;
                        case 2:
                        case 3:
                            //impresion de una linea por una imagen
                            InputStream bm = context.getResources().openRawResource(R.raw.linea6);
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(bm);
                            Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                            AidlUtil.getInstance().printText(linea,TAMANIO_NORMAL,false, false);
                            valor = 0;
                            break;
                        case 4:
                            AidlUtil.getInstance().sendRawData(new byte[]{0x1D, 0x42, 0x00,0x1B, 0x21, 0x00});
                            AidlUtil.getInstance().printText(linea,TAMANIO_NORMAL,false, false);
                            AidlUtil.getInstance().sendRawData(new byte[]{0x1D, 0x42, 0x00,0x1B, 0x21, 0x00});
                            valor = 0;
                            break;
                        case 5:
                            AidlUtil.getInstance().printText(linea,TAMANIO_GRANDE,false, false);
                            valor = 0;
                            break;
                        case 6:
                            // impresion de una segunda linea
                            InputStream bm2 = context.getResources().openRawResource(R.raw.linea);
                            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(bm2);
                            Bitmap bmp2 = BitmapFactory.decodeStream(bufferedInputStream2);

                            AidlUtil.getInstance().printBitmap(bmp2);
                            valor = 0;
                            break;
                        case 7:
                            //impresion de imagen de pie de pagina
                            InputStream bmpp = context.getResources().openRawResource(R.raw.pie_de_pagina);
                            BufferedInputStream bufferedInputStreampp = new BufferedInputStream(bmpp);
                            Bitmap bmppp = BitmapFactory.decodeStream(bufferedInputStreampp);

                            AidlUtil.getInstance().printBitmap(bmppp);
                            valor = 0;
                            break;
                        case 8:
                            // impresion de imagen de la leyenda generica
                            InputStream bmlg = context.getResources().openRawResource(R.raw.leyenda_generica);
                            BufferedInputStream bufferedInputStreamlg = new BufferedInputStream(bmlg);
                            Bitmap bmplg = BitmapFactory.decodeStream(bufferedInputStreamlg);
                            AidlUtil.getInstance().printBitmap(bmplg);
                            valor = 0;
                            break;
                        case 9:
                            valor = 0;
                            AidlUtil.getInstance().printText(linea,TAMANIO_GRANDE,false, false);
                            break;

                        case 10:
                            int idImagen = Integer.parseInt(linea);

                            InputStream bmid = context.getResources().openRawResource(idImagen);
                            BufferedInputStream bufferedInputStreamid = new BufferedInputStream(bmid);
                            Bitmap bmpid = BitmapFactory.decodeStream(bufferedInputStreamid);
                            AidlUtil.getInstance().printBitmap(bmpid);
                            valor = 0;
                            break;
                        case 11:
                            File archivo = new File(linea);
                            Uri uri = Uri.fromFile(archivo);
                            InputStream inputStream = context.getContentResolver().openInputStream(uri);

                            BufferedInputStream bufferedInputStreamid2 = new BufferedInputStream(inputStream);
                            Bitmap bmpid2 = BitmapFactory.decodeStream(bufferedInputStreamid2);
                            AidlUtil.getInstance().printBitmap(bmpid2);
                            valor = 0;
                            break;
                        case 100:
                            AidlUtil.getInstance().printText(linea + "\n",TAMANIO_NORMAL,false, false);
                            valor = 0;
                            break;
                        case 101:
                            valor =0;
                            AidlUtil.getInstance().printText(linea,TAMANIO_NORMAL,false, false);
                            valor = 0;
                            break;
                        case -101:
                            valor =0;
                            AidlUtil.getInstance().printText(linea + "\n",TAMANIO_NORMAL,true, false,2);
                            valor = 0;
                            break;
                        case 102:
                            AidlUtil.getInstance().printText(linea + "\n",TAMANIO_GRANDE,false, false);
                            valor = 0;
                            break;
                        case -102:
                            AidlUtil.getInstance().printText(linea + "\n",TAMANIO_GRANDE,true, false, 2);
                            valor = 0;
                            break;
                        case 103:
                            AidlUtil.getInstance().printText(linea,TAMANIO_NORMAL,false, false);
                            valor = 0;
                            valor = 0;
                            break;
                        case 104:
                            AidlUtil.getInstance().sendRawData(new byte[]{0x1D, 0x42, 0x00,0x1B, 0x21, 0x00});
                            AidlUtil.getInstance().printText(linea + "\n",TAMANIO_NORMAL,false, false);
                            AidlUtil.getInstance().sendRawData(new byte[]{0x1D, 0x42, 0x00,0x1B, 0x21, 0x00});
                            valor = 0;
                            break;
                        case 110:
                            AidlUtil.getInstance().printText(linea + "\n",TAMANIO_NORMAL,false, false);
                            valor = 0;
                            break;
                        case 111:
                            AidlUtil.getInstance().printText(linea,TAMANIO_NORMAL,false, false);
                            valor = 0;
                            break;
                        case 112:
                            AidlUtil.getInstance().printText(linea + "\n",TAMANIO_GRANDE,false, false);
                            valor = 0;
                            break;
                        case 113:
                            AidlUtil.getInstance().printText(linea + "\n",TAMANIO_GRANDE,false, false);
                            valor = 0;
                            break;
                        case 200:
                            AidlUtil.getInstance().printQr(linea, 4, 3);
                            break;

                    }

                } else if (objeto instanceof Integer) {

                   if (valor == 210) {
//                       iPrinter.spaceSet((byte)0,((Integer) objeto).byteValue());
                       AidlUtil.getInstance().printNLine(1);
                       valor = 0;
                   }else{
                       valor = (Integer) objeto;
                    }

                } else if (objeto instanceof InputStream) {
                    InputStream bmob = (InputStream) objeto;
                    BufferedInputStream bufferedInputStreamob = new BufferedInputStream(bmob);
                    Bitmap bmpob = BitmapFactory.decodeStream(bufferedInputStreamob);
                    AidlUtil.getInstance().printBitmap(bmpob);
                    valor = 0;
                } else if (objeto instanceof Bitmap){

                    Bitmap bmpido = (Bitmap) objeto;
                    AidlUtil.getInstance().printBitmap(bmpido);
                    valor = 0;
                }

                cont++;
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }

        AidlUtil.getInstance().print3Line();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        estado = false;

        if (avisoListener != null){
            avisoListener.terminoDeImprimir();
        }
        notify();

    }

    public void prepararImpresionRcv(User user, EfectivizarRcvUnivida efectivizarRcvUnivida, String secuencial, RcvListarVentaRespUnivida rcvListarVentaRespUnivida) {

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
        pImp.add(-2);
        pImp.add(("REPORTE CIERRE DE VENTAS"));
        pImp.add(3);
        pImp.add(linea);
        //pImp.add(101);
        pImp.add("CÓDIGO RCV          : " + Double.valueOf(secuencial).intValue());
        //pImp.add(101);
        if (dateRcv != null) {
            pImp.add("FECHA               : " + simpleDateFormatr.format(dateRcv));
        }else{
            pImp.add("FECHA               : " + efectivizarRcvUnivida.getVentaFecha());
        }
        pImp.add("IMPORTE             : " + efectivizarRcvUnivida.getRcvImporte());
        pImp.add("CANTIDAD VALIDOS    : " + rcvListarVentaRespUnivida.getDatos().getCantidadValidos());
        pImp.add("CANTIDAD REVERTIDOS : " + rcvListarVentaRespUnivida.getDatos().getCantidadRevertidos());
        pImp.add("CANTIDAD ANULADOS   : " + rcvListarVentaRespUnivida.getDatos().getCantidadAnulados());
        pImp.add("CANTIDAD TOTAL      : " + efectivizarRcvUnivida.getRcvCantidad());
        pImp.add(3);
        pImp.add(linea);
        pImp.add("SUCURSAL : " + user.getDatosUsuario().getSucursalNombre());
        pImp.add("USUARIO  : " + user.getDatosUsuario().getEmpleadoNombreCompleto());
        pImp.add("CARGO    : " + user.getDatosUsuario().getEmpleadoCargo());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        pImp.addAll(formatearLineaCadena2("FECHA IMPRESIÓN : " + simpleDateFormat.format(new Date())));
        pImp.add(3);
        pImp.add(linea);

    }

    public void prepararImpresionRcv(User user, ObtenerRcvRespUnivida obtenerRcvRespUnivida) {

        LogUtils.i(TAG, "PREPARAMOS RCV REPORTE");

        if (pImp != null){
            pImp.clear();
        }

        if (obtenerRcvRespUnivida != null){
            DatosRcvResp datosRcvResp = obtenerRcvRespUnivida.getDatos();

            if (datosRcvResp != null){
                pImp = new Vector<>();

                pImp.add(3);
                pImp.add(linea);
                pImp.add(-2);
                pImp.add(("REPORTE CIERRE DE VENTAS"));
                pImp.add(3);
                pImp.add(linea);
                //pImp.add(101);
                pImp.add("CÓDIGO RCV          : " + datosRcvResp.getRcvSecuencial());
                //pImp.add(101);
                pImp.add("FECHA               : " + datosRcvResp.getRcvFormularioFecha());
                pImp.add("IMPORTE             : " + datosRcvResp.getRcvFormularioImporte());
                pImp.add("CANTIDAD VALIDOS    : " + datosRcvResp.getRcvCantidadValidos());
                pImp.add("CANTIDAD REVERTIDOS : " + datosRcvResp.getRcvCantidadRevertidos());
                pImp.add("CANTIDAD ANULADOS   : " + datosRcvResp.getRcvCantidadAnulados());
                pImp.add("CANTIDAD TOTAL      : " + datosRcvResp.getRcvCantidad());
                pImp.add(3);
                pImp.add(linea);
                pImp.add("SUCURSAL : " + user.getDatosUsuario().getSucursalNombre());
                pImp.add("USUARIO  : " + user.getDatosUsuario().getEmpleadoNombreCompleto());
                pImp.add("CARGO    : " + user.getDatosUsuario().getEmpleadoCargo());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                pImp.addAll(formatearLineaCadena2("FECHA IMPRESIÓN : " + simpleDateFormat.format(new Date())));
                pImp.add(3);
                pImp.add(linea);
            }


        }


    }

    public void prepararImpresionFactura(User user, EfectivizarRespUnivida efectivizarRespUnivida) {
        prepararImpresionFacturaResumen(user, efectivizarRespUnivida);
    }
    public void prepararImpresionFacturaCompleto(User user, EfectivizarRespUnivida efectivizarRespUnivida) {
        prepararImpresionFactura2022(user, efectivizarRespUnivida);
    }
    // 2022
    public void prepararImpresionFactura2022(User user, EfectivizarRespUnivida efectivizarRespUnivida) {

        LogUtils.i(TAG, "PREPARAMOS FACTURA");

        if (pImp != null){
            pImp.clear();
        }

        pImp = new Vector<>();
        Vector<Object> vectoDoc = new Vector<>();

        Datos datos = efectivizarRespUnivida.getDatos();

        List<String> strLey = formatearLineaCadena4(datos.getFacturaMaestro().getLeyenda());
        List<String> strTipoEmisionLeyenda = formatearLineaCadena4(datos.getFacturaMaestro().getTipoEmisionLeyenda());
        List<String> vector11 = formatearLineaCadena2(datos.getFacturaMaestro().getDireccionSucursal() + " Teléfono: " + datos.getFacturaMaestro().getTelefonoSucursal());

//        BigDecimal bigDecimalAmount = new BigDecimal(datos.getFacturaMaestro().getImporteNumeral());
//
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
            nnotes.add((detalle.getCatalogoCodigo() + " - " + detalle.getUnidadMedida() + "/ " + detalle.getLineaDetalle()));
//            String lineaDatos = construirFila(decimalFormat.format(detalle.getCantidad()), "", decimalFormat.format(detalle.getImporteUnitario()), decimalFormat.format(detalle.getImporteTotal()));
            String lineaDatos = (decimalFormat.format(detalle.getCantidad()) + "x " + decimalFormat.format(detalle.getImporteUnitario()) + " - " + decimalFormat.format(detalle.getDescuento()) + "   " + decimalFormat.format(detalle.getImporteTotal()));
//            String lineaDatos = construirFila(decimalFormat.format(detalle.getCantidad()), "", (detalle.getImporteUnitario()), (detalle.getImporteTotal()));
            nnotes.add(lineaDatos);

        }

        LogUtils.i("IMPRESION ", "TRY");

        LogUtils.i("IMPRESION ", "logo");

        vectoDoc.add(10);
        vectoDoc.add(String.valueOf(R.raw.logop));

        vectoDoc.add(-2);
        vectoDoc.add(datos.getFacturaMaestro().getTitulo());
//        LogUtils.i("IMPRIMRI_FACTURA_SUNMI", new Gson().toJson(datos));
//        vectoDoc.add(-2);
        if(datos.getFacturaMaestro().getSubtitulo() != null && (! datos.getFacturaMaestro().getSubtitulo().isEmpty())){
            List<String> subtituloLista = formatearLineaCadena4(datos.getFacturaMaestro().getSubtitulo());
            for (String strSubtitulo : subtituloLista){
                vectoDoc.add(-2);
                vectoDoc.add(strSubtitulo);
            }
        }

        vectoDoc.add(-2);
        vectoDoc.add("CASA MATRIZ");

        List<String> vector111 = formatearLineaCadena2(datos.getFacturaMaestro().getDireccionEmpresa() + " Teléfono: " + datos.getFacturaMaestro().getTelefonosEmpresa());
        for (String strDire2 : vector111){
            vectoDoc.add(-2);
            vectoDoc.add(strDire2);
        }
        vectoDoc.add(-2);
        vectoDoc.add((datos.getFacturaMaestro().getLugar()));
        vectoDoc.add(" ");

        if (datos.getFacturaMaestro().getNumeroSucursal() != 0) {
            vectoDoc.add(-2);
            vectoDoc.add("SUCURSAL: " + datos.getFacturaMaestro().getNumeroSucursal());

            for (String strDire : vector11){
                vectoDoc.add(-2);
                vectoDoc.add(strDire);

            }
            vectoDoc.add(-2);
            vectoDoc.add(datos.getFacturaMaestro().getMunicipioDepartamento());
        }
        vectoDoc.add(-2);
        vectoDoc.add("PDV: " + datos.getFacturaMaestro().getPuntoVentaCodigo());

        vectoDoc.add(3);
        vectoDoc.add(linea);

        vectoDoc.add(-2);
        vectoDoc.add("NIT");
        vectoDoc.add(-2);
        vectoDoc.add(datos.getFacturaMaestro().getNitEmpresa());
        vectoDoc.add(-2);
        vectoDoc.add("FACTURA No.");
        vectoDoc.add(-2);
        vectoDoc.add(datos.getFacturaMaestro().getNumeroFactura().toString());
        vectoDoc.add(-2);
        vectoDoc.add("CÓD. AUTORIZACIÓN");
//        vectoDoc.add(110);
        vectoDoc.add(-2);
        vectoDoc.add(datos.getFacturaMaestro().getCodigoControl());

        vectoDoc.add(3);
        vectoDoc.add(linea);

//        vectoDoc.add(datos.getFacturaMaestro().getActividadEconomica());
//        List<String> activitys = (datos.getFacturaMaestro().getActividadEconomica(), 0);
//
//        for (String strActividad : activitys){
//            vectoDoc.add((strActividad));
//        }

        String fecha1 = datos.getFacturaMaestro().getFechaEmision();
        fecha1 = fecha1.replace('-', '/');


        vectoDoc.add("Fecha       : " + fecha1);
        vectoDoc.add("Código de Cliente: " + datos.getFacturaMaestro().getCodigoCliente());
        vectoDoc.addAll(formatearLineaCadena2("Nombre/Razón Social: " + datos.getFacturaMaestro().getRazonSocialClient()));
        vectoDoc.add("NIT/CI/CEX   : " + datos.getFacturaMaestro().getNitCiCliente());

        vectoDoc.add(3);
        vectoDoc.add(linea);

//        vectoDoc.add(construirFila("Cant.", " ", "Precio", "Sub total"));
        vectoDoc.add(-2);
        vectoDoc.add("DETALLE");
//        vectoDoc.add(3);
//        vectoDoc.add(linea);

        for (int i = 0; i < nnotes.size(); i += 2) {

            String nnotes2 = (String) nnotes.get(i);

            vectoDoc.add(nnotes2);

            vectoDoc.add(String.valueOf(nnotes.get(i + 1)));

        }

        //vectoDoc.add(getApplicationContext().getResources().openRawResource(R.raw.linea));
        vectoDoc.add(3);
        vectoDoc.add(linea);
        // colocamos valor unao para imprimir cadenas al revez y con alineacion a la derecha
        vectoDoc.add(-101);
        vectoDoc.add(sbSubTotal.toString());
        vectoDoc.add(-101);
        vectoDoc.add(sbDescuento.toString());
        vectoDoc.add(-101);
        vectoDoc.add(sbTotal.toString());
        vectoDoc.add(-101);
        vectoDoc.add(sbMontoPagar.toString());
//        vectoDoc.add(-101);
//        vectoDoc.add(formatearLineaCadena2(sbImporteBaseCreditoFiscal.toString()));
        if (datos.getFacturaMaestro().getImporteBaseCreditoFiscal() != null && datos.getFacturaMaestro().getImporteBaseCreditoFiscal() > 0) {
            List<String> listaImporte = formatearLineaCadena2(sbImporteBaseCreditoFiscal.toString());
            for (String importeList : listaImporte) {
                vectoDoc.add(-101);
                vectoDoc.add(importeList);
            }
        }

//        vectoDoc.add("MONTO A PAGAR: Bs " + decimalFormat.format(bigDecimalAmount));
        for (String litre: literal) {
            vectoDoc.add(litre);
        }
        vectoDoc.add(3);
        vectoDoc.add(linea);

//        vectoDoc.add(101);
//        vectoDoc.add("CÓDIGO DE CONTROL:");
//        vectoDoc.add(110);
//        vectoDoc.add(datos.getFacturaMaestro().getCodigoControl());
//        vectoDoc.addAll(formatearLineaCadena2("FECHA LÍMITE DE EMISIÓN: " + datos.getFacturaMaestro().getFechaLimiteEmision()));


        vectoDoc.add("USUARIO: " + user.getDatosUsuario().getEmpleadoNombreCompleto());
        vectoDoc.add(" ");
        List<String> listaLeyenda = formatearLineaCadena4("\"ESTA FACTURA CONTRIBUYE AL DESARROLLO DEL PAIS, EL USO ILÍCITO DE ESTA SERÁ SANCIONADO PENALMENTE DE ACUERDO A LEY\"");
//        vectoDoc.add(listaLeyenda);
        for (String strLeyenda : listaLeyenda){
            vectoDoc.add(-3);
            vectoDoc.add((strLeyenda));
        }
        vectoDoc.add(" ");
        for (String strLe : strLey) {
            vectoDoc.add(-3);
            vectoDoc.add((strLe));
        }
        vectoDoc.add(" ");
        for (String strTipoEmision : strTipoEmisionLeyenda) {
            vectoDoc.add(-3);
            vectoDoc.add((strTipoEmision));
        }

        String informacionQr = datos.getFacturaMaestro().getCodigoQr();

        vectoDoc.add(200);
        vectoDoc.add(getQr(informacionQr));

        vectoDoc.add(" ");
        String corteAqui = ">>>------- CORTE AQUI -------<<<";
        vectoDoc.add(corteAqui);
        vectoDoc.add(" ");
        vectoDoc.add(10);
        vectoDoc.add(String.valueOf(R.raw.logop));

        if (datos.getSoatQrContenido() != null) {
            vectoDoc.add(200);
            vectoDoc.add((datos.getSoatQrContenido().get(0)));
        }

        vectoDoc.add(-2);
        vectoDoc.add((("COMPROBANTE SOAT " + datos.getSoatGestionFk())));

        //SOAT
        vectoDoc.add("Fecha emisión    :" + datos.getFacturaMaestro().getFechaEmision());
        vectoDoc.add("Nº de comprobante:" + decimalFormatLleno.format(datos.getSoatNumeroComprobante()));
        vectoDoc.add("Nº de factura    :" + decimalFormatLleno.format(datos.getFacturaMaestro().getNumeroFactura()));
        vectoDoc.add("Nº de roseta     :" + decimalFormatLleno.format(datos.getSoatRosetaNumero()));
        //SOAT
        vectoDoc.add(210);
        vectoDoc.add(5);
        //COMPRADOR
        List<String> listComprador=formatearLineaCadena3("Comprador        :" +datos.getPropTomador(), 10);;
        vectoDoc.addAll(listComprador);
//        vectoDoc.add("Comprador        :" +datos.getPropTomador());
        vectoDoc.add("CI               : " + datos.getPropCi());
        vectoDoc.add("NIT              : " + datos.getPropNit());
        List<String> listaDireccion = formatearLineaCadena3("Dirección        : " + datos.getPropDireccion(), 10);
        vectoDoc.addAll(listaDireccion);
//        vectoDoc.addAll(formatearLineaCadena3("Dirección        : " + datos.getPropDireccion(), 10));
        vectoDoc.add("Teléfono         : " + datos.getPropTelefono());
        vectoDoc.add("Celular          : " + datos.getPropCelular());
        vectoDoc.add(210);
        vectoDoc.add(5);
        //VEHICULO
        vectoDoc.add("Placa            :" +datos.getVehiculoPlaca());
        vectoDoc.add("Marca            :" + datos.getVehiculoMarca());
        vectoDoc.add("Color            :" + datos.getVehiculoColor());
        List<String> listTipoVehiculos=formatearLineaCadena3("Tipo de vehículo :" + datos.getSoatVehiculoTipoDescripcion(), 10);
        vectoDoc.addAll(listTipoVehiculos);
//        vectoDoc.add("Tipo de vehículo :" + datos.getSoatVehiculoTipoDescripcion());
        vectoDoc.add("Modelo           :" + datos.getVehiculoModelo());
        vectoDoc.add("Año              :" + datos.getVehiculoAnio());
        vectoDoc.add("Cilindrada       :" + (datos.getVehiculoCilindrada() != null ? datos.getVehiculoCilindrada(): ""));
        vectoDoc.add("Nro de Motor     :" + datos.getVehiculoMotor());
        vectoDoc.add("Cap. de carga    :" + (datos.getVehiculoCapacidadCarga() != null ? datos.getVehiculoCapacidadCarga(): ""));
        vectoDoc.add("Plaza circulación:" + datos.getSoatDepartamentoPcDescription());
        vectoDoc.add("Nro Chasis       :" + datos.getVehiculoChasis());
        vectoDoc.add("Tipo de uso      :" + datos.getSoatVehiculoUsoDescription());
        //VEHICULO

        vectoDoc.add(3);
        vectoDoc.add(linea);
        // MENSAJE
        if (datos.getSoatMensaje() != null) {
//            vectoDoc.addAll(formatearLineaCadena2(String.valueOf(datos.getSoatMensaje())));
            List<String> listaMensaje = formatearLineaCadena4(String.valueOf(datos.getSoatMensaje()));
            for (String strMensaje : listaMensaje){
                vectoDoc.add(-3);
                vectoDoc.add((strMensaje));
            }
            vectoDoc.add(3);
            vectoDoc.add(linea);
        }
        //MENSAJE
        vectoDoc.add(" ");
        vectoDoc.add(-3);
        vectoDoc.add(("VIGENCIA DE LA COBERTURA SOAT " + datos.getSoatGestionFk()));
        vectoDoc.add(-3);
        vectoDoc.add(("Del " + datos.getSoatFechaCoberturaInicio() + " hasta el " + datos.getSoatFechaCoberturaFin()));
        vectoDoc.add(" ");
        vectoDoc.add(-3);
        vectoDoc.add(("VIGENCIA COBERTURA"));

        String fechaIniSoat = "01/01/" + datos.getSoatGestionFk();
        if (datos.getSoatGestionFk() == 2021){
            fechaIniSoat = "01/05/" + datos.getSoatGestionFk();
        };

        List<String> listaVigen = formatearLineaCadena4("El SOAT " + datos.getSoatGestionFk() + " adquirido a partir del " + fechaIniSoat + " entra en vigor desde las 00:00 horas del dia siguiente de la adquisicion.");

//        vectoDoc.add(listaVigen);
        for (String strVigen : listaVigen){
            vectoDoc.add(-3);
            vectoDoc.add((strVigen));
        }
        vectoDoc.add(3);
        vectoDoc.add(linea);
        List<String> cadenaInfor = formatearLineaCadena4("Para actualizar la información del presente formulario apersonese a nuestras oficinas o ingresar a la página web www.univida.bo");
//        vectoDoc.add(cadenaInfor);
        for (String strInfor : cadenaInfor){
            vectoDoc.add(-3);
            vectoDoc.add((strInfor));
        }

        vectoDoc.add(-3);
        vectoDoc.add("Línea gratuita: 800-10-8444");
        vectoDoc.add(-3);
        vectoDoc.add("Central teléfonica: " + datos.getFacturaMaestro().getTelefonosEmpresa());
        vectoDoc.add(-3);
        vectoDoc.add(("www.univida.bo"));
        vectoDoc.add(3);
        vectoDoc.add(linea);

        if (datos.getSoatQrContenido().size() > 1) {
            // extra 2022
            vectoDoc.add(" ");
            vectoDoc.add(corteAqui);
            vectoDoc.add(" ");
            vectoDoc.add(10);
            vectoDoc.add(String.valueOf(R.raw.logop));

            vectoDoc.add(-3);
            vectoDoc.add(("Para descargar tu Roseta Digital, escanea desde tu dispositivo móvil el siguiente código QR"));
            if (datos.getSoatQrContenido() != null) {
                vectoDoc.add(200);
                vectoDoc.add((datos.getSoatQrContenido().get(1)));
            }

            vectoDoc.add(-3);
            vectoDoc.add(("Aclaraciones:"));

            vectoDoc.add(("-La Roseta Dígital puede ser descargada en cualquier momento, sin afectar la vigencia de la cobertura del SOAT " + datos.getSoatGestionFk()));
            vectoDoc.add(("-No es necesario que la Roseta Digital sea impresa ni adherida al motorizado."));

        }

        pImp.addAll(vectoDoc);

    }
    //2022
    public void prepararImpresionFacturaResumen(User user, EfectivizarRespUnivida efectivizarRespUnivida) {

        LogUtils.i(TAG, "PREPARAMOS FACTURA");

        if (pImp != null){
            pImp.clear();
        }

        pImp = new Vector<>();
        Vector<Object> vectoDoc = new Vector<>();

        Datos datos = efectivizarRespUnivida.getDatos();

        List<String> strLey = formatearLineaCadena4(datos.getFacturaMaestro().getLeyenda());
        List<String> strTipoEmisionLeyenda = formatearLineaCadena4(datos.getFacturaMaestro().getTipoEmisionLeyenda());
        List<String> vector11 = formatearLineaCadena2(datos.getFacturaMaestro().getDireccionSucursal() + " Teléfono: " + datos.getFacturaMaestro().getTelefonoSucursal());

//        BigDecimal bigDecimalAmount = new BigDecimal(datos.getFacturaMaestro().getImporteNumeral());
//
//        bigDecimalAmount = bigDecimalAmount.setScale(2, RoundingMode.HALF_EVEN);
        StringBuilder sbSubTotal = new StringBuilder("Sub Total Bs" + " " + datos.getFacturaMaestro().getImporteSubtotalStr());
        StringBuilder sbDescuento = new StringBuilder("Descuento Bs" + " " + datos.getFacturaMaestro().getImporteDescuentoStr());
        StringBuilder sbTotal = new StringBuilder("Total Bs" + " " + datos.getFacturaMaestro().getImporteNumeralStr());
        //
        StringBuilder sbImporteBaseCreditoFiscal = new StringBuilder("Importe base crédito fiscal Bs" + " " + datos.getFacturaMaestro().getImporteBaseCreditoFiscalStr());


        Vector<Object> nnotes = new Vector<>();

        List<String> literal = formatearLineaCadena2("SON: " + datos.getFacturaMaestro().getImporteLiteral());

        List<Detalle> detalles = datos.getFacturaMaestro().getDetalle();

        for (Detalle detalle : detalles){
            nnotes.add( detalle.getLineaDetalle());
//            String lineaDatos = construirFila(decimalFormat.format(detalle.getCantidad()), "", decimalFormat.format(detalle.getImporteUnitario()), decimalFormat.format(detalle.getImporteTotal()));
            String lineaDatos = (decimalFormat.format(detalle.getCantidad()) + "x " + decimalFormat.format(detalle.getImporteUnitario()) + " - " + decimalFormat.format(detalle.getDescuento()) + "   " + decimalFormat.format(detalle.getImporteTotal()));
//            String lineaDatos = construirFila(decimalFormat.format(detalle.getCantidad()), "", (detalle.getImporteUnitario()), (detalle.getImporteTotal()));
            nnotes.add(lineaDatos);

        }

        LogUtils.i("IMPRESION ", "TRY");

        LogUtils.i("IMPRESION ", "logo");

        vectoDoc.add(10);
        vectoDoc.add(String.valueOf(R.raw.logop));

//        vectoDoc.add(-2);
//        vectoDoc.add(datos.getFacturaMaestro().getTitulo());
//        LogUtils.i("IMPRIMRI_FACTURA_SUNMI", new Gson().toJson(datos));
//        vectoDoc.add(-2);
//        if(datos.getFacturaMaestro().getSubtitulo() != null && (! datos.getFacturaMaestro().getSubtitulo().isEmpty())){
//            List<String> subtituloLista = formatearLineaCadena4(datos.getFacturaMaestro().getSubtitulo());
//            for (String strSubtitulo : subtituloLista){
//                vectoDoc.add(-2);
//                vectoDoc.add(strSubtitulo);
//            }
//        }
        if (datos.getFacturaMaestro().getNumeroSucursal() == 0) {
            vectoDoc.add(-2);
            vectoDoc.add("CASA MATRIZ");

            List<String> vector111 = formatearLineaCadena2(datos.getFacturaMaestro().getDireccionEmpresa() + " Teléfono: " + datos.getFacturaMaestro().getTelefonosEmpresa());
            for (String strDire2 : vector111) {
                vectoDoc.add(-2);
                vectoDoc.add(strDire2);
            }
            vectoDoc.add(-2);
            vectoDoc.add((datos.getFacturaMaestro().getLugar()));
            vectoDoc.add(" ");
        }
        if (datos.getFacturaMaestro().getNumeroSucursal() != 0) {
            vectoDoc.add(-2);
            //vectoDoc.add("SUCURSAL: " + datos.getFacturaMaestro().getNumeroSucursal());
            vectoDoc.add("SUCURSAL: " + datos.getFacturaMaestro().getDireccionSucursal());

            vectoDoc.add(-2);
            vectoDoc.add(datos.getFacturaMaestro().getMunicipioDepartamento());
        }


        vectoDoc.add(linea);

        String fecha1 = datos.getFacturaMaestro().getFechaEmision();
        fecha1 = fecha1.replace('-', '/');
//        vectoDoc.add("Nro. Factura       : " + datos.getFacturaMaestro().getNumeroFactura().toString());
//        vectoDoc.add("Fecha       : " + fecha1);
//        //vectoDoc.add("Código de Cliente: " + datos.getFacturaMaestro().getCodigoCliente());
//        vectoDoc.addAll(formatearLineaCadena2("Nombre/Razón Social: " + datos.getFacturaMaestro().getRazonSocialClient()));
//        vectoDoc.add("NIT/CI/CEX   : " + datos.getFacturaMaestro().getNitCiCliente());

        vectoDoc.add("Nro. Factura       :" + datos.getFacturaMaestro().getNumeroFactura().toString());
        vectoDoc.add("Fecha              :" + fecha1);
        vectoDoc.add("Nombre/Razón Social:" + datos.getFacturaMaestro().getRazonSocialClient());
        vectoDoc.add("NIT/CI/CEX         :" + datos.getFacturaMaestro().getNitCiCliente());

        vectoDoc.add(3);
        vectoDoc.add(linea);

//        vectoDoc.add(construirFila("Cant.", " ", "Precio", "Sub total"));
        vectoDoc.add(-2);
        vectoDoc.add("DETALLE");
//        vectoDoc.add(3);
//        vectoDoc.add(linea);

        for (int i = 0; i < nnotes.size(); i += 2) {

            String nnotes2 = (String) nnotes.get(i);

            vectoDoc.add(nnotes2);

            vectoDoc.add(String.valueOf(nnotes.get(i + 1)));

        }

        //vectoDoc.add(getApplicationContext().getResources().openRawResource(R.raw.linea));
        vectoDoc.add(3);
        vectoDoc.add(linea);
        // colocamos valor unao para imprimir cadenas al revez y con alineacion a la derecha
        vectoDoc.add(-101);
        vectoDoc.add(sbSubTotal.toString());
        vectoDoc.add(-101);
        vectoDoc.add(sbDescuento.toString());
        vectoDoc.add(-101);
        vectoDoc.add(sbTotal.toString());
      //  vectoDoc.add(-101);
       // vectoDoc.add(sbMontoPagar.toString());
//        vectoDoc.add(-101);
//        vectoDoc.add(formatearLineaCadena2(sbImporteBaseCreditoFiscal.toString()));
        if (datos.getFacturaMaestro().getImporteBaseCreditoFiscal() != null && datos.getFacturaMaestro().getImporteBaseCreditoFiscal() > 0) {
            List<String> listaImporte = formatearLineaCadena2(sbImporteBaseCreditoFiscal.toString());
            for (String importeList : listaImporte) {
                vectoDoc.add(-101);
                vectoDoc.add(importeList);
            }
        }

//        vectoDoc.add("MONTO A PAGAR: Bs " + decimalFormat.format(bigDecimalAmount));
        for (String litre: literal) {
            vectoDoc.add(litre);
        }
        vectoDoc.add(3);
        vectoDoc.add(linea);

        String informacionQr = datos.getFacturaMaestro().getCodigoQr();

        vectoDoc.add(200);
        vectoDoc.add(getQr(informacionQr));

        vectoDoc.add(-2);

        List<String> cadenaInfor1 = formatearLineaCadena4("Para la impresión de la factura ingresar a la página web: www.univida.bo/facturacion/");
        for (String strInfor1 : cadenaInfor1){
            vectoDoc.add(-3);
            vectoDoc.add((strInfor1));
        }

        vectoDoc.add(" ");
        String corteAqui = ">>>------- CORTE AQUI -------<<<";
        vectoDoc.add(corteAqui);
        vectoDoc.add(" ");
        vectoDoc.add(10);
        vectoDoc.add(String.valueOf(R.raw.logop));

        vectoDoc.add(-2);
        vectoDoc.add((("COMPROBANTE SOAT " + datos.getSoatGestionFk())));

        List<String> cadenaComprobante = formatearLineaCadena4("Para descargar tu comprobante SOAT "+datos.getSoatGestionFk()+", escanea desde tu dispositivo móvil el siguiente código QR");
        for (String strInfor2 : cadenaComprobante){
            vectoDoc.add(-3);
            vectoDoc.add((strInfor2));
        }

        if (datos.getSoatQrContenido() != null) {
            vectoDoc.add(200);
            vectoDoc.add((datos.getSoatQrContenido().get(0)));
        }

        vectoDoc.add(-3);
        vectoDoc.add(linea);

        //SOAT
        vectoDoc.add("Fecha emisión    :" + datos.getFacturaMaestro().getFechaEmision());
        vectoDoc.add("Nº de comprobante:" + decimalFormatLleno.format(datos.getSoatNumeroComprobante()));
        vectoDoc.add("Nº de factura    :" + decimalFormatLleno.format(datos.getFacturaMaestro().getNumeroFactura()));
        vectoDoc.add("Nº de roseta     :" + decimalFormatLleno.format(datos.getSoatRosetaNumero()));

//
        vectoDoc.add(3);
        vectoDoc.add(linea);
        // MENSAJE
//        if (datos.getSoatMensaje() != null) {
////            vectoDoc.addAll(formatearLineaCadena2(String.valueOf(datos.getSoatMensaje())));
//            List<String> listaMensaje = formatearLineaCadena4(String.valueOf(datos.getSoatMensaje()));
//            for (String strMensaje : listaMensaje){
//                vectoDoc.add(-3);
//                vectoDoc.add((strMensaje));
//            }
//            vectoDoc.add(3);
//            vectoDoc.add(linea);
//        }
        //MENSAJE
        vectoDoc.add(-2);
//        vectoDoc.add("DETALLE");
//        vectoDoc.add(3);
//        vectoDoc.add(linea);


        String nnotes2 = "DETALLE: " + (String) nnotes.get(0);
        vectoDoc.add(nnotes2);

        vectoDoc.add(" ");
        vectoDoc.add(-3);
        vectoDoc.add(("VIGENCIA DE LA COBERTURA SOAT " + datos.getSoatGestionFk()));
        vectoDoc.add(-3);
        vectoDoc.add(("Del " + datos.getSoatFechaCoberturaInicio() + " hasta el " + datos.getSoatFechaCoberturaFin()));
        vectoDoc.add(" ");
        vectoDoc.add(-3);
//        vectoDoc.add(("VIGENCIA COBERTURA"));
//
//        String fechaIniSoat = "01/01/" + datos.getSoatGestionFk();
//        if (datos.getSoatGestionFk() == 2021){
//            fechaIniSoat = "01/05/" + datos.getSoatGestionFk();
//        };
//
//        List<String> listaVigen = formatearLineaCadena4("El SOAT " + datos.getSoatGestionFk() + " adquirido a partir del " + fechaIniSoat + " entra en vigor desde las 00:00 horas del dia siguiente de la adquisicion.");

//        vectoDoc.add(listaVigen);
//        for (String strVigen : listaVigen){
//            vectoDoc.add(-3);
//            vectoDoc.add((strVigen));
//        }
        vectoDoc.add(-3);
        vectoDoc.add(linea);
        List<String> cadenaInfor = formatearLineaCadena4("Para actualizar la información apersonarse a nuestras oficinas o ingresar a la página web: www.univida.bo");
//        vectoDoc.add(cadenaInfor);
        for (String strInfor : cadenaInfor){
            vectoDoc.add(-3);
            vectoDoc.add((strInfor));
        }



        if (datos.getSoatQrContenido().size() > 1) {
            // extra 2022
            vectoDoc.add(" ");
            vectoDoc.add(corteAqui);
            vectoDoc.add(" ");
            vectoDoc.add(10);
            vectoDoc.add(String.valueOf(R.raw.logop));

            vectoDoc.add(-3);
            vectoDoc.add(("Para descargar tu Roseta Digital, escanea desde tu dispositivo móvil el siguiente código QR"));
            if (datos.getSoatQrContenido() != null) {
                vectoDoc.add(200);
                vectoDoc.add((datos.getSoatQrContenido().get(1)));
            }

            vectoDoc.add(-3);
            vectoDoc.add(("Aclaraciones:"));

            List<String> cadenaInfor4 = formatearLineaCadena4("-La Roseta Dígital puede ser descargada en cualquier momento, sin afectar la vigencia de la cobertura del SOAT " + datos.getSoatGestionFk());
            for (String strInfor2 : cadenaInfor4){
                vectoDoc.add(-3);
                vectoDoc.add((strInfor2));
            }
            List<String> cadenaInfor5 = formatearLineaCadena4("-No es necesario que la Roseta Digital sea impresa ni adherida al motorizado.");
            for (String strInfor2 : cadenaInfor5){
                vectoDoc.add(-3);
                vectoDoc.add((strInfor2));
            }

        }

        pImp.addAll(vectoDoc);

    }
    public void procesarColillaVentaSoat(User user, EfectivizarRespUnivida efectivizarRespUnivida, Date fecha){
        if (pImp != null){
            pImp.clear();
        }
        Datos datos = efectivizarRespUnivida.getDatos();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


        pImp = new Vector<>();


        Vector<Object> vectoColilla = new Vector<>();
        vectoColilla.add("PLACA                :" +datos.getVehiculoPlaca());
        vectoColilla.add("ROSETA               :" +datos.getSoatRosetaNumero());
        vectoColilla.add("SOAT No. COMPROBANTE :" + datos.getSoatNumeroComprobante());
        vectoColilla.add("Fecha de emisión     :" + datos.getFacturaMaestro().getFechaEmision());
        vectoColilla.add("NIT                  :" + datos.getFacturaMaestro().getNitEmpresa());
        vectoColilla.add("No. AUTORIZACIÓN     :" + datos.getFacturaMaestro().getNumeroAutorizacion());
        vectoColilla.add("No. FACTURA          :" + decimalFormatLleno. format(datos.getFacturaMaestro().getNumeroFactura()));
        vectoColilla.add("NIT/CI               :" + datos.getFacturaMaestro().getNitCiCliente());
        vectoColilla.addAll(formatearLineaCadena2("Nombre/Razón Social  :" + datos.getFacturaMaestro().getRazonSocialClient()));
        vectoColilla.add("Monto Total Bs       :" + datos.getFacturaMaestro().getImporteNumeral());

        vectoColilla.addAll(formatearLineaCadena2("FECHA/HORA Impresión :" + simpleDateFormat.format(fecha)));
        vectoColilla.add("CÓDIGO               :" + user.getDatosUsuario().getEmpleadoSecuencial());
        vectoColilla.addAll(formatearLineaCadena2("USUARIO              :" + user.getDatosUsuario().getEmpleadoNombreCompleto()));
        vectoColilla.addAll(formatearLineaCadena2("CARGO                :" + user.getDatosUsuario().getEmpleadoCargo()));

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

    public static String cleanString(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }

    public List<String> formatearLineaCadena2(String cadena) {
        List<String> resp = new ArrayList<>();
        int tamanio_Total = 0;


        if (sonMayusculas(cadena)) {
            tamanio_Total = 32;
        } else if (sonMinisculas(cadena)) {
            tamanio_Total = 32;
        } else {
            tamanio_Total = 32;
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

    public List<String> formatearLineaCadena3(String cadena, int tipo) {
        List<String> resp = new ArrayList<>();
        int tamanio_Total = 32;

        String cadena2 = cadena + "";

        String vacio = cleanString(cadena2);

        if (! cadena.equals(vacio)){
            tamanio_Total = 31;
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

    public List<String> formatearLineaCadena4(String cadena) {
        List<String> resp = new ArrayList<>();
        int tamanio_Total = 0;


        if (sonMayusculas(cadena)) {
            tamanio_Total = 34;
        } else if (sonMinisculas(cadena)) {
            tamanio_Total = 34;
        } else {
            tamanio_Total = 34;
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

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1 && Build.BRAND.toUpperCase().equals("MOBIWIRE") && Build.MODEL.toUpperCase().equals("MOBIPRINT")) {
            if (sonMayusculas(cadena)) {
                tamanio_Total = 35;
            } else if (sonMinisculas(cadena)) {
                tamanio_Total = 40;
            } else {
                tamanio_Total = 40;
            }
        }else{
            if (sonMayusculas(cadena)) {
                tamanio_Total = 8;
            } else if (sonMinisculas(cadena)) {
                tamanio_Total = 15;
            } else {
                tamanio_Total = 10;
            }
        }



        String linea3 = cadena;

        int tamdos = 0;

        tamdos = (tamanio_Total / 2) - (linea3.length() / 2);

        int divisor = cadena.length() / 5;

        switch (divisor) {
            case 0:
                //LogUtils.i("case 0"," se adiciona 15 a " + tamdos);
                tamdos += 13;
                break;
            case 1:
                //LogUtils.i("case 1"," se adiciona 12 a " + tamdos);
                tamdos += 12;
                break;
            case 2:
                //LogUtils.i("case 2"," se adiciona 9 a " + tamdos);
                tamdos += 9;
                break;
            case 3:
                //LogUtils.i("case 3"," se adiciona 6 a " + tamdos);
                tamdos += 6;
                break;
            case 4:
                //LogUtils.i("case 4"," se adiciona 3 a " + tamdos);
                tamdos += 3;
                break;
            case 5:
                //LogUtils.i("case 5"," se adiciona 0 a " + tamdos);
                tamdos += 2;
                break;
            case 6:
                //LogUtils.i("case 6"," se adiciona 0 a " + tamdos);
                if (!sonMayusculas(cadena)) {
                    tamdos += 5;
                }
                break;
            case 7:
                //LogUtils.i("case 7"," se adiciona 0 a " + tamdos);
                if (!sonMayusculas(cadena)) {
                    tamdos += 4;
                }
                break;
            case 8:
                //LogUtils.i("case 8"," se adiciona 0 a " + tamdos);
                tamdos += 3;
                break;
            case 9:

                break;
        }

        //LogUtils.i("adEspCad", " tamanio a adicionar de " + tamdos + " a cadena " + linea3);
        for (int g = 0; g < tamdos; g++) {
            linea3 = " " + linea3;
        }
        //LogUtils.i("adic", " cadena con espacios |" + linea3);

        return linea3;
    }

    private String construirFila(String cantidad, String detalle, String concepto, String monto) {

        int valorInicial1 = 0, valorInicial2 = 0, valorInicial3 = 0;

        valorInicial1 = 6;
        valorInicial2 = 7;
        valorInicial3 = 10;

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

//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1 && Build.BRAND.toUpperCase().equals("MOBIWIRE") && Build.MODEL.toUpperCase().equals("MOBIPRINT")) {
//            if (espDet == valorInicial2) {
//                linea.append("    ");
//            }
//        }

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
    public void prepararImpresionFacturaSoatc(User user, EmiPolizaObtenerResponse efectivizarRespUnivida) {

    }
    public void procesarColillaVentaSoatc(User user, EmiPolizaObtenerResponse efectivizarRespUnivida, Date fecha){

    }
}
