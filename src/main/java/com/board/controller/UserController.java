package com.board.controller;

import com.board.domain.User;
import com.board.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    //로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model){
        User user = userService.login(username, password);

        if( user!= null){
            //세션에 사용자 정보 저장
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            return "redirect:/board/list";
        } else {
            model.addAttribute("error","아이디 또는 비밀번호가 올바르지 않습니다.");
            return "login";
        }
    }

    // 회원가입 페이지
    @GetMapping("/register")
    public String registerForm() {
        return "board/register";
    }


    // 회원가입 처리
    @PostMapping("/register")
    public String register(User user, Model model) {

            return "board/register";

    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";

    }

    // 접근 거부 페이지
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    // 메인 페이지
    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String username = auth.getName();
            model.addAttribute("username", username);
        }
        return "home";
    }
}
