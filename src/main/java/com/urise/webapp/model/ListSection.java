package com.urise.webapp.model;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class ListSection extends AbstractSection {
    @Serial
    private static final long serialVersionUID = -1903026708926442619L;
    private List<String> data;

    public ListSection() {
    }

    public ListSection(List<String> data) {
        this.data = requireNonNull(data, "List section data must not be null");
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = requireNonNull(data, "List section data must not be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return "ListSection{" +
                "data=" + data +
                '}';
    }
}
