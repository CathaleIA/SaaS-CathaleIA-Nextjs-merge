package com.normalizar.thymeleaRender;

import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


public class ThymeleaRenderTeamplate {
    private static final TemplateEngine engine;

    static {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/"); // Asegúrate que estén en src/main/resources/templates
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");

        engine = new TemplateEngine();
        engine.setTemplateResolver(templateResolver);
    }

    public static String render(String template, Map<String, Object> activo){
        Context context = new Context();
        context.setVariables(activo);
        return engine.process(template, context);
    }
}
