package com.emizor.univida.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.emizor.univida.R;
import com.emizor.univida.modelo.dominio.univida.ventas.QrGenerado;

import java.util.List;

public class MovimientoAdapter extends ArrayAdapter<QrGenerado> {

    private Context context;
    private List<QrGenerado> qrGenerados;

    public MovimientoAdapter(Context context, List<QrGenerado> qrGenerados) {
        super(context, R.layout.item_historial_qr, qrGenerados);
        this.context = context;
        this.qrGenerados = qrGenerados;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_historial_qr, parent, false);
        }

        // Obtener el objeto Movimiento para esta posición
        QrGenerado qrGenerado = qrGenerados.get(position);
        int tVehiSoatPropFk = qrGenerado.getTVehiSoatPropFk();

        // Acceder a los componentes de la vista y asignar los valores
        TextView placaTextView = convertView.findViewById(R.id.textViewPlaca);
        TextView gestionTextView = convertView.findViewById(R.id.gestionTextView);
        TextView fechaTextView = convertView.findViewById(R.id.textViewFecha);
        TextView estadoTextView = convertView.findViewById(R.id.textViewEstado);
        TextView fechaEstadoTextView = convertView.findViewById(R.id.textViewFechaEstado);
        TextView efectivizadoTextView = convertView.findViewById(R.id.textViewEfectivizado);
        TextView mensajeTextView = convertView.findViewById(R.id.textViewMensaje);
        placaTextView.setText(qrGenerado.getIdentificadorVehiculo());
        gestionTextView.setText(qrGenerado.getGestion());
        fechaTextView.setText(qrGenerado.getFechaHoraSolicitud());
        estadoTextView.setText(qrGenerado.getEstadoSolicitud());
        fechaEstadoTextView.setText(qrGenerado.getFechaHoraEstado());
        if (tVehiSoatPropFk == 0) {
            efectivizadoTextView.setTextColor(Color.RED);
            efectivizadoTextView.setText("No efectivizado");
        } else {
            efectivizadoTextView.setTextColor(Color.GREEN);
            efectivizadoTextView.setText("Efectivizado");
        }

        mensajeTextView.setText(qrGenerado.getMensajeEfectivizacion());

        return convertView;
    }
}
