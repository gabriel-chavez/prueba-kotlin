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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.emizor.univida.dialogo.DialogoEmizor;
import com.emizor.univida.excepcion.ErrorPapelException;
import com.emizor.univida.excepcion.ImpresoraErrorException;
import com.emizor.univida.excepcion.NoHayPapelException;
import com.emizor.univida.excepcion.VoltageBajoException;
import com.emizor.univida.fragmento.OnFragmentInteractionListener4;
import com.emizor.univida.imprime.ImprimirAvisoListener;
import com.emizor.univida.imprime.ImprimirFactura;
import com.emizor.univida.modelo.dominio.univida.parametricas.TipoDocumentoIdentidad;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.ventas.EfectivizarAdicionalUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.EfectivizarFacturaUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.EfectivizarRespUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.ObtenerVentaUnivida;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.rest.VolleySingleton;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LoadingDialog;
import com.emizor.univida.util.LogUtils;
import com.emizor.univida.util.Utils;
import com.emizor.univida.util.ValidarCampo;
import com.google.gson.Gson;
import com.pax.dal.ISys;
import com.pax.dal.entity.ETermInfoKey;
import com.pax.neptunelite.api.NeptuneLiteUser;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;


public class EfectivizarVentaActivity extends RootActivity implements DialogoEmizor.NotificaDialogEmizorListener, ImprimirAvisoListener {

    private final String TAG = "EFECTIVIZARVENTA";

    private EfectivizarFacturaUnivida efectivizarFacturaUnivida;

    private User user;
    private EditText etRoseta, etNit, etRazonSocial, etTelefonoClienteEfectivizar, etCorreoClienteEfectivizar, etComplementoEfectivizar;
    //private TextView tvCoordenada;
    private View vistaFull, vistaProgress;

    private boolean estadoNuevo, estadoAceptarDialogo;

    private long tiempoEspera;
    private ImprimirFactura imprimirFactura;
    private Integer cantDocsImprimir, contadorImprimir;
    private Date fechaImpresion;

    private EfectivizarRespUnivida efectivizarRespUnivida;

    //nuevos datos
    private View vistaForm1, vistaForm2, vistaForm3;

    private BootstrapButton btnSiguiente2, btnSiguiente3;

    private boolean errorImp;

    private EfectivizarAdicionalUnivida efectivizarAdicionalUnivida;
    private BootstrapButton btnEfectivizar;
    private String parametrosJson3;

