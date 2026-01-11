package org.bplte.core.api.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.bplte.core.api.domain.user.entity.UserEntity;

@Mapper
public interface UserMapper {
	int userInsert(UserEntity user);
	UserEntity selectUserByUserId(String userId);
}
