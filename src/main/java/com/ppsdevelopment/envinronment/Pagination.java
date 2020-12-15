package com.ppsdevelopment.envinronment;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;


public class Pagination implements Serializable {
    int pageSize; // Размер страницы
    int firstPage; // Номер первой страницы на панели кнопок
    long pageCount; // Количество страниц
    long recordsCount; // Количество записей
    long currentPage; // Текущая страница
    int buttonsCount; // Количество кнопок на панели
    int maxButtonsCount;// Максимальное количество кнопок
    String sortColumnName; // Столбец сортировки
    boolean sortDirection;
    int sortingColumnNumber;


/*
    public Pagination() {
        pageSize=5;
        firstPage=1;

//        recordsCount=0;
//        pageCount=200/pageSize;
    }
*/
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
        this.pageCount=this.recordsCount/pageSize;
        if ((this.recordsCount%pageSize>0)) this.pageCount++;
        this.buttonsCount= (int) this.pageCount;
        if (this.buttonsCount>this.maxButtonsCount) this.buttonsCount= this.maxButtonsCount;
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
                +",\"buttonsCount\":"+buttonsCount
                +",\"sortColumnName\":\""+sortColumnName+"\""
                +",\"sortDirection\":\""+sortDirection+"\""
                +",\"sortingColumnNumber\":"+sortingColumnNumber;

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

    public int getSortingColumnNumber() {
        return sortingColumnNumber;
    }

    public void setSortingColumnNumber(int sortingColumnNumber) {
        this.sortingColumnNumber = sortingColumnNumber;
    }
}
