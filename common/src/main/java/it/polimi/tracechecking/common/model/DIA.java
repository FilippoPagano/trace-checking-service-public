package it.polimi.tracechecking.common.model;

import java.util.List;

public class DIA {

    private String diaName;
    private List<String> mtlFormulae;
    private Integer intervalBetweenChecks;

    public DIA() {
    }

    public List<String> getMtlFormulae() {
        return mtlFormulae;
    }

    public void setMtlFormulae(List<String> mtlFormulae) {
        this.mtlFormulae = mtlFormulae;
    }

    public Integer getIntervalBetweenChecks() {
        return intervalBetweenChecks;
    }

    public void setIntervalBetweenChecks(Integer intervalBetweenChecks) {
        this.intervalBetweenChecks = intervalBetweenChecks;
    }

    public String getDiaName() {
        return diaName;
    }

    public void setDiaName(String diaName) {
        this.diaName = diaName;
    }


}
