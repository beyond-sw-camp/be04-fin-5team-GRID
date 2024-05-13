package org.highfives.grid.user.query.repository;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface ImgMapper {
    String getProfileImg(int id);
    String getSealImg(int id);
}
