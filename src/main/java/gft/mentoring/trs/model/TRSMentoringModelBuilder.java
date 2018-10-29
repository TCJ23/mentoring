package gft.mentoring.trs.model;

import gft.mentoring.Family;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

class TRSMentoringModelBuilder {
    private TRSMentoringModel trsMM = new TRSMentoringModel();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    TRSMentoringModel build() {
        return trsMM;
    }

    TRSMentoringModelBuilder setleaver(String leave) {
        if (leave.trim().equalsIgnoreCase("Notice Period") ||
                leave.trim().equalsIgnoreCase("Hired")) trsMM.setLeaver(true);
        return this;
    }

    TRSMentoringModelBuilder setlevel(String level) {
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

    TRSMentoringModelBuilder setspecialization(String spec) {
        trsMM.setSpecialization(spec.trim().toLowerCase());
        return this;
    }

    TRSMentoringModelBuilder setfamily(String fam) {
        trsMM.setFamily(Family.fromString(fam));
        return this;
    }

    TRSMentoringModelBuilder setseniority(String days) throws DateTimeParseException {
        LocalDate parsedDate = LocalDate.parse(days, formatter);
        LocalDate now = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(parsedDate, now);
        trsMM.setSeniority((int) daysBetween);
        return this;
    }

    TRSMentoringModelBuilder setlocalization(String office) {
        trsMM.setLocalization(office);
        return this;
    }

    TRSMentoringModelBuilder setcontractor(String contract) {
        if (contract.trim().equalsIgnoreCase("Contract")) trsMM.setContractor(true);
        else if (contract.trim().equalsIgnoreCase("Permanent")) trsMM.setContractor(false);
        return this;
    }

    TRSMentoringModelBuilder setfirstName(String firstName) {
        trsMM.setFirstName(firstName);
        return this;
    }

    TRSMentoringModelBuilder setlastName(String lastName) {
        trsMM.setLastName(lastName);
        return this;
    }
}
