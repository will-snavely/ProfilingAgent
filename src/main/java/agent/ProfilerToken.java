package agent;

import model.profile.FunctionInfo;
import model.profile.MethodProfile;

public class ProfilerToken {
    private long timestamp;
    private MethodProfile profile;
    private String workingDirectory;

    public ProfilerToken(FunctionInfo info, String workingDirectory) {
        this.profile = new MethodProfile();
        this.profile.setFunction(info);
        this.workingDirectory = workingDirectory;
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

    public String getWorkingDirectory() { return this.workingDirectory; }
}