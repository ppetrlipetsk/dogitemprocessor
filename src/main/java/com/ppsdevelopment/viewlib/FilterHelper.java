package com.ppsdevelopment.viewlib;

import com.ppsdevelopment.envinronment.Credentials;
import com.ppsdevelopment.envinronment.Pagination;
import com.ppsdevelopment.envinronment.UsersSettingsRepository;
import com.ppsdevelopment.service.FilterQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class FilterHelper {
    private HttpSession session;
    private UsersSettingsRepository usersSettingsRepository;

    public FilterQuery getFilter(String filterName){
            Object value=session.getAttribute(filterName);
            if (value!=null){
                return  (FilterQuery) value;
            }
            else
                return new FilterQuery();
    }

    public void setFilter(String filterName, FilterQuery filter){ //"pagination"
        session.setAttribute(filterName,filter);
        this.usersSettingsRepository.set(Credentials.getUser(),filterName,filter);//"maintable.pagination"
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
