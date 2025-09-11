package com.example.authservice.domain.user;
import java.util.UUID;

import com.example.authservice.domain.user.vo.Email;
import com.example.authservice.domain.user.vo.Role;
import com.example.authservice.domain.user.vo.RoleType;

import jakarta.persistence.Column;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "usuario")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Embedded
    @Valid
    private Email email;

    @Embedded
    private Role role;

    public User(String name, String pw, Email email, RoleType role){
        this.name = name;
        this.password = pw;
        this.email = email;
        this.role = Role.of(role);
        
    }

    public void promoteTo(RoleType role){
        if (role.getLevel() > this.role.getValue().getLevel()){
            this.role = Role.of(role);
        }
    }

    public boolean canAccess(RoleType role){
        return this.role.covers(role);
    } 

    public void changePassword(String password) {
        this.password = password;
    }
}
