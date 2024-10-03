package com.nexos.inventario.controller;

import com.nexos.inventario.dto.ChargeRequestDto;
import com.nexos.inventario.dto.ChargeResponseDto;
import com.nexos.inventario.service.IChargeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/charges")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Cargo", description = "Controlador de cargo")
public class ChargeController {

    private final IChargeService chargeService;

    @Operation(summary = "Lista de cargo")
    @GetMapping
    public ResponseEntity<List<ChargeResponseDto>> findAllCharges() {
        return ResponseEntity.ok(chargeService.findAll());
    }

    @Operation(summary = "Crear cargo")
    @PostMapping
    public ResponseEntity<ChargeResponseDto> createCharge(@Valid @RequestBody ChargeRequestDto chargeRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chargeService.createCharge(chargeRequestDto));
    }
}
