package gft.mentoring.matcher;

import gft.mentoring.Family;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

@AllArgsConstructor
@Value
@Data
class UnifiedModel {
    private String firstName;
    private String lastName;
    private Family family;
    private String specialization;
    private int level;
    private int seniority;
    private String localization;
    private boolean contractor;
    private boolean leaver;
    private int menteesAssigned;
    private int age;
}
