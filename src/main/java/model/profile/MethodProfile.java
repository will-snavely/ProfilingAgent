package model.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MethodProfile {
    private FunctionInfo function;
    private List<RuntimeValue> actualParameters;
    private Map<String, CallInfo> calls;
    private long startTime;
    private long endTime;
    private int callCount;

    public FunctionInfo getFunction() {
        return function;
    }

    public void setFunction(FunctionInfo function) {
        this.function = function;
    }

    public List<RuntimeValue> getActualParameters() {
        if (this.actualParameters == null) {
            return new ArrayList<>();
        }
        return this.actualParameters;
    }

    public void setActualParameters(List<RuntimeValue> actualParameters) {
        if (actualParameters == null) {
            actualParameters = new ArrayList<>();
        }
        this.actualParameters = new ArrayList<>(actualParameters);
    }

    public Map<String, CallInfo> getCalls() {
        if (this.calls == null) {
            return new TreeMap<>();
        }
        return calls;
    }

    public void setCalls(Map<String, CallInfo> calls) {
        if (calls == null) {
            calls = new TreeMap<>();
        }
        this.calls = new TreeMap<>(calls);
    }

    public void addActualParameter(RuntimeValue rv) {
        if (this.actualParameters == null) {
            this.actualParameters = new ArrayList<>();
        }
        this.actualParameters.add(rv);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getCallCount() {
        return callCount;
    }

    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }

    public void incrementCallCount() {
        this.callCount++;
    }

    private String methodKey(String name, String descriptor) {
        return String.format("%s::%s", name, descriptor);
    }

    public void addCall(String name, String descriptor, long duration) {
        if (this.calls == null) {
            this.calls = new TreeMap<>();
        }

        this.incrementCallCount();
        String key = methodKey(name, descriptor);
        if (!this.calls.containsKey(key)) {
            this.calls.put(key, new CallInfo());
        }
        CallInfo info = this.calls.get(key);
        info.update(duration);
    }
}
