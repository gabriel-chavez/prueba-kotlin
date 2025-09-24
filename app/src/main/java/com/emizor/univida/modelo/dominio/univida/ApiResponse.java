package com.emizor.univida.modelo.dominio.univida;
public class ApiResponse<T> {
    public boolean exito;
    public int codigo_retorno;
    public String mensaje;
    public T datos;

    // Constructor vacío necesario para Gson
    public ApiResponse() {}
}
