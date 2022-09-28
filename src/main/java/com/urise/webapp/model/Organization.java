package com.urise.webapp.model;

import com.urise.webapp.util.LocalDateAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {
    @Serial
    private static final long serialVersionUID = 1884140744856283600L;
    private String title;
    private String website;
    private List<Period> periods;

    public Organization() {
    }

    public Organization(String title, String website, List<Period> periods) {
        this.title = requireNonNull(title, "Organization title  must not be null");
        this.website = website;
        this.periods = requireNonNull(periods, "Periods must not be null");
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = requireNonNull(periods, "Periods must not be null");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = requireNonNull(title, "Organization title  must not be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(title, that.title) && Objects.equals(website, that.website);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, website);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "title='" + title + '\'' +
                ", link='" + website + '\'' +
                ", periods=" + periods +
                '}';
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Period implements Serializable {
        @Serial
        private static final long serialVersionUID = -8776575920028854076L;
        private String title;
        private String desc;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate startDate;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate endDate;

        public Period() {
        }

        public Period(String title, String desc, LocalDate startDate, LocalDate endDate) {
            this.title = requireNonNull(title, "Title must not be null");
            this.desc = desc;
            this.startDate = requireNonNull(startDate, "Start date must not be null");
            this.endDate = requireNonNull(endDate, "End date must not be null");
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = requireNonNull(endDate, "End date must not be null");
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = requireNonNull(startDate, "Start date must not be null");
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = requireNonNull(title, "Title must not be null");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Period period = (Period) o;
            return Objects.equals(title, period.title) && Objects.equals(desc, period.desc) &&
                    Objects.equals(startDate, period.startDate) && Objects.equals(endDate, period.endDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, desc, startDate, endDate);
        }

        @Override
        public String toString() {
            return "Period{" +
                    "title='" + title + '\'' +
                    ", desc='" + desc + '\'' +
                    ", startDate=" + startDate +
                    ", endDate=" + endDate +
                    '}';
        }
    }
}