package com.nexos.inventario.service.impl;

import com.nexos.inventario.dto.InventoryRequestDto;
import com.nexos.inventario.dto.InventoryResponseDto;
import com.nexos.inventario.entity.InventoryEntity;
import com.nexos.inventario.entity.UserEntity;
import com.nexos.inventario.exception.CustomHttpException;
import com.nexos.inventario.repository.InventoryRepository;
import com.nexos.inventario.repository.UserRepository;
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
class InventoryServiceImplTest {

    @Mock
    InventoryRepository inventoryRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    InventoryServiceImpl inventoryService;

    @Test
    void createInventory() throws CustomHttpException {
        InventoryRequestDto requestDto = InventoryRequestDto.builder()
                .productName("Laptop")
                .quantity(5)
                .entryDate(LocalDate.now())
                .userId(1L)
                .build();

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("John Doe");

        InventoryEntity inventoryEntity = InventoryEntity.builder()
                .productName(requestDto.getProductName())
                .quantity(requestDto.getQuantity())
                .entryDate(requestDto.getEntryDate())
                .userCreator(user)
                .build();

        when(userRepository.findById(requestDto.getUserId())).thenReturn(Optional.of(user));
        when(inventoryRepository.existsByProductName(requestDto.getProductName())).thenReturn(false);
        when(inventoryRepository.save(any(InventoryEntity.class))).thenReturn(inventoryEntity);

        InventoryResponseDto result = inventoryService.createInventory(requestDto);

        assertNotNull(result);
        assertEquals("Laptop", result.getProductName());
        assertEquals("John Doe", result.getCreatorUserName());
        verify(inventoryRepository, times(1)).save(any(InventoryEntity.class));
    }

    @Test
    void updateInventory() throws CustomHttpException {
        Long inventoryId = 1L;
        InventoryRequestDto requestDto = InventoryRequestDto.builder()
                .productName("Laptop Pro")
                .quantity(10)
                .entryDate(LocalDate.now())
                .userId(2L)
                .build();

        UserEntity userEditor = new UserEntity();
        userEditor.setId(2L);
        userEditor.setName("Jane Doe");

        InventoryEntity existingInventory = InventoryEntity.builder()
                .id(inventoryId)
                .productName("Laptop")
                .quantity(5)
                .entryDate(LocalDate.now())
                .userCreator(new UserEntity(1L, "John Doe", 21, null, LocalDate.now(), true))
                .build();

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(existingInventory));
        when(userRepository.findById(requestDto.getUserId())).thenReturn(Optional.of(userEditor));

        InventoryResponseDto result = inventoryService.updateInventory(inventoryId, requestDto);

        assertNotNull(result);
        assertEquals("Laptop Pro", result.getProductName());
        assertEquals(10, result.getQuantity());
        assertEquals("Jane Doe", result.getEditorUserName());
        verify(inventoryRepository, times(1)).save(existingInventory);
    }

    @Test
    void getInventoryById() throws CustomHttpException {
        Long inventoryId = 1L;
        InventoryEntity inventory = InventoryEntity.builder()
                .id(inventoryId)
                .productName("Laptop")
                .quantity(10)
                .entryDate(LocalDate.now())
                .userCreator(new UserEntity(1L, "John Doe", 21, null, LocalDate.now(), true))
                .userEditor(new UserEntity(2L, "Jane Doe", 22, null, LocalDate.now(), true))
                .build();

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));

        InventoryResponseDto result = inventoryService.getInventoryById(inventoryId);

        assertNotNull(result);
        assertEquals("Laptop", result.getProductName());
        assertEquals("John Doe", result.getCreatorUserName());
        assertEquals("Jane Doe", result.getEditorUserName());
        verify(inventoryRepository, times(1)).findById(inventoryId);
    }

    @Test
    void deleteInventory() throws CustomHttpException {
        Long inventoryId = 1L;
        Long userId = 1L;

        UserEntity user = new UserEntity(1L, "John Doe", 21, null, LocalDate.now(), true);
        InventoryEntity inventory = InventoryEntity.builder()
                .id(inventoryId)
                .productName("Laptop")
                .quantity(10)
                .entryDate(LocalDate.now())
                .userCreator(user)
                .status(true)
                .build();

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));

        inventoryService.deleteInventory(inventoryId, userId);

        verify(inventoryRepository, times(1)).save(inventory);
        assertFalse(inventory.getStatus());
    }

    @Test
    void getAllActiveInventory() {
        List<InventoryEntity> mockInventories = Arrays.asList(
                InventoryEntity.builder()
                        .id(1L)
                        .productName("Laptop")
                        .quantity(10)
                        .entryDate(LocalDate.now())
                        .status(true)
                        .userCreator(new UserEntity(1L, "John Doe", 21, null, LocalDate.now(), true))
                        .build(),
                InventoryEntity.builder()
                        .id(2L)
                        .productName("Phone")
                        .quantity(5)
                        .entryDate(LocalDate.now())
                        .status(true)
                        .userCreator(new UserEntity(1L, "John Doe", 21, null, LocalDate.now(), true))
                        .build()
        );

        when(inventoryRepository.findAllByStatus(true)).thenReturn(mockInventories);
        List<InventoryResponseDto> result = inventoryService.getAllActiveInventory();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getProductName());
        verify(inventoryRepository, times(1)).findAllByStatus(true);
    }

    @Test
    void searchInventory() {
        List<InventoryEntity> mockInventories = Arrays.asList(
                InventoryEntity.builder()
                        .id(1L)
                        .productName("Laptop")
                        .quantity(10)
                        .entryDate(LocalDate.now())
                        .status(true)
                        .userCreator(new UserEntity(1L, "John Doe", 21, null, LocalDate.now(), true))
                        .build()
        );

        when(inventoryRepository.searchByFilters("Laptop", 1L, LocalDate.now())).thenReturn(mockInventories);

        List<InventoryResponseDto> result = inventoryService.searchInventory("Laptop", 1L, LocalDate.now());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getProductName());
        verify(inventoryRepository, times(1)).searchByFilters("Laptop", 1L, LocalDate.now());
    }
}