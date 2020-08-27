package model.dump;

import java.util.ArrayList;
import java.util.List;

public class Class {
    private int version;
    private int access;
    private String name;
    private String signature;
    private String superName;

    private List<String> interfaces;
    private List<Method> methods;

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getAccess() {
        return this.access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSuperName() {
        return this.superName;
    }

    public void setSuperName(String superName) {
        this.superName = superName;
    }

    public List<String> getInterfaces() {
        if (this.interfaces == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(this.interfaces);
    }

    public void setInterfaces(List<String> interfaces) {
        if (interfaces == null) {
            this.interfaces = new ArrayList<>();
        } else {
            this.interfaces = new ArrayList<>(interfaces);
        }
    }

    public List<Method> getMethods() {
        if (this.methods == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(this.methods);
    }

    public void setMethods(List<Method> methods) {
        if (methods == null) {
            this.methods = new ArrayList<>();
        } else {
            this.methods = new ArrayList<>(methods);
        }
    }

    public void addMethod(Method method) {
        if (this.methods == null) {
            this.methods = new ArrayList<>();
        }
        this.methods.add(method);
    }
}
