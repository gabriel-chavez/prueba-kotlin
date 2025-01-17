package com.emizor.univida.dialogo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.emizor.univida.R;
import com.emizor.univida.util.LogUtils;

/**
 * Created by root on 10/03/17.
 */
public class DialogoEmizor extends DialogFragment implements View.OnClickListener{

    private NotificaDialogEmizorListener mListener;
    private int tipoDialogo;
    private View vista;
    private StringBuilder[] stringBuilders;

    private String mensaje;
    private String titulo = "MENSAJE";

    private EditText etMotivo;

    public DialogoEmizor(){
        super();
        mensaje = null;
    }

    public void setTipoDialogo(int tipoDialogo){
        this.tipoDialogo = tipoDialogo;

        switch (tipoDialogo){
            case 1:

                break;
            case 2:
                stringBuilders = new StringBuilder[1];
                stringBuilders[0] = new StringBuilder();
                break;
            case 3:

                break;
        }

    }

    public void setMensaje(String mensaje) {

        this.mensaje = mensaje;
    }

    public String getMotivo(){
        return etMotivo.getText().toString();
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        final View vistaDialogo = layoutInflater.inflate(R.layout.dialog_emizor_layout,null);

        final BootstrapButton bsbAceptar = (BootstrapButton) vistaDialogo.findViewById(R.id.bsbAceptarDialogo);
        BootstrapButton bsbCancelar = (BootstrapButton) vistaDialogo.findViewById(R.id.bsbCancelarDialogo);

        bsbAceptar.setOnClickListener(this);
        bsbCancelar.setOnClickListener(this);
        bsbCancelar.setVisibility(View.GONE);

        final View vistaMotivo = vistaDialogo.findViewById(R.id.vistaMotivo);

        etMotivo = vistaMotivo.findViewById(R.id.etMotivo);

        ((BootstrapLabel)vistaDialogo.findViewById(R.id.bslTituloDialogo)).setBootstrapText(new BootstrapText.Builder(getContext()).addText(titulo).build());

        if (tipoDialogo == 1){
            vistaDialogo.findViewById(R.id.vistaCerrarSesion).setVisibility(View.VISIBLE);
            bsbCancelar.setVisibility(View.VISIBLE);
            vista = null;
        }else if (tipoDialogo == 3 || tipoDialogo == 4 || tipoDialogo == 5 || tipoDialogo == 6 || tipoDialogo == 7){

            if (tipoDialogo == 3 || tipoDialogo == 5) {
                bsbCancelar.setVisibility(View.VISIBLE);
            }

            if (tipoDialogo == 5){
                vistaMotivo.setVisibility(View.VISIBLE);
            }

            TextView tvMensaje = vistaDialogo.findViewById(R.id.tvMensajeDialogo);

            View vistaMensaje = vistaDialogo.findViewById(R.id.vistaMensaje);

            vistaMensaje.setVisibility(View.VISIBLE);

            LogUtils.i("DIALOG", mensaje);

            tvMensaje.setText(mensaje);
        }


        builder.setView(vistaDialogo);

        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                switch (keyCode){
                    case KeyEvent.KEYCODE_CALL:

                        if (vistaMotivo.getVisibility() == View.VISIBLE){

                            if (etMotivo.getText().toString().isEmpty()){

                                return true;
                            }

                        }

                            mListener.onRealizaAccionDialogEmizor(DialogoEmizor.this, NotificaDialogEmizorListener.ACCION_ACEPTAR, tipoDialogo);


                        return true;
                    case KeyEvent.KEYCODE_BACK:
                        return true;
                }

                return false;
            }
        });
        return builder.create();
    }

    public String obtenerDatos(int posicion){
        if (stringBuilders != null) {
            if (stringBuilders[posicion].length() > 0) {
                return stringBuilders[posicion].toString();
            }
        }
        return "0.00";
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        try{

            mListener = (NotificaDialogEmizorListener) activity;

        }catch (ClassCastException ccex){

            throw new ClassCastException(activity.toString() + " debe implementar NotificaDialogEmizorListener");

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bsbAceptarDialogo:

                if (v.getVisibility() == View.VISIBLE) {

                    if (tipoDialogo != 5) {

                        v.setEnabled(false);
                        mListener.onRealizaAccionDialogEmizor(DialogoEmizor.this, NotificaDialogEmizorListener.ACCION_ACEPTAR, tipoDialogo);
                    } else {
                        if (etMotivo.getText().toString().trim().isEmpty()) {
                            etMotivo.setError("Debe ingresar el motivo por el que solicita la reversión de la venta.");

                        } else {
                            v.setEnabled(false);
                            mListener.onRealizaAccionDialogEmizor(DialogoEmizor.this, NotificaDialogEmizorListener.ACCION_ACEPTAR, tipoDialogo);
                        }
                    }
                }

                break;
            case R.id.bsbCancelarDialogo:

                mListener.onRealizaAccionDialogEmizor(DialogoEmizor.this,NotificaDialogEmizorListener.ACCION_CANCELAR, tipoDialogo);

                break;
        }
    }



    public interface NotificaDialogEmizorListener{

        static final int ACCION_ACEPTAR = 10;
        static final int ACCION_CANCELAR = 11;

        public void onRealizaAccionDialogEmizor(DialogoEmizor dialogoEmizor, int accion, int tipodialogo);
    }


}
