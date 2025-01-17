package com.emizor.univida.dialogo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;

import com.emizor.univida.InicioActivity;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;


/**
 * Created by root on 10/03/17.
 */
public class DialogoCerrarSesion extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setMessage("¿Confirma cerrar la sesión?")
                .setTitle("Cerrar sesión")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Aceptada.");
                        dialog.cancel();
                        cerrarLaSession();
                        //dialog.cancel();www.megadescargashd.org
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmación cancelada.");
                        dialog.cancel();
                    }
                });

        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                switch (keyCode){
                    case KeyEvent.KEYCODE_CALL:
                        dialog.cancel();
                        cerrarLaSession();
                        return true;
                    case KeyEvent.KEYCODE_DEL:
                        dialog.cancel();
                        return true;
                    case KeyEvent.KEYCODE_BACK:
                        return true;
                }

                return false;
            }
        });
        return builder.create();
    }
    private void cerrarLaSession(){
        ControladorSqlite2 controladorSqlite = new ControladorSqlite2(getActivity().getApplicationContext());
        controladorSqlite.eliminarTodoPrincipal();
        controladorSqlite.crearTablasPrincipal();
        controladorSqlite.cerrarConexion();
        Intent intent = new Intent(getActivity().getApplicationContext(), InicioActivity.class);
        getActivity().setVisible(false);
        startActivity(intent);
        getActivity().finish();
    }


}
