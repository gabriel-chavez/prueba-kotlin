package com.emizor.univida.fragmento;

import android.app.DatePickerDialog;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.emizor.univida.R;
import com.emizor.univida.adapter.ListaVentaAdapter;
import com.emizor.univida.adapter.RecyclerViewOnItemCickListener;
import com.emizor.univida.dialogo.DialogoEmizor;
import com.emizor.univida.excepcion.ErrorPapelException;
import com.emizor.univida.excepcion.ImpresoraErrorException;
import com.emizor.univida.excepcion.NoHayPapelException;
import com.emizor.univida.excepcion.VoltageBajoException;
import com.emizor.univida.imprime.ImprimirAvisoListener;
import com.emizor.univida.imprime.ImprimirFactura;
import com.emizor.univida.modelo.dominio.univida.RespUnivida;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.ventas.EfectivizarRespUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.ListarVentaRespUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.ListarVentaUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.ObtenerVentaUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.SoatDatosVentum;
import com.emizor.univida.modelo.dominio.univida.ventas.SolicitarReversionUnivida;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.rest.VolleySingleton;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LogUtils;
import com.emizor.univida.util.SoftKeyboard;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaVentasFragment extends Fragment implements ImprimirAvisoListener, DialogoEmizor.NotificaDialogEmizorListener {

    private final String TAG = "LISTAVENTAS";

    private DatePickerDialog datePickerDialog;

    private OnFragmentInteractionListener4 mListener;

    private Date fechaSeleccion;

    private Button btnFechaListaVenta;
    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat simpleDateFormat2;

    private ListarVentaUnivida listarVentaUnivida;
    private User user;

    private ListaVentaAdapter listaVentaAdapter;
    private RecyclerView rvListaVentas;
    private LinearLayoutManager linearLayoutManager;
    private SoatDatosVentum soatDatosVentumSeleccionado;
    private ImprimirFactura imprimirFactura;
    private SoftKeyboard softKeyboard;
    private View vistaDatos, vistaFechabuscar;
    private EditText etBuscarPlaca;
    private boolean impColilla;
    private EfectivizarRespUnivida efectivizarRespUnivida;
    private Date fechaImpresion;
    private TextView tvTotalMontoListaVenta, tvCantidadVendidasListaVenta, tvCantidadValidasListaVenta, tvCantidadAnuladasListaVenta, tvCantidadRevertidosListaVenta;

    public ListaVentasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vistaListaVenta = inflater.inflate(R.layout.fragment_lista_ventas, container, false);;

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        impColilla = false;

        efectivizarRespUnivida = null;

        datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);

                            fechaSeleccion = calendar.getTime();

                            btnFechaListaVenta.setText(simpleDateFormat.format(fechaSeleccion));

                    }

                }, 1, 1, 1);

        fechaSeleccion = Calendar.getInstance().getTime();

        btnFechaListaVenta = vistaListaVenta.findViewById(R.id.btnFechaListaVenta);
        btnFechaListaVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirdatePicker();
            }
        });

        rvListaVentas = vistaListaVenta.findViewById(R.id.rvListaVentas);

        listaVentaAdapter = new ListaVentaAdapter(getContext(), new ArrayList<SoatDatosVentum>(), new RecyclerViewOnItemCickListener() {
            @Override
            public void onClick(View view, int position, int accionTipo) {

                soatDatosVentumSeleccionado = listaVentaAdapter.getSoatDatosVentum(position);

                abrirMenuItems();
            }
        });

        vistaDatos = vistaListaVenta.findViewById(R.id.linearLayout4);

        linearLayoutManager = new LinearLayoutManager(vistaListaVenta.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvListaVentas.setLayoutManager(linearLayoutManager);

        rvListaVentas.setAdapter(listaVentaAdapter);
        vistaFechabuscar = vistaListaVenta.findViewById(R.id.vistaFechabuscar);

        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getContext());

        btnFechaListaVenta.setText(simpleDateFormat.format(fechaSeleccion));

        tvTotalMontoListaVenta = vistaListaVenta.findViewById(R.id.tvTotalMontoListaVenta);
        tvCantidadVendidasListaVenta = vistaListaVenta.findViewById(R.id.tvCantidadVendidasListaVenta);
        tvCantidadValidasListaVenta = vistaListaVenta.findViewById(R.id.tvCantidadValidasListaVenta);
        tvCantidadAnuladasListaVenta = vistaListaVenta.findViewById(R.id.tvCantidadAnuladasListaVenta);
        tvCantidadRevertidosListaVenta = vistaListaVenta.findViewById(R.id.tvCantidadRevertidosListaVenta);

        ImageButton ibBuscar = vistaListaVenta.findViewById(R.id.ibBuscar);

        ibBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarListaVentas();
            }
        });

        user = controladorSqlite2.obtenerUsuario();

        if (user == null){
            controladorSqlite2.cerrarConexion();
            return vistaListaVenta;
        }

        listarVentaUnivida = new ListarVentaUnivida();

        listarVentaUnivida.setGestionFk(null);
        listarVentaUnivida.setVehiculoPlaca("");
        listarVentaUnivida.setVentaFecha(simpleDateFormat.format(fechaSeleccion));
        listarVentaUnivida.setVentaVendedor(user.getUsername());

        imprimirFactura = ImprimirFactura.obtenerImpresora(getContext());
        imprimirFactura.setAvisoListener(this);

        simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        controladorSqlite2.guardarFechaUso(simpleDateFormat2.format(Calendar.getInstance().getTime()));

        controladorSqlite2.cerrarConexion();

        etBuscarPlaca = vistaListaVenta.findViewById(R.id.etBuscarPlaca);

        etBuscarPlaca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listaVentaAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etBuscarPlaca.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                LogUtils.i(TAG, "actionId " + actionId + ", keyevent " + event + ", v " + v + ", imeActionDone " + EditorInfo.IME_ACTION_DONE);

                return false;
            }
        });

        etBuscarPlaca.setSelectAllOnFocus(true);
        etBuscarPlaca.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                LogUtils.i(TAG, "v " + v + ", keyCode " + keyCode + ", event " + event);
                return false;
            }
        });

        /*
Somewhere else in your code
*/
        ConstraintLayout mainLayout = vistaListaVenta.findViewById(R.id.vistaPrinciapVentaLista); // You must use your root layout
        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);

