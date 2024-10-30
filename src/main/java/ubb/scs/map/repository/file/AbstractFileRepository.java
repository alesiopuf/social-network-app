package ubb.scs.map.repository.file;

import ubb.scs.map.domain.Entity;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.memory.InMemoryRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    private String filename;

    public AbstractFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        filename = fileName;
        loadData();
    }

    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            reader.lines().forEach(line -> {
                E entity = createEntity(line);
                super.save(entity);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public abstract E createEntity(String line);

    public abstract String saveEntity(E entity);

    //nu e obligatoriu sa suprascriem
    @Override
    public Optional<E> findOne(ID id) {
        return super.findOne(id);
    }

    @Override
    public Optional<E> save(E entity) {
        Optional<E> e = super.save(entity);
        if (e.isEmpty())
            writeToFile();
        return e;
    }

    private void writeToFile() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (E entity : entities.values()) {
                String ent = saveEntity(entity);
                writer.write(ent);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> e = super.delete(id);
        if (e.isPresent())
            writeToFile();
        return e;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> e = super.update(entity);
        if (e.isEmpty())
            writeToFile();
        return e;
    }
}
