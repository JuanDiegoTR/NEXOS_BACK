package com.nexos.inventario.service.impl;

import com.nexos.inventario.dto.InventoryRequestDto;
import com.nexos.inventario.dto.InventoryResponseDto;
import com.nexos.inventario.entity.InventoryEntity;
import com.nexos.inventario.entity.UserEntity;
import com.nexos.inventario.exception.CustomHttpException;
import com.nexos.inventario.repository.InventoryRepository;
import com.nexos.inventario.repository.UserRepository;
import com.nexos.inventario.service.IInventoryService;
import com.nexos.inventario.utils.MessangeConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements IInventoryService {

    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    @Override
    public InventoryResponseDto createInventory(InventoryRequestDto inventoryRequestDto) throws CustomHttpException {

        if (inventoryRepository.existsByProductName(inventoryRequestDto.getProductName())) {
            throw new CustomHttpException(MessangeConstants.PRODUCT_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        UserEntity user = userRepository.findById(inventoryRequestDto.getUserId())
                .orElseThrow(() -> new CustomHttpException(MessangeConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        InventoryEntity inventory = InventoryEntity.builder()
                .productName(inventoryRequestDto.getProductName())
                .quantity(inventoryRequestDto.getQuantity())
                .entryDate(inventoryRequestDto.getEntryDate())
                .status(Boolean.TRUE)
                .userCreator(user)
                .build();

        inventoryRepository.save(inventory);

        return InventoryResponseDto.builder()
                .id(inventory.getId())
                .productName(inventory.getProductName())
                .quantity(inventory.getQuantity())
                .entryDate(inventory.getEntryDate().toString())
                .creatorUserName(inventory.getUserCreator().getName())
                .status(true)
                .build();
    }

    @Override
    public InventoryResponseDto updateInventory(Long id, InventoryRequestDto inventoryRequestDto) throws CustomHttpException {
        InventoryEntity inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new CustomHttpException(MessangeConstants.INVENTORY_NOT_FOUND, HttpStatus.NOT_FOUND));

        UserEntity userEditor = userRepository.findById(inventoryRequestDto.getUserId())
                .orElseThrow(() -> new CustomHttpException(MessangeConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        inventory.setProductName(inventoryRequestDto.getProductName());
        inventory.setQuantity(inventoryRequestDto.getQuantity());
        inventory.setEntryDate(inventoryRequestDto.getEntryDate());
        inventory.setUserEditor(userEditor);
        inventory.setEditionDate(LocalDate.now());

        inventoryRepository.save(inventory);

        return InventoryResponseDto.builder()
                .id(inventory.getId())
                .productName(inventory.getProductName())
                .quantity(inventory.getQuantity())
                .entryDate(inventory.getEntryDate().toString())
                .creatorUserName(inventory.getUserCreator().getName())
                .editorUserName(inventory.getUserEditor().getName())
                .status(true)
                .build();
    }

    @Override
    public InventoryResponseDto getInventoryById(Long id) throws CustomHttpException {
        InventoryEntity inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new CustomHttpException(MessangeConstants.INVENTORY_NOT_FOUND, HttpStatus.NOT_FOUND));

        return InventoryResponseDto.builder()
                .id(inventory.getId())
                .productName(inventory.getProductName())
                .quantity(inventory.getQuantity())
                .entryDate(inventory.getEntryDate().toString())
                .creatorUserName(inventory.getUserCreator().getName())
                .editorUserName(Objects.nonNull(inventory.getUserEditor()) ? inventory.getUserEditor().getName() : null)
                .status(inventory.getStatus())
                .build();
    }

    @Override
    public void deleteInventory(Long id, Long userId) throws CustomHttpException {
        InventoryEntity inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new CustomHttpException(MessangeConstants.INVENTORY_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (!inventory.getUserCreator().getId().equals(userId)) {
            throw new CustomHttpException(MessangeConstants.USER_NOT_AUTHORIZED_TO_DELETE_INVENTORY, HttpStatus.FORBIDDEN);
        }

        inventory.setStatus(false);
        inventoryRepository.save(inventory);
    }

    @Override
    public List<InventoryResponseDto> getAllActiveInventory() {
        List<InventoryEntity> inventories = inventoryRepository.findAllByStatus(true);

        return inventories.stream()
                .map(inventory -> InventoryResponseDto.builder()
                        .id(inventory.getId())
                        .productName(inventory.getProductName())
                        .quantity(inventory.getQuantity())
                        .entryDate(inventory.getEntryDate().toString())
                        .creatorUserName(inventory.getUserCreator().getName())
                        .status(inventory.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponseDto> searchInventory(String productName, Long userId, LocalDate entryDate) {
        List<InventoryEntity> inventories = inventoryRepository.searchByFilters(productName, userId, entryDate);

        return inventories.stream()
                .map(inventory -> InventoryResponseDto.builder()
                        .id(inventory.getId())
                        .productName(inventory.getProductName())
                        .quantity(inventory.getQuantity())
                        .entryDate(inventory.getEntryDate().toString())
                        .creatorUserName(inventory.getUserCreator().getName())
                        .status(true)
                        .build())
                .collect(Collectors.toList());
    }
}
