package com.emrsystem.global.common.mapper;

/**
 * Entity와 DTO 간 변환을 위한 공통 매퍼 인터페이스
 *
 * @param <E> Entity 타입
 * @param <D> DTO 타입
 */
public interface EntityMapper<E, D> {

    /**
     * Entity를 DTO로 변환합니다.
     *
     * @param entity 변환할 Entity
     * @return 변환된 DTO
     */
    D toDto(E entity);

    /**
     * DTO를 Entity로 변환합니다.
     * 주로 생성 시 사용됩니다.
     *
     * @param dto 변환할 DTO
     * @return 변환된 Entity
     */
    E toEntity(D dto);

    /**
     * DTO의 정보로 기존 Entity를 업데이트합니다.
     * 주로 수정 시 사용됩니다.
     *
     * @param entity 업데이트할 Entity
     * @param dto    업데이트 정보가 담긴 DTO
     */
    void updateEntity(E entity, D dto);
}

