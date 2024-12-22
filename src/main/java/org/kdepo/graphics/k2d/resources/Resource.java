package org.kdepo.graphics.k2d.resources;

import java.util.Objects;

public class Resource {

    private ResourceType type;
    private String id;
    private String path;

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "type=" + type +
                ", id='" + id + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return type == resource.type
                && Objects.equals(id, resource.id)
                && Objects.equals(path, resource.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, path);
    }

}
