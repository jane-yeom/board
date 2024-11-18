package com.board.controller;

import com.board.domain.User;
import com.board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String regForm(){
        return "board/register";

    }

    @PostMapping
    public String regUser(User user, Model model){
        if(user.getPassword().length() < 8 ){
            model.addAttribute("error", "비밀번호는 8자 이상이어야 합니다.");
            return "board/register";
        }

        if(!userService.registerUser(user)){
            model.addAttribute("error","이미 사용 중인 아이디 또는 이메일입니다.");
            return "board/register";
        }
        return "redirect:/login?registered";

    }
}
