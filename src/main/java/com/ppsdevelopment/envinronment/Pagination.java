package com.ppsdevelopment.envinronment;

//@Component
public class Pagination {
    int pageSize; // Размер страницы
    int firstPage; // Номер первой страницы на панели кнопок
    long pageCount; // Количество страниц
    long recordsCount; // Количество записей
    long currentPage; // Текущая страница
    int buttonsCount; // Количество кнопок на панели


    public Pagination() {
        pageSize=5;
        firstPage=1;

//        recordsCount=0;
//        pageCount=200/pageSize;
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

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getRecordsCount() {
        return recordsCount;
    }

    public void setRecordsCount(long recordsCount) {
        this.recordsCount = recordsCount;
        if (this.pageSize!=0){
            this.pageCount=this.recordsCount/this.pageSize;
            if ((this.recordsCount%this.pageSize)>0) this.pageCount++;
        }
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public int getButtonsCount() {
        return buttonsCount;
    }

    public void setButtonsCount(int buttonsCount) {
        this.buttonsCount = buttonsCount;
    }

    public String toValueString() {
        return   "\"pageSize\":"+pageSize
                +",\"firstPage\":"+firstPage
                +",\"pageCount\":"+pageCount
                +",\"recordsCount\":"+recordsCount
                +",\"currentPage\":"+currentPage
                +",\"buttonsCount\":"+buttonsCount;

/*
        int pageSize; // Размер страницы
        int firstPage; // Номер первой страницы на панели кнопок
        long pageCount; // Количество страниц
        long recordsCount; // Количество записей
        long currentPage; // Текущая страница
        int buttonsCount; // Количество кнопок на панели
*/
    }
}
