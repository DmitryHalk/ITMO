package itmo;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;

public class CollectionManager { // Receiver - исполнитель
    private HashSet<Person> personList = new HashSet<>();
    private HashSet<Integer> usedId = new HashSet<>();
    private String fileName;

    public CollectionManager() {

    }

    public CollectionManager(String fileName) {
        this.fileName = fileName;
    }

    public void add(Person p){
        Random r = new Random();
        int id;
        do {
            id = r.nextInt(1000);
        } while (usedId.contains(id));

        p.setId(id);
        usedId.add(id);

        Date now = new Date();
        p.setCreationDate(now);
        personList.add(p);
    }

    public void add(Person p, int id){
        if (usedId.contains(id)) {
            p.setId(id);
            Date now = new Date();
            p.setCreationDate(now);
            removeById(id);
            personList.add(p);
        }
    }

    public boolean getContainsId(int id) {
        if (usedId.contains(id)) return true;
        else return false;
    }

    public void loadFromFile() {
        if(fileName == null) {
            System.err.println("имя файла не задано, загрузка невозможна");
            return;
        }
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName))) {
            XMLDecoder decoder = new XMLDecoder(bis);
            Object obj = decoder.readObject();
            if (obj instanceof HashSet<?>) {
                personList = (HashSet<Person>) obj;
                usedId.clear();
                for (Person p : personList) {
                    usedId.add(p.getId());
                }
                System.out.println("Коллекция загружена из " + fileName);
            } else {
                System.err.println("Error: Файл не содержит HashSet<Person>");
            }
            decoder.close();
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + fileName + " - будет создан новый при сохранении");
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке: " + e.getMessage());
        }
    }

    public boolean saveToFile() {
        if (fileName == null) {
            System.err.println("Имя файла не задано");
            return false;
        }
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName))){
            XMLEncoder encoder = new XMLEncoder(bos);
            encoder.writeObject(personList);
            encoder.close();
            System.out.println("Коллекция сохранена в файл " + fileName);
            return true;
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении в " + fileName);
            return false;
        }

    }

//    public boolean containsId(int id){
//
//    }

    public boolean removeById(Integer id){
        Iterator<Person> iterator = personList.iterator();
        while (iterator.hasNext()) {
            Person p = iterator.next();
            if (p.getId() == id) {
                iterator.remove();
                usedId.remove(id);
                return true;
            }

        }
        return false;

    }

    public int getSize(){
        return personList.size();
    }

    public String getType(){
        return personList.getClass().getSimpleName();
    }

    public HashSet<Person> getSet(){
        return personList;
    }

    public Person getMin(){
        if (personList.size() > 0) {
            List<Person> sortedList = new ArrayList<>(personList);
            Collections.sort(sortedList);
            return sortedList.get(0);
        } else {
            return null;
        }

    }

    public void clear(){
        personList.clear();
    }

}
