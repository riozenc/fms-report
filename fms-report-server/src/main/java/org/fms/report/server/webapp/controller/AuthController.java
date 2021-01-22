package org.fms.report.server.webapp.controller;

import java.util.List;

import org.fms.report.common.webapp.domain.UserDomain;
import org.fms.report.server.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@ControllerAdvice
@RequestMapping(value = "auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @RequestMapping("findUSerByIds")
    @ResponseBody
    public List<UserDomain> findUSerByIds(@RequestBody(required = false) String idsJson) {
        List<Long> ids= JSONObject.parseArray(idsJson,Long.class);
        List<UserDomain> userDomains=userService.findByIds(ids);
        return userDomains;
    }
}
