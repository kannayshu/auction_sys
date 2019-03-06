package com.auction_sys.service;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.dao.CategoryMapper;
import com.auction_sys.pojo.Category;

import java.util.List;

/**
 * Created By liuda on 2018/5/29
 */
public interface CategoryService {
    ServerResponse<List<Category>> getPrimaryNode();
    ServerResponse<List<Category>> getSameLevelNode(Integer id);
    ServerResponse<List<Category>> getNextLevelNode(Integer id);
    ServerResponse<List<Category>> getParentsNode(Integer id);
    ServerResponse<List<Category>> addNode(Category category);
    ServerResponse<List<Category>> updateNode(Category category);
    ServerResponse<List<Category>> deleteNode(Integer id);
}
