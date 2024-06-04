package com.cashwu.JavaReflection.model;

import com.cashwu.JavaReflection.annotation.Column;
import com.cashwu.JavaReflection.annotation.PrimaryKey;
import lombok.*;
import org.springframework.context.annotation.Primary;

/**
 * @author cash.wu
 * @since 2024/06/04
 */

@Data
@Builder
public class Person {

    public Person(String name,
                  int age) {
        this.name = name;
        this.age = age;
    }

    public Person() {
    }

    @PrimaryKey(name="id")
    private long id;
    @Column(name="name")
    private String name;
    @Column(name="age")
    private int age;

    public String getName() {
        System.out.println("get name invoked");
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age + '}';
    }
}
