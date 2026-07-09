package itmo.lab6;

import itmo.lab6.model.Person;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 2L;

    private String name;
    private String arg;
    private Person entity;
    private String login;
    private String password;

    public Request(String name, String arg, Person entity) {
        this.name = name;
        this.arg = arg;
        this.entity = entity;
    }

    public Request(String name, String arg, Person entity, String login, String password) {
        this.name = name;
        this.arg = arg;
        this.entity = entity;
        this.login = login;
        this.password = password;
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

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Request{name='" + name + "', arg='" + arg + "', login='" + login + "'}";
    }
}