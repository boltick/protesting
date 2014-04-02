package org.protesting.jft.form.requirements;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ab83625
 * Date: 22.01.2008
 * Time: 11:42:46
 */
public class RequirementList {

    private List requirementList;

    public RequirementList() {
        requirementList = new ArrayList();
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
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < requirementList.size(); i++) {
            Requirement requirement = (Requirement) requirementList.get(i);
            sb.append("\t\t").append(requirement).append("\n");
        }

        return sb.toString();
    }

    // TODO add requirements recognition

    public int size() {
        return requirementList.size();
    }
}
