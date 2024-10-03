package com.nexos.inventario.repository;

import com.nexos.inventario.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity,Long> {
    boolean existsByProductName(String productName);

    @Query("SELECT i FROM InventoryEntity i WHERE (:productName IS NULL OR i.productName LIKE %:productName%) AND " +
            "(:userId IS NULL OR i.userCreator.id = :userId) AND " +
            "(:entryDate IS NULL OR i.entryDate = :entryDate)")
    List<InventoryEntity> searchByFilters(@Param("productName") String productName,
                                          @Param("userId") Long userId,
                                          @Param("entryDate") LocalDate entryDate);

    List<InventoryEntity> findAllByStatus(Boolean status);
}
