package com.signinproeject.domain.user.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "member_id")
    private Long id;

    @Column(name = "account_id",nullable = false,length = 20)
    private String accountId;
    @Column(name = "name", nullable = false, length = 4 ,unique = true)
    private String name;

    @Column(name = "password",nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(name = "student_number",nullable = false)
    private int studentNumber;

    @OneToMany(mappedBy = "member")
    private List<Post> postList = new ArrayList<>();


}
