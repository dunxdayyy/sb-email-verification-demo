package com.example.demo.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.NaturalId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "tb_user")
public class User {

    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String firstName;
    String lastName;
    @NaturalId(mutable = true)
    String email;
    String password;
    String role;
    boolean isEnabled = false;

}
