package com.example.springbootexample.web.rest.producto;

import com.example.springbootexample.domain.Producto;
import com.example.springbootexample.service.ProductoService;
import com.example.springbootexample.service.dto.cliente.request.ClienteRequestDTO;
import com.example.springbootexample.service.dto.cliente.response.ClienteResponseDTO;
import com.example.springbootexample.service.dto.producto.response.ProductoResponseDTO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("api/productos")
@RequiredArgsConstructor
public class ProductoResource {

    private static final String REPORT_PATH = "reportes/ReporteProductos.jrxml";
    private final ProductoService productoService;

    @GetMapping("")
    public List<ProductoResponseDTO> findAll() {
        return this.productoService.findAll();
    }


    @GetMapping("/{id}")
    public ProductoResponseDTO findById(@PathVariable Long id) {
        return this.productoService.findById(id);
    }


    @GetMapping("/top")
    public ResponseEntity<ProductoResponseDTO> obtenerProductoTop() {
        Producto producto = productoService.obtenerProductoTop();

        if (producto != null) {
            ProductoResponseDTO productoResponseDTO = new ProductoResponseDTO(producto);
            return ResponseEntity.ok(productoResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/descuentos")
    public List<ProductoResponseDTO> getProductosConDescuentoBasadoEnDia() {
        // Llama al método del servicio que aplica descuentos basados en el día de la semana
        List<ProductoResponseDTO> productosConDescuento = productoService.applyDiscountsBasedOnDay();
        return productosConDescuento;
    }


    @GetMapping("/report/")
    public ResponseEntity<Resource> print() {
        try {
            List<ProductoResponseDTO> outCuenta = productoService.findAll();
            if (null != outCuenta) {

                // Convierte el informe en un flujo de bytes
                byte[] reportBytes = generateReportBytes(outCuenta);

                // Crea un ByteArrayResource a partir del flujo de bytes
                ByteArrayResource resource = new ByteArrayResource(reportBytes);

                return ResponseEntity.ok()

                        .header("Content-Disposition", String.format("attachment; filename=\"reporte de productos.pdf\""))
                        .contentType(MediaType.parseMediaType("application/force-download"))
                        .body(new ByteArrayResource(reportBytes));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
        }
    }

    private byte[] generateReportBytes(List<ProductoResponseDTO> productos) {
        // Ruta al archivo JRXML del informe
        String reportPath = REPORT_PATH;

        try {
            // Compila el archivo JRXML en un informe Jasper
            JasperReport jasperReport = JasperCompileManager.compileReport(ResourceUtils.getFile(reportPath).getAbsolutePath());

            // Crea una fuente de datos (DataSource) a partir de la lista de productos
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productos);

            // Parámetros (si los tienes)
            java.util.Map<String, Object> params = new java.util.HashMap<>();

            // Llena el informe con los datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // Convierte el informe a bytes
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            // Manejo de errores
            e.printStackTrace();
            return null;
        }
    }
}
