package com.auction_sys.controller;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.pojo.Category;
import com.auction_sys.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created By liuda on 2018/5/29
 */
@CrossOrigin("*")
@Controller
@ResponseBody
@RequestMapping("/category/")
public class CategoryController {

    @Autowired
    CategoryService categoryService;


    @GetMapping("get_parimary_node")
    public ServerResponse<List<Category>> getPrimaryNode() {
        return categoryService.getPrimaryNode();
    }
    @GetMapping("get_same_level_node")
    public ServerResponse<List<Category>> getSameLevelNode(@RequestParam(value = "id",required = true) Integer id) {
        return categoryService.getSameLevelNode(id);
    }
    @GetMapping("get_next_level_nodes")
    public ServerResponse<List<Category>> getNextLevelNode(@RequestParam(value ="id",required = true)Integer id) {
        return categoryService.getNextLevelNode(id);
    }
    @GetMapping("get_parents_node")
    public ServerResponse<List<Category>> getParentsNode(@RequestParam(value = "id",required = true) Integer id) {
        return categoryService.getParentsNode(id);
    }

    @PostMapping("add_node")
    public ServerResponse<List<Category>> addNode(Category category) {
        return categoryService.addNode(category);
    }

    @PostMapping("update_node")
    public ServerResponse<List<Category>> updateNode(Category category) {
        return categoryService.updateNode(category);
    }
    @GetMapping("del_node")
    public ServerResponse<List<Category>> deleteNode(@RequestParam(value = "id",required = true)Integer id) {
        return categoryService.deleteNode(id);
    }
}
