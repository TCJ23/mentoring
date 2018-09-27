package gft.mentoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.logging.Level;

@Builder(toBuilder = true)
@AllArgsConstructor
@Value
public class MentoringModel {
    private Family family;
    private String specialization;
    private int level;
    private int seniority;
    private String localization;
    private boolean contractor;
    private boolean leaver;

}