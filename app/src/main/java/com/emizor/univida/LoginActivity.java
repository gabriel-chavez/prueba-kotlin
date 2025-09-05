package com.emizor.univida;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.emizor.univida.activities.ObtenerParametricasActivity;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.activities.RootActivity;
import com.emizor.univida.dialogo.DialogoEmizor;
import com.emizor.univida.modelo.dominio.univida.seguridad.LoginRespUnivida;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.Conexion;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.rest.VolleySingleton;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LogUtils;
import com.emizor.univida.util.actualizar.Autoupdater;
import com.emizor.univida.utils.AidlUtil;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends RootActivity implements DialogoEmizor.NotificaDialogEmizorListener {

    private static final String TAG = "LOGIN123";
    /**
     * Lleve un registro de la tarea de inicio de sesión para asegurarse de que podemos cancelarla si se solicita.
     */
    //private UserLoginTask mAuthTask = null;

    // UI referencias
    // Variable Campo de texto etUsuario que contendra el nombre del usuario
    private BootstrapEditText etUsuario;
    // Variable Campo de texto etClave que contendra la clave del usuario
    private BootstrapEditText etClave;
    // Variable vista o contenedor mProgressView contendra al progressBar
    private View mProgressView;
    // Variable vista o contenedor mLoginFormView que contendra el formulario de logueo de la aplicacion
    private View mLoginFormView;
    // Variable vista o contenedor llFondo que contendra un progress y buscando un
    private View llFondo;
    // variable boton btnLoguear
    private BootstrapButton btnLoguear;

    private TextView tvActualizar;

    private boolean estadoClick;

    private ControladorSqlite2 controladorSqlite2;

    // objeto para realizar las actualizaciones
    private Autoupdater updater;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // configuracion para que el teclado no se muestre en pantalla cuando se visualice la interfaz por pantalla
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //bloqueamos la tecla HOME
        this.getWindow().addFlags(0x80000000);

        //registramos los iconos para el modulo de AndroidBootstrap
        TypefaceProvider.registerDefaultIconSets();
        // cargamos la interfaz grafica en pantalla
        setContentView(R.layout.activity_login);

        // Check whether the user has granted us the READ/WRITE_EXTERNAL_STORAGE permissions
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request both READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE so that the
            // Pushy SDK will be able to persist the device token in the external storage
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        // obtenemos un manejador de la BD local.
        controladorSqlite2 = new ControladorSqlite2(this);

        String currentVersionName = "Sin Version Por acceso";
        try {
            // Datos locales
            LogUtils.d(TAG, "INFO APP");
            PackageInfo pckginfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            //obtenemos la version actual de la aplicacion
            currentVersionName = "v" + pckginfo.versionName;


        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, "Ha habido un error con el packete :S", e);
        }

        // seteamos la version actual en la varaible VERSION(poublica y estatica).
        ConfigEmizor.VERSION = currentVersionName;

        // enlazamos la variable etUsuario de la interfaz con la variable etUsuario del codigo
        etUsuario = (BootstrapEditText) findViewById(R.id.etUsuario);
        // Agregamos Listener(Para Manejar Un Evento) OnEditorActionListener al campo TextView etUsuario
        etUsuario.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                Log.i("informacion 1"," id " + id + " editor info ime null " + EditorInfo.IME_NULL + " Ime done " + EditorInfo.IME_ACTION_DONE + " otros " + keyEvent);
                // Verifivamos si el evento que el id produce es del campo etUsuario o el id es igual al IME_NULL
                if (id == R.id.etUsuario || id == EditorInfo.IME_NULL) {
                    // llamamos al metodod intentarLogin para verificar los campos y validar en el servidor los datos enviados.
                    //intentarLogin();
                    etClave.requestFocus();
                    // retornamos true para que android no haga nada
                    return true;
                    // fin del if-si
                }
                // retorna falso para que continue las acciones por defecto
                return false;
            }// fin de la funcion
        });

        // Agregamos un Listener(Para Manejar un evento) OnKeyListener al campo TextView  etUsuario
        etUsuario.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Log.i("informacion 1"," id " + keyCode + " editor info ime null " + EditorInfo.IME_NULL + " Ime done " + EditorInfo.IME_ACTION_DONE + " otros " + event + " ke " + KeyEvent.KEYCODE_CALL);
                // verificamos si la tecla presionada es la de llamar(KEYCODE_CALL)
                if (KeyEvent.KEYCODE_CALL == keyCode) {
                    // llamamos al metodod intentarLogin para verificar los campos y validar en el servidor los datos enviados.
                    //intentarLogin();
                    etClave.requestFocus();
                    // retornamos true para que android no haga nada
                    return true;
                    // fin del if-si
                }
                // retorna falso para que continue las acciones por defecto
                return false;
            }
        });


        // enlazamos el campo TextView etClave de la interfaz con la variable etClave del Codigo
        etClave = (BootstrapEditText) findViewById(R.id.etClave);
        // agregamos un listener OnEditorActionListener
        etClave.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                //Log.i("informacion 2"," id " + id + " editor info ime null " + EditorInfo.IME_NULL + " Ime done " + EditorInfo.IME_ACTION_DONE + " otros " + keyEvent);
                // verficamos si el id es igual al id del campo etClave o si el id es igual al IME_NULL
                if (id == R.id.etClave || id == EditorInfo.IME_NULL) {
                    // llamamos al metodod intentarLogin para verificar los campos y validar en el servidor los datos enviados.
                    if (!estadoClick) {
                        intentarLogin();
                    }
                    // retornamos true para que android no haga nada
                    return true;
                    // fin del if-si
                }
                // retronamos falso para que siga con las acciones por defecto de android
                return false;
            }
        });
        // agregamos un listener OnKeyListener a etClave
        etClave.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Verficamos si se presiono la tecla llamar del dispositivo
                if (KeyEvent.KEYCODE_CALL == keyCode) {
                    // llamamos al metodo intentar login para verificar los campos y verificar los datos en el servidor
                    if (!estadoClick) {
                        intentarLogin();
                    }
                    // retornamos verdad para que android no haga nada mas
                    return true;
                    // fin if-si
                }
                // retornamos falso para que android continue con las acciones por defecto
                return false;
            }
        });

        //enlazamos el Button btnLoguear de la interfaz a la varable del codigo btnLoguear
        btnLoguear = (BootstrapButton) findViewById(R.id.btnLoguear);
        //agregamos el Listener OnClickListener a btnLoguear
        btnLoguear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // llamamos al metodo intentarLogin para validar los campos y validar los datos en el servidor
                if (!estadoClick) {
                    if(existePermisoLocacion()) {
                        if (checkLocation()) {
                            toggleGPSUpdates("init");
                            toggleNetworkUpdates("init");
                            intentarLogin();
                        } else {
                            verificarPermisoLocacion();
                            estadoClick = false;
                        }
                    } else {
                        verificarPermisoLocacion();
                        estadoClick = false;
                    }

                }
            }
        });

        // enlazamos el contenedor o vista login_form de la interfaz con la variable mLoginFormView del codigo
        mLoginFormView = findViewById(R.id.login_form);
        //enlazamos el contendor o vista login_progress de la interfaz con la variable mProgressView del codigo
        mProgressView = findViewById(R.id.login_progress);

        llFondo = findViewById(R.id.llFondo);

        // enlazamos el textview tvActualizar de la interfaz con la variable tvActualizar de esta actividad
        tvActualizar = findViewById(R.id.tvActualizar);

        //managerServicio = new ManagerServicioImpl(this, this);

        estadoClick = false;

        // para que se seleccione todo el contenido del campo de Usuario
        etUsuario.setSelectAllOnFocus(true);
        // para que se seleccione todo el contenido del campo de Contraseña
        etClave.setSelectAllOnFocus(true);

        // verificamos si tiene internet.
        if (Conexion.estaConectado(this)) {
            if (! AidlUtil.getInstance().isConnect()) {
                // llamamos al metodo comenzarActualizar que verificara si existe una actualizacion de la aplicacion
               // comenzarActualizar();
            }
        } else {
            // mostramos un mensaje de que no tiene internet
            mostrarMensaje(" No tiene conexión a internet.");
        }

        toggleGPSUpdates("iniciar");
        toggleNetworkUpdates("iniciar");
    }

    /**
     * Intenta iniciar sesión.
     * Si hay errores de formulario se presentan los errores y no se realiza ningún intento de inicio de sesión real.
     */
    private void intentarLogin() {

        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (! estadoClick) {
                    estadoClick = true;

                    // Reseteamos los mensajes de error de los campos.
                    etUsuario.setError(null);
                    etClave.setError(null);

                    //etClave.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etClave.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // Almacenar valores en el momento del intento de inicio de sesión.
                    final String strUsAux = etUsuario.getText().toString();
                    final String strClaveAux = etClave.getText().toString();


                    // variable cancel igual a falso(indica el estado de la validacion un true significa que no esta bien)
                    boolean cancel = false;
                    // variable focusview que apuntara al campo que este con errores
                    View focusView = null;

                    // verficamos si strClaveAux es una cadena vacia.
                    if (TextUtils.isEmpty(strClaveAux)) {
                        // seteamos el error en etClave
                        etClave.setError(getString(R.string.error_field_required));
                        // focusView apunta a la variable etClave
                        focusView = etClave;
                        // asignamos a cancel el valor de true
                        cancel = true;
                    }

                    // verificamos si strClaveAux es una clave valida.
                    if (!isClaveValida(strClaveAux)) {
                        // seteamos el error en etClave
                        etClave.setError(getString(R.string.error_invalid_password));
                        //focusView apunta a etClave
                        focusView = etClave;
                        // asignamos a cancel el valor de true
                        cancel = true;
                    }

                    // Verificamos que la variable strUsAux no sea una cadena Vacia.
                    if (TextUtils.isEmpty(strUsAux)) {
                        // seteamo el error en etUsuario
                        etUsuario.setError(getString(R.string.error_field_required));
                        // focusView apunta a etUsuario
                        focusView = etUsuario;
                        // asignamos el valor de true a cancel
                        cancel = true;
                    }

                    // verificamos si cancel es true o false
                    if (cancel) {
                        estadoClick = false;
                        // hay un error en los campor
                        // pasamos el foco al campo que apunta focusView
                        focusView.requestFocus();
                    } else {

                        // Mostrar la barra de progreso en pantalla
                        mostrarProgressBar(true);
                        if (Conexion.estaConectado(LoginActivity.this)) {
                            // creamos un objeto usuario

                            final User usuario = new User();

                            //seteamos los valores de username y clave
                            usuario.setUsername(strUsAux);
                            usuario.setPassword(strClaveAux);

                            try {

                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // con el try capturaremos errores
                                        try {
                                            // ingresamos un texto para indicar en que estado se encuentra el proceso de autenticar
                                            mostrarTexto("Verificando usuario ...");
                                            // validamos al usuario
                                            validarUsuario(new Gson().toJson(usuario));
                                        } catch (Exception e) {
                                            mostrarProgressBar(false);
                                            e.printStackTrace();
                                            estadoClick = false;
                                        }

                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                                estadoClick = false;
                            }

                        } else {
                            mostrarProgressBar(false);

                            mostrarMensaje(" No tiene conexión a internet.");

                            estadoClick = false;
                        }
                    }
                }
            }
        });

    }

    /**
     * <b>Metodo isClaveValida</b>, metodo para verifcar si la clave enviada de parametro es valida
     *
     * @param clave, string que contiene la clave a verificar
     * @return boolean, verdad si la clve cumple con las
     */
    private boolean isClaveValida(String clave) {
        //una verificacion a la clave, el tamaño de la cadena debe ser mayor a 4
        return clave.length() > 4;
    }


    /**
     * @param mostrar
     */
    private void mostrarProgressBar(final boolean mostrar) {

        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
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

                btnLoguear.setVisibility((mostrar)? View.GONE: View.VISIBLE);

                estadoClick = mostrar;

                if (! mostrar){

                    limpiarTexto();

                }
            }
        });


    }

    /**
     * <b>Metodo mostrarTexto</b>, metodo setear un texto en pantalla
     *
     * @param mensaje, string que contiene el texto a agregar a la pantalla
     */
    private void mostrarTexto(final String mensaje){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tvTexto = ((TextView)findViewById(R.id.tvCargaTextoV3));
                String textoActual = tvTexto.getText().toString() + "\n\n" + mensaje;
                tvTexto.setText(textoActual);
            }
        });

    }

    /**
     * <b>Metodo limpiarTexto</b>, metodo para limpiar el texto ingresado en pantalla
     */
    private void limpiarTexto(){
        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.tvCargaTextoV3)).setText("");
            }
        });

    }

    /**
     * <b>Metodo mostrarMensaje</b>, Muestra un mensaje en un Dialogo para informar al usuario.
     */
    private void mostrarMensaje(final String mensaje){
        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogoEmizor dialogoEmizor = new DialogoEmizor();
                dialogoEmizor.setTipoDialogo(4);
                dialogoEmizor.setMensaje(mensaje);
                dialogoEmizor.setCancelable(false);
                dialogoEmizor.show(getSupportFragmentManager(), null);
            }
        });

    }

    /**
     * <b>Metodo validarUsuario</b>, valida la informacion del usuario.
     * @param parametrosJson String que contiene los datos del usuario en formato json
     */
    private void validarUsuario(final String parametrosJson){

        // imprimimos por la consola la url de autenticar. La impresion en el logcat de android dependera si esta habilitado o no en la clase LogUtils.
        LogUtils.i(TAG, "autenticar URL " + DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_SEGURIDAD_AUNTENTICACION);
        Log.i("Login123","autenticar URL " + DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_SEGURIDAD_AUNTENTICACION);
        // vaciamos el cahce de volley
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();

        // Creamos una variable request y la seteamos con los datos necesarios para la conexion.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_SEGURIDAD_AUNTENTICACION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // imprimimos por consola la respuesta
                LogUtils.i(TAG, "OK RESPONSE" + response);

                LoginRespUnivida loginRespUnivida = null;
                Log.i("Login123","OK RESPONSE" + response);
                try {
                    // convertimos el json en un objeto de la clase LoginRespUnivida
                    loginRespUnivida = new Gson().fromJson(response, LoginRespUnivida.class);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                if (loginRespUnivida == null){
                    // mostramos un mensaje al usuario
                    mostrarMensaje(loginRespUnivida.getMensaje() + ".\n Datos del usuario son nulos.");
                    // el estado de progress lo ocultamos.
                    mostrarProgressBar(false);
                    return;
                }

                // verificamos el exito de la autenticacion
                if (loginRespUnivida.getExito()){
                    // verificamos que los datos del usuario no sean nulos, por que son requeridos.
                    if (loginRespUnivida.getDatosUser().getDatosUsuario() != null) {
                        // creamos un objeto user para setear los datos del usuario que nos llego como respuesta de la autenticacion
                        User user = new User();
                        //lo setamos como univida por que no seran necesarion
                        user.setFirstName("UNIVIDA");
                        user.setLastName("UNIVIDA");
                        // ingresamos el secuencial del empleado en el id del usuario
                        user.setId(loginRespUnivida.getDatosUser().getDatosUsuario().getEmpleadoSecuencial().toString());
                        // seteamos el webtoken en user, pero antes se lo encripta para que no puede ser utilizado si es que logran obtenerlo
                        user.setTokenAuth(UtilRest.getInstance().procesarDatosInterno(loginRespUnivida.getDatosUser().getToken(), 1000));

                        // setamos el usuario
                        user.setUsername(etUsuario.getText().toString());
                        //seteamos los datos del usuario de univida
                        user.setDatosUsuario(loginRespUnivida.getDatosUser().getDatosUsuario());
                        //creamos un usuario final user2
                        final User user2 = user;
                        //actualizamos en pantalla el estado en que nos encontramos
                        mostrarTexto(loginRespUnivida.getMensaje());
                        //GUARDAR EL TOKEN
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("numero", loginRespUnivida.getDatosUser().getNumero().toString());
                        editor.apply();

                        // un hilo de la actividad principal
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // creamos un hilo de java para no interrumpir ni colgar la pantalla de progress
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //eliminamos los datos de la tabla usuario
                                        controladorSqlite2.eliminarTodoDatoTabla("usuario");
                                        // guardamos los datos del user2 en al bd local
                                        controladorSqlite2.insertarUsuario(user2);
                                        // cambiamos el estado del usuario a 1 por que aun no obtenemos todas las parametricas
                                        controladorSqlite2.cambiarEstado(1);
                                        //cerramos la conexion
                                        controladorSqlite2.cerrarConexion();
                                        // creamos un intent para la actividad de obtener parametricas
                                        Intent intent = new Intent(getApplicationContext(), ObtenerParametricasActivity.class);
                                        // inciamos la actividad esperando un resultado de la misma, mediante el codigo 1256.
                                        startActivityForResult(intent, 1256);
                                    }
                                }).start();
                            }
                        });
                        return;
                    }else{
                        // mostramos un mensaje al usuario
                        mostrarMensaje(loginRespUnivida.getMensaje() + ".\n Datos del usuario son nulos.");
                        // el estado de progress lo ocultamos.
                        mostrarProgressBar(false);
                    }
                }else{
                    // eliminamos los datos de la tabla usuario
                    controladorSqlite2.eliminarTodoDatoTabla("usuario");
                    //cerramos la conexion
                    controladorSqlite2.cerrarConexion();
                    // mostramos un mensaje al usuario
                    mostrarMensaje(loginRespUnivida.getMensaje());
                    // el progress lo ocultamos
                    mostrarProgressBar(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.i(TAG, "ERROR RESPONSE" + error);
                        Log.i("loginInternet","ERROR RESPONSE" + error);
                        controladorSqlite2.eliminarTodoDatoTabla("usuario");

                        mostrarProgressBar(false);
                        //validamos que el parametro de error nosea nulo
                        if(error != null){
                            // verificamos si el parametro error sea una instancia de TIMEOUTERROR
                            if (error instanceof TimeoutError){
                                mostrarMensaje(getString(R.string.mensaje_error_timeout));
                            }else {
//                                mostrarMensaje(getString(R.string.mensaje_error_volley) + error.getMessage());
                                mostrarMensaje("No tiene Conexión a INTERNET...");
                            }
                        }else{
                            mostrarMensaje(getString(R.string.mensaje_error_volley_default));
                        }
                    }
                });

            }
        }){

            @Override
            public byte[] getBody() throws AuthFailureError {
                LogUtils.i(TAG, "||||||||||||||||||||||           ***************************");
                // verificamos que los parametrosJson no sean nulos
                Log.i("Login123","||||||||||||||||||||||           ***************************");
                if (parametrosJson != null) {

                    LogUtils.i(TAG, "getBody Enviando parametros :: " + parametrosJson);
                    Log.i("Login123","getBody Enviando parametros :: " + parametrosJson);
                    String enviarJson;

                    try {

                        // quitamos espacion al final y al principio del contenido de parametrosJson
                        enviarJson = parametrosJson.trim();

                        LogUtils.i(TAG, "getBody Enviando parametros encryp :: " + enviarJson);
                        Log.i("Login123","getBody Enviando parametros encryp :: " + enviarJson);
                        // enviamos el json en formato UTF-8
                        return enviarJson.getBytes("utf-8");

                    } catch (UnsupportedEncodingException e) {
                        Log.e("Login123","error: " + e);
                        e.printStackTrace();
                    }
                }
                LogUtils.i(TAG, "||||||||||||||||||||||           ***************************");
                // se envia vacio si es nulo parametrosJson
                return new byte[0];
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("version-app-pax", ConfigEmizor.VERSION);

                return params;
            }

            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }
        };

        // para que ignore el cache de volley
        stringRequest.setShouldCache(false);

        // seteamos el tiempo de espera y numero de reintentos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //agregamos un tag al request
        stringRequest.setTag("iniciosesion");

        // agregamos el requesta al singleton de volley para que inicie el consumo del sericio.
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        if (controladorSqlite2 != null){
            controladorSqlite2.cerrarConexion();
        }

        Intent intent = new Intent(getApplicationContext(), InicioActivity.class);

        startActivity(intent);

        LoginActivity.this.finish();
        //super.onBackPressed();

    }

    @Override
    public void finish() {
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll("iniciosesion");
        if (controladorSqlite2 != null) {
            controladorSqlite2.cerrarConexion();
            controladorSqlite2 = null;
        }
        super.finish();
    }


    @Override
    public void onRealizaAccionDialogEmizor(DialogoEmizor dialogoEmizor, int accion, int tipodialogo) {
        dialogoEmizor.getDialog().cancel();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1256){
            if (resultCode == RESULT_OK){
                guardarTiempo();
                Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);

                startActivity(intent);
                LoginActivity.this.finish();

            }else{

                controladorSqlite2.eliminarTodoDatoTabla(ControladorSqlite2.TABLA_USUARIO);
                etClave.setText("");
                mostrarProgressBar(false);

            }
        }
    }

    /**
     * <b>Metodo comenzarActualizar</b>, metodo privado que verificara si existe una actualizacion de la apliacion
     *  si existe una actualizacion avisara al usuario si quiere actualizar la aplicacion mediante un cuadro de dialogo
     */
    private void comenzarActualizar(){
        //Para tener el contexto mas a mano.
        context = this;
        //Creamos el Autoupdater.
        updater = new Autoupdater(this);
        // Seteamos el texto a mostrar por pantalla para informar de lo que se esta haciendo
        tvActualizar.setText("Verificando actualizaciones ... ");
        //Ponemos a correr el ProgressBar.
        llFondo.setVisibility(View.VISIBLE);
        // ocultamos el contedor principal
        mLoginFormView.setVisibility(View.GONE);
        //Ejecutamos el primer metodo del Autoupdater.
        updater.downloadData(finishBackgroundDownload);
    }

    /**
     * Codigo que se va a ejecutar una vez terminado de bajar los datos.
     */
    private Runnable finishBackgroundDownload = new Runnable() {
        @Override
        public void run() {
            //Volvemos el ProgressBar a invisible.
            llFondo.setVisibility(View.GONE);
            // Volvemos visible el contenedor principal
            mLoginFormView.setVisibility(View.VISIBLE);
            //Comprueba que halla nueva versión.
            if(updater.isNewVersionAvailable()){

                //Crea mensaje con datos de versión.
                String msj = "Nueva versión: " + updater.isNewVersionAvailable();
                msj += "\n   Versión actual: " + updater.getCurrentVersionName() + "(" + updater.getCurrentVersionCode() + ")";
                msj += "\n   Versión nueva: " + updater.getLatestVersionName() + "(" + updater.getLatestVersionCode() +")";
                msj += "\n¿Desea actualizar?";
                //Crea ventana de alerta.
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(context);
                // seteamos el mensaje que se mostrara en el cuadro de dialogo
                dialog1.setMessage(msj);
                // Establecemos el boton Cancelar y no se hara nada,
                dialog1.setNegativeButton(R.string.dialog_btntext_cancelar, null);
                //Establece el boton de Aceptar y que hacer si se selecciona.
                dialog1.setPositiveButton(R.string.dialog_btntext_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        tvActualizar.setText("Descargando actualización ...");
                        //Vuelve a poner el ProgressBar mientras se baja e instala.
                        llFondo.setVisibility(View.VISIBLE);
                        mLoginFormView.setVisibility(View.GONE);
                        //Se ejecuta el Autoupdater con la orden de instalar. Se puede poner un listener o no
                        updater.installNewVersion(null);
                    }
                });
                if (!LoginActivity.this.isFinishing()){
                    dialog1.show();
                }
                //Muestra la ventana esperando respuesta.

            }else{
                if (! LoginActivity.this.isFinishing()) {
                    // sacamos un mensaje corto por pantalla
                    ConfigEmizor.mostrarMensaje(LoginActivity.this, getLayoutInflater(), "fa_info_circle", " No hay actualizaciones disponibles.");

                }

                LogUtils.i("No Hay actualizaciones","no hay");
            }
        }
    };

    private void guardarTiempo(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("pref_datos",Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putLong("fecha_hora_inicio", Calendar.getInstance().getTimeInMillis());
                    editor.apply();
                    editor.commit();

            }
        });

    }

}

