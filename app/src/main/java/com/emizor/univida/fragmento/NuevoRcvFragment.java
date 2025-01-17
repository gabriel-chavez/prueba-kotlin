package com.emizor.univida.fragmento;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
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
import com.emizor.univida.adapter.RcvListaVentaAdapter;
import com.emizor.univida.adapter.RecyclerViewOnItemCickListener;
import com.emizor.univida.excepcion.ErrorPapelException;
import com.emizor.univida.excepcion.ImpresoraErrorException;
import com.emizor.univida.excepcion.NoHayPapelException;
import com.emizor.univida.excepcion.VoltageBajoException;
import com.emizor.univida.imprime.ImprimirAvisoListener;
import com.emizor.univida.imprime.ImprimirFactura;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.EfectivizarRcvUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RcvListarVentaRespUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RcvListarVentaUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RcvVenta;
import com.emizor.univida.modelo.dominio.univida.RespUnivida;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.Conexion;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.rest.VolleySingleton;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LogUtils;
import com.google.gson.Gson;

import org.w3c.dom.Text;

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
public class NuevoRcvFragment extends Fragment implements ImprimirAvisoListener {

    private final String TAG = "NUEVORCV";


    private OnFragmentInteractionListener4 mListener;

    private DatePickerDialog datePickerDialog;
    private Date fechaSeleccion;

    private Button btnFechaListaVenta;
    private SimpleDateFormat simpleDateFormat;

    private User user;
    //private Gestion gestionSeleccionada;
    //private Spinner spGestion;

    private RcvListaVentaAdapter rcvListaVentaAdapterRcvNuevo;
    private RecyclerView rvListaVentasRcvNuevo;
    private LinearLayoutManager linearLayoutManager;

    private RcvListarVentaRespUnivida rcvListarVentaRespUnivida;

    private BootstrapButton bsbNuevoRcv;

    private ImprimirFactura imprimirFactura;
    private TextView tvTotalMontoListaVentaRcvNuevo, tvCantidadVendidasListaVentaRcvNuevo, tvCantidadValidasListaVentaRcvNuevo, tvCantidadAnuladasListaVentaRcvNuevo, tvCantidadRevertidosListaVentaRcvNuevo;

