package com.example.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String roles;

    public UserInfo() {
    }

    public UserInfo(Integer id, String name, String email, String password, String roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public static UserInfoBuilder builder() {
        return new UserInfoBuilder();
    }

    public static class UserInfoBuilder {
        private Integer id;
        private String name;
        private String email;
        private String password;
        private String roles;

        public UserInfoBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public UserInfoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserInfoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserInfoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserInfoBuilder roles(String roles) {
            this.roles = roles;
            return this;
        }

        public UserInfo build() {
            return new UserInfo(id, name, email, password, roles);
        }
    }
}
