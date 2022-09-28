package com.urise.webapp.storage;

import com.urise.webapp.exception.NotFoundStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;
import com.urise.webapp.util.JsonParser;
import com.urise.webapp.util.JsonParserObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;
    private final JsonParser jsonParser = new JsonParserObject();

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid = ?", (ps) -> {
            ps.setString(1, uuid);
            ps.execute();
            if (ps.getUpdateCount() == 0) {
                throw new NotFoundStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(conn -> {
            Resume resume;
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume AS r WHERE r.uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotFoundStorageException(uuid);
                }
                resume = new Resume(uuid, rs.getString("full_name"));
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT type, value FROM contact WHERE resume_uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    readContact(rs, resume);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT value, type FROM section WHERE resume_uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    readSection(rs, resume);
                }

                return resume;
            }
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        var result = new HashMap<String, Resume>();
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                        ResultSet rs = ps.executeQuery();
                        String uuid;
                        String full_name;
                        while (rs.next()) {
                            uuid = rs.getString("uuid").trim();
                            full_name = rs.getString("full_name");
                            Resume r = new Resume(uuid, full_name);
                            result.put(uuid, r);
                        }
                    }

                    try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            readContact(rs, getResumeByUuid(result, rs));
                        }
                    }

                    try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section")) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            readSection(rs, getResumeByUuid(result, rs));
                        }
                    }
                    return null;
                }
        );
        return new ArrayList<>(result.values());
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                        ps.execute();
                    }
                    writeContacts(r, conn);
                    writeSections(r, conn);
                    return null;
                }
        );
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT COUNT(*) FROM resume", (ps) -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                        ps.setString(1, r.getFullName());
                        ps.setString(2, r.getUuid());
                        if (ps.executeUpdate() == 0) {
                            throw new NotFoundStorageException(r.getUuid());
                        }
                    }
                    deleteRecordsByUuid(r.getUuid(), "contact", conn);
                    deleteRecordsByUuid(r.getUuid(), "section", conn);
                    writeContacts(r, conn);
                    writeSections(r, conn);
                    return null;
                }
        );
    }

    private Resume getResumeByUuid(HashMap<String, Resume> result, ResultSet rs) throws SQLException {
        return result.get(rs.getString("resume_uuid").trim());
    }

    private void deleteRecordsByUuid(String uuid, String tableName, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM " + tableName + " WHERE resume_uuid = ?")) {
            ps.setString(1, uuid);
            ps.execute();
        }
    }

    private void readContact(ResultSet rs, Resume resume) throws SQLException {
        String type = rs.getString("type");
        String value = rs.getString("value");
        if (value != null) {
            resume.putContact(ContactType.valueOf(type), value);
        }
    }

    private void readSection(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        var type = SectionType.valueOf(rs.getString("type"));
        if (value != null) {
            switch (type) {
                case PERSONAL, OBJECTIVE -> resume.putSection(type, jsonParser.read(value, TextSection.class));
                case ACHIEVEMENTS, QUALIFICATIONS -> resume.putSection(type, jsonParser.read(value, ListSection.class));
                case EXPERIENCE, EDUCATION -> resume.putSection(type, jsonParser.read(value, OrganizationSection.class));
            }
        }
    }

    private void writeContacts(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void writeSections(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : resume.getSections().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, jsonParser.write(e.getValue()));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}