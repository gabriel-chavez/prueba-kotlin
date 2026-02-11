package com.emizor.univida.utils;

import com.emizor.univida.modelo.dominio.univida.parametricas.ParametricaGenerica;
import com.emizor.univida.modelo.dominio.univida.turnos.Evento;
import com.emizor.univida.modelo.dominio.univida.turnos.Punto;

import java.util.List;

public class ParametricasCache {
    private static ParametricasCache instance;
    private List<ParametricaGenerica> departamentos;
    private List<ParametricaGenerica> documentosIdentidad;
    private List<ParametricaGenerica> estadoCivil;
    private List<ParametricaGenerica> genero;
    private List<ParametricaGenerica> nacionalidad;
    private List<ParametricaGenerica> parentesco;
    private List<ParametricaGenerica> documentosIdentidadFacturacion;
    private List<Punto> puntos;
    private List<Evento> eventos;

    private ParametricasCache() { }

    public static synchronized ParametricasCache getInstance() {
        if (instance == null) {
            instance = new ParametricasCache();
        }
        return instance;
    }

    public List<ParametricaGenerica> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<ParametricaGenerica> departamentos) {
        this.departamentos = departamentos;
    }

    public List<ParametricaGenerica> getDocumentosIdentidad() {
        return documentosIdentidad;
    }

    public void setDocumentosIdentidad(List<ParametricaGenerica> documentosIdentidad) {
        this.documentosIdentidad = documentosIdentidad;
    }

    public List<ParametricaGenerica> getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(List<ParametricaGenerica> estadoCivil) {
        this.estadoCivil = estadoCivil;
    }
    public List<ParametricaGenerica> getGenero() {
        return genero;
    }

    public void setGenero(List<ParametricaGenerica> genero) {
        this.genero = genero;
    }

    public List<ParametricaGenerica> getNacionalidad() {
        return nacionalidad;
    }
    public void setNacionalidad(List<ParametricaGenerica> nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
    public List<ParametricaGenerica> getParentesco() {
        return parentesco;
    }
    public void setParentesco(List<ParametricaGenerica> parentesco) {
        this.parentesco = parentesco;
    }
    public List<ParametricaGenerica> getDocumentosIdentidadFacturacionFacturacion() {
        return documentosIdentidadFacturacion;
    }
    public void setDocumentosIdentidadFacturacionFacturacion(List<ParametricaGenerica> facturacion) {
        this.documentosIdentidadFacturacion = facturacion;
    }
    public void setPuntos(List<Punto> puntos) {
        this.puntos = puntos;
    }
    public List<Punto> getPuntos() {
        return this.puntos;
    }
    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }
    public List<Evento> getEventos() {
        return this.eventos;
    }
}
