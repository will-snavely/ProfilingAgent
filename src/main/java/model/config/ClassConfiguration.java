package model.config;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ClassConfiguration {
    private String name;
    private List<MethodConfiguration> methods;
    private boolean detailedCalls;

    @JsonIgnore
    public Pattern namePattern;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MethodConfiguration> getMethods() {
        if (this.methods == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(this.methods);
    }

    public void setMethods(List<MethodConfiguration> methods) {
        if (methods == null) {
            this.methods = new ArrayList<>();
        } else {
            this.methods = new ArrayList<>(methods);
        }
    }

    public boolean classMatches(String name) {
        if (this.namePattern == null) {
            this.namePattern = Pattern.compile(this.name);
        }
        return this.namePattern.matcher(name).matches();
    }

    public boolean methodMatches(String name) {
        for (MethodConfiguration methodConfig : this.methods) {
            if (methodConfig.methodMatches(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDetailedCalls() {
        return detailedCalls;
    }

    public void setDetailedCalls(boolean detailedCalls) {
        this.detailedCalls = detailedCalls;
    }
}
