package com.nexos.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexos.inventario.dto.ChargeRequestDto;
import com.nexos.inventario.dto.ChargeResponseDto;
import com.nexos.inventario.service.IChargeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChargeController.class)
class ChargeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IChargeService chargeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAllCharges() throws Exception {
        List<ChargeResponseDto> chargeList = Arrays.asList(
                ChargeResponseDto.builder().id(1L).name("Asesor de ventas").build(),
                ChargeResponseDto.builder().id(2L).name("Administrador").build()
        );

        when(chargeService.findAll()).thenReturn(chargeList);

        mockMvc.perform(get("/charges")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Asesor de ventas"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Administrador"));
    }

    @Test
    void createCharge() throws Exception {
        ChargeRequestDto requestDto = ChargeRequestDto.builder()
                .name("Gerente de Marketing")
                .build();

        ChargeResponseDto responseDto = ChargeResponseDto.builder()
                .id(3L)
                .name("Gerente de Marketing")
                .build();

        when(chargeService.createCharge(any(ChargeRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/charges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("Gerente de Marketing"));
    }
}