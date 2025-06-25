package com.s3reportlambda.pdfCreateUseCase.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Workbook;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.s3reportlambda.pdfCreateUseCase.ICreatePdf;


public class ImplCreateService implements ICreatePdf{

        @Override
        public PdfPCell cell(String text, Font font) {
                return getCell(text, font, PdfPCell.ALIGN_LEFT);

        }

        // Utilizamos este metodo para poder retornar un objeto tipo PfgPCell y
        // utilizarlo en la implementacion
        public static PdfPCell getCell(String text, Font font, int alignment) {
                PdfPCell cell = new PdfPCell(new Phrase(text, font));
                cell.setHorizontalAlignment(alignment);
                cell.setPadding(1);
                return cell;
        }

        @Override
        public void addRow(PdfPTable table, String key1, String val1, String key2, String val2, String key3,
                        String val3,
                        Font boldFont, Font normalFont) {

                table.addCell(cell(key1, boldFont));
                table.addCell(cell(val1, normalFont));
                table.addCell(cell(key2, boldFont));
                table.addCell(cell(val2, normalFont));
                table.addCell(cell(key3, boldFont));
                table.addCell(cell(val3, normalFont));

        }

        @Override
        public void titleRows(PdfPTable tableName, String space, String title, String title2, String title3,
                        String title4,
                        Font bolFont) {
                tableName.addCell(cell(space, bolFont));
                tableName.addCell(cell(title, bolFont));
                tableName.addCell(cell(title2, bolFont));
                tableName.addCell(cell(title3, bolFont));
                tableName.addCell(cell(title4, bolFont));
        }

        @Override
        public void rowsBacis(PdfPTable tableName, String space, String title, String title2, String title3,
                        String title4,
                        Font bolFont, Font normalfont) {
                tableName.addCell(cell(space, bolFont));
                tableName.addCell(cell(title, normalfont));
                tableName.addCell(cell(title2, normalfont));
                tableName.addCell(cell(title3, normalfont));
                tableName.addCell(cell(title4, normalfont));
        }

