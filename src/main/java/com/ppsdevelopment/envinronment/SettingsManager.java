package com.ppsdevelopment.envinronment;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

@Service
public class SettingsManager {
    private HttpSession session;
    private UsersSettingsRepository usersSettingsRepository;
    private Credentials credentials;


    public Object getSettingsValue(String fieldName, Class<?> instanceClass){
        return getSettingsValue(fieldName,instanceClass,true, null);
    }

    public Object getSettingsValue(String fieldName, Class<?> instanceClass, boolean dbSearch, TypeReference typeRef){
        Object result=null;
        try {
            if (session.getAttribute(fieldName) == null) {
                Object value = usersSettingsRepository.get(credentials.getUser(), fieldName, instanceClass, typeRef);
                if ((value != null) &&(instanceClass!=null)) result = instanceClass.cast(value);
                else {
                    if (value==null) {
                        try {
                            result = instanceClass.getDeclaredConstructor().newInstance();
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                        result=value;
                    }
            } else
                result = session.getAttribute(fieldName);
        }
        catch (Exception ignored){};
        return result;
    }

    public Object getSettingsValue(String fieldName, Class<?> instanceClass, TypeReference typeRef){
        return getSettingsValue(fieldName,instanceClass, true, typeRef);
    }

    public Object getFromDataBase(String fieldName, Class<?> instanceClass) {
        Object result = null;
        Object value = usersSettingsRepository.getFromDataBase(credentials.getUser(), fieldName);
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
