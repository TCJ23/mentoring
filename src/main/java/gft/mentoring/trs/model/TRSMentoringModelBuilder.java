package gft.mentoring.trs.model;

import gft.mentoring.Family;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

class TRSMentoringModelBuilder {
    private static final Logger LOGGER = Logger.getLogger(TRSMentoringModelBuilder.class.getName());
    private static final int DEFAULT_SENIORITY = 0;
    private static final String DATE_PATTERN = "dd-MM-yyyy";
    private TRSMentoringModel trsMM = new TRSMentoringModel();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

    TRSMentoringModel build() {
        return trsMM;
    }

    TRSMentoringModelBuilder setLeaver(String leave) {
        if (leave.trim().equalsIgnoreCase("Notice Period") ||
                leave.trim().equalsIgnoreCase("Hired")) trsMM.setLeaver(true);
        return this;
    }

    TRSMentoringModelBuilder setLevel(String level) {
        try {
            if (level.equalsIgnoreCase("LD")) {
                trsMM.setLevel(8);
            } else {
                String lvl = level.replaceAll("^L(\\d)+.*$", "$1");
                trsMM.setLevel(Integer.parseInt(lvl));
            }
        } catch (NumberFormatException e) {
            trsMM.setLevel(0);
        }
        return this;
    }

    TRSMentoringModelBuilder setSpecialization(String spec) {
        trsMM.setSpecialization(spec.trim().toLowerCase());
        return this;
    }

    TRSMentoringModelBuilder setFamily(String fam) {
        trsMM.setFamily(Family.fromString(fam));
        return this;
    }

    TRSMentoringModelBuilder setSeniority(String days) {
        try {
            LocalDate parsedDate = LocalDate.parse(days, formatter);
            LocalDate now = LocalDate.now();
            long daysBetween = ChronoUnit.DAYS.between(parsedDate, now);
            trsMM.setSeniority((int) daysBetween);
        } catch (DateTimeParseException e) {
            LOGGER.warning("Couldn't read start date column due to wrong format. Format should be "
                    + DATE_PATTERN + "\n Setting seniority to " + DEFAULT_SENIORITY);
            trsMM.setSeniority(DEFAULT_SENIORITY);
        }
        return this;
    }

    TRSMentoringModelBuilder setLocalization(String office) {
        trsMM.setLocalization(office);
        return this;
    }

    TRSMentoringModelBuilder setContractor(String contract) {
        if (contract.trim().equalsIgnoreCase("Contract")) trsMM.setContractor(true);
        else if (contract.trim().equalsIgnoreCase("Permanent")) trsMM.setContractor(false);
        return this;
    }

    TRSMentoringModelBuilder setFirstName(String firstName) {
        trsMM.setFirstName(firstName);
        return this;
    }

    TRSMentoringModelBuilder setLastName(String lastName) {
        trsMM.setLastName(lastName);
        return this;
    }
}
