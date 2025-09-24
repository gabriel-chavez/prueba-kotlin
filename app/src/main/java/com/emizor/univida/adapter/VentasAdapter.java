package com.emizor.univida.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
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
import com.emizor.univida.modelo.dominio.univida.soatc.ComprobanteDato;
import com.emizor.univida.modelo.dominio.univida.soatc.ListarVentasResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VentasAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final Context ctx;
    private final List<ListarVentasResponse> data = new ArrayList<>();
    TextView tv_certificado, tv_documento, tv_autorizacion, tv_factura, tv_fecha, tv_prima, tv_estado;

    public VentasAdapter(Context context) {
        this.ctx = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<ListarVentasResponse> nueva) {
        data.clear();
        if (nueva != null) data.addAll(nueva);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ListarVentasResponse getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_certificado, parent, false);
        }

        ListarVentasResponse it = getItem(position);
        tv_certificado = convertView.findViewById(R.id.tv_certificado);
        tv_documento = convertView.findViewById(R.id.tv_documento);
        tv_factura = convertView.findViewById(R.id.tv_factura);
        tv_fecha = convertView.findViewById(R.id.tv_fecha);
        tv_prima = convertView.findViewById(R.id.tv_prima);
        tv_estado = convertView.findViewById(R.id.tv_estado);

        // Asigna los valores de ListarVentasResponse a los TextView correspondientes
        tv_certificado.setText("Nro. Certificado: " + safe(it.PolMaeCodigoPoliza));
        tv_documento.setText("Nro. Documento identidad: " + safe(String.valueOf(it.PerDocumentoIdentidadNumero)));
        tv_factura.setText("Nro. Factura: " + String.valueOf(it.FacturaMaestroNumeroFactura));
        tv_fecha.setText("Fecha Factura: " + formatearFecha(it.FacturaMaestroFechaEmision));
        tv_prima.setText("Prima: " + formatDouble(it.PolDetPrimaCobrada));

        // Estado con badge (cambia color según estado)
        //pendiente de cobro
        if (it.PlaPagDetHistEstFk == 2 || it.PlaPagDetHistEstFk == 0) {
            tv_estado.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_estado_pendiente));
            tv_estado.setText(
                    (it.PlaPagDetHistEstDescripcion == null || it.PlaPagDetHistEstDescripcion.trim().isEmpty())
                            ? "No se realizó el pago"
                            : it.PlaPagDetHistEstDescripcion
            );
        } else if (it.PolDetTParEmiPolizaPV1EstFk == 1) {
            tv_estado.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_estado_procesado));
            tv_estado.setText(it.PolDetTParEmiPolizaPV1EstFkDescripcion);
        } else {
            tv_estado.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_estado_anulado));
            tv_estado.setText(it.PolDetTParEmiPolizaPV1EstFkDescripcion);
        }
        convertView.setOnClickListener(v -> mostrarOpciones(it));


        return convertView;
    }

    private void mostrarOpciones(ListarVentasResponse item) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
        builder.setTitle("Seleccione una acción");

        // Lista de opciones básicas
        List<CharSequence> opcionesList = new ArrayList<>();

        // Agregar opciones solo si se cumple la condición PolDetTParEmiPolizaPV1EstFk == 1
        if (item.PolDetTParEmiPolizaPV1EstFk == 1) {
            opcionesList.add("🖨️  Reimprimir");
            opcionesList.add("📧  Enviar factura y comprobante");
            opcionesList.add("📄  Solicitar reversión");
        }

        // Condición 1: Si PlaPagDetHistEstFk es 2 o 0, agregar "Reintentar pago"
        if (item.PlaPagDetHistEstFk == 2 || item.PlaPagDetHistEstFk == 0) {
            opcionesList.clear();

            opcionesList.add("💰  Reintentar pago");
        }

        // Verificar si la lista tiene opciones antes de mostrar el diálogo
        if (!opcionesList.isEmpty()) {
            // Convertir la lista a un arreglo de CharSequence
            CharSequence[] opciones = opcionesList.toArray(new CharSequence[0]);

            builder.setItems(opciones, (dialog, which) -> {
                switch (which) {
                    case 0:
                        // Acción de reimprimir
                        reimprimir(item);
                        break;
                    case 1:
                        // Acción de enviar correo
                        enviarCorreo(item);
                        break;
                    case 2:
                        // Acción de solicitar reversión
                        solicitarReversion(item);
                        break;
                    case 3:
                        // Acción de reintentar pago (solo visible si se cumple la condición)
                        if (item.PlaPagDetHistEstFk == 2 || item.PlaPagDetHistEstFk == 0) {
                            reintentarPago(item);
                        }
                        break;
                }
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.show();
        }
        // Si opcionesList está vacío, no se hace nada y no se muestra el diálogo.
    }


    private void reimprimir(ListarVentasResponse item) {
        // TODO: lógica para reimprimir
    }

    private void enviarCorreo(ListarVentasResponse item) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
        builder.setTitle("¿Está seguro de enviar por correo al asegurado?");
        builder.setMessage("Correo: " + item.FacturaMaestroCorreoCliente);

        // Crear un LinearLayout para contener los botones y establecer la orientación vertical
        LinearLayout layout = new LinearLayout(ctx);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Establecer márgenes para el LinearLayout (padding)
        int padding = 16;  // Puedes ajustar este valor según tus necesidades
        layout.setPadding(padding, padding, padding, padding);

        // Crear el botón "ENVIAR"
        Button enviarButton = new Button(ctx);
        enviarButton.setText("ENVIAR");
        enviarButton.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde
        enviarButton.setTextColor(Color.WHITE);
        enviarButton.setAllCaps(false);  // Para que el texto no esté en mayúsculas
        enviarButton.setOnClickListener(v -> {
            // Lógica para enviar el correo
            Toast.makeText(ctx, "Correo enviado a: " + item.FacturaMaestroCorreoCliente, Toast.LENGTH_SHORT).show();
        });

        // Crear un LayoutParams para el botón "ENVIAR"
        LinearLayout.LayoutParams enviarParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        // Márgenes: 10px izquierda/derecha, 5px abajo
        enviarParams.setMargins(40, 0, 40, 15);  // margen izquierda, arriba, derecha, abajo
        enviarButton.setLayoutParams(enviarParams);

        // Crear el botón "CANCELAR"
        Button cancelarButton = new Button(ctx);
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
        layout.addView(enviarButton);
        layout.addView(cancelarButton);

        // Establecer el layout como vista del diálogo
        builder.setView(layout);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();

        cancelarButton.setOnClickListener(v -> dialog.dismiss());
    }


    private void solicitarReversion(ListarVentasResponse item) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
        builder.setTitle("REVERSIÓN");

        // Crear un EditText para que el usuario ingrese el motivo
        final EditText motivoInput = new EditText(ctx);
        motivoInput.setHint("Ingrese el motivo de la reversión");
        motivoInput.setInputType(InputType.TYPE_CLASS_TEXT);

        // Crear un LinearLayout para contener el EditText y los botones
        LinearLayout layout = new LinearLayout(ctx);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);
        layout.addView(motivoInput);  // Añadir el EditText al LinearLayout

        // Crear los botones
        Button aceptarButton = new Button(ctx);
        aceptarButton.setText("ACEPTAR");
        aceptarButton.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde
        aceptarButton.setTextColor(Color.WHITE);
        aceptarButton.setAllCaps(false);  // Para que el texto no esté en mayúsculas
        aceptarButton.setOnClickListener(v -> {
            String motivo = motivoInput.getText().toString().trim();
            if (!TextUtils.isEmpty(motivo)) {
                // Lógica para aceptar la reversión con el motivo
                Toast.makeText(ctx, "Reversión aceptada. Motivo: " + motivo, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ctx, "Por favor ingrese un motivo", Toast.LENGTH_SHORT).show();
            }
        });

        // Crear un LayoutParams para el botón "ACEPTAR"
        LinearLayout.LayoutParams aceptarParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        // Márgenes: 10px izquierda/derecha, 5px abajo
        aceptarParams.setMargins(40, 0, 40, 15);  // margen izquierda, arriba, derecha, abajo
        aceptarButton.setLayoutParams(aceptarParams);

        Button cancelarButton = new Button(ctx);
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

        cancelarButton.setOnClickListener(v -> dialog.dismiss());
    }

    private void reintentarPago(ListarVentasResponse item) {
        // TODO: lógica para reintentar pago
    }

    private String formatearFecha(String fechaIso) {
        if (fechaIso == null || fechaIso.isEmpty()) {
            return "-";
        }

        try {
            // Si la fecha tiene formato con "T"
            SimpleDateFormat formatoEntrada = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                formatoEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            }
            Date fecha = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fecha = formatoEntrada.parse(fechaIso);
            }

            // Verificamos si es 1900-01-01
            SimpleDateFormat formatoComparacion = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                formatoComparacion = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            }
            String fechaComparacion = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fechaComparacion = formatoComparacion.format(fecha);
            }
            if (fechaComparacion.equals("1900-01-01")) {
                return "-";
            }

            // Formato final de salida (dd/MM/yyyy)
            SimpleDateFormat formatoSalida = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                formatoSalida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return formatoSalida.format(fecha);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "-";
        }
        return "-";
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private String formatDouble(double d) {
        return String.format(java.util.Locale.getDefault(), "%.2f", d);
    }

}
