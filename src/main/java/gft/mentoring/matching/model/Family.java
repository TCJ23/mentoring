package gft.mentoring.matching.model;

public enum Family {
    AMS,
    ARCHITECTURE,
    BUSINESS_CONSULTING,
    CORPORATE_SERVICES,
    DATA,
    DIGITAL,
    EXECUTIVE_DIRECTOR,
    PROJECT_DEVELOPMENT,
    PROJECT_GOVERNANCE,
    TESTING;

    public boolean isDevelopmentGroup() {
        switch (this) {
            case DIGITAL:
            case PROJECT_DEVELOPMENT:
            case ARCHITECTURE:
            case DATA:
                return true;
            default:
                return false;
        }
    }
}
