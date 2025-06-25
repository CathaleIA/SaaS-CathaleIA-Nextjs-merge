package com.normalizar.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfRenderRequest {
    private String archivoHtml;
    private String report_id;
    private String tenant_id;
    private String poolUserId;
    private String fileName;

}
