package com.nexos.inventario.controller;

import com.nexos.inventario.dto.InventoryRequestDto;
import com.nexos.inventario.dto.InventoryResponseDto;
import com.nexos.inventario.exception.CustomHttpException;
import com.nexos.inventario.service.IInventoryService;
import com.nexos.inventario.utils.MessangeConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/inventory")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Inventario", description = "Controlador de inventario")
public class InventoryController {

    private final IInventoryService inventoryService;

    @Operation(summary = "Nuevo registro inventario")
    @PostMapping
    public ResponseEntity<InventoryResponseDto> createInventory(@RequestBody @Valid InventoryRequestDto inventoryRequestDto) throws CustomHttpException {
        return new ResponseEntity<>(inventoryService.createInventory(inventoryRequestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar registro inventario")
    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponseDto> updateInventory(
            @PathVariable @NotNull(message = MessangeConstants.MANDATORY_ID_PARAMETER) Long id,
            @RequestBody @Valid InventoryRequestDto inventoryRequestDto) throws CustomHttpException {
        return ResponseEntity.ok(inventoryService.updateInventory(id, inventoryRequestDto));
    }

    @Operation(summary = "Consulta inventario id")
    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponseDto> getInventoryById(
            @PathVariable @NotNull(message = MessangeConstants.MANDATORY_ID_PARAMETER) Long id) throws CustomHttpException {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @Operation(summary = "Eliminar registro inventario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(
            @PathVariable @NotNull(message = MessangeConstants.MANDATORY_ID_PARAMETER) Long id,
            @RequestParam Long userId) throws CustomHttpException {
        inventoryService.deleteInventory(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Consulra inventario activo")
    @GetMapping("/active")
    public ResponseEntity<List<InventoryResponseDto>> getAllActiveInventory() {
        return ResponseEntity.ok(inventoryService.getAllActiveInventory());
    }

    @Operation(summary = "Consulta inventario")
    @GetMapping("/search")
    public ResponseEntity<List<InventoryResponseDto>> searchInventory(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) LocalDate entryDate) {
        return ResponseEntity.ok(inventoryService.searchInventory(productName, userId, entryDate));
    }
}
