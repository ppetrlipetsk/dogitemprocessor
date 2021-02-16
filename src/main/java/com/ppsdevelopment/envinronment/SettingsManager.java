package com.ppsdevelopment.envinronment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ppsdevelopment.domain.User;
import com.ppsdevelopment.domain.UsersPref;
import com.ppsdevelopment.repos.UserPrefRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

@Service
public class SettingsManager {
    private HttpSession session;
    private Credentials credentials;
    private UserPrefRepo userPrefRepo;

    public Object getSettingsValue(String fieldName, Class<?> instanceClass){
        return getSettingsValue(fieldName,instanceClass,true, null);
    }

    public Object getSettingsValue(String fieldName, Class<?> instanceClass, boolean dbSearch, TypeReference typeRef){
        Object result=null;
        try {
            if ((session.getAttribute(fieldName) == null)) {
                Object value =null;
                if (dbSearch) {
                    value = getFromStorage(credentials.getUser(), fieldName, instanceClass, typeRef);
                    if (value!=null) setSettingsValue(fieldName,value);
                }
                if ((value != null) &&(instanceClass!=null)) result = instanceClass.cast(value);
                else {
                    if (value==null) {
                        try {
                            result = instanceClass.getDeclaredConstructor().newInstance();
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
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

    public void setSettingsValue(String fieldName, Object value){
        setSettingsValue(fieldName,value,true);
    }

    public void setSettingsValue(String fieldName, Object value, boolean saveToDataBase){
        session.setAttribute(fieldName,value);
        if (saveToDataBase) setToStorage(credentials.getUser(),fieldName,value);
    }

    public Object getFromStorage(User user, String paramName, Class<?>  itemClass, TypeReference typeRef) {
        return getFromDataBase(user, paramName, itemClass,typeRef);
    }

    public void setToStorage(User user, String paramName, Object value)
    {
        save(user,paramName, value);
    }

    private void save(User user, String paramName, Object value) {
        Gson gson = new Gson();
        UsersPref u=new UsersPref();
        UsersPref upref=  userPrefRepo.getFirstByUserIdAndParamName(user.getId(), paramName);
        if (upref!=null)
            u.setId(upref.getId());

        u.setUserId(user.getId());
        u.setObjectname(value.getClass().getCanonicalName());
        u.setParamName(paramName);
        u.setSettings(gson.toJson(value));
        userPrefRepo.save(u);
    }

    private Object parceValue(Object value, Class itemClass, TypeReference typeReference){
        if (typeReference == null) {
            Gson gson = new Gson();
            value=gson.fromJson((String)value,itemClass);
            return value;
        }
        else{
            ObjectMapper mapper = new ObjectMapper();
            try{
                return  mapper.readValue((String) value,typeReference);
            }
            catch (Exception ignored){}
        }
        return null;
    }

    private Object getFromDataBase(User user, String paramName, Class<?> itemClass, TypeReference typeReference) {
        Object value;
        UsersPref usersPref=  userPrefRepo.getFirstByUserIdAndParamName(user.getId(),paramName);
        if (usersPref!=null) {
            value=parceValue(usersPref.getSettings(),itemClass,typeReference);
            return value;
        }
        else
            return null;
    }

    @Autowired
    private void setUserPrefRepo(UserPrefRepo urp){
        this.userPrefRepo=urp;
    }

    @Autowired
    public void setSession(HttpSession session) {
        this.session = session;
    }

    @Autowired
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }



}
