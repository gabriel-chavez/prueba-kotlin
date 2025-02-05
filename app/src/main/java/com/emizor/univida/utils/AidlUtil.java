package com.emizor.univida.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.emizor.univida.R;
import com.emizor.univida.bean.TableItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;


public class AidlUtil {
    private static final String SERVICE＿PACKAGE = "woyou.aidlservice.jiuiv5";
    private static final String SERVICE＿ACTION = "woyou.aidlservice.jiuiv5.IWoyouService";

    private IWoyouService woyouService;
    private static AidlUtil mAidlUtil = new AidlUtil();
    private Context context;

    private AidlUtil() {
    }

    public static AidlUtil getInstance() {
        return mAidlUtil;
    }

    /**
     * Servicios conectados
     *
     * @param context context
     */
    public void connectPrinterService(Context context) {
        this.context = context.getApplicationContext();
        Intent intent = new Intent();
        intent.setPackage(SERVICE＿PACKAGE);
        intent.setAction(SERVICE＿ACTION);
        context.getApplicationContext().startService(intent);
        context.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    /**
     * Desconectar el servicio
     *
     * @param context context
     */
    public void disconnectPrinterService(Context context) {
        if (woyouService != null) {
            context.getApplicationContext().unbindService(connService);
            woyouService = null;
        }
    }

    public boolean isConnect() {
        return woyouService != null;
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    public ICallback generateCB(final PrinterCallback printerCallback){
        return new ICallback.Stub(){


            @Override
            public void onRunResult(boolean isSuccess) throws RemoteException {

            }

            @Override
            public void onReturnString(String result) throws RemoteException {
                printerCallback.onReturnString(result);
            }

            @Override
            public void onRaiseException(int code, String msg) throws RemoteException {

            }
        };
    }

    /**
     * Establecer densidad de impresión
     */
    private int[] darkness = new int[]{0x0600, 0x0500, 0x0400, 0x0300, 0x0200, 0x0100, 0,
            0xffff, 0xfeff, 0xfdff, 0xfcff, 0xfbff, 0xfaff};

    public void setDarkness(int index) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        int k = darkness[index];
        try {
            woyouService.sendRAWData(ESCUtil.setPrinterDarkness(k), null);
            woyouService.printerSelfChecking(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtenga la información del sistema de la impresora y póngala en la lista
     *
     * @return list
     */
    public List<String> getPrinterInfo(PrinterCallback printerCallback) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return null;
        }

        List<String> info = new ArrayList<>();
        try {
            woyouService.getPrintedLength(generateCB(printerCallback));
            info.add(woyouService.getPrinterSerialNo());
            info.add(woyouService.getPrinterModal());
            info.add(woyouService.getPrinterVersion());
            info.add(printerCallback.getResult());
            info.add("");
            //info.add(woyouService.getServiceVersion());
            PackageManager packageManager = context.getPackageManager();
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(SERVICE＿PACKAGE, 0);
                if(packageInfo != null){
                    info.add(packageInfo.versionName);
                    info.add(packageInfo.versionCode+"");
                }else{
                    info.add("");info.add("");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * Inicializa la impresora
     */
    public void initPrinter() {
        if (woyouService == null) {
            Toast.makeText(context,R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.printerInit(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Imprimir código QR
     */
    public void printQr(String data, int modulesize, int errorlevel) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }


        try {
            woyouService.lineWrap(1, null);
		    woyouService.setAlignment(1, null);
            woyouService.printQRCode(data, modulesize, errorlevel, null);
            woyouService.lineWrap(1, null);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * Imprimir código de barras
     */
    public void printBarCode(String data, int symbology, int height, int width, int textposition) {
        if (woyouService == null) {
            Toast.makeText(context,R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }


        try {
            woyouService.printBarCode(data, symbology, height, width, textposition, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Imprimir texto
     */
    public void printText(String content, float size, boolean isBold, boolean isUnderLine) {
        printText(content, size, isBold, isUnderLine, 0);
    }

    /**
     * Imprimir texto
     */
    public void printText(String content, float size, boolean isBold, boolean isUnderLine, int aligment) {
        if (woyouService == null) {
            Toast.makeText(context,R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (isBold) {
                woyouService.sendRAWData(ESCUtil.boldOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.boldOff(), null);
            }

            if (isUnderLine) {
                woyouService.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.underlineOff(), null);
            }

            woyouService.setAlignment(aligment, null);

            woyouService.printTextWithFont(content, "gh", size, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * Imprimir foto
     */
    public void printBitmap(Bitmap bitmap) {
        // centreado por defecto
        printBitmap(bitmap, 1);
    }

    /**
     * Imprimir foto con alineacion
     */
    public void printBitmap(Bitmap bitmap, int alignment) {
        if (woyouService == null) {
            Toast.makeText(context,R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.setAlignment(alignment, null);
            woyouService.printBitmap(bitmap, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Form impresa
     */
    public void printTable(LinkedList<TableItem> list) {
        if (woyouService == null) {
            Toast.makeText(context,R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            for (TableItem tableItem : list) {
                Log.i("kaltin", "printTable: "+tableItem.getText()[0]+tableItem.getText()[1]+tableItem.getText()[2]);
                woyouService.printColumnsString(tableItem.getText(), tableItem.getWidth(), tableItem.getAlign(), null);
            }
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Corre tres líneas！
     */
    public void print3Line(){
        if (woyouService == null) {
            Toast.makeText(context,R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Corre N líneas！
     */
    public void printNLine(int lines){
        if (woyouService == null) {
            Toast.makeText(context,R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.lineWrap(lines, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void sendRawData(byte[] data) {
        if (woyouService == null) {
            Toast.makeText(context,R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.sendRAWData(data, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
