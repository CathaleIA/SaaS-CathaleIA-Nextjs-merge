package com.normalizar.utility.impl;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.normalizar.domain.Report;
import com.normalizar.utility.ImappingUseCase;


// 
//      * Parses the raw JSON string and maps its content to a structured Map
//      * suitable for Thymeleaf rendering.
//      *
//      * @param jsonString The raw JSON string from the normalization service.
//      * @param report The Report object from the initial request, used for header data.
//      * @return A Map<String, Object> containing data structured for Thymeleaf.
//      * @throws IOException If JSON parsing fails.
// 


public class ImplMappingUseCase implements ImappingUseCase {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, Object> mapJsonToThymeleafModel(String jsonString, Report report) throws IOException {
        Map<String, Object> fullNormalizedData = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});

        // Este es el objeto de tipo map que espera el Template que esta en Thymeleaf
        Map<String, Object> modelo = new HashMap<>();

        // vamos a mapear el primer objeto de jsonresult
        // 1. infogeneral Mapping
                                            //Este tipo de syntaxis es type cast esta asegurando el tipo de valor que esta consultado
                                            // esta seguro que el objeto infoGeral tiene un objeto de tipo map o anida o reserva o contiene un tipo de ese objeto
        Map<String, Object> infoGeneral =  (Map<String, Object>) fullNormalizedData.get("InfoGeneral");
        // en pocas palrabas estoyadentro del objeto infoGerenel que s parte de la repsuesta del JSONRESULT
        if (infoGeneral != null){
            Map<String, Object> informacionGeneral = new HashMap<>();
            informacionGeneral.put("fabricante", infoGeneral.get("fabricante"));
            informacionGeneral.put("modelo", infoGeneral.get("modelo"));
            informacionGeneral.put("numero_de_serie", infoGeneral.get("numero_de_serie"));
            informacionGeneral.put("I_priaria", infoGeneral.get("I_priaria"));
            informacionGeneral.put("I_secundaria", infoGeneral.get("I_secundaria"));
            informacionGeneral.put("Nucleo", infoGeneral.get("Nucleo"));
            informacionGeneral.put("applicacion", infoGeneral.get("applicacion"));
            informacionGeneral.put("norma", infoGeneral.get("norma"));
            informacionGeneral.put("clase", infoGeneral.get("clase"));
            informacionGeneral.put("Burden", infoGeneral.get("Burden"));
            informacionGeneral.put("Compañia", infoGeneral.get("Compañia"));
            informacionGeneral.put("planta", infoGeneral.get("planta"));
            informacionGeneral.put("subestacion", infoGeneral.get("subestacion"));
            informacionGeneral.put("tag", infoGeneral.get("tag"));
            informacionGeneral.put("fase", infoGeneral.get("fase"));

            //enviamos los datos cargados de esta seccion al objeto 
            // pirnciplal es muy parecido a lo que hicimos en la estrucutra del Script de normalizacion


            modelo.put("InformacionGeneral", informacionGeneral);


            // Seccion de encabezado
            Map<String, String> headerData = new HashMap<>();
            headerData.put("protocolo",  infoGeneral.get("norma") + "-PROT"); // Example
            headerData.put("hoja", "1 de X"); // Dynamic later?
            headerData.put("codigo", report.getActivo() + "-CODE"); // Example
            headerData.put("ciudad", "Bucaramanga"); // Static for now
            headerData.put("fecha", Instant.now().atZone(ZoneId.of("-05:00")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            modelo.put("header", headerData);


        }
        // Seccion Resulados
        Map<String, Object> resultadosBlockJson = (Map<String , Object>) fullNormalizedData.get("resultados");
        if (resultadosBlockJson != null) {
            Map<String, Object> resultados = new HashMap<>();
            resultados.put("nucleo", resultadosBlockJson.get("nucleo"));
            resultados.put("R_devanados", resultadosBlockJson.get("R_devanados"));
            resultados.put("relacionCorriente", resultadosBlockJson.get("relacionCorriente"));
            resultados.put("poplaridad", resultadosBlockJson.get("poplaridad"));
            resultados.put("EvalGeneral", resultadosBlockJson.get("EvalGeneral"));
            
            modelo.put("resultados", resultados);


        }

        // tablaRelacionCorrienteNominal

        List<String> burdenLabelsRC = Arrays.asList("10 / 0,8", "5 / 0,8", "2,5 / 1", "1,25 / 1");
        modelo.put("burdenLabelsRC", burdenLabelsRC);

        List<String> burdenLabelsCN = Arrays.asList("10 / 0,8", "5 / 0,8", "2,5 / 1", "1,25 / 1");
        modelo.put("burdenLabelsCN", burdenLabelsCN);

        Map<String, Object> tablaRelacionCorrienteNominal = (Map<String, Object>) fullNormalizedData.get("tablaRelacionCorrienteNominal");
        if (tablaRelacionCorrienteNominal != null) {

            // se ha hecho asi porque estamos ya nadando adentro del json estamos mapeando la estrucura
            List<Object> headerRC = (List<Object>) tablaRelacionCorrienteNominal.get("header");
            modelo.put("tablaRelacionCorrienteNominalHeader", headerRC);

            // Data de tabla

            List<List<Double>> dataRC = (List<List<Double>>) tablaRelacionCorrienteNominal.get("dataRC");
            modelo.put("tablaRelacionCorrienteNominalData", dataRC);
        }

        // tablaCorrienteNominal
        Map<String, Object> tablaCorrienteNominal = (Map<String, Object>) fullNormalizedData.get("tablaCorrienteNominal");
        if (tablaCorrienteNominal != null) {
            List<Object> headerCN = (List<Object>) tablaCorrienteNominal.get("header");
            modelo.put("tablaCorrienteNominalHeader", headerCN);

            List<List<Double>> dataCN = (List<List<Double>>) tablaCorrienteNominal.get("dataCN");
            modelo.put("tablaCorrienteNominalData", dataCN); // Pass the raw List<List<Double>>
        }
        Map<String, Object> ResultadoExitacionJson =  (Map<String, Object>) fullNormalizedData.get("ResultadoExitacion");
        // estamos como si fuera mapeando un mega DTO
        if (ResultadoExitacionJson != null) {
            Map<String, Object> ResultadoExitacion = new HashMap<>();
            ResultadoExitacion.put("norma", ResultadoExitacionJson.get("norma"));
            ResultadoExitacion.put("V_Saturacion", ResultadoExitacionJson.get("V_Saturacion"));
            ResultadoExitacion.put("I_magnet", ResultadoExitacionJson.get("I_magnet"));
            ResultadoExitacion.put("ALF", ResultadoExitacionJson.get("ALF"));
            ResultadoExitacion.put("ALFi", ResultadoExitacionJson.get("ALFi"));
            ResultadoExitacion.put("eci", ResultadoExitacionJson.get("eci"));
            ResultadoExitacion.put("Cirterio", ResultadoExitacionJson.get("Cirterio"));
            ResultadoExitacion.put("evaluacion", ResultadoExitacionJson.get("evaluacion"));

            modelo.put("ResultadoExitacion", ResultadoExitacion);
        }

        // --- 5. Graph Data (GraficaCN and GraficaRCN) ---
        // These are already well-structured for Chart.js, so just pass them through.
        modelo.put("GraficaCN", fullNormalizedData.get("GraficaCN"));
        modelo.put("GraficaRCN", fullNormalizedData.get("GraficaRCN"));
        modelo.put("GraficaExitacion", fullNormalizedData.get("GraficaExitacion"));
        modelo.put("conclusiones", new ArrayList<>()); // Start empty for preview
    return modelo;
    }

}
