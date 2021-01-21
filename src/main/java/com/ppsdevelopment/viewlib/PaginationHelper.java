package com.ppsdevelopment.viewlib;

import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.service.viewservices.Pagination;
import com.ppsdevelopment.envinronment.SettingsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaginationHelper {

    private SettingsManager settingsProvider;

    public Pagination pageSize(Integer pageSizeNew, String paginationName){
        Pagination pagination=getPagination(paginationName);
        int pageSize=pagination.getPageSize();
        if (pageSize!=pageSizeNew){
            pagination.setPageSize(pageSizeNew);
            pagination.setCurrentPage(1);
            pagination.setFirstPage(1);
            setPagination(paginationName,pagination);
        }
        return pagination;
    }

    public Pagination setPage(Integer pageNumber, String paginationName) {
        Pagination pagination=getPagination(paginationName);
        pagination.setCurrentPage(pageNumber);
        setPagination(paginationName,pagination);
        return pagination;
    }

    public Pagination setPageBlock(Integer pageNumber, Integer firstPage,String paginationName) {
        Pagination pagination=getPagination(paginationName);
        pagination.setCurrentPage(pageNumber);
        pagination.setFirstPage(firstPage);
        setPagination(paginationName,pagination);
        return pagination;
    }

    public   Pagination sortPage(Integer columnnumber, List<Aliases> aliases, String paginationName){
        Pagination pagination=getPagination(paginationName);
        pagination.setSortColumnNumber(columnnumber);
        String columnName=aliases.get(pagination.getSortColumnNumber()-1).getFieldalias();// pagination.getSortColumnName();

        pagination.setSortColumnName(aliases.get(columnnumber-1).getFieldalias());
        if (columnName.equals(aliases.get(columnnumber-1).getFieldalias())){
            pagination.setSortDirection(!pagination.isSortDirection());
        }
        else{
            pagination.setSortDirection(true);
        }
        pagination.setCurrentPage(1);
        pagination.setFirstPage(1);
        pagination.setSortColumnNumber(columnnumber);
        setPagination(paginationName,pagination);
    return pagination;
    }

    public Pagination getPagination(String paginationName){ //"pagination"
        Object value=settingsProvider.getSettingsValue(paginationName,Object.class);
        if (value!=null){
            return  (Pagination) value;
        }
        else
            return new Pagination();
    }

    public void setPagination(String paginationName, Pagination pagination){ //"pagination"
        settingsProvider.setSettingsValue(paginationName,pagination);
/*
        session.setAttribute(paginationName,pagination);
        this.usersSettingsRepository.set(Credentials.getUser(),paginationName,pagination);//"maintable.pagination"
*/
    }

/*
    @Autowired
    public void setSession(HttpSession session) {
        this.session = session;
    }

    @Autowired
    public void setUsersSettingsRepository(UsersSettingsRepository usersSettingsRepository) {
        this.usersSettingsRepository = usersSettingsRepository;
    }
*/

    @Autowired
    public void setSettingsProvider(SettingsManager settingsProvider) {
        this.settingsProvider = settingsProvider;
    }
}
