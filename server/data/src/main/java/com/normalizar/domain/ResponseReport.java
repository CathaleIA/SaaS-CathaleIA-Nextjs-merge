package com.normalizar.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseReport {
    private String report_id;
    private String archivoHtml;
    private GraficaData graficaCNData; // Objeto con los datos del gráfico CN
    private GraficaData graficaRCNData; // Objeto con los datos del gráfico RCN
    private GraficaDataExi graficaDataExi;



   
}
