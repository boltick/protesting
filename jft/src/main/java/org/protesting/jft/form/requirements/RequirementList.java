package org.protesting.jft.form.requirements;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ab83625
 * Date: 22.01.2008
 * Time: 11:42:46
 */
public class RequirementList {

    private List<Requirement> requirementList;

    public RequirementList() {
        requirementList = new ArrayList<Requirement>();
    }

    public List getRequirementList() {
        return requirementList;
    }

    public void addRequirement(Requirement req) {
        requirementList.add(req);
    }

    public Requirement get(int i) {
        return (Requirement) requirementList.get(i);
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Requirement aRequirement : requirementList) {
            sb.append("\t\t").append(aRequirement).append("\n");
        }

        return sb.toString();
    }

    // TODO add requirements recognition

    public int size() {
        return requirementList.size();
    }
}
