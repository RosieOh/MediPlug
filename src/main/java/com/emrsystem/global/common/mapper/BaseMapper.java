package com.emrsystem.global.common.mapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 기존 static from() 메서드 패턴을 지원하는 기본 매퍼 유틸리티
 * 기존 코드와의 호환성을 위해 제공됩니다.
 */
public class BaseMapper {

    /**
     * Entity를 DTO로 변환합니다.
     * DTO 클래스에 static from(Entity) 메서드가 있는 경우 사용합니다.
     *
     * @param entity 변환할 Entity
     * @param mapper DTO의 static from 메서드를 참조하는 함수
     * @param <E>    Entity 타입
     * @param <D>    DTO 타입
     * @return 변환된 DTO
     */
    public static <E, D> D from(E entity, Function<E, D> mapper) {
        if (entity == null) {
            return null;
        }
        return mapper.apply(entity);
    }

    /**
     * Entity 리스트를 DTO 리스트로 변환합니다.
     * DTO 클래스에 static from(Entity) 메서드가 있는 경우 사용합니다.
     *
     * @param entities 변환할 Entity 리스트
     * @param mapper   DTO의 static from 메서드를 참조하는 함수
     * @param <E>      Entity 타입
     * @param <D>      DTO 타입
     * @return 변환된 DTO 리스트
     */
    public static <E, D> List<D> fromList(List<E> entities, Function<E, D> mapper) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
}

