package com.emizor.univida.fragmento;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.emizor.univida.R;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.databinding.ActivityPrincipalBinding;
import com.emizor.univida.modelo.dominio.univida.ApiResponse;
import com.emizor.univida.modelo.dominio.univida.parametricas.ParametricaGenerica;
import com.emizor.univida.modelo.dominio.univida.soatc.CliValidarCoberturaAseguradoResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.ClientesObtenerDatosResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.DatosBusquedaAseguradoTomador;
import com.emizor.univida.modelo.dominio.univida.turnos.Punto;
import com.emizor.univida.rest.ApiService;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.util.ValidarCampo;
import com.emizor.univida.utils.ParametricasCache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoatcAseguradoBuscarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoatcAseguradoBuscarFragment extends Fragment {

    private FormViewModel viewModel;
//    private Spinner spinnerTipoDocumento;
//    private Spinner spinnerDepartamento;
//    private EditText etComplemento;
    private EditText etNumeroDocumento;

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
//        spinnerTipoDocumento = root.findViewById(R.id.spinnerTipoDocumento);
//        spinnerDepartamento = root.findViewById(R.id.spinnerDepartamento);
//        etComplemento = root.findViewById(R.id.complemento);
        etNumeroDocumento = root.findViewById(R.id.numeroDocumento);


        builder = new AlertDialog.Builder(getContext());

        Button btnBuscar = root.findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buscarAsegurado();
            }
        });
