package gft.mentoring.matcher;

import gft.mentoring.Family;

import java.time.LocalDate;

class UnifiedModelBuilder {

    private String firstName;
    private String lastName;
    private Family family;
    private String specialization;
    private int level;
    private int seniority;
    private String localization;
    private boolean contractor;
    private boolean leaver = false;
    private int menteesAssigned;
    private int age;

    UnifiedModelBuilder() {
    }

    UnifiedModel build() {
        // valiudation and defaults
        return new UnifiedModel(firstName, lastName, family, specialization, level, seniority, localization, contractor,
                leaver, menteesAssigned, age);
    }

    UnifiedModelBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    UnifiedModelBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    UnifiedModelBuilder setFamily(Family fam) {
        this.family = fam;
        return this;
    }

    UnifiedModelBuilder setSpecialization(String specialization) {
        this.specialization = specialization;
        return this;
    }

    UnifiedModelBuilder setLevel(int level) {
        this.level = level;
        return this;
    }

    UnifiedModelBuilder setSeniority(int seniority) {
        this.seniority = seniority;
        return this;
    }

    UnifiedModelBuilder setLocalization(String localization) {
        this.localization = localization;
        return this;
    }

    UnifiedModelBuilder setContractor(boolean contractor) {
        this.contractor = contractor;
        return this;
    }

    UnifiedModelBuilder setLeaver(boolean leaver) {
        this.leaver = leaver;
        return this;
    }

    UnifiedModelBuilder setAge(int age) {
        this.age = age;
        return this;
    }

    UnifiedModelBuilder setMenteesAssigned(int menteesAssigned) {
        this.menteesAssigned = menteesAssigned;
        return this;
    }

}
