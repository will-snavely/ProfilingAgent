package agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.AtomicLongMap;
import model.profile.FunctionInfo;
import model.profile.MethodProfile;
import org.objectweb.asm.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProfilerCallbacks {
    private static AtomicLongMap<String> functionCounter = AtomicLongMap.create();

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
        dumpProfile(token.getProfile());
    }

    private static void dumpProfile(MethodProfile profile) {
        FileOutputStream out = null;
        String workingDir = AgentMain.Config().getWorkingDir();
        String functionName = profile.getFunction().getName();
        Long counter = functionCounter.getAndIncrement(functionName);
        Path path = Paths.get(workingDir, String.format("%s_%d.json", functionName, counter));

        try {
            out = new FileOutputStream(path.toFile());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(out, profile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
