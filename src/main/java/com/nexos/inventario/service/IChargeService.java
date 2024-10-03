package com.nexos.inventario.service;

import com.nexos.inventario.dto.ChargeRequestDto;
import com.nexos.inventario.dto.ChargeResponseDto;
import com.nexos.inventario.entity.ChargeEntity;
import com.nexos.inventario.exception.CustomHttpException;

import java.util.List;

public interface IChargeService {

    /**
     * Metodo para consultar por id
     * @param id identificador unico
     * @return entidad
     * @throws CustomHttpException error
     */
    ChargeEntity findById(Long id) throws CustomHttpException;

    /**
     * Método para listar todos los cargos
     * @return Lista de cargos
     */
    List<ChargeResponseDto> findAll();

    /**
     * Método para crear un nuevo cargo
     * @param chargeRequestDto Objeto con parametros de consulta
     * @return dto
     */
    ChargeResponseDto createCharge(ChargeRequestDto chargeRequestDto);
}
