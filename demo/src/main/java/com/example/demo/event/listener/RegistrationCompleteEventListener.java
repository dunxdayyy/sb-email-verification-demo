package com.example.demo.event.listener;

import com.example.demo.event.RegistrationCompleteEvent;
import com.example.demo.user.User;
import com.example.demo.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;
    private final JavaMailSender javaMailSender;
    private User theUser;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        //lấy người dùng mới đăng ký
        theUser = event.getUser();
        //tạo mã xác thực cho người dùng
        String verificationToken = UUID.randomUUID().toString();
        //lưu mã xác thực cho người dùng
        userService.saveUserVerificationToken(theUser, verificationToken);
        //tạo đường dẫn xác thực gửi cho người dùng
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;
        //gửi email
        try {
            sendVerificationEmail(url);
        }catch (MessagingException | UnsupportedEncodingException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException{

        String sub = "Xác thực email";
        String senderName = "Dịch vụ đăng ký người dùng";
        String mailContent = "<p>Xin chào, "+theUser.getFirstName()+", </p>"+
                "<p>Cảm ơn bạn vì đã đăng ký,"+"" +
                "vui lòng truy cập vào liên kết bên dưới để hoàn tất thủ tục đăng ký.</p>"+
                "<a href=\"" +url+ "\">Xác thực email để kích hoạt tài khoản</a>"+
                "<p> Chân thành cảm ơn <br> Dịch vụ đăng ký người dùng";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject(sub);
        helper.setFrom("sendermail@gmail.com",senderName);
        helper.setTo(theUser.getEmail());
        helper.setText(mailContent, true);
        javaMailSender.send(message);
    }
}
