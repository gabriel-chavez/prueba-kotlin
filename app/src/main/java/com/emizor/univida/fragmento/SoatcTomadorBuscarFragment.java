package com.emizor.univida.fragmento;

import android.arch.lifecycle.ViewModelProvider;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.emizor.univida.R;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.adapter.AseguradoTomadorAdapter;
import com.emizor.univida.modelo.dominio.univida.ApiResponse;
import com.emizor.univida.modelo.dominio.univida.parametricas.ParametricaGenerica;
import com.emizor.univida.modelo.dominio.univida.soatc.CliObtenerDatosResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.CliValidarCoberturaAseguradoResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.DatosBusquedaAseguradoTomador;
import com.emizor.univida.rest.ApiService;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.util.ValidarCampo;
import com.emizor.univida.utils.ParametricasCache;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.util.ArrayList;
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
    private Button btnAtras, btnSiguiente;
    private EditText etNumeroDocumento;
    private RecyclerView rvResultados;
    private TextView tvMensajeResultadosMultiples;
    private AseguradoTomadorAdapter tomadorAdapter;

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
        etNumeroDocumento = root.findViewById(R.id.numeroDocumento);
        rvResultados = root.findViewById(R.id.rvResultados);
        tvMensajeResultadosMultiples = root.findViewById(R.id.tvMensajeResultadosMultiples);

        btnAtras = root.findViewById(R.id.btnAtras);
        btnSiguiente = root.findViewById(R.id.btnSiguiente);

        rvResultados.setLayoutManager(new LinearLayoutManager(getContext()));
        tomadorAdapter = new AseguradoTomadorAdapter(new ArrayList<>(), item -> {
            // Acción al seleccionar un asegurado de la lista
            item.EsNuevo=false;
            procesarSeleccionTomador(item);
        });
        rvResultados.setAdapter(tomadorAdapter);


        builder = new AlertDialog.Builder(getContext());
        btnAtras.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());


        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buscarTomador();
            }
        });
        return root;
    }
    private void procesarSeleccionTomador(CliObtenerDatosResponse datosTomador) {


        // Actualizar datos de búsqueda en PrincipalActivity para evitar NPE en el siguiente fragmento
        DatosBusquedaAseguradoTomador dbat = new DatosBusquedaAseguradoTomador();
        dbat.NumeroDocumentoIdentidad = datosTomador.PerDocumentoIdentidadNumero;
        dbat.TipoDocumentoIdentidad = datosTomador.PerTParCliDocumentoIdentidadTipoFk;
        dbat.TipoDocumentoIdentidadDescripcion = datosTomador.PerTParCliDocumentoIdentidadTipoDescripcion;
        dbat.Complemento = datosTomador.PerDocumentoIdentidadExtension;
        dbat.DepartamentoDocumentoIdentidad = datosTomador.PerTParGenDepartamentoFkDocumentoIdentidad;
        dbat.DepartamentoDocumentoIdentidadDescripcion = datosTomador.PerTParGenDepartamentoDescripcionDocumentoIdentidad;
        ((PrincipalActivity) getActivity()).datosBusquedaAseguradoTomador = dbat;
        ((PrincipalActivity) getActivity()).datosBeneficiario = null;

        // Navegar al siguiente paso con los datos del tomador
        ((PrincipalActivity)getActivity()).datosTomador = datosTomador;
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedor_vistas, new SoatcTomadorDatosFragment())
                .addToBackStack(null)
                .commit();
    }
    private void buscarTomador()
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
                                        procesarSeleccionTomador(cliente);
                                    } else if (apiResponse.datos != null && apiResponse.datos.size() > 1) {
                                        tomadorAdapter.updateData(apiResponse.datos);
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
                                                        procesarSeleccionTomador(cliente);
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
                                Log.e("Tomador",e.getMessage());
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