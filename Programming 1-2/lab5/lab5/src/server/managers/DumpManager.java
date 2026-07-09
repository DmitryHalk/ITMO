package server.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.*;
import java.util.*;

import server.common.Clases.Person;


public  class DumpManager {
    private String fileName;
    private HashSet<Person> personList;
    private HashSet<Integer> usedId;
    private CollectionManager cm;

    public DumpManager(String fileName, CollectionManager cm) {
        this.fileName = fileName;
        this.personList = cm.getPersonList();
        this.usedId = cm.getUsedId();
        this.cm = cm;
    }

    public boolean saveToFile() {
        if (fileName == null) {
            System.err.println("Имя файла не задано");
            return false;
        }
        XmlMapper xmlMapper = XmlMapper.builder()
                .addModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        // Устанавливаем формат даты для java.util.Date
        xmlMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName))) {
            xmlMapper.writeValue(bos, personList);
            System.out.println("Коллекция сохранена в файл " + fileName);
            return true;
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении в " + fileName + ": " + e.getMessage());
            return false;
        }
    }

    public void loadFromFile() {
        if (fileName == null) {
            System.err.println("Имя файла не задано, загрузка невозможна");
            return;
        }
        XmlMapper xmlMapper = XmlMapper.builder()
                .addModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();
        xmlMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName))) {
            personList = xmlMapper.readValue(bis, new TypeReference<HashSet<Person>>() {});
            cm.clear();
            for (Person p : personList) {
                usedId.add(p.getId());
            }
            cm.setPersonList(personList);
            cm.setUsedId(usedId);
            System.out.println("Коллекция загружена из " + fileName + ", элементов: " + personList.size());

        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + fileName + " - будет создан новый при сохранении");
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Неизвестная ошибка при загрузке: " + e.getMessage());
            e.printStackTrace();
        }
    }

}