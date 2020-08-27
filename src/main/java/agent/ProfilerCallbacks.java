package agent;

import model.profile.FunctionInfo;
import model.profile.Profile;
import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProfilerCallbacks {
    private static Profile profile = new Profile();

    public static Profile getProfile() {
        return profile;
    }

    public static ProfilerToken enterMethod(String name, String descriptor) {
        List<String> args = Arrays.stream(Type.getArgumentTypes(descriptor))
                .map(Type::getDescriptor)
                .collect(Collectors.toList());
        ProfilerToken token = new ProfilerToken(new FunctionInfo(name, args));
        token.getProfile().setStartTime(System.nanoTime());
        return token;
    }

    public static void exitMethod(ProfilerToken token) {
        token.getProfile().setEndTime(System.nanoTime());
        profile.addTrace(token.getProfile());
    }

    public static void beforeCall(
            ProfilerToken token,
            String name,
            String descriptor) {
        token.setTimestamp(System.nanoTime());
    }

    public static void afterCall(
            ProfilerToken token,
            String name,
            String descriptor) {
        long prevTimeStamp = token.getTimestamp();
        long curTimeStamp = System.nanoTime();
        long duration = curTimeStamp - prevTimeStamp;
        token.getProfile().addCall(name, descriptor, duration);
    }

}