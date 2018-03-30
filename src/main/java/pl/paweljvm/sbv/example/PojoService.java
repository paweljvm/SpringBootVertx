package pl.paweljvm.sbv.example;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PojoService {

    private final Map<String,Pojo> dataSource;

    public PojoService() {
        this.dataSource = new HashMap<>();
    }

    public Pojo getById(String id) {
        return dataSource.get(id);
    }
    public String create(Pojo pojo) {
        final String id = UUID.randomUUID().toString();
        dataSource.put(id,pojo);
        return id;
    }
    public Collection<Pojo> getAll() {
        return dataSource.values();
    }
    public void deleteById(String id) {
        dataSource.remove(id);
    }
}
