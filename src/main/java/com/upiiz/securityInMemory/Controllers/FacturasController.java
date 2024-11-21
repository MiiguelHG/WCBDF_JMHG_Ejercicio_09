package com.upiiz.securityInMemory.Controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facturas")
public class FacturasController {

    @GetMapping
    public String listarFacturas(){
        return "Listado de facturas";
    }

    @GetMapping("/{id}")
    public String buscarFactura(@PathVariable Long id){
        return "Factura con id: " + id;
    }

    @PostMapping("/guardar")
    public String guardarFactura(){
        return "Factura guardada";
    }

    @PutMapping("/actualizar")
    public String actualizarFactura(){
        return "Factura actualizada";
    }

    @DeleteMapping("/eliminar")
    public String eliminarFactura(){
        return "Factura eliminada";
    }
}
