package com.emizor.univida.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.emizor.univida.R;
import com.emizor.univida.fragmento.HistorialQrGeneradosFragment;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.ventas.ObtenerVentaUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.QrGenerado;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.rest.VolleySingleton;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LoadingDialog;
import com.emizor.univida.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistorialQrAdapter extends ArrayAdapter<QrGenerado> {
    private User user;
    private Context context;
    private List<QrGenerado> qrGenerados;
    private LoadingDialog loadingDialog = new LoadingDialog();
    private Handler handler = new Handler();
    private boolean isEfectivizado = false;
    private HistorialQrGeneradosFragment fragment;

    public HistorialQrAdapter(Context context, List<QrGenerado> qrGenerados, HistorialQrGeneradosFragment fragment) {
        super(context, R.layout.item_historial_qr, qrGenerados);
        this.context = context;
        this.qrGenerados = qrGenerados;
        this.fragment = fragment;

        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getContext());
        user = controladorSqlite2.obtenerUsuario();
        controladorSqlite2.cerrarConexion();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_historial_qr, parent, false);
        }


        QrGenerado qrGenerado = qrGenerados.get(position);
        int tVehiSoatPropFk = qrGenerado.getTVehiSoatPropFk();


        TextView placaTextView = convertView.findViewById(R.id.textViewPlaca);
        TextView gestionTextView = convertView.findViewById(R.id.gestionTextView);
        TextView fechaTextView = convertView.findViewById(R.id.textViewFecha);
        TextView estadoTextView = convertView.findViewById(R.id.textViewEstado);
        TextView fechaEstadoTextView = convertView.findViewById(R.id.textViewFechaEstado);
        TextView efectivizadoTextView = convertView.findViewById(R.id.textViewEfectivizado);
        TextView mensajeTextView = convertView.findViewById(R.id.textViewMensaje);
        TextView importeTextView = convertView.findViewById(R.id.importeTextView);

        Button btnConsultarEstado = convertView.findViewById(R.id.btnConsultarEstado);
        Button btnAnular = convertView.findViewById(R.id.btnAnular);
        Button btnComprobante = convertView.findViewById(R.id.btnComprobante);


        placaTextView.setText(qrGenerado.getIdentificadorVehiculo());
        gestionTextView.setText("Gestión: " +qrGenerado.getGestion());
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
        importeTextView.setText("Importe: Bs. "+String.valueOf(qrGenerado.getImporte()));
        mensajeTextView.setText(qrGenerado.getMensajeEfectivizacion());
        int estadoFk = qrGenerado.getTParSimpleEstadoSolicitudFk();
        btnConsultarEstado.setVisibility(View.GONE);
        btnAnular.setVisibility(View.GONE);
        btnComprobante.setVisibility(View.GONE);
        switch (estadoFk) {
            case 1:
                btnConsultarEstado.setVisibility(View.VISIBLE);
                break;
            case 2:
                btnConsultarEstado.setVisibility(View.VISIBLE);
                btnAnular.setVisibility(View.VISIBLE);
                break;
//            case 3:
//                btnComprobante.setVisibility(View.VISIBLE);
//                break;
        }
        int tramiteSecuencialQr = qrGenerado.getTramiteSecuencial();
        btnConsultarEstado.setOnClickListener(v -> {

            consultarEstadoQr(tramiteSecuencialQr);
        });
        btnAnular.setOnClickListener(v -> {
            anularQr(tramiteSecuencialQr);
        });


        return convertView;
    }

    private void consultarEstadoQr(int tramiteSecuencialQr) {
        /************consultar Qr********************/
        loadingDialog.showLoading(getContext(), "Consultando estado QR...");


        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("codigo_unico", tramiteSecuencialQr);
            jsonRequest.put("tipo_tramite", 4);//POS

            JSONObject seguridadExterna = new JSONObject();
            seguridadExterna.put("seg_ext_usuario", "");
            seguridadExterna.put("seg_ext_token", 0);

            jsonRequest.put("seguridad_externa", seguridadExterna);

            JSONObject transaccionOrigen = new JSONObject();
            transaccionOrigen.put("tra_ori_intermediario", 0);
            transaccionOrigen.put("tra_ori_entidad", "000");
            transaccionOrigen.put("tra_ori_sucursal", "Oficina Nacional");
            transaccionOrigen.put("tra_ori_agencia", "");
            transaccionOrigen.put("tra_ori_canal", 28);
            transaccionOrigen.put("tra_ori_cajero", user.getDatosUsuario().getEmpleadoUsuario());

            jsonRequest.put("transaccion_origen", transaccionOrigen);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("consultarQr", e.toString());
        }

        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_QR_CONSULTAR;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean exito = jsonResponse.getBoolean("exito");
                        String mensaje = jsonResponse.optString("mensaje", "Operación completada.");
                        JSONObject datos = jsonResponse.getJSONObject("datos");
                        String estadoDescripcion = datos.getString("EstadoDescripcion");
                        int estadoSecuencial = datos.getInt("EstadoSecuencial");
                        int secuencialPago = datos.getInt("Secuencial");
                        if (exito) {
                            ((Activity) context).runOnUiThread(() -> {

                                loadingDialog.hideLoading();
                                if (estadoSecuencial == 3) {

                                    mostrarMensajeConAccion(mensaje, () -> {

                                        iniciarVerificacionEfectivizacion(secuencialPago);

                                    });
                                } else {
                                    new AlertDialog.Builder(context)
                                            .setTitle("Aviso")
                                            .setMessage(mensaje)
                                            .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                                            .show();

                                }

                            });


                        } else {

                            loadingDialog.hideLoading();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loadingDialog.hideLoading();

                        Log.e("consultarQr", e.toString());
                        ((Activity) context).runOnUiThread(() -> Toast.makeText(getContext(), "Error al procesar la respuesta.", Toast.LENGTH_LONG).show());
                    }
                },
                error -> manejarError(error)) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return obtenerCuerpoSolicitud(jsonRequest.toString());
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return construirEncabezados();

            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        stringRequest.setShouldCache(false);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS_IMG, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("efectivizarVenta");

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
        /**********Fin consultar QR*/

    }

    private void manejarError(VolleyError error) {
        loadingDialog.hideLoading();
        LogUtils.i("consultar", "On error response: " + error);
    }

    private byte[] obtenerCuerpoSolicitud(String parametrosJson) {
        if (parametrosJson != null) {

            LogUtils.i("consultar", "getBody Enviando parametros :: " + parametrosJson);
            String enviarJson;

            try {

                enviarJson = parametrosJson.trim();

                LogUtils.i("consultar", "getBody Enviando parametros encryp :: " + enviarJson);

                return enviarJson.getBytes("utf-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        LogUtils.i("consultar", "||||||||||||||||||||||           ***************************");
        return new byte[0];
    }

    private void mostrarMensajeConAccion(final String mensaje, final Runnable accionAceptar) {
        try {
            ((Activity) context).runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(mensaje)
                        .setCancelable(false)
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            if (accionAceptar != null) {
                                accionAceptar.run(); // Ejecuta la acción adicional si se define
                            }
                            dialog.dismiss(); // Cierra el diálogo
                        });

                AlertDialog alert = builder.create();
                alert.show();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void iniciarVerificacionEfectivizacion(int secuencialPago) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                VerificarEfectivizacion(secuencialPago);
                if (!isEfectivizado) {
                    handler.postDelayed(this, 5000);
                }
            }
        }, 0);
    }

    private void VerificarEfectivizacion(int secuencialPago) {
        //verificar efectivización
        loadingDialog.showLoading(getContext(), "Verificando efectivización...");

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("tqr_solicitud_fk", secuencialPago);

            JSONObject seguridadExterna = new JSONObject();
            seguridadExterna.put("seg_ext_usuario", "");
            seguridadExterna.put("seg_ext_token", 0);

            jsonRequest.put("seguridad_externa", seguridadExterna);

            JSONObject transaccionOrigen = new JSONObject();
            transaccionOrigen.put("tra_ori_intermediario", 0);
            transaccionOrigen.put("tra_ori_entidad", "000");
            transaccionOrigen.put("tra_ori_sucursal", "Oficina Nacional");
            transaccionOrigen.put("tra_ori_agencia", "");
            transaccionOrigen.put("tra_ori_canal", 28);
            transaccionOrigen.put("tra_ori_cajero", user.getDatosUsuario().getEmpleadoUsuario());

            jsonRequest.put("transaccion_origen", transaccionOrigen);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Efectivizacion", e.toString());
        }

        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_QR_CONSULTAR_EFECTIVIZACION;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean exito = jsonResponse.getBoolean("exito");
                        String mensaje = jsonResponse.optString("mensaje", "Operación completada.");
                        JSONObject datos = jsonResponse.getJSONObject("datos");
                        int tParSimpleEstadoEjecucionFk = datos.getInt("TParSimpleEestadoSolicitudEjecucionFk");
                        String mensajeEfectivizacion = datos.getString("Mensaje");
                        int tVehiSoatPropFk = datos.getInt("TVehiSoatPropFk");


                        if (exito) {

                            ((Activity) context).runOnUiThread(() -> {
                                //mostrarTexto(mensajeEfectivizacion);
                                View rootView = ((Activity) context).findViewById(android.R.id.content);
                                Log.e("Efectivizacion", mensajeEfectivizacion.toString());
                                loadingDialog.hideLoading();
                                if (mensajeEfectivizacion.trim() != "")
                                    Snackbar.make(rootView, mensajeEfectivizacion, Snackbar.LENGTH_SHORT).show();

                            });
                            if (tParSimpleEstadoEjecucionFk == 2) {
                                isEfectivizado = true;
                                handler.removeCallbacksAndMessages(null);
                                fragment.obtenerHistorial();
                                Log.e("Efectivizacion", mensajeEfectivizacion.toString());
                                loadingDialog.hideLoading();
                                //imprimirComprobanteYFactura(tVehiSoatPropFk);
                                new AlertDialog.Builder(context)
                                        .setTitle("Aviso")
                                        .setMessage(mensaje)
                                        .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                                        .show();
                            }


                        } else {
                            ((Activity) context).runOnUiThread(() -> {
                                //mostrarTexto(mensaje);
                                Log.e("Efectivizacion", mensaje.toString());
                                View rootView = ((Activity) context).findViewById(android.R.id.content);
                                loadingDialog.hideLoading();
                                if (mensaje.trim() != "")
                                    Snackbar.make(rootView, mensaje, Snackbar.LENGTH_LONG).show();
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Efectivizacion", e.toString());
                        ((Activity) context).runOnUiThread(() -> Toast.makeText(getContext(), "Error al procesar la respuesta.", Toast.LENGTH_LONG).show());
                    }
                },
                error -> manejarError(error)) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return obtenerCuerpoSolicitud(jsonRequest.toString());
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return construirEncabezados();

            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        stringRequest.setShouldCache(false);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS_IMG, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("Efectivizacion");

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

        /**********Fin verificar efectivización*/
    }
    private void anularQr(int tramiteSecuencialQr) {
        /************anular Qr********************/
        loadingDialog.showLoading(getContext(), "Anulando QR...");

        //mostrarMensaje("Anulando codigo QR");

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("codigo_unico", tramiteSecuencialQr);
            jsonRequest.put("tipo_tramite", 4);//POS

            JSONObject seguridadExterna = new JSONObject();
            seguridadExterna.put("seg_ext_usuario", "");
            seguridadExterna.put("seg_ext_token", 0);

            jsonRequest.put("seguridad_externa", seguridadExterna);

            JSONObject transaccionOrigen = new JSONObject();
            transaccionOrigen.put("tra_ori_intermediario", 0);
            transaccionOrigen.put("tra_ori_entidad", "000");
            transaccionOrigen.put("tra_ori_sucursal", "Oficina Nacional");
            transaccionOrigen.put("tra_ori_agencia", "");
            transaccionOrigen.put("tra_ori_canal", 28);
            transaccionOrigen.put("tra_ori_cajero", user.getDatosUsuario().getEmpleadoUsuario());

            jsonRequest.put("transaccion_origen", transaccionOrigen);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("AnularQR", e.toString());
        }

        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_QR_ANULAR;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean exito = jsonResponse.getBoolean("exito");
                        String mensaje = jsonResponse.optString("mensaje", "Operación completada.");

                        if (exito) {
                            ((Activity) context).runOnUiThread(() -> {
                                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
                                fragment.obtenerHistorial();
                                loadingDialog.hideLoading();

                            });
                        } else {

                            loadingDialog.hideLoading();

                            ((Activity) context).runOnUiThread(() -> Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show());


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loadingDialog.hideLoading();

                        Log.e("AnularQR", e.toString());
                        ((Activity) context).runOnUiThread(() -> Toast.makeText(getContext(), "Error al procesar la respuesta.", Toast.LENGTH_LONG).show());
                    }
                },
                error -> manejarError(error)) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return obtenerCuerpoSolicitud(jsonRequest.toString());
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return construirEncabezados();

            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        stringRequest.setShouldCache(false);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS_IMG, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("efectivizarVenta");

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
        /**********Fin anular QR*/
    }



    private Map<String, String> construirEncabezados() {
        Map<String, String> params = new HashMap<>();
        params.put("version-app-pax", ConfigEmizor.VERSION);

        if (user.getTokenAuth() != null) {
            String xtoken = UtilRest.getInstance().procesarDatosInterno(user.getTokenAuth(), 1);
            params.put("Authorization", xtoken);
        }
        return params;
    }
}
