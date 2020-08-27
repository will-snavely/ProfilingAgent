package model.profile;

import model.dump.Method;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private List<MethodProfile> traces;

    public List<MethodProfile> getTraces() {
        if (this.traces == null) {
            return new ArrayList<>();
        }
        return traces;
    }

    public void setTraces(List<MethodProfile> traces) {
        if (traces == null) {
            traces = new ArrayList<>();
        }
        this.traces = new ArrayList<>(traces);
    }

    public void addTrace(MethodProfile ct) {
        if (this.traces == null) {
            this.traces = new ArrayList<>();
        }
        this.traces.add(ct);
    }
}
