package com.nexos.inventario.service;

import com.nexos.inventario.dto.UserRequestDto;
import com.nexos.inventario.dto.UserResponseDto;
import com.nexos.inventario.exception.CustomHttpException;

import java.util.List;

public interface IUserService {

    /**
     * Crear un usuario
     * @param userRequestDto Data del usuario
     * @return dto
     * @throws CustomHttpException error
     */
    UserResponseDto createUser(UserRequestDto userRequestDto) throws CustomHttpException;

    /**
     * Actualizar un usuario
     * @param id identificador del usuario
     * @param userRequestDto Data del usuario a actualizar
     * @return dto
     * @throws CustomHttpException error
     */
    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) throws CustomHttpException;

    /**
     * Consulta usuairo
     * @param id identificador unico
     * @return dto
     * @throws CustomHttpException error
     */
    UserResponseDto getUserById(Long id) throws CustomHttpException;

    /**
     * Eliminar usuario
     * @param id identificador unico
     * @throws CustomHttpException error
     */
    void deleteUser(Long id) throws CustomHttpException;

    /**
     * Todos los usuairos
     * @return lista de usuarios
     */
    List<UserResponseDto> getAllUsers();
}
