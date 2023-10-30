package com.example.springbootexample.web.rest.cliente;

import com.example.springbootexample.service.ClienteService;
import com.example.springbootexample.service.dto.cliente.request.ClienteRequestDTO;
import com.example.springbootexample.service.dto.cliente.response.ClienteResponseDTO;
import com.example.springbootexample.service.dto.producto.response.ProductoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("api/clientes")
@RequiredArgsConstructor
public class ClienteResource {
    private static final String REPORT_PATH = "reportes/ReporteClientes.jrxml";
    private final ClienteService clienteService;

    @GetMapping("")
    public List<ClienteResponseDTO> findAll(){
        return this.clienteService.findAll();
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO findById( @PathVariable Long id ){
        return this.clienteService.findById( id );
    }

    @GetMapping("/search")
    public List<ClienteResponseDTO> findById( ClienteRequestDTO request ){
        return this.clienteService.search( request );
    }

    @GetMapping("/bests")
    public List<ClienteResponseDTO> bestClients(){
        return this.clienteService.bestClients();
    }

    @PostMapping("")
    public ResponseEntity<ClienteResponseDTO> save( @RequestBody @Valid ClienteRequestDTO request ){
        final var result = this.clienteService.save( request );
        return ResponseEntity.accepted().body( result );
    }

    @GetMapping("/lucky")
    public ClienteResponseDTO luckyClient(){
        return this.clienteService.luckyClient();
    }

    @GetMapping("/report/")
    public ResponseEntity<Resource> print() {
        try {
            List<ClienteResponseDTO> outCuenta = clienteService.findAll();
            if (null != outCuenta) {

                // Convierte el informe en un flujo de bytes
                byte[] reportBytes = generateReportBytes(outCuenta);

                // Crea un ByteArrayResource a partir del flujo de bytes
                ByteArrayResource resource = new ByteArrayResource(reportBytes);

                return ResponseEntity.ok()

                        .header("Content-Disposition", String.format("attachment; filename=\"reporte de clientes.pdf\""))
                        .contentType(MediaType.parseMediaType("application/force-download"))
                        .body(new ByteArrayResource(reportBytes));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
        }
    }

    private byte[] generateReportBytes(List<ClienteResponseDTO> clientes) {
        // Ruta al archivo JRXML del informe
        String reportPath = REPORT_PATH;

        try {
            // Compila el archivo JRXML en un informe Jasper
            JasperReport jasperReport = JasperCompileManager.compileReport(ResourceUtils.getFile(reportPath).getAbsolutePath());

            // Crea una fuente de datos (DataSource) a partir de la lista de productos
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(clientes);

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
