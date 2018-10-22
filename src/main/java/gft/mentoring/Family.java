package gft.mentoring;

public enum Family {
    AMS("AMS"),
    ARCHITECTURE("Architecture"),
    BUSINESS_CONSULTING("Business Consulting"),
    CORPORATE_SERVICES("Corporate Services"),
    DATA("Data"),
    DIGITAL("Digital"),
    EXECUTIVE_DIRECTOR("ExD – Executive Director"),
    PROJECT_DEVELOPMENT("Project Development"),
    PROJECT_GOVERNANCE("Project Governance"),
    TESTING("Testing"),
    UNDEFINED("");

    String name;

    Family(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Family fromString(String name) {
        for (Family family : Family.values()) {
            if (family.getName().equals(name)) {
                return family;
            }
        }
        return UNDEFINED;
    }
}

