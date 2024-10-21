package com.example.demo.registration.token;

import com.example.demo.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tb_verification_token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String token;
    Date expirationTime;
    final int EXPIRATION_TIME = 15;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    public VerificationToken(String token, User user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public VerificationToken(String token) {
        super();
        this.token = token;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
