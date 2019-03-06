package com.auction_sys.dao;

import com.auction_sys.pojo.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    List<Category> selectByParentId(Integer parentId);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);
}