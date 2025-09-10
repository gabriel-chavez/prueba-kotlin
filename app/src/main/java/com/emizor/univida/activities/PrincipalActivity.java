package com.emizor.univida.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.emizor.univida.LoginActivity;
import com.emizor.univida.R;
import com.emizor.univida.fragmento.TurnoControlFragment;
import com.emizor.univida.dialogo.DialogoEmizor;
import com.emizor.univida.fragmento.CambiarClaveFragment;
import com.emizor.univida.fragmento.HistorialQrGeneradosFragment;
import com.emizor.univida.fragmento.ListaRcvFragment;
import com.emizor.univida.fragmento.ListaVentasFragment;
import com.emizor.univida.fragmento.NuevaVentaFragment;
import com.emizor.univida.fragmento.NuevoRcvFragment;
import com.emizor.univida.fragmento.OnFragmentInteractionListener4;
import com.emizor.univida.fragmento.TurnoHistorialFragment;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.ventas.EfectivizarFacturaUnivida;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LogUtils;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.profile.Attribute;
import com.yandex.metrica.profile.GenderAttribute;
import com.yandex.metrica.profile.UserProfile;

import java.io.IOException;
import java.util.Calendar;

public class PrincipalActivity extends RootActivity implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener4, DialogoEmizor.NotificaDialogEmizorListener {

    private final String TAG = "PRINCIPAL";

