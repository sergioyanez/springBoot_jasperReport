package com.example.springbootexample.config;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class JasperReportConfig {

    @Bean
    public JasperReport jasperReport() {
        // Aquí configura y carga tu informe Jasper y devuelve una instancia de JasperReport
        // Puedes cargar el informe desde un archivo JRXML o directamente desde un archivo .jasper

        // Ejemplo:
        JasperReport report = null;
        try {
            String reportPath = "src/main/resources/reportes/ReporteProductos.jasper"; // Ruta al archivo del informe
            InputStream inputStream = getClass().getResourceAsStream(reportPath);
            report = JasperCompileManager.compileReport(inputStream);
        } catch (JRException e) {
            // Maneja excepciones apropiadamente
        }

        return report;
    }

}
