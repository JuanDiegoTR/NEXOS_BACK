package com.nexos.inventario.controller;

import com.nexos.inventario.dto.UserRequestDto;
import com.nexos.inventario.dto.UserResponseDto;
import com.nexos.inventario.exception.CustomHttpException;
import com.nexos.inventario.service.IUserService;
import com.nexos.inventario.utils.MessangeConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Usuairo", description = "Controlador de usuario")
public class UserController {

    private final IUserService userService;

    @Operation(summary = "Crear usuario")
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto userRequestDto) throws CustomHttpException {
        return new ResponseEntity<>(userService.createUser(userRequestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar usuario")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable @NotNull(message = MessangeConstants.MANDATORY_ID_PARAMETER) Long id,
                                                      @Valid @RequestBody UserRequestDto userRequestDto) throws CustomHttpException {
        return ResponseEntity.ok(userService.updateUser(id, userRequestDto));
    }

    @Operation(summary = "Usuario por id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(
            @PathVariable @NotNull(message = MessangeConstants.MANDATORY_ID_PARAMETER) Long id) throws CustomHttpException {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable @NotNull(message = MessangeConstants.MANDATORY_ID_PARAMETER) Long id) throws CustomHttpException {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Obtener usuarios")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
