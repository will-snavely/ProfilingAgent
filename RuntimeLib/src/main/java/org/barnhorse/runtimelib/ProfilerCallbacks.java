package org.barnhorse.runtimelib;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.AtomicLongMap;
import org.barnhorse.runtimelib.model.profile.FunctionInfo;
import org.barnhorse.runtimelib.model.profile.MethodProfile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProfilerCallbacks {
    private static AtomicLongMap<String> functionCounter = AtomicLongMap.create();

    public static ProfilerToken enterMethod(
            String name,
            String descriptor,
            String workingDirectory) {
        ProfilerToken token = new ProfilerToken(
                new FunctionInfo(name),
                workingDirectory);
        token.getProfile().setStartTime(System.nanoTime());
        return token;
    }

    public static void exitMethod(ProfilerToken token) {
        token.getProfile().setEndTime(System.nanoTime());
        dumpProfile(token.getProfile(), token.getWorkingDirectory());
    }

    private static void dumpProfile(MethodProfile profile, String workingDirectory) {
        FileOutputStream out = null;
        String functionName = profile.getFunction().getName();
        Long counter = functionCounter.getAndIncrement(functionName);
        Path path = Paths.get(
                workingDirectory,
                String.format("%s_%d.json", functionName, counter));

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
