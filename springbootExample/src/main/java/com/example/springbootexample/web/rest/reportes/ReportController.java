//package com.example.springbootexample.web.rest.reportes;
//
//import com.example.springbootexample.service.ProductoService;
//import com.example.springbootexample.service.dto.producto.response.ProductoResponseDTO;
//import lombok.RequiredArgsConstructor;
//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.view.JasperViewer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("api/reportes")
//@RequiredArgsConstructor
//public class ReportController {
//
//    @Autowired
//    private final JasperReport reporte;
//    @Autowired
//    private final ProductoService productoService;
//
//    @GetMapping("/generateReport")
//    public ModelAndView generateReport() throws JRException {
//        List<ProductoResponseDTO> data = productoService.findAll();
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("dataSource", new JRBeanCollectionDataSource(data));
//
//        JasperPrint reporteLleno = JasperFillManager.fillReport(reporte, parameters, new JREmptyDataSource());
////        JasperPrint reporteLleno = reporte..generarReporteSimplePeliculas();
////        JasperExportManager.exportReportToPdfFile(reporteLleno,"ReportePorRating.pdf");
//        JasperViewer viewer = new JasperViewer(reporteLleno);
//        viewer.setVisible(true);
//
//        return new ModelAndView(new JasperReportsPdfView(), Collections.singletonMap("jasperPrint", reporteLleno));
//    }
//}
