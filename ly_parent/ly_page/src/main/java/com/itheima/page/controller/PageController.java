package com.itheima.page.controller;

import com.itheima.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    /**
     * 通过thyemleaf静态化技术，在服务器端产生一个渲染完毕html页面给前端
     * @return
     */
    @GetMapping("/test")
    public String test(@RequestParam("msg") String msg, Model model){
        model.addAttribute("msg", "hello");
        return "test";  //找到templates对应文件进行渲染
    }

    /**
     * 在服务器端通过静态化技术产生html 将生成html以流的形式响应给前端
     * @param spuId
     * @return
     */
    @GetMapping("/item/{id}.html")
    public String gerericHtml(@PathVariable("id") Long spuId, Model model){
        //准备静态页需要参数
        Map<String, Object> mapData = pageService.loadItemData(spuId);
        model.addAllAttributes(mapData);
        return "item";
    }

}
