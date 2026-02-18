package com.emizor.univida.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emizor.univida.R;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.excepcion.ImpresoraErrorException;
import com.emizor.univida.excepcion.NoHayPapelException;
import com.emizor.univida.excepcion.VoltageBajoException;
import com.emizor.univida.fragmento.SoatcRccListarFragment;
import com.emizor.univida.imprime.ImprimirFactura;
import com.emizor.univida.modelo.dominio.univida.ApiResponse;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.soatc.ListarRccResponse;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.rest.ApiService;
import com.emizor.univida.rest.DatosConexion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemSoatcRccListarAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final Context context;
    private final List<ListarRccResponse> data = new ArrayList<>();
    private SoatcRccListarFragment.OnRccListadoListener rccListener;
    private ImprimirFactura imprimirFactura;

    public ItemSoatcRccListarAdapter(Context context, SoatcRccListarFragment.OnRccListadoListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.rccListener = listener;
        this.imprimirFactura = ImprimirFactura.obtenerImpresora(context);
    }
    public void setData(List<ListarRccResponse> nueva) {
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
        ItemSoatcRccListarAdapter.ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_soatc_rcc_listar, parent, false);
            vh = new ItemSoatcRccListarAdapter.ViewHolder();
            vh.tv_num = convertView.findViewById(R.id.tv_num);
            vh.tv_importe = convertView.findViewById(R.id.tv_importe);
            vh.tv_cantidad = convertView.findViewById(R.id.tv_cantidad);
            vh.tv_validos = convertView.findViewById(R.id.tv_validos);
            vh.tv_revertidos = convertView.findViewById(R.id.tv_revertidos);
            vh.tv_anulados = convertView.findViewById(R.id.tv_anulados);
            vh.tv_estado = convertView.findViewById(R.id.tv_estado);
            convertView.setTag(vh);
        } else {
            vh = (ItemSoatcRccListarAdapter.ViewHolder) convertView.getTag();
        }

        ListarRccResponse item = data.get(position);

        vh.tv_num.setText( safe(String.valueOf(item.RepCieCobFormularioNumero)));
        vh.tv_importe.setText(safe(String.valueOf(item.RepCieCobFormularioImporte)));
        vh.tv_cantidad.setText( safe(String.valueOf(item.RepCieCobCantidadComprobante)));
        vh.tv_validos.setText( safe(String.valueOf(item.RepCieCobCantidadComprobanteValidos)));
        vh.tv_revertidos.setText( safe(String.valueOf(item.RepCieCobCantidadComprobanteRevertidos)));
        vh.tv_anulados.setText( safe(String.valueOf(item.RepCieCobCantidadComprobanteAnulados)));


        // Cambiar color del estado según descripción
        if ("VALIDO".equalsIgnoreCase(item.RepCieCobEstadoDescripcion)) {
            vh.tv_estado.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_estado_procesado));
            vh.tv_estado.setText(item.RepCieCobEstadoDescripcion);
        } else {
            vh.tv_estado.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_estado_anulado));
            vh.tv_estado.setText(safe(item.RepCieCobEstadoDescripcion));
        }
        convertView.setOnClickListener(v -> mostrarOpciones(item));
        return convertView;
    }

    private void mostrarOpciones(ListarRccResponse item) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Seleccione una acción");

        List<CharSequence> opcionesList = new ArrayList<>();

        // Caso 1: Estado = 1


            opcionesList.add("🖨️  Reimprimir");
            opcionesList.add("📄  Solicitar reversión");


        // Mostrar solo si hay opciones
        if (!opcionesList.isEmpty()) {
            CharSequence[] opciones = opcionesList.toArray(new CharSequence[0]);

            builder.setItems(opciones, (dialog, which) -> {
                String seleccion = opcionesList.get(which).toString();

                // Se identifica la opción seleccionada por el texto
                if (seleccion.contains("Reimprimir")) {
                    reimprimir(item);
                }  else if (seleccion.contains("Solicitar reversión")) {
                    solicitarReversion(item);
                }
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.show();
        }
    }
    private void reimprimir(ListarRccResponse item) {

        try {
            ControladorSqlite2 controlador = new ControladorSqlite2(context);
            User user = controlador.obtenerUsuario();
            controlador.cerrarConexion();

            if (user == null) {
                Toast.makeText(context, "Error: Usuario no autenticado.", Toast.LENGTH_SHORT).show();
                return;
            }
            imprimirFactura.prepararImpresionRcc(user, item);
            ejecutarImpresion();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al preparar la impresión.", Toast.LENGTH_SHORT).show();
        }
    }

    private void ejecutarImpresion() {
        try {
            imprimirFactura.imprimirFactura2();
        } catch (NoHayPapelException | ImpresoraErrorException | VoltageBajoException e) {
            new AlertDialog.Builder(context)
                    .setTitle("Impresión")
                    .setMessage(e.getMessage())
                    .setCancelable(false)
                    .setPositiveButton("Reintentar", (dialog, which) -> ejecutarImpresion())
                    .setNegativeButton("Cancelar", null)
                    .show();
        } catch (Exception e) {
            Toast.makeText(context, "Error inesperado al imprimir.", Toast.LENGTH_SHORT).show();
        }
    }

    private void solicitarReversion(ListarRccResponse item) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("REVERSIÓN");

        // Crear un EditText para que el usuario ingrese el motivo
        final EditText motivoInput = new EditText(context);
        motivoInput.setHint("Ingrese el motivo de la reversión");
        motivoInput.setInputType(InputType.TYPE_CLASS_TEXT);

        // Crear un LinearLayout para contener el EditText y los botones
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);
        layout.addView(motivoInput);  // Añadir el EditText al LinearLayout

        // Crear los botones
        Button aceptarButton = new Button(context);
        aceptarButton.setText("ACEPTAR");
        aceptarButton.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde
        aceptarButton.setTextColor(Color.WHITE);
        aceptarButton.setAllCaps(false);  // Para que el texto no esté en mayúsculas


        // Crear un LayoutParams para el botón "ACEPTAR"
        LinearLayout.LayoutParams aceptarParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        // Márgenes: 10px izquierda/derecha, 5px abajo
        aceptarParams.setMargins(40, 0, 40, 15);  // margen izquierda, arriba, derecha, abajo
        aceptarButton.setLayoutParams(aceptarParams);

        Button cancelarButton = new Button(context);
        cancelarButton.setText("CANCELAR");
        cancelarButton.setBackgroundColor(Color.parseColor("#F44336")); // Rojo
        cancelarButton.setTextColor(Color.WHITE);
        cancelarButton.setAllCaps(false);  // Para que el texto no esté en mayúsculas


        // Crear un LayoutParams para el botón "CANCELAR"
        LinearLayout.LayoutParams cancelarParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        // Márgenes: 10px izquierda/derecha, sin margen abajo
        cancelarParams.setMargins(40, 15, 40, 15);  // margen izquierda, arriba, derecha, abajo
        cancelarButton.setLayoutParams(cancelarParams);

        // Añadir los botones al LinearLayout
        layout.addView(aceptarButton);
        layout.addView(cancelarButton);

        // Establecer el layout como vista del diálogo
        builder.setView(layout);

        builder.setView(layout);
        android.app.AlertDialog dialog = builder.create();

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        aceptarButton.setOnClickListener(v -> {
            String motivo = motivoInput.getText().toString().trim();
            if (!TextUtils.isEmpty(motivo)) {
                revertirRcc(item.RepCieCobSecuencial);
                Toast.makeText(context, "Reversión aceptada. Motivo: " + motivo, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Por favor ingrese un motivo", Toast.LENGTH_SHORT).show();
            }
        });

        cancelarButton.setOnClickListener(v -> dialog.dismiss());
    }
    private static class ViewHolder {
        TextView tv_num, tv_importe, tv_cantidad, tv_validos, tv_revertidos,tv_anulados, tv_estado;
    }
    private void revertirRcc(int tReporteCierreCobrosFk) {

        Toast.makeText(context, "Revirtiendo RCC...", Toast.LENGTH_SHORT).show();

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("t_reporte_cierre_cobros_fk", tReporteCierreCobrosFk);

        ((PrincipalActivity) context).mostrarLoading(true);
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_CONCILIACION_REVERTIR;

        ApiService api = new ApiService(context);
        api.solicitudPost(url, parametros, response -> {
            ((PrincipalActivity) context).mostrarLoading(false);
            Type responseType = new TypeToken<ApiResponse<Void>>() {
            }.getType();
            ApiResponse<Void> apiResp = new Gson().fromJson(response, responseType);;

            if (apiResp != null && apiResp.exito) {
               // ListarCobrosResponse datos  = apiResp.datos;

                new AlertDialog.Builder(context)
                        .setMessage(apiResp.mensaje)
                        .setPositiveButton("Aceptar", (dialog, id) -> {
                            if (rccListener != null) {
                                rccListener.onRccListener();
                                dialog.dismiss();
                            }

                        })
                        .show();

            } else {
                String msg = (apiResp != null && !TextUtils.isEmpty(apiResp.mensaje))
                        ? apiResp.mensaje : "No se pudo obtener la información.";
                new AlertDialog.Builder(context)
                        .setMessage(msg)
                        .setPositiveButton("Aceptar", (dialog, id) -> dialog.dismiss())
                        .show();
              //  adapter.setData(null);
            }

        }, error -> {
            ((PrincipalActivity) context).mostrarLoading(false);

            mostrarToast("Error de conexión. Intentá nuevamente.");
           // adapter.setData(null);
        });

    }
    private void mostrarToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

    }
    private String safe(String v) {
        return (v == null) ? "" : v;
    }
}
