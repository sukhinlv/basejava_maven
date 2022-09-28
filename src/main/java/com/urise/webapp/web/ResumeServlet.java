package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.urise.webapp.util.DateUtil.NOW;

public class ResumeServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = -8026143453684069754L;
    private Storage storage;

    @Override
    public void init() throws ServletException {
        super.init();
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r;
        boolean isNew = (uuid == null) || (uuid.isEmpty());
        if (isNew) {
            r = new Resume(fullName);
        } else {
            r = storage.get(uuid);
            r.setFullName(fullName);
        }
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.putContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }

        var sections = new HashMap<SectionType, AbstractSection>();
        for (SectionType section : SectionType.values()) {
            switch (section) {
                case PERSONAL, OBJECTIVE -> {
                    String value = request.getParameter(section.name());
                    if (value != null && value.trim().length() != 0) {
                        sections.put(SectionType.valueOf(section.name()), new TextSection(value.trim()));
                    }
                }
                case ACHIEVEMENTS, QUALIFICATIONS -> {
                    var values = request.getParameter(section.name());
                    if (values != null) {
                        var valuesList = Arrays.stream(values.replace("\r", "\n")
                                        .split("\\n"))
                                .map(String::trim)
                                .filter(s -> !s.isBlank())
                                .toList();
                        if (valuesList.size() > 0) {
                            sections.put(SectionType.valueOf(section.name()), new ListSection(valuesList));
                        }
                    }
                }
                case EXPERIENCE, EDUCATION -> {
                    var orgSections = request.getParameterValues(section.name());
                    var orgEntries = request.getParameterValues(section.name() + "_ENTRY");

                    var organizationsMap = new HashMap<String, List<Organization.Period>>();
                    if (orgEntries != null) {
                        var periods = new ArrayList<Organization.Period>();
                        for (int i = 0; i < orgEntries.length; i += 5) {
                            String orgEntry = orgEntries[i];
                            String title = orgEntries[i + 1].trim();
                            String desc = orgEntries[i + 2].trim();
                            LocalDate startDate = LocalDate.parse(orgEntries[i + 3] + ".1", DateTimeFormatter.ofPattern("yyyy.M.d"));
                            LocalDate endDate = orgEntries[i + 4].isBlank() ? NOW : LocalDate.parse(orgEntries[i + 4] + ".1", DateTimeFormatter.ofPattern("yyyy.M.d"));
                            if (title.isBlank()) {
                                continue;
                            }

                            if (!organizationsMap.containsKey(orgEntry)) {
                                periods = new ArrayList<>();
                                organizationsMap.put(orgEntry, periods);
                            }
                            periods.add(new Organization.Period(title, desc, startDate, endDate));
                        }
                    }

                    var orgList = new ArrayList<Organization>();
                    if (orgSections != null) {
                        for (int i = 0; i < orgSections.length; i += 3) {
                            String orgSection = orgSections[i];
                            String title = orgSections[i + 1];
                            String website = orgSections[i + 2];
                            List<Organization.Period> periods = organizationsMap.get(orgSection);
                            if (title.isBlank() || periods == null) {
                                continue;
                            }
                            orgList.add(new Organization(title, website, periods));
                        }
                        if (orgList.size() > 0) {
                            sections.put(SectionType.valueOf(section.name()), new OrganizationSection(orgList));
                        }
                    }
                }
            }
        }
        r.setSections(sections);
        if (isNew) {
            storage.save(r);
        } else {
            storage.update(r);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete" -> {
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            }
            case "view", "edit" -> r = storage.get(uuid);
            case "new" -> r = Resume.EMPTY;
            default -> throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")).forward(request, response);
    }
}