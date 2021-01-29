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
        return getSettingsValue(fieldName,instanceClass,true);
    }

    public Object getSettingsValue(String fieldName, Class<?> instanceClass, boolean dbSearch){
        Object result=null;
        try {
            if (session.getAttribute(fieldName) == null) {
                Object value = usersSettingsRepository.get(credentials.getUser(), fieldName, instanceClass, dbSearch);
                if (value != null) result = instanceClass.cast(value);
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
        catch (Exception ignored){};
        return result;
    }

    public void setSettingsValue(String fieldName, Object value){
        setSettingsValue(fieldName,value,true);
    }

    public void setSettingsValue(String fieldName, Object value, boolean saveToDataBase){
        session.setAttribute(fieldName,value);
        this.usersSettingsRepository.set(credentials.getUser(),fieldName,value, saveToDataBase);
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
