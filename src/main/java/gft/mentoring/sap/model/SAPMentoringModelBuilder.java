package gft.mentoring.sap.model;

import gft.mentoring.Family;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

class SAPMentoringModelBuilder {
    private static final Logger LOGGER = Logger.getLogger(SAPMentoringModelBuilder.class.getName());
    private static final int DEFAULT_SENIORITY = 0;
    private static final int DEFAULT_AGE = 0;
    private static final String DATE_PATTERN = "dd-MM-yyyy";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private SAPMentoringModel sapMM = new SAPMentoringModel();

    SAPMentoringModel build() {
        return sapMM;
    }

    SAPMentoringModelBuilder setLevel(String s) {
        try {
            if (s.equalsIgnoreCase("LD")) {
                sapMM.setLevel(8);
            } else {
                String lvl = s.replaceAll("^L(\\d)+.*$", "$1");
                sapMM.setLevel(Integer.parseInt(lvl));
                //sapMM.setLevel(Integer.parseInt(s.trim().replaceAll("[\\D]", "")));
            }
        } catch (NumberFormatException e) {
            sapMM.setLevel(0);
        }
        return this;
    }

    SAPMentoringModelBuilder setContractor(String s) {
        sapMM.setContractor(s.trim().equalsIgnoreCase("Contractors"));
        return this;
    }

    SAPMentoringModelBuilder setFamily(String s) {
        sapMM.setFamily(Family.fromString(s));
        return this;
    }

    SAPMentoringModelBuilder setFirstName(String firstName) {
        sapMM.setFirstName(firstName);
        return this;
    }

    SAPMentoringModelBuilder setLastName(String lastName) {
        sapMM.setLastName(lastName);
        return this;
    }

    SAPMentoringModelBuilder setFederationID(String initials) {
        sapMM.setFederationID(initials);
        return this;
    }

    SAPMentoringModelBuilder setSapID(String personalNR) {
        sapMM.setSapID(personalNR);
        return this;
    }

    SAPMentoringModelBuilder setSpecialization(String costCenter) {
        sapMM.setSpecialization(costCenter);
        return this;
    }

    SAPMentoringModelBuilder setSeniority(String days) {
        try {
            LocalDate parsedDate = LocalDate.parse(days, formatter);
            LocalDate now = LocalDate.now();
            long daysBetween = ChronoUnit.DAYS.between(parsedDate, now);
            sapMM.setSeniority((int) daysBetween);
        } catch (DateTimeParseException e) {
            LOGGER.warning("Couldn't read start date column due to wrong format. Format should be "
                    + DATE_PATTERN + "\n Setting seniority to " + DEFAULT_SENIORITY);
            sapMM.setSeniority(DEFAULT_SENIORITY);
        }
        return this;
    }

    SAPMentoringModelBuilder setAge(String days) {
        try {
            LocalDate parsedDate = LocalDate.parse(days, formatter);
            LocalDate now = LocalDate.now();
            long daysBetween = ChronoUnit.YEARS.between(parsedDate, now);
            sapMM.setAge((int) daysBetween);
        } catch (DateTimeParseException e) {
            LOGGER.warning("Couldn't read date of birth column due to wrong format. Format should be "
                    + DATE_PATTERN + "\n Setting age to " + DEFAULT_AGE);
            sapMM.setAge(DEFAULT_AGE);
        }
        return this;
    }

    SAPMentoringModelBuilder setLineManagerID(String persNrSuperior) {
        sapMM.setLineManagerID(persNrSuperior);
        return this;
    }

    SAPMentoringModelBuilder setMenteeID(String persNrMentor) {
        sapMM.setMenteeID(persNrMentor);
        return this;
    }

}
