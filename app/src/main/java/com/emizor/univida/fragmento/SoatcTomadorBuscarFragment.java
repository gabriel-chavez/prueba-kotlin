package com.emizor.univida.fragmento;

import android.arch.lifecycle.ViewModelProvider;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.emizor.univida.R;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.modelo.dominio.univida.ApiResponse;
import com.emizor.univida.modelo.dominio.univida.parametricas.ParametricaGenerica;
import com.emizor.univida.modelo.dominio.univida.soatc.CliObtenerDatosResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.CliValidarCoberturaAseguradoResponse;
import com.emizor.univida.rest.ApiService;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.utils.ParametricasCache;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.util.Date;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoatcTomadorBuscarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoatcTomadorBuscarFragment extends Fragment {
    private FormViewModel viewModel;
    private Spinner spinnerTipoDocumento;
    private Spinner spinnerDepartamento;
    private EditText etNumeroDocumento;
    private EditText etComplemento;
    private Button btnAtras, btnSiguiente;
    public CliObtenerDatosResponse CliObtenerDatosActivity;

    AlertDialog.Builder builder;


    public SoatcTomadorBuscarFragment() {
        // Required empty public constructor
    }


    public static SoatcTomadorBuscarFragment newInstance(String param1, String param2) {
        SoatcTomadorBuscarFragment fragment = new SoatcTomadorBuscarFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_soatc_tomador_buscar, container, false);


        spinnerTipoDocumento = root.findViewById(R.id.spinnerTipoDocumento);
        spinnerDepartamento = root.findViewById(R.id.spinnerDepartamento);
        etNumeroDocumento = root.findViewById(R.id.numeroDocumento);
        etComplemento = root.findViewById(R.id.complemento);

        builder = new AlertDialog.Builder(getContext());
        btnAtras = root.findViewById(R.id.btnAtras);
        btnSiguiente = root.findViewById(R.id.btnSiguienteBuscaTomador);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFormulario()) {
                    buscarTomador();
                }
            }
        });
        btnAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        obtenerTiposDocumento();
        obtenerDepartamentos();
        return root;
    }

    private void buscarTomador() {

        // Obtener valores seleccionados
        ParametricaGenerica tipoDocumento = (ParametricaGenerica) spinnerTipoDocumento.getSelectedItem();
        ParametricaGenerica departamento = (ParametricaGenerica) spinnerDepartamento.getSelectedItem();

        String numeroDocumento = etNumeroDocumento.getText().toString().trim();
        String complemento = etComplemento.getText().toString().trim();

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("per_t_par_cli_documento_identidad_tipo_fk", tipoDocumento.Identificador);
        parametros.put("per_documento_identidad_numero", numeroDocumento);
        parametros.put("per_documento_identidad_extension", complemento);
        parametros.put("per_t_par_gen_departamento_fk_documento_identidad", departamento.Identificador);
        ApiService apiService = new ApiService(getContext());
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_CLIENTES_OBTENER_DATOS;
        ((PrincipalActivity) requireActivity()).mostrarLoading(true);

        apiService.solicitudPost(url, parametros, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ((PrincipalActivity) requireActivity()).mostrarLoading(false);
                try {
                    Type responseType = new TypeToken<ApiResponse<CliObtenerDatosResponse>>() {
                    }.getType();
                    ApiResponse<CliObtenerDatosResponse> apiResponse = null;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        apiResponse = parsearRespuesta(response);
                    }

                    if (!apiResponse.exito) {
                        if(apiResponse.codigo_retorno==0){
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Confirmación")
                                    .setMessage(apiResponse.mensaje)
                                    .setPositiveButton("Registrar Tomador", (dialog, which) -> {
                                        CliObtenerDatosResponse cliObtenerDatos= new CliObtenerDatosResponse();
                                        cliObtenerDatos.PerTParCliDocumentoIdentidadTipoFk=tipoDocumento.Identificador;
                                        cliObtenerDatos.PerTParCliDocumentoIdentidadTipoDescripcion=tipoDocumento.Descripcion;
                                        cliObtenerDatos.PerDocumentoIdentidadNumero=Integer.parseInt(numeroDocumento);;
                                        cliObtenerDatos.PerTParGenDepartamentoDescripcionDocumentoIdentidad=departamento.Descripcion;
                                        cliObtenerDatos.PerTParGenDepartamentoFkDocumentoIdentidad=departamento.Identificador;
                                        cliObtenerDatos.PerDocumentoIdentidadExtension=complemento;

                                        ((PrincipalActivity)getActivity()).datosTomador=cliObtenerDatos;
                                        getFragmentManager().beginTransaction()
                                                .replace(R.id.contenedor_vistas, new SoatcTomadorDatosFragment())
                                                .addToBackStack(null)
                                                .commit();
                                    }).setNegativeButton("Cancelar", (dialog, which) -> {
                                        dialog.dismiss();
                                    }).show();
                        }else{
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Atención")
                                    .setMessage(apiResponse.mensaje)
                                    .setPositiveButton("Aceptar", (dialog, which) -> {
                                    }).show();
                        }


                    } else {

                        ((PrincipalActivity)getActivity()).datosTomador=apiResponse.datos;

                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.contenedor_vistas, new SoatcTomadorDatosFragment())
                                .addToBackStack(null)
                                .commit();
                    }

                } catch (Exception e) {

                    Toast.makeText(getContext(), "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((PrincipalActivity) requireActivity()).mostrarLoading(false);
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ApiResponse<CliObtenerDatosResponse> parsearRespuesta(String response) {
        Type responseType = new TypeToken<ApiResponse<CliObtenerDatosResponse>>() {}.getType();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> {
                    String dateStr = json.getAsString();
                    try {
                        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(dateStr);
                    } catch (ParseException e1) {
                        try {
                            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateStr);
                        } catch (ParseException e2) {
                            return null;
                        }
                    }
                })
                .create();

        return gson.fromJson(response, responseType);
    }

    private boolean validarFormulario() {

        if (etNumeroDocumento.getText().toString().isEmpty()) {
            etNumeroDocumento.setError("Este campo es obligatorio");
            return false;
        }
        return true;
    }

    private void obtenerTiposDocumento() {
        List<ParametricaGenerica> tiposDocumento = ParametricasCache.getInstance().getDocumentosIdentidad();
        if (tiposDocumento != null && !tiposDocumento.isEmpty()) {
            ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_spinner_item, tiposDocumento);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTipoDocumento.setAdapter(adapter);

        }
    }
    private void obtenerDepartamentos() {
        List<ParametricaGenerica> departamentos = ParametricasCache.getInstance().getDepartamentos();

        if (departamentos != null && !departamentos.isEmpty()) {
            ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_spinner_item, departamentos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDepartamento.setAdapter(adapter);
        }
    }

}