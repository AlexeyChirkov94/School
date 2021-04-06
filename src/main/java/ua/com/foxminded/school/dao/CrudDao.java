package ua.com.foxminded.school.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CrudDao<E> {

    void save(E entity);

    void saveAll(List<E> entities);

    Optional<E> findById(Integer id);

    List<E> findAll();

    void update(E entity);

    void updateAll(List<E> entities);

    void deleteById(Integer id);

    void deleteByIds (Set<Integer> id);

}
