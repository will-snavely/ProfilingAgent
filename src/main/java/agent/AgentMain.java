package agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.config.Configuration;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

public class AgentMain {
    private static Configuration Config;

    public static Configuration Config() {
        return Config;
    }

    public static Set<String> denyList;

    static {
        denyList = new HashSet<>();
        denyList.add("agent/AgentMain");
        denyList.add("agent/DumpClassVisitor");
        denyList.add("agent/InstructionVisitor");
        denyList.add("agent/ProfilerCallbacks");
        denyList.add("agent/ProfilerClassVisitor");
        denyList.add("agent/ProfilerMethodVisitor");
        denyList.add("agent/ProfilerToken");
        denyList.add("model/config/ClassConfiguration");
        denyList.add("model/config/MethodConfiguration");
        denyList.add("model/config/Configuration");
        denyList.add("model/dump/Class");
        denyList.add("model/dump/Dump");
        denyList.add("model/dump/Method");
        denyList.add("model/profile/CallInfo");
        denyList.add("model/profile/FunctionInfo");
        denyList.add("model/profile/MethodProfile");
        denyList.add("model/profile/Profile");
        denyList.add("model/profile/RuntimeValue");
    }

    static class Transformer implements ClassFileTransformer {
        private byte[] transformImpl(ClassLoader loader,
                                     String className,
                                     Class<?> classBeingRedefined,
                                     ProtectionDomain protectionDomain,
                                     byte[] classfileBuffer) {

            ClassReader reader;
            ClassWriter writer;

            if (denyList.contains(className)) {
                return classfileBuffer;
            }

            switch (Config.getMode()) {
                case "dumpNames":
                    reader = new ClassReader(classfileBuffer);
                    writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
                    DumpClassVisitor pv = new DumpClassVisitor(writer, Config);
                    reader.accept(pv, 0);
                    return writer.toByteArray();
                case "trace":
                    reader = new ClassReader(classfileBuffer);
                    writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
                    ProfilerClassVisitor tv = new ProfilerClassVisitor(writer, Config);
                    reader.accept(tv, ClassReader.EXPAND_FRAMES);
                    return writer.toByteArray();
                default:
                    break;
            }
            return classfileBuffer;
        }

        @Override
        public byte[] transform(
                ClassLoader loader,
                String className,
                Class<?> classBeingRedefined,
                ProtectionDomain protectionDomain,
                byte[] classfileBuffer) {
            try {
                if (Config.classMatches(className)) {
                    return transformImpl(
                            loader,
                            className,
                            classBeingRedefined,
                            protectionDomain,
                            classfileBuffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return classfileBuffer;
        }
    }

    private static Configuration loadConfig(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(path), Configuration.class);
    }

    public static void premain(String arguments, Instrumentation instrumentation) {
        try {
            Config = loadConfig(arguments);
        } catch (IOException e) {
            System.err.println("Failed to load config file from: " + arguments);
            System.err.println("Exception:");
            e.printStackTrace();
            return;
        }
        instrumentation.addTransformer(new Transformer());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String workingDir = Config().getWorkingDir();
                Path path = Paths.get(workingDir, "trace.json");
                objectMapper.writeValue(
                        new FileOutputStream(path.toFile()),
                        ProfilerCallbacks.getProfile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }
}