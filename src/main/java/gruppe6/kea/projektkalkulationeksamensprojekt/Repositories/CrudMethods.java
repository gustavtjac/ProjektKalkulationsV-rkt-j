package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;

import java.util.List;

public interface CrudMethods<T, ID> {

    List<T> findAll();

    T findByID(ID id);

    T save(T object);
}
