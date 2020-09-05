package org.barnhorse.runtimelib.model.dump;

import java.util.ArrayList;
import java.util.List;

public class Dump {
    private List<Class> classes;

    public List<Class> getClasses() {
        if (this.classes == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(this.classes);
    }

    public void setClasses(List<Class> classes) {
        if (classes == null) {
            this.classes = new ArrayList<>();
        } else {
            this.classes = new ArrayList<>(classes);
        }
    }
}
