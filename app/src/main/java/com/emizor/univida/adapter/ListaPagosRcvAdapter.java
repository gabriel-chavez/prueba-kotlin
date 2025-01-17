package com.emizor.univida.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emizor.univida.R;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RcvJsonMedioPagoUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.SoatDatosVentum;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

/**
 * Created by silisqui on 01/03/2017.
 */

public class ListaPagosRcvAdapter extends RecyclerView.Adapter<ListaPagosRcvAdapter.ViewHolder>{

    private Context context;
    private List<RcvJsonMedioPagoUnivida> rcvJsonMedioPagoUnividas;
    private RecyclerViewOnItemCickListener recyclerViewOnItemCickListener;
    private DecimalFormat decimalFormat;

    public ListaPagosRcvAdapter(Context context, List<RcvJsonMedioPagoUnivida> rcvJsonMedioPagoUnividas, RecyclerViewOnItemCickListener recyclerViewOnItemCickListener) {
        this.context = context;
        this.rcvJsonMedioPagoUnividas = rcvJsonMedioPagoUnividas;
        this.recyclerViewOnItemCickListener = recyclerViewOnItemCickListener;
        decimalFormat = new DecimalFormat("#######0.00");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vistaLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_json,parent,false);
        ViewHolder viewHolder = new ViewHolder(vistaLista);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RcvJsonMedioPagoUnivida rcvJsonMedioPagoUnivida = rcvJsonMedioPagoUnividas.get(position);

        holder.tvNumeroOrdinalRcvItem.setText(String.valueOf(rcvJsonMedioPagoUnivida.getNumero()));
        holder.tvMedioPagoRcvItem.setText(rcvJsonMedioPagoUnivida.getMedioDePagoDescripcion());
        holder.tvMontoPagoRcvItem.setText(String.valueOf(rcvJsonMedioPagoUnivida.getMedioDePagoPrima()));
        holder.tvReferenciaPagoRcvItem.setText(rcvJsonMedioPagoUnivida.getMedioDePagoDato());
        holder.tvFechaPagoRcvItem.setText(rcvJsonMedioPagoUnivida.getMedioDePagoFecha());
        holder.tvBancoPagoRcvItem.setText(rcvJsonMedioPagoUnivida.getMedioBanco());

    }

    @Override
    public int getItemCount() {
        return rcvJsonMedioPagoUnividas.size();
    }

    public RcvJsonMedioPagoUnivida getRcvJsonMedioPagoUnivida(int position) {
        return rcvJsonMedioPagoUnividas.get(position);
    }

    public void cambiarLista(List<RcvJsonMedioPagoUnivida> listaSoatDatosVenta) {
        this.rcvJsonMedioPagoUnividas.clear();
        this.rcvJsonMedioPagoUnividas.addAll(listaSoatDatosVenta);
        notifyDataSetChanged();
    }

    public void agregarLista(List<RcvJsonMedioPagoUnivida> listanueva) {

        rcvJsonMedioPagoUnividas.addAll(listanueva);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvNumeroOrdinalRcvItem, tvMedioPagoRcvItem, tvMontoPagoRcvItem, tvReferenciaPagoRcvItem, tvFechaPagoRcvItem, tvBancoPagoRcvItem;

        public ViewHolder(View item){
            super(item);
            tvNumeroOrdinalRcvItem = item.findViewById(R.id.tvNumeroOrdinalRcvItem);
            tvMedioPagoRcvItem = item.findViewById(R.id.tvMedioPagoRcvItem);
            tvMontoPagoRcvItem = item.findViewById(R.id.tvMontoPagoRcvItem);
            tvReferenciaPagoRcvItem = item.findViewById(R.id.tvReferenciaPagoRcvItem);
            tvFechaPagoRcvItem = item.findViewById(R.id.tvFechaPagoRcvItem);
            tvBancoPagoRcvItem = item.findViewById(R.id.tvBancoPagoRcvItem);
            item.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            recyclerViewOnItemCickListener.onClick(view,getAdapterPosition(), -1);
        }

    }

}
