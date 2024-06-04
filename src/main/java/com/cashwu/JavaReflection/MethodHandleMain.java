package com.cashwu.JavaReflection;

import com.cashwu.JavaReflection.model.Person;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author cash.wu
 * @since 2024/06/04
 */
public class MethodHandleMain {

    public static void main(String[] args) throws Throwable {

        MethodHandles.Lookup lookup = MethodHandles.lookup();

        MethodType emptyConstructorMethodtype = MethodType.methodType(void.class);
        MethodHandle emptyConstructor = lookup.findConstructor(Person.class,
                                                               emptyConstructorMethodtype);
        Person person = (Person) emptyConstructor.invoke();
        System.out.println(person);

        MethodType constructorMethodtype = MethodType.methodType(void.class, String.class, int.class);
        MethodHandle constructor = lookup.findConstructor(Person.class,
                                                               constructorMethodtype);
        Person person2 = (Person) constructor.invoke("James", 28);
        System.out.println(person2);

        MethodType nameGetterMethodType = MethodType.methodType(String.class);
        MethodHandle nameGetter = lookup.findVirtual(Person.class, "getName", nameGetterMethodType);

        String name = (String) nameGetter.invoke(person2);
        System.out.println(name);

        MethodType nameSetterMethodType = MethodType.methodType(void.class, String.class);
        MethodHandle nameSetter = lookup.findVirtual(Person.class, "setName", nameSetterMethodType);

        nameSetter.invoke(person2, "Linda");
        System.out.println(person2);

        MethodHandles.Lookup privateLookup = MethodHandles.privateLookupIn(Person.class, lookup);
        MethodHandle nameReader = privateLookup.findGetter(Person.class, "name", String.class);
        String name2 = (String) nameReader.invoke(person2);
        System.out.println(name2);
    }
}
