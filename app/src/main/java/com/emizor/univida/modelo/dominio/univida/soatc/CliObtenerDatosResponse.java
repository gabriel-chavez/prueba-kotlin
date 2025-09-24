package com.emizor.univida.modelo.dominio.univida.soatc;

import java.util.Date;

public class CliObtenerDatosResponse  {

    // Datos principales del cliente
    public String PerApellidoCasada;
    public String PerApellidoMaterno;
    public String PerApellidoPaterno;
    public String PerCorreoElectronico;
    public String PerDocumentoIdentidadExtension;
    public Integer PerDocumentoIdentidadNumero;
    public String PerDomicilioComercial;
    public String PerDomicilioParticular;
    public Integer PerEdad;
    public String PerFechaAdicion; // usar String y parsear si se necesita Date
    public String PerFechaAdicionFormato;
    public String PerNacimientoFecha;
    public String PerNacimientoFechaFormato;
    public String PerNombrePrimero;
    public String PerNombreSegundo;
    public String PerNumeroIdentificacionTributaria;
    public Integer PerSecuencial;
    public String PerTParCliDocumentoIdentidadTipoAbreviacion;
    public String PerTParCliDocumentoIdentidadTipoDescripcion;
    public Integer PerTParCliDocumentoIdentidadTipoFk;
    public String PerTParCliEstadoCivilAbreviacion;
    public String PerTParCliEstadoCivilDescripcion;
    public Integer PerTParCliEstadoCivilFk;
    public String PerTParCliGeneroAbreviacion;
    public String PerTParCliGeneroDescripcion;
    public Integer PerTParCliGeneroFk;
    public String PerTParCliProfesionAbreviacion;
    public String PerTParCliProfesionDescripcion;
    public Integer PerTParCliProfesionFk;
    public String PerTParCliRegistroEstadoAbreviacion;
    public String PerTParCliRegistroEstadoDescripcion;
    public String PerTParCliRegistroTipoAbreviacion;
    public String PerTParCliRegistroTipoDescripcion;
    public Integer PerTParCliRegistroTipoFk;
    public String PerTParCliSituacionLaboralDescripcion;
    public Integer PerTParCliSituacionLaboralFk;
    public String PerTParGenActividadEconomicaAbreviacion;
    public String PerTParGenActividadEconomicaDescripcion;
    public Integer PerTParGenActividadEconomicaFk;
    public String PerTParGenActividadEconomicaSecDescripcion;
    public Integer PerTParGenActividadEconomicaSecFk;
    public String PerTParGenDepartamentoAbreviacionDocumentoIdentidad;
    public String PerTParGenDepartamentoAbreviacionNacimiento;
    public String PerTParGenDepartamentoDescripcionDocumentoIdentidad;
    public String PerTParGenDepartamentoDescripcionNacimiento;
    public Integer PerTParGenDepartamentoFkDocumentoIdentidad;
    public Integer PerTParGenDepartamentoFkNacimiento;
    public String PerTParGenNivelIngresosAbreviacion;
    public String PerTParGenNivelIngresosDescripcion;
    public Integer PerTParGenNivelIngresosFk;
    public String PerTParGenPaisAbreviacionNacionalidad;
    public String PerTParGenPaisAbreviacionResidencia;
    public String PerTParGenPaisDescripcionNacionalidad;
    public String PerTParGenPaisDescripcionPaisNacionalidad;
    public String PerTParGenPaisDescripcionResidencia;
    public Integer PerTParGenPaisFkNacionalidad;
    public Integer PerTParGenPaisFkResidencia;
    public Integer PerTPersonaFk;
    public String PerTelefonoFijo;
    public String PerTelefonoMovil;
    public Integer PerTrabajoAnioIngreso;
    public String PerTrabajoCargo;
    public String PerTrabajoCargoSec;
    public String PerTrabajoLugar;

    // Objetos opcionales
    public Object oConyuge;

    public DatosGenericos oDatosGenericos;
    public FormularioRespaldoPrincipal oFormularioRespaldoPrincipal;

    // Clases internas
    public static class DatosGenericos {
        public String DatGenFecha;
        public String DatGenFechaFormato;
        public Integer DatGenSecuencial;
        public String DatGenTParGenDepartamentoAbreviacion;
        public String DatGenTParGenDepartamentoDescripcion;
        public Integer DatGenTParGenDepartamentoFk;
        public Integer DatGenTSucursalFk;
    }

    public static class FormularioRespaldoPrincipal {
        public Long FRPNumeroPrincipal;
        public Integer FRPNumeroSecundario;
        public Integer FRPSecuencial;
        public Double FRPTArchivoAdjuntoFk; // puede ser null o decimal
    }
    public String getNombreCompleto() {
        StringBuilder sb = new StringBuilder();

        if (PerNombrePrimero != null && !PerNombrePrimero.isEmpty()) {
            sb.append(PerNombrePrimero.trim());
        }

        if (PerNombreSegundo != null && !PerNombreSegundo.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(PerNombreSegundo.trim());
        }

        if (PerApellidoPaterno != null && !PerApellidoPaterno.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(PerApellidoPaterno.trim());
        }

        if (PerApellidoMaterno != null && !PerApellidoMaterno.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(PerApellidoMaterno.trim());
        }

        if (PerApellidoCasada != null && !PerApellidoCasada.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(PerApellidoCasada.trim());
        }

        return sb.toString().trim();
    }
}