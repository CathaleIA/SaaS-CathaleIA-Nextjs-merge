package com.normalizar.templateMemori;

import java.io.ByteArrayOutputStream;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.normalizar.controller.RenderPdfLambda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImpleCaseMemory implements ItemplateCase {
    private static final Logger logger = LoggerFactory.getLogger(RenderPdfLambda.class);

    @Override
    public String selectTemplate(String activo) {
        System.out.println("ImpleCaseMemory: Valor de 'activo' recibido: '" + activo + "'");
        String lowerCaseActivo = activo.toLowerCase();
        System.out.println("ImpleCaseMemory: 'activo' convertido a minúsculas: '" + lowerCaseActivo + "'");

        String selectedTemplate = switch (lowerCaseActivo) {
            case "alt" -> "ALT";
            case "ct" -> "CT";
            case "dtest" -> "DTEST";
            case "eng" -> "ENG";
            case "etest" -> "ETEST";
            case "ib" -> "IB";
            case "im" -> "IM";
            case "pt" -> "PT";
            case "rl" -> "RL";
            default -> "default";
        };
        System.out.println("ImpleCaseMemory: Template seleccionado: '" + selectedTemplate + "'");
        return selectedTemplate;
    }

    @Override
    public byte[] createPDFComponent(String htmlComponeent) {
        if (htmlComponeent == null || htmlComponeent.isEmpty()) {
            logger.error("Error: El archivo HTML está vacío o no es válido.");
            throw new IllegalArgumentException("El archivo HTML está vacío o no es válido.");
        } else {

            try (ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream()) {
                Document doc = Jsoup.parse(htmlComponeent, "UTF-8");
                if (!htmlComponeent.contains("id=\"Report_PDF_Component\"")) {
                    throw new IllegalArgumentException("El HTML no contiene la sección 'Report_PDF_Component'.");
                }
                doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
                Element componentSecction = doc.getElementById("Report_PDF_Component");

                if (componentSecction == null) {
                    logger.error("Error: El archivo HTML está vacío o no es válido.");
                    throw new IllegalArgumentException("No se encontró la sección específica en el HTML.");
                }

                String cleanHtml = componentSecction.outerHtml(); // Obtiene el HTML completo de la sección
                logger.info("Retorno de componente especifico normalizado: {}", cleanHtml);

                // URL cssUrl = getClass().getClassLoader().getResource("templates/generalStyle.css");

                // if (cssUrl == null) {
                //     throw new FileNotFoundException("No se encontró el archivo CSS: templates/generalStyle.css");
                // }
                // String cssPath = cssUrl.toExternalForm();
                // logger.info("Usando archivo CSS: {}", cssPath);

                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocumentFromString(cleanHtml);
       
                SharedContext cntxt = renderer.getSharedContext();
                cntxt.setPrint(true);
                cntxt.setInteractive(false);
                // Soporte para renderizacion de imagenes en base64
                renderer.getSharedContext().setReplacedElementFactory(new B64ImageReplacedElementFactory());
                renderer.layout();
                renderer.createPDF(pdfOutputStream);

                return pdfOutputStream.toByteArray();

            } catch (Exception e) {
                logger.error("Error al generar el PDF: {}", e.getMessage(), e);
                throw new RuntimeException("Error al generar el PDF: " + e.getMessage(), e);
            }

        }
    }
}
