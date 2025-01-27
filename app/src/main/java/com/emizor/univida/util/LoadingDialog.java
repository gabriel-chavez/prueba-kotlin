package com.emizor.univida.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.emizor.univida.R;

public class LoadingDialog {

    private AlertDialog loadingDialog;

    public void showLoading(Context context, String message) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            TextView loadingMessage = loadingDialog.findViewById(R.id.loading_message);
            if (loadingMessage != null) {
                loadingMessage.setText(message);
            }
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View loadingView = inflater.inflate(R.layout.loading_dialog, null);

        TextView loadingMessage = loadingView.findViewById(R.id.loading_message);
        loadingMessage.setText(message);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(loadingView);
        builder.setCancelable(false);

        loadingDialog = builder.create();
        loadingDialog.show();
    }

    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
