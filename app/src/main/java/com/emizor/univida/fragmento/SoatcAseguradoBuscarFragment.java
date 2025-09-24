package com.emizor.univida.fragmento;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.emizor.univida.modelo.dominio.univida.soatc.DatosBusquedaAseguradoTomador;
import com.emizor.univida.modelo.dominio.univida.turnos.Punto;
import com.emizor.univida.rest.ApiService;
import com.emizor.univida.rest.DatosConexion;
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
    private Spinner spinnerTipoDocumento;
    private Spinner spinnerDepartamento;
    private EditText etNumeroDocumento;
    private EditText etComplemento;
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
        spinnerTipoDocumento = root.findViewById(R.id.spinnerTipoDocumento);
        spinnerDepartamento = root.findViewById(R.id.spinnerDepartamento);
        etNumeroDocumento = root.findViewById(R.id.numeroDocumento);
        etComplemento = root.findViewById(R.id.complemento);

        builder = new AlertDialog.Builder(getContext());

        Button btnBuscar = root.findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarAsegurado();
            }
        });
        obtenerTiposDocumento();
        obtenerDepartamentos();
        return root;
    }
    private void buscarAsegurado()
    {
        if(validarFormulario()){
            // Obtener valores seleccionados
            ParametricaGenerica tipoDocumento = (ParametricaGenerica) spinnerTipoDocumento.getSelectedItem();
            ParametricaGenerica departamento = (ParametricaGenerica) spinnerDepartamento.getSelectedItem();

            String numeroDocumento = etNumeroDocumento.getText().toString().trim();
            String complemento = etComplemento.getText().toString().trim();

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("t_par_cli_documento_identidad_tipo_fk", tipoDocumento.Identificador);
            parametros.put("documento_identidad_numero", numeroDocumento);
            parametros.put("documento_identidad_extension", complemento);
            // Si el departamento es necesario agregarlo
            if (departamento != null) {
                parametros.put("t_par_geo_departamento_fk", departamento.Identificador);
            }


            DatosBusquedaAseguradoTomador datosBusquedaAsegurado = new DatosBusquedaAseguradoTomador();
            datosBusquedaAsegurado.NumeroDocumentoIdentidad=Integer.parseInt(numeroDocumento);
            datosBusquedaAsegurado.TipoDocumentoIdentidad=tipoDocumento.Identificador;
            datosBusquedaAsegurado.Complemento=complemento;
            datosBusquedaAsegurado.DepartamentoDocumentoIdentidad=departamento.Identificador;
            datosBusquedaAsegurado.DepartamentoDocumentoIdentidadDescripcion=departamento.Descripcion;
            datosBusquedaAsegurado.TipoDocumentoIdentidadDescripcion=tipoDocumento.Descripcion;


            ApiService apiService = new ApiService(getContext());
            String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_CLIENTES_VALIDAR_COBERTURA_ASEGURADO;
            ((PrincipalActivity) requireActivity()).mostrarLoading(true);

            apiService.solicitudPost(url, parametros,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
                            try {
                                Type responseType = new TypeToken<ApiResponse<CliValidarCoberturaAseguradoResponse>>() {}.getType();
                                ApiResponse<CliValidarCoberturaAseguradoResponse> apiResponse = new Gson().fromJson(response, responseType);
                                if(apiResponse.codigo_retorno!=0)
                                {
                                    builder.setTitle("Atención")
                                            .setIcon(android.R.drawable.ic_dialog_info);
                                    builder.setMessage(apiResponse.mensaje)
                                            .setCancelable(false)
                                            .setPositiveButton("Aceptar", null);
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    return;
                                }
                                if (!apiResponse.exito) {

                                    new AlertDialog.Builder(requireContext())
                                            .setTitle("Confirmación")
                                            .setMessage(apiResponse.mensaje)
                                            .setPositiveButton("Venta nueva", (dialog, which) -> {

                                                ((PrincipalActivity)getActivity()).datosBusquedaAsegurado=datosBusquedaAsegurado;

                                                getFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.contenedor_vistas, new SoatcTomadorDiferenteFragment())
                                                        .addToBackStack(null)
                                                        .commit();
                                            })
                                            .setNegativeButton("Cancelar", (dialog, which) -> {
                                                dialog.dismiss();
                                            })
                                            .show();

                                } else {
                                    String mensaje = "El asegurado con documento de identidad "
                                            + apiResponse.datos.PerDocumentoIdentidadNumero + " " + apiResponse.datos.PerTParGenDepartamentoAbreviacionDocumentoIdentidad
                                            + " cuenta con cobertura vigente desde el "
                                            + apiResponse.datos.PolDetFechaVigenciaIniFormato + " hasta el "
                                            + apiResponse.datos.PolDetFechaVigenciaFinFormato + ".";

                                    new AlertDialog.Builder(requireContext())
                                            .setTitle("Cobertura Vigente")
                                            .setMessage(mensaje)
                                            .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                                            .show();
                                }
                            } catch (Exception e) {

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