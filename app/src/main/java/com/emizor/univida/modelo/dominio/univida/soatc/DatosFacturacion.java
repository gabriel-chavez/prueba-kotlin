package com.emizor.univida.modelo.dominio.univida.soatc;

public class DatosFacturacion {
    public int fact_tipo_doc_identidad_fk;
    public String fact_nit_ci;
    public String fact_ci_complemento;
    public String fact_razon_social;
    public String fact_correo_cliente;
    public String fact_telefono_cliente;

    // Constructor vacío (necesario para Gson)
    public DatosFacturacion() {
    }

    // Constructor con parámetros (opcional)
    public DatosFacturacion(int fact_tipo_doc_identidad_fk, String fact_nit_ci, String fact_ci_complemento,
                            String fact_razon_social, String fact_correo_cliente, String fact_telefono_cliente) {
        this.fact_tipo_doc_identidad_fk = fact_tipo_doc_identidad_fk;
        this.fact_nit_ci = fact_nit_ci;
        this.fact_ci_complemento = fact_ci_complemento;
        this.fact_razon_social = fact_razon_social;
        this.fact_correo_cliente = fact_correo_cliente;
        this.fact_telefono_cliente = fact_telefono_cliente;
    }
}
