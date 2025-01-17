package com.emizor.univida.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.emizor.univida.R;
import com.emizor.univida.dialogo.DialogoEmizor;
import com.emizor.univida.modelo.dominio.univida.parametricas.Banco;
import com.emizor.univida.modelo.dominio.univida.parametricas.BancosRespUnivida;
import com.emizor.univida.modelo.dominio.univida.parametricas.Departamento;
import com.emizor.univida.modelo.dominio.univida.parametricas.DepartamentoRespUnivida;
import com.emizor.univida.modelo.dominio.univida.parametricas.Gestion;
import com.emizor.univida.modelo.dominio.univida.parametricas.GestionRespUnivida;
import com.emizor.univida.modelo.dominio.univida.parametricas.MedioPago;
import com.emizor.univida.modelo.dominio.univida.parametricas.MedioPagoRespUnivida;
import com.emizor.univida.modelo.dominio.univida.parametricas.TipoDocumentoIdentidad;
import com.emizor.univida.modelo.dominio.univida.parametricas.TipoDocumentosIdentidadRespUnivida;
import com.emizor.univida.modelo.dominio.univida.parametricas.TipoPlaca;
import com.emizor.univida.modelo.dominio.univida.parametricas.TipoPlacasRespUnivida;
import com.emizor.univida.modelo.dominio.univida.parametricas.TipoVehiculo;
import com.emizor.univida.modelo.dominio.univida.parametricas.TipoVehiculoRespUnivida;
import com.emizor.univida.modelo.dominio.univida.parametricas.UsoVehiculo;
import com.emizor.univida.modelo.dominio.univida.parametricas.UsosVehiculoRespUnivida;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.ControladorTablas;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.rest.VolleySingleton;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LogUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ObtenerParametricasActivity extends AppCompatActivity implements DialogoEmizor.NotificaDialogEmizorListener {

    private static final String TAG = "OBTPARAMETRICAS";

    private View mProgressView;
    private View mLoginFormView;

    private ControladorSqlite2 controladorSqlite2;
    private String xLlave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obtener_parametricas);

        mLoginFormView = findViewById(R.id.vista_full_parametricas);
        mProgressView = findViewById(R.id.parametricas_progress);

        controladorSqlite2 = new ControladorSqlite2(this);

        String currentVersionName = "Sin Version Por acceso";
        try {
            // Datos locales
            LogUtils.d(TAG, "INFO APP");
            PackageInfo pckginfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersionName = "v" + pckginfo.versionName;


        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, "Ha habido un error con el packete :S", e);
        }

        ConfigEmizor.VERSION = currentVersionName;

        User user = controladorSqlite2.obtenerUsuario();

        if (user == null){
            controladorSqlite2.cerrarConexion();
            setResult(RESULT_CANCELED);
            ObtenerParametricasActivity.this.finish();
            return ;
        }
        mostrarProgressBar(true);
        if (user == null){
            setResult(RESULT_CANCELED);
            ObtenerParametricasActivity.this.finish();
            return;
        }

        xLlave = UtilRest.getInstance().procesarDatosInterno(user.getTokenAuth(), 1);


        obtenerTiposVehiculos();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                setResult(RESULT_CANCELED);
                ObtenerParametricasActivity.this.finish();

                break;
        }

        return true;
    }

    private void mostrarTexto(final String mensaje){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tvTexto = ((TextView)findViewById(R.id.tvCargaParametricasV3));
                String textoActual = tvTexto.getText().toString() + "\n\n" + mensaje;
                tvTexto.setText(textoActual);
            }
        });

    }

    private void limpiarTexto(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.tvCargaParametricasV3)).setText("");
            }
        });

    }

    private void mostrarMensaje(String mensaje){
        DialogoEmizor dialogoEmizor = new DialogoEmizor();
        dialogoEmizor.setTipoDialogo(4);
        dialogoEmizor.setMensaje(mensaje);
        dialogoEmizor.setCancelable(false);
        dialogoEmizor.show(getSupportFragmentManager(), null);
    }

    /**
     * @param mostrar
     */
    private void mostrarProgressBar(final boolean mostrar) {

        // creamos variable local int shortTiempoAnimacion y le asignamo el valor por defecto que tiene android
        int shortTiempoAnimacion = getResources().getInteger(android.R.integer.config_shortAnimTime);

        // seteamos la visibilidad del contenedor mLoguinFormView, en base al valor del parametro mostrar
        // si es true seteamos GONE si es falso seteamos VISIBLE
        mLoginFormView.setVisibility(mostrar ? View.GONE : View.VISIBLE);
        // animamos la visivilidad de mLoginFormView
        mLoginFormView.animate().setDuration(shortTiempoAnimacion).alpha(
                mostrar ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // ocultamos o mostramos el contenedor mLoginFormView, depende del valor de la variable cancel
                mLoginFormView.setVisibility(mostrar ? View.GONE : View.VISIBLE);
            }
        });

        // animamos la visivilidad de mProgressView
        mProgressView.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortTiempoAnimacion).alpha(
                mostrar ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // ocultamos o mostramos el contenedor mPorgressView, depende del valor de la variable cancel
                mProgressView.setVisibility(mostrar ? View.VISIBLE : View.GONE);
            }
        });

        if (! mostrar){

            limpiarTexto();

        }

    }

    private void obtenerTiposVehiculos(){

        mostrarTexto("Obteniendo Tipo de vehiculos ...");

        Log.i(TAG, "obtenerTiposVehiculos URL " + DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_TIPO_VEHICULO);
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_TIPO_VEHICULO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.i(TAG, "OK RESPONSE" + response);

                TipoVehiculoRespUnivida tipoVehiculoRespUnivida2;

                try{
                    tipoVehiculoRespUnivida2 = new Gson().fromJson(response, TipoVehiculoRespUnivida.class);
                }catch (Exception ex){
                    tipoVehiculoRespUnivida2 = null;
                    ex.printStackTrace();

                }

                if (tipoVehiculoRespUnivida2 == null){
                    mostrarMensaje("Datos nulos");
                    mostrarProgressBar(false);
                    return;
                }

                final TipoVehiculoRespUnivida tipoVehiculoRespUnivida = tipoVehiculoRespUnivida2;

                if (tipoVehiculoRespUnivida.getExito()) {
                    //ConfigEmizor.listaTipoVehiculo = tipoVehiculoRespUnivida.getMontoPrima();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mostrarTexto(tipoVehiculoRespUnivida.getMensaje());
                                    controladorSqlite2.eliminarTodoDatoTabla(ControladorTablas.TABLA_TIPO_VEHICULO);
                                    for (TipoVehiculo tipoVehiculo: tipoVehiculoRespUnivida.getDatos()){
                                        controladorSqlite2.insertarTipoVehiculo(tipoVehiculo);
                                    }

                                    controladorSqlite2.cerrarConexion();

                                    obtenerUsosVehiculos();
                                }
                            }).start();
                        }
                    });

                    //obtenerUsosVehiculos();

                }else{
                    //controladorSqlite2.eliminarTodoDatoTabla("usuario");
                    mostrarMensaje(tipoVehiculoRespUnivida.getMensaje());
                    mostrarProgressBar(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.i(TAG, "ERROR RESPONSE" + error);
                //controladorSqlite2.eliminarTodoDatoTabla("usuario");
                mostrarProgressBar(false);
                if (error != null){
                    if (error.getCause() instanceof TimeoutError){
                        mostrarMensaje(getString(R.string.mensaje_error_timeout));
                    } else {
//                        mostrarMensaje(getString(R.string.mensaje_error_volley) + error.getMessage());
                        mostrarMensaje("No tiene Conexión a INTERNET");
                    }
                }else{
                    mostrarMensaje(getString(R.string.mensaje_error_volley_default));
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                LogUtils.i(TAG, "++++++++++++ -------------");
                params.put("version-app-pax", ConfigEmizor.VERSION);

                if (xLlave != null) {

                    String xtoken = xLlave;

                    LogUtils.i(TAG, "getHeaders Enviando token :: " + xtoken);
                    params.put("Authorization", xtoken);
                }
                LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");

                return params;
            }

            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }
        };

        stringRequest.setShouldCache(false);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("tipovehiculo");

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void obtenerUsosVehiculos(){

        mostrarTexto("Obteniendo Usos vehiculo ...");
        Log.i(TAG, "obtenerUsosVehiculos URL " + DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_USO_VEHICULO);

        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_USO_VEHICULO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.i(TAG, "OK RESPONSE" + response);

                UsosVehiculoRespUnivida usosVehiculoRespUnivida2;

                try{
                    usosVehiculoRespUnivida2 = new Gson().fromJson(response, UsosVehiculoRespUnivida.class);
                }catch (Exception ex){
                    usosVehiculoRespUnivida2 = null;
                    ex.printStackTrace();
                }

                if (usosVehiculoRespUnivida2 == null){
                    mostrarMensaje("Datos nulos.");
                    mostrarProgressBar(false);
                    return;
                }
                final UsosVehiculoRespUnivida usosVehiculoRespUnivida = usosVehiculoRespUnivida2;
                if (usosVehiculoRespUnivida.getExito()) {

                    mostrarTexto(usosVehiculoRespUnivida.getMensaje());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    controladorSqlite2.eliminarTodoDatoTabla(ControladorTablas.TABLA_TIPO_USO);

                                    for (UsoVehiculo usoVehiculo : usosVehiculoRespUnivida.getDatos()){
                                        controladorSqlite2.insertarTipoUso(usoVehiculo);
                                    }

                                    controladorSqlite2.cerrarConexion();

                                    obtenerDepartamentos();
                                }
                            }).start();
                        }
                    });
                }else{
                    //controladorSqlite2.eliminarTodoDatoTabla("usuario");
                    mostrarMensaje(usosVehiculoRespUnivida.getMensaje());
                    mostrarProgressBar(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.i(TAG, "ERROR RESPONSE" + error);
                //controladorSqlite2.eliminarTodoDatoTabla("usuario");
                mostrarProgressBar(false);
                if (error != null){
                    if (error.getCause() instanceof TimeoutError){
                        mostrarMensaje(getString(R.string.mensaje_error_timeout));
                    } else {
//                        mostrarMensaje(getString(R.string.mensaje_error_volley) + error.getMessage());
                        mostrarMensaje("No tiene Conexión a INTERNET");
                    }
                }else{
                    mostrarMensaje(getString(R.string.mensaje_error_volley_default));
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");
                params.put("version-app-pax", ConfigEmizor.VERSION);

                if (xLlave != null) {

                    String xtoken = xLlave;

                    LogUtils.i(TAG, "getHeaders Enviando token :: " + xtoken);
                    params.put("Authorization", xtoken);
                }
                LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");

                return params;
            }

            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }
        };

        stringRequest.setShouldCache(false);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("usosvehiculo");

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void obtenerDepartamentos(){

        mostrarTexto("Obteniendo Departamentos ...");
        Log.i(TAG, "obtenerDepartamentos URL " + DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_DEPARTAMENTOS);

        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_DEPARTAMENTOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.i(TAG, "OK RESPONSE" + response);

                DepartamentoRespUnivida departamentoRespUnivida2;

                try{
                    departamentoRespUnivida2 = new Gson().fromJson(response, DepartamentoRespUnivida.class);
                }catch (Exception ex){
                    departamentoRespUnivida2 = null;
                    ex.printStackTrace();
                }

                if (departamentoRespUnivida2 == null){
                    mostrarMensaje("Datos nulos.");
                    mostrarProgressBar(false);
                    return;
                }

                final DepartamentoRespUnivida departamentoRespUnivida = departamentoRespUnivida2;
                if (departamentoRespUnivida.getExito()) {
                    mostrarTexto(departamentoRespUnivida.getMensaje());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    controladorSqlite2.eliminarTodoDatoTabla(ControladorTablas.TABLA_DEPARTAMENTOS);
                                    for (Departamento departamento : departamentoRespUnivida.getDatos()){
                                        controladorSqlite2.insertarDepartamento(departamento);
                                    }

                                    controladorSqlite2.cerrarConexion();

                                    obtenerGestion();
                                }
                            }).start();
                        }
                    });

                }else{
                    mostrarMensaje(departamentoRespUnivida.getMensaje());
                    mostrarProgressBar(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.i(TAG, "ERROR RESPONSE" + error);
                if (error != null){
                    if (error.getCause() instanceof TimeoutError){
                        mostrarMensaje(getString(R.string.mensaje_error_timeout));
                    } else {
//                        mostrarMensaje(getString(R.string.mensaje_error_volley) + error.getMessage());
                        mostrarMensaje("No tiene Conexión a INTERNET");
                    }
                }else{
                    mostrarMensaje(getString(R.string.mensaje_error_volley_default));
                }

                mostrarProgressBar(false);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");
                params.put("version-app-pax", ConfigEmizor.VERSION);


                if (xLlave != null) {

                    String xtoken = xLlave;

                    LogUtils.i(TAG, "getHeaders Enviando token :: " + xtoken);
                    params.put("Authorization", xtoken);
                }
                LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");

                return params;
            }

            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }
        };

        stringRequest.setShouldCache(false);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("departamento");

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void obtenerGestion(){

        mostrarTexto("Obteniendo Gestiones ...");

        Log.i(TAG, "obtenerGestion URL " + DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_GESTIONES);

        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_GESTIONES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.i(TAG, "OK RESPONSE" + response);

                GestionRespUnivida gestionRespUnivida2;

                try{

                    gestionRespUnivida2 = new Gson().fromJson(response, GestionRespUnivida.class);

                }catch (Exception ex){
                     gestionRespUnivida2 = null;
                    ex.printStackTrace();
                }

                if (gestionRespUnivida2 == null){
                    mostrarMensaje("Datos nulos.");
                    mostrarProgressBar(false);
                    return;
                }

                final GestionRespUnivida gestionRespUnivida = gestionRespUnivida2;

                if (gestionRespUnivida.getExito()) {

                    mostrarTexto(gestionRespUnivida.getMensaje());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarProgressBar(true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    controladorSqlite2.eliminarTodoDatoTabla(ControladorTablas.TABLA_GESTION);
                                    for (Gestion gestion : gestionRespUnivida.getDatos()){
                                        controladorSqlite2.insertarGestion(gestion);
                                    }

                                    obtenerMedioPago();
                                }
                            }).start();
                        }
                    });

                }else{
                    //controladorSqlite2.eliminarTodoDatoTabla("usuario");
                    mostrarMensaje(gestionRespUnivida.getMensaje());
                    mostrarProgressBar(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.i(TAG, "ERROR RESPONSE" + error);
                //controladorSqlite2.eliminarTodoDatoTabla("usuario");
                if (error != null){
                    if (error.getCause() instanceof TimeoutError){
                        mostrarMensaje(getString(R.string.mensaje_error_timeout));
                    } else {
//                        mostrarMensaje(getString(R.string.mensaje_error_volley) + error.getMessage());
                        mostrarMensaje("No tiene Conexión a INTERNET");
                    }
                }else{
                    mostrarMensaje(getString(R.string.mensaje_error_volley_default));
                }

                mostrarProgressBar(false);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");
                params.put("version-app-pax", ConfigEmizor.VERSION);


                if (xLlave != null) {

                    String xtoken = xLlave;

                    LogUtils.i(TAG, "getHeaders Enviando token :: " + xtoken);
                    params.put("Authorization", xtoken);
                }
                LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");

                return params;
            }

            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }
        };

        stringRequest.setShouldCache(false);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("gestion");

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void obtenerMedioPago(){

        mostrarTexto("Obteniendo Medios de Pago ...");

        Log.i(TAG, "obtenerGestion URL " + DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_MEDIO_PAGO);

        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_MEDIO_PAGO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.i(TAG, "OK RESPONSE" + response);

                MedioPagoRespUnivida medioPagoRespUnivida2;

                try{
                    medioPagoRespUnivida2= new Gson().fromJson(response, MedioPagoRespUnivida.class);
                }catch (Exception ex){
                    medioPagoRespUnivida2 = null;
                    ex.printStackTrace();
                }

                if (medioPagoRespUnivida2 == null){
                    mostrarMensaje("Datos nulos");
                    mostrarProgressBar(false);
                    return;
                }

                final MedioPagoRespUnivida medioPagoRespUnivida = medioPagoRespUnivida2;

                if (medioPagoRespUnivida.getExito()) {

                    mostrarTexto(medioPagoRespUnivida.getMensaje());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarProgressBar(true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    controladorSqlite2.eliminarTodoDatoTabla(ControladorTablas.TABLA_MEDIO_PAGO);
                                    for (MedioPago medioPago : medioPagoRespUnivida.getDatos()){
                                        controladorSqlite2.insertarMedioPago(medioPago);
                                    }

                                    obtenerBancos();
                                }
                            }).start();
                        }
                    });

                }else{

                    mostrarMensaje(medioPagoRespUnivida.getMensaje());
                    mostrarProgressBar(false);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.i(TAG, "ERROR RESPONSE" + error);

                if (error != null){
                    if (error.getCause() instanceof TimeoutError){
                        mostrarMensaje(getString(R.string.mensaje_error_timeout));
                    } else {
//                        mostrarMensaje(getString(R.string.mensaje_error_volley) + error.getMessage());
                        mostrarMensaje("No tiene Conexión a INTERNET");
                    }
                }else{
                    mostrarMensaje(getString(R.string.mensaje_error_volley_default));
                }

                mostrarProgressBar(false);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");
                params.put("version-app-pax", ConfigEmizor.VERSION);


                if (xLlave != null) {

                    String xtoken = xLlave;

                    LogUtils.i(TAG, "getHeaders Enviando token :: " + xtoken);
                    params.put("Authorization", xtoken);
                }
                LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");

                return params;
            }

            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }
        };

        stringRequest.setShouldCache(false);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("mediopago");

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void obtenerBancos(){

        mostrarTexto("Obteniendo Bancos ...");

        Log.i(TAG, "obtenerBancos URL " + DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_BANCOS);

        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_BANCOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.i(TAG, "OK RESPONSE" + response);

                BancosRespUnivida bancosRespUnivida2;

                try{

                    bancosRespUnivida2 = new Gson().fromJson(response, BancosRespUnivida.class);

                }catch (Exception ex){
                    bancosRespUnivida2 = null;
                    ex.printStackTrace();
                }

                if (bancosRespUnivida2 == null){
                    mostrarMensaje("Datos nulos.");
                    mostrarProgressBar(false);
                    return;
                }

                final BancosRespUnivida bancosRespUnivida = bancosRespUnivida2;

                if (bancosRespUnivida.getExito()) {

                    mostrarTexto(bancosRespUnivida.getMensaje());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarProgressBar(true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    controladorSqlite2.eliminarTodoDatoTabla(ControladorTablas.TABLA_BANCO);
                                    for (Banco banco : bancosRespUnivida.getDatos()){
                                        controladorSqlite2.insertarBanco(banco);
                                    }

                                    obtenerTipoPlacas();
                                }
                            }).start();
                        }
                    });

                }else{

                    mostrarMensaje(bancosRespUnivida.getMensaje());
                    mostrarProgressBar(false);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.i(TAG, "ERROR RESPONSE" + error);

                if (error != null){
                    if (error.getCause() instanceof TimeoutError){
                        mostrarMensaje(getString(R.string.mensaje_error_timeout));
                    } else {
//                        mostrarMensaje(getString(R.string.mensaje_error_volley) + error.getMessage());
                        mostrarMensaje("No tiene Conexión a INTERNET");
                    }
                }else{
                    mostrarMensaje(getString(R.string.mensaje_error_volley_default));
                }

                mostrarProgressBar(false);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");
                params.put("version-app-pax", ConfigEmizor.VERSION);


                if (xLlave != null) {

                    String xtoken = xLlave;

                    LogUtils.i(TAG, "getHeaders Enviando token :: " + xtoken);
                    params.put("Authorization", xtoken);
                }
                LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");

                return params;
            }

            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }
        };

        stringRequest.setShouldCache(false);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("banco");

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void obtenerTipoPlacas(){

        mostrarTexto("Obteniendo Tipo de Placas ...");

        Log.i(TAG, "obtenerTipoPlacas URL " + DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_TIPO_PLACAS);

        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_TIPO_PLACAS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.i(TAG, "OK RESPONSE" + response);

                TipoPlacasRespUnivida tipoPlacasRespUnivida2;

                try{
                    tipoPlacasRespUnivida2 = new Gson().fromJson(response, TipoPlacasRespUnivida.class);
                }catch (Exception ex){
                    tipoPlacasRespUnivida2 = null;
                    ex.printStackTrace();
                }

                if (tipoPlacasRespUnivida2 == null){
                    mostrarMensaje("Datos nulos");
                    mostrarProgressBar(false);
                    return;
                }

                final TipoPlacasRespUnivida tipoPlacasRespUnivida = tipoPlacasRespUnivida2;

                if (tipoPlacasRespUnivida.getExito()) {

                    mostrarTexto(tipoPlacasRespUnivida.getMensaje());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarProgressBar(true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    controladorSqlite2.eliminarTodoDatoTabla(ControladorTablas.TABLA_TIPO_PLACA);

                                    for (TipoPlaca tipoPlaca : tipoPlacasRespUnivida.getDatos()){
                                        controladorSqlite2.insertarTipoPlaca(tipoPlaca);
                                    }

                                    obtenerTipoDocumentosIdentidad();
                                }
                            }).start();
                        }
                    });

                }else{

                    mostrarMensaje(tipoPlacasRespUnivida.getMensaje());
                    mostrarProgressBar(false);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.i(TAG, "ERROR RESPONSE" + error);

                if (error != null){
                    if (error.getCause() instanceof TimeoutError){
                        mostrarMensaje(getString(R.string.mensaje_error_timeout));
                    } else {
//                        mostrarMensaje(getString(R.string.mensaje_error_volley) + error.getMessage());
                        mostrarMensaje("No tiene Conexión a INTERNET");
                    }
                }else{
                    mostrarMensaje(getString(R.string.mensaje_error_volley_default));
                }

                mostrarProgressBar(false);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");
                params.put("version-app-pax", ConfigEmizor.VERSION);


                if (xLlave != null) {

                    String xtoken = xLlave;

                    LogUtils.i(TAG, "getHeaders Enviando token :: " + xtoken);
                    params.put("Authorization", xtoken);
                }
                LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");

                return params;
            }

            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }
        };

        stringRequest.setShouldCache(false);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("tipoplaca");

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void obtenerTipoDocumentosIdentidad(){

        mostrarTexto("Obteniendo Tipo de documentos identidad ...");

        Log.i(TAG, "obtenerTipoDocumentosIdentidad URL " + DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_TIPO_DOC_IDENTIDAD);

        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_PARAMETRICAS_TIPO_DOC_IDENTIDAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.i(TAG, "OK RESPONSE" + response);

                TipoDocumentosIdentidadRespUnivida tipoDocumentosIdentidadRespUnivida2;

                try{
                    tipoDocumentosIdentidadRespUnivida2 = new Gson().fromJson(response, TipoDocumentosIdentidadRespUnivida.class);
                }catch (Exception ex){
                    tipoDocumentosIdentidadRespUnivida2 = null;
                    ex.printStackTrace();
                }

                if (tipoDocumentosIdentidadRespUnivida2 == null){
                    mostrarMensaje("Datos nulos");
                    mostrarProgressBar(false);
                    return;
                }

                final TipoDocumentosIdentidadRespUnivida tipoDocumentosIdentidadRespUnivida = tipoDocumentosIdentidadRespUnivida2;

                if (tipoDocumentosIdentidadRespUnivida.getExito()) {

                    mostrarTexto(tipoDocumentosIdentidadRespUnivida.getMensaje());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarProgressBar(true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    controladorSqlite2.eliminarTodoDatoTabla(ControladorTablas.TABLA_TIPO_DOCUMENTO_IDENTIDAD);

                                    for (TipoDocumentoIdentidad tipoDocumentoIdentidad : tipoDocumentosIdentidadRespUnivida.getDatos()){
                                        LogUtils.d(TAG, "insert data tipos documentos :: " + tipoDocumentoIdentidad + " json " + new Gson().toJson(tipoDocumentoIdentidad));
                                        controladorSqlite2.insertarTipoDocumentosIdentidad(tipoDocumentoIdentidad);
                                    }

                                    controladorSqlite2.cambiarEstado(2);

                                    setResult(RESULT_OK);
                                    ObtenerParametricasActivity.this.finish();
                                }
                            }).start();
                        }
                    });

                }else{

                    mostrarMensaje(tipoDocumentosIdentidadRespUnivida.getMensaje());
                    mostrarProgressBar(false);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.i(TAG, "ERROR RESPONSE" + error);

                if (error != null){
                    if (error.getCause() instanceof TimeoutError){
                        mostrarMensaje(getString(R.string.mensaje_error_timeout));
                    } else {
//                        mostrarMensaje(getString(R.string.mensaje_error_volley) + error.getMessage());
                        mostrarMensaje("No tiene Conexión a INTERNET");
                    }
                }else{
                    mostrarMensaje(getString(R.string.mensaje_error_volley_default));
                }

                mostrarProgressBar(false);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");
                params.put("version-app-pax", ConfigEmizor.VERSION);


                if (xLlave != null) {

                    String xtoken = xLlave;

                    LogUtils.i(TAG, "getHeaders Enviando token :: " + xtoken);
                    params.put("Authorization", xtoken);
                }
                LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");

                return params;
            }

            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }
        };

        stringRequest.setShouldCache(false);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("tipodocumentoidentidad");

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void finish() {
        if (controladorSqlite2 != null) {
            controladorSqlite2.cerrarConexion();
            controladorSqlite2 = null;
        }
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll("tipovehiculo");
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll("usosvehiculo");
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll("departamento");
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll("gestion");
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll("mediopago");
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll("banco");
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll("tipoplaca");
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll("tipodocumentoidentidad");

        super.finish();
    }

    @Override
    public void onRealizaAccionDialogEmizor(DialogoEmizor dialogoEmizor, int accion, int tipodialogo) {
        dialogoEmizor.getDialog().cancel();
        setResult(RESULT_CANCELED);
        finish();
    }

}
