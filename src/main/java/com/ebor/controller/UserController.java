package com.ebor.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ebor.common.R;
import com.ebor.entity.User;
import com.ebor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());

        // get email address
        String email = map.get("email").toString();

        // get code
        String code = map.get("code").toString();

        // get code from session
        Object codeInSession = session.getAttribute(email);

        if(codeInSession!=null && codeInSession.equals(code)){

            // judge whether this is a new user
            LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
            qw.eq(User::getEmail, email);
            User user = userService.getOne(qw);
            if(user==null){// new user
                user = new User();
                user.setEmail(email);
                user.setStatus(1);
                userService.save(user);


            }
            session.setAttribute("user",user.getId());
            return R.success(user);

        }


        return R.error("Failed to login");
    }

}
