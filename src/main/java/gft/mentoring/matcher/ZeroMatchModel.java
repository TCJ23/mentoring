package gft.mentoring.matcher;

import gft.mentoring.Family;
import lombok.Data;

@Data
class ZeroMatchModel implements SegregationModel {
    private final String firstName;
    private final String lastName;
    private final Family family;
    private final String specialization;
    private final int level;
    private int seniority;
    private String localization;
    private boolean contractor;
    private int menteesAssigned;
    private int age;
}
