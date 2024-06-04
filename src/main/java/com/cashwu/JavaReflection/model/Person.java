package com.cashwu.JavaReflection.model;

import com.cashwu.JavaReflection.annotation.Column;
import com.cashwu.JavaReflection.annotation.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Primary;

/**
 * @author cash.wu
 * @since 2024/06/04
 */

@Data
@AllArgsConstructor
@Builder
public class Person {

    public Person(String name,
                  int age) {
        this.name = name;
        this.age = age;
    }

    @PrimaryKey(name="id")
    private long id;
    @Column(name="name")
    private String name;
    @Column(name="age")
    private int age;
}
