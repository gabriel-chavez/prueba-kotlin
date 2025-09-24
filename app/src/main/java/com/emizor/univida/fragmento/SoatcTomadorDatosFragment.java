package com.emizor.univida.fragmento;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoatcTomadorDatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoatcTomadorDatosFragment extends Fragment {
    private Button btnAtras, btnSiguiente;
    // EditTexts
    private EditText etApellidoPaterno, etApellidoMaterno, etApellidoCasada;
    private EditText etPrimerNombre, etSegundoNombre, etFechaNacimiento;
    private EditText etNumeroDocumento, etExtensionDocumento, etCelular;
    private EditText etEmail, etDireccion, etTipoDocumento, etDeptoDocumento;

    // Spinners
    private Spinner spinnerDeptoResidencia, spinnerDeptoContratacion;
    private Spinner spinnerSexo, spinnerEstadoCivil, spinnerNacionalidad;
    private CliObtenerDatosResponse datosTomador;


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

        View view = inflater.inflate(R.layout.fragment_soatc_tomador_datos, container, false);
        datosTomador = ((PrincipalActivity) getActivity()).datosTomador;

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

        // Mostrar o ocultar extensión/complemento según tipo de documento
        TextView tvExtensionDocumento = view.findViewById(R.id.tvExtensionDocumento);
        LinearLayout llExtensionDocumento = view.findViewById(R.id.llExtensionDocumento);

        if (datosTomador.PerTParCliDocumentoIdentidadTipoFk == 7 ||
                datosTomador.PerTParCliDocumentoIdentidadTipoFk == 4 ||
                datosTomador.PerTParCliDocumentoIdentidadTipoFk == 2) {
            tvExtensionDocumento.setVisibility(View.VISIBLE);
            llExtensionDocumento.setVisibility(View.VISIBLE);
            etExtensionDocumento.setEnabled(true);
            etExtensionDocumento.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            tvExtensionDocumento.setVisibility(View.GONE);
            llExtensionDocumento.setVisibility(View.GONE);
        }
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
                    }, year, month, day);
            datePickerDialog.show();
        });
        obtenerDepartamentos();
        obtenerEstadoCivil();
        obtenerGenero();
        obtenerNacionalidad();
        if (datosTomador != null) {
            cargarDatosTomador();
        }
        btnAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        btnSiguiente.setOnClickListener(v -> {
            if (validarFormulario()) {
                validarVendible();
            }
        });


        return view;
    }

    private void cargarDatosTomador() {
        // EditTexts
        etApellidoPaterno.setText(datosTomador.PerApellidoPaterno);
        etApellidoMaterno.setText(datosTomador.PerApellidoMaterno);
        etApellidoCasada.setText(datosTomador.PerApellidoCasada);
        etPrimerNombre.setText(datosTomador.PerNombrePrimero);
        etSegundoNombre.setText(datosTomador.PerNombreSegundo);
        etFechaNacimiento.setText(datosTomador.PerNacimientoFechaFormato);
        etNumeroDocumento.setText(datosTomador.PerDocumentoIdentidadNumero != null ?
                String.valueOf(datosTomador.PerDocumentoIdentidadNumero) : "");
        etExtensionDocumento.setText(datosTomador.PerDocumentoIdentidadExtension);
        etCelular.setText(datosTomador.PerTelefonoMovil);
        etEmail.setText(datosTomador.PerCorreoElectronico);
        etDireccion.setText(datosTomador.PerDomicilioParticular);
        etTipoDocumento.setText(datosTomador.PerTParCliDocumentoIdentidadTipoDescripcion);
        etDeptoDocumento.setText(datosTomador.PerTParGenDepartamentoDescripcionDocumentoIdentidad);

        // Spinners
        setSpinnerValue(spinnerDeptoResidencia, datosTomador.PerTParGenDepartamentoDescripcionNacimiento);
        setSpinnerValue(spinnerDeptoContratacion, datosTomador.PerTParGenDepartamentoDescripcionNacimiento);
        setSpinnerValue(spinnerSexo, datosTomador.PerTParCliGeneroDescripcion);
        setSpinnerValue(spinnerEstadoCivil, datosTomador.PerTParCliEstadoCivilDescripcion);
        setSpinnerValue(spinnerNacionalidad, datosTomador.PerTParGenPaisDescripcionResidencia);
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
    private CliObtenerDatosResponse obtenerDatosTomadorDesdeVista() {
        CliObtenerDatosResponse datos = new CliObtenerDatosResponse();

        // EditTexts
        datos.PerApellidoPaterno = etApellidoPaterno.getText().toString();
        datos.PerApellidoMaterno = etApellidoMaterno.getText().toString();
        datos.PerApellidoCasada = etApellidoCasada.getText().toString();
        datos.PerNombrePrimero = etPrimerNombre.getText().toString();
        datos.PerNombreSegundo = etSegundoNombre.getText().toString();
        datos.PerNacimientoFecha = etFechaNacimiento.getText().toString();
        datos.PerDocumentoIdentidadNumero = parseInteger(etNumeroDocumento.getText().toString());
        datos.PerDocumentoIdentidadExtension = etExtensionDocumento.getText().toString();
        datos.PerTelefonoMovil = etCelular.getText().toString();
        datos.PerCorreoElectronico = etEmail.getText().toString();
        datos.PerDomicilioParticular = etDireccion.getText().toString();
        datos.PerTParCliDocumentoIdentidadTipoDescripcion = etTipoDocumento.getText().toString();
        datos.PerTParGenDepartamentoDescripcionDocumentoIdentidad = etDeptoDocumento.getText().toString();

        // Spinners
        datos.PerTParGenDepartamentoFkNacimiento = getSpinnerId(spinnerDeptoResidencia);
        datos.PerTParGenDepartamentoFkDocumentoIdentidad = getSpinnerId(spinnerDeptoContratacion);
        datos.PerTParCliGeneroFk = getSpinnerId(spinnerSexo);
        datos.PerTParCliEstadoCivilFk = getSpinnerId(spinnerEstadoCivil);
        datos.PerTParGenPaisFkNacionalidad = getSpinnerId(spinnerNacionalidad);

        return datos;
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
        datosTomador = obtenerDatosTomadorDesdeVista();
        /*solo para validar datos del tomador se envia en asegurado lo mismo que tomador y null en beneficiarios y facturacion*/
        CliObtenerDatosResponse datosAsegurado = datosTomador;
        List<Beneficiario> datosBeneficiario = null;
        DatosFacturacion datosFacturacion = null;

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
                //datosTomador = obtenerDatosTomadorDesdeVista();
                ((PrincipalActivity)getActivity()).datosTomador = datosTomador;
                FragmentTransaction transaction = requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.contenedor_vistas, new SoatcAseguradoDatosFragment());
                transaction.addToBackStack(null);
                transaction.commit();
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