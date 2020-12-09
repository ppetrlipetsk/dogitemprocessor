package com.ppsdevelopment.envinronment;

import org.springframework.stereotype.Component;

@Component
public class Pagination {
    int pageSize;
    int firstPage;
    int pageCount;

    public Pagination() {
        pageSize=5;
        firstPage=1;
        pageCount=200/pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
