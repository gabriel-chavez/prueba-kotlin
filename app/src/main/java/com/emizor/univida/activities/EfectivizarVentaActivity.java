package com.emizor.univida.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.emizor.univida.imprime.ImprimirAvisoListener;
import com.emizor.univida.imprime.ImprimirFactura;
import com.emizor.univida.modelo.dominio.univida.parametricas.TipoDocumentoIdentidad;
import com.emizor.univida.modelo.dominio.univida.parametricas.UsoVehiculo;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.ventas.EfectivizarAdicionalUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.EfectivizarFacturaUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.EfectivizarRespUnivida;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.rest.VolleySingleton;
import com.emizor.univida.util.ConfigEmizor;
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

import android.widget.ToggleButton;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_efectivizar_venta);
        // Referencias a los botones QR efectivo
        ToggleButton btnPagoQR = findViewById(R.id.btnPagoQR);
        ToggleButton btnPagoEfectivo = findViewById(R.id.btnPagoEfectivo);

        // Configuración de comportamiento exclusivo
        btnPagoQR.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                btnPagoEfectivo.setChecked(false);
            }
        });

        btnPagoEfectivo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                btnPagoQR.setChecked(false);
            }
        });
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

        btnEfectivizar = findViewById(R.id.btnEfectivizar);

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
                    VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_EFECTIVIZAR_FACTURA_CICLOS_INTER, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

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
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
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
                    stringRequest.setTag("efectivizarVenta");

                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
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

}
