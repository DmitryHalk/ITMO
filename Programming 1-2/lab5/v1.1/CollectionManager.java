package itmo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Date;

public class CollectionManager { // Receiver - исполнитель
    private HashSet<Person> personList = new HashSet<>();
    private HashSet<Integer> usedId = new HashSet<>();

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

    public void clear(){
        personList.clear();
    }

}
