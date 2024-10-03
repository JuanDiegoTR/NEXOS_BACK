package com.nexos.inventario.service.impl;

import com.nexos.inventario.dto.ChargeRequestDto;
import com.nexos.inventario.dto.ChargeResponseDto;
import com.nexos.inventario.entity.ChargeEntity;
import com.nexos.inventario.repository.ChargeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChargeServiceImplTest {

    @Mock
    ChargeRepository chargeRepository;
    @InjectMocks
    ChargeServiceImpl chargeService;

    @Test
    void findById() {
        Long chargeId = 1L;
        ChargeEntity mockCharge = new ChargeEntity(chargeId, "Asesor de ventas");
        when(chargeRepository.findById(chargeId)).thenReturn(Optional.of(mockCharge));
        ChargeEntity result = chargeService.findById(chargeId);
        assertNotNull(result);
        assertEquals(chargeId, result.getId());
        assertEquals("Asesor de ventas", result.getName());
        verify(chargeRepository, times(1)).findById(chargeId);
    }

    @Test
    void findAll() {
        List<ChargeEntity> mockCharges = Arrays.asList(
                new ChargeEntity(1L, "Asesor de ventas"),
                new ChargeEntity(2L, "Administrador"),
                new ChargeEntity(3L, "Soporte")
        );

        when(chargeRepository.findAll()).thenReturn(mockCharges);

        List<ChargeResponseDto> result = chargeService.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Asesor de ventas", result.get(0).getName());

        verify(chargeRepository, times(1)).findAll();
    }

    @Test
    void createCharge() {
        ChargeRequestDto requestDto = ChargeRequestDto.builder()
                .name("Gerente de Marketing")
                .build();

        ChargeEntity savedCharge = new ChargeEntity(1L, "Gerente de Marketing");

        when(chargeRepository.save(any(ChargeEntity.class))).thenReturn(savedCharge);

        ChargeResponseDto result = chargeService.createCharge(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Gerente de Marketing", result.getName());

        verify(chargeRepository, times(1)).save(any(ChargeEntity.class));

    }
}