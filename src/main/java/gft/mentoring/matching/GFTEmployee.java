package gft.mentoring.matching;

public class GFTEmployee {
    protected long id;
    protected int level;
    Family family;
    protected boolean isDev;

    public boolean checkIfDev() {
        switch (family) {
            case DIGITAL:
                return isDev = true;
            case DEVELOPMENT:
                return isDev = true;
            case ARCHITECTURE:
                return isDev = true;
            case DATA:
                return isDev = true;
            default:
                return false;
        }
    }
}
