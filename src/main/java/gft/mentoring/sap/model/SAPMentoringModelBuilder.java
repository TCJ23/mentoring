package gft.mentoring.sap.model;

import gft.mentoring.Family;

public class SAPMentoringModelBuilder {
    private SAPMentoringModel sapMM = new SAPMentoringModel();

    SAPMentoringModel build() {
        return sapMM;
    }

    SAPMentoringModelBuilder setlevel(String s) {
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

    SAPMentoringModelBuilder setcontractor(String s) {
        sapMM.setContractor(s.trim().equalsIgnoreCase("Contractors"));
        return this;
    }

    public SAPMentoringModelBuilder setfamily(String s) {
        sapMM.setFamily(Family.fromString(s));
        return this;
    }

    public SAPMentoringModelBuilder setfirstName() {
        return this;
    }

    public SAPMentoringModelBuilder setlastName() {
        return this;
    }

    public SAPMentoringModelBuilder setfederationID() {
        return this;
    }

    public SAPMentoringModelBuilder setsapID() {
        return this;
    }


    public SAPMentoringModelBuilder setspecialization() {
        return this;
    }

    public SAPMentoringModelBuilder setseniority() {
        return this;
    }

    public SAPMentoringModelBuilder setlineManagerID() {
        return this;
    }

    public SAPMentoringModelBuilder setmenteeID() {
        return this;
    }

}
