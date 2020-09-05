package org.barnhorse.agent.config;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.regex.Pattern;

public class MethodConfiguration {
    private String name;
    private String descriptor;

    @JsonIgnore
    public Pattern namePattern;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptor() {
        return this.descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public boolean methodMatches(String name) {
        if (this.namePattern == null) {
            this.namePattern = Pattern.compile(this.name);
        }
        return this.namePattern.matcher(name).matches();
    }
}