/*
Instantiate and pass a callback
*/

        softKeyboard = new SoftKeyboard(mainLayout, im);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged()
        {

            @Override
            public void onSoftKeyboardHide()
            {

                LogUtils.i(TAG, "keyboard hide");
                // Code here
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // Code here will run in UI thread
                        vistaDatos.setVisibility(View.VISIBLE);
                        vistaFechabuscar.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onSoftKeyboardShow()
            {
                LogUtils.i(TAG, "keyboard show");
                // Code here
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // Code here will run in UI thread
                        vistaDatos.setVisibility(View.GONE);
                        vistaFechabuscar.setVisibility(View.GONE);
                    }
                });

            }
        });

        softKeyboard.closeSoftKeyboard();

        buscarListaVentas();

        return vistaListaVenta;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener4) {
            mListener = (OnFragmentInteractionListener4) context;
            try {
                mListener.onRegisterFragment(this, OnFragmentInteractionListener4.VISTA_LISTA_VENTAS);
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
            if (softKeyboard != null) {
                softKeyboard.closeSoftKeyboard();
            }
            super.onDetach();
            mListener = null;
            if (softKeyboard != null) {
                softKeyboard.unRegisterSoftKeyboardCallback();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void buscarListaVentas(){

        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    etBuscarPlaca.setText("");
                    listaVentaAdapter.cambiarLista(new ArrayList<SoatDatosVentum>());
                    rvListaVentas.invalidate();

                    tvTotalMontoListaVenta.setText(String.format("Bs %s", 0));

                    tvCantidadVendidasListaVenta.setText(String.valueOf(0));
                    tvCantidadValidasListaVenta.setText(String.valueOf(0));
                    tvCantidadAnuladasListaVenta.setText(String.valueOf(0));
                    tvCantidadRevertidosListaVenta.setText(String.valueOf(0));

                    listarVentaUnivida.setGestionFk(-1);
                    listarVentaUnivida.setVentaFecha(simpleDateFormat.format(fechaSeleccion));

                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.TRUE);

                    final String parametrosJson3 = listarVentaUnivida.toString();
                    VolleySingleton.getInstance(getContext()).getRequestQueue().getCache().clear();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_LISTAR, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            LogUtils.i(TAG, "OK RESPONSE " + response);

                            ListarVentaRespUnivida listarVentaRespUnivida = null;

                            try{

                                listarVentaRespUnivida = new Gson().fromJson(response, ListarVentaRespUnivida.class);

                                LogUtils.i(TAG, "OK CONVERSION " + new Gson().toJson(listarVentaRespUnivida));

                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                            final ListarVentaRespUnivida listarVentaRespUnivida2 = listarVentaRespUnivida;
                            if (listarVentaRespUnivida != null){
                                if (listarVentaRespUnivida.getExito()){

                                    final List<SoatDatosVentum> listaDatosVenta = listarVentaRespUnivida2.getDatos().getSoatDatosVenta();

                                    for (int i = 0; i< listaDatosVenta.size(); i++){
                                        listaDatosVenta.get(i).resetEstado();
                                    }

                                    rvListaVentas.invalidate();
                                    rvListaVentas.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            List<SoatDatosVentum> listaTotal = new ArrayList<>();

                                            listaTotal.addAll(listaDatosVenta);

                                            if (tvTotalMontoListaVenta != null)
                                                tvTotalMontoListaVenta.setText(String.format("Bs %s", String.valueOf(listarVentaRespUnivida2.getDatos().getRcvFormularioImporte())));

                                            tvCantidadVendidasListaVenta.setText(String.valueOf(listarVentaRespUnivida2.getDatos().getRcvCantidadSoat()));
                                            tvCantidadValidasListaVenta.setText(String.valueOf(listarVentaRespUnivida2.getDatos().getRcvCantidadSoatValidos()));
                                            tvCantidadAnuladasListaVenta.setText(String.valueOf(listarVentaRespUnivida2.getDatos().getRcvCantidadSoatAnulados()));
                                            tvCantidadRevertidosListaVenta.setText(String.valueOf(listarVentaRespUnivida2.getDatos().getRcvCantidadSoatRevertidos()));


                                            listaVentaAdapter.cambiarLista(listaTotal);

                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                        }
                                    });

                                }else{
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, listarVentaRespUnivida.getMensaje());
                                }
                            }else{

                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Los datos estan vacion, comuniquese con el administrador.");
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            LogUtils.i(TAG, "FAIL RESPONSE " + error);
                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
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
                    stringRequest.setTag("listarventa");

                    VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
                }
            });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        VolleySingleton.getInstance(getContext()).getRequestQueue().cancelAll("listarventa");
        VolleySingleton.getInstance(getContext()).getRequestQueue().cancelAll("obtenerventa");
        VolleySingleton.getInstance(getContext()).getRequestQueue().cancelAll("solicitarrevertirventa");
    }

    private void abrirMenuItems(){

        if (soatDatosVentumSeleccionado != null ) {
            if (soatDatosVentumSeleccionado.getSoatGenericaEstadoFk() == 1) {

                BottomSheetMenuDialog dialog = new BottomSheetBuilder(getContext(), R.style.AppTheme_BottomSheetDialog_Custom)
                        .setMode(BottomSheetBuilder.MODE_LIST)
                        .addItem(502,"REIMPRIMIR", R.drawable.ic_action_printer)
                        .addItem(503, "SOLICITAR REVERSIÓN", R.drawable.ic_action_revertir)
                        .setItemClickListener(new BottomSheetItemClickListener() {
                            @Override
                            public void onBottomSheetItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case 502:

                                        obtenerDatosVenta();
                                        break;
                                    case 503:

                                        obtenerMotivo();
                                        break;
                                }
                            }
                        })
                        .createDialog();

                dialog.show();

            }else{
                soatDatosVentumSeleccionado = null;
            }
        }

    }

    private void obtenerDatosVenta(){

        if (getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ObtenerVentaUnivida obtenerVentaUnivida = new ObtenerVentaUnivida();

                    obtenerVentaUnivida.setAutorizacionNumero(soatDatosVentumSeleccionado.getFacturaAutorizacionNumero());
                    obtenerVentaUnivida.setGestionFk(-1);
                    obtenerVentaUnivida.setNumero(soatDatosVentumSeleccionado.getFacturaNumero());
                    obtenerVentaUnivida.setNumeroComprobante(soatDatosVentumSeleccionado.getSoatNumeroComprobante());
                    obtenerVentaUnivida.setVehiculoPlaca(soatDatosVentumSeleccionado.getVehiculoPlaca());
                    obtenerVentaUnivida.setVentaCajero("");
                    obtenerVentaUnivida.setVentaCanalFk(28);
                    obtenerVentaUnivida.setVentaVendedor(user.getUsername());

                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.TRUE);

                    final String parametrosJson3 = obtenerVentaUnivida.toString();
                    VolleySingleton.getInstance(getContext()).getRequestQueue().getCache().clear();


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_OBTENER, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            LogUtils.i(TAG, "OK RESPONSE " + response);

                            fechaImpresion = Calendar.getInstance().getTime();

                            try{

                                efectivizarRespUnivida = new Gson().fromJson(response, EfectivizarRespUnivida.class);

                                LogUtils.i(TAG, "OK CONVERSION " + new Gson().toJson(efectivizarRespUnivida));

                            }catch (Exception ex){
                                ex.printStackTrace();
                            }

                            if (efectivizarRespUnivida != null){
                                if (efectivizarRespUnivida.getExito()){

                                    try {
                                        imprimirFactura.prepararImpresionFactura(user, efectivizarRespUnivida);
                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No se puede imprimir los datos por que algunos o todos son nulos.");
                                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                        return;
                                    }

                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.TRUE);
                                    impColilla = true;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        imprimirFactura.imprimirFactura2();
                                                    } catch (ImpresoraErrorException e) {
                                                        e.printStackTrace();
                                                        if (mListener != null)
                                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error en la impresora.");
                                                    } catch (NoHayPapelException e) {
                                                        e.printStackTrace();
                                                        if (mListener != null)
                                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No hay papel para imprimir.");
                                                    } catch (VoltageBajoException e) {
                                                        e.printStackTrace();
                                                        if (mListener != null)
                                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error de Bateria baja. No podra imprimir con la bateria baja.");
                                                    } catch (ErrorPapelException e) {
                                                        e.printStackTrace();
                                                        if (mListener != null)
                                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error en la impresora");
                                                    } catch (Exception ex) {
                                                        ex.printStackTrace();
                                                        if (mListener != null)
                                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error al imprimir los datos. Si esto persiste comuniquese con el encargado.");
                                                    }
                                                }
                                            }).start();
                                        }
                                    });


                                }else{
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, efectivizarRespUnivida.getMensaje());
                                }
                            }else{

                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Los datos estan vacion, comuniquese con el administrador.");
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            LogUtils.i(TAG, "FAIL RESPONSE " + error);
                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
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
                    stringRequest.setTag("obtenerventa");

                    VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
                }
            });
        }

    }

    public void obtenerMotivo(){
        DialogoEmizor dialogoEmizor = new DialogoEmizor();
        dialogoEmizor.setTipoDialogo(5);
        dialogoEmizor.setMensaje("Motivo: ");
        dialogoEmizor.setCancelable(false);
        dialogoEmizor.show(getActivity().getSupportFragmentManager(), null);
    }

    public void solicitarReversion(final String motivo){

        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SolicitarReversionUnivida solicitarReversionUnivida = new SolicitarReversionUnivida();

                    solicitarReversionUnivida.setGestionFk(-1);
                    solicitarReversionUnivida.setMotivo(motivo);
                    solicitarReversionUnivida.setNumeroComprobante(soatDatosVentumSeleccionado.getSoatNumeroComprobante());
                    solicitarReversionUnivida.setVehiculoPlaca(soatDatosVentumSeleccionado.getVehiculoPlaca());

                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.TRUE);

                    final String parametrosJson3 = solicitarReversionUnivida.toString();
                    VolleySingleton.getInstance(getContext()).getRequestQueue().getCache().clear();


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_SOLICITAR_REVERTIR, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            LogUtils.i(TAG, "OK RESPONSE " + response);

                            RespUnivida respUnivida = null;

                            try{

                                respUnivida = new Gson().fromJson(response, RespUnivida.class);

                                LogUtils.i(TAG, "OK CONVERSION " + new Gson().toJson(respUnivida));

                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                            if (respUnivida != null){
                                if (respUnivida.getExito()){

                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, respUnivida.getMensaje());

                                }else{

                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, respUnivida.getMensaje());
                                }
                            }else{

                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Los datos estan vacion, comuniquese con el administrador.");
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            LogUtils.i(TAG, "FAIL RESPONSE " + error);
                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
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
                    stringRequest.setTag("solicitarrevertirventa");

                    VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

                }
            });

    }

    public void imprimirColilla(){
        efectivizarRespUnivida = null;
        fechaImpresion = null;
        impColilla = false;
        if (getActivity() != null)
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            imprimirFactura.imprimirFactura2();

                        } catch (ImpresoraErrorException e) {
                            e.printStackTrace();
                            if (mListener != null)
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error en la impresora.");
                        } catch (NoHayPapelException e) {
                            e.printStackTrace();
                            if (mListener != null)
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No hay papel para imprimir.");
                        } catch (VoltageBajoException e) {
                            e.printStackTrace();
                            if (mListener != null)
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error de Bateria baja. No podra imprimir con la bateria baja.");
                        } catch (ErrorPapelException e) {
                            e.printStackTrace();
                            if (mListener != null)
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error en la impresora");
                        }catch (Exception ex){
                            ex.printStackTrace();
                            if (mListener != null)
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error al imprimir los datos. Si esto persiste comuniquese con el encargado.");
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    public void terminoDeImprimir() {

        if (impColilla){
            imprimirFactura.procesarColillaVentaSoat(user, efectivizarRespUnivida, fechaImpresion);

            DialogoEmizor dialogoEmizor = new DialogoEmizor();
            dialogoEmizor.setTipoDialogo(6);
            dialogoEmizor.setMensaje("Imprimir Colilla?");
            dialogoEmizor.setCancelable(false);
            dialogoEmizor.show(getActivity().getSupportFragmentManager(), null);
        }else {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                }
            });
        }

    }

    @Override
    public void onRealizaAccionDialogEmizor(DialogoEmizor dialogoEmizor, int accion, int tipodialogo) {

        if (tipodialogo == 5){
            if (accion == ACCION_ACEPTAR){
                solicitarReversion(dialogoEmizor.getMotivo());
            }
        }

        dialogoEmizor.getDialog().cancel();
    }
}
