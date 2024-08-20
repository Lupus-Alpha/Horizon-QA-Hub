package com.universe.backend.controller;

import com.universe.backend.request.RunRequest;
import com.universe.backend.service.impl.RunService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auto_test")
public class RunController {

        @Resource
        private RunService runService;

        @PostMapping("/run")
        public String run(@RequestBody RunRequest runRequest, HttpServletRequest request) {
            String user = request.getSession().getAttribute("account").toString();
            runRequest.setRunUser(user);
            return runService.run(runRequest);
        }

}
