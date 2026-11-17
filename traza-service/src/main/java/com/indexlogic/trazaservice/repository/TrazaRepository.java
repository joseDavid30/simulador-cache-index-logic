package com.indexlogic.trazaservice.repository;

import com.indexlogic.trazaservice.entity.TrazaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrazaRepository extends JpaRepository<TrazaEntity, Long> {
    // Aquí podrás agregar consultas personalizadas más adelante si lo necesitas
}