    @Override
    public ByteArrayOutputStream generatePDf(InputStream file) throws IOException, DocumentException {
      try(Workbook workbook = new HSSFWorkbook(file)) {
        
                        // Sheet sheet = workbook.getSheetAt(0);
                        // // Row row = sheet.getRow(0);

                        // String encabezado1 = row.getCell(0).getStringCellValue();
                        // String encabezado2 = row.getCell(1).getStringCellValue();
                        // String encabezado3 = row.getCell(2).getStringCellValue();

                        // Generar PDF en memoria
                        // Clase para guardarlo en memoria
                        ByteArrayOutputStream pdOutput = new ByteArrayOutputStream();
                        Document document = new Document(PageSize.A4);
                        PdfWriter.getInstance(document, pdOutput);
                        document.open();

                        // Fonts PDFs
                        Font titleFont = new Font(Font.HELVETICA, 14, Font.BOLD);
                        Font boldFontWhite = new Font(Font.HELVETICA, 9, Font.BOLD, java.awt.Color.WHITE);
                        Font boldFont = new Font(Font.HELVETICA, 9, Font.BOLD);
                        Font normalfont = new Font(Font.HELVETICA, 9);

                        PdfPTable tableProtoclo = new PdfPTable(4);
                        tableProtoclo.setWidths(new float[] { 1, 4, 1, 1 });
                        // tableProtoclo.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tableProtoclo.setSpacingBefore(10);
                        tableProtoclo.setWidthPercentage(100);

                        PdfPCell imgCopower = new PdfPCell(new PdfPCell());
                        imgCopower.setRowspan(4);
                        imgCopower.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                        imgCopower.setPadding(8);

                        PdfPCell titel = new PdfPCell(
                                        new Phrase("REGISTRO DE PRUEBAS DINAMICAS A MOTOR\nELECTRICO DE INDUCCIÓN",
                                                        titleFont));
                        titel.setRowspan(2);
                        titel.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                        titel.setPadding(8);

                        PdfPCell cliente = new PdfPCell(new Phrase("CEMENTO ARGOS, S.A", titleFont));
                        cliente.setRowspan(2);
                        cliente.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                        cliente.setPadding(8);

                        tableProtoclo.addCell(imgCopower);
                        tableProtoclo.addCell(titel);
                        tableProtoclo.addCell(cell("Protocolo 01", boldFont));
                        tableProtoclo.addCell(cell("Hoja 1 de 2", boldFont));

                        tableProtoclo.addCell(cell("Código.", normalfont));
                        tableProtoclo.addCell(cell("DFDE234", normalfont));

                        tableProtoclo.addCell(cliente);

                        tableProtoclo.addCell(cell("Ciudad", normalfont));
                        tableProtoclo.addCell(cell("TOLÚ VIEJO", normalfont));

                        tableProtoclo.addCell(cell("Fecha", boldFont));
                        tableProtoclo.addCell(cell("3/25/2025", normalfont));
                        tableProtoclo.setSpacingAfter(20);
                        document.add(tableProtoclo);

                        // document.add(Chunk.NEWLINE);

                        // ==== termina encabezado====
                        // Tabla de infromacion de Motor

                        PdfPTable infotableMotor = new PdfPTable(6);
                        infotableMotor.setWidthPercentage(100);
                        infotableMotor.setSpacingBefore(10);

                        PdfPCell titCell = new PdfPCell(new Phrase("INFORMACION DEL MOTOR", boldFontWhite));
                        titCell.setColspan(6);
                        titCell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                        titCell.setBackgroundColor(new java.awt.Color(0, 40, 95));
                        titCell.setPadding(8);

                        infotableMotor.addCell(titCell);

                        addRow(infotableMotor, "Numero de Serie", "Excel Value", "Potencia [HP]", "Excel Value",
                                        "Velocidad nom. [RPM]", "Excel Value", boldFont, normalfont);
                        addRow(infotableMotor, "Fabricante", "Excel Value", "Corriente nom. [A]", "Excel Value",
                                        "Frecuencia [Hz]",
                                        "Excel Value", boldFont, normalfont);
                        addRow(infotableMotor, "Ubicacion", "Excel Value", "Tension nom. [V]", "Excel Value",
                                        "Tipo de Motor",
                                        "Excel Value", boldFont, normalfont);
                        addRow(infotableMotor, "Nombre", "Excel Value", "Pot. Activa [KW]", "Excel Value",
                                        "Punto de Medida",
                                        "Excel Value", boldFont, normalfont);

                        document.add(infotableMotor);

                        document.add(Chunk.NEWLINE);

                        // Tabla de Tenciones de Linea.
                        PdfPTable tensionesLine = new PdfPTable(5);
                        tensionesLine.setWidthPercentage(100);
                        tensionesLine.setSpacingBefore(10);
                        // Creacion del Titulo centrado a la tabla tenciones de linea y Configuración.
                        PdfPCell titleTencion = new PdfPCell(new Phrase("TENSIONES DE LINEA", boldFontWhite));
                        titleTencion.setColspan(5);
                        titleTencion.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                        titleTencion.setBackgroundColor(new java.awt.Color(0, 40, 95));
                        titleTencion.setPadding(8);

                        // Añadido a la tabla tenciones de libre.
                        tensionesLine.addCell(titleTencion);

                        titleRows(tensionesLine, "", "Activa [KW]", "Reactiva [kVAr]", "Aparente [kVA]", "PF",
                                        boldFont);
                        rowsBacis(tensionesLine, "Fase A - Fase B", "Excel Value", "Excel Value", "Excel Value",
                                        "Excel Value", boldFont, normalfont);
                        rowsBacis(tensionesLine, "Fase B - Fase C", "Excel Value", "Excel Value", "Excel Value",
                                        "Excel Value", boldFont, normalfont);
                        rowsBacis(tensionesLine, "Fase C - Fase A", "Excel Value", "Excel Value", "Excel Value",
                                        "Excel Value", boldFont, normalfont);
                        rowsBacis(tensionesLine, "Promedio", "Excel Value", " ", "", "", boldFont, normalfont);
                        rowsBacis(tensionesLine, "% Desbalance", "Excel Value", " ", "", "", boldFont, normalfont);
                        document.add(tensionesLine);

                        // Espacio entre Tablas
                        document.add(Chunk.NEWLINE);

                        // Tabla de Tensiones de Fase
                        PdfPTable tensionesFase = new PdfPTable(5);
                        tensionesFase.setWidthPercentage(100);
                        tensionesFase.setSpacingBefore(10);
                        // Creacion del Titulo centrado a la tabla tenciones de linea y Configuración.
                        PdfPCell titleFase = new PdfPCell(new Phrase("TENSIONES DE FASE", boldFontWhite));
                        titleFase.setColspan(6);
                        titleFase.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                        titleFase.setBackgroundColor(new java.awt.Color(0, 40, 95));
                        titleFase.setPadding(8);

                        // Añadido a la tabla tenciones de libre.
                        tensionesFase.addCell(titleFase);

                        titleRows(tensionesFase, "", "Activa [KW]", "Reactiva [kVAr]", "Aparente [kVA]", "PF",
                                        boldFont);
                        rowsBacis(tensionesFase, "Fase A - Fase B", "Excel Value", "Excel Value", "Excel Value",
                                        "Excel Value", boldFont, normalfont);
                        rowsBacis(tensionesFase, "Fase B - Fase C", "Excel Value", "Excel Value", "Excel Value",
                                        "Excel Value", boldFont, normalfont);
                        rowsBacis(tensionesFase, "Fase C - Fase A", "Excel Value", "Excel Value", "Excel Value",
                                        "Excel Value", boldFont, normalfont);
                        rowsBacis(tensionesFase, "Promedio", "Excel Value", " ", "", "", boldFont, normalfont);
                        rowsBacis(tensionesFase, "% Desbalance", "Excel Value", " ", "", "", boldFont, normalfont);
                        document.add(tensionesFase);

                        // Espacio entre Tablas
                        document.add(Chunk.NEWLINE);

                        // Tabla de Corriente de Linea
                        PdfPTable corrientesLinea = new PdfPTable(5);
                        corrientesLinea.setWidthPercentage(100);
                        corrientesLinea.setSpacingBefore(10);
                        // Creacion del Titulo centrado a la tabla Corriente de Linea y Configuración.
                        PdfPCell titleTencionesLinea = new PdfPCell(new Phrase("CORRIENTES DE LINEA", boldFontWhite));
                        titleTencionesLinea.setColspan(6);
                        titleTencionesLinea.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                        titleTencionesLinea.setBackgroundColor(new java.awt.Color(0, 40, 95));
                        titleTencionesLinea.setPadding(8);

                        // Añadido a la tabla Corriente de Linea
                        corrientesLinea.addCell(titleTencionesLinea);

                        titleRows(corrientesLinea, "", "Activa [KW]", "Reactiva [kVAr]", "Aparente [kVA]", "PF",
                                        boldFont);
                        rowsBacis(corrientesLinea, "Fase A - Fase B", "Excel Value", "Excel Value", "Excel Value",
                                        "Excel Value", boldFont, normalfont);
                        rowsBacis(corrientesLinea, "Fase B - Fase C", "Excel Value", "Excel Value", "Excel Value",
                                        "Excel Value", boldFont, normalfont);
                        rowsBacis(corrientesLinea, "Fase C - Fase A", "Excel Value", "Excel Value", "Excel Value",
                                        "Excel Value", boldFont, normalfont);
                        rowsBacis(corrientesLinea, "Promedio", "Excel Value", " ", "", "", boldFont, normalfont);
                        rowsBacis(corrientesLinea, "% Desbalance", "Excel Value", " ", "", "", boldFont, normalfont);
                        document.add(corrientesLinea);

                        // Espacio entre Tablas
                        document.add(Chunk.NEWLINE);

                        // Tabla de Corriente de Linea
                        PdfPTable potenciTable = new PdfPTable(5);
                        potenciTable.setWidthPercentage(100);
                        potenciTable.setSpacingBefore(10);
                        // Creacion del Titulo centrado a la tabla Corriente de Linea y Configuración.
                        PdfPCell titlePotencia = new PdfPCell(new Phrase("POTENCIA", boldFontWhite));
                        titlePotencia.setColspan(6);
                        titlePotencia.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                        titlePotencia.setBackgroundColor(new java.awt.Color(0, 40, 95));
                        titlePotencia.setPadding(8);

                        // Añadido a la tabla Corriente de Linea
                        potenciTable.addCell(titlePotencia);

                        titleRows(potenciTable, "", "Activa [KW]", "Reactiva [kVAr]", "Aparente [kVA]", "PF", boldFont);
                        rowsBacis(potenciTable, "Fase A - Fase B", "Excel Value", "Excel Value", "Excel Value",
                                        "Excel Value", boldFont, normalfont);
                        rowsBacis(potenciTable, "Fase B - Fase C", "Excel Value", "Excel Value", "Excel Value",
                                        "Excel Value", boldFont, normalfont);
                        rowsBacis(potenciTable, "Fase C - Fase A", "Excel Value", "Excel Value", "Excel Value",
                                        "Excel Value", boldFont, normalfont);
                        rowsBacis(potenciTable, "Promedio", "Excel Value", " ", "", "", boldFont, normalfont);
                        document.add(potenciTable);

                        document.close();
                        return pdOutput;
        
      } catch (Exception e) {
        throw new UnsupportedOperationException("Unimplemented method 'generatePDf'");
      }
    }

}
