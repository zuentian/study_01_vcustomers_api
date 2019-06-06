package com.example.zuer02.dao;

import com.example.zuer02.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogDao {

    int insertBlog(Blog blog);

    List<Blog> selectBlog();

    Blog selectBlogByBlogId(String blogId);

    int deleteBlogByBlogId(String blogId);

    int updateBlogById(Blog blog);
}
