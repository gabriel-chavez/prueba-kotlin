package com.emizor.univida.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emizor.univida.R;
import com.emizor.univida.modelo.dominio.univida.soatc.CliObtenerDatosResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.ClientesObtenerDatosResponse;

import java.util.List;

public class AseguradoAdapter extends RecyclerView.Adapter<AseguradoAdapter.ViewHolder> {

    private List<CliObtenerDatosResponse> mData;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(CliObtenerDatosResponse item);
    }

    public AseguradoAdapter(List<CliObtenerDatosResponse> data, OnItemClickListener listener) {
        this.mData = data;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asegurado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CliObtenerDatosResponse item = mData.get(position);
        String nombreCompleto = item.PerNombrePrimero + " " +
                               (item.PerNombreSegundo != null ? item.PerNombreSegundo + " " : "") +
                               item.PerApellidoPaterno + " " +
                               item.PerApellidoMaterno;
        holder.tvNombre.setText(nombreCompleto.trim());
        holder.tvDocumento.setText(item.PerTParCliDocumentoIdentidadTipoDescripcion+": " + item.PerDocumentoIdentidadNumero + " " + (item.PerTParGenDepartamentoAbreviacionDocumentoIdentidad != null ? item.PerTParGenDepartamentoAbreviacionDocumentoIdentidad : ""));

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public void updateData(List<CliObtenerDatosResponse> newData) {
        this.mData = newData;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDocumento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDocumento = itemView.findViewById(R.id.tvDocumento);
        }
    }
}
