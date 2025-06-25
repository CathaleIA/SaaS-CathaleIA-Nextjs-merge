package com.normalizar.domain;

import java.util.List;
import java.util.Map;

public class GraficaData {
    private String title;
    private String xAxisLabel;
    private String yAxisLabel;
    private List<String> rangosEjeY; // Asumiendo que son Strings con el formato "X,XXXX"
    private List<Integer> rangosEjeX;

    private Map<String, List<Double>> puntosGrficar;
    public GraficaData() {
    }
    public GraficaData(String title, String xAxisLabel, String yAxisLabel, List<String> rangosEjeY,
            List<Integer> rangosEjeX, Map<String, List<Double>> puntosGrficar) {
        this.title = title;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        this.rangosEjeY = rangosEjeY;
        this.rangosEjeX = rangosEjeX;
        this.puntosGrficar = puntosGrficar;
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
    public List<String> getRangosEjeY() {
        return rangosEjeY;
    }
    public void setRangosEjeY(List<String> rangosEjeY) {
        this.rangosEjeY = rangosEjeY;
    }
    public List<Integer> getRangosEjeX() {
        return rangosEjeX;
    }
    public void setRangosEjeX(List<Integer> rangosEjeX) {
        this.rangosEjeX = rangosEjeX;
    }
    public Map<String, List<Double>> getPuntosGrficar() {
        return puntosGrficar;
    }
    public void setPuntosGrficar(Map<String, List<Double>> puntosGrficar) {
        this.puntosGrficar = puntosGrficar;
    }

  

}