    public NuevoRcvFragment() {
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

        View vistaNuevoRcv = inflater.inflate(R.layout.fragment_nuevo_rcv, container, false);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

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

        btnFechaListaVenta = vistaNuevoRcv.findViewById(R.id.btnFechaListaVentaRcvNuevo);
        btnFechaListaVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirdatePicker();
            }
        });

        rvListaVentasRcvNuevo = vistaNuevoRcv.findViewById(R.id.rvListaVentasRcvNuevo);

        rcvListaVentaAdapterRcvNuevo = new RcvListaVentaAdapter(getContext(), new ArrayList<RcvVenta>(), new RecyclerViewOnItemCickListener() {
            @Override
            public void onClick(View view, int position, int accionTipo) {


            }
        });

        linearLayoutManager = new LinearLayoutManager(vistaNuevoRcv.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvListaVentasRcvNuevo.setLayoutManager(linearLayoutManager);

        rvListaVentasRcvNuevo.setAdapter(rcvListaVentaAdapterRcvNuevo);

        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getContext());

        btnFechaListaVenta.setText(simpleDateFormat.format(fechaSeleccion));

        ImageButton ibBuscar = vistaNuevoRcv.findViewById(R.id.ibBuscarRcvNuevo);

        ibBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listarVentasForRcv();
            }
        });

        user = controladorSqlite2.obtenerUsuario();

        controladorSqlite2.cerrarConexion();

        if (user == null){
            return vistaNuevoRcv;
        }

        rcvListarVentaRespUnivida = null;

        tvTotalMontoListaVentaRcvNuevo = vistaNuevoRcv.findViewById(R.id.tvTotalMontoListaVentaRcvNuevo);
        tvCantidadVendidasListaVentaRcvNuevo = vistaNuevoRcv.findViewById(R.id.tvCantidadVendidasListaVentaRcvNuevo);
        tvCantidadValidasListaVentaRcvNuevo = vistaNuevoRcv.findViewById(R.id.tvCantidadValidasListaVentaRcvNuevo);
        tvCantidadAnuladasListaVentaRcvNuevo = vistaNuevoRcv.findViewById(R.id.tvCantidadAnuladasListaVentaRcvNuevo);
        tvCantidadRevertidosListaVentaRcvNuevo = vistaNuevoRcv.findViewById(R.id.tvCantidadRevertidosListaVentaRcvNuevo);

        bsbNuevoRcv = vistaNuevoRcv.findViewById(R.id.bsbAceptarRcvNuevo);

        bsbNuevoRcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Conexion.estaConectado(getContext())) {
                    nuevaVentaRcv();
                }else{
                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Sin conexion a intenet.");
                }
            }
        });

        bsbNuevoRcv.setEnabled(false);

        imprimirFactura = ImprimirFactura.obtenerImpresora(getContext());
        imprimirFactura.setAvisoListener(this);

        listarVentasForRcv();

        return vistaNuevoRcv;
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
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        try {
            VolleySingleton.getInstance(getContext()).getRequestQueue().cancelAll("listarcv");
            VolleySingleton.getInstance(getContext()).getRequestQueue().cancelAll("nuevorcv");
            super.onDetach();
            mListener = null;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void limpiarDatos(){
        try {
            tvTotalMontoListaVentaRcvNuevo.setText(String.format("Bs %s", String.valueOf(0)));

            tvCantidadVendidasListaVentaRcvNuevo.setText(String.valueOf(0));
            tvCantidadValidasListaVentaRcvNuevo.setText(String.valueOf(0));
            tvCantidadAnuladasListaVentaRcvNuevo.setText(String.valueOf(0));
            tvCantidadRevertidosListaVentaRcvNuevo.setText(String.valueOf(0));

            rvListaVentasRcvNuevo.invalidate();
            rvListaVentasRcvNuevo.post(new Runnable() {
                @Override
                public void run() {
                    rcvListaVentaAdapterRcvNuevo.cambiarLista(new ArrayList<RcvVenta>());

                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void listarVentasForRcv(){

        RcvListarVentaUnivida rcvListarVentaUnivida = new RcvListarVentaUnivida();

        rcvListarVentaUnivida.setGestionFk(-1);
        rcvListarVentaUnivida.setVentaFecha(simpleDateFormat.format(fechaSeleccion));
        rcvListarVentaUnivida.setVentaVendedor(user.getUsername());

        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.TRUE);

        final String parametrosJson3 = rcvListarVentaUnivida.toString();
        VolleySingleton.getInstance(getContext()).getRequestQueue().getCache().clear();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_RCV_LISTAR_VENTAS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                LogUtils.i(TAG, "OK RESPONSE " + response);

                rcvListarVentaRespUnivida = null;

                try{

                    rcvListarVentaRespUnivida = new Gson().fromJson(response, RcvListarVentaRespUnivida.class);

                    LogUtils.i(TAG, "OK CONVERSION " + new Gson().toJson(rcvListarVentaRespUnivida));

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                if (rcvListarVentaRespUnivida != null){
                    if (rcvListarVentaRespUnivida.getExito()){

                        final List<RcvVenta> listaDatosVenta = rcvListarVentaRespUnivida.getDatos().getDatosVenta();

                        for (int i = 0; i< listaDatosVenta.size(); i++){
                            listaDatosVenta.get(i).resetEstado();
                        }

                        rvListaVentasRcvNuevo.invalidate();
                        rvListaVentasRcvNuevo.post(new Runnable() {
                            @Override
                            public void run() {
                                List<RcvVenta> listaTotal = new ArrayList<>();

                                listaTotal.addAll(listaDatosVenta);

                                    if (listaTotal.size() > 0){
                                        bsbNuevoRcv.setEnabled(true);
                                    }else{
                                        bsbNuevoRcv.setEnabled(false);

                                    }

                                    if (tvTotalMontoListaVentaRcvNuevo != null)
                                        tvTotalMontoListaVentaRcvNuevo.setText(String.format("Bs %s", String.valueOf(rcvListarVentaRespUnivida.getDatos().getFormularioImporte())));

                                    if (tvCantidadVendidasListaVentaRcvNuevo != null)
                                        tvCantidadVendidasListaVentaRcvNuevo.setText(String.valueOf(rcvListarVentaRespUnivida.getDatos().getCantidad()));
                                    if (tvCantidadValidasListaVentaRcvNuevo != null)
                                        tvCantidadValidasListaVentaRcvNuevo.setText(String.valueOf(rcvListarVentaRespUnivida.getDatos().getCantidadValidos()));
                                    if (tvCantidadAnuladasListaVentaRcvNuevo != null)
                                        tvCantidadAnuladasListaVentaRcvNuevo.setText(String.valueOf(rcvListarVentaRespUnivida.getDatos().getCantidadAnulados()));
                                    if (tvCantidadRevertidosListaVentaRcvNuevo != null)
                                        tvCantidadRevertidosListaVentaRcvNuevo.setText(String.valueOf(rcvListarVentaRespUnivida.getDatos().getCantidadRevertidos()));


                                    rcvListaVentaAdapterRcvNuevo.cambiarLista(listaTotal);

                                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                            }
                        });

                    }else{
                        limpiarDatos();
                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, rcvListarVentaRespUnivida.getMensaje());
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
        stringRequest.setTag("listarcv");

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
        //}

    }

    private void nuevaVentaRcv(){
        if (rcvListarVentaRespUnivida == null){

            return;
        }

        rcvListarVentaRespUnivida.setFechaRcv(simpleDateFormat.format(fechaSeleccion));

        limpiarDatos();

        String envioRcv = new Gson().toJson(rcvListarVentaRespUnivida);

        rcvListarVentaRespUnivida = null;
        bsbNuevoRcv.setEnabled(false);

        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_VISTA_EFECTIVIZAR_RCV, envioRcv);



    }

    @Override
    public void terminoDeImprimir() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
            }
        });
    }
}
