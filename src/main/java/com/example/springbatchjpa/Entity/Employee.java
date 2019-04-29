package com.example.springbatchjpa.Entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Component
@Entity
@Table(name = "EMPLOYEE")
public class Employee {

    /* Identify id as this entity's unique identifier. The id value is auto generated. */
    @Id
    @Column(name="id")
    private Long id;
    @Column(name="password")
    private String password;
    @Column(name="email")
    private String email;
    @Column(name="sex")
    private String sex;
    @Column(name="age")
    private long age;
    @Column(name="title")
    private String title;
    @Column(name="salary")
    private long salary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }
}
