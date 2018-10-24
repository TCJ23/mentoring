package gft.mentoring.trs.model;

class TRSMentoringModelBuilder {
    private TRSMentoringModel trsMM = new TRSMentoringModel();

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
}
