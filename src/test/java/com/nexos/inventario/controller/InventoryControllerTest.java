package com.nexos.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexos.inventario.dto.InventoryRequestDto;
import com.nexos.inventario.dto.InventoryResponseDto;
import com.nexos.inventario.service.IInventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IInventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private InventoryRequestDto inventoryRequestDto;
    private InventoryResponseDto inventoryResponseDto;

    @BeforeEach
    void setUp() {
        inventoryRequestDto = InventoryRequestDto.builder()
                .productName("Laptop")
                .quantity(10)
                .entryDate(LocalDate.now())
                .userId(1L)
                .build();

        inventoryResponseDto = InventoryResponseDto.builder()
                .id(1L)
                .productName("Laptop")
                .quantity(10)
                .entryDate(LocalDate.now().toString())
                .creatorUserName("John Doe")
                .status(true)
                .build();
    }

    @Test
    void createInventory() throws Exception {
        when(inventoryService.createInventory(any(InventoryRequestDto.class))).thenReturn(inventoryResponseDto);

        mockMvc.perform(post("/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(inventoryResponseDto.getId()))
                .andExpect(jsonPath("$.productName").value(inventoryResponseDto.getProductName()))
                .andExpect(jsonPath("$.quantity").value(inventoryResponseDto.getQuantity()))
                .andExpect(jsonPath("$.creatorUserName").value(inventoryResponseDto.getCreatorUserName()));
    }

    @Test
    void updateInventory() throws Exception {
        when(inventoryService.updateInventory(anyLong(), any(InventoryRequestDto.class))).thenReturn(inventoryResponseDto);

        mockMvc.perform(put("/inventory/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(inventoryResponseDto.getId()))
                .andExpect(jsonPath("$.productName").value(inventoryResponseDto.getProductName()));
    }

    @Test
    void getInventoryById() throws Exception {
        when(inventoryService.getInventoryById(anyLong())).thenReturn(inventoryResponseDto);

        mockMvc.perform(get("/inventory/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(inventoryResponseDto.getId()))
                .andExpect(jsonPath("$.productName").value(inventoryResponseDto.getProductName()));
    }

    @Test
    void deleteInventory() throws Exception {
        mockMvc.perform(delete("/inventory/{id}", 1L)
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllActiveInventory() throws Exception {
        List<InventoryResponseDto> activeInventory = Arrays.asList(inventoryResponseDto);

        when(inventoryService.getAllActiveInventory()).thenReturn(activeInventory);

        mockMvc.perform(get("/inventory/active")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(inventoryResponseDto.getId()))
                .andExpect(jsonPath("$[0].productName").value(inventoryResponseDto.getProductName()));
    }

    @Test
    void searchInventory() throws Exception {
        List<InventoryResponseDto> searchedInventory = Arrays.asList(inventoryResponseDto);

        when(inventoryService.searchInventory(any(), any(), any())).thenReturn(searchedInventory);

        mockMvc.perform(get("/inventory/search")
                        .param("productName", "Laptop")
                        .param("userId", "1")
                        .param("entryDate", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(inventoryResponseDto.getId()))
                .andExpect(jsonPath("$[0].productName").value(inventoryResponseDto.getProductName()));
    }
}
