package org.barnhorse.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.barnhorse.agent.config.Configuration;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

public class AgentMain {
    public static Set<String> denyList;

    static {
        denyList = new HashSet<>();
        denyList.add("src/main/java/agent/AgentMain");
        denyList.add("src/main/java/agent/DumpClassVisitor");
        denyList.add("src/main/java/agent/InstructionVisitor");
        denyList.add("src/main/java/agent/org.barnhorse.runtimelib.model.ProfilerCallbacks");
        denyList.add("src/main/java/agent/ProfilerClassVisitor");
        denyList.add("src/main/java/agent/ProfilerMethodVisitor");
        denyList.add("src/main/java/agent/org.barnhorse.runtimelib.model.ProfilerToken");
        denyList.add("org/barnhorse/runtimelib/model/org.barnhorse.agent.config/ClassConfiguration");
        denyList.add("org/barnhorse/runtimelib/model/org.barnhorse.agent.config/MethodConfiguration");
        denyList.add("org/barnhorse/runtimelib/model/org.barnhorse.agent.config/Configuration");
        denyList.add("org/barnhorse/runtimelib/model/dump/Class");
        denyList.add("org/barnhorse/runtimelib/model/dump/Dump");
        denyList.add("org/barnhorse/runtimelib/model/dump/Method");
        denyList.add("org/barnhorse/runtimelib/model/profile/CallInfo");
        denyList.add("org/barnhorse/runtimelib/model/profile/FunctionInfo");
        denyList.add("org/barnhorse/runtimelib/model/profile/MethodProfile");
        denyList.add("org/barnhorse/runtimelib/model/profile/Profile");
        denyList.add("org/barnhorse/runtimelib/model/profile/RuntimeValue");
    }

    static class Transformer implements ClassFileTransformer {
        private Configuration config;

        public Transformer(Configuration config) {
            this.config = config;
        }

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

            switch (config.getMode()) {
                case "dumpNames":
                    reader = new ClassReader(classfileBuffer);
                    writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
                    DumpClassVisitor pv = new DumpClassVisitor(writer, config);
                    reader.accept(pv, 0);
                    return writer.toByteArray();
                case "trace":
                    reader = new ClassReader(classfileBuffer);
                    writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
                    ProfilerClassVisitor tv = new ProfilerClassVisitor(writer, config);
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
                if (config.classMatches(className)) {
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
            Configuration config = loadConfig(arguments);
            instrumentation.addTransformer(new Transformer(config));
        } catch (IOException e) {
            System.err.println("Failed to load org.barnhorse.agent.config file from: " + arguments);
            System.err.println("Exception:");
            e.printStackTrace();
        }
    }
}