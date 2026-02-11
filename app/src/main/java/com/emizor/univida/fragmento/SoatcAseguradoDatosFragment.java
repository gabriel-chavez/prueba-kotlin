package com.emizor.univida.fragmento;

import android.app.DatePickerDialog;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.emizor.univida.R;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.modelo.dominio.univida.ApiResponse;
import com.emizor.univida.modelo.dominio.univida.parametricas.ParametricaGenerica;
import com.emizor.univida.modelo.dominio.univida.soatc.Beneficiario;
import com.emizor.univida.modelo.dominio.univida.soatc.CliObtenerDatosResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.DatosBusquedaAseguradoTomador;
import com.emizor.univida.modelo.dominio.univida.soatc.DatosFacturacion;
import com.emizor.univida.modelo.dominio.univida.soatc.RequestBuilderEfectivizar;
import com.emizor.univida.rest.ApiService;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.utils.ParametricasCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoatcAseguradoDatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoatcAseguradoDatosFragment extends Fragment {

    private Button btnAtras, btnSiguiente;
    // EditTexts
    private EditText etApellidoPaterno, etApellidoMaterno, etApellidoCasada;
    private EditText etPrimerNombre, etSegundoNombre, etFechaNacimiento;
    private EditText etNumeroDocumento, etExtensionDocumento, etCelular;
    private EditText etEmail, etDireccion, etTipoDocumento, etDeptoDocumento;

    // Spinners
    private Spinner spinnerDeptoResidencia, spinnerDeptoContratacion;
    private Spinner spinnerSexo, spinnerEstadoCivil, spinnerNacionalidad;
    private CliObtenerDatosResponse datosAsegurado;
    private String fechaNacimientoFormato;

    View view;
    public SoatcAseguradoDatosFragment() {
        // Required empty public constructor
    }

    public static SoatcAseguradoDatosFragment newInstance(String param1, String param2) {
        SoatcAseguradoDatosFragment fragment = new SoatcAseguradoDatosFragment();
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
        view = inflater.inflate(R.layout.fragment_soatc_asegurado_datos, container, false);
        btnAtras = view.findViewById(R.id.btnAtras);
        btnSiguiente = view.findViewById(R.id.btnSiguiente);
        // Inicializar EditTexts
        etApellidoPaterno = view.findViewById(R.id.etApellidoPaterno);
        etApellidoMaterno = view.findViewById(R.id.etApellidoMaterno);
        etApellidoCasada = view.findViewById(R.id.etApellidoCasada);
        etPrimerNombre = view.findViewById(R.id.etPrimerNombre);
        etSegundoNombre = view.findViewById(R.id.etSegundoNombre);
        etFechaNacimiento = view.findViewById(R.id.etFechaNacimiento);
        etNumeroDocumento = view.findViewById(R.id.etNumeroDocumento);
        etExtensionDocumento = view.findViewById(R.id.etExtensionDocumento);
        etCelular = view.findViewById(R.id.etCelular);
        etEmail = view.findViewById(R.id.etEmail);
        etDireccion = view.findViewById(R.id.etDireccion);
        etTipoDocumento = view.findViewById(R.id.etTipoDocumento);
        etDeptoDocumento = view.findViewById(R.id.etDeptoDocumento);
        // Inicializar Spinners
        spinnerDeptoResidencia = view.findViewById(R.id.spinnerDeptoResidencia);
        spinnerDeptoContratacion = view.findViewById(R.id.spinnerDeptoContratacion);
        spinnerSexo = view.findViewById(R.id.spinnerSexo);
        spinnerEstadoCivil = view.findViewById(R.id.spinnerEstadoCivil);
        spinnerNacionalidad = view.findViewById(R.id.spinnerNacionalidad);

        etFechaNacimiento.setOnClickListener(v -> {

            final Calendar calendar = Calendar.getInstance();

            String fechaNacimiento = etFechaNacimiento.getText().toString();
            if (!fechaNacimiento.isEmpty()) {
                String[] partes = fechaNacimiento.split("/");
                int day = Integer.parseInt(partes[0]);
                int month = Integer.parseInt(partes[1]) - 1;
                int year = Integer.parseInt(partes[2]);
                calendar.set(year, month, day);
            }

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (datePickerView, selectedYear, selectedMonth, selectedDay) -> {
                        String fecha = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        etFechaNacimiento.setText(fecha);

                        fechaNacimientoFormato=String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    }, year, month, day);

            datePickerDialog.show();
        });
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFormulario()) {
                    datosAsegurado = obtenerDatosAseguradoDesdeVista();
                    ((PrincipalActivity)getActivity()).datosAsegurado = datosAsegurado;
                    validarVendible();

                }
            }
        });
        btnAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        obtenerDepartamentos();
        obtenerEstadoCivil();
        obtenerGenero();
        obtenerNacionalidad();

        //para mostrar los datos
        if (getActivity() instanceof PrincipalActivity) {
            PrincipalActivity principal = (PrincipalActivity) getActivity();

            if (principal.datosAsegurado != null) {
                this.datosAsegurado = principal.datosAsegurado;
                cargarDatosAsegurado();
            }else{
                buscarAsegurado();
            }

        }
        return view;
    }
    private void buscarAsegurado() {

        DatosBusquedaAseguradoTomador datosBusquedaAsegurado = ((PrincipalActivity) getActivity()).datosBusquedaAsegurado;

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("per_t_par_cli_documento_identidad_tipo_fk", datosBusquedaAsegurado.TipoDocumentoIdentidad);
        parametros.put("per_documento_identidad_numero", datosBusquedaAsegurado.NumeroDocumentoIdentidad);
        parametros.put("per_documento_identidad_extension", datosBusquedaAsegurado.Complemento);
        parametros.put("per_t_par_gen_departamento_fk_documento_identidad", datosBusquedaAsegurado.DepartamentoDocumentoIdentidad);
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

                            CliObtenerDatosResponse cliObtenerDatos= new CliObtenerDatosResponse();
                            cliObtenerDatos.PerTParCliDocumentoIdentidadTipoFk=datosBusquedaAsegurado.TipoDocumentoIdentidad;
                            cliObtenerDatos.PerTParCliDocumentoIdentidadTipoDescripcion=datosBusquedaAsegurado.TipoDocumentoIdentidadDescripcion;
                            cliObtenerDatos.PerDocumentoIdentidadNumero=datosBusquedaAsegurado.NumeroDocumentoIdentidad;
                            cliObtenerDatos.PerTParGenDepartamentoDescripcionDocumentoIdentidad=datosBusquedaAsegurado.DepartamentoDocumentoIdentidadDescripcion;
                            cliObtenerDatos.PerTParGenDepartamentoFkDocumentoIdentidad=datosBusquedaAsegurado.DepartamentoDocumentoIdentidad;
                            cliObtenerDatos.PerDocumentoIdentidadExtension=datosBusquedaAsegurado.Complemento;

                            datosAsegurado=cliObtenerDatos;
//                            new AlertDialog.Builder(requireContext())
//                                    .setTitle("Atención")
//                                    .setMessage(apiResponse.mensaje)
//                                    .setPositiveButton("Continuar", (dialog, which) -> {
//                                    }).show();
                            cargarDatosAsegurado();
                        }else{
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Atención")
                                    .setMessage(apiResponse.mensaje)
                                    .setPositiveButton("Aceptar", (dialog, which) -> {
                                    }).show();
                        }
                    } else {
                        datosAsegurado=apiResponse.datos;
                        cargarDatosAsegurado();
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
    private void cargarDatosAsegurado(){


        TextView tvExtensionDocumento = view.findViewById(R.id.tvExtensionDocumento);
        LinearLayout llExtensionDocumento = view.findViewById(R.id.llExtensionDocumento);

        if (datosAsegurado.PerTParCliDocumentoIdentidadTipoFk == 7 ||
                datosAsegurado.PerTParCliDocumentoIdentidadTipoFk == 4 ||
                datosAsegurado.PerTParCliDocumentoIdentidadTipoFk == 2) {
            tvExtensionDocumento.setVisibility(View.VISIBLE);
            llExtensionDocumento.setVisibility(View.VISIBLE);
            etExtensionDocumento.setEnabled(true);
            etExtensionDocumento.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            tvExtensionDocumento.setVisibility(View.GONE);
            llExtensionDocumento.setVisibility(View.GONE);
        }

        // EditTexts
        etApellidoPaterno.setText(datosAsegurado.PerApellidoPaterno);
        etApellidoMaterno.setText(datosAsegurado.PerApellidoMaterno);
        etApellidoCasada.setText(datosAsegurado.PerApellidoCasada);
        etPrimerNombre.setText(datosAsegurado.PerNombrePrimero);
        etSegundoNombre.setText(datosAsegurado.PerNombreSegundo);
        etFechaNacimiento.setText(datosAsegurado.PerNacimientoFechaFormato);
        etNumeroDocumento.setText(datosAsegurado.PerDocumentoIdentidadNumero != null ?
                String.valueOf(datosAsegurado.PerDocumentoIdentidadNumero) : "");
        etExtensionDocumento.setText(datosAsegurado.PerDocumentoIdentidadExtension);
        etCelular.setText(datosAsegurado.PerTelefonoMovil);
        etEmail.setText(datosAsegurado.PerCorreoElectronico);
        etDireccion.setText(datosAsegurado.PerDomicilioParticular);
        etTipoDocumento.setText(datosAsegurado.PerTParCliDocumentoIdentidadTipoDescripcion);
        etDeptoDocumento.setText(datosAsegurado.PerTParGenDepartamentoDescripcionDocumentoIdentidad);

        // Spinners
        setSpinnerValue(spinnerDeptoResidencia, datosAsegurado.PerTParGenDepartamentoDescripcionNacimiento);
        setSpinnerValue(spinnerDeptoContratacion, datosAsegurado.PerTParGenDepartamentoDescripcionNacimiento);
        setSpinnerValue(spinnerSexo, datosAsegurado.PerTParCliGeneroDescripcion);
        setSpinnerValue(spinnerEstadoCivil, datosAsegurado.PerTParCliEstadoCivilDescripcion);
        setSpinnerValue(spinnerNacionalidad, datosAsegurado.PerTParGenPaisDescripcionResidencia);
    }
    private void setSpinnerValue(Spinner spinner, String value) {
        if (spinner.getAdapter() == null || value == null) return;
        for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
            if (value.equals(spinner.getAdapter().getItem(i).toString())) {
                spinner.setSelection(i);
                break;
            }
        }
    }
    private void obtenerDepartamentos() {
        List<ParametricaGenerica> departamentos = ParametricasCache.getInstance().getDepartamentos();

        if (departamentos != null && !departamentos.isEmpty()) {
            ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_spinner_item, departamentos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDeptoResidencia.setAdapter(adapter);
            spinnerDeptoContratacion.setAdapter(adapter);
        }
    }

    private void obtenerEstadoCivil() {
        List<ParametricaGenerica> estadoCivil = ParametricasCache.getInstance().getEstadoCivil();

        if (estadoCivil != null && !estadoCivil.isEmpty()) {
            ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_spinner_item, estadoCivil);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEstadoCivil.setAdapter(adapter);
        }
    }

    private void obtenerGenero() {
        List<ParametricaGenerica> genero = ParametricasCache.getInstance().getGenero();

        if (genero != null && !genero.isEmpty()) {
            ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_spinner_item, genero);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSexo.setAdapter(adapter);
        }
    }

    private void obtenerNacionalidad() {
        List<ParametricaGenerica> nacionalidad = ParametricasCache.getInstance().getNacionalidad();

        if (nacionalidad != null && !nacionalidad.isEmpty()) {
            ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_spinner_item, nacionalidad);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerNacionalidad.setAdapter(adapter);
        }
    }

    private boolean validarFormulario() {
        boolean valido = true;

        if (etPrimerNombre.getText().toString().isEmpty()) {
            etPrimerNombre.setError("Este campo es obligatorio");
            valido = false;
        }

        String fecha = etFechaNacimiento.getText().toString();
        if (fecha.isEmpty()) {
            etFechaNacimiento.setError("Ingrese fecha de nacimiento");
            valido = false;
        } else if (!fecha.matches("\\d{2}/\\d{2}/\\d{4}")) {
            etFechaNacimiento.setError("Formato inválido DD/MM/AAAA");
            valido = false;
        }

        String celular = etCelular.getText().toString();
        if (!celular.isEmpty() && !celular.matches("\\d{8}")) {
            etCelular.setError("Número de celular inválido");
            valido = false;
        }

        String email = etEmail.getText().toString();
        if (!email.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Correo electrónico inválido");
            valido = false;
        }

        String direccion = etDireccion.getText().toString();
        if (direccion.length() > 100) {
            etDireccion.setError("Dirección demasiado larga");
            valido = false;
        }

        return valido;
    }
    private CliObtenerDatosResponse obtenerDatosAseguradoDesdeVista() {
        CliObtenerDatosResponse datos = new CliObtenerDatosResponse();
        DatosBusquedaAseguradoTomador datosBusquedaAsegurado = ((PrincipalActivity) getActivity()).datosBusquedaAsegurado;

        // De busqueda
        datos.PerTParCliDocumentoIdentidadTipoFk = datosBusquedaAsegurado.TipoDocumentoIdentidad;
        datos.PerTParCliDocumentoIdentidadTipoDescripcion = datosBusquedaAsegurado.TipoDocumentoIdentidadDescripcion;
        datos.PerDocumentoIdentidadNumero = datosBusquedaAsegurado.NumeroDocumentoIdentidad;
        datos.PerTParGenDepartamentoDescripcionDocumentoIdentidad = datosBusquedaAsegurado.DepartamentoDocumentoIdentidadDescripcion;
        datos.PerTParGenDepartamentoFkDocumentoIdentidad = datosBusquedaAsegurado.DepartamentoDocumentoIdentidad;
        datos.PerDocumentoIdentidadExtension = datosBusquedaAsegurado.Complemento;

        // EditTexts
        datos.PerApellidoPaterno = getStringOrNull(etApellidoPaterno);
        datos.PerApellidoMaterno = getStringOrNull(etApellidoMaterno);
        datos.PerApellidoCasada = getStringOrNull(etApellidoCasada);
        datos.PerNombrePrimero = getStringOrNull(etPrimerNombre);
        datos.PerNombreSegundo = getStringOrNull(etSegundoNombre);
        datos.PerNacimientoFecha = fechaNacimientoFormato;
        datos.PerDocumentoIdentidadNumero = parseInteger(etNumeroDocumento.getText().toString());
        datos.PerDocumentoIdentidadExtension = getStringOrNull(etExtensionDocumento);
        datos.PerTelefonoMovil = getStringOrNull(etCelular);
        datos.PerCorreoElectronico = getStringOrNull(etEmail);
        datos.PerDomicilioParticular = getStringOrNull(etDireccion);
        datos.PerTParCliDocumentoIdentidadTipoDescripcion = getStringOrNull(etTipoDocumento);

        // Spinners
        datos.PerTParGenDepartamentoFkNacimiento = getSpinnerId(spinnerDeptoResidencia);
        datos.PerTParGenDepartamentoFkDocumentoIdentidad = getSpinnerId(spinnerDeptoContratacion);
        datos.PerTParCliGeneroFk = getSpinnerId(spinnerSexo);
        datos.PerTParCliEstadoCivilFk = getSpinnerId(spinnerEstadoCivil);
        datos.PerTParGenPaisFkNacionalidad = getSpinnerId(spinnerNacionalidad);

        return datos;
    }
    private String getStringOrNull(EditText editText) {
        return editText.getText().toString().isEmpty() ? null : editText.getText().toString();
    }

    private Integer parseInteger(String valor) {
        try {
            return valor != null && !valor.isEmpty() ? Integer.parseInt(valor) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer getSpinnerId(Spinner spinner) {
        ParametricaGenerica parametrica = (ParametricaGenerica) spinner.getSelectedItem();
        if (spinner.getSelectedItem() instanceof ParametricaGenerica) {
            return parametrica.Identificador;
        }
        return null;
    }
    private void validarVendible() {
        CliObtenerDatosResponse datosAsegurado = ((PrincipalActivity) getActivity()).datosAsegurado;
        CliObtenerDatosResponse datosTomador = ((PrincipalActivity) getActivity()).datosTomador;
        List<Beneficiario> datosBeneficiario = ((PrincipalActivity) getActivity()).datosBeneficiario;
        DatosFacturacion datosFacturacion = ((PrincipalActivity) getActivity()).datosFacturacion;

        Map<String, Object> request = RequestBuilderEfectivizar.construirRequest(
                datosFacturacion,
                datosTomador,
                datosAsegurado,
                datosBeneficiario
        );

        ApiService apiService = new ApiService(getContext());
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_EMISION_VALIDAR_VENDIBLE;
        ((PrincipalActivity) requireActivity()).mostrarLoading(true);
        apiService.solicitudPost(url, request, response -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
            Type responseType = new TypeToken<ApiResponse<Object>>() {}.getType();
            ApiResponse<Object> apiResponse = new Gson().fromJson(response, responseType);
            if (apiResponse.exito) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.contenedor_vistas, new SoatcBeneficiariosFragment())
                        .addToBackStack(null)
                        .commit();
            } else {
                mostrarDialogo(apiResponse.mensaje);
            }
        }, error -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
            // Manejo de error de conexión
        });
    }
    private void mostrarDialogo(String mensaje) {
        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Validación")
                .setMessage(mensaje)
                .setPositiveButton("OK", null)
                .show();
    }
}