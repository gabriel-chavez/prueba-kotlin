package com.emizor.univida.adapter;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.emizor.univida.R;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.dialogo.DialogoEmizor;
import com.emizor.univida.excepcion.ErrorPapelException;
import com.emizor.univida.excepcion.ImpresoraErrorException;
import com.emizor.univida.excepcion.NoHayPapelException;
import com.emizor.univida.excepcion.VoltageBajoException;
import com.emizor.univida.fragmento.SoatcAseguradoBuscarFragment;
import com.emizor.univida.fragmento.SoatcListarVentasFragment;
import com.emizor.univida.imprime.ImprimirAvisoListener;
import com.emizor.univida.imprime.ImprimirFactura;
import com.emizor.univida.modelo.dominio.univida.ApiResponse;
import com.emizor.univida.modelo.dominio.univida.parametricas.ParametricaGenerica;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.soatc.EmiPolizaObtenerResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.ListarVentasResponse;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.rest.ApiService;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.util.ValidarCampo;
import com.emizor.univida.utils.ParametricasCache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ItemSoatcVentaListarAdapter extends BaseAdapter implements ImprimirAvisoListener {

    private final LayoutInflater inflater;
    private final Context context;
    private final List<ListarVentasResponse> data = new ArrayList<>();
    TextView tv_certificado, tv_documento, tv_autorizacion, tv_factura, tv_fecha, tv_prima, tv_estado;

    private Spinner spinnerTipoDocumento;
    private EditText etNumeroDocumento, etExtensionDocumento, etNombreRazonSocial, etEmail, etCelular;
    private TextView tvTitulo;
    private LinearLayout layoutExtensionDocumento;
    private Button btnAtras, btnSiguiente;
    private SoatcListarVentasFragment.OnVentaListadoListener ventaListener;
    private android.app.AlertDialog dialogReintentarPago;
    private android.app.AlertDialog dialogReversion;
    private android.app.AlertDialog dialogNotificacion;
    private EmiPolizaObtenerResponse respuestaEfectivizacion;
    private ImprimirFactura imprimirFactura;
    public ItemSoatcVentaListarAdapter(Context context, SoatcListarVentasFragment.OnVentaListadoListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.ventaListener = listener;
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
            convertView = inflater.inflate(R.layout.item_soatc_venta_listar, parent, false);
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
            tv_estado.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_estado_pendiente));
            tv_estado.setText(
                    (it.PlaPagDetHistEstDescripcion == null || it.PlaPagDetHistEstDescripcion.trim().isEmpty())
                            ? "No se realizó el pago"
                            : it.PlaPagDetHistEstDescripcion
            );
        } else if (it.PolDetTParEmiPolizaPV1EstFk == 1) {
            tv_estado.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_estado_procesado));
            tv_estado.setText(it.PolDetTParEmiPolizaPV1EstFkDescripcion);
        } else {
            tv_estado.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_estado_anulado));
            tv_estado.setText(it.PolDetTParEmiPolizaPV1EstFkDescripcion);
        }
        convertView.setOnClickListener(v -> mostrarOpciones(it));

        imprimirFactura = ImprimirFactura.obtenerImpresora(context);
        imprimirFactura.setAvisoListener(this);

        return convertView;
    }

    private void mostrarOpciones(ListarVentasResponse item) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Seleccione una acción");

        List<CharSequence> opcionesList = new ArrayList<>();

        // Caso 1: Estado = 1
        if (item.PolDetTParEmiPolizaPV1EstFk == 1) {

            opcionesList.add("🖨️  Reimprimir");
            opcionesList.add("📧  Enviar factura y comprobante");
            opcionesList.add("📄  Revertir");
        }

        // Caso 2: Estado = 2 o 0
        if (item.PlaPagDetHistEstFk == 2 || item.PlaPagDetHistEstFk == 0) {
            opcionesList.clear();
            opcionesList.add("💳  Reintentar pago");

        }

        // Mostrar solo si hay opciones
        if (!opcionesList.isEmpty()) {
            CharSequence[] opciones = opcionesList.toArray(new CharSequence[0]);

            builder.setItems(opciones, (dialog, which) -> {
                String seleccion = opcionesList.get(which).toString();

                // Se identifica la opción seleccionada por el texto
                if (seleccion.contains("Reimprimir")) {
                    reimprimir(item);
                } else if (seleccion.contains("Enviar factura")) {
                    enviarCorreo(item);
                } else if (seleccion.contains("Revertir")) {
                    dialogSolicitarReversion(item);
                } else if (seleccion.contains("Reintentar pago")) {
                    reintentarPago(item);
                } else if (seleccion.contains("Anular venta")) {
                    anularVenta(item);
                }
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.show();
        }
    }

    private void anularVenta(ListarVentasResponse item) {
    }


    private void reimprimir(ListarVentasResponse item) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("t_poliza_detalle_fk", item.PolDetSecuencialFk);
        ApiService apiService = new ApiService(context);
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_EMISION_POLIZA_OBTENER;
        ((PrincipalActivity) context).mostrarLoading(true);
        dialogReversion.dismiss();
        apiService.solicitudPost(url, parametros, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ((PrincipalActivity) context).mostrarLoading(false);
                try {

                    Type responseType = new TypeToken<ApiResponse<EmiPolizaObtenerResponse>>() {
                    }.getType();
                    ApiResponse<EmiPolizaObtenerResponse> apiResponse = new Gson().fromJson(response, responseType);


                    if (apiResponse.exito) {
                        respuestaEfectivizacion=apiResponse.datos;
                        // INICIAR IMPRESIÓN
                        try {
                            ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(context);
                            User user = controladorSqlite2.obtenerUsuario();
                            imprimirFactura.prepararImpresionFacturaSoatc(user, respuestaEfectivizacion);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //mostrarMensaje("No se puede imprimir los datos por que algunos o todos son nulos.");
                            ((PrincipalActivity) context).mostrarLoading(false);
                            new AlertDialog.Builder(context)
                                    .setTitle("Atención")
                                    .setMessage("No se puede imprimir los datos por que algunos o todos son nulos.")
                                    .setPositiveButton("Aceptar", (dialog, which) -> {
                                      //  cambiarFragmento(new SoatcAseguradoBuscarFragment());
                                    }).show();
                            return;
                        }

                        iniciarImprimir();
                    } else {

                        new AlertDialog.Builder(context)
                                .setTitle("Atención")
                                .setMessage(apiResponse.mensaje)
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                    if (ventaListener != null) {
                                        ventaListener.onVentaListener();
                                        dialogReversion.dismiss();
                                    }
                                }).show();
                    }

                } catch (Exception e) {

                    Toast.makeText(context, "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((PrincipalActivity) context).mostrarLoading(false);
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void enviarCorreo(ListarVentasResponse item) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("¿Está seguro de enviar por correo al asegurado?");
        builder.setMessage("Correo: " + item.FacturaMaestroCorreoCliente);

        // Crear un LinearLayout para contener los botones y establecer la orientación vertical
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Establecer márgenes para el LinearLayout (padding)
        int padding = 16;  // Puedes ajustar este valor según tus necesidades
        layout.setPadding(padding, padding, padding, padding);

        // Crear el botón "ENVIAR"
        Button enviarButton = new Button(context);
        enviarButton.setText("ENVIAR");
        enviarButton.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde
        enviarButton.setTextColor(Color.WHITE);
        enviarButton.setAllCaps(false);  // Para que el texto no esté en mayúsculas
        enviarButton.setOnClickListener(v -> {
            // Lógica para enviar el correo
            notificacion(item);
            //Toast.makeText(context, "Correo enviado a: " + item.FacturaMaestroCorreoCliente, Toast.LENGTH_SHORT).show();
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
        layout.addView(enviarButton);
        layout.addView(cancelarButton);

        // Establecer el layout como vista del diálogo
        builder.setView(layout);
        dialogNotificacion = builder.create();
        dialogNotificacion.show();

        cancelarButton.setOnClickListener(v -> dialogNotificacion.dismiss());
    }

    private void solicitarReversion(ListarVentasResponse item,String motivo){

        Map<String, Object> parametros = new HashMap<>();
        Map<String, Object> oArchivosAdjuntos = new HashMap<>();
        oArchivosAdjuntos.put("nombre", "");
        oArchivosAdjuntos.put("imagen_base_64", "JVBERi0xLjQKJVNpbXVsYXRlZCBQREYgY29udGVudAolJUVPRg==");
        oArchivosAdjuntos.put("extension", ".PDF");
        oArchivosAdjuntos.put("descripcion", "");

        oArchivosAdjuntos.put("ArchivoTipoContenido", "application/pdf");

        parametros.put("t_poliza_maestro_fk", item.PolDetTPolizaMaestroFk);
        parametros.put("comentario", motivo);
        parametros.put("t_par_emi_reversion_motivo_fk", 1);
        parametros.put("o_archivos_adjuntos", oArchivosAdjuntos);


        ApiService apiService = new ApiService(context);
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_EMISION_REVERTIR;
        ((PrincipalActivity) context).mostrarLoading(true);
        dialogReversion.dismiss();
        apiService.solicitudPost(url, parametros, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ((PrincipalActivity) context).mostrarLoading(false);
                try {
                    Type responseType = new TypeToken<ApiResponse<Void>>() {
                    }.getType();
                    ApiResponse<Void> apiResponse = new Gson().fromJson(response, responseType);;



                    if (!apiResponse.exito) {
                        dialogReversion.show();
                        new AlertDialog.Builder(context)
                                .setTitle("Atención")
                                .setMessage(apiResponse.mensaje)
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                }).show();
                    } else {

                        new AlertDialog.Builder(context)
                                .setTitle("Atención")
                                .setMessage(apiResponse.mensaje)
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                    if (ventaListener != null) {
                                        ventaListener.onVentaListener();
                                        dialogReversion.dismiss();
                                    }
                                }).show();
                    }

                } catch (Exception e) {

                    Toast.makeText(context, "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((PrincipalActivity) context).mostrarLoading(false);
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void notificacion(ListarVentasResponse item){

        Map<String, Object> parametros = new HashMap<>();


        parametros.put("correo_cliente", item.FacturaMaestroCorreoCliente);
        parametros.put("t_poliza_detalle_fk", item.PolDetSecuencialFk);



        ApiService apiService = new ApiService(context);
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_EMISION_NOTIFICACION;
        ((PrincipalActivity) context).mostrarLoading(true);
        dialogNotificacion.dismiss();
        apiService.solicitudPost(url, parametros, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ((PrincipalActivity) context).mostrarLoading(false);
                try {
                    Type responseType = new TypeToken<ApiResponse<Void>>() {
                    }.getType();
                    ApiResponse<Void> apiResponse = new Gson().fromJson(response, responseType);;



                    if (!apiResponse.exito) {
                        dialogNotificacion.show();
                        new AlertDialog.Builder(context)
                                .setTitle("Atención")
                                .setMessage(apiResponse.mensaje)
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                }).show();
                    } else {

                        new AlertDialog.Builder(context)
                                .setTitle("Correcto")
                                .setMessage(apiResponse.mensaje)
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                        dialogNotificacion.dismiss();
                                }).show();
                    }

                } catch (Exception e) {

                    Toast.makeText(context, "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((PrincipalActivity) context).mostrarLoading(false);
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void dialogSolicitarReversion(ListarVentasResponse item) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);


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
        aceptarButton.setOnClickListener(v -> {
            String motivo = motivoInput.getText().toString().trim();
            if (!TextUtils.isEmpty(motivo)) {
                // Lógica para aceptar la reversión con el motivo
                solicitarReversion(item,motivo);
               // dialogReintentarPago.dismiss();
                //Toast.makeText(context, "Reversión aceptada. Motivo: " + motivo, Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(context, "Por favor ingrese un motivo", Toast.LENGTH_SHORT).show();
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
        //dialogReversion = builder.create();
        builder.setView(layout);

        builder.setView(layout);

        builder.setTitle("REVERSIÓN");
        dialogReversion = builder.create();
        dialogReversion.show();

        cancelarButton.setOnClickListener(v -> dialogReversion.dismiss());
    }

    private void mostrarDialogoFacturacion(int tPlanPagoDetalleFk) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_soatc_facturacion, null);

        // Referencias UI
        spinnerTipoDocumento = view.findViewById(R.id.spinnerTipoDocumento);
        etNumeroDocumento = view.findViewById(R.id.etNumeroDocumento);
        etExtensionDocumento = view.findViewById(R.id.etExtensionDocumento);
        etNombreRazonSocial = view.findViewById(R.id.etNombreRazonSocial);
        etEmail = view.findViewById(R.id.etEmail);
        etCelular = view.findViewById(R.id.etCelular);
        btnAtras = view.findViewById(R.id.btnAtras);
        btnSiguiente = view.findViewById(R.id.btnSiguiente);
        tvTitulo = view.findViewById(R.id.tvTitulo);
        layoutExtensionDocumento = view.findViewById(R.id.layoutExtensionDocumento);

        layoutExtensionDocumento.setVisibility(View.GONE);

        obtenerDocumentosIdentidadFacturacion();
        spinnerTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                if (item instanceof ParametricaGenerica) {
                    ParametricaGenerica seleccionado = (ParametricaGenerica) item;
                    if (seleccionado.Identificador == 1) {
                        layoutExtensionDocumento.setVisibility(View.VISIBLE);
                    } else {
                        layoutExtensionDocumento.setVisibility(View.GONE);
                        etExtensionDocumento.setText("");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // Botón Atrás



        dialogReintentarPago = new android.app.AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create();
        btnAtras.setText("Cancelar");
        btnSiguiente.setText("Efectivizar");
        btnAtras.setOnClickListener(v -> {
            dialogReintentarPago.dismiss();
        });
        btnSiguiente.setOnClickListener(v -> {
            if (validarFormulario()) {
                EmitirPago(tPlanPagoDetalleFk);
            }
        });

        dialogReintentarPago.show();
    }
    private boolean validarFormulario() {
        String numeroDoc = etNumeroDocumento.getText().toString().trim();
        String nombre = etNombreRazonSocial.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String celular = etCelular.getText().toString().trim();

        if (numeroDoc.isEmpty()) {
            etNumeroDocumento.setError("Ingrese número de documento");
            return false;
        }

        if (nombre.isEmpty()) {
            etNombreRazonSocial.setError("Ingrese nombre o razón social");
            return false;
        }

        if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Correo inválido");
            return false;
        }

        if (celular.isEmpty()) {
            etCelular.setError("Ingrese número de celular");
            return false;
        }

        if (!celular.isEmpty() && celular.length() < 8) {
            etCelular.setError("Ingrese un número válido (mínimo 8 dígitos)");
            return false;
        }
        String numeroDocumento = etNumeroDocumento.getText().toString();
        if (!ValidarCampo.validarNumeroEntero(numeroDocumento)) {
            etNumeroDocumento.setError("El número de documento debe ser un número entero válido");
            return false;
        }

        return true;
    }
    private void obtenerDocumentosIdentidadFacturacion() {
        List<ParametricaGenerica> documentosIdentidadFacturacion = ParametricasCache.getInstance().getDocumentosIdentidadFacturacionFacturacion();

        if (documentosIdentidadFacturacion != null && !documentosIdentidadFacturacion.isEmpty()) {
            ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(
                    context, android.R.layout.simple_spinner_item, documentosIdentidadFacturacion);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTipoDocumento.setAdapter(adapter);

        }
    }
    private void reintentarPago(ListarVentasResponse item) {
        mostrarDialogoFacturacion(item.PlaPagDetSecuencial);

    }
    private void EmitirPago(int tPlanPagoDetalleFk){
        dialogReintentarPago.dismiss();
        int tipoDocumento = spinnerTipoDocumento.getSelectedItemPosition() + 1;
        String numeroDocumento = etNumeroDocumento.getText().toString().trim();
        String extensionDocumento = etExtensionDocumento.getText().toString().trim();
        String nombreRazonSocial = etNombreRazonSocial.getText().toString().trim();
        String correoCliente = etEmail.getText().toString().trim();
        String telefonoCliente = etCelular.getText().toString().trim();

        Map<String, Object> parametros = new HashMap<>();
        Map<String, Object> e_datos_facturacion = new HashMap<>();
        e_datos_facturacion.put("fact_tipo_doc_identidad_fk", tipoDocumento);
        e_datos_facturacion.put("fact_nit_ci", numeroDocumento);
        e_datos_facturacion.put("fact_ci_complemento", extensionDocumento);
        e_datos_facturacion.put("fact_razon_social", nombreRazonSocial);
        e_datos_facturacion.put("fact_correo_cliente", correoCliente);
        e_datos_facturacion.put("fact_telefono_cliente", telefonoCliente);


        parametros.put("t_plan_pago_detalle_fk", tPlanPagoDetalleFk);
        parametros.put("t_par_medio_pago_fk", 1);//efectivo
        parametros.put("e_datos_facturacion", e_datos_facturacion);

        ApiService apiService = new ApiService(context);
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_COBRANZAS_RECIBO_EMITIR;
        ((PrincipalActivity) context).mostrarLoading(true);

        apiService.solicitudPost(url, parametros, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ((PrincipalActivity) context).mostrarLoading(false);
                try {
                    Type responseType = new TypeToken<ApiResponse<Void>>() {
                    }.getType();
                    ApiResponse<Void> apiResponse = new Gson().fromJson(response, responseType);;


                    if (apiResponse.exito) {
                        new AlertDialog.Builder(context)
                                .setTitle("Atención")
                                .setMessage(apiResponse.mensaje)
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                    if (ventaListener != null) {
                                        ventaListener.onVentaListener();
                                    }
                                }).show();
                    } else {

                        new AlertDialog.Builder(context)
                                .setTitle("Atención")
                                .setMessage(apiResponse.mensaje)
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                    dialogReintentarPago.show();
                                }).show();
                    }

                } catch (Exception e) {

                    Toast.makeText(context, "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((PrincipalActivity) context).mostrarLoading(false);
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
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
    //impresion
    private void iniciarImprimir() {

        ((AppCompatActivity) context).runOnUiThread(() -> {
            //  contadorImprimir++;

            new Thread(() -> {
                try {
                    imprimirFactura.imprimirFactura2();
                } catch (ImpresoraErrorException e) {
                    e.printStackTrace();
                    // errorImp = true;
                    mostrarMensaje("Error en la impresora.");
                    //contadorImprimir = cantDocsImprimir;
                } catch (NoHayPapelException e) {
                    e.printStackTrace();
                    //errorImp = true;
                    mostrarMensaje("No hay papel para imprimir.");
                    //contadorImprimir = cantDocsImprimir;
                } catch (VoltageBajoException e) {
                    e.printStackTrace();
                    //errorImp = true;
                    mostrarMensaje("Error de batería baja. No podrá imprimir con la batería baja.");
                    //contadorImprimir = cantDocsImprimir;
                } catch (ErrorPapelException e) {
                    e.printStackTrace();
                    //errorImp = true;
                    mostrarMensaje("Error en la impresora");
                    //contadorImprimir = cantDocsImprimir;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ///errorImp = true;
                    mostrarMensaje("Error al imprimir los datos. Si esto persiste, comuníquese con el encargado.");
                    //  contadorImprimir = cantDocsImprimir;
                }
            }).start();
        });

    }
    private void mostrarMensaje(final String mensaje) {
        try {
            new AlertDialog.Builder(context)
                    .setTitle("Atención")
                    .setMessage(mensaje)
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        dialog.dismiss();
                    }).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void terminoDeImprimir() {
        ((AppCompatActivity) context).runOnUiThread(() -> {

            preguntarAbrir();

        });
    }
    private void preguntarAbrir() {
        try {
            Date fechaImpresion = Calendar.getInstance().getTime();
            ControladorSqlite2 controlador = new ControladorSqlite2(context);
            com.emizor.univida.modelo.dominio.univida.seguridad.User user = controlador.obtenerUsuario();
            controlador.cerrarConexion();

            imprimirFactura.procesarColillaVentaSoatc(user, respuestaEfectivizacion, fechaImpresion);

            DialogoEmizor dialogoEmizor = new DialogoEmizor();
            dialogoEmizor.setTipoDialogo(7);
            dialogoEmizor.setMensaje("Imprimir Colilla?");
            dialogoEmizor.setCancelable(false);
            dialogoEmizor.show(((AppCompatActivity) context).getSupportFragmentManager(), null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
