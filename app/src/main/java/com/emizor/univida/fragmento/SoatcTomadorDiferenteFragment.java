package com.emizor.univida.fragmento;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.emizor.univida.R;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.modelo.dominio.univida.soatc.CliObtenerDatosResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.DatosBusquedaAseguradoTomador;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoatcTomadorDiferenteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoatcTomadorDiferenteFragment extends Fragment {

    private RadioGroup radioGroupTomador;
    private RadioButton radioSi, radioNo;
    private Button btnAtras, btnSiguiente;

    public SoatcTomadorDiferenteFragment() {
        // Required empty public constructor
    }


    public static SoatcTomadorDiferenteFragment newInstance(String param1, String param2) {
        SoatcTomadorDiferenteFragment fragment = new SoatcTomadorDiferenteFragment();
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
        View view = inflater.inflate(R.layout.fragment_soatc_tomador_diferente, container, false);

        radioGroupTomador = view.findViewById(R.id.radioGroupTomador);
        radioSi = view.findViewById(R.id.radioSi);
        radioNo = view.findViewById(R.id.radioNo);
        btnAtras = view.findViewById(R.id.btnAtras);
        btnSiguiente = view.findViewById(R.id.btnSiguiente);

        radioGroupTomador.setOnCheckedChangeListener((group, checkedId) -> {
            btnSiguiente.setEnabled(checkedId != -1);
        });
        btnSiguiente.setOnClickListener(v -> {
            Fragment destino;

            if (radioSi.isChecked()) {

                destino = new SoatcTomadorBuscarFragment();
            } else {
                ((PrincipalActivity)getActivity()).datosTomador = null;
                destino = new SoatcAseguradoDatosFragment();
            }

            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.contenedor_vistas, destino);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        btnAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}