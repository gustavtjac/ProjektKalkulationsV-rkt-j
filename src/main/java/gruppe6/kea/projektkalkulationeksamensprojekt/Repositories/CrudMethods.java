package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;

public interface CrudMethods<T,ID>{

    T findAll();

    T findByID(ID id);

    T save(T object);



}
