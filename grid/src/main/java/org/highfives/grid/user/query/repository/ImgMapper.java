package org.highfives.grid.user.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ImgMapper {
    String getProfileImg(int id);
    String getSealImg(int id);
    Map<Integer, String> getProfileImages(@Param("userIdList")List<Integer> userIds);
}
