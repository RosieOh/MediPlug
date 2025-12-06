package com.emrsystem.global.common.mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity와 DTO 간 변환을 위한 유틸리티 클래스
 */
public class MapperUtils {

    /**
     * Entity 리스트를 DTO 리스트로 변환합니다.
     *
     * @param entities 변환할 Entity 리스트
     * @param mapper   변환에 사용할 매퍼
     * @param <E>      Entity 타입
     * @param <D>      DTO 타입
     * @return 변환된 DTO 리스트
     */
    public static <E, D> List<D> toDtoList(List<E> entities, EntityMapper<E, D> mapper) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * DTO 리스트를 Entity 리스트로 변환합니다.
     *
     * @param dtos   변환할 DTO 리스트
     * @param mapper 변환에 사용할 매퍼
     * @param <E>    Entity 타입
     * @param <D>    DTO 타입
     * @return 변환된 Entity 리스트
     */
    public static <E, D> List<E> toEntityList(List<D> dtos, EntityMapper<E, D> mapper) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Entity를 DTO로 변환합니다 (null-safe).
     *
     * @param entity 변환할 Entity
     * @param mapper  변환에 사용할 매퍼
     * @param <E>     Entity 타입
     * @param <D>     DTO 타입
     * @return 변환된 DTO (entity가 null이면 null 반환)
     */
    public static <E, D> D toDto(E entity, EntityMapper<E, D> mapper) {
        if (entity == null) {
            return null;
        }
        return mapper.toDto(entity);
    }

    /**
     * DTO를 Entity로 변환합니다 (null-safe).
     *
     * @param dto    변환할 DTO
     * @param mapper 변환에 사용할 매퍼
     * @param <E>    Entity 타입
     * @param <D>    DTO 타입
     * @return 변환된 Entity (dto가 null이면 null 반환)
     */
    public static <E, D> E toEntity(D dto, EntityMapper<E, D> mapper) {
        if (dto == null) {
            return null;
        }
        return mapper.toEntity(dto);
    }
}

