package com.ppsdevelopment.viewlib;

import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.envinronment.Credentials;
import com.ppsdevelopment.envinronment.Pagination;
import com.ppsdevelopment.envinronment.UsersSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class PaginationHelper {

    private HttpSession session;
    private UsersSettingsRepository usersSettingsRepository;

    public com.ppsdevelopment.envinronment.Pagination pageSize(Integer pageSizeNew){
        Pagination pagination=getPagination();
        int pageSize=pagination.getPageSize();
        if (pageSize!=pageSizeNew){
            pagination.setPageSize(pageSizeNew);
            pagination.setCurrentPage(1);
            pagination.setFirstPage(1);
            setPagination(pagination);
        }
        return pagination;
    }

    public com.ppsdevelopment.envinronment.Pagination setPage(Integer pageNumber) {
        com.ppsdevelopment.envinronment.Pagination pagination=getPagination();
        pagination.setCurrentPage(pageNumber);
        setPagination(pagination);
        return pagination;
    }

    public Pagination setPageBlock(Integer pageNumber, Integer firstPage) {
        com.ppsdevelopment.envinronment.Pagination pagination=getPagination();
        pagination.setCurrentPage(pageNumber);
        pagination.setFirstPage(firstPage);
        setPagination(pagination);
        return pagination;
    }

    public   Pagination sortMainPage(Integer columnnumber, List<Aliases> aliases){
        com.ppsdevelopment.envinronment.Pagination pagination=getPagination();
        String columnName=pagination.getSortColumnName();
        if (columnName.equals(aliases.get(columnnumber-1).getFieldalias())){
            pagination.setSortDirection(!pagination.isSortDirection());
        }
        else{
            pagination.setSortColumnName(aliases.get(columnnumber-1).getFieldalias());
            pagination.setSortDirection(true);
        }
        pagination.setCurrentPage(1);
        pagination.setFirstPage(1);
        pagination.setSortColumnNumber(columnnumber);
        setPagination(pagination);
    return pagination;
    }

    private com.ppsdevelopment.envinronment.Pagination getPagination(){
        com.ppsdevelopment.envinronment.Pagination pagination;
        if (session.getAttribute("pagination")==null){
            pagination=new com.ppsdevelopment.envinronment.Pagination();
        }
        else
            pagination=(com.ppsdevelopment.envinronment.Pagination) session.getAttribute("pagination");
        return pagination;
    }

    private void setPagination(com.ppsdevelopment.envinronment.Pagination pagination){
        session.setAttribute("pagination",pagination);
        this.usersSettingsRepository.set(Credentials.getUser(),"maintable.pagination",pagination);
    }

    @Autowired
    public void setSession(HttpSession session) {
        this.session = session;
    }

    @Autowired
    public void setUsersSettingsRepository(UsersSettingsRepository usersSettingsRepository) {
        this.usersSettingsRepository = usersSettingsRepository;
    }
}
