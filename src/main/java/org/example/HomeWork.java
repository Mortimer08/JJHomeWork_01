package org.example;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class HomeWork {
    public static void main(String[] args) {
        ArrayList<Person> people = Person.getPeople();
        people.forEach(System.out::println);
        System.out.println(Person.findMostYoungestPerson(people));
        System.out.println(Person.getDepartmentOldestPerson(people));
        System.out.println(Person.groupByDepartment(people));
        System.out.println(Person.groupByDepartmentName(people));
        System.out.println(Person.getDepartmentOldestPerson(people));
        System.out.println(Person.cheapPersonsInDepartment(people));
    }

    private static class Department {
        private String name;
        public static ArrayList<Department> departments = new ArrayList<>(Arrays.asList(
                new Department("HR"),
                new Department("Production"),
                new Department("Finance"),
                new Department("Purchase")
        ));

        public static Department getRandomDepartment() {
            return departments.get(ThreadLocalRandom.current().nextInt(departments.size()));
        }

        public Department(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Department{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    private static class Person {

        public Person(String name, int age, double salary, Department department) {
            this.name = name;
            this.age = age;
            this.salary = salary;
            this.department = department;
        }

        //region Utils
        private static final ArrayList<String> names = new ArrayList<>(Arrays.asList("Ivan", "Peter", "Phil", "Stepan", "Dave"));

        public static String getRandomName() {
            return names.get(ThreadLocalRandom.current().nextInt(names.size()));
        }

        public static ArrayList<Person> getPeople() {
            ArrayList<Person> people = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Person person = new Person(
                        getRandomName(),
                        ThreadLocalRandom.current().nextInt(20, 50),
                        ThreadLocalRandom.current().nextInt(50000, 500000),
                        Department.getRandomDepartment()
                );
                people.add(person);
            }
            return people;
        }

        //endregion
        //region Fields
        private String name;
        private int age;
        private double salary;
        private Department department;

        //endregion
        //region Getters setters
        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public double getSalary() {
            return salary;
        }

        public Department getDepartment() {
            return department;
        }

        public String getDepartmentName() {
            return department.getName();
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setSalary(double salary) {
            this.salary = salary;
        }

        public void setDepartment(Department department) {
            this.department = department;
        }

        //endregion

        /**
         * Найти самого молодого сотрудника
         */
        static Optional<Person> findMostYoungestPerson(List<Person> people) {
            return people.stream()
                    .min(Comparator.comparingInt(Person::getAge));
        }

        /**
         * Найти департамент, в котором работает сотрудник с самой большой зарплатой
         */
        static Optional<Department> findMostExpensiveDepartment(List<Person> people) {
            Person person = people.stream()
                    .max((Comparator.comparingDouble(Person::getSalary)))
                    .get();
            return Optional.ofNullable(person.getDepartment());
        }

        /**
         * Сгруппировать сотрудников по департаментам
         */
        static Map<Department, List<Person>> groupByDepartment(List<Person> people) {
            return people.stream()
                    .collect(Collectors.groupingBy(Person::getDepartment));
        }

        /**
         * Сгруппировать сотрудников по названиям департаментов
         */
        static Map<String, List<Person>> groupByDepartmentName(List<Person> people) {
            return people.stream()
                    .collect(Collectors.groupingBy(Person::getDepartmentName));
        }

        /**
         * В каждом департаменте найти самого старшего сотрудника
         */
        static Map<String, Person> getDepartmentOldestPerson(List<Person> people) {
            return people.stream()
                    .collect(Collectors.toMap(
                            Person::getDepartmentName,
                            p -> p,
                            (a, b) -> {
                                if (a.getAge() > b.getAge()) {
                                    return a;
                                } else return b;
                            }));
        }

        /**
         * *Найти сотрудников с минимальными зарплатами в своем отделе
         * (прим. можно реализовать в два запроса)
         */
        static List<Person> cheapPersonsInDepartment(List<Person> people) {
            ArrayList<Person> poorest = new ArrayList<>();
            people.stream()
                    .collect(Collectors.groupingBy(
                            Person::getDepartment,
                            Collectors.minBy(Comparator.comparingDouble(Person::getSalary))
                    ))
                    .forEach((k, v) -> poorest.add(v.get()));
            return poorest;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", salary=" + salary +
                    ", department=" + department +
                    '}';
        }
    }
}
