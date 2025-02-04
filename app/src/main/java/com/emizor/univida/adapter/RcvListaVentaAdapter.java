package com.emizor.univida.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emizor.univida.R;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RcvVenta;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

/**
 * Created by silisqui on 01/03/2017.
 */

public class RcvListaVentaAdapter extends RecyclerView.Adapter<RcvListaVentaAdapter.ViewHolder>{

    private Context context;
    private List<RcvVenta> listaRcvDatosVenta;
    private RecyclerViewOnItemCickListener recyclerViewOnItemCickListener;
    private DecimalFormat decimalFormat;

    public RcvListaVentaAdapter(Context context, List<RcvVenta> listaRcvDatosVenta, RecyclerViewOnItemCickListener recyclerViewOnItemCickListener) {
        this.context = context;
        this.listaRcvDatosVenta = listaRcvDatosVenta;
        this.recyclerViewOnItemCickListener = recyclerViewOnItemCickListener;
        decimalFormat = new DecimalFormat("#######0.00");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vistaLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta,parent,false);
        ViewHolder viewHolder = new ViewHolder(vistaLista);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RcvVenta rcvVenta = listaRcvDatosVenta.get(position);

        holder.tvSoatNumeroComprobanteVentaItem.setText(String.valueOf(rcvVenta.getSoatNumeroComprobante()));
        holder.tvVehiculoPlacaVentaItem.setText(rcvVenta.getVehiculoPlaca());
        holder.tvAutorizacionNumeroVentaItem.setText(rcvVenta.getFacturaAurotizacionNumero());
        holder.tvNumeroFacturaVentaItem.setText(String.valueOf(rcvVenta.getFacturaNumero()));
        holder.tvFechaFacturaVentaItem.setText(rcvVenta.getFacturaFecha());
        holder.tvPrimaFacturaVentaItem.setText(decimalFormat.format(rcvVenta.getFacturaPrima()));
        holder.tvEstadoVentaItem.setText(rcvVenta.getEstado());
        holder.tvMedioPagoVentaItem.setText(rcvVenta.getSoatMediosDePago());


    }

    @Override
    public int getItemCount() {
        return listaRcvDatosVenta.size();
    }

    public RcvVenta getRcvVenta(int position) {
        return listaRcvDatosVenta.get(position);
    }

    public void cambiarLista(List<RcvVenta> listaSoatDatosVenta) {
        this.listaRcvDatosVenta.clear();
        this.listaRcvDatosVenta.addAll(listaSoatDatosVenta);
        notifyDataSetChanged();
    }

    public void agregarLista(List<RcvVenta> listanueva) {

        listaRcvDatosVenta.addAll(listanueva);
        notifyDataSetChanged();
    }

    public void cambiarEstado(RcvVenta rcvVentaSeleccionado, int estado) {
        if (rcvVentaSeleccionado != null){
            int posicion = listaRcvDatosVenta.indexOf(rcvVentaSeleccionado);

            if (posicion >= 0){

                listaRcvDatosVenta.get(posicion).setSoatEstado(estado);
                listaRcvDatosVenta.get(posicion).resetEstado();
                notifyItemChanged(posicion);

            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvSoatNumeroComprobanteVentaItem, tvVehiculoPlacaVentaItem, tvAutorizacionNumeroVentaItem, tvNumeroFacturaVentaItem, tvFechaFacturaVentaItem, tvPrimaFacturaVentaItem, tvEstadoVentaItem,tvMedioPagoVentaItem;

        public ViewHolder(View item){
            super(item);
            tvSoatNumeroComprobanteVentaItem = item.findViewById(R.id.tvSoatNumeroComprobanteVentaItem);
            tvVehiculoPlacaVentaItem = item.findViewById(R.id.tvVehiculoPlacaVentaItem);
            tvAutorizacionNumeroVentaItem = item.findViewById(R.id.tvAutorizacionNumeroVentaItem);
            tvNumeroFacturaVentaItem = item.findViewById(R.id.tvNumeroFacturaVentaItem);
            tvFechaFacturaVentaItem = item.findViewById(R.id.tvFechaFacturaVentaItem);
            tvPrimaFacturaVentaItem = item.findViewById(R.id.tvPrimaFacturaVentaItem);
            tvEstadoVentaItem = item.findViewById(R.id.tvEstadoVentaItem);
            tvMedioPagoVentaItem=item.findViewById(R.id.tvMedioPagoVentaItem);
            item.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            recyclerViewOnItemCickListener.onClick(view,getAdapterPosition(), -1);
        }

    }

}
