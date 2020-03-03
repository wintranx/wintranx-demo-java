package com.wintranx.client.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WintranxRecevieServerNoticeController {

    @RequestMapping("/notice/success")
    public String success() {
        // TODO添加签名验证

        // 异步进行进行相应的业务逻辑

        return "SUCCESS";
    }
}
