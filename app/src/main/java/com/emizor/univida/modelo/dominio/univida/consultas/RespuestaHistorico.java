package com.emizor.univida.modelo.dominio.univida.consultas;


public class RespuestaHistorico {
    private boolean exito;
    private int codigo_retorno;
    private String mensaje;
    private HistoricoDatos datos;

    // Getters
    public boolean isExito() {
        return exito;
    }

    public int getCodigo_retorno() {
        return codigo_retorno;
    }

    public String getMensaje() {
        return mensaje;
    }

    public HistoricoDatos getDatos() {
        return datos;
    }

    public static class HistoricoDatos {
        private double AdjuntoFkImagen;
        private String DispositivoId;
        private String EmpleadoNombreCompleto;
        private String FechaHora;
        private String ImageBase64;
        private double Latitud;
        private double Longitud;
        private int Secuencial;
        private String TParControlTurnosTipoEventoDescripcion;

        // Getters
        public double getAdjuntoFkImagen() {
            return AdjuntoFkImagen;
        }

        public String getDispositivoId() {
            return DispositivoId;
        }

        public String getEmpleadoNombreCompleto() {
            return EmpleadoNombreCompleto;
        }

        public String getFechaHora() {
            return FechaHora;
        }

        public String getImageBase64() {
            return ImageBase64;
        }

        public double getLatitud() {
            return Latitud;
        }

        public double getLongitud() {
            return Longitud;
        }

        public int getSecuencial() {
            return Secuencial;
        }

        public String getTParControlTurnosTipoEventoDescripcion() {
            return TParControlTurnosTipoEventoDescripcion;
        }
    }
}