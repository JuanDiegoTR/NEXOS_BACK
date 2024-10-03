package com.nexos.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDto {
    private Long id;
    private String productName;
    private Integer quantity;
    private String entryDate;
    private String creatorUserName;
    private String editorUserName;
    private Boolean status;
}
