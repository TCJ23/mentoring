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
}
