package com.emizor.univida.fragmento;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.emizor.univida.modelo.dominio.univida.parametricas.ParametricaGenerica;
import com.emizor.univida.modelo.dominio.univida.soatc.Beneficiario;
import com.emizor.univida.utils.ParametricasCache;

import java.util.ArrayList;
import java.util.List;


public class SoatcBeneficiariosFragment extends Fragment {

    private LinearLayout containerBeneficiarios;
    private TextView tvListaVacia;
    private Button btnAgregarBeneficiario;
    private Button btnAtras, btnSiguiente;
    private List<Beneficiario> listaBeneficiarios = new ArrayList<>();


    Spinner spinnerParentesco;
    public static SoatcBeneficiariosFragment newInstance(String param1, String param2) {
        SoatcBeneficiariosFragment fragment = new SoatcBeneficiariosFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soatc_beneficiarios, container, false);

        containerBeneficiarios = view.findViewById(R.id.containerBeneficiarios);
        tvListaVacia = view.findViewById(R.id.tvListaVacia);
        btnAgregarBeneficiario = view.findViewById(R.id.btnAgregarBeneficiario);
        btnAtras = view.findViewById(R.id.btnAtras);
        btnSiguiente = view.findViewById(R.id.btnSiguiente);

        if (getActivity() instanceof PrincipalActivity) {
            PrincipalActivity principal = (PrincipalActivity) getActivity();

            if (principal.datosBeneficiario != null) {
                this.listaBeneficiarios = principal.datosBeneficiario;
                cargarDatosBeneficiario();
            }

        }
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFormulario()) {
                    ((PrincipalActivity)getActivity()).datosBeneficiario =listaBeneficiarios;
                    getFragmentManager().beginTransaction()
                            .replace(R.id.contenedor_vistas, new SoatcFacturacionFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        btnAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        btnAgregarBeneficiario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoAgregarBeneficiario();
            }
        });

        return view;
    }
    private void mostrarDialogoAgregarBeneficiario() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_beneficiario_simple, null);


        final EditText etNombre = dialogView.findViewById(R.id.etNombreCompleto);
        spinnerParentesco = dialogView.findViewById(R.id.spinnerParentesco);
        final EditText etPorcentaje = dialogView.findViewById(R.id.etPorcentaje);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardar);

        // Configurar spinner
        List<ParametricaGenerica> listaParentescos = ParametricasCache.getInstance().getParentesco();

        ArrayAdapter<ParametricaGenerica> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, listaParentescos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerParentesco.setAdapter(adapter);

//        String[] parentescos = {"Hijo", "Hija", "Cónyuge", "Padre", "Madre", "Otro"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
//                android.R.layout.simple_spinner_item, parentescos);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerParentesco.setAdapter(adapter);

        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialog.dismiss(); }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombre.getText().toString().trim();
                ParametricaGenerica seleccionado = (ParametricaGenerica) spinnerParentesco.getSelectedItem();
                int parentescoFk = seleccionado.Identificador;
                String porcentajeText = etPorcentaje.getText().toString().trim();

                if (nombre.isEmpty()) {
                    etNombre.setError("Ingrese un nombre");
                    return;
                }

                if (porcentajeText.isEmpty()) {
                    etPorcentaje.setError("Ingrese un porcentaje");
                    return;
                }

                int porcentaje;
                try {
                    porcentaje = Integer.parseInt(porcentajeText);
                } catch (NumberFormatException e) {
                    etPorcentaje.setError("Porcentaje inválido");
                    return;
                }

                if (porcentaje < 1 || porcentaje > 100) {
                    etPorcentaje.setError("Porcentaje inválido (1-100)");
                    return;
                }

                agregarBeneficiario(new Beneficiario(nombre, parentescoFk, porcentaje));
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void agregarBeneficiario(final Beneficiario beneficiario) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View itemView = inflater.inflate(R.layout.item_beneficiario_simple, containerBeneficiarios, false);

        TextView tvNombre = itemView.findViewById(R.id.tvNombreCompleto);
        TextView tvParentesco = itemView.findViewById(R.id.tvParentesco);
        TextView tvPorcentaje = itemView.findViewById(R.id.tvPorcentaje);
        Button btnEliminar = itemView.findViewById(R.id.btnEliminar);


        ParametricaGenerica parentesco = (ParametricaGenerica) spinnerParentesco.getSelectedItem();

        tvNombre.setText(beneficiario.getNombre());
        tvParentesco.setText(parentesco.Descripcion);
        tvPorcentaje.setText( beneficiario.getPorcentaje() + "%");

        listaBeneficiarios.add(beneficiario);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerBeneficiarios.removeView(itemView);
                listaBeneficiarios.remove(beneficiario);
                checkListaVacia();
            }
        });

        containerBeneficiarios.addView(itemView);
        checkListaVacia();
    }
    private void cargarDatosBeneficiario() {
        if (listaBeneficiarios == null || listaBeneficiarios.isEmpty()) {
            tvListaVacia.setVisibility(View.VISIBLE);
            return;
        }

        tvListaVacia.setVisibility(View.GONE);
        containerBeneficiarios.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (Beneficiario beneficiario : listaBeneficiarios) {
            View itemView = inflater.inflate(R.layout.item_beneficiario_simple, containerBeneficiarios, false);

            TextView tvNombre = itemView.findViewById(R.id.tvNombreCompleto);
            TextView tvParentesco = itemView.findViewById(R.id.tvParentesco);
            TextView tvPorcentaje = itemView.findViewById(R.id.tvPorcentaje);
            Button btnEliminar = itemView.findViewById(R.id.btnEliminar);

            // Obtener descripción del parentesco desde cache paramétricas
            ParametricaGenerica parentesco = null;
            if (ParametricasCache.getInstance().getParentesco() != null) {
                for (ParametricaGenerica p : ParametricasCache.getInstance().getParentesco()) {
                    if (p.Identificador == beneficiario.getParentesco()) {
                        parentesco = p;
                        break;
                    }
                }
            }

            tvNombre.setText(beneficiario.getNombre());
            tvParentesco.setText(parentesco != null ? parentesco.Descripcion : "Sin definir");
            tvPorcentaje.setText(beneficiario.getPorcentaje() + "%");

            // Acción eliminar
            btnEliminar.setOnClickListener(v -> {
                containerBeneficiarios.removeView(itemView);
                listaBeneficiarios.remove(beneficiario);
                checkListaVacia();
            });

            containerBeneficiarios.addView(itemView);
        }
    }

    private void checkListaVacia() {
        if (containerBeneficiarios.getChildCount() == 0) {
            tvListaVacia.setVisibility(View.VISIBLE);
        } else {
            tvListaVacia.setVisibility(View.GONE);
        }
    }
    private boolean validarFormulario() {
        int suma = 0;

        // Si no hay beneficiarios, está bien, retornamos true
        if (listaBeneficiarios.isEmpty()) {
            return true;
        }

        // Sumamos los porcentajes
        for (Beneficiario b : listaBeneficiarios) {
            suma += b.getPorcentaje();
        }

        // Validamos que sumen 100%
        if (suma != 100) {
            mostrarDialogo("La suma de porcentajes debe ser 100%. Actualmente es " + suma + "%");
            return false;
        }

        return true;
    }

    private void mostrarDialogo(String mensaje) {
        new AlertDialog.Builder(getContext())
                .setTitle("Atención")
                .setMessage(mensaje)
                .setPositiveButton("OK", null)
                .show();
    }

}