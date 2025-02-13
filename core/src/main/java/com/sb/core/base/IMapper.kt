package com.sb.core.base

interface IMapper<Dto, Entity> {

    fun mapToEntity(dto: Dto): Entity

    fun mapToDto(entity: Entity): Dto

}