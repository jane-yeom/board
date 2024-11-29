package com.board.controller;

import com.board.domain.Email;
import com.board.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/send")
    public String sendForm(){
        return "email/send";

    }

    @PostMapping("/send")
    public String send(Email email, RedirectAttributes redirectAttributes){
         log.info("Sending email to: {}", email.getReceiver());  // 받는 사람 로그
         log.info("Email subject: {}", email.getSubject());
        try {
            if (email.getReceiver() == null || email.getReceiver().trim().isEmpty()) {
                throw new IllegalArgumentException("받는 사람의 이메일 주소를 입력해주세요.");
            }

            emailService.sendEmail(email);
            redirectAttributes.addFlashAttribute("message", "이메일이 성공적으로 전송되었습니다.");
            return "redirect:/email/list";
        } catch (Exception e) {
            log.error("Failed to send email", e);
            redirectAttributes.addFlashAttribute("error", "이메일 전송에 실패했습니다: " + e.getMessage());
            return "redirect:/email/send";
        }

    }

    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("emails", emailService.getEmailList());
        return "email/list";

    }
}
