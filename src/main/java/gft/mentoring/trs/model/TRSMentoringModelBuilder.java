package gft.mentoring.trs.model;

import gft.mentoring.Family;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

class TRSMentoringModelBuilder {
    private TRSMentoringModel trsMM = new TRSMentoringModel();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    TRSMentoringModel build() {
        return trsMM;
    }

    TRSMentoringModelBuilder setleaver(String s) {
        if (s.trim().equalsIgnoreCase("Notice Period") ||
                s.trim().equalsIgnoreCase("Hired")) trsMM.setLeaver(true);
        return this;
    }

    TRSMentoringModelBuilder setlevel(String s) {
        try {
            if (s.equalsIgnoreCase("LD")) {
                trsMM.setLevel(8);
            } else {
                String lvl = s.replaceAll("^L(\\d)+.*$", "$1");
                trsMM.setLevel(Integer.parseInt(lvl));
            }
        } catch (NumberFormatException e) {
            trsMM.setLevel(0);
        }
        return this;
    }

    TRSMentoringModelBuilder setspecialization(String s) {
        trsMM.setSpecialization(s);
        return this;
    }

    TRSMentoringModelBuilder setfamily(String s) {
        trsMM.setFamily(Family.fromString(s));
        return this;
    }

    TRSMentoringModelBuilder setseniority(String s) {
        LocalDate parsedDate = LocalDate.parse(s, formatter);
        LocalDate now = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(parsedDate, now);
        trsMM.setSeniority((int) daysBetween);
        return this;
//        Exception in thread "main" java.time.format.DateTimeParseException: Text '18-10-2017' could not be parsed at index 0
    }
}
