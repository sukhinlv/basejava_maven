package com.urise.webapp.model;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class  OrganizationSection extends AbstractSection {
    @Serial
    private static final long serialVersionUID = -9064212700326464060L;
    private List<Organization> data;

    public OrganizationSection() {
    }

    public OrganizationSection(List<Organization> data) {
        this.data = requireNonNull(data, "Organization section data must not be null");
    }

    public List<Organization> getData() {
        return data;
    }

    public void setData(List<Organization> data) {
        this.data = requireNonNull(data, "Organization section data must not be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return "OrganizationSection{" +
                "data=" + data +
                '}';
    }
}
