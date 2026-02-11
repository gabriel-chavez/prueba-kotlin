package com.emizor.univida.fragmento;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.emizor.univida.R;
import com.emizor.univida.activities.EfectivizarVentaActivity;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.dialogo.DialogoEmizor;
import com.emizor.univida.excepcion.ErrorPapelException;
import com.emizor.univida.excepcion.ImpresoraErrorException;
import com.emizor.univida.excepcion.NoHayPapelException;
import com.emizor.univida.excepcion.VoltageBajoException;
import com.emizor.univida.imprime.ImpresionManagerSoatc;
import com.emizor.univida.imprime.ImprimirAvisoListener;
import com.emizor.univida.imprime.ImprimirFactura;
import com.emizor.univida.modelo.dominio.univida.ApiResponse;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoatcResumenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoatcResumenFragment extends Fragment implements ImpresionManagerSoatc.ImpresionListener, ImprimirAvisoListener {

    private String nombreAsegurado, direccion, ciudad, telefono, email, vigencia;
    private String nombreFacturacion, documento, extension, emailFacturacion, celularFacturacion;
    private ImpresionManagerSoatc impresionManager;
    private EmiPolizaObtenerResponse respuestaEfectivizacion;
    private ImprimirFactura imprimirFactura;

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
        imprimirFactura = ImprimirFactura.obtenerImpresora(requireContext());
        imprimirFactura.setAvisoListener(this);
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
                    .setTitle("Efectivización")
                    .setMessage(mensaje)
                    .setPositiveButton("OK", null)

                    .show();
        });
    }
    private void mostrarDialogo2(String mensaje, Runnable onReintentar, Runnable onCancelar) {
        if (!isAdded() || getActivity() == null) return;

        requireActivity().runOnUiThread(() -> {
            new android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Efectivización")
                    .setMessage(mensaje)
                    .setCancelable(false)
                    .setPositiveButton("Reintentar", (dialog, which) -> {
                        if (onReintentar != null) onReintentar.run();
                    })
                    .setNegativeButton("Aceptar", (dialog, which) -> {
                        if (onCancelar != null) onCancelar.run();
                        dialog.dismiss();
                    })
                    .show();
        });
    }


    @Override
    public void onImpresionCompletada() {
        if (getActivity() != null && isAdded()) {
            getActivity().runOnUiThread(() -> {
                mostrarDialogo("Impresión completada exitosamente");
                navegarASiguientePantalla();
            });
        }
    }

    @Override
    public void onErrorImpresion(String mensaje) {
        if (getActivity() != null && isAdded()) {
            getActivity().runOnUiThread(() -> {
                mostrarDialogo("Error en impresión: " + mensaje);
            });
        }
    }

    @Override
    public void onTerminoDeImprimir() {
        getActivity().runOnUiThread(() -> {
            cambiarFragmento(new SoatcAseguradoBuscarFragment());
          //  preguntarImpresionColilla();
        });
    }

    @Override
    public void onImpresionIniciada() {
        if (getActivity() != null && isAdded()) {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), "Iniciando impresión...", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void procesarRespuestaEfectivizacion(ApiResponse<EmiPolizaObtenerResponse> apiResponse) {

        if (!isAdded() || getActivity() == null) {
            // Si el fragmento no está adjunto, evitamos proceder
            return;
        }

        if (apiResponse.exito && apiResponse.datos != null) {
            respuestaEfectivizacion = apiResponse.datos;

            // VERIFICAR LOS DATOS QUE LLEGAN
            Log.d("IMPRESION", "Número de factura: " + respuestaEfectivizacion.getFacturaMaestro().getNumeroFactura());
//            Log.d("IMPRESION", "Código CCI: " + respuestaEfectivizacion.getPolizaDetalle().getPolDetCodigoCCI());
//            Log.d("IMPRESION", "Prima: " + respuestaEfectivizacion.getPolizaDetalle().getPrimaCobrada());
//            Log.d("IMPRESION", "Cliente: " + respuestaEfectivizacion.getPolizaDetalle().getClientePN().getNombreCompleto());

            // INICIAR IMPRESIÓN
            try {
                ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(requireContext());
                User user = controladorSqlite2.obtenerUsuario();
                imprimirFactura.prepararImpresionFacturaSoatc(user, respuestaEfectivizacion);

            } catch (Exception ex) {
                ex.printStackTrace();
                //mostrarMensaje("No se puede imprimir los datos por que algunos o todos son nulos.");
                ((PrincipalActivity) requireActivity()).mostrarLoading(false);
                new AlertDialog.Builder(getActivity())
                        .setTitle("Atención")
                        .setMessage("No se puede imprimir los datos por que algunos o todos son nulos.")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            cambiarFragmento(new SoatcAseguradoBuscarFragment());
                        }).show();
                return;
            }

            iniciarImprimir();

//
//            if (impresionManager != null) {
//                mostrarDialogoExito(apiResponse.mensaje, () -> {
//                    impresionManager.imprimirFacturaSoatc(respuestaEfectivizacion);
//                });
//            } else {
//                ((PrincipalActivity) requireActivity()).mostrarLoading(false);
//                new AlertDialog.Builder(getActivity())
//                        .setTitle("Atención")
//                        .setMessage("Impresión no disponible")
//                        .setPositiveButton("Aceptar", (dialog, which) -> {
//                            cambiarFragmento(new SoatcAseguradoBuscarFragment());
//                        }).show();
//                //     btnConfirmar.setEnabled(true);
//            }
        }
        else{
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
            mostrarDialogo2(apiResponse.mensaje,()->{
                efectivizarVenta();
            },()->{
                //cambiarFragmento(new SoatcAseguradoBuscarFragment());
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
                        ((PrincipalActivity) requireActivity()).mostrarLoading(false);
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
    private void cambiarFragmento(Fragment fragment) {

        try {
            if (getActivity() != null) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedor_vistas, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void iniciarImprimir() {

        requireActivity().runOnUiThread(() -> {
          //  contadorImprimir++;

            new Thread(() -> {
                try {
                    imprimirFactura.imprimirFactura2();
                } catch (ImpresoraErrorException e) {
                    e.printStackTrace();
                   // errorImp = true;
                    mostrarMensaje("Error en la impresora.");
                    //contadorImprimir = cantDocsImprimir;
                } catch (NoHayPapelException e) {
                    e.printStackTrace();
                    //errorImp = true;
                    mostrarMensaje("No hay papel para imprimir.");
                    //contadorImprimir = cantDocsImprimir;
                } catch (VoltageBajoException e) {
                    e.printStackTrace();
                    //errorImp = true;
                    mostrarMensaje("Error de batería baja. No podrá imprimir con la batería baja.");
                    //contadorImprimir = cantDocsImprimir;
                } catch (ErrorPapelException e) {
                    e.printStackTrace();
                    //errorImp = true;
                    mostrarMensaje("Error en la impresora");
                    //contadorImprimir = cantDocsImprimir;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ///errorImp = true;
                    mostrarMensaje("Error al imprimir los datos. Si esto persiste, comuníquese con el encargado.");
                  //  contadorImprimir = cantDocsImprimir;
                }
            }).start();
        });

    }
    private void mostrarMensaje(final String mensaje) {
        try {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Atención")
                    .setMessage(mensaje)
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        dialog.dismiss();
                    }).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void terminoDeImprimir() {
        requireActivity().runOnUiThread(() -> {
            cambiarFragmento(new SoatcAseguradoBuscarFragment());
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
              //  preguntarAbrir();

        });
    }
    private void preguntarAbrir() {
        try {
            Date fechaImpresion = Calendar.getInstance().getTime();
            ControladorSqlite2 controlador = new ControladorSqlite2(getActivity());
            com.emizor.univida.modelo.dominio.univida.seguridad.User user = controlador.obtenerUsuario();
            controlador.cerrarConexion();

            imprimirFactura.procesarColillaVentaSoatc(user, respuestaEfectivizacion, fechaImpresion);

            DialogoEmizor dialogoEmizor = new DialogoEmizor();
            dialogoEmizor.setTipoDialogo(7);
            dialogoEmizor.setMensaje("Imprimir Colilla?");
            dialogoEmizor.setCancelable(false);
            dialogoEmizor.show(requireActivity().getSupportFragmentManager(), null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}