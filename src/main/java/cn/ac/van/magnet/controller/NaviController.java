package cn.ac.van.magnet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NaviController {
    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return "index";
    }
}
