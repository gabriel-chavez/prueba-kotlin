package com.emizor.univida.fragmento;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.emizor.univida.R;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.imprime.ImpresionManagerSoatc;
import com.emizor.univida.modelo.dominio.univida.ApiResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.Beneficiario;
import com.emizor.univida.modelo.dominio.univida.soatc.CliObtenerDatosResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.DatosFacturacion;
import com.emizor.univida.modelo.dominio.univida.soatc.EmiPolizaObtenerResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.RequestBuilderEfectivizar;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.rest.ApiService;
import com.emizor.univida.rest.DatosConexion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoatcResumenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoatcResumenFragment extends Fragment implements ImpresionManagerSoatc.ImpresionListener {

    private String nombreAsegurado, direccion, ciudad, telefono, email, vigencia;
    private String nombreFacturacion, documento, extension, emailFacturacion, celularFacturacion;
    private ImpresionManagerSoatc impresionManager;
    private EmiPolizaObtenerResponse respuestaEfectivizacion;

    public SoatcResumenFragment() {
        // Required empty public constructor
    }


    public static SoatcResumenFragment newInstance(String param1, String param2) {
        SoatcResumenFragment fragment = new SoatcResumenFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializarImpresionManager();
    }

    private void inicializarImpresionManager() {
        try {
            ControladorSqlite2 controlador = new ControladorSqlite2(getActivity());
            com.emizor.univida.modelo.dominio.univida.seguridad.User user = controlador.obtenerUsuario();
            controlador.cerrarConexion();

            if (user != null) {
                impresionManager = new ImpresionManagerSoatc(getActivity(), user, this);
            } else {
                Toast.makeText(getActivity(), "Error: Usuario no disponible", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error al inicializar impresora", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_soatc_resumen, container, false);

        // Referencias a los TextView
        TextView tvNombreAsegurado = view.findViewById(R.id.tvResumenNombreAsegurado);
        TextView tvDireccion = view.findViewById(R.id.tvResumenDireccion);
        TextView tvCiudad = view.findViewById(R.id.tvResumenCiudad);
        TextView tvTelefono = view.findViewById(R.id.tvResumenTelefono);
        TextView tvEmail = view.findViewById(R.id.tvResumenEmail);
        TextView tvVigencia = view.findViewById(R.id.tvResumenVigencia);

        TextView tvNombreFacturacion = view.findViewById(R.id.tvResumenNombreFacturacion);
        TextView tvDocumento = view.findViewById(R.id.tvResumenDocumento);
        TextView tvExtension = view.findViewById(R.id.tvResumenExtension);
        TextView tvEmailFacturacion = view.findViewById(R.id.tvResumenEmailFacturacion);
        TextView tvCelularFacturacion = view.findViewById(R.id.tvResumenCelularFacturacion);

        CliObtenerDatosResponse datosAsegurado = ((PrincipalActivity) getActivity()).datosAsegurado;

        DatosFacturacion datosFacturacion = ((PrincipalActivity) getActivity()).datosFacturacion;

        // Llenar los datos
        tvNombreAsegurado.setText(datosAsegurado.getNombreCompleto());
        tvDireccion.setText(datosAsegurado.PerDomicilioParticular);
        tvCiudad.setText(datosAsegurado.PerTParGenDepartamentoDescripcionDocumentoIdentidad);
        tvTelefono.setText(datosAsegurado.PerTelefonoMovil);
        tvEmail.setText(datosAsegurado.PerCorreoElectronico);
        //tvVigencia.setText(emptyToDash(vigencia));

        tvNombreFacturacion.setText(datosFacturacion.fact_razon_social);
        tvDocumento.setText(datosFacturacion.fact_nit_ci);
        tvExtension.setText(datosFacturacion.fact_ci_complemento);
        tvEmailFacturacion.setText(datosFacturacion.fact_correo_cliente);
        tvCelularFacturacion.setText(datosFacturacion.fact_telefono_cliente);

        // Botones
        Button btnAtras = view.findViewById(R.id.btnAtrasResumen);
        Button btnConfirmar = view.findViewById(R.id.btnConfirmar);

        btnAtras.setOnClickListener(v -> {
            // Volver atrás en la pila de fragmentos
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnConfirmar.setOnClickListener(v -> {
            //Toast.makeText(getContext(), "Datos confirmados correctamente", Toast.LENGTH_SHORT).show();
            efectivizarVenta();

        });

        return view;
    }

    private void efectivizarVenta() {
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
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_EMISION_EFECTIVIZAR;
        ((PrincipalActivity) requireActivity()).mostrarLoading(true);
        apiService.solicitudPost(url, request, response -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
            Type responseType = new TypeToken<ApiResponse<EmiPolizaObtenerResponse>>() {
            }.getType();
            ApiResponse<EmiPolizaObtenerResponse> apiResponse = new Gson().fromJson(response, responseType);
           // if (apiResponse.exito) {
                procesarRespuestaEfectivizacion(apiResponse);
                //   -- > AQUI DEBE INICIAR LA IMPRESION<--
                //      mostrarDialogo(apiResponse.mensaje);
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.contenedor_vistas, new SoatcBeneficiariosFragment())
//                        .addToBackStack(null)
//                        .commit();
//            } else {
//                mostrarDialogo(apiResponse.mensaje);
//            }
        }, error -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
            // Manejo de error de conexión
        });

    }

    private void mostrarDialogo(String mensaje) {
        if (!isAdded() || getActivity() == null) {
            return;
        }

        requireActivity().runOnUiThread(() -> {
            new android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Validación")
                    .setMessage(mensaje)
                    .setPositiveButton("OK", null)
                    .show();
        });
    }
    private void mostrarDialogo2(String mensaje, Runnable onReintentar, Runnable onCancelar) {
        if (!isAdded() || getActivity() == null) return;

        requireActivity().runOnUiThread(() -> {
            new android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Validación")
                    .setMessage(mensaje)
                    .setCancelable(false)
                    .setPositiveButton("Reintentar", (dialog, which) -> {
                        if (onReintentar != null) onReintentar.run();
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        if (onCancelar != null) onCancelar.run();
                        dialog.dismiss();
                    })
                    .show();
        });
    }


    @Override
    public void onImpresionCompletada() {
        getActivity().runOnUiThread(() -> {
            mostrarDialogo("Impresión completada exitosamente");

            navegarASiguientePantalla();
        });
    }

    @Override
    public void onErrorImpresion(String mensaje) {
        getActivity().runOnUiThread(() -> {
            //  btnConfirmar.setEnabled(true);
            mostrarDialogo("Error en impresión: " + mensaje);
        });
    }

    @Override
    public void onTerminoDeImprimir() {
        getActivity().runOnUiThread(() -> {

            preguntarImpresionColilla();
        });
    }

    @Override
    public void onImpresionIniciada() {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getActivity(), "Iniciando impresión...", Toast.LENGTH_SHORT).show();
        });
    }

    private void procesarRespuestaEfectivizacion(ApiResponse<EmiPolizaObtenerResponse> apiResponse) {


        if (apiResponse.exito && apiResponse.datos != null) {
            respuestaEfectivizacion = apiResponse.datos;

            // VERIFICAR LOS DATOS QUE LLEGAN
            Log.d("IMPRESION", "Número de factura: " + respuestaEfectivizacion.getFacturaMaestro().getNumeroFactura());
//            Log.d("IMPRESION", "Código CCI: " + respuestaEfectivizacion.getPolizaDetalle().getPolDetCodigoCCI());
//            Log.d("IMPRESION", "Prima: " + respuestaEfectivizacion.getPolizaDetalle().getPrimaCobrada());
//            Log.d("IMPRESION", "Cliente: " + respuestaEfectivizacion.getPolizaDetalle().getClientePN().getNombreCompleto());

            // INICIAR IMPRESIÓN
            if (impresionManager != null) {
                mostrarDialogoExito(apiResponse.mensaje, () -> {
                    impresionManager.imprimirFacturaSoatc(respuestaEfectivizacion);
                });
            } else {
                mostrarDialogo("Impresión no disponible - ");
                //     btnConfirmar.setEnabled(true);
            }
        }
        else{
            mostrarDialogo2(apiResponse.mensaje,()->{
                efectivizarVenta();
            },()->{
                Toast.makeText(getContext(), "Operación cancelada", Toast.LENGTH_SHORT).show();
            });
        }
    }


    private void preguntarImpresionColilla() {
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Impresión Completa")
                .setMessage("¿Desea imprimir la colilla?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    if (impresionManager != null && respuestaEfectivizacion != null) {
                        impresionManager.imprimirColillaSoatc(respuestaEfectivizacion);
                    }
                })
                .setNegativeButton("No", (dialog, which) -> {
                    navegarASiguientePantalla();
                })
                .setCancelable(false)
                .show();
    }

    private void mostrarDialogoExito(String mensaje, Runnable onAceptar) {
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Éxito")
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    if (onAceptar != null) {
                        onAceptar.run();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (impresionManager != null) {
            impresionManager.liberarRecursos();
        }
    }

    private void navegarASiguientePantalla() {

        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}