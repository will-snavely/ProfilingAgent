package org.barnhorse.agent.config;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private List<ClassConfiguration> classes;
    private String mode;
    private String workingDir;

    public List<ClassConfiguration> getClasses() {
        if (this.classes == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(this.classes);
    }

    public void setClasses(List<ClassConfiguration> classes) {
        if (classes == null) {
            this.classes = new ArrayList<>();
        } else {
            this.classes = new ArrayList<>(classes);
        }
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getWorkingDir() {
        return this.workingDir;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    public boolean classMatches(String className) {
        for (ClassConfiguration classConfig : this.classes) {
            if (classConfig.classMatches(className)) {
                return true;
            }
        }
        return false;
    }

    public boolean methodMatches(String className, String methodName) {
        for (ClassConfiguration classConfig : this.classes) {
            if (classConfig.classMatches(className)) {
                return classConfig.methodMatches(methodName);
            }
        }
        return false;
    }
}
