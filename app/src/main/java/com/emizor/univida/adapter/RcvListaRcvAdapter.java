package com.emizor.univida.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emizor.univida.R;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.DatoRcvLista;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RcvVenta;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

/**
 * Created by silisqui on 21/11/2018.
 */

public class RcvListaRcvAdapter extends RecyclerView.Adapter<RcvListaRcvAdapter.ViewHolder>{

    private Context context;
    private List<DatoRcvLista> listaRcvDatos;
    private RecyclerViewOnItemCickListener recyclerViewOnItemCickListener;
    private DecimalFormat decimalFormat;

    public RcvListaRcvAdapter(Context context, List<DatoRcvLista> listaRcvDatos, RecyclerViewOnItemCickListener recyclerViewOnItemCickListener) {
        this.context = context;
        this.listaRcvDatos = listaRcvDatos;
        this.recyclerViewOnItemCickListener = recyclerViewOnItemCickListener;
        decimalFormat = new DecimalFormat("#######0.00");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vistaLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv,parent,false);
        ViewHolder viewHolder = new ViewHolder(vistaLista);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DatoRcvLista datoRcvLista = listaRcvDatos.get(position);

        holder.tvFechaRcvItem.setText(String.valueOf(datoRcvLista.getRcvFormularioFecha()));
        holder.tvImporteRcvItem.setText(decimalFormat.format(datoRcvLista.getRcvFormularioImporte()));
        holder.tvCantidadTotalRcvItem.setText(String.valueOf(datoRcvLista.getRcvCantidad()));
        holder.tvCantidadValidosRcvItem.setText(String.valueOf(datoRcvLista.getRcvCantidadValidos()));
        holder.tvCantidadAnuladosRcvItem.setText(String.valueOf(datoRcvLista.getRcvCantidadAnulados()));
        holder.tvCantidadRevertidosRcvItem.setText(String.valueOf(datoRcvLista.getRcvCantidadRevertidos()));
        holder.tvEstadoRcvItem.setText(datoRcvLista.getEstado());

        holder.tvEstadoRemitidoRcvItem.setText(((datoRcvLista.getRcvEstadoRemitido())? "SI":"NO"));



    }

    @Override
    public int getItemCount() {
        return listaRcvDatos.size();
    }

    public DatoRcvLista getRcvDato(int position) {
        return listaRcvDatos.get(position);
    }

    public void cambiarLista(List<DatoRcvLista> lista) {
        this.listaRcvDatos.clear();
        this.listaRcvDatos.addAll(lista);
        notifyDataSetChanged();
    }

    public void agregarLista(List<DatoRcvLista> listanueva) {
        listaRcvDatos.addAll(listanueva);
        notifyDataSetChanged();
    }

    public void cambiarEstado(DatoRcvLista rcvSeleccionado, int estado) {
        if (rcvSeleccionado != null){
            int posicion = listaRcvDatos.indexOf(rcvSeleccionado);

            if (posicion >= 0){

                listaRcvDatos.get(posicion).setRcvEstadoFk(estado);
                listaRcvDatos.get(posicion).resetEstado();
                notifyItemChanged(posicion);

            }
        }
    }
    public void cambiarEstadoRemitir(DatoRcvLista rcvSeleccionado, boolean estado) {
        if (rcvSeleccionado != null){
            int posicion = listaRcvDatos.indexOf(rcvSeleccionado);

            if (posicion >= 0){

                listaRcvDatos.get(posicion).setRcvEstadoRemitido(estado);
                notifyItemChanged(posicion);

            }
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvFechaRcvItem, tvImporteRcvItem, tvCantidadTotalRcvItem, tvCantidadValidosRcvItem, tvCantidadAnuladosRcvItem, tvCantidadRevertidosRcvItem, tvEstadoRcvItem, tvEstadoRemitidoRcvItem;
        public ViewHolder(View item){
            super(item);
            tvFechaRcvItem = item.findViewById(R.id.tvFechaRcvItem);
            tvImporteRcvItem = item.findViewById(R.id.tvImporteRcvItem);
            tvCantidadTotalRcvItem = item.findViewById(R.id.tvCantidadTotalRcvItem);
            tvCantidadValidosRcvItem = item.findViewById(R.id.tvCantidadValidosRcvItem);
            tvCantidadAnuladosRcvItem = item.findViewById(R.id.tvCantidadAnuladosRcvItem);
            tvCantidadRevertidosRcvItem = item.findViewById(R.id.tvCantidadRevertidosRcvItem);
            tvEstadoRcvItem = item.findViewById(R.id.tvEstadoRcvItem);
            tvEstadoRemitidoRcvItem = item.findViewById(R.id.tvEstadoRemitidoRcvItem);
            item.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            recyclerViewOnItemCickListener.onClick(view,getAdapterPosition(), -1);
        }

    }

}
