package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName ViewController
 * @Description: com.example.controller
 * @Auther: xiwd
 * @Date: 2022/4/14 - 04 - 14 - 22:54
 * @version: 1.0
 */
@Controller
public class ViewController {
    //properties

    //methods
    @RequestMapping("/{uri}")
    public String getUriView(@PathVariable(value = "uri")String uri){
        return uri;
    }
}
