package com.emizor.univida.fragmento;

import android.app.AlertDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import android.widget.AdapterView;
import android.widget.Toast;


public class SoatcFacturacionFragment extends Fragment {
    private Spinner spinnerTipoDocumento;
    private EditText etNumeroDocumento, etExtensionDocumento, etNombreRazonSocial, etEmail, etCelular;
    private Button btnAtras, btnSiguiente;
    private TextView tvTitulo;
    private LinearLayout layoutExtensionDocumento;

    public static SoatcFacturacionFragment newInstance(String param1, String param2) {
        SoatcFacturacionFragment fragment = new SoatcFacturacionFragment();
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
        View view = inflater.inflate(R.layout.fragment_soatc_facturacion, container, false);

        // Referencias UI
        spinnerTipoDocumento = view.findViewById(R.id.spinnerTipoDocumento);
        etNumeroDocumento = view.findViewById(R.id.etNumeroDocumento);
        etExtensionDocumento = view.findViewById(R.id.etExtensionDocumento);
        etNombreRazonSocial = view.findViewById(R.id.etNombreRazonSocial);
        etEmail = view.findViewById(R.id.etEmail);
        etCelular = view.findViewById(R.id.etCelular);
        btnAtras = view.findViewById(R.id.btnAtras);
        btnSiguiente = view.findViewById(R.id.btnSiguiente);
        tvTitulo = view.findViewById(R.id.tvTitulo);
        layoutExtensionDocumento = view.findViewById(R.id.layoutExtensionDocumento);

        layoutExtensionDocumento.setVisibility(View.GONE);

        obtenerDocumentosIdentidadFacturacion();

        // Botón Atrás
        btnAtras.setOnClickListener(v -> {
            getActivity().onBackPressed(); // vuelve atrás
        });

        // Botón Siguiente
        btnSiguiente.setOnClickListener(v -> {
            if (validarFormulario()) {
                DatosFacturacion datosFacturacion = registrarDatosFacturacion();
                ((PrincipalActivity) getActivity()).datosFacturacion = datosFacturacion;

                validarVendible();
            }
        });

        spinnerTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                if (item instanceof ParametricaGenerica) {
                    ParametricaGenerica seleccionado = (ParametricaGenerica) item;
                    if (seleccionado.Identificador == 1) {
                        layoutExtensionDocumento.setVisibility(View.VISIBLE);
                    } else {
                        layoutExtensionDocumento.setVisibility(View.GONE);
                        etExtensionDocumento.setText("");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }



    private void obtenerDocumentosIdentidadFacturacion() {
        List<ParametricaGenerica> documentosIdentidadFacturacion = ParametricasCache.getInstance().getDocumentosIdentidadFacturacionFacturacion();

        if (documentosIdentidadFacturacion != null && !documentosIdentidadFacturacion.isEmpty()) {
            ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_spinner_item, documentosIdentidadFacturacion);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTipoDocumento.setAdapter(adapter);

        }
    }

    private boolean validarFormulario() {
        String numeroDoc = etNumeroDocumento.getText().toString().trim();
        String nombre = etNombreRazonSocial.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String celular = etCelular.getText().toString().trim();

        if (numeroDoc.isEmpty()) {
            etNumeroDocumento.setError("Ingrese número de documento");
            return false;
        }

        if (nombre.isEmpty()) {
            etNombreRazonSocial.setError("Ingrese nombre o razón social");
            return false;
        }

        if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Correo inválido");
            return false;
        }

        if (celular.isEmpty()) {
            etCelular.setError("Ingrese número de documento");
            return false;
        }

        if (!celular.isEmpty() && celular.length() < 8) {
            etCelular.setError("Ingrese un número válido (mínimo 8 dígitos)");
            return false;
        }

        return true;
    }

    private void mostrarDialogo(String mensaje) {
        new AlertDialog.Builder(getContext())
                .setTitle("Validación")
                .setMessage(mensaje)
                .setPositiveButton("OK", null)
                .show();
    }
    private DatosFacturacion registrarDatosFacturacion() {
        // Obtener referencias a los campos del layout
//        Spinner spinnerTipoDocumento = getView().findViewById(R.id.spinnerTipoDocumento);
//        EditText etNumeroDocumento = getView().findViewById(R.id.etNumeroDocumento);
//        EditText etExtensionDocumento = getView().findViewById(R.id.etExtensionDocumento);
//        EditText etNombreRazonSocial = getView().findViewById(R.id.etNombreRazonSocial);
//        EditText etEmail = getView().findViewById(R.id.etEmail);
//        EditText etCelular = getView().findViewById(R.id.etCelular);

        // Obtener valores
        int tipoDocumento = spinnerTipoDocumento.getSelectedItemPosition() + 1;
        String numeroDocumento = etNumeroDocumento.getText().toString().trim();
        String extensionDocumento = etExtensionDocumento.getText().toString().trim();
        String nombreRazonSocial = etNombreRazonSocial.getText().toString().trim();
        String correoCliente = etEmail.getText().toString().trim();
        String telefonoCliente = etCelular.getText().toString().trim();



        // Crear objeto DatosFacturacion
        DatosFacturacion datosFacturacion = new DatosFacturacion();
        datosFacturacion.fact_tipo_doc_identidad_fk = tipoDocumento;
        datosFacturacion.fact_nit_ci = numeroDocumento;
        datosFacturacion.fact_ci_complemento = extensionDocumento.isEmpty() ? null : extensionDocumento;
        datosFacturacion.fact_razon_social = nombreRazonSocial;
        datosFacturacion.fact_correo_cliente = correoCliente;
        datosFacturacion.fact_telefono_cliente = telefonoCliente;

        return datosFacturacion;
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

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contenedor_vistas, new SoatcResumenFragment())
                        .addToBackStack(null)
                        .commit();
            } else {
                mostrarDialogo(apiResponse.mensaje);
            }
        }, error -> {
            // Manejo de error de conexión
        });
    }
}