package com.geli.warehouse.repository;

import com.geli.warehouse.model.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemsRepository extends JpaRepository<Items, UUID> {

    @Query("SELECT i.productCode FROM Items i WHERE i.productCode LIKE CONCAT(:prefix, '-%') ORDER BY i.productCode DESC LIMIT 1")
    String findLastProductCodeByPrefix(@Param("prefix") String prefix);

    @Modifying
    @Query("UPDATE Items i SET i.deletedAt = CURRENT_TIMESTAMP WHERE i.id = :id")
    void softDeleteById(@Param("id") UUID id);
}
