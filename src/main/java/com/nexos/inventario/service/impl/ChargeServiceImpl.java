package com.nexos.inventario.service.impl;

import com.nexos.inventario.dto.ChargeRequestDto;
import com.nexos.inventario.dto.ChargeResponseDto;
import com.nexos.inventario.entity.ChargeEntity;
import com.nexos.inventario.exception.CustomHttpException;
import com.nexos.inventario.repository.ChargeRepository;
import com.nexos.inventario.service.IChargeService;
import com.nexos.inventario.utils.MessangeConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChargeServiceImpl implements IChargeService {

    private final ChargeRepository chargeRepository;
    @Override
    public ChargeEntity findById(Long id) throws CustomHttpException {
        if(Objects.isNull(id)){
            throw new CustomHttpException(MessangeConstants.MANDATORY_ID_PARAMETER,HttpStatus.BAD_REQUEST);
        }

        return chargeRepository.findById(id)
                .orElseThrow(() -> new CustomHttpException(MessangeConstants.CHARGE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public List<ChargeResponseDto> findAll() {
        return chargeRepository.findAll().stream()
                .map(charge -> ChargeResponseDto.builder()
                        .id(charge.getId())
                        .name(charge.getName())
                        .build())
                .collect(Collectors.toList());
    }
    @Override
    public ChargeResponseDto createCharge(ChargeRequestDto chargeRequestDto) {
        ChargeEntity newCharge = ChargeEntity.builder()
                .name(chargeRequestDto.getName())
                .build();

        ChargeEntity savedCharge = chargeRepository.save(newCharge);

        return ChargeResponseDto.builder()
                .id(savedCharge.getId())
                .name(savedCharge.getName())
                .build();
    }

}
