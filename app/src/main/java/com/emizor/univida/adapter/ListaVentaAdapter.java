package com.emizor.univida.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.emizor.univida.R;
import com.emizor.univida.modelo.dominio.univida.ventas.SoatDatosVentum;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by silisqui on 01/03/2017.
 */

public class ListaVentaAdapter extends RecyclerView.Adapter<ListaVentaAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<SoatDatosVentum> listaSoatDatosVenta;
    private List<SoatDatosVentum> listaSoatDatosVentaFilter;
    private RecyclerViewOnItemCickListener recyclerViewOnItemCickListener;
    private DecimalFormat decimalFormat;
    private CustomFilter mFilter;

    public ListaVentaAdapter(Context context, List<SoatDatosVentum> listaSoatDatosVenta, RecyclerViewOnItemCickListener recyclerViewOnItemCickListener) {
        this.context = context;
        this.listaSoatDatosVenta = listaSoatDatosVenta;
        this.listaSoatDatosVentaFilter = new ArrayList<>();

        this.listaSoatDatosVentaFilter.addAll(this.listaSoatDatosVenta);
        this.recyclerViewOnItemCickListener = recyclerViewOnItemCickListener;
        decimalFormat = new DecimalFormat("#######0.00");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        this.mFilter = new CustomFilter(ListaVentaAdapter.this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vistaLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta,parent,false);
        ViewHolder viewHolder = new ViewHolder(vistaLista);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        SoatDatosVentum soatDatosVentum = listaSoatDatosVentaFilter.get(position);

        holder.tvSoatNumeroComprobanteVentaItem.setText(String.valueOf(soatDatosVentum.getSoatNumeroComprobante()));
        holder.tvVehiculoPlacaVentaItem.setText(soatDatosVentum.getVehiculoPlaca());
        holder.tvAutorizacionNumeroVentaItem.setText(soatDatosVentum.getFacturaAutorizacionNumero());
        holder.tvNumeroFacturaVentaItem.setText(String.valueOf(soatDatosVentum.getFacturaNumero()));
        holder.tvFechaFacturaVentaItem.setText(soatDatosVentum.getFacturaFecha());
        holder.tvPrimaFacturaVentaItem.setText(decimalFormat.format(soatDatosVentum.getFacturaPrima()));
        holder.tvEstadoVentaItem.setText(soatDatosVentum.getEstado());
        holder.tvMedioPagoVentaItem.setText(soatDatosVentum.getSoatMediosDePago());

    }

    @Override
    public int getItemCount() {
        return listaSoatDatosVentaFilter.size();
    }

    public SoatDatosVentum getSoatDatosVentum(int position) {
        if (listaSoatDatosVentaFilter != null && position < listaSoatDatosVentaFilter.size()) {
            return listaSoatDatosVentaFilter.get(position);
        }else{
            return null;
        }
    }

    public void cambiarLista(List<SoatDatosVentum> listaSoatDatosVenta) {
        this.listaSoatDatosVenta.clear();
        this.listaSoatDatosVenta.addAll(listaSoatDatosVenta);
        this.listaSoatDatosVentaFilter.clear();
        this.listaSoatDatosVentaFilter.addAll(listaSoatDatosVenta);
        notifyDataSetChanged();
    }

    public void cambiarEstado(SoatDatosVentum soatDatosVentumSeleccionado, int estado) {
        if (soatDatosVentumSeleccionado != null){
            int posicion = listaSoatDatosVenta.indexOf(soatDatosVentumSeleccionado);

            if (posicion >= 0){

                listaSoatDatosVenta.get(posicion).setSoatGenericaEstadoFk(estado);
                listaSoatDatosVenta.get(posicion).resetEstado();
                notifyItemChanged(posicion);

            }
        }
    }

    @Override
    public Filter getFilter() {
        return mFilter;
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
            tvMedioPagoVentaItem = item.findViewById(R.id.tvMedioPagoVentaItem);
            item.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            recyclerViewOnItemCickListener.onClick(view,getAdapterPosition(), -1);
        }

    }

    public class CustomFilter extends Filter {
        private ListaVentaAdapter listaVentaAdapter;

        private CustomFilter(ListaVentaAdapter listaVentaAdapter) {
            super();
            this.listaVentaAdapter = listaVentaAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            listaSoatDatosVentaFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                listaSoatDatosVentaFilter.addAll(listaSoatDatosVenta);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final SoatDatosVentum soatDatosVentum : listaSoatDatosVenta) {
                    if (soatDatosVentum.getVehiculoPlaca().toLowerCase().contains(filterPattern) || String.valueOf(soatDatosVentum.getSoatNumeroComprobante()).contains(filterPattern)) {
                        listaSoatDatosVentaFilter.add(soatDatosVentum);
                    }
                }
            }
            results.values = listaSoatDatosVentaFilter;
            results.count = listaSoatDatosVentaFilter.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.listaVentaAdapter.notifyDataSetChanged();
        }
    }

}
