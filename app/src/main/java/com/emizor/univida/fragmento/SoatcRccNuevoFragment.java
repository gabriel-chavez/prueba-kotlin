package com.emizor.univida.fragmento;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emizor.univida.R;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.adapter.ItemSoatcRccNuevoAdapter;
import com.emizor.univida.excepcion.ImpresoraErrorException;
import com.emizor.univida.excepcion.NoHayPapelException;
import com.emizor.univida.excepcion.VoltageBajoException;
import com.emizor.univida.imprime.ImprimirFactura;
import com.emizor.univida.modelo.dominio.univida.ApiResponse;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.soatc.ElaborarRccResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.ListarCobrosResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.ListarRccResponse;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.rest.ApiService;
import com.emizor.univida.rest.DatosConexion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoatcRccNuevoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoatcRccNuevoFragment extends Fragment {

    private Button btnFecha, btnNuevoReporte;
    private ItemSoatcRccNuevoAdapter adapter;
    private final Calendar cal = Calendar.getInstance();
    private final SimpleDateFormat sdfApi = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat sdfApiFormato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final Gson gson = new Gson();
    private TextView tvTotalImporte, tvCantidadVendidos, tvCantidadValidos, tvCantidadRevertidos, tvCantidadAnulados;
    private ListarCobrosResponse datos;
    private ImprimirFactura imprimirFactura;

    public SoatcRccNuevoFragment() {
        // Required empty public constructor
    }

    public static SoatcRccNuevoFragment newInstance(String param1, String param2) {
        SoatcRccNuevoFragment fragment = new SoatcRccNuevoFragment();
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
        View v= inflater.inflate(R.layout.fragment_soatc_rcc_nuevo, container, false);
        btnFecha = v.findViewById(R.id.btnFechaListaVenta);
        ListView listView = v.findViewById(R.id.listViewVentas);
        adapter = new ItemSoatcRccNuevoAdapter(getContext());
        listView.setAdapter(adapter);
        btnFecha.setText("Seleccionar fecha");
        btnFecha.setOnClickListener(_v -> mostrarDatePicker());

        btnNuevoReporte=v.findViewById(R.id.btnNuevoReporte);

        tvTotalImporte = v.findViewById(R.id.tvTotalImporte);
        tvCantidadVendidos = v.findViewById(R.id.tvCantidadVendidos);
        tvCantidadValidos = v.findViewById(R.id.tvCantidadValidos);
        tvCantidadRevertidos = v.findViewById(R.id.tvCantidadRevertidos);
        tvCantidadAnulados = v.findViewById(R.id.tvCantidadAnulados);
        btnNuevoReporte.setOnClickListener(x ->{
            if(validarFormulario())
                mostrarDialogResumenRcc();
        } );

        imprimirFactura = ImprimirFactura.obtenerImpresora(getContext());

        cargarVentasDeFecha(cal);
        return v;
    }
    private void mostrarDatePicker() {
        DatePickerDialog dp = new DatePickerDialog(
                requireContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    cargarVentasDeFecha(cal);
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        dp.show();
    }
    private void cargarVentasDeFecha(Calendar fecha) {
        String fechaStr = sdfApi.format(fecha.getTime());
        String fechaStrFormato = sdfApiFormato.format(fecha.getTime());
        btnFecha.setText(fechaStrFormato);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("fecha", fechaStr);
        parametros.put("ramo_principal_secuencial", 1);//solo ramo soatc


        ((PrincipalActivity) requireActivity()).mostrarLoading(true);
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_CONCILIACION_COBROS_LISTAR;

        ApiService api = new ApiService(getContext());
        api.solicitudPost(url, parametros, response -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
            Type type = new TypeToken<ApiResponse<ListarCobrosResponse>>() {}.getType();
            ApiResponse<ListarCobrosResponse> apiResp = gson.fromJson(response, type);

            if (apiResp != null && apiResp.exito && apiResp.datos != null) {
                datos = apiResp.datos;

                tvTotalImporte.setText(String.format(Locale.getDefault(), "Bs. %.2f", datos.RccFormularioImporte));
                tvCantidadVendidos.setText(String.valueOf(datos.RccCantidadComprobante));
                tvCantidadValidos.setText(String.valueOf(datos.RccCantidadComprobanteValidos));
                tvCantidadRevertidos.setText(String.valueOf(datos.RccCantidadComprobanteRevertidos));
                tvCantidadAnulados.setText(String.valueOf(datos.RccCantidadComprobanteAnulados));

                adapter.setData(datos.lComprobanteDatos);

            } else {
                String msg = (apiResp != null && !TextUtils.isEmpty(apiResp.mensaje))
                        ? apiResp.mensaje : "No se pudo obtener la información.";
                new AlertDialog.Builder(getContext())
                        .setMessage(msg)
                        .setPositiveButton("Aceptar", (dialog, id) -> dialog.dismiss())
                        .show();
                adapter.setData(null);
            }

        }, error -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);

            mostrarToast("Error de conexión. Intentá nuevamente.");
            adapter.setData(null);
        });
    }
    private void mostrarDialogResumenRcc() {
        String fechaStr = sdfApiFormato.format(cal.getTime());
        //  Inflar el layout personalizado
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View vistaDialogo = inflater.inflate(R.layout.dialog_nuevo_reporte_cierre, null);

        // Referenciar los TextView del XML
        TextView tvTotalImporte = vistaDialogo.findViewById(R.id.tvTotalImporte);
        TextView tvCantidadValidos = vistaDialogo.findViewById(R.id.tvCantidadValidos);
        TextView tvCantidadRevertidos = vistaDialogo.findViewById(R.id.tvCantidadRevertidos);
        TextView tvCantidadAnulados = vistaDialogo.findViewById(R.id.tvCantidadAnulados);
        TextView tvCantidadVendidos = vistaDialogo.findViewById(R.id.tvCantidadVendidos);
        TextView tvFechaRendicion = vistaDialogo.findViewById(R.id.tvFechaRendicion);

        // asignar datos dinámicos
        tvTotalImporte.setText(String.valueOf(datos.RccFormularioImporte));
        tvCantidadVendidos.setText(String.valueOf(datos.RccCantidadComprobante));
        tvCantidadValidos.setText(String.valueOf(datos.RccCantidadComprobanteValidos));
        tvCantidadRevertidos.setText(String.valueOf(datos.RccCantidadComprobanteRevertidos));
        tvCantidadAnulados.setText(String.valueOf(datos.RccCantidadComprobanteAnulados));
        tvCantidadAnulados.setText(String.valueOf(datos.RccCantidadComprobanteAnulados));
        tvFechaRendicion.setText(fechaStr);

        //  Crear el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(vistaDialogo)
                .setTitle("Reporte de cierre de cobros")
                .setPositiveButton("Efectivizar RCC", (dialog, which) -> {
                    // Acción al confirmar
                    efectivizarRcc();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                });

        // Mostrar
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void efectivizarRcc() {

        Toast.makeText(getContext(), "Efectivizando RCC...", Toast.LENGTH_SHORT).show();

        String fechaStr = sdfApi.format(cal.getTime());
        btnFecha.setText(fechaStr);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("formulario_fecha", fechaStr);

        parametros.put("formulario_importe", datos.RccFormularioImporte);
        Map<String, Object> medioPago = new HashMap<>();
        medioPago.put("MedioDePagoFk", 1);
        medioPago.put("MedioDePagoPrima", datos.RccFormularioImporte);
        medioPago.put("MedioDePagoDato", "");
        medioPago.put("MedioDePagoFecha", fechaStr+"T00:00:00");
        medioPago.put("MedioBancoFk", 0);
        medioPago.put("MedioSigno", 0);

        Gson gson = new Gson();
        String medioPagoJson = gson.toJson(medioPago);
        parametros.put("rcc_json_medio_pago", medioPagoJson);
        parametros.put("ramo_principal_secuencial", 1);//solo ramo soatc


        ((PrincipalActivity) requireActivity()).mostrarLoading(true);
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_CONCILIACION_ELABORAR;

        ApiService api = new ApiService(getContext());
        api.solicitudPost(url, parametros, response -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
            try {
                Type type = new TypeToken<ApiResponse<ElaborarRccResponse>>() {}.getType();
                ApiResponse<ElaborarRccResponse> apiResp = gson.fromJson(response, type);

                if (apiResp != null && apiResp.exito && apiResp.datos != null) {
                    
                    final ElaborarRccResponse rccResult = apiResp.datos;

                    tvTotalImporte.setText(String.format(Locale.getDefault(), "Bs. %.2f", 0.0));
                    tvCantidadVendidos.setText(String.valueOf(0));
                    tvCantidadValidos.setText(String.valueOf(0));
                    tvCantidadRevertidos.setText(String.valueOf(0));
                    tvCantidadAnulados.setText(String.valueOf(0));
                    this.datos=null;
                    adapter.setData(null);

                    new AlertDialog.Builder(getContext())
                            .setTitle("Éxito")
                            .setMessage(apiResp.mensaje)
                            .setCancelable(false)
                            .setPositiveButton("Aceptar", (dialog, id) -> {
                                this.datos=null;
                                dialog.dismiss();
                                obtenerRccParaImpresion(rccResult.RccSecuencial);
                            })
                            .show();

                } else {
                    String msg = (apiResp != null && !TextUtils.isEmpty(apiResp.mensaje))
                            ? apiResp.mensaje : "No se pudo obtener la información.";
                    new AlertDialog.Builder(getContext())
                            .setMessage(msg)
                            .setPositiveButton("Aceptar", (dialog, id) -> dialog.dismiss())
                            .show();
                    adapter.setData(null);
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);

            mostrarToast("Error de conexión. Intentá nuevamente.");
            adapter.setData(null);
        });
    }

    private void obtenerRccParaImpresion(int rccSecuencial) {
        ((PrincipalActivity) requireActivity()).mostrarLoading(true);
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_CONCILIACION_OBTENER;
        Map<String, Object> params = new HashMap<>();
        params.put("rcc_secuencial", rccSecuencial);

        new ApiService(getContext()).solicitudPost(url, params, response -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
            try {
                Type type = new TypeToken<ApiResponse<ListarRccResponse>>() {}.getType();
                ApiResponse<ListarRccResponse> apiResp = gson.fromJson(response, type);
                if(apiResp.exito && apiResp.datos != null) {
                    imprimirReporte(apiResp.datos);
                } else {
                    mostrarToast(apiResp.mensaje != null ? apiResp.mensaje : "No se pudo obtener el reporte para imprimir.");
                }
            } catch (Exception e) {
                mostrarToast("Error al obtener el reporte para imprimir.");
            }
        }, error -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
            mostrarToast("Error de red al obtener el reporte.");
        });
    }

    private void imprimirReporte(ListarRccResponse rccResponse) {
        try {
            ControladorSqlite2 controlador = new ControladorSqlite2(getContext());
            User user = controlador.obtenerUsuario();
            controlador.cerrarConexion();

            if (user == null) {
                Toast.makeText(getContext(), "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            imprimirFactura.prepararImpresionRcc(user, rccResponse);
            ejecutarImpresion();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al preparar la impresión", Toast.LENGTH_SHORT).show();
        }
    }

    private void ejecutarImpresion() {
        try {
            imprimirFactura.imprimirFactura2();
        } catch (NoHayPapelException | ImpresoraErrorException | VoltageBajoException e) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Impresión")
                    .setMessage(e.getMessage())
                    .setCancelable(false)
                    .setPositiveButton("Reintentar", (dialog, which) -> ejecutarImpresion())
                    .setNegativeButton("Cancelar", null)
                    .show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error inesperado al imprimir", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarFormulario() {
        boolean valido = true;

        if (datos==null ) {
            mostrarToast("No contiene registros para realizar el Rcc");
            valido = false;
        }


        return valido;
    }

    private void mostrarToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
