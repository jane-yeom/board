package com.board.controller;

import com.board.domain.User;
import com.board.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String username,
                       @RequestParam String password,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {

        User user = userService.login(username, password);

        if (user != null) {
            // 로그인 성공
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            redirectAttributes.addFlashAttribute("loginMessage", "로그인 성공!");
            return "redirect:/board/list";
        } else {
            // 로그인 실패
            redirectAttributes.addFlashAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "redirect:/login";
        }
    }

    // 회원가입 페이지
    @GetMapping("/register")
    public String registerForm() {
        return "board/register";
    }


    // 회원가입 처리
    @PostMapping("/register")
    public String register(User user, RedirectAttributes redirectAttributes) {
        if(user.getPassword().length() < 4){
            redirectAttributes.addFlashAttribute("error","비밀번호는 4자 이상이어야 합니다.");
            return "redirect:/register";
        }

        try {
            boolean result = userService.registerUser(user);
            if (result){
                redirectAttributes.addFlashAttribute("message","회원가입이 완료되었습니다.");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("error","이미 사용중인 아이디입니다.");
                return "redirect:/register";
            }
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error","오류발생");
            return "redirect:/register";
        }

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
}
