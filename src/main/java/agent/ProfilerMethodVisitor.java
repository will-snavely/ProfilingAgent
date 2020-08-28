package agent;

import model.config.Configuration;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ProfilerMethodVisitor extends AdviceAdapter {
    private static Method exitMethod;
    private static Method enterMethod;
    private static Method beforeCall;
    private static Method afterCall;

    static Method lookupTraceMethod(String name) throws NoSuchMethodException {
        return Arrays.stream(ProfilerCallbacks.class.getMethods())
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException(name));
    }

    static {
        try {
            exitMethod = lookupTraceMethod("exitMethod");
            enterMethod = lookupTraceMethod("enterMethod");
            beforeCall = lookupTraceMethod("beforeCall");
            afterCall = lookupTraceMethod("afterCall");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private String name;
    private String descriptor;
    private int localId;
    private Configuration config;

    public ProfilerMethodVisitor(
            MethodVisitor mv,
            int access,
            String name,
            String descriptor,
            Configuration config) {
        super(Opcodes.ASM8, mv, access, name, descriptor);
        this.descriptor = descriptor;
        this.name = name;
        this.config = config;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        this.localId = this.newLocal(Type.getType(ProfilerToken.class));
        mv.visitLdcInsn(this.name);
        mv.visitLdcInsn(this.descriptor);
        mv.visitLdcInsn(config.getWorkingDir());
        mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                Type.getType(ProfilerCallbacks.class).getInternalName(),
                enterMethod.getName(),
                Type.getMethodDescriptor(enterMethod),
                false);
        mv.visitVarInsn(
                Type.getType(ProfilerToken.class).getOpcode(Opcodes.ISTORE),
                this.localId);
    }

    @Override
    protected void onMethodExit(int opcode) {
        mv.visitVarInsn(
                Type.getType(ProfilerToken.class).getOpcode(Opcodes.ILOAD),
                this.localId);
        mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                Type.getType(ProfilerCallbacks.class).getInternalName(),
                exitMethod.getName(),
                Type.getMethodDescriptor(exitMethod),
                false);
        super.onMethodExit(opcode);
    }

    @Override
    public void visitMethodInsn(
            int opcode,
            String owner,
            String name,
            String descriptor,
            boolean isInterface) {

        mv.visitVarInsn(
                Type.getType(ProfilerToken.class).getOpcode(Opcodes.ILOAD),
                this.localId);
        mv.visitLdcInsn(name);
        mv.visitLdcInsn(descriptor);
        mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                Type.getType(ProfilerCallbacks.class).getInternalName(),
                beforeCall.getName(),
                Type.getMethodDescriptor(beforeCall),
                false);

        mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);

        mv.visitVarInsn(
                Type.getType(ProfilerToken.class).getOpcode(Opcodes.ILOAD),
                this.localId);
        mv.visitLdcInsn(name);
        mv.visitLdcInsn(descriptor);
        mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                Type.getType(ProfilerCallbacks.class).getInternalName(),
                afterCall.getName(),
                Type.getMethodDescriptor(afterCall),
                false);
    }
}
