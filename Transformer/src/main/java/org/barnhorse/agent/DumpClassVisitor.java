package org.barnhorse.agent;

import org.barnhorse.agent.config.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class DumpClassVisitor extends ClassVisitor {
    private org.barnhorse.runtimelib.model.dump.Class classInfo;
    private Configuration config;

    public DumpClassVisitor(ClassVisitor cv, Configuration config) {
        super(Opcodes.ASM8, cv);
        this.classInfo = new org.barnhorse.runtimelib.model.dump.Class();
        this.config = config;
    }

    @Override
    public void visit(
            int version,
            int access,
            String name,
            String signature,
            String superName,
            String[] interfaces) {
        classInfo.setAccess(access);
        if (interfaces != null) {
            classInfo.setInterfaces(Arrays.asList(interfaces));
        }
        classInfo.setName(name);
        classInfo.setVersion(version);
        classInfo.setSignature(signature);
        classInfo.setSuperName(superName);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitEnd() {
        ObjectMapper objectMapper = new ObjectMapper();
        String fileName = this.classInfo.getName().replace("/", "_") + ".json";
        Path path = Paths.get(config.getWorkingDir(), fileName);
        try {
            objectMapper.writeValue(path.toFile(), this.classInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.visitEnd();
    }

    @Override
    public MethodVisitor visitMethod(
            int access,
            String name,
            String desc,
            String signature,
            String[] exceptions) {
        if (this.config.methodMatches(this.classInfo.getName(), name)) {
            org.barnhorse.runtimelib.model.dump.Method method = new org.barnhorse.runtimelib.model.dump.Method();
            method.setAccess(access);
            method.setDesc(desc);
            method.setName(name);
            method.setSignature(signature);
            if (exceptions != null) {
                method.setExceptions(Arrays.asList(exceptions));
            }
            this.classInfo.addMethod(method);
        }
        return super.visitMethod(access, name, desc, signature, exceptions);
    }
}