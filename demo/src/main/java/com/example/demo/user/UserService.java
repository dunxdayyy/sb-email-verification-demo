package com.example.demo.user;

import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.registration.RegistrationRequest;
import com.example.demo.registration.token.VerificationToken;
import com.example.demo.registration.token.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest request) {

        if(this.findByEmail(request.email()).isPresent()){
            throw new UserAlreadyExistsException("[Lỗi] người dùng có email "+request.email()+" đã tồn tại.");
        }

        return userRepository.save(
                User.builder()
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .email(request.email())
                        .password(passwordEncoder.encode(request.password()))
                        .role(request.role())
                        .build()
        );
    }

    @Override
    public void saveUserVerificationToken(User theUser, String verificationToken) {
        verificationTokenRepository.save(new VerificationToken(verificationToken, theUser));
    }

    @Override
    public String validateToken(String theToken) {
        VerificationToken token = verificationTokenRepository.findByToken(theToken);
        if(token == null){
            return "[Lỗi] mã xác thực không hợp lệ!";
        }

        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if(token.getTokenExpirationTime().getTime() - calendar.getTime().getTime() <=0 ){
            verificationTokenRepository.delete(token);
            return "[Lỗi] mã xác thực đã hết hạn!";
        }
        user.setIsEnabled(true);
        userRepository.save(user);
        return "Mã có hiệu lực";
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
