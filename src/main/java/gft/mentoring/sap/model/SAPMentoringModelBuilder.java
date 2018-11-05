package gft.mentoring.sap.model;

import gft.mentoring.Family;

class SAPMentoringModelBuilder {
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

    SAPMentoringModelBuilder setSeniority(String initEntry) {
        sapMM.setSeniority(initEntry);
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
