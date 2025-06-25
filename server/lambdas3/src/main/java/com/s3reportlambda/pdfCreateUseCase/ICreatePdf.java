package com.s3reportlambda.pdfCreateUseCase;

import java.io.IOException;
import java.io.InputStream;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import java.io.ByteArrayOutputStream;
public interface ICreatePdf {
        // Creacion de celda unica, alineada a la izquierda predefinadamente
    PdfPCell cell(String text, Font font);
    
    // Crear tabla 6 valores con pares de 2(Clave -> Valor)
    void addRow(PdfPTable table,
            String key1, String val1,
            String key2, String val2,
            String key3, String val3,
            Font boldFont, Font normalFont);

    // Escribe 5 filas puedes generar los paacios dejando en Blanco los parametros
    // -> {""} de esta manera. estos son titulos o encabezados en negrilla
    void titleRows(PdfPTable tableName,
            String space,
            String title,
            String title2,
            String title3,
            String title4,
            Font bolFont);

    // Escribe 5 filas pero puede alternar en blodFont y normalFont
    void rowsBacis(PdfPTable tableName,
            String space,
            String title,
            String title2,
            String title3,
            String title4,
            Font bolFont, Font normalfont);


    ByteArrayOutputStream generatePDf( InputStream file)throws IOException, DocumentException ;
}
