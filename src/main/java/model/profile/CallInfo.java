package model.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CallInfo {
    private long callCount;
    private long totalTime;

    public long getCallCount() {
        return callCount;
    }

    public void setCallCount(long callCount) {
        this.callCount = callCount;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public void update(long duration) {
        this.callCount += 1;
        this.totalTime += duration;
    }
}
