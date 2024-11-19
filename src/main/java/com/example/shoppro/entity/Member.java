package com.example.shoppro.entity;


import com.example.shoppro.constant.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="member")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Member {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String name;

    @Column(unique = true, nullable = false)
    private String email;

    private  String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;
}
