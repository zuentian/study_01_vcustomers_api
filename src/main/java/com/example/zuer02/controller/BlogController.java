package com.example.zuer02.controller;

import com.example.zuer02.dao.BlogDao;
import com.example.zuer02.dao.BlogTypeDao;
import com.example.zuer02.entity.Blog;
import com.example.zuer02.entity.BlogType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@EnableAutoConfiguration
@RequestMapping(value="")
public class BlogController {
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private BlogTypeDao blogTypeDao;

    @Transactional(rollbackFor = { Exception.class })
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public int addBlog(@RequestBody Map<String,Object> param) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Blog blog = mapper.convertValue(param.get("blog"), Blog.class);
        System.out.println(blog);
        //List<Blog> filterList = mapper.convertValue(param.get("blog"), new TypeReference<List<Blog>>() { });
        String blogId=UUID.randomUUID().toString();
        blog.setBlogId(blogId);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        blog.setBlogCrtDate(sdf.format(new Date()));
        if(blog.getCategories()!=null&&blog.getCategories().length>0){
            for(int i=0;i<blog.getCategories().length;i++) {

                BlogType blogType=new BlogType();
                blogType.setBlogId(blogId);
                blogType.setBlogTypeId(UUID.randomUUID().toString());
                blogType.setBlogTypeName(blog.getCategories()[i]);
                blogType.setBlogTypeCrtDate(sdf.format(new Date()));
                blogTypeDao.insertBlogType(blogType);
            }
        }
        int i = blogDao.insertBlog(blog);
        return i;
    }

    @Transactional(rollbackFor = { Exception.class })
    @RequestMapping(value="/selectBlog", method=RequestMethod.GET)
    public List<Blog> selectBlog()  throws Exception{
        System.out.println(3333);
        List<Blog> blogList=blogDao.selectBlog();
        return blogList;
    }
    @Transactional(rollbackFor = { Exception.class })
    @RequestMapping(value="/selectBlogByBlogId", method=RequestMethod.GET)
    public Blog selectBlogByBlogId(@RequestParam("blogId") String blogId)  throws Exception{
        Blog blog=blogDao.selectBlogByBlogId(blogId);
        List<BlogType> blogTypeLists=blogTypeDao.selectBlogTypeByBlogId(blogId);
        if(blogTypeLists!=null&&blogTypeLists.size()>0){
            String[] categories=new String[blogTypeLists.size()];
            for (int i = 0; i < blogTypeLists.size(); i++) {
                categories[i]=blogTypeLists.get(i).getBlogTypeName();
            }
            blog.setCategories(categories);
        }
        return blog;
    }
    @Transactional(rollbackFor = { Exception.class })
    @RequestMapping(value="/deleteBlogByBlogId", method=RequestMethod.GET)
    public String deleteBlogByBlogId(@RequestParam("blogId") String blogId) {

        try {
            int i=blogDao.deleteBlogByBlogId(blogId);
            System.out.println("i=" + i);
            if (i > 0) {
                blogTypeDao.deleteBlogTypeByBlogId(blogId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//就是这一句了，加上之后，如果doDbStuff2()抛了异常,     doDbStuff1()是会回滚的                                                                                   //doDbStuff1()是会回滚的
            return e.getMessage();
        }
        return "success";
    }

    @Transactional(rollbackFor = { Exception.class })
    @RequestMapping(value="/editByBlogId", method=RequestMethod.POST)
    public String editByBlogId(@RequestBody Map<String,Object> param) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Blog blog = mapper.convertValue(param.get("blog"), Blog.class);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        blog.setBlogAltDate(sdf.format(new Date()));
        try {
            int i=blogDao.updateBlogById(blog);
            if(i>0){
                blogTypeDao.deleteBlogTypeByBlogId(blog.getBlogId());
                if(blog.getCategories()!=null&&blog.getCategories().length>0){
                    for(int j=0;j<blog.getCategories().length;j++) {

                        BlogType blogType=new BlogType();
                        blogType.setBlogId(blog.getBlogId());
                        blogType.setBlogTypeId(UUID.randomUUID().toString());
                        blogType.setBlogTypeName(blog.getCategories()[j]);
                        blogType.setBlogTypeCrtDate(sdf.format(new Date()));
                        blogTypeDao.insertBlogType(blogType);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//就是这一句了，加上之后，如果doDbStuff2()抛了异常,     doDbStuff1()是会回滚的                                                                                   //doDbStuff1()是会回滚的
            return e.getMessage();
        }
        return "success";
    }
}
