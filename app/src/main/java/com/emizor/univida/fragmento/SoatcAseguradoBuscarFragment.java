package com.emizor.univida.fragmento;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.emizor.univida.R;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.adapter.AseguradoTomadorAdapter;
import com.emizor.univida.modelo.dominio.univida.ApiResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.CliObtenerDatosResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.CliValidarCoberturaAseguradoResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.DatosBusquedaAseguradoTomador;
import com.emizor.univida.rest.ApiService;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.util.ValidarCampo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoatcAseguradoBuscarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoatcAseguradoBuscarFragment extends Fragment {

    private EditText etNumeroDocumento;
    private RecyclerView rvResultados;
    private TextView tvMensajeResultadosMultiples;
    private AseguradoTomadorAdapter aseguradoAdapter;

    AlertDialog.Builder builder;
    public SoatcAseguradoBuscarFragment() {
        // Required empty public constructor
    }


    public static SoatcAseguradoBuscarFragment newInstance(String param1, String param2) {
        SoatcAseguradoBuscarFragment fragment = new SoatcAseguradoBuscarFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_soatc_asegurado_buscar, container, false);

        etNumeroDocumento = root.findViewById(R.id.numeroDocumento);
        rvResultados = root.findViewById(R.id.rvResultados);
        tvMensajeResultadosMultiples = root.findViewById(R.id.tvMensajeResultadosMultiples);

        rvResultados.setLayoutManager(new LinearLayoutManager(getContext()));
        aseguradoAdapter = new AseguradoTomadorAdapter(new ArrayList<>(), item -> {
            // Acción al seleccionar un asegurado de la lista
            item.EsNuevo=false;
            procesarSeleccionAsegurado(item);
        });
        rvResultados.setAdapter(aseguradoAdapter);


        builder = new AlertDialog.Builder(getContext());

        Button btnBuscar = root.findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buscarAsegurado();
            }
        });
        return root;
    }

    private void procesarSeleccionAsegurado(CliObtenerDatosResponse datosAsegurado) {


        // Actualizar datos de búsqueda en PrincipalActivity para evitar NPE en el siguiente fragmento
        DatosBusquedaAseguradoTomador dbat = new DatosBusquedaAseguradoTomador();
        dbat.NumeroDocumentoIdentidad = datosAsegurado.PerDocumentoIdentidadNumero;
        dbat.TipoDocumentoIdentidad = datosAsegurado.PerTParCliDocumentoIdentidadTipoFk;
        dbat.TipoDocumentoIdentidadDescripcion = datosAsegurado.PerTParCliDocumentoIdentidadTipoDescripcion;
        dbat.Complemento = datosAsegurado.PerDocumentoIdentidadExtension;
        dbat.DepartamentoDocumentoIdentidad = datosAsegurado.PerTParGenDepartamentoFkDocumentoIdentidad;
        dbat.DepartamentoDocumentoIdentidadDescripcion = datosAsegurado.PerTParGenDepartamentoDescripcionDocumentoIdentidad;
        ((PrincipalActivity) getActivity()).datosBusquedaAseguradoTomador = dbat;
        ((PrincipalActivity) getActivity()).datosBeneficiario = null;

        validarCoberturaAsegurado(datosAsegurado, resultado -> {
            if (resultado.exito) {
                builder.setTitle("Atención")
                        .setIcon(android.R.drawable.ic_dialog_info);
                builder.setMessage("El cliente cuenta con cobertura de SOATC con los siguientes datos: " + resultado.datos.DatosAsegurado)
                        .setCancelable(false)
                        .setPositiveButton("Aceptar", null);
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                // Navegar al siguiente paso con los datos del asegurado
                ((PrincipalActivity)getActivity()).datosAsegurado = datosAsegurado;
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contenedor_vistas, new SoatcAseguradoDatosFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void buscarAsegurado()
    {
        if(validarFormulario()){

            String numeroDocumento = etNumeroDocumento.getText().toString().trim();

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("per_t_par_cli_documento_identidad_tipo_fk", -1);
            parametros.put("per_documento_identidad_numero", numeroDocumento);
            parametros.put("per_documento_identidad_extension", "");
            parametros.put("per_t_par_gen_departamento_fk_documento_identidad", -1);

            ApiService apiService = new ApiService(getContext());
            String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_CLIENTES_OBTENER_DATOS;
            ((PrincipalActivity) requireActivity()).mostrarLoading(true);

            apiService.solicitudPost(url, parametros,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
                            try {
                                Type responseType = new TypeToken<ApiResponse<List<CliObtenerDatosResponse>>>() {}.getType();
                                ApiResponse<List<CliObtenerDatosResponse>> apiResponse = new Gson().fromJson(response, responseType);
                                Log.i("obtenerdatos", "onResponse: "+response);

                                if(apiResponse.exito){
                                    rvResultados.setVisibility(View.GONE);
                                    tvMensajeResultadosMultiples.setVisibility(View.GONE);
                                    if (apiResponse.datos != null && apiResponse.datos.size() == 1) {
                                        CliObtenerDatosResponse cliente = apiResponse.datos.get(0);
                                        cliente.EsNuevo = false;
                                        procesarSeleccionAsegurado(cliente);
                                    } else if (apiResponse.datos != null && apiResponse.datos.size() > 1) {
                                        aseguradoAdapter.updateData(apiResponse.datos);
                                        rvResultados.setVisibility(View.VISIBLE);
                                        tvMensajeResultadosMultiples.setVisibility(View.VISIBLE);
                                    }
                                }
                                else{
                                    //mostrar lista de asegurados si vienen en datos aunque exito sea falso
                                    if (apiResponse.mensaje.equals("La información del cliente no pudo ser obtenida.")) {
                                        builder.setTitle("Atención")
                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                .setMessage("El cliente no se encuentra registrado ¿desea registrarlo ahora?")
                                                .setCancelable(false)
                                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        CliObtenerDatosResponse cliente = new CliObtenerDatosResponse();
                                                        cliente.EsNuevo = true;
                                                        cliente.PerDocumentoIdentidadNumero = Integer.parseInt(numeroDocumento);
                                                        procesarSeleccionAsegurado(cliente);
                                                    }
                                                })
                                                .setNegativeButton("Cancelar", null);
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    } else {
                                        rvResultados.setVisibility(View.GONE);
                                        tvMensajeResultadosMultiples.setVisibility(View.GONE);
                                        // Manejar caso de no encontrado o error
                                        builder.setTitle("Atención")
                                                .setIcon(android.R.drawable.ic_dialog_info);
                                        builder.setMessage(apiResponse.mensaje)
                                                .setCancelable(false)
                                                .setPositiveButton("Aceptar", null);
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("Asegurado",e.getMessage());
                                Toast.makeText(getContext(), "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
                            Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    public interface CoberturaCallback {
        void onResultado(ApiResponse<CliValidarCoberturaAseguradoResponse> resultado);
    }
    private void validarCoberturaAsegurado(CliObtenerDatosResponse cliente, CoberturaCallback callback) {

        ((PrincipalActivity) requireActivity()).mostrarLoading(true);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("t_poliza_detalle_fk", -1);
        parametros.put("t_par_cli_documento_identidad_tipo_fk", cliente.PerTParCliDocumentoIdentidadTipoFk);
        parametros.put("documento_identidad_numero", cliente.PerDocumentoIdentidadNumero);
        parametros.put("documento_identidad_extension", cliente.PerDocumentoIdentidadExtension);

        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_CLIENTES_VALIDAR_COBERTURA_ASEGURADO;
        ApiResponse<CliValidarCoberturaAseguradoResponse> resultado = new ApiResponse<>();
        ApiService api = new ApiService(getContext());
        api.solicitudPost(url, parametros, response -> {

            ((PrincipalActivity) requireActivity()).mostrarLoading(false);


            try {
                Type type = new TypeToken<ApiResponse<CliValidarCoberturaAseguradoResponse>>() {}.getType();
                ApiResponse<CliValidarCoberturaAseguradoResponse> apiResp = new Gson().fromJson(response, type);
                callback.onResultado(apiResp);


            } catch (Exception e) {
                resultado.exito = false;
                resultado.mensaje = "Error procesando respuesta del servidor.";
                callback.onResultado(resultado);
            }

        }, error -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);

            resultado.exito = false;
            resultado.mensaje = "Error de conexión. Intentá nuevamente.";
            callback.onResultado(resultado);
        });
    }
    private boolean validarFormulario() {
        // Obtener el texto del campo de número de documento
        String numeroDocumento = etNumeroDocumento.getText().toString();

        // Verificar si el campo está vacío
        if (numeroDocumento.isEmpty()) {
            etNumeroDocumento.setError("Este campo es obligatorio");
            return false;
        }

        // Verificar si el valor ingresado es un número entero
        if (!ValidarCampo.validarNumeroEntero(numeroDocumento)) {
            etNumeroDocumento.setError("El número de documento debe ser un número entero válido");
            return false;
        }

        return true;
    }

}
