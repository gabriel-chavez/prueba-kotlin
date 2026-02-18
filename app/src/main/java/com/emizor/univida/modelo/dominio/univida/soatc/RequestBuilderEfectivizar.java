package com.emizor.univida.modelo.dominio.univida.soatc;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class RequestBuilderEfectivizar {
    public static Map<String, Object> construirRequest(
            DatosFacturacion datosFacturacion,
            CliObtenerDatosResponse datosTomador,
            CliObtenerDatosResponse datosAsegurado,
            List<Beneficiario> listaBeneficiarios) {

        if (datosTomador == null) {
            datosTomador = datosAsegurado;
        }

        Map<String, Object> request = new HashMap<>();

        // e_datos_facturacion
        Map<String, Object> e_datos_facturacion = new HashMap<>();

        if(datosFacturacion!=null){
            e_datos_facturacion.put("fact_tipo_doc_identidad_fk", datosFacturacion.fact_tipo_doc_identidad_fk);
            e_datos_facturacion.put("fact_nit_ci", checkEmpty(datosFacturacion.fact_nit_ci));
            e_datos_facturacion.put("fact_ci_complemento", checkEmpty(datosFacturacion.fact_ci_complemento));
            e_datos_facturacion.put("fact_razon_social", checkEmpty(datosFacturacion.fact_razon_social));
            e_datos_facturacion.put("fact_correo_cliente", checkEmpty(datosFacturacion.fact_correo_cliente));
            e_datos_facturacion.put("fact_telefono_cliente", checkEmpty(datosFacturacion.fact_telefono_cliente));
        }else {
            e_datos_facturacion.put("fact_tipo_doc_identidad_fk", null);
            e_datos_facturacion.put("fact_nit_ci", null);
            e_datos_facturacion.put("fact_ci_complemento", null);
            e_datos_facturacion.put("fact_razon_social", null);
            e_datos_facturacion.put("fact_correo_cliente", null);
            e_datos_facturacion.put("fact_telefono_cliente", null);
        }

        request.put("e_datos_facturacion", e_datos_facturacion);
        request.put("plan_pagos_comprobante_datos", e_datos_facturacion);

        // Campos simples
        request.put("t_par_medio_pago_fk", 1);
        request.put("pol_mae_t_producto_plan_prima_fk", 25);
        request.put("pol_mae_t_par_gen_departamento_fk", datosAsegurado.PerTParGenDepartamentoFkVenta);

        // e_datos_tomador
        request.put("e_datos_tomador", mapDatosPersona(datosTomador));

        // e_datos_asegurado
        request.put("e_datos_asegurado", mapDatosPersona(datosAsegurado));

        // l_pol_ben_beneficiario
        List<Map<String, Object>> l_pol_ben_beneficiario = new ArrayList<>();
        if (listaBeneficiarios != null) {
            for (Beneficiario b : listaBeneficiarios) {
                Map<String, Object> ben = new HashMap<>();
                ben.put("pol_ben_nombre_completo", checkEmpty(b.getNombre()));
                ben.put("pol_ben_t_par_emi_beneficiario_parentesco_fk", b.getParentesco());
                ben.put("pol_ben_beneficio_porcentaje", b.getPorcentaje());
                l_pol_ben_beneficiario.add(ben);
            }
        }
        request.put("l_pol_ben_beneficiario", l_pol_ben_beneficiario);

        // e_transaccion_identificador
        Map<String, Object> e_transaccion_identificador = new HashMap<>();
        String llaveA = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                ? new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date())
                : "";
        int llaveB = 100000 + new Random().nextInt(900000);
        e_transaccion_identificador.put("tra_ide_llave_a", llaveA);
        e_transaccion_identificador.put("tra_ide_llave_b", llaveB);
        e_transaccion_identificador.put("tra_ide_llave_c", null);
        request.put("e_transaccion_identificador", e_transaccion_identificador);

        return request;
    }

    private static Map<String, Object> mapDatosPersona(CliObtenerDatosResponse persona) {
        Map<String, Object> map = new HashMap<>();
        map.put("per_documento_identidad_numero", persona.PerDocumentoIdentidadNumero);
        map.put("per_t_par_cli_documento_identidad_tipo_fk", persona.PerTParCliDocumentoIdentidadTipoFk);
        map.put("per_t_par_gen_departamento_fk_documento_identidad", persona.PerTParGenDepartamentoFkDocumentoIdentidad);
        map.put("per_apellido_paterno", checkEmpty(persona.PerApellidoPaterno));
        map.put("per_apellido_materno", checkEmpty(persona.PerApellidoMaterno));
        map.put("per_nombre_primero", checkEmpty(persona.PerNombrePrimero));
        map.put("per_nombre_segundo", checkEmpty(persona.PerNombreSegundo));
        map.put("per_nacimiento_fecha", checkEmpty(persona.PerNacimientoFecha));
        map.put("per_documento_identidad_extension", checkEmpty(persona.PerDocumentoIdentidadExtension));
        map.put("per_telefono_movil", checkEmpty(persona.PerTelefonoMovil));
        map.put("per_domicilio_particular", checkEmpty(persona.PerDomicilioParticular));
        map.put("per_t_par_gen_actividad_economica_fk", persona.PerTParGenActividadEconomicaFk);
        map.put("per_t_par_gen_pais_fk_nacionalidad", persona.PerTParGenPaisFkNacionalidad);
        map.put("per_t_par_gen_departamento_fk_nacimiento", persona.PerTParGenDepartamentoFkNacimiento);
        map.put("per_t_par_cli_genero_fk", persona.PerTParCliGeneroFk);
        map.put("per_correo_electronico", checkEmpty(persona.PerCorreoElectronico));
        return map;
    }

    private static String checkEmpty(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value;
    }
}

