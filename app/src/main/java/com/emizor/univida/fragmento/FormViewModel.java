package com.emizor.univida.fragmento;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;



import java.util.List;

public class FormViewModel extends ViewModel {
    // Paso 1: AseguradoBuscarFragment
    public MutableLiveData<String> documentoAsegurado = new MutableLiveData<>();
    public MutableLiveData<String> nombreAsegurado = new MutableLiveData<>();

    // Paso 2: TomadorDiferenteFragment
    public MutableLiveData<Boolean> esTomadorDiferente = new MutableLiveData<>();

    // Paso 3: TomadorBuscarFragment
    public MutableLiveData<String> documentoTomador = new MutableLiveData<>();

    // Paso 4: TomadorDatosFragment
    public MutableLiveData<String> nombreTomador = new MutableLiveData<>();

    // Paso 5: AseguradoDatosFragment
    public MutableLiveData<String> direccionAsegurado = new MutableLiveData<>();

    // Paso 6: BeneficiariosFragment
    public MutableLiveData<List<String>> beneficiarios = new MutableLiveData<>();

    // Paso 7: FacturacionFragment
    public MutableLiveData<String> metodoPago = new MutableLiveData<>();

    // Paso 8: ResumenFragment
    public MutableLiveData<Boolean> confirmacion = new MutableLiveData<>();

    //public MutableLiveData<CliObtenerDatosResponse> cliObtenerDatosResponse = new MutableLiveData<>();
}