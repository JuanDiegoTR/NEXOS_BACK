package com.nexos.inventario.repository;

import com.nexos.inventario.entity.ChargeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeRepository extends JpaRepository<ChargeEntity,Long> {
}
