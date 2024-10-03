package com.nexos.inventario.service.impl;

import com.nexos.inventario.dto.UserRequestDto;
import com.nexos.inventario.dto.UserResponseDto;
import com.nexos.inventario.entity.ChargeEntity;
import com.nexos.inventario.entity.UserEntity;
import com.nexos.inventario.exception.CustomHttpException;
import com.nexos.inventario.repository.UserRepository;
import com.nexos.inventario.service.IChargeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    IChargeService chargeService;

    @InjectMocks
     UserServiceImpl userService;

    @Test
    void createUser() throws CustomHttpException {
        UserRequestDto requestDto = UserRequestDto.builder()
                .name("John Doe")
                .age(30)
                .entryDate(LocalDate.now())
                .cargoId(1L)
                .build();

        ChargeEntity charge = new ChargeEntity(1L, "Asesor de ventas");
        UserEntity savedUser = UserEntity.builder()
                .id(1L)
                .name("John Doe")
                .age(30)
                .entryDate(LocalDate.now())
                .charge(charge)
                .status(true)
                .build();

        when(chargeService.findById(requestDto.getCargoId())).thenReturn(charge);
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        UserResponseDto result = userService.createUser(requestDto);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("Asesor de ventas", result.getCargoName());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void updateUser() throws CustomHttpException {
        Long userId = 1L;
        UserRequestDto requestDto = UserRequestDto.builder()
                .name("Jane Doe")
                .age(25)
                .entryDate(LocalDate.now())
                .cargoId(2L)
                .build();

        ChargeEntity charge = new ChargeEntity(2L, "Administrador");
        UserEntity existingUser = UserEntity.builder()
                .id(userId)
                .name("John Doe")
                .age(30)
                .entryDate(LocalDate.now())
                .charge(new ChargeEntity(1L, "Asesor de ventas"))
                .status(true)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(chargeService.findById(requestDto.getCargoId())).thenReturn(charge);
        when(userRepository.save(any(UserEntity.class))).thenReturn(existingUser);

        UserResponseDto result = userService.updateUser(userId, requestDto);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("Administrador", result.getCargoName());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void getUserById() throws CustomHttpException {
        Long userId = 1L;
        UserEntity user = UserEntity.builder()
                .id(userId)
                .name("John Doe")
                .age(30)
                .entryDate(LocalDate.now())
                .charge(new ChargeEntity(1L, "Asesor de ventas"))
                .status(true)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("Asesor de ventas", result.getCargoName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void deleteUser() throws CustomHttpException {
        Long userId = 1L;
        UserEntity user = UserEntity.builder()
                .id(userId)
                .name("John Doe")
                .status(true)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.deleteUser(userId);
        assertFalse(user.getStatus());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getAllUsers() {
        List<UserEntity> mockUsers = Arrays.asList(
                UserEntity.builder()
                        .id(1L)
                        .name("John Doe")
                        .age(30)
                        .charge(new ChargeEntity(1L, "Asesor de ventas"))
                        .entryDate(LocalDate.now())
                        .status(true)
                        .build(),
                UserEntity.builder()
                        .id(2L)
                        .name("Jane Doe")
                        .age(25)
                        .charge(new ChargeEntity(2L, "Administrador"))
                        .entryDate(LocalDate.now())
                        .status(true)
                        .build()
        );

        when(userRepository.findAllByStatus(true)).thenReturn(mockUsers);

        List<UserResponseDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
        verify(userRepository, times(1)).findAllByStatus(true);
    }
}