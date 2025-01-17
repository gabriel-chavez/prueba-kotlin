package com.emizor.univida.fragmento;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.emizor.univida.R;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.seguridad.CambiarClaveUnivida;
import com.emizor.univida.modelo.dominio.univida.RespUnivida;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.rest.VolleySingleton;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LogUtils;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class CambiarClaveFragment extends Fragment {

    private final String TAG = "CAMBIARCLAVE";

    private OnFragmentInteractionListener4 mListener;

    private EditText etClaveActual, etClaveNueva, etClaveRepetida;

    private User user;

    public CambiarClaveFragment() {
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
        View visatCambiarClave = inflater.inflate(R.layout.fragment_cambiar_clave, container, false);

        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getContext());

        user = controladorSqlite2.obtenerUsuario();

        controladorSqlite2.cerrarConexion();

        if (user == null){
            return visatCambiarClave;
        }

        etClaveActual = visatCambiarClave.findViewById(R.id.etClaveActual);
        etClaveNueva = visatCambiarClave.findViewById(R.id.etClaveNueva);
        etClaveRepetida = visatCambiarClave.findViewById(R.id.etClaveRepetida);

        BootstrapButton bsbAceptarCambiarClave = visatCambiarClave.findViewById(R.id.bsbAceptarCambiarClave);

        bsbAceptarCambiarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validarDatos();

            }
        });

        return visatCambiarClave;
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
            VolleySingleton.getInstance(getContext()).getRequestQueue().cancelAll("cambiarclave");
            super.onDetach();
            mListener = null;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void validarDatos(){
        View vistaForm = null;
        boolean estado = false;

        etClaveActual.setError(null);
        etClaveNueva.setError(null);
        etClaveRepetida.setError(null);

        etClaveActual.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etClaveActual.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        String strClave, strClaveRep, strClaveNueva;

        strClave = etClaveActual.getText().toString();
        strClaveNueva = etClaveNueva.getText().toString();
        strClaveRep = etClaveRepetida.getText().toString();

        if (strClave.isEmpty()){
            etClaveActual.setError("La contraseña actual es necesario.");
            vistaForm = etClaveActual;
            estado = true;
        }

        if (strClaveNueva.isEmpty()){
            etClaveNueva.setError("La contraseña nueva es necesario.");
            vistaForm = etClaveNueva;
            estado = true;
        }else if (strClaveNueva.length() < 6){
            etClaveActual.setError("La contraseña nueva debe ser mayor a 6 caracteres.");
            vistaForm = etClaveNueva;
            estado = true;
        }

        if (strClaveRep.isEmpty()){
            etClaveRepetida.setError("La contraseña repetida es necesaria.");
            vistaForm = etClaveRepetida;
            estado = true;
        }else if (! strClaveNueva.equals(strClaveRep)){
            etClaveRepetida.setError("La contraseña NO es igual a la contraseña nueva.");
            vistaForm = etClaveRepetida;
            estado = true;
        }


        if (estado){
            vistaForm.requestFocus();
            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);
        }else{
            mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.TRUE);

            cambiarClave();
        }

    }

    private void cambiarClave(){

        CambiarClaveUnivida cambiarClaveUnivida = new CambiarClaveUnivida();
        cambiarClaveUnivida.setUsername(UtilRest.getInstance().procesarDatosInterno(user.getUsername(), 1));
        cambiarClaveUnivida.setPassword(etClaveActual.getText().toString());
        cambiarClaveUnivida.setNewPassword(etClaveNueva.getText().toString());

        final String parametrosJson3 = cambiarClaveUnivida.toString();
        VolleySingleton.getInstance(getContext()).getRequestQueue().getCache().clear();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_SEGURIDAD_CAMBIAR_CLAVE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                LogUtils.i(TAG, "OK RESPONSE " + response);

                RespUnivida respUnivida = null;

                try{

                    respUnivida = new Gson().fromJson(response, RespUnivida.class);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_PROGRESS, Boolean.FALSE);

                if (respUnivida != null){
                    if (respUnivida.getExito()){

                        etClaveActual.setText("");
                        etClaveNueva.setText("");
                        etClaveRepetida.setText("");

                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE,respUnivida.getMensaje());

                    }else{
                        mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE,respUnivida.getMensaje());
                    }
                }else{

                    mListener.onAccionFragment(null, OnFragmentInteractionListener4.ACCION_MENSAJE,"Los datos estan vacios, comuniquese con el administrador.");
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
        stringRequest.setTag("cambiarclave");

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

}
