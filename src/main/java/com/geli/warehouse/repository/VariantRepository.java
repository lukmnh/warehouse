package com.geli.warehouse.repository;

import com.geli.warehouse.model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VariantRepository extends JpaRepository<Variant, UUID> {

    @Query("SELECT v FROM Variant v WHERE v.item.id = :itemId AND v.deletedAt IS NULL")
    List<Variant> findActiveByItemId(@Param("itemId") UUID itemId);

    boolean existsByItemIdAndVariantNameAndVariantValue(UUID itemId, String name, String value);

    @Modifying
    @Query("UPDATE Variant v SET v.deletedAt = CURRENT_TIMESTAMP WHERE v.id = :id")
    void softDeleteById(@Param("id") UUID id);
}
