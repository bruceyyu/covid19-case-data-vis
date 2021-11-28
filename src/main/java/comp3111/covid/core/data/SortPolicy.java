package comp3111.covid.core.data;

/**
 * Sort policy class
 */
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


