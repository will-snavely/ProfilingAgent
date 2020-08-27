package model.profile;

import java.util.ArrayList;
import java.util.List;

public class FunctionInfo {
    private String name;

    private List<String> args;

    public FunctionInfo(String name, List<String> args) {
        this.setName(name);
        this.setArgs(args);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getArgs() {
        return new ArrayList<>(this.args);
    }

    public void setArgs(List<String> args) {
        this.args = new ArrayList<>(args);
    }
}
