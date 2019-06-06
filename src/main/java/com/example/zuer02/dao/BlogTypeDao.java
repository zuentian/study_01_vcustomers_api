package com.example.zuer02.dao;

import com.example.zuer02.entity.BlogType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogTypeDao {

    int insertBlogType(BlogType blogType);

    List<BlogType> selectBlogTypeByBlogId(String blogId);

    int deleteBlogTypeByBlogId(String blogId);
}
