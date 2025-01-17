package com.emizor.univida.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.emizor.univida.R;
import com.emizor.univida.adapter.ListaPagosRcvAdapter;
import com.emizor.univida.adapter.RecyclerViewOnItemCickListener;
import com.emizor.univida.dialogo.DialogoEmizor;
import com.emizor.univida.excepcion.ErrorPapelException;
import com.emizor.univida.excepcion.ImpresoraErrorException;
import com.emizor.univida.excepcion.NoHayPapelException;
import com.emizor.univida.excepcion.VoltageBajoException;
import com.emizor.univida.imprime.ImprimirAvisoListener;
import com.emizor.univida.imprime.ImprimirFactura;
import com.emizor.univida.modelo.dominio.univida.RespUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.EfectivizarRcvUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RcvJsonMedioPagoUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RcvListarVentaRespUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RcvVentaDatos;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.rest.VolleySingleton;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LogUtils;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EfectivizarRcvActivity extends AppCompatActivity implements ImprimirAvisoListener, DialogoEmizor.NotificaDialogEmizorListener  {

    private final String TAG = "EFECTIVIZARRCV";

    private List<RcvJsonMedioPagoUnivida> listaMedioPagos;

    private RecyclerView rvListaPagosRcv;
    private LinearLayoutManager linearLayoutManager;
    private ListaPagosRcvAdapter listaPagosRcvAdapter;

    private User user;

    private View vistaFull, vistaProgress;

    private ImprimirFactura imprimirFactura;

    private RcvListarVentaRespUnivida rcvListarVentaRespUnivida;
    private RcvJsonMedioPagoUnivida rcvJsonMedioPagoUnividaSeleccionado;
    private boolean estadoEfectivo;

    private BigDecimal montoPrimaTotalPagos;

    private BootstrapButton btnEfectivizarRcv, btnAgregarPago;
    private EfectivizarRcvUnivida efectivizarRcvUnivida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_efectivizar_rcv);

        rcvListarVentaRespUnivida = null;

        listaMedioPagos = new ArrayList<>();
        montoPrimaTotalPagos = new BigDecimal(0);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            if (bundle.containsKey("listarventarcv")){

                rcvListarVentaRespUnivida = new Gson().fromJson(bundle.getString("listarventarcv"), RcvListarVentaRespUnivida.class);

            }
        }

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
        verificarTiempo();
        Toolbar toolbar = findViewById(R.id.toolbar_efectivizar_rcv);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        if (rcvListarVentaRespUnivida == null){
            setResult(RESULT_CANCELED);
            EfectivizarRcvActivity.this.finish();
            return;
        }

        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(this);
        user = controladorSqlite2.obtenerUsuario();

        controladorSqlite2.cerrarConexion();

        if (user == null){
            setResult(RESULT_CANCELED);
            EfectivizarRcvActivity.this.finish();
            return ;
        }

        btnEfectivizarRcv = findViewById(R.id.btnEfectivizarRcv);
        btnAgregarPago = findViewById(R.id.btnAgregarPago);

        estadoEfectivo = false;

        listaPagosRcvAdapter = new ListaPagosRcvAdapter(this, new ArrayList<RcvJsonMedioPagoUnivida>(), new RecyclerViewOnItemCickListener() {
            @Override
            public void onClick(View view, int position, int accionTipo) {

                rcvJsonMedioPagoUnividaSeleccionado = listaPagosRcvAdapter.getRcvJsonMedioPagoUnivida(position);

                abrirMenuItems();
            }
        });

        ((TextView) findViewById(R.id.tvMontoEfectivizarRcv)).setText("PRIMA(Bs): " + String.valueOf(rcvListarVentaRespUnivida.getDatos().getFormularioImporte()));

        RcvVentaDatos rcvVentaDatos = rcvListarVentaRespUnivida.getDatos();

        String cantidades = "Cantidad Total: " + rcvVentaDatos.getCantidad() + "\nCantidad validos: " + rcvVentaDatos.getCantidadValidos() + "\nCantidad Anulados: " + rcvVentaDatos.getCantidadAnulados() +
                "\nCantidad Revertidos: " + rcvVentaDatos.getCantidadRevertidos();

        String fecharr = rcvListarVentaRespUnivida.getFechaRcv();

        cantidades += "\nRcv Fecha: " + fecharr.substring(8, 10) + "/" + fecharr.substring(5,7) + "/" + fecharr.substring(0, 4);

        ((TextView) findViewById(R.id.tvCantidadesEfectivizarRcv)).setText(cantidades);

        rvListaPagosRcv = findViewById(R.id.rvListaPagosRcv);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvListaPagosRcv.setLayoutManager(linearLayoutManager);

        rvListaPagosRcv.setAdapter(listaPagosRcvAdapter);

        vistaFull= findViewById(R.id.vista_full_efectivizar_rcv);
        vistaProgress = findViewById(R.id.vista_progress_efectivizar_rcv);

        imprimirFactura = ImprimirFactura.obtenerImpresora(getApplicationContext());
        imprimirFactura.setAvisoListener(this);

        btnEfectivizarRcv.setEnabled(false);

        efectivizarRcvUnivida = null;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                setResult(RESULT_CANCELED);
                EfectivizarRcvActivity.this.finish();

                break;
        }

        return true;
    }

    /**
     * @param mostrar
     */
    private void mostrarProgressBar(final boolean mostrar) {
        try {

            EfectivizarRcvActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    verificarTiempo();
                    // creamos variable local int shortTiempoAnimacion y le asignamo el valor por defecto que tiene android
                    int shortTiempoAnimacion = getResources().getInteger(android.R.integer.config_shortAnimTime);

                    // seteamos la visibilidad del contenedor mLoguinFormView, en base al valor del parametro mostrar
                    // si es true seteamos GONE si es falso seteamos VISIBLE
                    vistaFull.setVisibility(mostrar ? View.GONE : View.VISIBLE);
                    // animamos la visivilidad de mLoginFormView
                    vistaFull.animate().setDuration(shortTiempoAnimacion).alpha(
                            mostrar ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // ocultamos o mostramos el contenedor mLoginFormView, depende del valor de la variable cancel
                            vistaFull.setVisibility(mostrar ? View.GONE : View.VISIBLE);
                        }
                    });

                    // animamos la visivilidad de mProgressView
                    vistaProgress.setVisibility(mostrar ? View.VISIBLE : View.GONE);
                    vistaProgress.animate().setDuration(shortTiempoAnimacion).alpha(
                            mostrar ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // ocultamos o mostramos el contenedor mPorgressView, depende del valor de la variable cancel
                            vistaProgress.setVisibility(mostrar ? View.VISIBLE : View.GONE);
                        }
                    });
                }
            });

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }


    private void mostrarMensaje(final String mensaje){
        try {
            EfectivizarRcvActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogoEmizor dialogoEmizor = new DialogoEmizor();
                    dialogoEmizor.setTipoDialogo(4);
                    dialogoEmizor.setMensaje(mensaje);
                    dialogoEmizor.setCancelable(false);
                    dialogoEmizor.show(EfectivizarRcvActivity.this.getSupportFragmentManager(), null);
                }
            });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void efectivizarRcv(View view){
        if (listaMedioPagos.size() <= 0){
            mostrarMensaje("Debe adicionar por lo menos un medio de pago. ");
            return;
        }

        if (btnEfectivizarRcv.isEnabled() && efectivizarRcvUnivida == null){

            btnEfectivizarRcv.setEnabled(false);
            btnAgregarPago.setEnabled(false);

            if (efectivizarRcvUnivida == null)
            EfectivizarRcvActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    efectivizarRcvUnivida = new EfectivizarRcvUnivida();

                    efectivizarRcvUnivida.setGestionFk(-1);
                    efectivizarRcvUnivida.setRcvCantidad(rcvListarVentaRespUnivida.getDatos().getCantidad());
                    efectivizarRcvUnivida.setRcvImporte(rcvListarVentaRespUnivida.getDatos().getFormularioImporte().doubleValue());
                    efectivizarRcvUnivida.setRcvJsonMedioPago(new Gson().toJson(listaMedioPagos));
                    efectivizarRcvUnivida.setSucursalFk(user.getDatosUsuario().getSucursalCodigo());
                    efectivizarRcvUnivida.setVentaFecha(rcvListarVentaRespUnivida.getFechaRcv());
                    efectivizarRcvUnivida.setVentaVendedor(user.getUsername());

                    mostrarProgressBar(true);

                    final String parametrosJson3 = efectivizarRcvUnivida.toString();
                    VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_RCV_EFECTIVIZAR, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //btnEfectivizarRcv.setEnabled(true);

                            LogUtils.i(TAG, "OK RESPONSE " + response);

                            RespUnivida respUnivida = null;


                            try{

                                respUnivida = new Gson().fromJson(response, RespUnivida.class);

                                LogUtils.i(TAG, "OK CONVERSION " + new Gson().toJson(respUnivida));

                            }catch (Exception ex){
                                ex.printStackTrace();
                            }

                            if (respUnivida != null){

                                if (respUnivida.getExito()) {

                                    try {

                                        imprimirFactura.prepararImpresionRcv(user, efectivizarRcvUnivida, String.valueOf(respUnivida.getDatos()), rcvListarVentaRespUnivida);

                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                        mostrarMensaje("No se puede imprimir los datos por que algunos o todos son nulos.");
                                        return;
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {

                                                        imprimirFactura.imprimirFactura2();

                                                    } catch (ImpresoraErrorException e) {
                                                        e.printStackTrace();
                                                        mostrarMensaje("Error en la impresora.");
                                                        mostrarProgressBar(false);
                                                    } catch (NoHayPapelException e) {
                                                        e.printStackTrace();
                                                        mostrarMensaje("No hay papel para imprimir.");
                                                        mostrarProgressBar(false);
                                                    } catch (VoltageBajoException e) {
                                                        e.printStackTrace();
                                                        mostrarMensaje("Error de Bateria baja. No podra imprimir con la bateria baja.");
                                                        mostrarProgressBar(false);
                                                    } catch (ErrorPapelException e) {
                                                        e.printStackTrace();
                                                        mostrarMensaje("Error en la impresora");
                                                        mostrarProgressBar(false);
                                                    }catch (Exception ex){
                                                        ex.printStackTrace();
                                                        mostrarMensaje("Error al imprimir los datos. Si esto persiste comuniquese con el encargado.");
                                                        mostrarProgressBar(false);
                                                    }
                                                }
                                            }).start();
                                        }
                                    });

                                }else{
                                    mostrarProgressBar(false);
                                }

                                mostrarMensaje(respUnivida.getMensaje());

                            }else{


                                mostrarMensaje("Los datos estan vacion, comuniquese con el administrador.");
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {

                            btnEfectivizarRcv.postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                    btnEfectivizarRcv.setEnabled(true);

//                                    efectivizarRcvUnivida = null;

                                    LogUtils.i(TAG, "FAIL RESPONSE " + error);
                                    mostrarProgressBar(false);
                                    if (error != null){
                                        if (error.getCause() instanceof TimeoutError){
                                            mostrarMensaje(getString(R.string.mensaje_error_timeout));
                                        } else {
//                                        mostrarMensaje(getString(R.string.mensaje_error_volley) + error.getMessage());
                                            mostrarMensaje("No tiene Conexión a INTERNET");
                                        }
                                    }else{
                                        mostrarMensaje(getString(R.string.mensaje_error_volley_default));
                                    }
                                }
                            }, 3000);

                        }
                    }) {

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            if (parametrosJson3 != null) {

                                LogUtils.i(TAG, "getBody Enviando parametros :: " + parametrosJson3);
                                String enviarJson;

                                try {

                                    enviarJson = parametrosJson3.trim();

                                    LogUtils.i(TAG, "getBody Enviando parametros encryp :: " + enviarJson);

                                    return enviarJson.getBytes("utf-8");

                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                            LogUtils.i(TAG, "||||||||||||||||||||||           ***************************");
                            return new byte[0];
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {

                            Map<String, String> params = new HashMap<>();
                            LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");
                            params.put("version-app-pax", ConfigEmizor.VERSION);


                            if (user.getTokenAuth() != null) {

                                String xtoken = UtilRest.getInstance().procesarDatosInterno(user.getTokenAuth(), 1);

                                LogUtils.i(TAG, "getHeaders Enviando autorization :: " + xtoken);
                                params.put("Authorization", xtoken);
                            }
                            LogUtils.i(TAG, "++++++++++++++++++++++++ ---------------------------");

                            return params;
                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }
                    };

                    stringRequest.setShouldCache(false);

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    stringRequest.setTag("nuevorcv");

                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }
            });

        }

    }

    public void adicionarMedioPago(View view){

        Intent intent = new Intent(getApplicationContext(), AMMedioPagoRCVActivity.class);

        intent.putExtra("estado_efectivo", estadoEfectivo);
        intent.putExtra("total_prima",(rcvListarVentaRespUnivida.getDatos().getFormularioImporte().doubleValue() - montoPrimaTotalPagos.doubleValue()));
        startActivityForResult(intent, 5841);

    }


    @Override
    public void terminoDeImprimir() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mostrarProgressBar(false);

                setResult(RESULT_OK);
                EfectivizarRcvActivity.this.finish();
            }
        });
    }

    private void abrirMenuItems(){
        verificarTiempo();
        if (rcvJsonMedioPagoUnividaSeleccionado != null ) {

                BottomSheetMenuDialog dialog = new BottomSheetBuilder(EfectivizarRcvActivity.this, R.style.AppTheme_BottomSheetDialog_Custom)
                        .setMode(BottomSheetBuilder.MODE_LIST)
                        .addItem(502,"EDITAR", R.drawable.ic_action_editar)
                        .addItem(503, "QUITAR", R.drawable.ic_action_eliminar)
                        .setItemClickListener(new BottomSheetItemClickListener() {
                            @Override
                            public void onBottomSheetItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case 502:

                                        Intent intent = new Intent(getApplicationContext(), AMMedioPagoRCVActivity.class);

                                        intent.putExtra("jsonpago", new Gson().toJson(rcvJsonMedioPagoUnividaSeleccionado));
                                        intent.putExtra("estado_efectivo", estadoEfectivo);

                                        BigDecimal bigDecimal = montoPrimaTotalPagos.subtract(new BigDecimal(rcvJsonMedioPagoUnividaSeleccionado.getMedioDePagoPrima()));
                                        intent.putExtra("total_prima",(rcvListarVentaRespUnivida.getDatos().getFormularioImporte().doubleValue() - bigDecimal.doubleValue()));

                                        startActivityForResult(intent, 5841);
                                        break;
                                    case 503:

                                        if (rcvJsonMedioPagoUnividaSeleccionado.getMedioDePagoFk() == 1){
                                            estadoEfectivo = false;
                                        }

                                        montoPrimaTotalPagos = montoPrimaTotalPagos.subtract(new BigDecimal(rcvJsonMedioPagoUnividaSeleccionado.getMedioDePagoPrima()));

                                            listaMedioPagos.remove(rcvJsonMedioPagoUnividaSeleccionado);

                                        break;
                                }
                            }
                        })
                        .createDialog();

                dialog.show();

        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        verificarTiempo();
        if (requestCode == 5841){
            if (resultCode == RESULT_OK){

                Bundle bundle = data.getExtras();

                if (bundle != null){
                    if (bundle.containsKey("jsonpago")){
                        final RcvJsonMedioPagoUnivida rcvJsonMedioPagoUnivida = new Gson().fromJson(bundle.getString("jsonpago"), RcvJsonMedioPagoUnivida.class);

                        //identificador del medio de pago en este caso 1 corresponde a Efectivo
                        if (rcvJsonMedioPagoUnivida.getMedioDePagoFk() == 1 && rcvJsonMedioPagoUnivida.getNumero() == -1){
                            if (!estadoEfectivo) {

                                estadoEfectivo = true;
                            }else{
                                mostrarMensaje("Ya existe un medio de pago Efectivo.");
                                return;
                            }
                        }

                        boolean existe = false;

                        if (rcvJsonMedioPagoUnivida.getNumero() == -1) {

                            for (RcvJsonMedioPagoUnivida rcvJsonMedioPagoUnivida22 : listaMedioPagos) {
                                LogUtils.i(TAG, rcvJsonMedioPagoUnivida.toString() + " 111 \n2222 " + rcvJsonMedioPagoUnivida22.toString());
                                if (rcvJsonMedioPagoUnivida22.getMedioDePagoFk().equals(rcvJsonMedioPagoUnivida.getMedioDePagoFk())) {
                                    if ((! rcvJsonMedioPagoUnivida22.getMedioDePagoDato().isEmpty()) && rcvJsonMedioPagoUnivida22.getMedioDePagoDato().equals(rcvJsonMedioPagoUnivida.getMedioDePagoDato())) {
                                        existe = true;
                                        break;
                                    }
                                }
                            }
                        }

                        if (existe){
                            if (rcvJsonMedioPagoUnivida.getMedioDePagoFk() == 3){
                                mostrarMensaje("El número de transferencia del pago ya existe.");
                            }else if (rcvJsonMedioPagoUnivida.getMedioDePagoFk() == 5) {
                                mostrarMensaje("El número de deposito del pago ya existe.");
                            }else{
                                mostrarMensaje("La referencia del pago ya existe.");
                            }
                        }else {

                            if (rcvJsonMedioPagoUnivida.getNumero() > 0) {
                                listaMedioPagos.set(rcvJsonMedioPagoUnivida.getNumero() - 1, rcvJsonMedioPagoUnivida);
                            } else {
                                listaMedioPagos.add(rcvJsonMedioPagoUnivida);
                            }

                            montoPrimaTotalPagos = new BigDecimal(0);
                            rvListaPagosRcv.post(new Runnable() {
                                @Override
                                public void run() {

                                    for (int i = 0; i < listaMedioPagos.size(); i++) {

                                        listaMedioPagos.get(i).setNumero((i + 1));

                                        montoPrimaTotalPagos = new BigDecimal(listaMedioPagos.get(i).getMedioDePagoPrima()).add(montoPrimaTotalPagos);

                                    }

                                    if (montoPrimaTotalPagos.doubleValue() == rcvListarVentaRespUnivida.getDatos().getFormularioImporte().doubleValue()){
                                        btnEfectivizarRcv.setEnabled(true);
                                    }else{
                                        btnEfectivizarRcv.setEnabled(false);
                                    }

                                    listaPagosRcvAdapter.cambiarLista(listaMedioPagos);
                                }
                            });
                        }

                    }
                }

            }
        }
    }

    @Override
    public void onRealizaAccionDialogEmizor(DialogoEmizor dialogoEmizor, int accion, int tipodialogo) {
        verificarTiempo();
        dialogoEmizor.getDialog().cancel();
    }

    private void verificarTiempo(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("pref_datos",Context.MODE_PRIVATE);

                long timeInicio = sharedPreferences.getLong("fecha_hora_inicio", 0);

                long timeFechaActual = Calendar.getInstance().getTimeInMillis();

                long tiempomls = timeFechaActual - timeInicio;

                LogUtils.i(TAG, "time inicio " + timeInicio + " time fechaactual " + timeFechaActual + " tiem rest " + tiempomls);

                if (tiempomls > ConfigEmizor.TIEMPO_ESPERA_CIERRE_SESION){

                    ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(EfectivizarRcvActivity.this);

                    controladorSqlite2.eliminarTodoPrincipal();
                    controladorSqlite2.crearTablasPrincipal();
                    setResult(RESULT_CANCELED);

                    EfectivizarRcvActivity.this.finish();

                }else{

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putLong("fecha_hora_inicio", Calendar.getInstance().getTimeInMillis());
                    editor.apply();
                    editor.commit();

                }
            }
        });

    }
}
