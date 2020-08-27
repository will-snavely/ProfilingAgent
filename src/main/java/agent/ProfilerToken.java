package agent;

import model.profile.FunctionInfo;
import model.profile.MethodProfile;

public class ProfilerToken {
    private long timestamp;
    private MethodProfile profile;

    public ProfilerToken(FunctionInfo info) {
        this.profile = new MethodProfile();
        this.profile.setFunction(info);
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long startTime) {
        this.timestamp = startTime;
    }

    public MethodProfile getProfile() {
        return this.profile;
    }
}