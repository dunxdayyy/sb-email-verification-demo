package com.example.demo.registration;

import com.example.demo.event.RegistrationCompleteEvent;
import com.example.demo.registration.token.VerificationToken;
import com.example.demo.registration.token.VerificationTokenRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

     private final UserService userService;
     private final VerificationTokenRepository verificationTokenRepository;
     private final ApplicationEventPublisher applicationEventPublisher;

     @PostMapping
     public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request){
         User user = userService.registerUser(registrationRequest);
         applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
         return "Thành công, vui lòng kiểm tra email để hoàn tất thủ tục đăng ký.";
     }

     @GetMapping("/verifyEmail")
     public  String verifyEmail(@RequestParam("token") String token){
         VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
         if(verificationToken.getUser().isEnabled()){
             return "Tài khoản này đã được xác thực, vui lòng đăng nhập.";
         }

         if(userService.validateToken(token).equalsIgnoreCase("Mã có hiệu lực")){
             return "Xác thực email thành công, hiện tại bạn có thể đăng nhập.";
         };
         return "Mã xác thực không hợp lệ";
     }

    private String applicationUrl(HttpServletRequest request) {
         return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