    private Spinner spTipoDocumentoIdentidad;
    private TipoDocumentoIdentidad tipoDocumentoIdentidad;
    private TextInputLayout tilNitEfectivisar, tiComplementoLayout;
    private String tramiteSecuencialQr;
    private LoadingDialog loadingDialog = new LoadingDialog();
    private Handler handler = new Handler();
    private boolean isEfectivizado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_efectivizar_venta);
        // Referencias a los botones QR efectivo
        ToggleButton btnPagoQR = findViewById(R.id.btnPagoQR);
        ToggleButton btnPagoEfectivo = findViewById(R.id.btnPagoEfectivo);
        btnEfectivizar = findViewById(R.id.btnEfectivizar);
        // Configuración de comportamiento
        btnPagoQR.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                btnPagoEfectivo.setChecked(false);
            }
            btnEfectivizar.setEnabled(btnPagoQR.isChecked() || btnPagoEfectivo.isChecked());

        });

        btnPagoEfectivo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                btnPagoQR.setChecked(false);
            }
            btnEfectivizar.setEnabled(btnPagoQR.isChecked() || btnPagoEfectivo.isChecked());

        });
        btnEfectivizar.setEnabled(btnPagoQR.isChecked() || btnPagoEfectivo.isChecked());//estado inicial del boton

        // Fin referencias a los botones

        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(this);

        user = controladorSqlite2.obtenerUsuario();

        efectivizarAdicionalUnivida = new EfectivizarAdicionalUnivida();
        efectivizarAdicionalUnivida.setMedioPago(controladorSqlite2.obtenerMedioPago(1));// 1 POR SER EL SECUENCIAL DEL MEDIO DE PAGO EFECTIVO

        if (user == null) {
            controladorSqlite2.cerrarConexion();
            setResult(RESULT_CANCELED);

            EfectivizarVentaActivity.this.finish();
            return;
        }

        efectivizarAdicionalUnivida.setIp(Utils.getIPAddress(true));

        int permissionCheckPhone = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheckPhone == PackageManager.PERMISSION_GRANTED) {

            efectivizarAdicionalUnivida.setImei(ConfigEmizor.getImei(EfectivizarVentaActivity.this));
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

        efectivizarAdicionalUnivida.setVersionApp(currentVersionName);

        try {
            ISys iSys = NeptuneLiteUser.getInstance().getDal(this).getSys();

            Map<ETermInfoKey, String> maps = iSys.getTermInfo();

            efectivizarAdicionalUnivida.setSerialNumberPax(maps.get(ETermInfoKey.SN));
            efectivizarAdicionalUnivida.setModelPax(maps.get(ETermInfoKey.MODEL));


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        verificarTiempo(3);
        errorImp = false;

        Toolbar toolbar = findViewById(R.id.toolbar_efectivizar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("EFECTIVIZAR VENTA");

        Bundle bundle = getIntent().getExtras();

        efectivizarFacturaUnivida = null;

        estadoNuevo = false;
        estadoAceptarDialogo = false;

        if (bundle != null) {
            if (bundle.containsKey("objeto_efectivizar_univida")) {
                efectivizarFacturaUnivida = bundle.getParcelable("objeto_efectivizar_univida");
            }
        }

        // tipos de documento identidad
        tilNitEfectivisar = findViewById(R.id.tilNitEfectivisar);
        tiComplementoLayout = findViewById(R.id.tiComplementoLayout);
        spTipoDocumentoIdentidad = findViewById(R.id.spTipoDocumentoIdentidad);
        List<TipoDocumentoIdentidad> listaTiposDocs = controladorSqlite2.obtenerTipoDocumentosIdentidad();
        LogUtils.i(TAG, "DAtos de documentos " + new Gson().toJson(listaTiposDocs));
        ArrayAdapter<TipoDocumentoIdentidad> arrayAdapterTipoDocumentoIdentidad = new ArrayAdapter<TipoDocumentoIdentidad>(EfectivizarVentaActivity.this, android.R.layout.simple_spinner_dropdown_item, listaTiposDocs);

        spTipoDocumentoIdentidad.setAdapter(arrayAdapterTipoDocumentoIdentidad);
        spTipoDocumentoIdentidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.i(TAG, "on item select inicia spTipoDocumentoIdentidad");

                tipoDocumentoIdentidad = (TipoDocumentoIdentidad) parent.getSelectedItem();
                LogUtils.i(TAG, "on item select spTipoDocumentoIdentidad " + new Gson().toJson(tipoDocumentoIdentidad));
                tiComplementoLayout.setVisibility(View.GONE);
//                etComplementoEfectivizar.setEnabled(false);
                tilNitEfectivisar.setHint("");
                if (tipoDocumentoIdentidad.getSecuencial() <= 0) {
                    tipoDocumentoIdentidad = null;

                }
                tilNitEfectivisar.setHint(tipoDocumentoIdentidad != null ? tipoDocumentoIdentidad.getDescripcion() : null);
                if (tipoDocumentoIdentidad.getRequiereComplemento()) {
                    tiComplementoLayout.setVisibility(View.VISIBLE);
//                    etComplementoEfectivizar.setEnabled(true);

                } else {
                    etComplementoEfectivizar.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //tipos documentos de identidad

        controladorSqlite2.cerrarConexion();


        btnEfectivizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnEfectivizar.isEnabled() && (!estadoAceptarDialogo)) {

                    btnEfectivizar.setEnabled(false);

                    EfectivizarVentaActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            obtenerCoordenadasVentaSoatPlaca();
                        }
                    });

                }

            }
        });

        if (efectivizarFacturaUnivida == null) {

            setResult(RESULT_CANCELED);

            EfectivizarVentaActivity.this.finish();
            return;
        } else {
            //vehiculo
            if (efectivizarFacturaUnivida.getVehiculoAnio() != null) {
                ((EditText) findViewById(R.id.etVehiculoAnioEfectivizar)).setText(String.valueOf(efectivizarFacturaUnivida.getVehiculoAnio()));
            } else {
                ((EditText) findViewById(R.id.etVehiculoAnioEfectivizar)).setText("0");
            }
            ((EditText) findViewById(R.id.etVehiculoNroMotorEfectivizar)).setText(efectivizarFacturaUnivida.getVehiculoMotor());
            ((EditText) findViewById(R.id.etVehiculoModeloEfectivizar)).setText(efectivizarFacturaUnivida.getVehiculoModelo());
            ((EditText) findViewById(R.id.etVehiculoMarcaEfectivizar)).setText(efectivizarFacturaUnivida.getVehiculoMarca());
            ((EditText) findViewById(R.id.etVehiculoColorEfectivizar)).setText(efectivizarFacturaUnivida.getVehiculoColor());
            ((EditText) findViewById(R.id.etVehiculoNroChasisEfectivizar)).setText(efectivizarFacturaUnivida.getVehiculoChasis());
            // vehiculo nuevo agregado 20 noviembre 2021
            ((EditText) findViewById(R.id.etVehiculoCilindradaEfectivizar)).setText(String.valueOf(efectivizarFacturaUnivida.getVehiculoCilindrada() == null ? "0" : efectivizarFacturaUnivida.getVehiculoCilindrada()));
            ((EditText) findViewById(R.id.etVehiculoCapacidadCargaEfectivizar)).setText(String.valueOf(efectivizarFacturaUnivida.getVehiculoCapacidadCarga() == null ? "0.0" : efectivizarFacturaUnivida.getVehiculoCapacidadCarga()));

            //propietario
            ((EditText) findViewById(R.id.etPropTomadorEfectivizar)).setText(efectivizarFacturaUnivida.getPropTomador());
            ((EditText) findViewById(R.id.etPropCelularEfectivizar)).setText(efectivizarFacturaUnivida.getPropCelular());
            ((EditText) findViewById(R.id.etPropNitEfectivizar)).setText(efectivizarFacturaUnivida.getPropNit());
            ((EditText) findViewById(R.id.etPropDireccionEfectivizar)).setText(efectivizarFacturaUnivida.getPropDireccion());
            ((EditText) findViewById(R.id.etPropCiEfectivizar)).setText(efectivizarFacturaUnivida.getPropCi());
            ((EditText) findViewById(R.id.etPropTelefonoEfectivizar)).setText(efectivizarFacturaUnivida.getPropTelefono());
        }

        TextView tvPrima = findViewById(R.id.tvMontoPrimaEfectivizar);

        String strPrima = "PRIMA " + efectivizarFacturaUnivida.getPrima() + " Bs";

        etRoseta = findViewById(R.id.etRosetaEfectivizar);
        // 20 nov 2021
        if (efectivizarFacturaUnivida.getGestionFk() >= 2022) {
            etRoseta.setVisibility(View.GONE);
        }

        etNit = findViewById(R.id.etNitEfectivizar);
        etRazonSocial = findViewById(R.id.etRazonSocialEfectivizar);
        etTelefonoClienteEfectivizar = findViewById(R.id.etTelefonoClienteEfectivizar);
        etCorreoClienteEfectivizar = findViewById(R.id.etCorreoClienteEfectivizar);
        etComplementoEfectivizar = findViewById(R.id.etComplementoEfectivizar);

        vistaFull = findViewById(R.id.vista_full_efectivizar);
        vistaProgress = findViewById(R.id.vista_progress_efectivizar);

        vistaForm1 = findViewById(R.id.vistaForm1);
        vistaForm2 = findViewById(R.id.vistaForm2);
        vistaForm3 = findViewById(R.id.vistaForm3);

        tvPrima.setText(strPrima);

        if (efectivizarFacturaUnivida.getRosetaNumero() != null && efectivizarFacturaUnivida.getRosetaNumero() != 0L) {
            etRoseta.setText(String.valueOf(efectivizarFacturaUnivida.getRosetaNumero()));
            etRoseta.setEnabled(false);
        } else {
            etRoseta.setText("0");
            estadoNuevo = true;
        }

        btnSiguiente2 = findViewById(R.id.btnSiguiente2);
        btnSiguiente3 = findViewById(R.id.btnSiguiente3);

        btnSiguiente2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vistaForm2.setVisibility(View.GONE);
                vistaForm3.setVisibility(View.VISIBLE);
            }
        });

        btnSiguiente3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vistaForm3.setVisibility(View.GONE);
                vistaForm1.setVisibility(View.VISIBLE);
            }
        });

        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);

        String longTime = preferencias.getString("tiempo_espera_gps", "5");

        if (longTime != null) {
            tiempoEspera = Long.valueOf(longTime);
        } else {
            tiempoEspera = 5L;
        }

        LogUtils.i(TAG, " tiempo espera gps " + tiempoEspera);

        boolean estadoActivoGps = preferencias.getBoolean("activar_gps", true);
        LogUtils.i(TAG, " activar_gps " + estadoActivoGps);
        if (!estadoActivoGps) {
            tiempoEspera = 0;
        }

        LogUtils.i(TAG, " tiempo espera gps FINAL " + tiempoEspera);

        imprimirFactura = ImprimirFactura.obtenerImpresora(getApplicationContext());
        imprimirFactura.setAvisoListener(this);

        efectivizarRespUnivida = null;
        cantDocsImprimir = 0;
        contadorImprimir = 0;

        parametrosJson3 = null;
        verificarTiempoLocacion();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        verificarTiempo(3);
        switch (item.getItemId()) {
            case android.R.id.home:

                if (vistaForm1.getVisibility() == View.VISIBLE && parametrosJson3 == null) {
                    vistaForm1.setVisibility(View.GONE);
                    vistaForm3.setVisibility(View.VISIBLE);
                } else if (vistaForm3.getVisibility() == View.VISIBLE && parametrosJson3 == null) {
                    vistaForm3.setVisibility(View.GONE);
                    vistaForm2.setVisibility(View.VISIBLE);
                } else {
                    setResult(RESULT_OK);
                    EfectivizarVentaActivity.this.finish();
                }

                break;
        }

        return true;
    }

    public void obtenerCoordenadasVentaSoatPlaca() {

        try {

            showProgress(true);

            String strRoseta, strNit, strRazonSocial, strCorreo, strTelefono;
            etNit.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etNit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            View vistaForm = null;
            boolean estado = false;
            strRoseta = etRoseta.getText().toString();
            strNit = etNit.getText().toString();
            strRazonSocial = etRazonSocial.getText().toString();
            strCorreo = etCorreoClienteEfectivizar.getText().toString();
            strTelefono = etTelefonoClienteEfectivizar.getText().toString();

            mostrarTexto("VALIDANDO ...");
            if (strNit.length() < 5) {
                if (!strNit.equals("0")) {
                    etNit.setError("El campo CI/NIT es incorrecto, debe tener por lo menos 5 digitos");
                    vistaForm = etNit;
                    estado = true;
                }
            }

            if (strRazonSocial.isEmpty()) {
                etRazonSocial.setError("El campo Razón social Es necesario.");
                vistaForm = etRazonSocial;
                estado = true;
            } else if (!ValidarCampo.validarTamanioCadena(strRazonSocial, 3, 180)) {
                etRazonSocial.setError("El campo Razón social debe tener 3 caracteres como minimo.");
                vistaForm = etRazonSocial;
                estado = true;
            }

            if (estadoNuevo) {
                if (strRoseta.isEmpty()) {
                    etRoseta.setError("El campo Roseta es necesario.");
                    vistaForm = etRoseta;
                    estado = true;
                } else if (!ValidarCampo.validarNumeroMayorACero(strRoseta)) {
                    if (!strRoseta.equals("0")) {
                        etRoseta.setError("El campo Roseta debe ser un número mayor o igual a 0.");
                        vistaForm = etRoseta;
                        estado = true;
                    }
                }
            }

            if (strCorreo.length() > 0) {
                if (!ValidarCampo.validarEmail(strCorreo)) {
                    vistaForm = etCorreoClienteEfectivizar;
                    estado = true;
                    etCorreoClienteEfectivizar.setError("El correo electrónico es incorrecto.");
                }
            }

            if (strTelefono.length() > 0) {
                if (!(strTelefono.startsWith("7") || strTelefono.startsWith("6"))) {
                    vistaForm = etTelefonoClienteEfectivizar;
                    estado = true;
                    etTelefonoClienteEfectivizar.setError("Teléfono incorrecto.");
                } else if (!TextUtils.isDigitsOnly(strTelefono)) {
                    vistaForm = etTelefonoClienteEfectivizar;
                    estado = true;
                    etTelefonoClienteEfectivizar.setError("Teléfono debe tener solo numeros.");
                } else if (strTelefono.length() < 5) {
                    vistaForm = etTelefonoClienteEfectivizar;
                    estado = true;
                    etTelefonoClienteEfectivizar.setError("Teléfono incorrecto.");
                }
            } else {
                vistaForm = etTelefonoClienteEfectivizar;
                estado = true;
                etTelefonoClienteEfectivizar.setError("Teléfono es requerido.");
            }

            if (estado) {
                showProgress(false);
                vistaForm.requestFocus();
//                toggleBestUpdates("parar");
                btnEfectivizar.setEnabled(true);
            } else {
                // obtener el estado de los botones
                ToggleButton btnPagoQR = findViewById(R.id.btnPagoQR);
                ToggleButton btnPagoEfectivo = findViewById(R.id.btnPagoEfectivo);

                String medioPago = "";
                if (btnPagoQR.isChecked()) {
                    medioPago = "Pago con QR";
                } else if (btnPagoEfectivo.isChecked()) {
                    medioPago = "Pago en efectivo";
                } else {
                    medioPago = "No seleccionado";
                }


                String mensaje;
                String titulo = "VERIFICAR";

                ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getApplication());


                mensaje = "CRITERIO DE BÚSQUEDA\n";
                mensaje += controladorSqlite2.obtenerTipoPlaca(efectivizarFacturaUnivida.getVehiculoPlacaTipo()).getDescripcion() + ": " + efectivizarFacturaUnivida.getVehiculoPlaca();
                mensaje += "\n\nPRIMA: " + efectivizarFacturaUnivida.getPrima();
                mensaje += "\nGESTIÓN: " + efectivizarFacturaUnivida.getGestionFk();
                mensaje += "\nUSO VEHÍCULO: " + controladorSqlite2.obtenerTipoUso(efectivizarFacturaUnivida.getVehiculoUsoFk()).getDescripcion();
                mensaje += "\nTIPO VEHÍCULO: " + controladorSqlite2.obtenerTipoVehiculo(efectivizarFacturaUnivida.getVehiculoTipoFk()).getDescripcion();
//                mensaje += "\nCRITERIO DE BÚSQUEDA: " + controladorSqlite2.obtenerTipoPlaca(efectivizarFacturaUnivida.getVehiculoPlacaTipo()).getDescripcion();
                mensaje += "\nPLAZA CIRCULACIÓN: " + controladorSqlite2.obtenerDepartamento(efectivizarFacturaUnivida.getDepartamentoPlazaCirculacionFk()).getDescripcion();
                if (efectivizarFacturaUnivida.getGestionFk() < 2022) {
                    mensaje += "\nROSETA: " + etRoseta.getText().toString();
                }
                mensaje += "\nRAZÓN SOCIAL: " + etRazonSocial.getText().toString();
                mensaje += "\nCI/NIT: " + etNit.getText().toString() + " " + etComplementoEfectivizar.getText().toString().trim();
                mensaje += "\nCorreo Electrónico: " + etCorreoClienteEfectivizar.getText().toString();
                mensaje += "\nNúmero de Celular: " + etTelefonoClienteEfectivizar.getText().toString();
                mensaje += "\nSUCURSAL: " + user.getDatosUsuario().getSucursalNombre();
                mensaje += "\nUSUARIO: " + user.getDatosUsuario().getEmpleadoNombreCompleto();
                mensaje += "\n\nMEDIO DE PAGO: " + medioPago;

                controladorSqlite2.cerrarConexion();

                DialogoEmizor dialogoEmizor = new DialogoEmizor();
                dialogoEmizor.setTipoDialogo(3);
                dialogoEmizor.setMensaje(mensaje);
                dialogoEmizor.setTitulo(titulo);
                dialogoEmizor.setCancelable(false);
                dialogoEmizor.show(getSupportFragmentManager(), null);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mostrarTexto(final String mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tvTexto = findViewById(R.id.tvTextoProgress);
                String textoActual = tvTexto.getText().toString() + "\n\n" + mensaje;
                tvTexto.setText(textoActual);
            }
        });

    }

    public void recolectarDatos() {
        mostrarTexto("Enviando datos.");

        String strRoseta, strNit, strRazonSocial, strCiComplemento;

        strRoseta = etRoseta.getText().toString();
        strNit = etNit.getText().toString();
        strRazonSocial = etRazonSocial.getText().toString();
        strCiComplemento = etComplementoEfectivizar.getText().toString();

        //EfectivizarAdicionalUnivida efectivizarAdicionalUnivida = new EfectivizarAdicionalUnivida();

        efectivizarAdicionalUnivida.setLatitud(getLocationLatitud());
        efectivizarAdicionalUnivida.setLongitud(getLocationLongitud());

        efectivizarFacturaUnivida.setCorreoCliente(etCorreoClienteEfectivizar.getText().toString());
        efectivizarFacturaUnivida.setVentaDatosAdicionales(new Gson().toJson(efectivizarAdicionalUnivida));
        efectivizarFacturaUnivida.setNitCi(strNit);
        efectivizarFacturaUnivida.setRazonSocial(strRazonSocial);
        efectivizarFacturaUnivida.setRosetaNumero(Long.valueOf(strRoseta));

        efectivizarFacturaUnivida.setCiComplemento(strCiComplemento);
        efectivizarFacturaUnivida.setTipoDocIdentidadFk(tipoDocumentoIdentidad.getSecuencial());

        efectivizarFacturaUnivida.setTelefonoCliente(etTelefonoClienteEfectivizar.getText().toString());

        //vehiculo
        //cambiado(11-12-2018) validar si no tiene nada colocarlo 0
        if (!((EditText) findViewById(R.id.etVehiculoAnioEfectivizar)).getText().toString().isEmpty()) {
            efectivizarFacturaUnivida.setVehiculoAnio(Integer.valueOf(((EditText) findViewById(R.id.etVehiculoAnioEfectivizar)).getText().toString()));
        } else {
            efectivizarFacturaUnivida.setVehiculoAnio(0);
        }
        efectivizarFacturaUnivida.setVehiculoMotor(((EditText) findViewById(R.id.etVehiculoNroMotorEfectivizar)).getText().toString());
        efectivizarFacturaUnivida.setVehiculoModelo(((EditText) findViewById(R.id.etVehiculoModeloEfectivizar)).getText().toString());
        efectivizarFacturaUnivida.setVehiculoMarca(((EditText) findViewById(R.id.etVehiculoMarcaEfectivizar)).getText().toString());
        efectivizarFacturaUnivida.setVehiculoColor(((EditText) findViewById(R.id.etVehiculoColorEfectivizar)).getText().toString());
        efectivizarFacturaUnivida.setVehiculoChasis(((EditText) findViewById(R.id.etVehiculoNroChasisEfectivizar)).getText().toString());
        // vehiculo nuevo agregado 20 noviembre 2021
        String valorCilindrada = ((EditText) findViewById(R.id.etVehiculoCilindradaEfectivizar)).getText().toString();

        if (valorCilindrada.isEmpty()) {
            efectivizarFacturaUnivida.setVehiculoCilindrada(null);
        } else {
            efectivizarFacturaUnivida.setVehiculoCilindrada(Integer.valueOf(valorCilindrada));
        }
        String valorCapacidadCarga = ((EditText) findViewById(R.id.etVehiculoCapacidadCargaEfectivizar)).getText().toString();

        if (valorCapacidadCarga.isEmpty()) {
            efectivizarFacturaUnivida.setVehiculoCapacidadCarga(null);
        } else {
            efectivizarFacturaUnivida.setVehiculoCapacidadCarga(Double.valueOf(valorCapacidadCarga));
        }

        //propietario
        efectivizarFacturaUnivida.setPropTomador(((EditText) findViewById(R.id.etPropTomadorEfectivizar)).getText().toString());
        efectivizarFacturaUnivida.setPropCelular(((EditText) findViewById(R.id.etPropCelularEfectivizar)).getText().toString());
        efectivizarFacturaUnivida.setPropCi(((EditText) findViewById(R.id.etPropCiEfectivizar)).getText().toString());
        efectivizarFacturaUnivida.setPropDireccion(((EditText) findViewById(R.id.etPropDireccionEfectivizar)).getText().toString());
        efectivizarFacturaUnivida.setPropNit(((EditText) findViewById(R.id.etPropNitEfectivizar)).getText().toString());
        efectivizarFacturaUnivida.setPropTelefono(((EditText) findViewById(R.id.etPropTelefonoEfectivizar)).getText().toString());

        parametrosJson3 = efectivizarFacturaUnivida.toString();
        LogUtils.i(TAG, "Enviando dataos " + parametrosJson3);
    }

    private void enviarDatos() {
        // Identificar el medio de pago seleccionado
        ToggleButton btnPagoQR = findViewById(R.id.btnPagoQR);
        ToggleButton btnPagoEfectivo = findViewById(R.id.btnPagoEfectivo);

        //QR
        if (btnPagoQR.isChecked()) {
            mostrarVentanaPagoQR(efectivizarFacturaUnivida);

        }
        //EFECTIVO
        if (btnPagoEfectivo.isChecked()) {
            String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_EFECTIVIZAR_FACTURA_CICLOS_INTER;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> manejarRespuesta(response),
                    error -> manejarError(error)) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return obtenerCuerpoSolicitud(parametrosJson3);
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

            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }
    }

    /**
     * construye encabezados http
     */
    private Map<String, String> construirEncabezados() {
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

    /**
     * Obtiene cuerpo de solicitud
     */
    private byte[] obtenerCuerpoSolicitud(String parametrosJson) {
        if (parametrosJson != null) {

            LogUtils.i(TAG, "getBody Enviando parametros :: " + parametrosJson);
            String enviarJson;

            try {

                enviarJson = parametrosJson.trim();

                LogUtils.i(TAG, "getBody Enviando parametros encryp :: " + enviarJson);

                return enviarJson.getBytes("utf-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        LogUtils.i(TAG, "||||||||||||||||||||||           ***************************");
        return new byte[0];
    }

    /**
     * Maneja la respuesta del servidor.
     */
    private void manejarRespuesta(String response) {
        //btnEfectivizar.setEnabled(true);
        //estadoAceptarDialogo = false;

        LogUtils.i(TAG, "OK RESPONSE " + response);

        efectivizarRespUnivida = null;

        try {

            efectivizarRespUnivida = new Gson().fromJson(response, EfectivizarRespUnivida.class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (efectivizarRespUnivida != null) {
            mostrarTexto(efectivizarRespUnivida.getMensaje());
            if (efectivizarRespUnivida.getExito()) {

                cantDocsImprimir = 2;
                contadorImprimir = 0;

                //imprimimos
                ((TextView) findViewById(R.id.tvTextoProgress)).setText("");
                mostrarTexto("IMPRIMIR FACTURA Y COMPROBANTE ... ");
                try {

                    imprimirFactura.prepararImpresionFactura(user, efectivizarRespUnivida);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    mostrarMensaje("No se puede imprimir los datos por que algunos o todos son nulos.");
                    return;
                }
                fechaImpresion = Calendar.getInstance().getTime();
                iniciarImprimir();

            } else {
                btnEfectivizar.setEnabled(true);
                estadoAceptarDialogo = false;
                parametrosJson3 = null;
                showProgress(false);
                mostrarMensaje(efectivizarRespUnivida.getMensaje());
            }
        } else {
            btnEfectivizar.setEnabled(true);
            estadoAceptarDialogo = false;
            parametrosJson3 = null;
            showProgress(false);
            mostrarMensaje("Los datos estan vacios, comuniquese con el administrador.");
        }
    }

    /**
     * Maneja los errores de la solicitud.
     */
    private void manejarError(VolleyError error) {
        LogUtils.i(TAG, "On error response: " + error);
        btnEfectivizar.postDelayed(new Runnable() {
            @Override
            public void run() {
//                                    btnEfectivizar.setEnabled(true);
//                                    estadoAceptarDialogo = false;
//                                    parametrosJson3 = null;

                LogUtils.i(TAG, "FAIL RESPONSE " + error);
                showProgress(false);
                if (error != null) {
                    if (error.getCause() instanceof TimeoutError) {
                        mostrarMensaje(getString(R.string.mensaje_error_timeout));
                    } else {
//                                        mostrarMensaje(getString(R.string.mensaje_error_volley) + error.getMessage());
                        mostrarMensaje("No tiene Conexión a INTERNET");
                    }

                } else {
                    mostrarMensaje(getString(R.string.mensaje_error_volley_default));
                }
            }
        }, ConfigEmizor.TIEMPO_ESPERA_INTENTO_MLS);
    }


    public void efectivizarVentaSoatPlaca(final int proceso) {
        //view.setVisibility(View.GONE);
        LogUtils.i(TAG, "efectivizarVentaSoatPlaca init estadoAceptarDialogo " + estadoAceptarDialogo + " btnEfectivizar.isEnabled() " + btnEfectivizar.isEnabled());

        if ((!btnEfectivizar.isEnabled()) && estadoAceptarDialogo) {
            if (proceso == 1) {
                verificarTiempo(1);
                return;
            }


            EfectivizarVentaActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recolectarDatos();
                    VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();
                    enviarDatos();

                }
            });
        }

    }

    @Override
    public void finish() {
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll("efectivizarVenta");

        super.finish();
    }

    private void mostrarMensaje(final String mensaje) {
        try {
            EfectivizarVentaActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogoEmizor dialogoEmizor = new DialogoEmizor();
                    dialogoEmizor.setTipoDialogo(4);
                    dialogoEmizor.setMensaje(mensaje);
                    dialogoEmizor.setCancelable(false);
                    dialogoEmizor.show(getSupportFragmentManager(), null);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mostrarMensajeConAccion(final String mensaje, final Runnable accionAceptar) {
        try {
            EfectivizarVentaActivity.this.runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(EfectivizarVentaActivity.this);
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

    private void showProgress(final boolean show) {

        try {

            EfectivizarVentaActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    verificarTiempo(0);
                    LogUtils.i(TAG, "VALORES VALORES ");

                    int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                    vistaFull.setVisibility(show ? View.GONE : View.VISIBLE);
                    vistaFull.animate().setDuration(shortAnimTime).alpha(
                            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            vistaFull.setVisibility(show ? View.GONE : View.VISIBLE);
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

                    if (!show) {
                        ((TextView) findViewById(R.id.tvTextoProgress)).setText("");
                    }
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        verificarTiempo(0);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            EfectivizarVentaActivity.this.finish();
        } else {
            try {

                showProgress(false);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void onRealizaAccionDialogEmizor(DialogoEmizor dialogoEmizor, int accion, int tipodialogo) {
        LogUtils.i(TAG, "efectivizarVentaSoatPlaca init estadoAceptarDialogo " + estadoAceptarDialogo + " btnEfectivizar.isEnabled() " + btnEfectivizar.isEnabled());
        dialogoEmizor.getDialog().cancel();

        if (tipodialogo == 3 && parametrosJson3 == null) {

            if (accion == ACCION_ACEPTAR) {
                if ((!estadoAceptarDialogo) && (!btnEfectivizar.isEnabled())) {
                    estadoAceptarDialogo = true;
                    mostrarTexto("DATOS CORRECTOS");
                    mostrarTexto("Obteniendo coordenada GPS ...");
//                    if (longitudeBest != 0 && latitudeBest != 0) {
//
//                        mostrarTexto("Coordenadas obtenidas.");
//                        toggleBestUpdates("parar");
                    efectivizarVentaSoatPlaca(1);

//                    } else {
//
//                        LogUtils.i(TAG, " tiempo espera gps " + tiempoEspera);
//                        miContadorCoordenadaTimer = new MiContadorCoordenadaTimer(tiempoEspera * 1000, 500);
//                        LogUtils.i(TAG, " tiempo espera gps " + tiempoEspera);
//
//                        showProgress(true);
//                        toggleBestUpdates("iniciar");
//
//                    }
                }

            } else {
                showProgress(false);
                btnEfectivizar.setEnabled(true);
            }

        }

        if (tipodialogo == 4) {
            if (errorImp) {
                setResult(RESULT_OK);
                EfectivizarVentaActivity.this.finish();
            }
        }

        if (tipodialogo == 7) {
            if (accion == ACCION_ACEPTAR) {
                mostrarTexto("IMPRIMIR COLILLA");
                //imprimirFactura.procesarColillaVentaSoat(user, efectivizarRespUnivida, fechaImpresion);

                contadorImprimir = 0;

                cantDocsImprimir = 1;
                iniciarImprimir();
            }
        }


    }

//    public class MiContadorCoordenadaTimer extends CountDownTimer {
//        public MiContadorCoordenadaTimer(long startTime, long interval) {
//            super(startTime, interval);
//        }
//
//        @Override
//        public void onFinish() {
//            LogUtils.i(TAG, "milisegundos onFinish ");
//            if (etNit != null) {
//                LogUtils.i(TAG, "milisegundos onFinish " + latitudeBest + " ----- " + longitudeBest);
//                latitudeBest = 0;
//                longitudeBest = 0;
//                mostrarTexto("Coordenadas obtenidas.");
//                toggleBestUpdates("parar");
//                efectivizarVentaSoatPlaca(1);
//            }
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//
//            LogUtils.i(TAG, "milisegundos pasados " + millisUntilFinished);
//        }
//    }

    private void verificarTiempo(final int proceso) {
        if (proceso < 5) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sharedPreferences = getSharedPreferences("pref_datos", Context.MODE_PRIVATE);

                    long timeInicio = sharedPreferences.getLong("fecha_hora_inicio", 0);

                    long timeFechaActual = Calendar.getInstance().getTimeInMillis();

                    long tiempomls = timeFechaActual - timeInicio;

                    LogUtils.i(TAG, "time inicio " + timeInicio + " time fechaactual " + timeFechaActual + " tiem rest " + tiempomls);

                    if (tiempomls > ConfigEmizor.TIEMPO_ESPERA_CIERRE_SESION) {

                        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(EfectivizarVentaActivity.this);

                        controladorSqlite2.eliminarTodoPrincipal();
                        controladorSqlite2.crearTablasPrincipal();

                        setResult(RESULT_CANCELED);

                        EfectivizarVentaActivity.this.finish();

                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putLong("fecha_hora_inicio", Calendar.getInstance().getTimeInMillis());
                        editor.apply();
                        editor.commit();

                        if (proceso == 1) {
                            efectivizarVentaSoatPlaca(12);
                        } else {

                        }

                    }
                }
            });
        }

    }

    private void iniciarImprimir() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                contadorImprimir++;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            imprimirFactura.imprimirFactura2();

                        } catch (ImpresoraErrorException e) {
                            e.printStackTrace();
                            errorImp = true;
                            mostrarMensaje("Error en la impresora.");
                            contadorImprimir = cantDocsImprimir;
                        } catch (NoHayPapelException e) {
                            e.printStackTrace();
                            errorImp = true;
                            mostrarMensaje("No hay papel para imprimir.");
                            contadorImprimir = cantDocsImprimir;
                        } catch (VoltageBajoException e) {
                            e.printStackTrace();
                            errorImp = true;
                            mostrarMensaje("Error de Bateria baja. No podra imprimir con la bateria baja.");
                            contadorImprimir = cantDocsImprimir;
                        } catch (ErrorPapelException e) {
                            e.printStackTrace();
                            errorImp = true;
                            mostrarMensaje("Error en la impresora");
                            contadorImprimir = cantDocsImprimir;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            errorImp = true;
                            mostrarMensaje("Error al imprimir los datos. Si esto persiste comuniquese con el encargado.");
                            contadorImprimir = cantDocsImprimir;
                        }
                    }
                }).start();
            }
        });


    }

    @Override
    public void terminoDeImprimir() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (contadorImprimir < cantDocsImprimir) {
                    mostrarTexto("... TERMINO DE IMPRIMIR FACTURA Y COMPROBANTE");
                    cantDocsImprimir = 1;
                    preguntarAbrir();
                } else {
                    setResult(RESULT_OK);
                    EfectivizarVentaActivity.this.finish();
                }
            }
        });

    }

    private void preguntarAbrir() {
        try {

            imprimirFactura.procesarColillaVentaSoat(user, efectivizarRespUnivida, fechaImpresion);

            DialogoEmizor dialogoEmizor = new DialogoEmizor();
            dialogoEmizor.setTipoDialogo(7);
            dialogoEmizor.setMensaje("Imprimir Colilla?");
            dialogoEmizor.setCancelable(false);
            dialogoEmizor.show(getSupportFragmentManager(), null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void mostrarVentanaPagoQR(EfectivizarFacturaUnivida efectivizarFacturaUnivida) {


        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_qr, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();

        validarParametros(dialog);

        ImageView ivCodigoQR = dialogView.findViewById(R.id.ivCodigoQR);
        ProgressBar progressBar = dialogView.findViewById(R.id.progressBarLoading);

        int width = getResources().getDisplayMetrics().widthPixels;
        int qrSize = (int) (width * 0.9); // 90% del ancho del modal


        ivCodigoQR.getLayoutParams().width = qrSize;
        ivCodigoQR.getLayoutParams().height = qrSize;
        ivCodigoQR.requestLayout();

        Button btnVerificarPago = dialogView.findViewById(R.id.btnVerificarPago);
        Button btnCancelarPago = dialogView.findViewById(R.id.btnCancelarPago);

        btnVerificarPago.setEnabled(false);
        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getApplication());

        String tipoUso = controladorSqlite2.obtenerTipoUso(efectivizarFacturaUnivida.getVehiculoUsoFk()).getDescripcion();
        String tipoVehiculo = controladorSqlite2.obtenerTipoVehiculo(efectivizarFacturaUnivida.getVehiculoTipoFk()).getDescripcion();
        String departamento = controladorSqlite2.obtenerDepartamento(efectivizarFacturaUnivida.getDepartamentoPlazaCirculacionFk()).getDescripcion();


        controladorSqlite2.cerrarConexion();

        TextView tvTipoUso = dialogView.findViewById(R.id.tvTipoUso);
        tvTipoUso.setText("Tipo de Uso: " + tipoUso);
        TextView tvTipoVehiculo = dialogView.findViewById(R.id.tvTipoVehiculo);
        tvTipoVehiculo.setText("Tipo de Vehículo: " + tipoVehiculo);
        TextView tvDepartamento = dialogView.findViewById(R.id.tvDepartamento);
        tvDepartamento.setText("Departamento: " + departamento);
        TextView tvPlaca = dialogView.findViewById(R.id.tvPlaca);
        tvPlaca.setText("Placa: " + efectivizarFacturaUnivida.getVehiculoPlaca());
        TextView tvPrima = dialogView.findViewById(R.id.tvPrima);
        tvPrima.setText("Prima: " + efectivizarFacturaUnivida.getPrima() + " Bs.");
        TextView tvGestion = dialogView.findViewById(R.id.tvGestion);
        tvGestion.setText("Gestión: " + efectivizarFacturaUnivida.getGestionFk());

        /************obtener Qr********************/
        //crear parametros de ejecución
        JSONObject jsonParametrosEjecucion = new JSONObject();
        try {
            jsonParametrosEjecucion.put("FactSucursalFk", efectivizarFacturaUnivida.getSucursalFk());
            jsonParametrosEjecucion.put("PropCelular", efectivizarFacturaUnivida.getPropCelular());
            jsonParametrosEjecucion.put("PropCi", efectivizarFacturaUnivida.getPropCi());
            jsonParametrosEjecucion.put("PropDireccion", efectivizarFacturaUnivida.getPropDireccion());
            jsonParametrosEjecucion.put("PropNit", efectivizarFacturaUnivida.getPropNit());
            jsonParametrosEjecucion.put("PropTelefono", efectivizarFacturaUnivida.getPropTelefono());
            jsonParametrosEjecucion.put("PropTomador", efectivizarFacturaUnivida.getPropTelefono());
            jsonParametrosEjecucion.put("VehiAnio", efectivizarFacturaUnivida.getVehiculoAnio());
            jsonParametrosEjecucion.put("VehiCapacidadCarga", efectivizarFacturaUnivida.getVehiculoCapacidadCarga());
            jsonParametrosEjecucion.put("VehiChasis", efectivizarFacturaUnivida.getVehiculoChasis());
            jsonParametrosEjecucion.put("VehiCilindrada", efectivizarFacturaUnivida.getVehiculoCilindrada());
            jsonParametrosEjecucion.put("VehiColor", efectivizarFacturaUnivida.getVehiculoColor());
            jsonParametrosEjecucion.put("VehiMarca", efectivizarFacturaUnivida.getVehiculoMarca());
            jsonParametrosEjecucion.put("VehiModelo", efectivizarFacturaUnivida.getVehiculoModelo());
            jsonParametrosEjecucion.put("VehiMotor", efectivizarFacturaUnivida.getVehiculoMotor());
            jsonParametrosEjecucion.put("VehiTParPlacaTipo", efectivizarFacturaUnivida.getVehiculoPlacaTipo());
            jsonParametrosEjecucion.put("SoatRosetaNumero", efectivizarFacturaUnivida.getRosetaNumero());
            jsonParametrosEjecucion.put("SoatTParDepartamentoPcFk", efectivizarFacturaUnivida.getDepartamentoPlazaCirculacionFk());
            jsonParametrosEjecucion.put("SoatTParDepartamentoVtFk", efectivizarFacturaUnivida.getDepartamentoVentaFk());
            jsonParametrosEjecucion.put("SoatTParMedioPagoFk", 30);
            jsonParametrosEjecucion.put("SoatTParVehiculoTipoFk", efectivizarFacturaUnivida.getVehiculoTipoFk());
            jsonParametrosEjecucion.put("SoatTParVehiculoUsoFk", efectivizarFacturaUnivida.getVehiculoUsoFk());
            jsonParametrosEjecucion.put("SoatVentaDatosAdi", efectivizarFacturaUnivida.getVentaDatosAdicionales());
            jsonParametrosEjecucion.put("SoatTParGestionFk", efectivizarFacturaUnivida.getGestionFk());
            jsonParametrosEjecucion.put("SoatTParVentaCanalFk", 28);
            jsonParametrosEjecucion.put("VehiPlaca", efectivizarFacturaUnivida.getVehiculoPlaca());

            jsonParametrosEjecucion.put("FactCiComplemento", efectivizarFacturaUnivida.getCiComplemento());//
            jsonParametrosEjecucion.put("FactCorreoCliente", efectivizarFacturaUnivida.getCorreoCliente());//
            jsonParametrosEjecucion.put("FactNitCi", efectivizarFacturaUnivida.getNitCi());
            jsonParametrosEjecucion.put("FactPrima", efectivizarFacturaUnivida.getPrima());
            jsonParametrosEjecucion.put("FactRazonSocial", efectivizarFacturaUnivida.getRazonSocial());
            jsonParametrosEjecucion.put("FactTelefonoCliente", efectivizarFacturaUnivida.getTelefonoCliente());
            jsonParametrosEjecucion.put("FactTipoDocIdentidadFk", efectivizarFacturaUnivida.getTipoDocIdentidadFk());
            jsonParametrosEjecucion.put("SoatVentaCajero", "");
            jsonParametrosEjecucion.put("SoatTIntermediarioFk", 0);

            SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            String numero = sharedPreferences.getString("numero", "0");

            jsonParametrosEjecucion.put("SoatVentaVendedor", user.getUsername());
            jsonParametrosEjecucion.put("SeguridadToken", numero);
            jsonParametrosEjecucion.put("Usuario", user.getUsername());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ObtenerQR", e.toString());
        }


        //fin crear parametros de ejecución

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("codigo_unico", 0);
            jsonRequest.put("tipo_tramite", 4);
            jsonRequest.put("parametros_ejecucion", jsonParametrosEjecucion.toString());

            JSONObject datosQR = new JSONObject();
            datosQR.put("importe", efectivizarFacturaUnivida.getPrima());
            datosQR.put("referencia", "Compra de Soat " + efectivizarFacturaUnivida.getGestionFk());
            datosQR.put("correo_notificacion", efectivizarFacturaUnivida.getCorreoCliente());
            datosQR.put("variable1", efectivizarFacturaUnivida.getVehiculoPlaca());
            datosQR.put("variable2", efectivizarFacturaUnivida.getGestionFk());
            datosQR.put("variable3", JSONObject.NULL);
            datosQR.put("variable4", JSONObject.NULL);
            datosQR.put("variable5", JSONObject.NULL);

            jsonRequest.put("datos_qr", datosQR);

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
            Log.e("ObtenerQR", e.toString());
        }

        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_QR_OBTENER;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {

                        JSONObject jsonResponse = new JSONObject(response);

                        boolean exito = jsonResponse.getBoolean("exito");
                        if (exito) {

                            String mensaje = jsonResponse.getString("mensaje");
                            mostrarTexto(mensaje);

                            progressBar.setVisibility(View.GONE);
                            ivCodigoQR.setVisibility(View.VISIBLE);

                            JSONObject datos = jsonResponse.getJSONObject("datos");
                            String codigoQRBase64 = datos.getString("CodigoQR");
                            double importe = datos.getDouble("Importe");
                            String moneda = datos.getString("Moneda");
                            tramiteSecuencialQr = datos.getString("Secuencial");


                            mostrarQRCode(dialogView, codigoQRBase64);


                            mostrarTexto("Importe: " + importe + " " + moneda);
                            btnVerificarPago.setEnabled(true);
                        } else {
                            String mensajeError = jsonResponse.getString("mensaje");
                            mostrarMensaje(mensajeError);
                            showProgress(false);
                            btnEfectivizar.setEnabled(true);
                            estadoAceptarDialogo = false;
                            parametrosJson3 = null;
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();

                            }
                            Log.e("ObtenerQR", mensajeError + " " + mensajeError);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ObtenerQR", e.toString());

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

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        /**********Fin obtener QR*/


        btnVerificarPago.setOnClickListener(v -> {
            consultarEstadoQr(dialog);
            mostrarTexto("Verificando pago...");
        });

        btnCancelarPago.setOnClickListener(v -> {
            anularQr(dialog);
        });


        dialog.setOnKeyListener((dialogInterface, keyCode, event) -> {
            return keyCode == KeyEvent.KEYCODE_BACK;
        });

        dialog.show();
    }

    private boolean validarParametros(AlertDialog dialog) {
        JSONObject facturaJson = new JSONObject();

        try {

            facturaJson.put("CodigoMetodoPago", 1);
            facturaJson.put("CodigoMonedaPago", "1");
            facturaJson.put("NumeroTarjetaPago", "1");
            facturaJson.put("DescuentoAdicional", 0);
            facturaJson.put("MontoGiftCard", 0);

            JSONObject oeFacturaCabecera = new JSONObject();
            oeFacturaCabecera.put("ClienteRazonSocial", efectivizarFacturaUnivida.getRazonSocial());
            oeFacturaCabecera.put("ClienteCodigoTipoDocIdentidad", efectivizarFacturaUnivida.getTipoDocIdentidadFk());
            oeFacturaCabecera.put("ClienteNumeroDocumento", efectivizarFacturaUnivida.getNitCi());
            oeFacturaCabecera.put("ClienteComplemento", efectivizarFacturaUnivida.getCiComplemento());
            oeFacturaCabecera.put("ClienteCodigo", efectivizarFacturaUnivida.getTipoDocIdentidadFk());
            oeFacturaCabecera.put("ClienteCorreo", efectivizarFacturaUnivida.getCorreoCliente());
            oeFacturaCabecera.put("ClienteTelefono", efectivizarFacturaUnivida.getTelefonoCliente());

            facturaJson.put("OEFacturaCabecera", oeFacturaCabecera);

            JSONArray leFacturaDetalle = new JSONArray();
            JSONObject detalle = new JSONObject();
            detalle.put("Cantidad", 1);
            detalle.put("PrecioUnitario", efectivizarFacturaUnivida.getPrima());
            detalle.put("MontoDescuento", 0.00);

            leFacturaDetalle.put(detalle);

            facturaJson.put("LEFacturaDetalle", leFacturaDetalle);


        } catch (JSONException e) {
            e.printStackTrace();
            System.err.println("Error al construir el JSON: " + e.getMessage());
        }
        JSONObject jsonRequest = new JSONObject();
        try {

            jsonRequest.put("factura_json", facturaJson.toString());

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
            transaccionOrigen.put("tra_ori_cajero", efectivizarFacturaUnivida.getCorreoCliente());

            jsonRequest.put("transaccion_origen", transaccionOrigen);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ObtenerQR", e.toString());
        }
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_QR_VALIDAR_PARAMTEROS;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean exito = jsonResponse.getBoolean("exito");
                        String mensaje = jsonResponse.optString("mensaje", "Operación completada.");

                        if (!exito) {
                            showProgress(false);
                            loadingDialog.hideLoading();

                            btnEfectivizar.setEnabled(true);
                            estadoAceptarDialogo = false;
                            parametrosJson3 = null;
                            runOnUiThread(() -> mostrarMensaje(mensaje));
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();

                            }
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loadingDialog.hideLoading();

                        Log.e("AnularQR", e.toString());
                        runOnUiThread(() -> Toast.makeText(this, "Error al procesar la respuesta.", Toast.LENGTH_LONG).show());
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

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        /**********Fin anular QR*/
        return true;
    }

    private void anularQr(AlertDialog dialog) {
        /************anular Qr********************/
        loadingDialog.showLoading(this, "Anulando QR...");

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
            transaccionOrigen.put("tra_ori_cajero", efectivizarFacturaUnivida.getCorreoCliente());

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
                            runOnUiThread(() -> {
                                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

                                loadingDialog.hideLoading();
                                showProgress(false);
                                btnEfectivizar.setEnabled(true);
                                estadoAceptarDialogo = false;
                                parametrosJson3 = null;
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();

                                }
                            });
                        } else {
                            showProgress(false);
                            loadingDialog.hideLoading();

                            btnEfectivizar.setEnabled(true);
                            estadoAceptarDialogo = false;
                            parametrosJson3 = null;
                            runOnUiThread(() -> Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show());
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();

                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loadingDialog.hideLoading();

                        Log.e("AnularQR", e.toString());
                        runOnUiThread(() -> Toast.makeText(this, "Error al procesar la respuesta.", Toast.LENGTH_LONG).show());
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

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        /**********Fin anular QR*/
    }

    private void consultarEstadoQr(AlertDialog dialog) {
        /************consultar Qr********************/
        loadingDialog.showLoading(this, "Consultando estado QR...");


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
            transaccionOrigen.put("tra_ori_cajero", efectivizarFacturaUnivida.getCorreoCliente());

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
                            runOnUiThread(() -> {

                                loadingDialog.hideLoading();
                                if (estadoSecuencial == 3) {
                                    dialog.dismiss();
                                    mostrarMensajeConAccion(mensaje, () -> {

                                        iniciarVerificacionEfectivizacion(secuencialPago);

                                    });
                                } else {
                                    mostrarMensaje(mensaje);
                                }

                            });


                        } else {
                            showProgress(false);
                            loadingDialog.hideLoading();
                            btnEfectivizar.setEnabled(true);
                            estadoAceptarDialogo = false;
                            parametrosJson3 = null;
                            runOnUiThread(() -> Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show());

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loadingDialog.hideLoading();

                        Log.e("consultarQr", e.toString());
                        runOnUiThread(() -> Toast.makeText(this, "Error al procesar la respuesta.", Toast.LENGTH_LONG).show());
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

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        /**********Fin consultar QR*/
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
        /************verificar efectivización********************/


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
            transaccionOrigen.put("tra_ori_cajero", efectivizarFacturaUnivida.getCorreoCliente());

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

                            runOnUiThread(() -> {
                                if (mensajeEfectivizacion.trim() != "")
                                    mostrarTexto(mensajeEfectivizacion);
                            });
                            if (tParSimpleEstadoEjecucionFk == 2) {
                                isEfectivizado = true;
                                handler.removeCallbacksAndMessages(null);
                                imprimirComprobanteYFactura(tVehiSoatPropFk);

                            }


                        } else {
                            runOnUiThread(() -> {
                                if (mensaje.trim() != "")
                                    mostrarTexto(mensaje);
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Efectivizacion", e.toString());
                        runOnUiThread(() -> Toast.makeText(this, "Error al procesar la respuesta.", Toast.LENGTH_LONG).show());
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

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        /**********Fin verificar efectivización*/
    }

    private void imprimirComprobanteYFactura(long nroComprobante) {
        ObtenerVentaUnivida obtenerVentaUnivida = new ObtenerVentaUnivida();

        obtenerVentaUnivida.setAutorizacionNumero("");
        obtenerVentaUnivida.setGestionFk(-1);
        obtenerVentaUnivida.setNumero(0);
        obtenerVentaUnivida.setNumeroComprobante(nroComprobante);
        obtenerVentaUnivida.setVehiculoPlaca("");
        obtenerVentaUnivida.setVentaCajero("");
        obtenerVentaUnivida.setVentaCanalFk(28);
        obtenerVentaUnivida.setVentaVendedor(user.getUsername());


        final String parametrosJson3 = obtenerVentaUnivida.toString();
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();

        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_OBTENER;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> manejarRespuesta(response),
                error -> manejarError(error)) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return obtenerCuerpoSolicitud(parametrosJson3.toString());
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

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void mostrarQRCode(View dialogView, String base64QR) {
        try {
            byte[] decodedString = Base64.decode(base64QR, Base64.DEFAULT);
            Bitmap qrBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ImageView ivCodigoQR = dialogView.findViewById(R.id.ivCodigoQR);
            ivCodigoQR.setImageBitmap(qrBitmap);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarTexto("Error al decodificar el código QR.");
        }
    }


}
