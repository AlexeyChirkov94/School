package ua.com.foxminded.school.dao.domain;

import java.util.Objects;

public class Page {
    private final int pageNumber;
    private final int itemsPerPage;

    public Page(int pageNumber, int itemsPerPage) {
        this.pageNumber = pageNumber;
        this.itemsPerPage = itemsPerPage;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Page page = (Page) o;
        return pageNumber == page.pageNumber &&
                itemsPerPage == page.itemsPerPage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageNumber, itemsPerPage);
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNumber=" + pageNumber +
                ", itemsPerPage=" + itemsPerPage +
                '}';
    }
}
