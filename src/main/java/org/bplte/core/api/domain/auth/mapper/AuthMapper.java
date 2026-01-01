package org.bplte.core.api.domain.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.bplte.core.api.domain.user.entity.UserEntity;

@Mapper
public interface AuthMapper {
	int userInsert(UserEntity user);
}