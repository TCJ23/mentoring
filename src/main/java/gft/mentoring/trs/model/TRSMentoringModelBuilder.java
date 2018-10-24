package gft.mentoring.trs.model;

class TRSMentoringModelBuilder {
    private TRSMentoringModel trsMM = new TRSMentoringModel();

    TRSMentoringModel build() {
        return trsMM;
    }

    TRSMentoringModelBuilder setleaver(String s) {
        trsMM.setLeaver(s.trim().equalsIgnoreCase("Notice Period"));
        return this;
    }
}
