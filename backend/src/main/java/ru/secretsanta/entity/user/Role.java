package ru.secretsanta.entity.user;

public enum Role {

    ADMIN("ADMIN"),
    USER("USER");
    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
