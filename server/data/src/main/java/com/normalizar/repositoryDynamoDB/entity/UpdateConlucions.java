package com.normalizar.repositoryDynamoDB.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateConlucions {
    private String tenant_id;
    private String report_id;
    private List<String> concluciones;
}   
