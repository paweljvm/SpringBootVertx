package pl.paweljvm.sbv.example;

public class Pojo {
    private final String prop1;
    private final boolean prop2;

    public Pojo(String prop1, boolean prop2) {
        this.prop1 = prop1;
        this.prop2 = prop2;
    }

    public String getProp1() {
        return prop1;
    }

    public boolean isProp2() {
        return prop2;
    }
}
