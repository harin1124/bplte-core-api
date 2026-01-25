package org.bplte.core.api.domain.auth.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
	int selectCountByUserId(String userId);
	int selectCountByEmail(String userId);
}