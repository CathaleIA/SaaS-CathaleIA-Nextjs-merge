package com.normalizar.utility;

import java.io.IOException;
import java.util.Map;

import com.normalizar.domain.Report;

public interface ImappingUseCase {
    Map<String, Object> mapJsonToThymeleafModel(String jsonString, Report report) throws IOException;
}
