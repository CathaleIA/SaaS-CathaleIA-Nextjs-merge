package com.normalizar.templateMemori;


import java.util.Base64;

import org.w3c.dom.Element;

import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;

import org.xhtmlrenderer.simple.extend.FormSubmissionListener;


import com.lowagie.text.Image;


public class B64ImageReplacedElementFactory implements ReplacedElementFactory {

    @Override
    public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth,
            int cssHeight) {
       try {
         String url = box.getElement().getAttribute("src");
            if (url != null && url.startsWith("data:image")) {

            // Extraer los datos Base64
            String base64Data = url.substring(url.indexOf(",") + 1);

            // Decodificar los datos Base64 y crear una imagen
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);
            Image image = Image.getInstance(imageBytes);

            // Crear un ITextFSImage desde la imagen
            ITextFSImage fsImage = new ITextFSImage(image);

            // Escalar la imagen según los tamaños CSS
            if (cssWidth > 0 && cssHeight > 0) {
                fsImage.scale(cssWidth, cssHeight);
            }

            // Crear un elemento reemplazado directamente para la imagen
            return new ITextImageElement(fsImage);
        }
       } catch (Exception e) {
         e.printStackTrace();
       }
         return null;
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reset'");
    }

    @Override
    public void remove(Element e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public void setFormSubmissionListener(FormSubmissionListener listener) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFormSubmissionListener'");
    }

}
