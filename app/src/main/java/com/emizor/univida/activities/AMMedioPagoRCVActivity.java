package com.emizor.univida.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.emizor.univida.R;
import com.emizor.univida.dialogo.DialogoEmizor;
import com.emizor.univida.modelo.dominio.univida.parametricas.Banco;
import com.emizor.univida.modelo.dominio.univida.parametricas.MedioPago;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RcvJsonMedioPagoUnivida;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.util.LogUtils;
import com.emizor.univida.util.ValidarCampo;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


public class AMMedioPagoRCVActivity extends AppCompatActivity implements DialogoEmizor.NotificaDialogEmizorListener {

    private final String TAG = "AMMEDIOPAGORCV";

    private RcvJsonMedioPagoUnivida rcvJsonMedioPagoUnivida;

    private DatePickerDialog datePickerDialog;

    private Date fechaSeleccion;

    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat simpleDateFormat2;

    private Button btnFechaRcv;

    private EditText etReferenciaEfectivizarRcv, etMontoRcv;
    private Spinner spMedioPago, spBancos;
    private User user;
    private MedioPago medioPagoSeleccionado;
    private Banco bancoSeleccionado;

    private int signoPagoRcv;

    private BigDecimal montoPrimaTotal;
    private TextView tvMontoRestante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ammedio_pago_rcv);

        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(this);

        Toolbar toolbar = findViewById(R.id.toolbar_addmediopago);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Bundle bundle = getIntent().getExtras();

        boolean estadoEfectivo = false;

        montoPrimaTotal = new BigDecimal(0);

        if (bundle != null) {

            if (bundle.containsKey("estado_efectivo")) {

                estadoEfectivo = bundle.getBoolean("estado_efectivo");
            }

            if (bundle.containsKey("total_prima")){
                montoPrimaTotal = new BigDecimal(bundle.getDouble("total_prima"));

            }

        }

        user = controladorSqlite2.obtenerUsuario();

        if (user == null){

            controladorSqlite2.cerrarConexion();
            setResult(RESULT_CANCELED);
            AMMedioPagoRCVActivity.this.finish();

            return ;
        }

        tvMontoRestante = findViewById(R.id.tvMontoRestantePrimaRcv);

        tvMontoRestante.setText(String.format("RESTANTE PRIMA(Bs): %s", montoPrimaTotal.toString()));
        fechaSeleccion = Calendar.getInstance().getTime();

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);

                        fechaSeleccion = calendar.getTime();

                        btnFechaRcv.setText(simpleDateFormat.format(fechaSeleccion));


                    }

                }, 1, 1, 1);

        rcvJsonMedioPagoUnivida = new RcvJsonMedioPagoUnivida();

        medioPagoSeleccionado = null;

        btnFechaRcv = findViewById(R.id.btnFechaPagoEfectivizarRcv);

        etMontoRcv = findViewById(R.id.etMontoPagoRcv);
        etReferenciaEfectivizarRcv = findViewById(R.id.etReferenciaEfectivizarRcv);
        //etNumeroMedioPago = findViewById(R.id.etNumeroMedioPago);

        btnFechaRcv.setText(simpleDateFormat.format(fechaSeleccion));

        spMedioPago = findViewById(R.id.spMedioPago);
        spBancos = findViewById(R.id.spBancos);

        List<MedioPago> medioPagos = controladorSqlite2.obtenerMedioPagos();

        if (estadoEfectivo){
            if (medioPagos.contains(new MedioPago(1))){
                medioPagos.remove(new MedioPago(1));
            }
        }

        ArrayAdapter<MedioPago> arrayAdapterMedioPago = new ArrayAdapter<MedioPago>(this, android.R.layout.simple_spinner_dropdown_item, medioPagos);

        spMedioPago.setAdapter(arrayAdapterMedioPago);

        spMedioPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                medioPagoSeleccionado = (MedioPago) parent.getSelectedItem();

                if (medioPagoSeleccionado.getSecuencial() <= 0){
                    medioPagoSeleccionado = null;
                }else{
                    readecuarVista(medioPagoSeleccionado.getDatosCompletos());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bancoSeleccionado = null;

        ArrayAdapter<Banco> arrayAdapterBanco = new ArrayAdapter<Banco>(this, android.R.layout.simple_spinner_dropdown_item, controladorSqlite2.obtenerBancos());

        spBancos.setAdapter(arrayAdapterBanco);

        spBancos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bancoSeleccionado = (Banco) parent.getSelectedItem();

                if (bancoSeleccionado.getSecuencial() <= 0){
                    bancoSeleccionado = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etMontoRcv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String cadena = s.toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        BigDecimal bigDecimal = new BigDecimal(0);
                        if (! cadena.isEmpty()) {
                            bigDecimal = new BigDecimal(cadena);
                        }
                        if (bigDecimal.doubleValue() > 0) {

                            bigDecimal = new BigDecimal(montoPrimaTotal.doubleValue()).subtract(bigDecimal);

                            if (bigDecimal.doubleValue() >= 0) {

                                tvMontoRestante.setText(String.format("RESTANTE PRIMA(Bs): %s", bigDecimal.toString()));
                            }
                        } else {
                            tvMontoRestante.setText(String.format("RESTANTE PRIMA(Bs): %s", montoPrimaTotal.toString()));
                        }

                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        controladorSqlite2.cerrarConexion();

        btnFechaRcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirdatePicker();
            }
        });

        if (bundle != null){
            if (bundle.containsKey("jsonpago")){

                rcvJsonMedioPagoUnivida = new Gson().fromJson(bundle.getString("jsonpago"), RcvJsonMedioPagoUnivida.class);
                llenarDatos();

            }
        }

    }

    private void readecuarVista(String datosCompletosMedioPago){
        View vistaBancoRcv, vistaDatoPagoRcv, vistaFechaPagoRcv;

        vistaBancoRcv = findViewById(R.id.vistaBancoRcv);
        vistaDatoPagoRcv = findViewById(R.id.vistaDatoPagoRcv);
        vistaFechaPagoRcv = findViewById(R.id.vistaFechaPagoRcv);

        TextView tvDatoTitulo;

        tvDatoTitulo = findViewById(R.id.tvDatoTituloPagoRcv);
        String separador = Pattern.quote("|");
        String[] datosCompletos = datosCompletosMedioPago.split(separador);

        try {

            for (String datoss : datosCompletos) {
                LogUtils.i(TAG, " iii " + datoss + " 00 " + datosCompletos.length + " ,, ... || " + datosCompletosMedioPago);
            }

            if (datosCompletos[9].equals("1")) {
                vistaFechaPagoRcv.setVisibility(View.VISIBLE);
                fechaSeleccion = Calendar.getInstance().getTime();

                btnFechaRcv.setText(simpleDateFormat.format(fechaSeleccion));

            } else {
                fechaSeleccion = null;
                vistaFechaPagoRcv.setVisibility(View.GONE);
            }

            if (datosCompletos[8].equals("1")) {
                vistaDatoPagoRcv.setVisibility(View.VISIBLE);

                tvDatoTitulo.setText(datosCompletos[2]);
                etReferenciaEfectivizarRcv.setHint(datosCompletos[2]);
            } else {
                etReferenciaEfectivizarRcv.setText("");
                vistaDatoPagoRcv.setVisibility(View.GONE);
            }

            if (datosCompletos[7].equals("1")) {
                vistaBancoRcv.setVisibility(View.VISIBLE);
            } else {
                spBancos.setSelection(0);
                vistaBancoRcv.setVisibility(View.GONE);
            }

            signoPagoRcv = Integer.parseInt(datosCompletos[4]);

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void abrirdatePicker(){
        Calendar c = Calendar.getInstance();
        c.setTime(fechaSeleccion);
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog

        datePickerDialog.updateDate(mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                setResult(RESULT_CANCELED);
                AMMedioPagoRCVActivity.this.finish();

                break;
        }

        return true;
    }

    private void mostrarMensaje(String mensaje){
        DialogoEmizor dialogoEmizor = new DialogoEmizor();
        dialogoEmizor.setTipoDialogo(4);
        dialogoEmizor.setMensaje(mensaje);
        dialogoEmizor.setCancelable(false);
        dialogoEmizor.show(getSupportFragmentManager(), null);
    }

    private void llenarDatos(){
        int posicionMedio = ((ArrayAdapter<MedioPago>)spMedioPago.getAdapter()).getPosition(new MedioPago(rcvJsonMedioPagoUnivida.getMedioDePagoFk()));

        spMedioPago.setSelection(posicionMedio, true);

        //etNumeroMedioPago.setText(String.valueOf(rcvJsonMedioPagoUnivida.getNumero()));
        etReferenciaEfectivizarRcv.setText(rcvJsonMedioPagoUnivida.getMedioDePagoDato());
        etMontoRcv.setText(String.valueOf(rcvJsonMedioPagoUnivida.getMedioDePagoPrima()));

        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyy");
        try {
            fechaSeleccion = simpleDateFormat2.parse(rcvJsonMedioPagoUnivida.getMedioDePagoFecha());
        } catch (ParseException e) {
            e.printStackTrace();
            fechaSeleccion = Calendar.getInstance().getTime();
        }

        int posicionBanco = ((ArrayAdapter<Banco>)spBancos.getAdapter()).getPosition(new Banco((rcvJsonMedioPagoUnivida.getMedioBancoFk())));

        spBancos.setSelection(posicionBanco, true);

        btnFechaRcv.setText(simpleDateFormat.format(fechaSeleccion));

    }

    private boolean validarDatosPago(){

        if (medioPagoSeleccionado == null){
            mostrarMensaje("Seleccione un Medio de pago.");
            return false;
        }

        String strMonto, strReferencia;

        //etNumeroMedioPago.setError(null);
        etMontoRcv.setError(null);
        etReferenciaEfectivizarRcv.setError(null);

        //strNumero = etNumeroMedioPago.getText().toString();
        strMonto = etMontoRcv.getText().toString();
        strReferencia = etReferenciaEfectivizarRcv.getText().toString();

        boolean resp = true;

        if (strMonto.length() <= 0){
            resp = false;
            etMontoRcv.setError("El monto es necesario");
        }else {

            BigDecimal primaPago = new BigDecimal(strMonto);

            BigDecimal montoTotal = primaPago.subtract(montoPrimaTotal);

            if (!ValidarCampo.validarNumeroDecimal(strMonto)) {
                resp = false;
                etMontoRcv.setError("El monto no es un número correcto.");
            } else if (montoTotal.doubleValue() > 0) {
                resp = false;
                etMontoRcv.setError("El monto debe ser menor o igual al monto PRIMA RESTANTE(" + montoPrimaTotal.toString() + ").");
            }
        }

        if (findViewById(R.id.vistaDatoPagoRcv).getVisibility() == View.VISIBLE){
            if (strReferencia.isEmpty()){
                resp = false;
                etReferenciaEfectivizarRcv.setError("El campo es necesario.");
            }
        }

        if (findViewById(R.id.vistaBancoRcv).getVisibility() == View.VISIBLE){
            if (bancoSeleccionado == null){
                resp = false;
                mostrarMensaje("Debe seleccionar un Banco.");
            }
        }

        return resp;
    }

    public void validarMedioPagoNuevo(View view){

        if (validarDatosPago()){
            rcvJsonMedioPagoUnivida.setMedioSigno(signoPagoRcv);

            if (bancoSeleccionado == null) {
                rcvJsonMedioPagoUnivida.setMedioBancoFk(0);
                rcvJsonMedioPagoUnivida.setMedioBanco("");
            }else{
                rcvJsonMedioPagoUnivida.setMedioBancoFk(bancoSeleccionado.getSecuencial());
                rcvJsonMedioPagoUnivida.setMedioBanco(bancoSeleccionado.getDescripcion());
            }

            rcvJsonMedioPagoUnivida.setMedioDePagoDato(etReferenciaEfectivizarRcv.getText().toString());
            rcvJsonMedioPagoUnivida.setMedioDePagoDescripcion(medioPagoSeleccionado.getDescripcion());
            if (fechaSeleccion != null) {
                rcvJsonMedioPagoUnivida.setMedioDePagoFecha(simpleDateFormat2.format(fechaSeleccion));
            }else{
                rcvJsonMedioPagoUnivida.setMedioDePagoFecha("");
            }
            rcvJsonMedioPagoUnivida.setMedioDePagoFk(medioPagoSeleccionado.getSecuencial());
            rcvJsonMedioPagoUnivida.setMedioDePagoPrima(Double.valueOf(etMontoRcv.getText().toString()));

            if (rcvJsonMedioPagoUnivida.getNumero() == null) {
                rcvJsonMedioPagoUnivida.setNumero(-1);
            }

            Intent intent = new Intent();

            intent.putExtra("jsonpago", new Gson().toJson(rcvJsonMedioPagoUnivida));

            setResult(RESULT_OK, intent);
            AMMedioPagoRCVActivity.this.finish();

        }

    }

    @Override
    public void onRealizaAccionDialogEmizor(DialogoEmizor dialogoEmizor, int accion, int tipodialogo) {
        dialogoEmizor.getDialog().cancel();
    }
}
