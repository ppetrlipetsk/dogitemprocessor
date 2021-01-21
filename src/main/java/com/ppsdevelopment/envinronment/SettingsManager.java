package com.ppsdevelopment.envinronment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;

@Service
public class SettingsManager {
    private HttpSession session;
    private UsersSettingsRepository usersSettingsRepository;
    private Credentials credentials;

    public Object getSettingsValue(String fieldName, Class<?> instanceClass){
        Object result=null;
        try {
            if (session.getAttribute(fieldName) == null) {
                Object value = usersSettingsRepository.get(credentials.getUser(), fieldName, instanceClass);
                //if ((value != null) && (value.getClass() == instanceClass)) result = instanceClass.cast(value);
                if (value != null) result = instanceClass.cast(value);
                        //&& (value.getClass() == instanceClass))
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
        this.usersSettingsRepository.set(credentials.getUser(),fieldName,value);
    }


    @Autowired
    public void setSession(HttpSession session) {
        this.session = session;
    }

    @Autowired
    public void setUsersSettingsRepository(UsersSettingsRepository usersSettingsRepository) {
        this.usersSettingsRepository = usersSettingsRepository;
    }

    @Autowired
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
