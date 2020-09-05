package org.barnhorse.runtimelib.model.profile;

public class FunctionInfo {
    private String name;

    public FunctionInfo(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
