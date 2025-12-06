package com.emrsystem.domain.resource.repository;

import com.emrsystem.domain.resource.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByCode(String code);
    Optional<Room> findByCode(String code);
}


