package com.nexos.inventario.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChargeResponseDto {
    private Long id;
    private String name;
}
