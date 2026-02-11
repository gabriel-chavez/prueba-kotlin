package com.emizor.univida.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emizor.univida.R;
import com.emizor.univida.modelo.dominio.univida.soatc.ComprobanteDatos;


import java.util.ArrayList;
import java.util.List;

public class ItemSoatcRccNuevoAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final Context ctx;
    private final List<ComprobanteDatos> data = new ArrayList<>();

    public ItemSoatcRccNuevoAdapter(Context context) {
        this.ctx = context;
        this.inflater = LayoutInflater.from(context);
    }
    public void setData(List<ComprobanteDatos> nueva) {
        data.clear();
        if (nueva != null) data.addAll(nueva);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_soatc_rcc_nuevo, parent, false);
            vh = new ViewHolder();
            vh.tv_detalle = convertView.findViewById(R.id.tv_detalle);
            vh.tv_factura = convertView.findViewById(R.id.tv_factura);
            vh.tv_fecha = convertView.findViewById(R.id.tv_fecha);
            vh.tv_prima = convertView.findViewById(R.id.tv_prima);
            vh.tv_estado = convertView.findViewById(R.id.tv_estado);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        ComprobanteDatos item = data.get(position);

        vh.tv_detalle.setText("Detalle: " + safe(item.PlaPagComLineaDetalle));
        vh.tv_factura.setText("Nro. Factura: " + safe(String.valueOf(item.PlaPagComTComprobanteExternoNumero)));
        vh.tv_fecha.setText("Fecha factura: " + safe(item.PlaPagComFechaFormato));
        vh.tv_prima.setText("Prima: Bs. " + String.format("%.2f", item.PlaPagComComprobanteImporte));

        // Cambiar color del estado según descripción
        if ("VALIDO".equalsIgnoreCase(item.PlaPagComEstadoDescripcion)) {
            vh.tv_estado.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_estado_procesado));
            vh.tv_estado.setText(item.PlaPagComEstadoDescripcion);
        } else {
            vh.tv_estado.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_estado_anulado));
            vh.tv_estado.setText(safe(item.PlaPagComEstadoDescripcion));
        }

        return convertView;
    }


    static class ViewHolder {
        TextView tv_detalle, tv_factura, tv_fecha, tv_prima, tv_estado;
    }

    private String safe(String v) {
        return (v == null) ? "" : v;
    }
}
