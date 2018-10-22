package gft.mentoring.sap.model;

import gft.mentoring.Family;

class SAPMentoringModelBuilder {
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

    SAPMentoringModelBuilder setfamily(String s) {
        sapMM.setFamily(Family.fromString(s));
        return this;
    }

    SAPMentoringModelBuilder setfirstName(String firstName) {
        sapMM.setFirstName(firstName);
        return this;
    }

    SAPMentoringModelBuilder setlastName(String lastName) {
        sapMM.setLastName(lastName);
        return this;
    }

    SAPMentoringModelBuilder setfederationID(String initials) {
        sapMM.setFederationID(initials);
        return this;
    }

    SAPMentoringModelBuilder setsapID(String personalNR) {
        sapMM.setSapID(personalNR);
        return this;
    }

    SAPMentoringModelBuilder setspecialization(String costCenter) {
        sapMM.setSpecialization(costCenter);
        return this;
    }

    SAPMentoringModelBuilder setseniority(String initEntry) {
        sapMM.setSeniority(initEntry);
        return this;
    }

    SAPMentoringModelBuilder setlineManagerID(String persNrSuperior) {
        sapMM.setLineManagerID(persNrSuperior);
        return this;
    }

    SAPMentoringModelBuilder setmenteeID(String persNrMentor) {
        sapMM.setMenteeID(persNrMentor);
        return this;
    }

}
