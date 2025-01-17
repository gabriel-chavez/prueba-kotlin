package com.emizor.univida;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.activities.RootActivity;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.rest.Conexion;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LogUtils;

import java.io.IOException;

public class InicioActivity extends RootActivity implements View.OnClickListener{

    private static final String TAG = "INICIO";
    // variables
    // boton loguear
    private BootstrapButton btnLoguear;

    //public static final String VERSIONAPP;
    private TextView tvVersion;

    //private PrinterInterface printInterfaceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().addFlags(0x80000000); // for take home key

        // cargamos la interfaz grafica en pantalla
        setContentView(R.layout.activity_inicio);
        LogUtils.resetDebug();

        //Typeface iconFont = ManejadorFuente.getTypeface(getApplicationContext(), ManejadorFuente.FONTAWESOME);
        TypefaceProvider.registerDefaultIconSets();

        //enlazamos el boton btnIngresar de la interfaz grafica con la variable btnLoguear de esta actividad
        btnLoguear = findViewById(R.id.btnIngresar);
        // le asignamos el listener onclick(en este caso es la propia actividad) al boton loguear
        btnLoguear.setOnClickListener(this);

        tvVersion = findViewById(R.id.tvVersionApp);

        // obtenemos un manejador de la bd local
        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(this);

        User usuario = null;
        try {
            // obtenemos al usuario
            usuario = controladorSqlite2.obtenerUsuario();
        }catch (Exception e){
            e.printStackTrace();
        }

        if (usuario != null){

            // cerramos coleccion.
            controladorSqlite2.cerrarConexion();
            Intent intent;
            if (usuario.getEstado() == 2) {

                // si el usuario ya esta logueado y tiene el,estado de 2 que indica que tiene todas las parametricas necesarias para realizar ventas.

                intent = new Intent(this, PrincipalActivity.class);

            }else{
                // caso contrario se pedira que inicie normalmente .
                intent = new Intent(this, LoginActivity.class);
                // borramos los datos de la tabla usuario
                controladorSqlite2.eliminarTodoDatoTabla(ControladorSqlite2.TABLA_USUARIO);
                // cerramos la conexion de la base de datos
                controladorSqlite2.cerrarConexion();
            }

            //iniciamos la actividad
            startActivity(intent);
            // finalizamos la actividad actual
            this.finish();

        }else {
            String currentVersionName = "";
            try {
                // Datos locales
                LogUtils.d(TAG, "INFO APP");
                PackageInfo pckginfo = getPackageManager().getPackageInfo(getPackageName(), 0);

                currentVersionName += pckginfo.versionName;
                ConfigEmizor.VERSION = "v" + pckginfo.versionName;

            }catch(PackageManager.NameNotFoundException e){
                LogUtils.e(TAG, "Ha habido un error con el packete :S", e);
            }

            tvVersion.setText("Versión " + currentVersionName);
        }
        // cerramos la conexion
        controladorSqlite2.cerrarConexion();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            verificarPermisoLocacion();
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_INSTALL_PACKAGES) != PackageManager.PERMISSION_GRANTED) {
                // No explanation needed; request the permission

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES},
                        1002);
            }
        }

    }


    /**
     * <b>Metodo salirDeInicio</b>, que cierra la actividad InicioActivity
     */
    public void salirDeInicio(){
        // llamamos al metodo finish de la actividad para finalizar la actividad
        InicioActivity.this.finish();
    }


    @Override
    public void onClick(View view) {

        //MEncriptar mEncriptar = new MEncriptar();

        if (Conexion.estaConectado(this)) {

            if (view.getId() == R.id.btnIngresar) {
                if(existePermisoLocacion()) {
                    if (checkLocation()) {
                        if (InicioActivity.this != null && !InicioActivity.this.isFinishing()) {
                            new InicioActivity.CheckInternetAsyncTask(InicioActivity.this, InicioActivity.this).execute();
                        }
                    }
                } else {
                    verificarPermisoLocacion();
                }
            }
        }else{
            ConfigEmizor.sinConexion(getApplication(),getLayoutInflater());
        }
    }

    public void ingresarLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        // inicializar la actividad LoginActivity
        startActivity(intent);
        // llamamos al metodo salirDeInicio
        salirDeInicio();
    }

    class CheckInternetAsyncTask extends AsyncTask<Void, Integer, Boolean> {

        private Context context;
        private ProgressDialog dialog;
        private InicioActivity principalActivity;
        public CheckInternetAsyncTask(Context context, InicioActivity principalActivity) {
            this.context = context;
            if (InicioActivity.this != null && ! InicioActivity.this.isFinishing()) {
                dialog = new ProgressDialog(principalActivity);
                this.principalActivity= principalActivity;
            }
        }

        @Override
        protected void onPreExecute() {
            if (InicioActivity.this != null && ! InicioActivity.this.isFinishing()) {
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
                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            assert cm != null;
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnected();


            if (isConnected) {
                Runtime runtime = Runtime.getRuntime();
                try {
                    Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                    int     exitValue = ipProcess.waitFor();
                    return (exitValue == 0);
                }
                catch (IOException e)          { e.printStackTrace(); }
                catch (InterruptedException e) { e.printStackTrace(); }

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
            if (InicioActivity.this != null && ! InicioActivity.this.isFinishing()) {
                // do UI work here
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                String mensaje = " No tiene conexión a INTERNET";
                int tipo = 2;//danger
                if (result) {
                    // do ur code
                    mensaje = " Tiene conexión a INTERNET";
                    tipo = 1;//danger
                }

                ConfigEmizor.mostrarMensaje(InicioActivity.this, InicioActivity.this.getLayoutInflater(), "fa_info_circle", mensaje, 2, tipo);
                if (result) {
                    if (principalActivity != null){
                        InicioActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                principalActivity.ingresarLogin();
                            }
                        });
                    }
                }
            }
        }

    }


}
