package com.bajie.page.controller;

import com.bajie.page.service.FileService;
import com.bajie.page.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("item")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private FileService fileService;

    @GetMapping("{id}.html")
    public String toItemPage(Model model, @PathVariable("id") Long id) {

        Map<String, Object> map = goodsService.loadModel(id);
        model.addAllAttributes(map);
        //在响应页面之前，将页面进行保存，保存至nginx配置的目录
        if (!this.fileService.exists(id)) {
            this.fileService.syncCreateHtml(id);
        }
        return "item";
    }
}