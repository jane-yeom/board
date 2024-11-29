package com.board.service;

import com.board.domain.Email;
import com.board.mapper.EmailMapper;
import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${spring.mail.password}")
    private String mailPassword;

    private final JavaMailSender mailSender;
    private final EmailMapper emailMapper;


    @PostConstruct
    public void init(){
        log.info("Mail username: {}", senderEmail);
        log.info("Mail password length : {}", mailPassword.length());

    }
    @Transactional
    public void sendEmail(Email email){
       log.info("Start sending email to: {}", email.getReceiver());

        if (email.getReceiver() == null || email.getReceiver().trim().isEmpty()) {
            throw new IllegalArgumentException("받는 사람의 이메일 주소가 필요합니다.");
        }

        email.setSender(senderEmail);
        email.setStatus("SENDING");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(email.getSender());
            helper.setTo(email.getReceiver());  // 받는 사람 설정
            helper.setSubject(email.getSubject());
            helper.setText(email.getContent(), true);

            log.info("Email configuration completed. Attempting to send...");
            mailSender.send(message);
            log.info("Email sent successfully");

            email.setStatus("SENT");

        } catch (Exception e) {
            log.error("Failed to send email", e);
            email.setStatus("FAILED");
            throw new RuntimeException("이메일 전송에 실패했습니다: " + e.getMessage(), e);
        } finally {
            emailMapper.save(email);
        }


    }


    public List<Email> getEmailList(){
        return emailMapper.findAll();

    }

    public Email getEmail(Long id){
        return emailMapper.findById(id);

    }
    /*
    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // true means this is HTML

        mailSender.send(message);
    }

    // 회원가입 인증 메일
    public void sendVerificationEmail(String to, String verificationToken) throws MessagingException {
        String subject = "회원가입 이메일 인증";
        String verificationLink = "http://localhost:8080/verify?token=" + verificationToken;

        String htmlContent =
            "<div style='margin:100px;'>" +
            "<h1>이메일 인증</h1>" +
            "<p>아래 링크를 클릭하여 이메일 인증을 완료해 주세요:</p>" +
            "<a href='" + verificationLink + "' style='padding:10px 20px; background:#007bff; color:#fff; text-decoration:none; border-radius:5px;'>" +
            "이메일 인증하기</a>" +
            "<br><br>" +
            "<p>링크가 작동하지 않는 경우 아래 URL을 브라우저에 복사하여 붙여넣으세요:</p>" +
            "<p>" + verificationLink + "</p>" +
            "</div>";

        sendEmail(to, subject, htmlContent);
    }

    // 비밀번호 재설정 메일
    public void sendPasswordResetEmail(String to, String resetToken) throws MessagingException {
        String subject = "비밀번호 재설정";
        String resetLink = "http://localhost:8080/password/reset?token=" + resetToken;

        String htmlContent =
            "<div style='margin:100px;'>" +
            "<h1>비밀번호 재설정</h1>" +
            "<p>아래 링크를 클릭하여 비밀번호를 재설정하세요:</p>" +
            "<a href='" + resetLink + "' style='padding:10px 20px; background:#007bff; color:#fff; text-decoration:none; border-radius:5px;'>" +
            "비밀번호 재설정하기</a>" +
            "<br><br>" +
            "<p>링크가 작동하지 않는 경우 아래 URL을 브라우저에 복사하여 붙여넣으세요:</p>" +
            "<p>" + resetLink + "</p>" +
            "<br>" +
            "<p>본인이 요청하지 않은 경우 이 메일을 무시하셔도 됩니다.</p>" +
            "</div>";

        sendEmail(to, subject, htmlContent);
    }*/


}