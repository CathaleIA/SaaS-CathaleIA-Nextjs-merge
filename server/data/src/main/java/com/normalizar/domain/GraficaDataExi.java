package com.normalizar.domain;

import java.util.List;

public class GraficaDataExi {
    private String title;
    private String xAxisLabel;
    private String yAxisLabel;
    private Double inflexcion; // Asumiendo que son Strings con el formato "X,XXXX"
    private List<Double> puntosEjeY;
    private List<Double> puntosEjeX;

    public GraficaDataExi() {
    }

    public GraficaDataExi(String title, String xAxisLabel, String yAxisLabel, Double inflexcion,
            List<Double> puntosEjeY, List<Double> puntosEjeX) {
        this.title = title;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        this.inflexcion = inflexcion;
        this.puntosEjeY = puntosEjeY;
        this.puntosEjeX = puntosEjeX;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public String getyAxisLabel() {
        return yAxisLabel;
    }

    public void setyAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public Double getInflexcion() {
        return inflexcion;
    }

    public void setInflexcion(Double inflexcion) {
        this.inflexcion = inflexcion;
    }

    public List<Double> getPuntosEjeY() {
        return puntosEjeY;
    }

    public void setPuntosEjeY(List<Double> puntosEjeY) {
        this.puntosEjeY = puntosEjeY;
    }

    public List<Double> getPuntosEjeX() {
        return puntosEjeX;
    }

    public void setPuntosEjeX(List<Double> puntosEjeX) {
        this.puntosEjeX = puntosEjeX;
    }

}
