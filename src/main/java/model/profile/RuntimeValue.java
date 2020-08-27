package model.profile;

public class RuntimeValue {
    private String type;
    private String repr;

    public RuntimeValue(String type, String repr) {
        this.setRepr(repr);
        this.setType(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRepr() {
        return repr;
    }

    public void setRepr(String repr) {
        this.repr = repr;
    }
}
