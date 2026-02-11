package com.emizor.univida.fragmento;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.emizor.univida.R;
import com.emizor.univida.activities.EfectivizarVentaActivity;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.adapter.ListaVentaAdapter;
import com.emizor.univida.adapter.RecyclerViewOnItemCickListener;
import com.emizor.univida.dialogo.DialogoEmizor;
import com.emizor.univida.excepcion.ErrorPapelException;
import com.emizor.univida.excepcion.ImpresoraErrorException;
import com.emizor.univida.excepcion.NoHayPapelException;
import com.emizor.univida.excepcion.VoltageBajoException;
import com.emizor.univida.imprime.ImprimirAvisoListener;
import com.emizor.univida.imprime.ImprimirFactura;
import com.emizor.univida.modelo.dominio.univida.ApiResponse;
import com.emizor.univida.modelo.dominio.univida.RespUnivida;
import com.emizor.univida.modelo.dominio.univida.parametricas.Departamento;
import com.emizor.univida.modelo.dominio.univida.parametricas.MedioPago;
import com.emizor.univida.modelo.dominio.univida.parametricas.ParametricaGenerica;
import com.emizor.univida.modelo.dominio.univida.parametricas.TipoDocumentoIdentidad;
import com.emizor.univida.modelo.dominio.univida.parametricas.TipoVehiculo;
import com.emizor.univida.modelo.dominio.univida.parametricas.UsoVehiculo;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.ventas.EfectivizarAdicionalUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.EfectivizarFacturaUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.EfectivizarRespUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.ListarVentaRespUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.ListarVentaUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.ObtenerPrimaRespUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.ObtenerPrimaUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.ObtenerVentaUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.SoatDatosVentum;
import com.emizor.univida.modelo.dominio.univida.ventas.SolicitarReversionUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.ValidarVendibleInterRespUnivida;
import com.emizor.univida.modelo.dominio.univida.ventas.VehiculoDatosInter;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.ApiService;
import com.emizor.univida.rest.Conexion;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.rest.VolleySingleton;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LogUtils;
import com.emizor.univida.util.SoftKeyboard;
import com.emizor.univida.util.ValidarCampo;
import com.emizor.univida.utils.ParametricasCache;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
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

    /***/
    private Spinner spinnerTipoDocumento;
    private EditText etNumeroDocumento, etExtensionDocumento, etNombreRazonSocial, etEmail, etCelular;
    private Button btnAtras, btnSiguiente;
    private TextView tvTitulo;
    private LinearLayout layoutExtensionDocumento;
    private android.app.AlertDialog dialogCambiarFactura;
    private TipoDocumentoIdentidad tipoDocumentoIdentidad;
    /***/
    private ValidarVendibleInterRespUnivida validarVendibleInterRespUnivida;
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

        View vistaListaVenta = inflater.inflate(R.layout.fragment_lista_ventas, container, false);
        ;

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

        if (user == null) {
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
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {

            @Override
            public void onSoftKeyboardHide() {

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
            public void onSoftKeyboardShow() {
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

    private void abrirdatePicker() {
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
            } catch (Exception ex) {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void buscarListaVentas() {

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

                            try {

                                listarVentaRespUnivida = new Gson().fromJson(response, ListarVentaRespUnivida.class);

                                LogUtils.i(TAG, "OK CONVERSION " + new Gson().toJson(listarVentaRespUnivida));

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            final ListarVentaRespUnivida listarVentaRespUnivida2 = listarVentaRespUnivida;
                            if (listarVentaRespUnivida != null) {
                                if (listarVentaRespUnivida.getExito()) {

                                    final List<SoatDatosVentum> listaDatosVenta = listarVentaRespUnivida2.getDatos().getSoatDatosVenta();

                                    for (int i = 0; i < listaDatosVenta.size(); i++) {
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

                                } else {
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, listarVentaRespUnivida.getMensaje());
                                }
                            } else {

                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Los datos estan vacion, comuniquese con el administrador.");
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            LogUtils.i(TAG, "FAIL RESPONSE " + error);
                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                            if (error != null) {
                                if (error.getCause() instanceof TimeoutError) {
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, getString(R.string.mensaje_error_timeout));
                                } else {
//                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, getString(R.string.mensaje_error_volley) + error.getMessage());
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No tiene Conexión a INTERNET");
                                }
                            } else {
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

    private void abrirMenuItems() {

        if (soatDatosVentumSeleccionado != null) {
            if (soatDatosVentumSeleccionado.getSoatGenericaEstadoFk() == 1) {

                BottomSheetMenuDialog dialog = new BottomSheetBuilder(getContext(), R.style.AppTheme_BottomSheetDialog_Custom)
                        .setMode(BottomSheetBuilder.MODE_LIST)
                        .addItem(505, "REEMPLAZAR VENTA", R.drawable.ic_sync_black_24dp)
                        .addItem(506, "CAMBIO DE FACTURA", R.drawable.ic_sync_black_24dp)
                        .addItem(502, "REIMPRIMIR RESUMIDO", R.drawable.ic_action_printer)
                        .addItem(504, "REIMPRIMIR COMPLETO", R.drawable.ic_action_printer)
                        .addItem(503, "SOLICITAR REVERSIÓN", R.drawable.ic_action_revertir)

                        .setItemClickListener(new BottomSheetItemClickListener() {
                            @Override
                            public void onBottomSheetItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case 502:

                                        obtenerDatosVenta();
                                        break;
                                    case 503:
                                        String medioPago=soatDatosVentumSeleccionado.getSoatMediosDePago();
                                        if ("$imple QR".equals(medioPago))
                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No se permite la reversión del SOAT para el medio de pago QR");
                                        else
                                            obtenerMotivo();
                                        break;
                                    case 504:

                                        obtenerDatosVenta(2);
                                        break;
                                    case 505:
                                        mostrarDialogoCambioPlaca();
                                        break;
                                    case 506:
                                        mostrarDialogoFacturacion();
                                        break;

                                }
                            }
                        })
                        .createDialog();

                dialog.show();

            } else {
                soatDatosVentumSeleccionado = null;
            }
        }

    }
    private void obtenerDatosVenta(){
        //1 Resumen
        //2 Completo
        obtenerDatosVenta(1);
    }
    private void obtenerDatosVenta(int tipo) {

        if (getActivity() != null) {
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

                            try {

                                efectivizarRespUnivida = new Gson().fromJson(response, EfectivizarRespUnivida.class);

                                LogUtils.i(TAG, "OK CONVERSION " + new Gson().toJson(efectivizarRespUnivida));

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            if (efectivizarRespUnivida != null) {
                                if (efectivizarRespUnivida.getExito()) {

                                    try {
                                        if(tipo==1)
                                            imprimirFactura.prepararImpresionFactura(user, efectivizarRespUnivida);
                                        if(tipo==2)
                                            imprimirFactura.prepararImpresionFacturaCompleto(user, efectivizarRespUnivida);
                                    } catch (Exception ex) {
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


                                } else {
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, efectivizarRespUnivida.getMensaje());
                                }
                            } else {

                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Los datos estan vacion, comuniquese con el administrador.");
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            LogUtils.i(TAG, "FAIL RESPONSE " + error);
                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                            if (error != null) {
                                if (error.getCause() instanceof TimeoutError) {
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, getString(R.string.mensaje_error_timeout));
                                } else {
//                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, getString(R.string.mensaje_error_volley) + error.getMessage());
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No tiene Conexión a INTERNET");
                                }
                            } else {
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

    public void obtenerMotivo() {
        DialogoEmizor dialogoEmizor = new DialogoEmizor();
        dialogoEmizor.setTipoDialogo(5);
        dialogoEmizor.setMensaje("Motivo: ");
        dialogoEmizor.setCancelable(false);
        dialogoEmizor.show(getActivity().getSupportFragmentManager(), null);
    }


    public void solicitarReversion(final String motivo) {

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

                            try {

                                respUnivida = new Gson().fromJson(response, RespUnivida.class);

                                LogUtils.i(TAG, "OK CONVERSION " + new Gson().toJson(respUnivida));

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                            if (respUnivida != null) {
                                if (respUnivida.getExito()) {

                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, respUnivida.getMensaje());

                                } else {

                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, respUnivida.getMensaje());
                                }
                            } else {

                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Los datos estan vacion, comuniquese con el administrador.");
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            LogUtils.i(TAG, "FAIL RESPONSE " + error);
                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                            if (error != null) {
                                if (error.getCause() instanceof TimeoutError) {
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, getString(R.string.mensaje_error_timeout));
                                } else {
//                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, getString(R.string.mensaje_error_volley) + error.getMessage());
                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No tiene Conexión a INTERNET");
                                }
                            } else {
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

    public void imprimirColilla() {
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
                            } catch (Exception ex) {
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

        if (impColilla) {
            imprimirFactura.procesarColillaVentaSoat(user, efectivizarRespUnivida, fechaImpresion);

            DialogoEmizor dialogoEmizor = new DialogoEmizor();
            dialogoEmizor.setTipoDialogo(6);
            dialogoEmizor.setMensaje("Imprimir Colilla?");
            dialogoEmizor.setCancelable(false);
            dialogoEmizor.show(getActivity().getSupportFragmentManager(), null);
        } else {

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

        if (tipodialogo == 5) {
            if (accion == ACCION_ACEPTAR) {
                solicitarReversion(dialogoEmizor.getMotivo());
            }
        }

        dialogoEmizor.getDialog().cancel();
    }


    private void obtenerDocumentosIdentidadFacturacion() {


        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getContext());

        List<TipoDocumentoIdentidad> listaTiposDocs = controladorSqlite2.obtenerTipoDocumentosIdentidad();
        LogUtils.i(TAG, "Datos de documentos " + new Gson().toJson(listaTiposDocs));
        ArrayAdapter<TipoDocumentoIdentidad> arrayAdapterTipoDocumentoIdentidad = new ArrayAdapter<TipoDocumentoIdentidad>(getContext(), android.R.layout.simple_spinner_dropdown_item, listaTiposDocs);

        spinnerTipoDocumento.setAdapter(arrayAdapterTipoDocumentoIdentidad);

    }

    private boolean validarFormularioCambioFactura() {
        String numeroDoc = etNumeroDocumento.getText().toString().trim();
        String nombre = etNombreRazonSocial.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String celular = etCelular.getText().toString().trim();

        if (numeroDoc.isEmpty()) {
            etNumeroDocumento.setError("Ingrese número de documento");
            return false;
        }

        if (nombre.isEmpty()) {
            etNombreRazonSocial.setError("Ingrese nombre o razón social");
            return false;
        }

        if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Correo inválido");
            return false;
        }

        if (celular.isEmpty()) {
            etCelular.setError("Ingrese número de celular");
            return false;
        }

        if (!celular.isEmpty() && celular.length() < 8) {
            etCelular.setError("Ingrese un número válido (mínimo 8 dígitos)");
            return false;
        }
        String numeroDocumento = etNumeroDocumento.getText().toString();
        if (!ValidarCampo.validarNumeroLong(numeroDocumento)) {
            etNumeroDocumento.setError("El número de documento debe ser un número válido");
            return false;
        }

        return true;
    }


    private void mostrarDialogoFacturacion() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_soatc_facturacion, null);

        // Referencias UI
        spinnerTipoDocumento = view.findViewById(R.id.spinnerTipoDocumento);
        etNumeroDocumento = view.findViewById(R.id.etNumeroDocumento);
        etExtensionDocumento = view.findViewById(R.id.etExtensionDocumento);
        etNombreRazonSocial = view.findViewById(R.id.etNombreRazonSocial);
        etEmail = view.findViewById(R.id.etEmail);
        etCelular = view.findViewById(R.id.etCelular);
        btnAtras = view.findViewById(R.id.btnAtras);
        btnSiguiente = view.findViewById(R.id.btnSiguiente);
        tvTitulo = view.findViewById(R.id.tvTitulo);
        layoutExtensionDocumento = view.findViewById(R.id.layoutExtensionDocumento);

        layoutExtensionDocumento.setVisibility(View.GONE);

        obtenerDocumentosIdentidadFacturacion();
        spinnerTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if ("CI - CEDULA DE IDENTIDAD".equals(item.toString())) {
                    layoutExtensionDocumento.setVisibility(View.VISIBLE);
                } else {
                    layoutExtensionDocumento.setVisibility(View.GONE);
                    etExtensionDocumento.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // Botón Atrás



        dialogCambiarFactura = new android.app.AlertDialog.Builder(getContext())
                .setView(view)
                .setCancelable(false)
                .create();
        btnAtras.setText("Cancelar");
        btnSiguiente.setText("Cambiar");
        btnAtras.setOnClickListener(v -> {
            dialogCambiarFactura.dismiss();
        });
        btnSiguiente.setOnClickListener(v -> {
            if (validarFormularioCambioFactura()) {
                 CambiarFacturacion();
            }
        });

        dialogCambiarFactura.show();
        dialogCambiarFactura.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE |
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        );
    }

    private void CambiarFacturacion(){
        dialogCambiarFactura.dismiss();
        int tipoDocumento = spinnerTipoDocumento.getSelectedItemPosition() + 1;
        String numeroDocumento = etNumeroDocumento.getText().toString().trim();
        String extensionDocumento = etExtensionDocumento.getText().toString().trim();
        String nombreRazonSocial = etNombreRazonSocial.getText().toString().trim();
        String correoCliente = etEmail.getText().toString().trim();
        String telefonoCliente = etCelular.getText().toString().trim();

        Map<String, Object> parametros = new HashMap<>();

        parametros.put("numero_comprobante", soatDatosVentumSeleccionado.getSoatNumeroComprobante());
        parametros.put("factura_tipo_doc_identidad_fk", tipoDocumento);
        parametros.put("nit_ci", numeroDocumento);
        parametros.put("factura_ci_complemento", extensionDocumento);
        parametros.put("razon_social", nombreRazonSocial);
        parametros.put("correo_cliente", correoCliente);
        parametros.put("telefono_cliente", telefonoCliente);
        parametros.put("usuario_uv", user.getDatosUsuario().getEmpleadoUsuario());


        ApiService apiService = new ApiService(getContext());
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_REEMPLAZAR_FACTURA;
        ((PrincipalActivity) getContext()).mostrarLoading(true);

        apiService.solicitudPost(url, parametros, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ((PrincipalActivity) getContext()).mostrarLoading(false);
                try {
                    Type responseType = new TypeToken<ApiResponse<Void>>() {
                    }.getType();
                    ApiResponse<Void> apiResponse = new Gson().fromJson(response, responseType);;


                    if (apiResponse.exito) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Atención")
                                .setMessage(apiResponse.mensaje)
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                    obtenerDatosVenta();
                                }).show();
                    } else {

                        new AlertDialog.Builder(getContext())
                                .setTitle("Atención")
                                .setMessage(apiResponse.mensaje)
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                    dialogCambiarFactura.show();
                                }).show();
                    }

                } catch (Exception e) {

                    Toast.makeText(getContext(), "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((PrincipalActivity) getContext()).mostrarLoading(false);
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoCambioPlaca() {

        String placaActual=soatDatosVentumSeleccionado.getVehiculoPlaca();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Reemplazo de venta");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        //Mensaje claro
        TextView tvMensaje = new TextView(getContext());
        tvMensaje.setText("Está a punto de reemplazar la venta correspondiente a la siguiente placa:");
        tvMensaje.setTextSize(16);
        tvMensaje.setPadding(0, 0, 0, 18);

        //Placa actual
        TextView tvPlacaActual = new TextView(getContext());
        tvPlacaActual.setText("Placa actual: " + placaActual);
        tvPlacaActual.setTextSize(18);
        tvPlacaActual.setTypeface(null, Typeface.BOLD);
        tvPlacaActual.setPadding(0, 0, 0, 8);
        //
        TextView tvPrimaActual = new TextView(getContext());
        tvPrimaActual.setText(String.format("Prima : Bs %.2f", soatDatosVentumSeleccionado.getFacturaPrima()));
        tvPrimaActual.setTextSize(16);
        tvPrimaActual.setPadding(0, 0, 0, 30);
        //Nueva placa
        TextView tvNueva = new TextView(getContext());
        tvNueva.setText("Ingrese la nueva placa:");
        tvNueva.setTypeface(null, Typeface.BOLD);
        tvNueva.setPadding(0, 0, 0, 10);

        EditText etPlacaNueva = new EditText(getContext());

        etPlacaNueva.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(10),
                (source, start, end, dest, dstart, dend) ->
                        source.toString().toUpperCase()
        });

        etPlacaNueva.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        layout.addView(tvMensaje);
        layout.addView(tvPlacaActual);
        layout.addView(tvPrimaActual);
        layout.addView(tvNueva);
        layout.addView(etPlacaNueva);

        builder.setView(layout);

        builder.setPositiveButton("Reemplazar", (dialog, which) -> {
            String nuevaPlaca = etPlacaNueva.getText().toString().trim();

            if (nuevaPlaca.isEmpty()) {
                Toast.makeText(getContext(), "Debe ingresar la nueva placa", Toast.LENGTH_SHORT).show();
                mostrarDialogoCambioPlaca();
                return;
            }

            validarPlaca(nuevaPlaca);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void validarPlaca(String nuevaPlaca){
        String placaActual=soatDatosVentumSeleccionado.getVehiculoPlaca();
        ApiService apiService = new ApiService(getContext());
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_VALIDAR_CAMBIO_PLACA;
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("placa_antigua", placaActual);
        parametros.put("placa_nueva", nuevaPlaca);

        apiService.solicitudPost(url, parametros, response -> {
            Type responseType = new TypeToken<ApiResponse<List<ParametricaGenerica>>>(){}.getType();
            ApiResponse<List<ParametricaGenerica>> apiResponse = new Gson().fromJson(response, responseType);
            if (apiResponse.exito) {
                //ParametricasCache.getInstance().setParentesco(apiResponse.datos)
                new AlertDialog.Builder(getContext())
                        .setTitle("Atención")
                        .setMessage("Al realizar un reemplazo de venta, la venta anterior será revertida y reemplazada por una nueva. ")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            //buscarPlaca(nuevaPlaca);
                            String medioPago=soatDatosVentumSeleccionado.getSoatMediosDePago();
                            int medioPagoFk;
                            if ("$imple QR".equals(medioPago))
                                medioPagoFk=30;
                            else
                                medioPagoFk=1;

                            Bundle args = new Bundle();
                            args.putDouble("reemplazoPrimaAnterior", soatDatosVentumSeleccionado.getFacturaPrima());
                            args.putInt("reemplazoMedioPagoFkAnterior", medioPagoFk);
                            args.putLong("reemplazoTVehiSoatPropFkAnterior", soatDatosVentumSeleccionado.getSoatNumeroComprobante());
                            args.putString("reemplazoPlacaNueva", nuevaPlaca);
                            cambiarAFragmento(NuevaVentaFragment.class, args);


                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("Dialogos", "Confirmación cancelada.");
                                dialog.cancel();
                            }
                        })
                        .show();
            }
            else{

                new AlertDialog.Builder(getContext())
                        .setTitle("Atención")
                        .setMessage(apiResponse.mensaje)
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            mostrarDialogoCambioPlaca();
                        }).show();
            }
        }, error -> {});
    }

    private void buscarPlaca(String nuevaPlaca){
        ApiService apiService = new ApiService(getContext());
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_VALIDARVENDIBLE_OBTENER_DATOS_INTER;
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("gestion_fk", soatDatosVentumSeleccionado.getSoatTParGestionFk());
        parametros.put("vehiculo_placa", nuevaPlaca);
        parametros.put("venta_cajero", "");
        parametros.put("venta_canal", 28);
        parametros.put("venta_vendedor", user.getUsername());
        parametros.put("vehiculo_placa_tipo", "1");

        apiService.solicitudPost(url, parametros, response -> {
//            efectivizarRespUnivida = new Gson().fromJson(response, EfectivizarRespUnivida.class);
            validarVendibleInterRespUnivida = new Gson().fromJson(response, ValidarVendibleInterRespUnivida.class);

            if (validarVendibleInterRespUnivida != null) {
                if (validarVendibleInterRespUnivida.getExito()) {

                    if (validarVendibleInterRespUnivida.getDatos() != null && validarVendibleInterRespUnivida.getDatos().getSoatRosetaNumero() == null){
                        validarVendibleInterRespUnivida.getDatos().setSoatRosetaNumero("0");
                    }
                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);

                    calcularPrima(nuevaPlaca);

                } else {
                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, validarVendibleInterRespUnivida.getMensaje());

                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                }

            }else{
                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Datos nulos e incompatibles en respuesta del servicio.");

                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
            }
        }, error -> {});
    }
    public void calcularPrima(String nuevaPlaca){
        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_INICIAR_CAPTURA_LOCACION, null);

        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (! Conexion.estaConectado(getContext())){
                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No tiene conexión a internet.");
                        return;
                    }

                    //String placa = nuevaPlaca;
                    if (nuevaPlaca.length() > 0){

                        ObtenerPrimaUnivida obtenerPrimaUnivida = new ObtenerPrimaUnivida();


                            obtenerPrimaUnivida.setDepartamentoPlazaCirculacionFk(soatDatosVentumSeleccionado.getSoatTParDepartamentoPCFk());

                           // obtenerPrimaUnivida.setDepartamentoPlazaCirculacionFk("");

                        obtenerPrimaUnivida.setGestionFk(soatDatosVentumSeleccionado.getSoatTParGestionFk());
                        obtenerPrimaUnivida.setVehiculoPlaca(nuevaPlaca);
                        obtenerPrimaUnivida.setVehiculoTipoFk(soatDatosVentumSeleccionado.getSoatTParVehiculoTipoFk());
                        obtenerPrimaUnivida.setVehiculoUsoFk(soatDatosVentumSeleccionado.getSoatTParVehiculoUsoFk());
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

                                            EfectivizarFacturaUnivida efectivizarFacturaUnivida = new EfectivizarFacturaUnivida();

                                            efectivizarFacturaUnivida.setDepartamentoPlazaCirculacionFk(soatDatosVentumSeleccionado.getSoatTParDepartamentoPCFk());
                                            efectivizarFacturaUnivida.setDepartamentoVentaFk(user.getDatosUsuario().getSucursalIdDepartamento());
                                            efectivizarFacturaUnivida.setGestionFk(soatDatosVentumSeleccionado.getSoatTParGestionFk());
                                            efectivizarFacturaUnivida.setPrima(obtenerPrimaRespUnivida.getMontoPrima());
                                            efectivizarFacturaUnivida.setSucursalFk(user.getDatosUsuario().getSucursalCodigo());
                                            efectivizarFacturaUnivida.setVehiculoPlaca(nuevaPlaca);
                                            efectivizarFacturaUnivida.setVehiculoTipoFk(soatDatosVentumSeleccionado.getSoatTParVehiculoTipoFk());
                                            efectivizarFacturaUnivida.setVehiculoUsoFk(soatDatosVentumSeleccionado.getSoatTParVehiculoUsoFk());
                                            efectivizarFacturaUnivida.setCorreoCliente(null);
                                            //MEDIO PAGO
                                            String medioPago=soatDatosVentumSeleccionado.getSoatMediosDePago();
                                            if ("$imple QR".equals(medioPago))
                                            {
                                                efectivizarFacturaUnivida.setMedioPagoFk(30);
                                            }
                                            else{
                                                efectivizarFacturaUnivida.setMedioPagoFk(1);
                                            }
                                            efectivizarFacturaUnivida.setVentaDatosAdicionales(null);
                                            efectivizarFacturaUnivida.setTelefonoCliente(null);
                                            efectivizarFacturaUnivida.setRazonSocial(null);
                                            efectivizarFacturaUnivida.setRosetaNumero(null);
                                            efectivizarFacturaUnivida.setNitCi(null);
                                            efectivizarFacturaUnivida.setVehiculoPlacaTipo(1);
                                            efectivizarFacturaUnivida.setVehiSoatPropSecuencialRevertir(soatDatosVentumSeleccionado.getSoatNumeroComprobante());

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
                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Es necesario un(a) Placa para realizar la búsqueda.");
                    }
                }
            });

    }
    public void cambiarAFragmento(Class<? extends Fragment> fragmentClass, Bundle args) {
        if (getActivity() != null && getActivity() instanceof FragmentActivity) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            try {
                Fragment fragment = fragmentClass.newInstance();
                if (args != null) {
                    fragment.setArguments(args);
                }

                transaction.replace(R.id.contenedor_vistas, fragment); // Ajusta el ID del contenedor
                transaction.addToBackStack(null); // Opcional: agrega a la pila
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}