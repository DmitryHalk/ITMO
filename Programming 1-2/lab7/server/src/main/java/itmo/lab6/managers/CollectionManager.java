package itmo.lab6.managers;

import itmo.lab6.model.Person;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * Потокобезопасный менеджер коллекции.
 * Хранит Person в CopyOnWriteArraySet (java.util.concurrent).
 */
public class CollectionManager {

    // CopyOnWriteArraySet — потокобезопасный аналог из java.util.concurrent
    private final CopyOnWriteArraySet<Person> personSet = new CopyOnWriteArraySet<>();

    // ─────────── Загрузка ───────────

    public synchronized void loadFromDatabase(List<Person> persons) {
        personSet.clear();
        if (persons != null) {
            personSet.addAll(persons);
        }
    }

    // ─────────── Добавление ───────────

    /** Добавляет объект с уже проставленным id и владельцем. */
    public synchronized void addWithId(Person p) {
        personSet.removeIf(existing -> existing.getId() == p.getId());
        personSet.add(p);
    }

    // ─────────── Обновление (только для владельца) ───────────

    /**
     * Обновляет объект. Возвращает false, если id не найден или не принадлежит пользователю.
     */
    public synchronized boolean updateById(int id, Person updated, String ownerLogin) {
        Person old = findById(id);
        if (old == null) return false;
        if (!Objects.equals(ownerLogin, old.getOwnerLogin())) return false;
        updated.setId(id);
        updated.setCreationDate(old.getCreationDate());
        updated.setOwnerLogin(ownerLogin);
        personSet.remove(old);
        personSet.add(updated);
        return true;
    }

    // ─────────── Удаление ───────────

    /** Удаляет по id. Возвращает false, если не найдено или не принадлежит пользователю. */
    public synchronized boolean removeById(int id, String ownerLogin) {
        Person p = findById(id);
        if (p == null) return false;
        if (!Objects.equals(ownerLogin, p.getOwnerLogin())) return false;
        personSet.remove(p);
        return true;
    }

    /** Удаляет всё, что принадлежит пользователю. Возвращает количество удалённых. */
    public synchronized int clearByOwner(String ownerLogin) {
        List<Person> toRemove = personSet.stream()
                .filter(p -> Objects.equals(ownerLogin, p.getOwnerLogin()))
                .collect(Collectors.toList());
        toRemove.forEach(personSet::remove);
        return toRemove.size();
    }

    /** Удаляет объекты владельца, большие чем данный. */
    public synchronized int removeGreater(Person ref, String ownerLogin) {
        List<Person> toRemove = personSet.stream()
                .filter(p -> Objects.equals(ownerLogin, p.getOwnerLogin()) && p.compareTo(ref) > 0)
                .collect(Collectors.toList());
        toRemove.forEach(personSet::remove);
        return toRemove.size();
    }

    /** Удаляет объекты владельца, меньшие чем данный. */
    public synchronized int removeLower(Person ref, String ownerLogin) {
        List<Person> toRemove = personSet.stream()
                .filter(p -> Objects.equals(ownerLogin, p.getOwnerLogin()) && p.compareTo(ref) < 0)
                .collect(Collectors.toList());
        toRemove.forEach(personSet::remove);
        return toRemove.size();
    }

    // ─────────── Поиск ───────────

    public Person findById(int id) {
        return personSet.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public boolean containsId(int id) {
        return findById(id) != null;
    }

    public Person getMin() {
        return personSet.stream().min(Comparator.naturalOrder()).orElse(null);
    }

    // ─────────── Чтение ───────────

    public CopyOnWriteArraySet<Person> getSet() {
        return personSet;
    }

    public int getSize() {
        return personSet.size();
    }

    public String getType() {
        return personSet.getClass().getSimpleName();
    }
}
