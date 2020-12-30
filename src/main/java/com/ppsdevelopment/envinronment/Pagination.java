package com.ppsdevelopment.envinronment;


import java.io.Serializable;


public class Pagination implements Serializable {
    private int pageSize; // Размер страницы
    private int firstPage; // Номер первой страницы на панели кнопок
    private long pagesCount; // Количество страниц
    private long recordsCount; // Количество записей
    private long currentPage; // Текущая страница
    private int buttonsCount; // Количество кнопок на панели
    private int maxButtonsCount;// Максимальное количество кнопок
    private String sortColumnName; // Столбец сортировки
    private boolean sortDirection;
    private int sortColumnNumber;

    public Pagination(){
        this.setFirstPage(1);
        this.setMaxButtonsCount(5);
        this.setPageSize(3);
        this.setButtonsCount(5);
        this.setCurrentPage(1);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        this.pagesCount =this.recordsCount/pageSize;
        if ((this.recordsCount%pageSize>0)) this.pagesCount++;
        this.buttonsCount= (int) this.pagesCount;
        if (this.buttonsCount>this.maxButtonsCount) this.buttonsCount= this.maxButtonsCount;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public long getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(long pagesCount) {
        this.pagesCount = pagesCount;
    }

    public long getRecordsCount() {
        return recordsCount;
    }

    public void setRecordsCount(long recordsCount) {
        this.recordsCount = recordsCount;
        if (this.pageSize!=0){
            this.pagesCount =this.recordsCount/this.pageSize;
            if ((this.recordsCount%this.pageSize)>0) this.pagesCount++;
            buttonsCount= this.pagesCount>this.maxButtonsCount?this.maxButtonsCount: (int) this.pagesCount;
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
                +",\"pageCount\":"+ pagesCount
                +",\"recordsCount\":"+recordsCount
                +",\"currentPage\":"+currentPage
                +",\"buttonsCount\":"+buttonsCount
                +",\"sortColumnName\":\""+sortColumnName+"\""
                +",\"sortDirection\":"+sortDirection
                +",\"sortColumnNumber\":"+ sortColumnNumber;

/*
        int pageSize; // Размер страницы
        int firstPage; // Номер первой страницы на панели кнопок
        long pageCount; // Количество страниц
        long recordsCount; // Количество записей
        long currentPage; // Текущая страница
        int buttonsCount; // Количество кнопок на панели
*/
    }

    public String getSortColumnName() {
        return sortColumnName;
    }

    public void setSortColumnName(String sortColumnName) {
        this.sortColumnName = sortColumnName;
    }

    public boolean isSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(boolean sortDirection) {
        this.sortDirection = sortDirection;
    }

    public int getMaxButtonsCount() {
        return maxButtonsCount;
    }

    public void setMaxButtonsCount(int maxButtonsCount) {
        this.maxButtonsCount = maxButtonsCount;
    }

    public int getSortColumnNumber() {
        return sortColumnNumber;
    }

    public void setSortColumnNumber(int sortColumnNumber) {
        this.sortColumnNumber = sortColumnNumber;
    }

}
