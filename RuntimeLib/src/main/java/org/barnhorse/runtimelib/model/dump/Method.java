package org.barnhorse.runtimelib.model.dump;

import java.util.ArrayList;
import java.util.List;

public class Method {
    private int access;
    private String name;
    private String desc;
    private String signature;
    private List<String> exceptions;

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

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<String> getExceptions() {
        if (this.exceptions == null) {
            return new ArrayList<>();
        }
        return this.exceptions;
    }

    public void setExceptions(List<String> exceptions) {
        if (exceptions == null) {
            this.exceptions = new ArrayList<>();
        } else {
            this.exceptions = new ArrayList<>(exceptions);
        }
    }


}

