package com.nexos.inventario.service;

import com.nexos.inventario.dto.InventoryRequestDto;
import com.nexos.inventario.dto.InventoryResponseDto;
import com.nexos.inventario.exception.CustomHttpException;

import java.time.LocalDate;
import java.util.List;

public interface IInventoryService {

    /**
     * Método para crear un nuevo producto en el inventario
     * @param inventoryRequestDto Objeto con los parametros de consulta
     * @return dto
     * @throws CustomHttpException error
     */
    InventoryResponseDto createInventory(InventoryRequestDto inventoryRequestDto) throws CustomHttpException;

    /**
     * Método para actualizar un producto existente en el inventario
     * @param id identificador unico
     * @param inventoryRequestDto Objeto con los parametros a actualizar
     * @return dto
     * @throws CustomHttpException error
     */
    InventoryResponseDto updateInventory(Long id, InventoryRequestDto inventoryRequestDto) throws CustomHttpException;

    /**
     * Método para obtener los detalles de un producto por su ID
     * @param id identificador unico
     * @return dto
     * @throws CustomHttpException error
     */
    InventoryResponseDto getInventoryById(Long id) throws CustomHttpException;

    /**
     * Método para eliminar lógicamente un producto del inventario (cambiar su estado)
     * @param id identiificador unico
     * @param userId identificador unico del usuario
     * @throws CustomHttpException error
     */
    void deleteInventory(Long id,Long userId) throws CustomHttpException;

    /**
     * Método para listar todos los productos activos en el inventario
     * @return lista de inventario activo
     */
    List<InventoryResponseDto> getAllActiveInventory();

    /**
     * Método para buscar productos por filtros (nombre, usuario o fecha)
     * @param productName nombre del producto
     * @param userId identificador del usuario
     * @param entryDate fecha de consulta
     * @return lista de inventario
     */
    List<InventoryResponseDto> searchInventory(String productName, Long userId, LocalDate entryDate);
}