package com.urise.webapp.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            SectionReader.read(dis, () -> resume.putContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            SectionReader.read(dis, () -> {
                var type = SectionType.valueOf(dis.readUTF());
                switch (type) {
                    case PERSONAL, OBJECTIVE -> resume.putSection(type, new TextSection(dis.readUTF()));
                    case ACHIEVEMENTS, QUALIFICATIONS ->
                            resume.putSection(type, new ListSection(ListReader.read(dis, dis::readUTF)));
                    case EXPERIENCE, EDUCATION ->
                            resume.putSection(type, new OrganizationSection(ListReader.read(dis, () -> {
                                var title = dis.readUTF();
                                var website = dis.readUTF();
                                return new Organization(title, website, ListReader.read(dis, () -> new Organization.Period(
                                        dis.readUTF(),
                                        dis.readUTF(),
                                        LocalDate.parse(dis.readUTF()),
                                        LocalDate.parse(dis.readUTF()))));
                            })));
                }
            });
            return resume;
        }
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            SectionWriter.write(dos, r.getContacts(), (entry)-> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            SectionWriter.write(dos, r.getSections(), (entry)-> {
                dos.writeUTF(entry.getKey().name());
                switch (entry.getKey()) {
                    case PERSONAL, OBJECTIVE -> dos.writeUTF(((TextSection) entry.getValue()).getData());
                    case ACHIEVEMENTS, QUALIFICATIONS ->
                            ListWriter.write(dos, ((ListSection) entry.getValue()).getData(), dos::writeUTF);
                    case EXPERIENCE, EDUCATION ->
                            ListWriter.write(dos, ((OrganizationSection) entry.getValue()).getData(), organization -> {
                                dos.writeUTF(organization.getTitle());
                                dos.writeUTF(organization.getWebsite());
                                ListWriter.write(dos, organization.getPeriods(), (period) -> {
                                    dos.writeUTF(period.getTitle());
                                    dos.writeUTF(period.getDesc());
                                    dos.writeUTF(period.getStartDate().toString());
                                    dos.writeUTF(period.getEndDate().toString());
                                });
                            });
                }
            });
        }
    }

    @FunctionalInterface
    public interface SectionItemReader {
        void read() throws IOException;
    }

    public static class SectionReader {
        public static void read(DataInputStream dis, SectionItemReader sectionItemReader) throws IOException {
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                sectionItemReader.read();
            }
        }
    }

    @FunctionalInterface
    public interface ListItemsReader<T> {
        T read() throws IOException;
    }

    public static class ListReader {
        public static <T> List<T> read(DataInputStream dis, ListItemsReader<T> listItemsReader) throws IOException {
            var values = new ArrayList<T>();
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                values.add(listItemsReader.read());
            }
            return values;
        }
    }

    @FunctionalInterface
    public interface SectionItemWriter<K, V> {
        void write(Map.Entry<K, V> entry) throws IOException;
    }

    public static class SectionWriter {
        public static <K, V> void write(DataOutputStream dos, Map<K, V> sections, SectionItemWriter<K, V> sectionItemWriter) throws IOException {
            dos.writeInt(sections.size());
            for (Map.Entry<K, V> entry : sections.entrySet()) {
                sectionItemWriter.write(entry);
            }
        }
    }

    @FunctionalInterface
    public interface ListItemsWriter<T> {
        void write(T value) throws IOException;
    }

    public static class ListWriter {
        public static <T> void write(DataOutputStream dos, List<T> sectionData, ListItemsWriter<T> listItemsWriter) throws IOException {
            dos.writeInt(sectionData.size());
            for (var value : sectionData) {
                listItemsWriter.write(value);
            }
        }
    }
}
