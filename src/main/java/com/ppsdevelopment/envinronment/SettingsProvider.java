package com.ppsdevelopment.envinronment;

import com.ppsdevelopment.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;

@Service
public class SettingsProvider {
    private HttpSession session;
    private UsersSettingsRepository usersSettingsRepository;

    public Object getSettingsValues(String fieldName, Class<?> instanceClass){
        Object result=null;
        try {
            if (session.getAttribute(fieldName) == null) {
                Object value = usersSettingsRepository.get(Credentials.getUser(), fieldName, instanceClass);
                if ((value != null) && (value.getClass() == instanceClass)) result = instanceClass.cast(value);
                else {
                    try {
                        result = instanceClass.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            } else
                result = session.getAttribute(fieldName);
        }
        catch (Exception e){};
        return result;
    }

    public void setSettingsValue(String fieldName, Object value){
        session.setAttribute(fieldName,value);
        this.usersSettingsRepository.set(Credentials.getUser(),fieldName,value);
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
