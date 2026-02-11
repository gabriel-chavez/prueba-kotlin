package com.emizor.univida.fragmento;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.emizor.univida.InicioActivity;
import com.emizor.univida.R;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.modelo.dominio.univida.parametricas.Departamento;
import com.emizor.univida.modelo.dominio.univida.parametricas.Gestion;
import com.emizor.univida.modelo.dominio.univida.parametricas.TipoPlaca;
import com.emizor.univida.modelo.dominio.univida.parametricas.TipoVehiculo;
import com.emizor.univida.modelo.dominio.univida.parametricas.UsoVehiculo;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.ventas.BuscarPlacaUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.EfectivizarFacturaUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.ObtenerPrimaRespUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.ObtenerPrimaUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.ValidarVendibleInterRespUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.VehiculoDatosInter;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.Conexion;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.rest.VolleySingleton;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LogUtils;
import com.emizor.univida.util.Utils;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NuevaVentaFragment extends Fragment {

    private final String TAG = "NUEVAVENTA";

    private OnFragmentInteractionListener4 mListener;

    private User user;
    private Gestion gestionSeleccionada;
    private UsoVehiculo usoVehiculoSeleccionado;
    private TipoVehiculo tipoVehiculoSeleccionado;
    private Departamento departamentoSeleccionado;

    private EditText etPlacaBuscar;
    private Spinner spTipoUso, spTipoVehiculo, spPlazaCirculacion, spGestion, spTipoPlaca;
    private ValidarVendibleInterRespUnivida validarVendibleInterRespUnivida;

    private View vistaPrima, vistaBuscar;

    private BootstrapButton bsbAceptarBuscarPlaca;
    private BootstrapButton bsbCancelarReemplazoPlaca;

    private TextView tvGestion, tvPlaca,tvNotaPrima;

    private TipoPlaca tipoPlacaSeleccionado;
    /***/
    private double reemplazoPrimaAnterior = 0.0;
    private int reemplazoMedioPagoFkAnterior = 0;
    private long reemplazoTVehiSoatPropFkAnterior = 0L;
    private String reemplazoPlacaNueva = "";
    private boolean esReemplazo = false;

    public NuevaVentaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            reemplazoPrimaAnterior = args.getDouble("reemplazoPrimaAnterior", 0.0);
            reemplazoMedioPagoFkAnterior = args.getInt("reemplazoMedioPagoFkAnterior", 0);
            reemplazoTVehiSoatPropFkAnterior = args.getLong("reemplazoTVehiSoatPropFkAnterior", 0L);
            reemplazoPlacaNueva = args.getString("reemplazoPlacaNueva", "");

            // Verificar si es un reemplazo (si se envió la placa)
            esReemplazo = !reemplazoPlacaNueva.isEmpty();

            LogUtils.i(TAG, "Es reemplazo: " + esReemplazo);
            if (esReemplazo) {
                LogUtils.i(TAG, "Placa para reemplazo: " + reemplazoPlacaNueva);
            }

            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity != null && activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("REEMPLAZO DE VENTA");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vistaNuevaVenta = inflater.inflate(R.layout.fragment_nueva_venta, container, false);

        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getContext());

        user = controladorSqlite2.obtenerUsuario();

        if (user == null){
            controladorSqlite2.cerrarConexion();
            return vistaNuevaVenta;
        }

        etPlacaBuscar = vistaNuevaVenta.findViewById(R.id.etPlacaBuscar);
        tvNotaPrima=vistaNuevaVenta.findViewById(R.id.tvNotaPrima);
        gestionSeleccionada = null;
        usoVehiculoSeleccionado = null;
        tipoVehiculoSeleccionado = null;

        // GESTION
        spGestion  = vistaNuevaVenta.findViewById(R.id.spGestion);

        List<Gestion> listGesti = controladorSqlite2.obtenerGestiones();

        ArrayAdapter<Gestion> arrayAdapter = new ArrayAdapter<Gestion>(getContext(), android.R.layout.simple_spinner_dropdown_item, listGesti);

        spGestion.setAdapter(arrayAdapter);

        if (listGesti.size() == 1){
            gestionSeleccionada = listGesti.get(0);
        }else{
            spGestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    LogUtils.i(TAG,"on item select inicia spGestion" );

                    gestionSeleccionada = (Gestion) parent.getSelectedItem();

                    if (gestionSeleccionada.getSecuencial() <= 0){
                        gestionSeleccionada = null;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        //FIUN GESTION

        // TIPO PLACA
        spTipoPlaca  = vistaNuevaVenta.findViewById(R.id.spTipoPlaca);

        List<TipoPlaca> listTipoPlaca = controladorSqlite2.obtenerTipoPlacas();

        ArrayAdapter<TipoPlaca> arrayAdapterTipoPlaca = new ArrayAdapter<TipoPlaca>(getContext(), android.R.layout.simple_spinner_dropdown_item, listTipoPlaca);

        spTipoPlaca.setAdapter(arrayAdapterTipoPlaca);

        spTipoPlaca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.i(TAG,"on item select inicia spTipoPlaca" );

                tipoPlacaSeleccionado = (TipoPlaca) parent.getSelectedItem();

                etPlacaBuscar.setText("");
                if (esReemplazo && !reemplazoPlacaNueva.isEmpty()) {
                    etPlacaBuscar.setText(reemplazoPlacaNueva);
                }

                setttingTipoPlacaSelected(tipoPlacaSeleccionado);

                if (tipoPlacaSeleccionado.getSecuencial() < 0){
                    tipoPlacaSeleccionado = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //FIN TIPO PLACA
        bsbCancelarReemplazoPlaca = vistaNuevaVenta.findViewById(R.id.bsbCancelarReemplazoPlaca);
        bsbCancelarReemplazoPlaca.setVisibility(View.GONE);
        tvNotaPrima.setVisibility(View.GONE);
        /*************************PARA REEMPLAZO********************/
        if (esReemplazo && !reemplazoPlacaNueva.isEmpty()) {
            etPlacaBuscar.setText(reemplazoPlacaNueva);
            etPlacaBuscar.setEnabled(false);
            spTipoPlaca.setEnabled(false);
            bsbCancelarReemplazoPlaca.setVisibility(View.VISIBLE);
            tvNotaPrima.setVisibility(View.VISIBLE);
        }
        /***********************************************************/

        // USO VEHICULO
        spTipoUso  = vistaNuevaVenta.findViewById(R.id.spTipoUso);

        ArrayAdapter<UsoVehiculo> arrayAdapterTipoUso = new ArrayAdapter<UsoVehiculo>(getContext(), android.R.layout.simple_spinner_dropdown_item, controladorSqlite2.obtenerTipoUsos());

        spTipoUso.setAdapter(arrayAdapterTipoUso);
        spTipoUso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.i(TAG,"on item select inicia spTipoUso" );

                usoVehiculoSeleccionado = (UsoVehiculo) parent.getSelectedItem();
                LogUtils.i(TAG,"on item select spTipoUso " + new Gson().toJson(usoVehiculoSeleccionado));

                if (usoVehiculoSeleccionado.getSecuencial() <= 0){
                    usoVehiculoSeleccionado = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //FIUN USO VEHICULO

        // TIPO VEHICULO
        spTipoVehiculo  = vistaNuevaVenta.findViewById(R.id.spTipoVehiculo);

        ArrayAdapter<TipoVehiculo> arrayAdapterTipoVehiculo = new ArrayAdapter<TipoVehiculo>(getContext(), android.R.layout.simple_spinner_dropdown_item, controladorSqlite2.obtenerTipoVehiculos());

        spTipoVehiculo.setAdapter(arrayAdapterTipoVehiculo);
        spTipoVehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.i(TAG,"on item select inicia spTipoVehiculo" );

                tipoVehiculoSeleccionado = (TipoVehiculo) parent.getSelectedItem();

                if (tipoVehiculoSeleccionado.getSecuencial() <= 0){
                    tipoVehiculoSeleccionado = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //FIUN TIPO VEHICULO

        // TIPO PLAZA CIRCULACION
        spPlazaCirculacion  = vistaNuevaVenta.findViewById(R.id.spPlazaCirculacion);

        ArrayAdapter<Departamento> arrayAdapterPlazaCirculacion = new ArrayAdapter<Departamento>(getContext(), android.R.layout.simple_spinner_dropdown_item, controladorSqlite2.obtenerDepartamentos());

        spPlazaCirculacion.setAdapter(arrayAdapterPlazaCirculacion);
        spPlazaCirculacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.i(TAG,"on item select inicia spPlazaCirculacion" );

                departamentoSeleccionado = (Departamento) parent.getSelectedItem();

                if (departamentoSeleccionado.getCodigo() == "-1"){
                    departamentoSeleccionado = null;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //FIUN PLAZA CIRCULACION

        controladorSqlite2.cerrarConexion();

        vistaPrima = vistaNuevaVenta.findViewById(R.id.vistaObtenerPrima);
        vistaBuscar = vistaNuevaVenta.findViewById(R.id.vistaBuscar);

        tvGestion = vistaNuevaVenta.findViewById(R.id.tvGestion);
        tvPlaca = vistaNuevaVenta.findViewById(R.id.tvPlaca);

        tvGestion.setText("GESTIÓN: ");
        tvPlaca.setText("PLACA: ");

        bsbAceptarBuscarPlaca = vistaNuevaVenta.findViewById(R.id.bsbAceptarBuscarPlaca);
        BootstrapButton bsbAceptarCalcularPrima = vistaNuevaVenta.findViewById(R.id.bsbAceptarCalcularPrima);
        final BootstrapButton bsbCancelarCalcularPrima = vistaNuevaVenta.findViewById(R.id.bsbCancelarCalcularPrima);

        bsbAceptarBuscarPlaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etPlacaBuscar.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                buscarPlaca();
            }
        });

        bsbAceptarCalcularPrima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularPrima();
            }
        });

        bsbCancelarCalcularPrima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetearBusqueda();
            }
        });
        bsbCancelarReemplazoPlaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetearBusqueda();
            }
        });

        return vistaNuevaVenta;
    }

    private void resetearBusqueda(){
        vistaPrima.setVisibility(View.GONE);
        vistaBuscar.setVisibility(View.VISIBLE);
        spGestion.setEnabled(true);
        spGestion.setSelection(0);
        departamentoSeleccionado = null;
        try {
            int posicionDepartamento = ((ArrayAdapter<Departamento>) spPlazaCirculacion.getAdapter()).getPosition(new Departamento(user.getDatosUsuario().getSucursalIdDepartamento()));
            spPlazaCirculacion.setSelection(posicionDepartamento, true);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        tipoVehiculoSeleccionado = null;
        spTipoVehiculo.setSelection(0);
        usoVehiculoSeleccionado = null;
        spTipoUso.setSelection(0);
        tvGestion.setText("GESTIÓN: ");
        tvPlaca.setText("PLACA: ");

        etPlacaBuscar.setEnabled(true);
        etPlacaBuscar.setText("");
        spTipoPlaca.setEnabled(true);
        bsbCancelarReemplazoPlaca.setVisibility(View.GONE);

        reemplazoPrimaAnterior = 0.0;
        reemplazoMedioPagoFkAnterior = 0;
        reemplazoTVehiSoatPropFkAnterior = 0L;
        reemplazoPlacaNueva = "";
        esReemplazo = false;
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle("NUEVA VENTA");
        }
        tvNotaPrima.setVisibility(View.GONE);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener4) {
            mListener = (OnFragmentInteractionListener4) context;
            try {
                mListener.onRegisterFragment(this, OnFragmentInteractionListener4.VISTA_NUEVA_VENTA);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        try {
            VolleySingleton.getInstance(getContext()).getRequestQueue().cancelAll("buscarPlaca");
            VolleySingleton.getInstance(getContext()).getRequestQueue().cancelAll("calcularPrima");
            super.onDetach();
            mListener = null;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void buscarPlaca(){

        if (getActivity() != null)
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_INICIAR_CAPTURA_LOCACION, null);
                if(Utils.validarEstadoPermisoLocacion(getContext())){
                    if(Utils.isLocationEnabled(getContext())){
                        if (! Conexion.estaConectado(getContext())){
                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No tiene conexión a internet.");
                            return;
                        }

                        if (gestionSeleccionada == null){
                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Debe seleccionar una gestión valida.");
                            return;
                        }

                        if (tipoPlacaSeleccionado == null){
                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Debe seleccionar un Tipo de Placa.");
                            return;
                        }

                        final String placa = etPlacaBuscar.getText().toString().trim();
                        if (placa.length() > 0){
                            tvPlaca.setError(null);

                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.TRUE);

                            final BuscarPlacaUnivida buscarPlacaUnivida = new BuscarPlacaUnivida();

                            buscarPlacaUnivida.setGestionFk(gestionSeleccionada.getSecuencial());
                            buscarPlacaUnivida.setVehiculoPlaca(placa);
                            buscarPlacaUnivida.setVentaCajero("");
                            buscarPlacaUnivida.setVentaCanalFk(28);
                            buscarPlacaUnivida.setVentaVendedor(user.getUsername());
                            buscarPlacaUnivida.setVehiculoPlacaTipo(tipoPlacaSeleccionado.getSecuencial());

                            final String parametrosJson3 = buscarPlacaUnivida.toString();
                            VolleySingleton.getInstance(getContext()).getRequestQueue().getCache().clear();

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_VALIDARVENDIBLE_OBTENER_DATOS_INTER, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    LogUtils.i(TAG, " ValidarVendibleInterRespUnivida OK RESPONSE  " + response);
                                    validarVendibleInterRespUnivida = null;
                                    try {

                                        validarVendibleInterRespUnivida = new Gson().fromJson(response, ValidarVendibleInterRespUnivida.class);

                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                    }

                                    if (validarVendibleInterRespUnivida != null) {
                                        if (validarVendibleInterRespUnivida.getExito()) {

                                            if (validarVendibleInterRespUnivida.getDatos() != null && validarVendibleInterRespUnivida.getDatos().getSoatRosetaNumero() == null){

                                                validarVendibleInterRespUnivida.getDatos().setSoatRosetaNumero("0");

                                            }

                                            vistaBuscar.setVisibility(View.GONE);
                                            vistaPrima.setVisibility(View.VISIBLE);

                                            tvGestion.setText(String.format("GESTIÓN: %s", String.valueOf(gestionSeleccionada.getSecuencial())));
                                            tvPlaca.setText(String.format(tipoPlacaSeleccionado.getDescripcion() + ": %s", placa));

                                            int posicionTipoUso, posicionTipoVehiculo, posicionDepartamento;

                                            if (validarVendibleInterRespUnivida.getDatos() != null) {

                                                LogUtils.i(TAG, " datos no nulos ");
                                                posicionDepartamento = ((ArrayAdapter<Departamento>) spPlazaCirculacion.getAdapter()).getPosition(new Departamento(validarVendibleInterRespUnivida.getDatos().getSoatDepartamentoPcFk()));
                                                posicionTipoUso = ((ArrayAdapter<UsoVehiculo>) spTipoUso.getAdapter()).getPosition(new UsoVehiculo(validarVendibleInterRespUnivida.getDatos().getSoatVehiculoUsoFk()));
                                                posicionTipoVehiculo = ((ArrayAdapter<TipoVehiculo>) spTipoVehiculo.getAdapter()).getPosition(new TipoVehiculo(validarVendibleInterRespUnivida.getDatos().getSoatVehiculoTipoFk()));

                                            } else {

                                                posicionTipoUso = 0;
                                                posicionTipoVehiculo = 0;
                                                posicionDepartamento = ((ArrayAdapter<Departamento>) spPlazaCirculacion.getAdapter()).getPosition(new Departamento(user.getDatosUsuario().getSucursalIdDepartamento()));

                                                ConfigEmizor.mostrarMensaje(getContext(), getLayoutInflater(), null, tipoPlacaSeleccionado.getDescripcion() + " nueva, crear nuevo registro.");
                                            }

                                            spTipoUso.setSelection(posicionTipoUso);
                                            spTipoVehiculo.setSelection(posicionTipoVehiculo);
                                            spPlazaCirculacion.setSelection(posicionDepartamento);

                                            //cambio (martes 11-12-2018 9:32am) validar que sea mayor a 0
                                            if (posicionTipoUso > 0) {
                                                usoVehiculoSeleccionado = ((ArrayAdapter<UsoVehiculo>) spTipoUso.getAdapter()).getItem(posicionTipoUso);
                                            }
                                            //cambio (martes 11-12-2018 9:32am) validar que sea mayor a 0
                                            if (posicionTipoVehiculo > 0) {
                                                tipoVehiculoSeleccionado = ((ArrayAdapter<TipoVehiculo>) spTipoVehiculo.getAdapter()).getItem(posicionTipoVehiculo);
                                            }

                                            //cambio (martes 11-12-2018 9:32am) validar que sea mayor a 0
                                            if (posicionDepartamento > 0) {
                                                departamentoSeleccionado = ((ArrayAdapter<Departamento>) spPlazaCirculacion.getAdapter()).getItem(posicionDepartamento);
                                            }

                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);

                                        } else {
                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, validarVendibleInterRespUnivida.getMensaje());

                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                        }

                                    }else{
                                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Datos nulos e incompatibles en respuesta del servicio.");

                                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    LogUtils.i(TAG, "FAIL RESPONSE " + error);

                                    if (error != null){
                                        if (error.getCause() instanceof TimeoutError){
                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, getString(R.string.mensaje_error_timeout));
                                        } else {
//                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, getString(R.string.mensaje_error_volley) + error.getMessage());
                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No tiene Conexión a INTERNET");
                                        }
                                    }else{
                                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, getString(R.string.mensaje_error_volley_default));
                                    }

                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);

                                }
                            }){

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
                                public String getBodyContentType(){
                                    return "application/json; charset=utf-8";
                                }
                            };

                            stringRequest.setShouldCache(false);

                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            stringRequest.setTag("buscarPlaca");

                            VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

                        }else{
                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Es necesario un(a) " + tipoPlacaSeleccionado.getDescripcion() + " para realizar la búsqueda.");

                        }
                    }

                }

            }
        });

    }

    public void calcularPrima(){
        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_INICIAR_CAPTURA_LOCACION, null);

        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (! Conexion.estaConectado(getContext())){
                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No tiene conexión a internet.");
                        return;
                    }

                    if (gestionSeleccionada == null){
                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Debe seleccionar una gestión valida.");
                        return;
                    }

                    if (tipoVehiculoSeleccionado == null){
                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Debe seleccionar un Tipo de Vehiculo.");
                        return;
                    }
                    if (usoVehiculoSeleccionado == null){
                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Debe seleccionar un Tipo de Uso.");
                        return;
                    }

                    if (departamentoSeleccionado == null){
                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Debe seleccionar una plaza de circulacion.");
                        return;
                    }

                    String placa = etPlacaBuscar.getText().toString().trim();
                    if (placa.length() > 0){

                        ObtenerPrimaUnivida obtenerPrimaUnivida = new ObtenerPrimaUnivida();

                        if (departamentoSeleccionado != null) {
                            obtenerPrimaUnivida.setDepartamentoPlazaCirculacionFk(departamentoSeleccionado.getCodigo());
                        }else{
                            obtenerPrimaUnivida.setDepartamentoPlazaCirculacionFk("");
                        }
                        obtenerPrimaUnivida.setGestionFk(gestionSeleccionada.getSecuencial());
                        obtenerPrimaUnivida.setVehiculoPlaca(etPlacaBuscar.getText().toString());
                        obtenerPrimaUnivida.setVehiculoTipoFk(tipoVehiculoSeleccionado.getSecuencial());
                        obtenerPrimaUnivida.setVehiculoUsoFk(usoVehiculoSeleccionado.getSecuencial());
                        obtenerPrimaUnivida.setVentaCanalFk(28);
                        obtenerPrimaUnivida.setVentaVendedor(user.getUsername());
                        obtenerPrimaUnivida.setVentaCajero("");

                        final String parametrosJson3 = obtenerPrimaUnivida.toString();
                        VolleySingleton.getInstance(getContext()).getRequestQueue().getCache().clear();

                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.TRUE);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_OBTENER_PRIMA, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                LogUtils.i(TAG, "OK RESPONSE " + response);
                                ObtenerPrimaRespUnivida obtenerPrimaRespUnivida = null;

                                try {
                                    obtenerPrimaRespUnivida = new Gson().fromJson(response, ObtenerPrimaRespUnivida.class);
                                }catch (Exception ex){
                                    ex.printStackTrace();
                                }

                                if (obtenerPrimaRespUnivida != null) {

                                    if (obtenerPrimaRespUnivida.getExito()) {

                                        if (obtenerPrimaRespUnivida.getMontoPrima() != null && obtenerPrimaRespUnivida.getMontoPrima() > 0) {

                                            if(esReemplazo && obtenerPrimaRespUnivida.getMontoPrima()!=reemplazoPrimaAnterior){
                                                ((PrincipalActivity) getContext()).mostrarLoading(false);

                                                int montoPrima = obtenerPrimaRespUnivida.getMontoPrima();

                                                new AlertDialog.Builder(getContext())
                                                        .setTitle("Atención")
                                                        .setMessage(
                                                                "Se ha detectado una inconsistencia en las primas.\n\n" +
                                                                        "Prima anterior: Bs " + String.format("%.2f", reemplazoPrimaAnterior) + "\n" +
                                                                        "Prima actual calculada: Bs " + String.format("%.2f", (double) montoPrima) + "\n\n" +
                                                                        "Por favor, verifique la información antes de continuar."
                                                        )
                                                        .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                                                        .show();
                                                return;
                                            }

                                            EfectivizarFacturaUnivida efectivizarFacturaUnivida = new EfectivizarFacturaUnivida();

                                            efectivizarFacturaUnivida.setDepartamentoPlazaCirculacionFk(departamentoSeleccionado.getCodigo());
                                            efectivizarFacturaUnivida.setDepartamentoVentaFk(user.getDatosUsuario().getSucursalIdDepartamento());
                                            efectivizarFacturaUnivida.setGestionFk(gestionSeleccionada.getSecuencial());
                                            efectivizarFacturaUnivida.setPrima(obtenerPrimaRespUnivida.getMontoPrima());
                                            efectivizarFacturaUnivida.setSucursalFk(user.getDatosUsuario().getSucursalCodigo());
                                            efectivizarFacturaUnivida.setVehiculoPlaca(etPlacaBuscar.getText().toString().trim());
                                            efectivizarFacturaUnivida.setVehiculoTipoFk(tipoVehiculoSeleccionado.getSecuencial());
                                            efectivizarFacturaUnivida.setVehiculoUsoFk(usoVehiculoSeleccionado.getSecuencial());
                                            efectivizarFacturaUnivida.setCorreoCliente(null);
                                            efectivizarFacturaUnivida.setVentaDatosAdicionales(null);
                                            efectivizarFacturaUnivida.setTelefonoCliente(null);
                                            efectivizarFacturaUnivida.setRazonSocial(null);
                                            efectivizarFacturaUnivida.setRosetaNumero(null);
                                            efectivizarFacturaUnivida.setNitCi(null);
                                            efectivizarFacturaUnivida.setVehiculoPlacaTipo(tipoPlacaSeleccionado.getSecuencial());
                                            /*para reemplazo*/
                                            efectivizarFacturaUnivida.setMedioPagoFk(reemplazoMedioPagoFkAnterior);
                                            efectivizarFacturaUnivida.setVehiSoatPropSecuencialRevertir(reemplazoTVehiSoatPropFkAnterior);

                                            /**/

                                            Object [] datos = new Object[2];

                                            if (validarVendibleInterRespUnivida.getDatos() != null) {

                                                VehiculoDatosInter vehiculoDatosInter = validarVendibleInterRespUnivida.getDatos();

                                                if (vehiculoDatosInter.getSoatRosetaNumero() != null) {
                                                    efectivizarFacturaUnivida.setRosetaNumero(Long.valueOf(validarVendibleInterRespUnivida.getDatos().getSoatRosetaNumero()));
                                                }

                                                //PROPIETARIO

                                                if (vehiculoDatosInter.getPropCelular() != null){
                                                    efectivizarFacturaUnivida.setPropCelular(vehiculoDatosInter.getPropCelular());
                                                }

                                                if (vehiculoDatosInter.getPropCi() != null){
                                                    efectivizarFacturaUnivida.setPropCi(String.valueOf(vehiculoDatosInter.getPropCi()));
                                                }

                                                if (vehiculoDatosInter.getPropDireccion() != null){
                                                    efectivizarFacturaUnivida.setPropDireccion(vehiculoDatosInter.getPropDireccion());
                                                }

                                                if (vehiculoDatosInter.getPropNit() != null){
                                                    efectivizarFacturaUnivida.setPropNit(String.valueOf(vehiculoDatosInter.getPropNit()));
                                                }

                                                if (vehiculoDatosInter.getPropTelefono() != null){
                                                    efectivizarFacturaUnivida.setPropTelefono(vehiculoDatosInter.getPropTelefono());
                                                }

                                                if (vehiculoDatosInter.getPropTomador() != null){
                                                    efectivizarFacturaUnivida.setPropTomador(vehiculoDatosInter.getPropTomador());
                                                }

                                                if (vehiculoDatosInter.getPropCelular() != null){
                                                    efectivizarFacturaUnivida.setPropCelular(vehiculoDatosInter.getPropCelular());
                                                }

                                                // PROPIETARIO

                                                // VEHICULO

                                                if (vehiculoDatosInter.getVehiculoAnio() != null){
                                                    efectivizarFacturaUnivida.setVehiculoAnio(Integer.valueOf(vehiculoDatosInter.getVehiculoAnio()));
                                                }

                                                if (vehiculoDatosInter.getVehiculoColor() != null){
                                                    efectivizarFacturaUnivida.setVehiculoColor(vehiculoDatosInter.getVehiculoColor());
                                                }

                                                if (vehiculoDatosInter.getVehiculoMarca() != null){
                                                    efectivizarFacturaUnivida.setVehiculoMarca(vehiculoDatosInter.getVehiculoMarca());
                                                }

                                                if (vehiculoDatosInter.getVehiculoModelo() != null){
                                                    efectivizarFacturaUnivida.setVehiculoModelo(vehiculoDatosInter.getVehiculoModelo());
                                                }

                                                if (vehiculoDatosInter.getVehiculoMotor() != null){
                                                    efectivizarFacturaUnivida.setVehiculoMotor(vehiculoDatosInter.getVehiculoMotor());
                                                }

                                                if (vehiculoDatosInter.getVahiculoChasis() != null){
                                                    efectivizarFacturaUnivida.setVehiculoChasis(vehiculoDatosInter.getVahiculoChasis());
                                                }

                                                if (vehiculoDatosInter.getVehiculoCapacidadCarga() != null){
                                                    efectivizarFacturaUnivida.setVehiculoCapacidadCarga((vehiculoDatosInter.getVehiculoCapacidadCarga() == null ? '0': Double.parseDouble(vehiculoDatosInter.getVehiculoCapacidadCarga())));
                                                }

                                                if (vehiculoDatosInter.getVehiculoCilindrada() != null){
                                                    efectivizarFacturaUnivida.setVehiculoCilindrada((vehiculoDatosInter.getVehiculoCilindrada() == null ? '0': Integer.parseInt(vehiculoDatosInter.getVehiculoCilindrada())));
                                                }

                                                //VEHICULO

                                                if (validarVendibleInterRespUnivida.getDatos().getSoatRosetaNumero().equals("0")){
                                                    datos[1] = Boolean.TRUE;
                                                }else {
                                                    datos[1] = Boolean.FALSE;
                                                }
                                            }else{
                                                datos[1] = Boolean.TRUE;
                                            }

                                            datos[0] = efectivizarFacturaUnivida;

                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_VISTA_EFECTIVIZAR, datos);

                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);


                                        }

                                    } else {
                                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, obtenerPrimaRespUnivida.getMensaje());

                                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                    }


                                }else{

                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Sin datos en la respuesta, datos NULOS.");
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                LogUtils.i(TAG, "FAIL RESPONSE " + error);

                                if (error != null){
                                    if (error.getCause() instanceof TimeoutError){
                                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, getString(R.string.mensaje_error_timeout));
                                    } else {
//                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, getString(R.string.mensaje_error_volley) + error.getMessage());
                                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No tiene Conexión a INTERNET");
                                    }
                                }else{
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, getString(R.string.mensaje_error_volley_default));
                                }

                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);

                            }
                        }){

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
                            public String getBodyContentType(){
                                return "application/json; charset=utf-8";
                            }
                        };

                        stringRequest.setShouldCache(false);

                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        stringRequest.setTag("calcularPrima");

                        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

                    }else{
                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Es necesario un(a) " + tipoPlacaSeleccionado.getDescripcion() + " para realizar la búsqueda.");
                    }
                }
            });

    }

    private void setttingTipoPlacaSelected(TipoPlaca tipoPlaca){
        try{
            String criterioBusqueda ="(Criterio de Búsqueda)";
            if (tipoPlaca != null){
                criterioBusqueda = tipoPlaca.getDescripcion();
            }
            ((TextView)NuevaVentaFragment.this.getView().findViewById(R.id.txtNumeroPlaca)).setText("Número de " + criterioBusqueda + ": ");
            etPlacaBuscar.setHint("Ingrese un número de " + criterioBusqueda);
            bsbAceptarBuscarPlaca.setBootstrapText(new BootstrapText.Builder(NuevaVentaFragment.this.getContext())
                    .addFontAwesomeIcon(FontAwesome.FA_SEARCH)
                    .addText(" BUSCAR " + criterioBusqueda)
                    .build());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
