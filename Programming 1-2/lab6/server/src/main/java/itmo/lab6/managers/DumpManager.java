package itmo.lab6.managers;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.*;
import java.util.*;

import itmo.lab6.model.Person;


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

        xmlMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName))) {
            xmlMapper.writeValue(bos, personList);
//            System.out.println("Коллекция сохранена в файл " + fileName);
            return true;
        } catch (IOException e) {
//            System.err.println("Ошибка при сохранении в " + fileName + ": " + e.getMessage());
            return false;
        }
    }

    public void loadFromFile() {
        if (fileName == null) {
            System.err.println("Имя файла не задано, загрузка невозможна");
            return;
        }
        System.out.println("Загрузка из файла: " + fileName);
        XmlMapper xmlMapper = XmlMapper.builder()
                .addModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();
        xmlMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName))) {
            JsonNode root = xmlMapper.readTree(bis);
            if (root == null) {
                System.err.println("Корневой элемент не найден");
                return;
            }


            List<JsonNode> itemNodes = new ArrayList<>();


            if (root.has("item")) {
                JsonNode items = root.get("item");
                if (items.isArray()) {
                    items.forEach(itemNodes::add);
                } else {
                    itemNodes.add(items);
                }
            } else {
                root.elements().forEachRemaining(node -> {
                    if (node.isObject() && !node.isNull()) {
                        itemNodes.add(node);
                    }
                });
            }

            if (itemNodes.isEmpty()) {
                System.out.println("В файле не найдено элементов коллекции.");
                cm.clear();
                return;
            }

            cm.clear();
            int validCount = 0;
            int total = itemNodes.size();

            for (JsonNode itemNode : itemNodes) {
                try {
                    Person p = xmlMapper.treeToValue(itemNode, Person.class);
                    if (p.validate() && !cm.getUsedId().contains(p.getId())) {
                        cm.getSet().add(p);
                        cm.getUsedId().add(p.getId());
                        validCount++;
                    } else {
                        System.err.println("Пропущен невалидный объект с id=" + p.getId());
                    }
                } catch (Exception e) {
                    System.err.println("Пропущен повреждённый объект: " + e.getMessage());
                }
            }

            System.out.println("Коллекция загружена из " + fileName +
                    ", валидных элементов: " + validCount + " из " + total);

        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + fileName + " - будет создан новый при сохранении");
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Неизвестная ошибка при загрузке: " + e.getMessage());
            e.printStackTrace();
        }
    }

}