    //Fragmentos activos
    private Fragment[] listaFragmentos;
    private View vistaPrincipal, vistaProgress;
    private static boolean estadoActivo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(0x80000000);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_principal);

        String currentVersionName = "Sin Version Por acceso";
        try {

            // Datos locales
            LogUtils.d(TAG, "INFO APP");
            PackageInfo pckginfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersionName = "v" + pckginfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, "Ha habido un error con el paquete :S", e);
        }

        ConfigEmizor.VERSION = currentVersionName;

        verificarTiempo();

        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(this);

        User user = controladorSqlite2.obtenerUsuario();

        listaFragmentos = new Fragment[5];

        controladorSqlite2.cerrarConexion();

        if (user == null) {
            controladorSqlite2.cerrarConexion();
            Intent intent = new Intent(this, LoginActivity.class);

            startActivity(intent);

            PrincipalActivity.this.finish();

            return;
        }

        logUser();

        Toolbar toolbar = findViewById(R.id.toolbar_principal);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // burger icon related
        getSupportActionBar().setTitle("NUEVA VENTA");

        DrawerLayout drawer = findViewById(R.id.container_principal);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_principal);
        navigationView.setNavigationItemSelectedListener(this);

        vistaPrincipal = findViewById(R.id.vista_principal_full);
        vistaProgress = findViewById(R.id.vista_progress_principal);

        int vistaActiva = 0;
        verificarTiempoLocacion();
        cambiarFragmento(new NuevaVentaFragment());
    }

    private void verificarTiempo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("pref_datos", Context.MODE_PRIVATE);

                long timeInicio = sharedPreferences.getLong("fecha_hora_inicio", 0);

                long timeFechaActual = Calendar.getInstance().getTimeInMillis();

                long tiempomls = timeFechaActual - timeInicio;

                LogUtils.i(TAG, "time inicio " + timeInicio + " time fechaactual " + timeFechaActual + " tiem rest " + tiempomls);

                if (tiempomls > ConfigEmizor.TIEMPO_ESPERA_CIERRE_SESION) {

                    ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(PrincipalActivity.this);

                    controladorSqlite2.eliminarTodoPrincipal();
                    controladorSqlite2.crearTablasPrincipal();

                    Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
                    estadoActivo = true;
                    startActivity(intent);

                    PrincipalActivity.this.finish();

                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putLong("fecha_hora_inicio", Calendar.getInstance().getTimeInMillis());
                    editor.apply();
                    editor.commit();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        verificarTiempo();
        LogUtils.i(TAG, "item menu " + item);
        switch (item.getItemId()) {
            case R.id.action_internet:
                if (PrincipalActivity.this != null && !PrincipalActivity.this.isFinishing()) {
                    new CheckInternetAsyncTask(PrincipalActivity.this, PrincipalActivity.this).execute();
                }
                break;
            case R.id.action_salir:

                ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getApplication());

                // eliminamos las tablas
                controladorSqlite2.eliminarTodoPrincipal();
                // volvemos a crear las tablas para tenerlas vacias en el siguiente inicio de sesion
                controladorSqlite2.crearTablasPrincipal();

                controladorSqlite2.cerrarConexion();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                // enviamos a la pantalla de Login
                startActivity(intent);

                // finalizamos la actividad
                PrincipalActivity.this.finish();

                break;
            case R.id.action_cambiar_clave:
                getSupportActionBar().setTitle("CAMBIAR CONTRASEÑA");
                cambiarFragmento(new CambiarClaveFragment());
                break;
            case R.id.action_obtener_parametricas:
                Intent intent1 = new Intent(getApplicationContext(), ObtenerParametricasActivity.class);

                startActivityForResult(intent1, 5526);
                break;
            case R.id.action_configuracion:

                intent = new Intent(getApplicationContext(), ConfiguracionAppActivity.class);

                startActivity(intent);

                break;
            case android.R.id.home:

                break;
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        verificarTiempo();

        switch (menuItem.getItemId()) {
            case R.id.menu_venta_nueva:
                // cambiamos a la vista de nueva venta
                cambiarFragmento(new NuevaVentaFragment());
                //cambiamos el titulo del toolbar
                getSupportActionBar().setTitle("NUEVA VENTA");
                break;
            case R.id.menu_venta_lista:
                //cambiamos el titulo del toolbar
                getSupportActionBar().setTitle("LISTAR VENTAS");
                // cambiamos a la vista de lista de ventas
                // verificamos si lista ventas es null

                if (listaFragmentos[1] == null) {
                    // creamos el fragmento Lista venta y lo visualizamos en pantalla
                    cambiarFragmento(new ListaVentasFragment());
                } else {
                    // enviamos el fragmento para visualizarlo en pantalla.
                    cambiarFragmento(listaFragmentos[1]);
                }
                break;
            case R.id.menu_historial_qr_generados:
                // cambiamos a la vista de nueva venta
                cambiarFragmento(new HistorialQrGeneradosFragment());
                //cambiamos el titulo del toolbar
                getSupportActionBar().setTitle("HISTORIAL QR GENERADOS");
                break;
            case R.id.menu_rcv_nuevo:
                //cambiamos el titulo del toolbar
                getSupportActionBar().setTitle("NUEVO RCV");
                // cambiamos a la vista de rcv nuevo
                // verificamos si nuevo rcv es null
                if (listaFragmentos[2] == null) {
                    // creamos el fragmento nuevo rcv y lo visualizamos en pantalla
                    cambiarFragmento(new NuevoRcvFragment());
                } else {
                    // enviamos el fragmento para visualizarlo en pantalla.
                    cambiarFragmento(listaFragmentos[2]);
                }
                break;
            case R.id.menu_rcv_realizadas:
                //cambiamos el titulo del toolbar
                getSupportActionBar().setTitle("LISTAR RCV");
                // cambiamos a la vista de lista de rcv realizadas
                // verificamos si lista de rcv es null
                if (listaFragmentos[3] == null) {
                    // creamos el fragmento lista rcv y lo visualizamos en pantalla
                    cambiarFragmento(new ListaRcvFragment());
                } else {
                    // enviamos el fragmento para visualizarlo en pantalla.
                    cambiarFragmento(listaFragmentos[3]);
                }
                break;
            case R.id.menu_turno_control:
                //cambiamos el titulo del toolbar
                getSupportActionBar().setTitle("Control de Turno");
                cambiarFragmento(new TurnoControlFragment());
                break;
            case R.id.menu_turno_historia:
                //cambiamos el titulo del toolbar
                getSupportActionBar().setTitle("Historial de registros");
                cambiarFragmento(new TurnoHistorialFragment());
                break;
                //SOATC
//            case R.id.menu_venta_nueva_soatc:
//                //cambiamos el titulo del toolbar
//                getSupportActionBar().setTitle("Historial de registros");
//                cambiarFragmento(new TurnoHistorialFragment());
//                break;
        }

        DrawerLayout drawer = findViewById(R.id.container_principal);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cambiarFragmento(Fragment fragment) {

        try {
            // obtenemos el manejador de transacciones de fragmentos
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // reemplazamos el fragmento actual por fragment
            transaction.replace(R.id.contenedor_vistas, fragment);
            // indicamos la transicion de fragment a FADE
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            // aplicamos los cambios con un commit en el manejador de las transacciondes de fragments.
            transaction.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onAccionFragment(View vista, int accion, Object parametros) {
        verificarTiempo();
        switch (accion) {

            case ACCION_MENSAJE:

                DialogoEmizor dialogoEmizor = new DialogoEmizor();
                dialogoEmizor.setTipoDialogo(4);
                dialogoEmizor.setMensaje((String.valueOf(parametros)));
                dialogoEmizor.setCancelable(false);
                dialogoEmizor.show(getSupportFragmentManager(), null);
                break;
            case ACCION_PROGRESS:
                showProgress(((Boolean) parametros));
                break;
            case ACCION_VISTA_EFECTIVIZAR:
                Intent intent = new Intent(getApplicationContext(), EfectivizarVentaActivity.class);

                Object[] objects = (Object[]) parametros;

                EfectivizarFacturaUnivida efectivizarFacturaUnivida = (EfectivizarFacturaUnivida) objects[0];
                boolean estadoNuevo = (Boolean) objects[1];

                intent.putExtra("objeto_efectivizar_univida", (Parcelable) efectivizarFacturaUnivida);
                intent.putExtra("estado_nuevo", estadoNuevo);

                startActivityForResult(intent, 4562);
                break;
            case ACCION_VISTA_EFECTIVIZAR_RCV:
                Intent intent1 = new Intent(getApplicationContext(), EfectivizarRcvActivity.class);

                intent1.putExtra("listarventarcv", String.valueOf(parametros));
                startActivityForResult(intent1, 4545);
                break;
            case ACCION_VISTA_REMITIR_RCV:
                Intent intentr = new Intent(getApplicationContext(), RemitirRcvActivity.class);
                intentr.putExtra("secuencial", Integer.valueOf(String.valueOf(parametros)));
                startActivityForResult(intentr, 1258);

                break;
            case OnFragmentInteractionListener4.ACCION_INICIAR_CAPTURA_LOCACION:
//                if(existePermisoLocacion()) {
//                    if (checkLocation()) {
//                        toggleGPSUpdates("init");
//                        toggleNetworkUpdates("init");
//                    }
//                } else {
//                    verificarPermisoLocacion();
//                }
                verificarTiempoLocacion();
                break;
        }

    }

    @Override
    public void onRegisterFragment(Fragment fragment, int tipoVista) {
        if (fragment != null) {
            switch (tipoVista) {
                case OnFragmentInteractionListener4.VISTA_NUEVA_VENTA:
                    listaFragmentos[0] = fragment;
                    break;
                case OnFragmentInteractionListener4.VISTA_LISTA_VENTAS:
                    listaFragmentos[1] = fragment;
                    break;
                case OnFragmentInteractionListener4.VISTA_RCV_NUEVO:
                    listaFragmentos[2] = fragment;
                    break;
                case OnFragmentInteractionListener4.VISTA_RCV_LISTA:
                    listaFragmentos[3] = fragment;
                    break;
                case OnFragmentInteractionListener4.VISTA_CAMBIAR_CONTRASENIA:
                    listaFragmentos[4] = fragment;
                    break;
            }
        }
    }

    private void showProgress(final boolean show) {
        try {
            PrincipalActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    verificarTiempo();
                    LogUtils.i(TAG, "VALORES VALORES ");

                    int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                    vistaPrincipal.setVisibility(show ? View.GONE : View.VISIBLE);
                    vistaPrincipal.animate().setDuration(shortAnimTime).alpha(
                            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            vistaPrincipal.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });

                    vistaProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                    vistaProgress.animate().setDuration(shortAnimTime).alpha(
                            show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            vistaProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        verificarTiempo();
        switch (requestCode) {
            case 4562:
                if (resultCode == RESULT_OK) {
                    listaFragmentos[0] = null;
                    cambiarFragmento(new NuevaVentaFragment());
                }
                break;
            case 1258:
                if (resultCode == RESULT_OK) {

                    if (listaFragmentos[3] != null) {
                        try {
                            Bundle bundle = data.getExtras();

                            Integer secuencial = -1000;

                            if (bundle != null) {
                                if (bundle.containsKey("secuencial")) {
                                    secuencial = bundle.getInt("secuencial");
                                }
                            }
                            ((ListaRcvFragment) listaFragmentos[3]).cambiar(secuencial);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                }
                break;
        }

    }

    @Override
    public void onRealizaAccionDialogEmizor(DialogoEmizor dialogoEmizor, int accion, int tipodialogo) {
        verificarTiempo();
        if (tipodialogo == 5) {
            if (accion == ACCION_ACEPTAR) {
                if (listaFragmentos[1] != null) {
                    ((ListaVentasFragment) listaFragmentos[1]).solicitarReversion(dialogoEmizor.getMotivo());
                }
            }
        } else if (tipodialogo == 6) {
            if (accion == ACCION_ACEPTAR) {
                if (listaFragmentos[1] != null) {
                    ((ListaVentasFragment) listaFragmentos[1]).imprimirColilla();
                }
            } else {
                showProgress(false);
            }
        }
        dialogoEmizor.getDialog().cancel();
    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods

        try {
            ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(this);

            User usuario = controladorSqlite2.obtenerUsuario();
            String nombreUsuario = usuario.getFirstName() + "_" + usuario.getLastName();

            // Creating the UserProfile instance.
//            User usuario = controladorSqlite2.obtenerUsuario();
            UserProfile userProfile = UserProfile.newBuilder()
                    // Updating predefined attributes.
                    .apply(Attribute.name().withValue(nombreUsuario))
                    .apply(Attribute.gender().withValue(GenderAttribute.Gender.OTHER))
                    //.apply(Attribute.birthDate().withAge(24))
                    .apply(Attribute.notificationsEnabled().withValue(false))
                    // Updating custom attributes.
                    .apply(Attribute.customString("sucursal").withValue(usuario.getDatosUsuario().getSucursalCiudad()))
                    .apply(Attribute.customString("cargo").withValue(usuario.getDatosUsuario().getEmpleadoCargo()))
                    .apply(Attribute.customString("completo").withValue(usuario.getDatosUsuario().getEmpleadoNombreCompleto()))
                    .build();
            // Setting the ProfileID using the method of the YandexMetrica class.
            YandexMetrica.setUserProfileID("id" + usuario.getId());

            // Sending the UserProfile instance.
            YandexMetrica.reportUserProfile(userProfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    class CheckInternetAsyncTask extends AsyncTask<Void, Integer, Boolean> {

        private Context context;
        private ProgressDialog dialog;

        public CheckInternetAsyncTask(Context context, PrincipalActivity principalActivity) {
            this.context = context;
            if (PrincipalActivity.this != null && !PrincipalActivity.this.isFinishing()) {
                dialog = new ProgressDialog(principalActivity);
            }
        }

        @Override
        protected void onPreExecute() {
            if (PrincipalActivity.this != null && !PrincipalActivity.this.isFinishing()) {
                dialog.setMessage("Verificando Internet.");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();
            } else {
                dialog = null;
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            assert cm != null;
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnected();


            if (isConnected) {
                Runtime runtime = Runtime.getRuntime();
                try {
                    Process ipProcess = runtime.exec("/system/bin/ping -c 1 127.0.0.1");
                    int exitValue = ipProcess.waitFor();
                    return (exitValue == 0);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return false;

            } else {
                LogUtils.d("TAG", "No network available!");
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            LogUtils.d("TAG", "result" + result);
            if (PrincipalActivity.this != null && !PrincipalActivity.this.isFinishing()) {
                // do UI work here
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                String mensaje = "No tiene conexión a INTERNET";
                int tipo = 2;//danger
                if (result) {
                    // do ur code
                    mensaje = " Tiene conexión a INTERNET";
                    tipo = 1;//danger
                }

                ConfigEmizor.mostrarMensaje(PrincipalActivity.this, PrincipalActivity.this.getLayoutInflater(), "fa_info_circle", mensaje, 2, tipo);
            }
        }

    }

}
