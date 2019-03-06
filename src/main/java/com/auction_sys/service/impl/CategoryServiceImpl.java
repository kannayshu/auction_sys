package com.auction_sys.service.impl;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.common.constant.CategoryConst;
import com.auction_sys.common.constant.CommonConst;
import com.auction_sys.common.constant.ProductConst;
import com.auction_sys.dao.CategoryMapper;
import com.auction_sys.pojo.Category;
import com.auction_sys.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created By liuda on 2018/5/29
 */
@Slf4j
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    CategoryMapper categoryMapper;
    public ServerResponse<List<Category>> getPrimaryNode(){
        List<Category> list = categoryMapper.selectByParentId(0);
        return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,list);
    }

    public ServerResponse<List<Category>> getSameLevelNode(Integer id){
        Category category = categoryMapper.selectByPrimaryKey(id);
        if (category==null)
            return ServerResponse.createByError(CategoryConst.CategoryResponStateConst.CATEGORY_INEXISTENCE);
        int parentId = category.getParentId();
        List<Category> list = categoryMapper.selectByParentId(parentId);
        return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,list);
    }

    public ServerResponse<List<Category>> getNextLevelNode(Integer id){
        List<Category> list = categoryMapper.selectByParentId(id);
        return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,list);
    }

    public ServerResponse<List<Category>> getParentsNode(Integer id){
        //找到自己
        Category category = categoryMapper.selectByPrimaryKey(id);
        if (category==null)
            return ServerResponse.createByError(CategoryConst.CategoryResponStateConst.CATEGORY_INEXISTENCE);
        LinkedList<Category> list = new LinkedList<>();
        //递归找到父节点
        while(category.getParentId()!=0){
            category = (Category) categoryMapper.selectByPrimaryKey(category.getParentId());
            list.addFirst(category);
        }
        return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,list);
    }

    public ServerResponse<List<Category>> addNode(Category category){
        Category c = new Category(null,category.getParentId(),category.getName());
        if(c.getParentId()==null||category.getName()==null||category.getName().equals(""))
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_PARAMETETS);
        int id = categoryMapper.insert(c);
        if(id>0)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }

    public ServerResponse<List<Category>> updateNode(Category category){
        if(category.getId()==null||category.getName().equals(""))
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_PARAMETETS);
        int id = categoryMapper.updateByPrimaryKeySelective(category);
        if(id>0)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }

    public ServerResponse<List<Category>> deleteNode(Integer id){
        if(id==null)
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_PARAMETETS);
        int count = categoryMapper.deleteByPrimaryKey(id);
        if(count>0)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }
}
