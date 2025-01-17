package com.emizor.univida.fragmento;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.emizor.univida.R;
import com.emizor.univida.adapter.RcvListaRcvAdapter;
import com.emizor.univida.adapter.RecyclerViewOnItemCickListener;
import com.emizor.univida.excepcion.ErrorPapelException;
import com.emizor.univida.excepcion.ImpresoraErrorException;
import com.emizor.univida.excepcion.NoHayPapelException;
import com.emizor.univida.excepcion.VoltageBajoException;
import com.emizor.univida.imprime.ImprimirAvisoListener;
import com.emizor.univida.imprime.ImprimirFactura;
import com.emizor.univida.modelo.dominio.univida.RespUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.DatoRcvLista;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.ObtenerRcvRespUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.ObtenerRcvUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RcvListarRespUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RcvListarUnivida;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.RevertirRcvUnivida;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.rest.VolleySingleton;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LogUtils;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaRcvFragment extends Fragment {

    private final String TAG = "LISTARRCV";

    private OnFragmentInteractionListener4 mListener;

    private RecyclerView rvListaRcv;
    private RcvListaRcvAdapter rcvListaRcvAdapter;
    private LinearLayoutManager linearLayoutManager;
    private User user;
    private DatoRcvLista datoRcvListaSeleccionado;

    public ListaRcvFragment() {
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
        View vistaListaRcv = inflater.inflate(R.layout.fragment_lista_rcv, container, false);

        linearLayoutManager = new LinearLayoutManager(vistaListaRcv.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvListaRcv = vistaListaRcv.findViewById(R.id.rvListaRcv);

        rcvListaRcvAdapter = new RcvListaRcvAdapter(vistaListaRcv.getContext(), new ArrayList<DatoRcvLista>(), new RecyclerViewOnItemCickListener() {
            @Override
            public void onClick(View view, int position, int accionTipo) {

                datoRcvListaSeleccionado = rcvListaRcvAdapter.getRcvDato(position);

                if (datoRcvListaSeleccionado.getRcvEstadoFk() == 1){
                    abrirMenuItems();
                }

            }
        });

        rvListaRcv.setLayoutManager(linearLayoutManager);

        rvListaRcv.setAdapter(rcvListaRcvAdapter);

        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getContext());

        user = controladorSqlite2.obtenerUsuario();

        controladorSqlite2.cerrarConexion();

        if (user == null){
            return vistaListaRcv;
        }

        listarRcv();

        return vistaListaRcv;
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
            VolleySingleton.getInstance(getContext()).getRequestQueue().cancelAll("revertirrcv");
            VolleySingleton.getInstance(getContext()).getRequestQueue().cancelAll("reimprimirrcv");
            super.onDetach();
            mListener = null;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void listarRcv(){

        RcvListarUnivida rcvListarUnivida = new RcvListarUnivida();

        rcvListarUnivida.setGestionFk(-1);
        rcvListarUnivida.setVentaVendedor(user.getUsername());

        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.TRUE);

        final String parametrosJson3 = rcvListarUnivida.toString();
        VolleySingleton.getInstance(getContext()).getRequestQueue().getCache().clear();

        LogUtils.i(TAG, "URL URL URL " +DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_RCV_LISTAR);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_RCV_LISTAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                LogUtils.i(TAG, "OK RESPONSE " + response);
                RcvListarRespUnivida rcvListarRespUnivida = null;

                try{

                    rcvListarRespUnivida = new Gson().fromJson(response, RcvListarRespUnivida.class);

                    LogUtils.i(TAG, "OK CONVERSION " + new Gson().toJson(rcvListarRespUnivida));

                }catch (Exception ex){

                    ex.printStackTrace();

                }
                final RcvListarRespUnivida rcvListarRespUnivida1 = rcvListarRespUnivida;
                if (rcvListarRespUnivida != null){

                    if (rcvListarRespUnivida.getExito()){

                        final List<DatoRcvLista> listaDatosRcv = rcvListarRespUnivida1.getDatos();

                        for (int i = 0; i< listaDatosRcv.size(); i++){
                            listaDatosRcv.get(i).resetEstado();
                        }

                        rvListaRcv.invalidate();
                        rvListaRcv.post(new Runnable() {
                            @Override
                            public void run() {
                                List<DatoRcvLista> listaTotal = new ArrayList<>();

                                listaTotal.addAll(listaDatosRcv);

                                rcvListaRcvAdapter.cambiarLista(listaTotal);

                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                            }
                        });

                    }else{

                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, rcvListarRespUnivida.getMensaje());

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

    }

    private void abrirMenuItems(){

        if (datoRcvListaSeleccionado != null ) {
            if (datoRcvListaSeleccionado.getRcvEstadoFk() == 1) {

                BottomSheetBuilder bottomSheetBuilder = new BottomSheetBuilder(getContext(), R.style.AppTheme_BottomSheetDialog_Custom)
                        .setMode(BottomSheetBuilder.MODE_LIST)
                        .addItem(503, "REVERTIR", R.drawable.ic_action_revertir)
                        .setItemClickListener(new BottomSheetItemClickListener() {
                            @Override
                            public void onBottomSheetItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case 502:
                                        remitirRcv();
                                        break;
                                    case 503:
                                        procesarReversionRcv();
                                        break;
                                    case 504:
                                        procesarReimprimirRcv();
                                        break;
                                }
                            }
                        });

                if (! datoRcvListaSeleccionado.getRcvEstadoRemitido()){
                    bottomSheetBuilder = bottomSheetBuilder.addItem(502,"REMITIR", R.drawable.ic_remitir_rcv);
                }

                if (datoRcvListaSeleccionado.getRcvEstadoFk() == 1){
                    bottomSheetBuilder = bottomSheetBuilder.addItem(504,"REIMPRIMIR", R.drawable.ic_imprimir);
                }

                BottomSheetMenuDialog dialog = bottomSheetBuilder.createDialog();

                dialog.show();

            }else{

                datoRcvListaSeleccionado = null;

            }

        }

    }

    private void procesarReversionRcv(){

        if (datoRcvListaSeleccionado == null){
            return;
        }

        RevertirRcvUnivida revertirRcvUnivida = new RevertirRcvUnivida();
        revertirRcvUnivida.setGestionFk(-1);
        revertirRcvUnivida.setRcvSecuencial(datoRcvListaSeleccionado.getRcvSecuencial());
        revertirRcvUnivida.setVentaVendedor(user.getUsername());

        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.TRUE);

        final String parametrosJson3 = revertirRcvUnivida.toString();
        VolleySingleton.getInstance(getContext()).getRequestQueue().getCache().clear();

        LogUtils.i(TAG, "URL URL URL " +DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_RCV_LISTAR);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_RCV_REVERTIR, new Response.Listener<String>() {
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

                if (respUnivida != null){

                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, respUnivida.getMensaje());
                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);

                    if (respUnivida.getExito()) {
                        rcvListaRcvAdapter.cambiarEstado(datoRcvListaSeleccionado, 2);
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
        stringRequest.setTag("revertirrcv");

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void remitirRcv(){
        if (datoRcvListaSeleccionado != null){

            LogUtils.i(TAG, " satoRCV " + new Gson().toJson(datoRcvListaSeleccionado));

            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_VISTA_REMITIR_RCV, datoRcvListaSeleccionado.getRcvSecuencial());

        }
    }

    public void cambiar(Integer secuencial){
        rcvListaRcvAdapter.cambiarEstadoRemitir(new DatoRcvLista(secuencial), true);
    }

    private void procesarReimprimirRcv(){
        if (datoRcvListaSeleccionado == null){
            return;
        }/*else{

            final ImprimirFactura imprimirFactura2;
            imprimirFactura2 = ImprimirFactura.obtenerImpresora(getContext());
            imprimirFactura2.setAvisoListener(new ImprimirAvisoListener() {
                @Override
                public void terminoDeImprimir() {
                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                }
            });

            EfectivizarRcvUnivida efectivizarRcvUnivida = new EfectivizarRcvUnivida();

            efectivizarRcvUnivida.setGestionFk(-1);
            efectivizarRcvUnivida.setRcvCantidad(datoRcvListaSeleccionado.getRcvCantidad());
            efectivizarRcvUnivida.setRcvImporte(datoRcvListaSeleccionado.getRcvFormularioImporte());
            efectivizarRcvUnivida.setSucursalFk(user.getDatosUsuario().getSucursalCodigo());
            efectivizarRcvUnivida.setVentaFecha(datoRcvListaSeleccionado.getRcvFormularioFecha());
            efectivizarRcvUnivida.setVentaVendedor(user.getUsername());

            RcvListarVentaRespUnivida rcvListarVentaRespUnivida = new RcvListarVentaRespUnivida();
            RcvVentaDatos rcvVentaDatos = new RcvVentaDatos();

            rcvVentaDatos.setCantidad(datoRcvListaSeleccionado.getRcvCantidad());
            rcvVentaDatos.setCantidadAnulados(datoRcvListaSeleccionado.getRcvCantidadAnulados());
            rcvVentaDatos.setCantidadRevertidos(datoRcvListaSeleccionado.getRcvCantidadRevertidos());
            rcvVentaDatos.setCantidadValidos(datoRcvListaSeleccionado.getRcvCantidadValidos());
            rcvVentaDatos.setFormularioImporte(datoRcvListaSeleccionado.getRcvFormularioImporte().intValue());

            rcvListarVentaRespUnivida.setDatos(rcvVentaDatos);

            try {

                imprimirFactura2.prepararImpresionRcv(user, efectivizarRcvUnivida, String.valueOf(datoRcvListaSeleccionado.getRcvSecuencial()), rcvListarVentaRespUnivida);

            }catch (Exception ex){
                ex.printStackTrace();
                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No se puede imprimir los datos por que algunos o todos son nulos.");
                return;
            }

            ListaRcvFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                imprimirFactura2.imprimirFactura2();

                            } catch (ImpresoraErrorException e) {
                                e.printStackTrace();
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error en la impresora.");
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                            } catch (NoHayPapelException e) {
                                e.printStackTrace();
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No hay papel para imprimir.");
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                            } catch (VoltageBajoException e) {
                                e.printStackTrace();
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error de Bateria baja. No podra imprimir con la bateria baja.");
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                            } catch (ErrorPapelException e) {
                                e.printStackTrace();
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error en la impresora");
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                            }catch (Exception ex){
                                ex.printStackTrace();
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error al imprimir los datos. Si esto persiste comuniquese con el encargado.");
                                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                            }
                        }
                    }).start();
                }
            });

            //return;
        }*/

        if (ListaRcvFragment.this.getActivity() != null){
            try{
                ListaRcvFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ObtenerRcvUnivida obtenerRcvUnivida = new ObtenerRcvUnivida();
                        obtenerRcvUnivida.setGestionFk(-1);
                        obtenerRcvUnivida.setRcvSecuencial(datoRcvListaSeleccionado.getRcvSecuencial());
                        obtenerRcvUnivida.setVentaVendedor(user.getUsername());

                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.TRUE);

                        final String parametrosJson3 = obtenerRcvUnivida.toString();
                        VolleySingleton.getInstance(getContext()).getRequestQueue().getCache().clear();

                        LogUtils.i(TAG, "URL URL URL " +DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_RCV_OBTENER);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_RCV_OBTENER, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                LogUtils.i(TAG, "OK RESPONSE " + response);
                                ObtenerRcvRespUnivida obtenerRcvRespUnivida = null;

                                final ImprimirFactura imprimirFactura;
                                imprimirFactura = ImprimirFactura.obtenerImpresora(getContext());
                                imprimirFactura.setAvisoListener(new ImprimirAvisoListener() {
                                    @Override
                                    public void terminoDeImprimir() {
                                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                    }
                                });

                                try{

                                    obtenerRcvRespUnivida = new Gson().fromJson(response, ObtenerRcvRespUnivida.class);

                                    LogUtils.i(TAG, "OK CONVERSION " + new Gson().toJson(obtenerRcvRespUnivida));

                                }catch (Exception ex){

                                    ex.printStackTrace();
                                }

                                if (obtenerRcvRespUnivida != null){



                                    if (obtenerRcvRespUnivida.getExito()) {
                                        //rcvListaRcvAdapter.cambiarEstado(datoRcvListaSeleccionado, 2);
                                        try {

                                            imprimirFactura.prepararImpresionRcv(user, obtenerRcvRespUnivida);

                                        }catch (Exception ex){
                                            ex.printStackTrace();
                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No se puede imprimir los datos por que algunos o todos son nulos.");
                                            return;
                                        }

                                        ListaRcvFragment.this.getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {

                                                            imprimirFactura.imprimirFactura2();

                                                        } catch (ImpresoraErrorException e) {
                                                            e.printStackTrace();
                                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error en la impresora.");
                                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                                        } catch (NoHayPapelException e) {
                                                            e.printStackTrace();
                                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "No hay papel para imprimir.");
                                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                                        } catch (VoltageBajoException e) {
                                                            e.printStackTrace();
                                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error de Bateria baja. No podra imprimir con la bateria baja.");
                                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                                        } catch (ErrorPapelException e) {
                                                            e.printStackTrace();
                                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error en la impresora");
                                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                                        }catch (Exception ex){
                                                            ex.printStackTrace();
                                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, "Error al imprimir los datos. Si esto persiste comuniquese con el encargado.");
                                                            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
                                                        }
                                                    }
                                                }).start();
                                            }
                                        });
                                    }else{
                                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE, obtenerRcvRespUnivida.getMensaje());
                                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
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
                        stringRequest.setTag("reimprimirrcv");

                        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
                    }
                });
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }


    }

}
