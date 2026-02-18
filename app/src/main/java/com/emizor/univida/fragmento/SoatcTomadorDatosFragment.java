package com.emizor.univida.fragmento;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
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
 * Use the {@link SoatcTomadorDatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoatcTomadorDatosFragment extends Fragment {
    private Button btnAtras, btnSiguiente;
    private EditText etApellidoPaterno, etApellidoMaterno, etApellidoCasada;
    private EditText etPrimerNombre, etSegundoNombre, etFechaNacimiento,etNumeroDocumento;
    private EditText etExtensionDocumento, etCelular;
    private EditText etEmail, etDireccion;
    private Spinner spinnerTipoDocumento,spinnerDeptoDocumento;
    private Spinner spinnerSexo, spinnerEstadoCivil, spinnerNacionalidad;
    private CliObtenerDatosResponse datosTomador;
    private String fechaNacimientoFormato;
    View view;

    public SoatcTomadorDatosFragment() {
        // Required empty public constructor
    }


    public static SoatcTomadorDatosFragment newInstance(String param1, String param2) {
        SoatcTomadorDatosFragment fragment = new SoatcTomadorDatosFragment();
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

        view = inflater.inflate(R.layout.fragment_soatc_tomador_datos, container, false);
        inicializarVistas();
        configurarListeners();
        cargarDatosParametricos();
        cargarDatosIniciales();
        return view;
    }
    private void inicializarVistas() {
        btnAtras = view.findViewById(R.id.btnAtras);
        btnSiguiente = view.findViewById(R.id.btnSiguiente);
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
        spinnerTipoDocumento = view.findViewById(R.id.spinnerTipoDocumento);
        spinnerDeptoDocumento = view.findViewById(R.id.spinnerDeptoDocumento);
        //spinnerDeptoResidencia = view.findViewById(R.id.spinnerDeptoResidencia);

        spinnerSexo = view.findViewById(R.id.spinnerSexo);
        spinnerEstadoCivil = view.findViewById(R.id.spinnerEstadoCivil);
        spinnerNacionalidad = view.findViewById(R.id.spinnerNacionalidad);
    }
    private void configurarListeners() {
        etFechaNacimiento.setOnClickListener(v -> mostrarSelectorFecha());
        btnSiguiente.setOnClickListener(v -> {
            if (validarFormulario()) {
                datosTomador = obtenerDatosTomadorDesdeVista();
                ((PrincipalActivity) getActivity()).datosTomador = datosTomador;
                validarVendible();
            }
        });
        btnAtras.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }
    private void cargarDatosIniciales() {
        if (getActivity() instanceof PrincipalActivity) {
            PrincipalActivity principal = (PrincipalActivity) getActivity();
            if (principal.datosTomador != null) {
                this.datosTomador = principal.datosTomador;
                cargarDatosTomadorEnVista();
            } else {
                buscarTomador();
            }
        }
    }
    private void buscarTomador() {
        DatosBusquedaAseguradoTomador datosBusqueda = ((PrincipalActivity) getActivity()).datosBusquedaAseguradoTomador;
        if (datosBusqueda == null) return;

        Map<String, Object> params = new HashMap<>();
        params.put("per_t_par_cli_documento_identidad_tipo_fk", datosBusqueda.TipoDocumentoIdentidad);
        params.put("per_documento_identidad_numero", datosBusqueda.NumeroDocumentoIdentidad);
        params.put("per_documento_identidad_extension", datosBusqueda.Complemento);
        params.put("per_t_par_gen_departamento_fk_documento_identidad", datosBusqueda.DepartamentoDocumentoIdentidad);

        ((PrincipalActivity) requireActivity()).mostrarLoading(true);
        new ApiService(getContext()).solicitudPost(DatosConexion.URL_UNIVIDA_CLIENTES_OBTENER_DATOS, params,
                response -> {
                    ((PrincipalActivity) requireActivity()).mostrarLoading(false);
                    try {
                        ApiResponse<CliObtenerDatosResponse> apiResponse = parsearRespuesta(response);
                        if (apiResponse.exito) {
                            datosTomador = apiResponse.datos;
                            datosTomador.EsNuevo = false;
                            cargarDatosTomadorEnVista();
                        } else if (apiResponse.codigo_retorno == 0) {
                            datosTomador = new CliObtenerDatosResponse();
                            datosTomador.EsNuevo = true;
                            cargarDatosDesdeBusqueda(datosBusqueda);
                            cargarDatosTomadorEnVista();
                        } else {
                            mostrarDialogo("Atención", apiResponse.mensaje);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    ((PrincipalActivity) requireActivity()).mostrarLoading(false);
                    Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                });
    }
    private void cargarDatosDesdeBusqueda(DatosBusquedaAseguradoTomador datos) {
        datosTomador.PerTParCliDocumentoIdentidadTipoFk = datos.TipoDocumentoIdentidad;
        datosTomador.PerTParCliDocumentoIdentidadTipoDescripcion = datos.TipoDocumentoIdentidadDescripcion;
        datosTomador.PerDocumentoIdentidadNumero = datos.NumeroDocumentoIdentidad;
        datosTomador.PerTParGenDepartamentoDescripcionDocumentoIdentidad = datos.DepartamentoDocumentoIdentidadDescripcion;
        datosTomador.PerTParGenDepartamentoFkDocumentoIdentidad = datos.DepartamentoDocumentoIdentidad;
        datosTomador.PerDocumentoIdentidadExtension = datos.Complemento;
    }

    private ApiResponse<CliObtenerDatosResponse> parsearRespuesta(String response) {
        Type responseType = new TypeToken<ApiResponse<CliObtenerDatosResponse>>() {}.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).parse(json.getAsString());
                        }
                    } catch (ParseException e) {
                        return null;
                    }
                    return null;
                }).create();
        return gson.fromJson(response, responseType);
    }

    private void cargarDatosTomadorEnVista() {
        if (datosTomador == null) return;

        LinearLayout llDocumentoExistente = view.findViewById(R.id.llDocumentoExistente);
        LinearLayout llDocumentoNuevo = view.findViewById(R.id.llDocumentoNuevo);

        if (datosTomador.EsNuevo) {
            llDocumentoExistente.setVisibility(View.GONE);
            llDocumentoNuevo.setVisibility(View.VISIBLE);
            TextView tvNumeroDocumentoGrande = view.findViewById(R.id.tvNumeroDocumentoGrande);
            tvNumeroDocumentoGrande.setText(String.valueOf(datosTomador.PerDocumentoIdentidadNumero));
        } else {
            llDocumentoExistente.setVisibility(View.VISIBLE);
            llDocumentoNuevo.setVisibility(View.GONE);
            TextView tvDocumentoResumen = view.findViewById(R.id.tvDocumentoResumen);
            TextView tvDeptoResumen = view.findViewById(R.id.tvDeptoResumen);
            String docInfo = datosTomador.PerTParCliDocumentoIdentidadTipoAbreviacion + " - " + datosTomador.PerDocumentoIdentidadNumero;
            tvDocumentoResumen.setText(docInfo);
            tvDeptoResumen.setText(datosTomador.PerTParGenDepartamentoDescripcionDocumentoIdentidad);
        }

        // Setear valores en los campos
        etApellidoPaterno.setText(datosTomador.PerApellidoPaterno);
        etApellidoMaterno.setText(datosTomador.PerApellidoMaterno);
        etApellidoCasada.setText(datosTomador.PerApellidoCasada);
        etPrimerNombre.setText(datosTomador.PerNombrePrimero);
        etSegundoNombre.setText(datosTomador.PerNombreSegundo);
        etFechaNacimiento.setText(datosTomador.PerNacimientoFechaFormato);
        etNumeroDocumento.setText(String.valueOf(datosTomador.PerDocumentoIdentidadNumero));
        etExtensionDocumento.setText(datosTomador.PerDocumentoIdentidadExtension);
        etCelular.setText(datosTomador.PerTelefonoMovil);
        etEmail.setText(datosTomador.PerCorreoElectronico);
        etDireccion.setText(datosTomador.PerDomicilioParticular);

        // Pre-cargar fecha en formato API si ya existe
        if (datosTomador.PerNacimientoFecha != null && !datosTomador.PerNacimientoFecha.isEmpty()) {
            fechaNacimientoFormato = datosTomador.PerNacimientoFecha;
        }

        setSpinnerValue(spinnerTipoDocumento, datosTomador.PerTParCliDocumentoIdentidadTipoDescripcion);
        setSpinnerValue(spinnerDeptoDocumento, datosTomador.PerTParGenDepartamentoDescripcionDocumentoIdentidad);
       // setSpinnerValue(spinnerDeptoResidencia, datosTomador.PerTParGenDepartamentoDescripcionNacimiento);

        setSpinnerValue(spinnerSexo, datosTomador.PerTParCliGeneroDescripcion);
        setSpinnerValue(spinnerEstadoCivil, datosTomador.PerTParCliEstadoCivilDescripcion);
        setSpinnerValue(spinnerNacionalidad, datosTomador.PerTParGenPaisDescripcionResidencia);
    }

    private void mostrarSelectorFecha() {
        final Calendar calendar = Calendar.getInstance();
        if (!etFechaNacimiento.getText().toString().isEmpty()) {
            try {
                Date date = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    date = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(etFechaNacimiento.getText().toString());
                }
                calendar.setTime(date);
            } catch (ParseException e) { /* Ignorar error */ }
        }
        new DatePickerDialog(getContext(), (view, year, month, day) -> {
            fechaNacimientoFormato = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day);
            etFechaNacimiento.setText(String.format(Locale.US, "%02d/%02d/%04d", day, month + 1, year));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void cargarDatosParametricos() {
        obtenerDepartamentos();
        obtenerEstadoCivil();
        obtenerGenero();
        obtenerNacionalidad();
        obtenerDocumentosIdentidad();
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
    private void obtenerDocumentosIdentidad() {
        List<ParametricaGenerica> documentosIdentidad = ParametricasCache.getInstance().getDocumentosIdentidad();

        if (documentosIdentidad != null && !documentosIdentidad.isEmpty()) {
            ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_spinner_item, documentosIdentidad);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTipoDocumento.setAdapter(adapter);

        }
    }
    private void obtenerDepartamentos() {
        List<ParametricaGenerica> data = ParametricasCache.getInstance().getDepartamentos();
        if (data != null) {
            ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //spinnerDeptoResidencia.setAdapter(adapter);

            spinnerDeptoDocumento.setAdapter(adapter);
        }
    }

    private void obtenerEstadoCivil() {
        List<ParametricaGenerica> data = ParametricasCache.getInstance().getEstadoCivil();
        if (data != null) {
            ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEstadoCivil.setAdapter(adapter);
        }
    }

    private void obtenerGenero() {
        List<ParametricaGenerica> data = ParametricasCache.getInstance().getGenero();
        if (data != null) {
            ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSexo.setAdapter(adapter);
        }
    }

    private void obtenerNacionalidad() {
        List<ParametricaGenerica> data = ParametricasCache.getInstance().getNacionalidad();
        if (data != null) {
            ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
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
        if (celular.isEmpty()) {
            etCelular.setError("Este campo es obligatorio");
            valido = false;
        } else if (!celular.matches("\\d{8}")) {
            etCelular.setError("Número de celular inválido");
            valido = false;
        }

        String email = etEmail.getText().toString();
        if (!email.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Correo electrónico inválido");
            valido = false;
        }

        String direccion = etDireccion.getText().toString();
        if (direccion.isEmpty()) {
            etDireccion.setError("Este campo es obligatorio");
            valido = false;
        } else if (direccion.length() > 100) {
            etDireccion.setError("Dirección demasiado larga");
            valido = false;
        }



        return valido;
    }

    private CliObtenerDatosResponse obtenerDatosTomadorDesdeVista() {
        CliObtenerDatosResponse datos = new CliObtenerDatosResponse();
        datos.EsNuevo=datosTomador.EsNuevo;
        datos.PerDocumentoIdentidadNumero = parseInteger(etNumeroDocumento.getText().toString());
        datos.PerDocumentoIdentidadExtension = getStringOrNull(etExtensionDocumento);
        datos.PerApellidoPaterno = getStringOrNull(etApellidoPaterno);
        datos.PerApellidoMaterno = getStringOrNull(etApellidoMaterno);
        datos.PerApellidoCasada = getStringOrNull(etApellidoCasada);
        datos.PerNombrePrimero = getStringOrNull(etPrimerNombre);
        datos.PerNombreSegundo = getStringOrNull(etSegundoNombre);
        
        // Si no se usó el selector de fecha, intentar parsear lo que haya en el EditText
        if (fechaNacimientoFormato == null || fechaNacimientoFormato.isEmpty()) {
            String fechaUI = etFechaNacimiento.getText().toString();
            if (fechaUI.matches("\\d{2}/\\d{2}/\\d{4}")) {
                String[] parts = fechaUI.split("/");
                fechaNacimientoFormato = parts[2] + "-" + parts[1] + "-" + parts[0];
            }
        }
        datos.PerNacimientoFecha = fechaNacimientoFormato;
        
        datos.PerTelefonoMovil = getStringOrNull(etCelular);
        datos.PerCorreoElectronico = getStringOrNull(etEmail);
        datos.PerDomicilioParticular = getStringOrNull(etDireccion);
        datos.PerTParCliDocumentoIdentidadTipoFk = getSpinnerId(spinnerTipoDocumento);
        datos.PerTParGenDepartamentoFkDocumentoIdentidad = getSpinnerId(spinnerDeptoDocumento);
        datos.PerTParCliGeneroFk = getSpinnerId(spinnerSexo);
        datos.PerTParCliEstadoCivilFk = getSpinnerId(spinnerEstadoCivil);
        datos.PerTParGenPaisFkNacionalidad = getSpinnerId(spinnerNacionalidad);
       // datos.PerTParGenDepartamentoFkVenta=getSpinnerId(spinnerDeptoResidencia);
        return datos;
    }
    private Integer getSpinnerId(Spinner spinner) {
        if (spinner != null && spinner.getSelectedItem() instanceof ParametricaGenerica) {
            return ((ParametricaGenerica) spinner.getSelectedItem()).Identificador;
        }
        return null;
    }
    private Integer parseInteger(String valor) {
        try {
            return valor != null && !valor.isEmpty() ? Integer.parseInt(valor) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    private String getStringOrNull(EditText editText) {
        return editText.getText().toString().isEmpty() ? null : editText.getText().toString();
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
            try {
                Type responseType = new TypeToken<ApiResponse<Object>>() {}.getType();
                ApiResponse<Object> apiResponse = new Gson().fromJson(response, responseType);
                if (apiResponse.exito) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.contenedor_vistas, new SoatcBeneficiariosFragment())
                            .addToBackStack(null)
                            .commit();
                } else {
                    mostrarDialogo("Validación", apiResponse.mensaje);
                }
            } catch (Exception e) {
                mostrarDialogo("Error", "Error al validar vendible");
            }
        }, error -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
            mostrarDialogo("Error", "Error de conexión al validar vendible");
        });
    }

    private void mostrarDialogo(String titulo, String mensaje) {
        new AlertDialog.Builder(getContext())
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("OK", null)
                .show();
    }
}
