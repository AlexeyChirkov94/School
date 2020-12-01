package ua.com.foxminded.school.dao;

import ua.com.foxminded.school.dao.domain.Page;
import ua.com.foxminded.school.dao.domain.Pageable;

import java.util.List;

public interface CrudPageableDao<E> extends CrudDao<E> {

    List<E> findAll(int page, int itemsPerPage);

    default Pageable<E> findAll(Page page, int maxPageNumber) {
        return new Pageable<>(findAll(page.getPageNumber(), page.getItemsPerPage()), page.getPageNumber(),
                page.getItemsPerPage(), maxPageNumber);
    }

    long count();
}
