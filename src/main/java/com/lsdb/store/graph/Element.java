package com.lsdb.store.graph;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.HashMap;
import java.util.UUID;

public abstract class Element {

    private final UUID id;
    private final String label;
    private HashMap<String, String> properties;

    public Element(UUID id, String label) {

        this.id = id;
        this.label = label;
        this.properties = new HashMap<>();
    }

    public Element(UUID id, String label, HashMap<String, String> properties) {

        this.id = id;
        this.label = label;
        this.properties = properties;
    }

    public UUID getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public HashMap<String, String> getProperties() { return properties; }

    public void setProperties(HashMap<String, String> properties) { this.properties = properties; }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public String setProperty(String key, String val) {
        return properties.put(key, val);
    }

    public static Builder builder() {
        return new Builder() {
            @Override
            public Builder me() {
                return this;
            }
        };
    }

    public abstract static class Builder<T extends Builder<T>> {

        protected UUID id;
        protected String label;
        protected HashMap<String, String> properties;

        public abstract T me();

        public T id(final UUID id) {
            this.id = id;
            return this.me();
        }

        public T label(final String label) {
            this.label = label;
            return this.me();
        }

        public T properties(final HashMap<String, String> properties) {
            this.properties = properties;
            return this.me();
        }
    }

    public static UUID generateId() {
        return UuidCreator.getTimeOrdered();
    }
}