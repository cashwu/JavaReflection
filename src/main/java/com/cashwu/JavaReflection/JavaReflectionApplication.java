package com.cashwu.JavaReflection;

import com.cashwu.JavaReflection.model.Person;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

//@SpringBootApplication
public class JavaReflectionApplication {

    public static void main(String[] args) throws
                                           SQLException,
                                           IllegalAccessException,
                                           InvocationTargetException,
                                           NoSuchMethodException,
                                           InstantiationException {
        //		SpringApplication.run(JavaReflectionApplication.class, args);

        //		Metamodel metamodel = Metamodel.of(Person.class);

        //		PrimaryKeyField primaryKeyField = metamodel.getPrimaryKey();
        //		List<ColumnField> columnFields = metamodel.getColumns();
        //
        //		System.out.println("primary key name = " + primaryKeyField.getName() +
        //				", type = " + primaryKeyField.getType().getSimpleName());
        //
        //		for (ColumnField columnField : columnFields) {
        //			System.out.println("column name = " + columnField.getName() +
        //									   ", type = " + columnField.getType().getSimpleName());
        //		}

        EntityManager<Person> entityManager = EntityManager.of(Person.class);

        Person linda = new Person("Linda", 31);
        Person james = new Person("James", 24);
        Person susan = new Person("Susan", 34);

        entityManager.persist(linda);
        entityManager.persist(james);
        entityManager.persist(susan);

        Person person = entityManager.find(Person.class, 1);;
    }

}
