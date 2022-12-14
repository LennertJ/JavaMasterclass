package com.crm.miniCRM.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "person_address",
            joinColumns = @JoinColumn(name = "person_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "address_id", referencedColumnName = "ID")
    )
    private List<Address> person_address;

    @ManyToMany
    @JoinTable(
            name = "member",
            joinColumns = @JoinColumn(name = "COMMUNITY_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PERSON_ID", referencedColumnName = "ID"))
    List<Community> member;


    private String firstName;
    private String lastName;
    private LocalDate birthDay;
    private String birthDayAsString;

    public String getBirthDayAsString() {
        return birthDayAsString;
    }

    public void setBirthDayAsString(String birthDayAsString) {
        this.birthDayAsString = birthDayAsString;
    }


    private boolean isActive;

    protected Person() {
    }

    public Person(String firstName, String lastName, LocalDate birthDay) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.isActive = true;
    }

    @Override
    public String toString() {
        return String.format(
                "Person[id=%d, firstName='%s', lastName='%s', birthDay= '%S']",
                id, firstName, lastName, birthDay.toString());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}