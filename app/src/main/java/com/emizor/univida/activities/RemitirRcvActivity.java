package com.emizor.univida.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.emizor.univida.BuildConfig;
import com.emizor.univida.R;
import com.emizor.univida.dialogo.DialogoEmizor;
import com.emizor.univida.modelo.dominio.univida.RespUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.ArchivoAdjunto;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RemitirUnivida;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.rest.VolleySingleton;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LogUtils;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RemitirRcvActivity extends AppCompatActivity implements DialogoEmizor.NotificaDialogEmizorListener{

    private final String TAG = "REMITIRRCV";

    private String file;

    private ImageView ivIamgenDeposito;
    private File mi_foto;
    private User user;
    private String nombreArchivo;
    private View vistaProgresAgregarDeposito, vistaFullAgregarDeposito;
    private EditText etDescripcionRemitirRcv;

    private Integer secuencial;

    private boolean estadoRemitido;
    private BootstrapButton bsbAceptarAgregarDeposito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remitir_rcv);

        Bundle bundle = getIntent().getExtras();
        secuencial = null;
        if (bundle != null){
            if (bundle.containsKey("secuencial")){
                secuencial = bundle.getInt("secuencial");
            }
        }

        if (secuencial == null){
            setResult(RESULT_CANCELED);
            RemitirRcvActivity.this.finish();
            return;
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

        estadoRemitido = false;

        Toolbar toolbar = findViewById(R.id.toolbar_remitir_rcv);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        final BootstrapButton bsbCamaraAgregarDeposito;

        bsbCamaraAgregarDeposito = findViewById(R.id.bsbCamaraAgregarDepositoRemitirRcv);
        bsbAceptarAgregarDeposito = findViewById(R.id.bsbAceptarRemitirRcv);
        ivIamgenDeposito = findViewById(R.id.ivImagenDeposito);
        vistaProgresAgregarDeposito = findViewById(R.id.vista_progress_remitir_rcv);
        vistaFullAgregarDeposito = findViewById(R.id.vista_full_remitir);
        etDescripcionRemitirRcv = findViewById(R.id.etDescripcionRemitirRcv);

        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(this);

        user = controladorSqlite2.obtenerUsuario();
        controladorSqlite2.cerrarConexion();
        if (user == null){
            setResult(RESULT_CANCELED);
            RemitirRcvActivity.this.finish();
            return;
        }

        file = Environment.getExternalStorageDirectory() + "/Pictures/";

        bsbCamaraAgregarDeposito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarTiempo();
                if (nombreArchivo == null){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    nombreArchivo = "img" + "-" + simpleDateFormat.format(new Date()) + ".jpg";
                    file += nombreArchivo;
                }
                agregarFoto();
            }
        });

        bsbAceptarAgregarDeposito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bsbAceptarAgregarDeposito.isEnabled()){
                    bsbAceptarAgregarDeposito.setEnabled(false);
                    RemitirRcvActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            verificarTiempo();
                            if (mi_foto != null){
                                try {
                                    Bitmap photobmp = BitmapFactory.decodeFile(file);
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    photobmp.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                                    byte[] imageBytes = baos.toByteArray();
                                    String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                    remitir(encodedImage);
                                    return;

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }

                            mostrarMensaje("La fotografia del comprobante es requerido.");
                        }
                    });
                }

            }
        });


    }

    private void mostrarMensaje(final String mensaje){
        try {
            RemitirRcvActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogoEmizor dialogoEmizor = new DialogoEmizor();
                    dialogoEmizor.setTipoDialogo(4);
                    dialogoEmizor.setMensaje(mensaje);
                    dialogoEmizor.setCancelable(false);
                    dialogoEmizor.show(getSupportFragmentManager(), null);
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void verificarTiempo(){
        RemitirRcvActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("pref_datos",Context.MODE_PRIVATE);

                long timeInicio = sharedPreferences.getLong("fecha_hora_inicio", 0);

                long timeFechaActual = Calendar.getInstance().getTimeInMillis();

                long tiempomls = timeFechaActual - timeInicio;

                LogUtils.i(TAG, "time inicio " + timeInicio + " time fechaactual " + timeFechaActual + " tiem rest " + tiempomls);

                if (tiempomls > ConfigEmizor.TIEMPO_ESPERA_CIERRE_SESION){

                    ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(RemitirRcvActivity.this);

                    controladorSqlite2.eliminarTodoPrincipal();
                    controladorSqlite2.crearTablasPrincipal();

                    setResult(RESULT_CANCELED);

                    RemitirRcvActivity.this.finish();

                }else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putLong("fecha_hora_inicio", Calendar.getInstance().getTimeInMillis());
                    editor.apply();
                    editor.commit();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        verificarTiempo();
        switch (item.getItemId()) {
            case android.R.id.home:

                setResult(RESULT_CANCELED);
                RemitirRcvActivity.this.finish();

                break;
        }

        return true;
    }

    public void agregarFoto(){

        // Check whether the user has granted us the READ/WRITE_EXTERNAL_STORAGE permissions
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request both READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE so that the
            // Pushy SDK will be able to persist the device token in the external storage
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request both CAMERA and WRITE_EXTERNAL_STORAGE so that the
            // Pushy SDK will be able to persist the device token in the external storage
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        } else {

            mi_foto = new File(file);
            try {
                mi_foto.createNewFile();
            } catch (IOException ex) {
                Log.e("ERROR ", "Error:" + ex);
            }
            Uri outputFileUri = null;
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                outputFileUri = FileProvider.getUriForFile(RemitirRcvActivity.this, BuildConfig.APPLICATION_ID+".provider", mi_foto);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                outputFileUri = Uri.fromFile(mi_foto);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, 1500);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        verificarTiempo();

        if (requestCode == 1500 && resultCode == RESULT_OK){
            actualizarImagen();
        }else{
            mi_foto = null;
        }

    }

    private void actualizarImagen(){

        if (ivIamgenDeposito != null)
            ivIamgenDeposito.setImageDrawable(Drawable.createFromPath(file));
        if (bsbAceptarAgregarDeposito != null)
            bsbAceptarAgregarDeposito.setEnabled(true);
    }

    /**
     * @param mostrar
     */
    private void showProgress(final boolean mostrar) {
        RemitirRcvActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                verificarTiempo();
                // creamos variable local int shortTiempoAnimacion y le asignamo el valor por defecto que tiene android
                int shortTiempoAnimacion = getResources().getInteger(android.R.integer.config_shortAnimTime);

                // seteamos la visibilidad del contenedor mLoguinFormView, en base al valor del parametro mostrar
                // si es true seteamos GONE si es falso seteamos VISIBLE
                vistaFullAgregarDeposito.setVisibility(mostrar ? View.GONE : View.VISIBLE);
                // animamos la visivilidad de mLoginFormView
                vistaFullAgregarDeposito.animate().setDuration(shortTiempoAnimacion).alpha(
                        mostrar ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // ocultamos o mostramos el contenedor mLoginFormView, depende del valor de la variable cancel
                        vistaFullAgregarDeposito.setVisibility(mostrar ? View.GONE : View.VISIBLE);
                    }
                });

                // animamos la visivilidad de mProgressView
                vistaProgresAgregarDeposito.setVisibility(mostrar ? View.VISIBLE : View.GONE);
                vistaProgresAgregarDeposito.animate().setDuration(shortTiempoAnimacion).alpha(
                        mostrar ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // ocultamos o mostramos el contenedor mPorgressView, depende del valor de la variable cancel
                        vistaProgresAgregarDeposito.setVisibility(mostrar ? View.VISIBLE : View.GONE);
                    }
                });
            }
        });


    }

    @Override
    public void onRealizaAccionDialogEmizor(DialogoEmizor dialogoEmizor, int accion, int tipodialogo) {
        if (estadoRemitido){
            Intent intent = new Intent();

            intent.putExtra("secuencial", secuencial);

            setResult(RESULT_OK, intent);
            RemitirRcvActivity.this.finish();
        }
        dialogoEmizor.getDialog().cancel();
    }

    public void remitir(String encodeImagen){
        //view.setVisibility(View.GONE);

        showProgress(true);

            RemitirUnivida remitirUnivida = new RemitirUnivida();

            ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();

            archivoAdjunto.setDescripcion(etDescripcionRemitirRcv.getText().toString());
            archivoAdjunto.setExtension(mi_foto.getName().substring(mi_foto.getName().indexOf("."), mi_foto.getName().length()));
            archivoAdjunto.setImagenBase64(encodeImagen);
            archivoAdjunto.setNombre(mi_foto.getName());

            remitirUnivida.setArchivoAdjunto(archivoAdjunto);
            remitirUnivida.setGestionFk(-1);
            remitirUnivida.setRcvSecuencial(secuencial);
            remitirUnivida.setVentaVendedor(user.getUsername());



            final String parametrosJson3 = remitirUnivida.toString();
            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();
        LogUtils.i(TAG, " size " + parametrosJson3.length());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_RCV_REMITIR, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

//                    bsbAceptarAgregarDeposito.setEnabled(true);

                    LogUtils.i(TAG, "OK RESPONSE " + response);

                    RespUnivida respUnivida = null;

                    try{

                        respUnivida = new Gson().fromJson(response, RespUnivida.class);

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                    if (respUnivida != null){

                        if (respUnivida.getExito()){

                            estadoRemitido = true;
                        }

                        mostrarMensaje(respUnivida.getMensaje());

                        showProgress(false);
                    }else{
                        showProgress(false);
                        mostrarMensaje("Los datos estan vacion, comuniquese con el administrador.");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

//                    bsbAceptarAgregarDeposito.setEnabled(true);

                    LogUtils.i(TAG, "FAIL RESPONSE " + error);
                    showProgress(false);
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

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS_IMG, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            stringRequest.setTag("remitirrcv");

            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


    }

    @Override
    public void finish() {
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll("remitirrcv");
        super.finish();
    }
}
