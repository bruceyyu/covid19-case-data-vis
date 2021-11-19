package comp3111.covid.core;

import java.util.Objects;

public class SortPolicy {
    private final String policy;
    public final SortPolicyE policyE;


    public SortPolicy(String policyName, SortPolicyE policyE) {
        this.policy = policyName;
        this.policyE = policyE;
    }



    @Override
    public String toString() {
        return policy;
    }
}


