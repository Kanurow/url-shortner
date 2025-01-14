package com.rowland.engineering.shortner.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users_table")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String fullName;
    private String email;
    private String role = "USER_ROLE";
    private String username;

    @OneToMany
    private List<ClickEvent> clickEventList;
}
