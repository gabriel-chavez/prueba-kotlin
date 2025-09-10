package com.emizor.univida.rest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.util.ConfigEmizor;

import java.util.HashMap;
import java.util.Map;

public class ApiService {

    private Context context;
    private User user;

    public ApiService(Context context) {
        this.context = context;
        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(context);
        user = controladorSqlite2.obtenerUsuario();
        controladorSqlite2.cerrarConexion();
    }


    public void solicitudPost(String url, final Map<String, String> parametros,
                                     final Response.Listener<String> onSuccess,
                                     final Response.ErrorListener onError) {
        mostrarProgressBar(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mostrarProgressBar(false);
                        onSuccess.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mostrarProgressBar(false);
                        manejarErrorGenerico(error);
                        if (onError != null) {
                            onError.onErrorResponse(error);
                        }
                    }
                }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                return parametros;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return obtenerHeaders();
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }
        };

        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("solicitudAPI");

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
    public void solicitudGet(String url, final Response.Listener<String> onSuccess, final Response.ErrorListener onError) {
        mostrarProgressBar(true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mostrarProgressBar(false);
                        onSuccess.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mostrarProgressBar(false);
                        manejarErrorGenerico(error);
                        if (onError != null) {
                            onError.onErrorResponse(error);
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return obtenerHeaders();
            }
        };

        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("solicitudGET");

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
    private Map<String, String> obtenerHeaders() {
        Map<String, String> headers = new HashMap<>();
        if (user.getTokenAuth() != null) {
            String xtoken = UtilRest.getInstance().procesarDatosInterno(user.getTokenAuth(), 1);
            headers.put("Authorization", xtoken);
        }
        return headers;
    }
    // Manejo genérico de errores
    private void manejarErrorGenerico(VolleyError error) {
        if (error != null) {
            if (error.networkResponse != null) {
                // Error del servidor (HTTP 500, etc.)
                int statusCode = error.networkResponse.statusCode;
                mostrarMensaje("Error del servidor: " + statusCode);
            } else if (error.getCause() instanceof TimeoutError) {
                // Timeout de conexión
                mostrarMensaje("Tiempo de espera agotado. Revise su conexión a internet.");
            } else if (error.getCause() instanceof NoConnectionError) {
                // Error por falta de conexión
                mostrarMensaje("No tiene conexión a internet.");
            } else if (error instanceof AuthFailureError) {
                // Error de autenticación (por ejemplo, token inválido)
                mostrarMensaje("Error de autenticación. Por favor inicie sesión de nuevo.");
            } else {
                // Otros tipos de errores
                mostrarMensaje("Error desconocido: " + error.getMessage());
            }
        } else {
            mostrarMensaje("Error desconocido en la conexión.");
        }
    }

    // Método para mostrar la barra de progreso
    private void mostrarProgressBar(boolean show) {

    }

    // Método para mostrar mensajes
    private void mostrarMensaje(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
