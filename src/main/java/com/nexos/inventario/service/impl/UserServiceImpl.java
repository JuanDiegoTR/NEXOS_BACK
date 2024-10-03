package com.nexos.inventario.service.impl;

import com.nexos.inventario.dto.UserRequestDto;
import com.nexos.inventario.dto.UserResponseDto;
import com.nexos.inventario.entity.UserEntity;
import com.nexos.inventario.exception.CustomHttpException;
import com.nexos.inventario.repository.UserRepository;
import com.nexos.inventario.service.IChargeService;
import com.nexos.inventario.service.IUserService;
import com.nexos.inventario.utils.MessangeConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final IChargeService chargeService;
    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) throws CustomHttpException {
        if (Objects.isNull(userRequestDto)){
            throw new CustomHttpException(MessangeConstants.NULL_INPUT_DATA,HttpStatus.BAD_REQUEST);
        }

        UserEntity user = UserEntity.builder()
                .name(userRequestDto.getName())
                .age(userRequestDto.getAge())
                .entryDate(userRequestDto.getEntryDate())
                .charge(chargeService.findById(userRequestDto.getCargoId()))
                .status(true)
                .build();

        userRepository.save(user);

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .cargoName(user.getCharge().getName())
                .entryDate(user.getEntryDate().toString())
                .build();
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) throws CustomHttpException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new CustomHttpException(MessangeConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (Objects.isNull(userRequestDto)) {
            throw new CustomHttpException(MessangeConstants.NULL_INPUT_DATA, HttpStatus.BAD_REQUEST);
        }

        user.setName(userRequestDto.getName());
        user.setAge(userRequestDto.getAge());
        user.setEntryDate(userRequestDto.getEntryDate());
        user.setCharge(chargeService.findById(userRequestDto.getCargoId()));

        userRepository.save(user);

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .cargoName(user.getCharge().getName())
                .entryDate(user.getEntryDate().toString())
                .build();

    }

    @Override
    public UserResponseDto getUserById(Long id) throws CustomHttpException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new CustomHttpException(MessangeConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .cargoName(user.getCharge().getName())
                .entryDate(user.getEntryDate().toString())
                .build();
    }

    @Override
    public void deleteUser(Long id) throws CustomHttpException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new CustomHttpException(MessangeConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        user.setStatus(false);
        userRepository.save(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<UserEntity> users = userRepository.findAllByStatus(true);

        return users.stream()
                .map(user -> UserResponseDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .age(user.getAge())
                        .cargoName(user.getCharge().getName())
                        .entryDate(user.getEntryDate().toString())
                        .build())
                .collect(Collectors.toList());
    }


}
