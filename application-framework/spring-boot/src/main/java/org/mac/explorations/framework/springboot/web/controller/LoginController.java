/*
 *          (          (
 *          )\ )  (    )\   )  )     (
 *  (  (   (()/( ))\( ((_| /( /((   ))\
 *  )\ )\   ((_))((_)\ _ )(_)|_))\ /((_)
 * ((_|(_)  _| (_))((_) ((_)__)((_|_))
 * / _/ _ \/ _` / -_|_-< / _` \ V // -_)
 * \__\___/\__,_\___/__/_\__,_|\_/ \___|
 *
 * 东隅已逝，桑榆非晚。(The time has passed,it is not too late.)
 * 虽不能至，心向往之。(Although I can't, my heart is longing for it.)
 *
 */

package org.mac.explorations.framework.springboot.web.controller;

import org.mac.explorations.framework.springboot.web.common.Constants;
import org.mac.explorations.framework.springboot.web.repository.entity.User;
import org.mac.explorations.framework.springboot.web.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @auther mac
 * @date 2020-02-02 19:14
 */
@Controller
public class LoginController {

    private UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Map<String,Object> errors, HttpSession session){
        if ("500".equals(username)){
            throw new RuntimeException("500 Error");
        }
        if(StringUtils.hasText(username) && StringUtils.hasText(password)) {
            User user = userService.getUserByUsername(username);
            if (user == null) {
                errors.put("msg","用户名或密码错误");
                return "login";
            }

            if (!password.equals(user.getPassword())) {
                errors.put("msg","用户名或密码错误");
                return "login";
            }

            session.setAttribute(Constants.SESSION_USER_KEY,user);
            return "redirect:/main.html";
        }

        errors.put("msg","用户名或密码错误");
        return "login";
    }
}
