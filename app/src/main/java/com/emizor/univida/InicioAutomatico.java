package com.emizor.univida;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by posemizor on 17-10-17.
 */

public class InicioAutomatico extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // la actividad inicia automaticamente al encender eldispositivo.
        Intent intent1 = new Intent(context, InicioActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

    }
}