//        obtenerTiposDocumento();
//        obtenerDepartamentos();
        return root;
    }
    private void buscarAsegurado()
    {
        if(validarFormulario()){
            // Obtener valores seleccionados
        //    ParametricaGenerica tipoDocumento = (ParametricaGenerica) spinnerTipoDocumento.getSelectedItem();
           // ParametricaGenerica departamento = (ParametricaGenerica) spinnerDepartamento.getSelectedItem();

            String numeroDocumento = etNumeroDocumento.getText().toString().trim();


            Map<String, Object> parametros = new HashMap<>();
            parametros.put("per_t_par_cli_documento_identidad_tipo_fk", -1);
            parametros.put("per_documento_identidad_numero", numeroDocumento);
            parametros.put("per_documento_identidad_extension", "");
            parametros.put("per_t_par_gen_departamento_fk_documento_identidad", -1);
            // Si el departamento es necesario agregarlo
//            if (departamento != null) {
//                parametros.put("t_par_geo_departamento_fk", departamento.Identificador);
//            }


//            DatosBusquedaAseguradoTomador datosBusquedaAsegurado = new DatosBusquedaAseguradoTomador();
//            datosBusquedaAsegurado.NumeroDocumentoIdentidad=Integer.parseInt(numeroDocumento);
//            datosBusquedaAsegurado.TipoDocumentoIdentidad=tipoDocumento.Identificador;
//           // datosBusquedaAsegurado.Complemento=complemento;
//            datosBusquedaAsegurado.DepartamentoDocumentoIdentidad=departamento.Identificador;
//            datosBusquedaAsegurado.DepartamentoDocumentoIdentidadDescripcion=departamento.Descripcion;
//            datosBusquedaAsegurado.TipoDocumentoIdentidadDescripcion=tipoDocumento.Descripcion;


            ApiService apiService = new ApiService(getContext());
            String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_CLIENTES_OBTENER_DATOS;
            ((PrincipalActivity) requireActivity()).mostrarLoading(true);

            apiService.solicitudPost(url, parametros,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
                            try {
                                Type responseType = new TypeToken<ApiResponse<List<ClientesObtenerDatosResponse>>>() {}.getType();
                                ApiResponse<List<ClientesObtenerDatosResponse>> apiResponse = new Gson().fromJson(response, responseType);
                                Log.i("obtenerdatos", "onResponse: "+response);
                                if(apiResponse.exito){
                                    if (apiResponse.datos != null && apiResponse.datos.size() == 1) {
                                        ClientesObtenerDatosResponse datosAsegurado = apiResponse.datos.get(0);
                                         datosAsegurado.setEsNuevo(false);
                                            validarCoberturaAsegurado(datosAsegurado, resultado -> {
                                                if (resultado.exito) {
                                                    builder.setTitle("Atención")
                                                        .setIcon(android.R.drawable.ic_dialog_info);
                                                    builder.setMessage( "El cliente cuenta con cobertura de SOATC con los siguientes datos: "+resultado.datos.DatosAsegurado)
                                                        .setCancelable(false)
                                                        .setPositiveButton("Aceptar", null);
                                                        AlertDialog alert = builder.create();
                                                        alert.show();
                                                        return;
                                                } else {
//                                                    mvFormulario.ActiveViewIndex = 1; // paso 2
//                                                    CargarDatosPaso2();
                                                }
                                            });
                                    }
                                }
                                else{

                                    //mostrar lista de asegurados
                                }
//                                if (!apiResponse.exito &&
//                                        !"No existe SOATC vigente para el Número de Documento proporcionado."
//                                                .equals(apiResponse.mensaje))
//                                {
//                                    builder.setTitle("Atención")
//                                            .setIcon(android.R.drawable.ic_dialog_info);
//                                    builder.setMessage(apiResponse.mensaje)
//                                            .setCancelable(false)
//                                            .setPositiveButton("Aceptar", null);
//                                    AlertDialog alert = builder.create();
//                                    alert.show();
//                                    return;
//                                }
//                                if (!apiResponse.exito) {
//
//                                    new AlertDialog.Builder(requireContext())
//                                            .setTitle("Confirmación")
//                                            .setMessage(apiResponse.mensaje)
//                                            .setPositiveButton("Venta nueva", (dialog, which) -> {
//
//                                                ((PrincipalActivity)getActivity()).datosBusquedaAsegurado=datosBusquedaAsegurado;
//                                                ((PrincipalActivity)getActivity()).datosBeneficiario=null;
//                                                ((PrincipalActivity)getActivity()).datosAsegurado=null;
//                                                ((PrincipalActivity)getActivity()).datosTomador=null;
//                                                ((PrincipalActivity)getActivity()).datosFacturacion=null;
//
//                                                getFragmentManager()
//                                                        .beginTransaction()
//                                                        .replace(R.id.contenedor_vistas, new SoatcTomadorDiferenteFragment())
//                                                        .addToBackStack(null)
//                                                        .commit();
//                                            })
//                                            .setNegativeButton("Cancelar", (dialog, which) -> {
//                                                dialog.dismiss();
//                                            })
//                                            .show();
//
//                                } else {
//                                    String mensaje = "El asegurado con documento de identidad "
//                                            + apiResponse.datos.PerDocumentoIdentidadNumero + " " + apiResponse.datos.PerTParGenDepartamentoAbreviacionDocumentoIdentidad
//                                            + " cuenta con cobertura vigente desde el "
//                                            + apiResponse.datos.PolDetFechaVigenciaIniFormato + " hasta el "
//                                            + apiResponse.datos.PolDetFechaVigenciaFinFormato + ".";
//
//                                    new AlertDialog.Builder(requireContext())
//                                            .setTitle("Cobertura Vigente")
//                                            .setMessage(mensaje)
//                                            .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
//                                            .show();
//                                }
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
    private void validarCoberturaAsegurado(ClientesObtenerDatosResponse cliente, CoberturaCallback callback) {

        ((PrincipalActivity) requireActivity()).mostrarLoading(true);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("t_poliza_detalle_fk", -1);
        parametros.put("t_par_cli_documento_identidad_tipo_fk", cliente.getPerTParCliDocumentoIdentidadTipoFk());
        parametros.put("documento_identidad_numero", cliente.getPerDocumentoIdentidadNumero());
        parametros.put("documento_identidad_extension", cliente.getPerDocumentoIdentidadExtension());

        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_CLIENTES_VALIDAR_COBERTURA_ASEGURADO;
        ApiResponse<CliValidarCoberturaAseguradoResponse> resultado = new ApiResponse<>();
        ApiService api = new ApiService(getContext());
        api.solicitudPost(url, parametros, response -> {

            ((PrincipalActivity) requireActivity()).mostrarLoading(false);


            try {
                Type type = new TypeToken<ApiResponse<CliValidarCoberturaAseguradoResponse>>() {}.getType();
                ApiResponse<CliValidarCoberturaAseguradoResponse> apiResp = new Gson().fromJson(response, type);

            } catch (Exception e) {
                resultado.exito = false;
                resultado.mensaje = "Error procesando respuesta del servidor.";
            }

            // devolver resultado al que llamó el método
            callback.onResultado(resultado);

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

//    private void obtenerTiposDocumento() {
//        List<ParametricaGenerica> tiposDocumento = ParametricasCache.getInstance().getDocumentosIdentidad();
//        if (tiposDocumento != null && !tiposDocumento.isEmpty()) {
//            ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(
//                    getContext(), android.R.layout.simple_spinner_item, tiposDocumento);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinnerTipoDocumento.setAdapter(adapter);
//
//        }
//    }
//    private void obtenerDepartamentos() {
//
//        List<ParametricaGenerica> departamentos = ParametricasCache.getInstance().getDepartamentos();
//
//        if (departamentos != null && !departamentos.isEmpty()) {
//            ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(
//                    getContext(), android.R.layout.simple_spinner_item, departamentos);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinnerDepartamento.setAdapter(adapter);
//        }
//
//
//    }

}