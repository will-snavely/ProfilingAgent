package agent;

import model.config.Configuration;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ProfilerClassVisitor extends ClassVisitor {
    private Configuration config;
    private String className;

    public ProfilerClassVisitor(ClassWriter writer, Configuration config) {
        super(Opcodes.ASM8, writer);
        this.config = config;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println("Class: " + name);
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
    }

    @Override
    public MethodVisitor visitMethod(
            int access,
            String name,
            String desc,
            String signature,
            String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        System.out.println("\tMethod: " + name + desc);
        if (name.contains("<init>")) {
            return mv;
        }
        if (this.config.methodMatches(this.className, name)) {
            mv = new ProfilerMethodVisitor(mv, access, name, desc);
        }
        return mv;
    }
}