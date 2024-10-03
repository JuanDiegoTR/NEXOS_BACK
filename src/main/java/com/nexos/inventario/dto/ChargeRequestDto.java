package com.nexos.inventario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChargeRequestDto {

    @NotNull(message = "Charge name cannot be null")
    @Size(min = 1, max = 100, message = "Charge name must be between 1 and 100 characters")
    private String name;

}
