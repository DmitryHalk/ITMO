package itmo.lab6;

import itmo.lab6.model.Person;
import itmo.lab6.TypeArgs;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String arg;
    private Person entity;

    public Request(String name, String arg, Person entity) {
        this.name = name;
        this.arg = arg;
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    public String getArg() {
        return arg;
    }

    public Person getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", arg=" + arg +
                '}';
    }
}
