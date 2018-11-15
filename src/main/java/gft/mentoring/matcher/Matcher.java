/*
package gft.mentoring.matcher;

import gft.mentoring.MentoringModel;
import gft.mentoring.sap.model.SAPMentoringModel;
import gft.mentoring.trs.model.TRSMentoringModel;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class Matcher implements BiFunction<SAPMentoringModel, TRSMentoringModel, MentoringModel> {

    List<BiPredicate<SAPMentoringModel, TRSMentoringModel>> predicates;

    @Override
    public MentoringModel apply(SAPMentoringModel sapMentoringModel, TRSMentoringModel trsMentoringModel) {
        if (predicates.stream().allMatch(p -> p.test(sapMentoringModel, trsMentoringModel))) {
            return convert(sapMentoringModel, trsMentoringModel);
        }
        return null;
    }

}
*/
