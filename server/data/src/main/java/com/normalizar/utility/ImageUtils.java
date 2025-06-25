package com.normalizar.utility;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.xhtmlrenderer.pdf.ITextFSImage;
import java.awt.image.BufferedImage;
import com.lowagie.text.Image;

public class ImageUtils {
      public static ITextFSImage createITextFSImageFromBase64(String base64Data) {
        try {
            // Decodificar los datos Base64
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // Convertir los bytes en un BufferedImage
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            if (bufferedImage == null) {
                throw new IllegalArgumentException("No se pudo leer la imagen desde los datos Base64.");
            }

            // Convertir el BufferedImage en una imagen iText
            Image iTextImage = Image.getInstance(imageBytes);
            return new ITextFSImage(iTextImage);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el ITextFSImage desde los datos Base64", e);
        }
    }
}
