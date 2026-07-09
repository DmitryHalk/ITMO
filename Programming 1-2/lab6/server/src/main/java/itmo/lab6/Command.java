package itmo.lab6;

public abstract class Command {
    private final String name;
    private final TypeArgs typeArg;

    public Command(String name, TypeArgs typeArg) {
        this.name = name;
        this.typeArg = typeArg;
    }

    public abstract Response execute(Request request);

    public String getName() {
        return name;
    }

    public TypeArgs getTypeArg() {
        return typeArg;
    }
}
