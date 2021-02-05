package com.ppsdevelopment.envinronment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.ppsdevelopment.domain.User;
import com.ppsdevelopment.domain.UsersPref;
import com.ppsdevelopment.repos.UserPrefRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
class UsersSettingsRepository {

    private UserPrefRepo userPrefRepo;
    private static Map<User, UserEnvinronmentSettings> settingsCollection =new HashMap<>();


    public Object get(User user, String paramName, Class<?>  itemClass, boolean dbSearch) {
        return get(user,paramName,itemClass,dbSearch,null);
    }

    public Object get(User user, String paramName, Class<?>  itemClass, TypeReference typeRef) {
        return get(user,paramName,itemClass,true, typeRef);
    }

    public Object get(User user, String paramName, Class<?>  itemClass, boolean dbSearch, TypeReference typeRef) {
        UserEnvinronmentSettings settings=getEnv(user);
        Object value=null;
        if (settings!=null)
            value=settings.get(paramName);
        if ((value==null)&&(dbSearch)) {
            value=getFromDataBase(user, paramName, itemClass,typeRef);
        }
        return value;
    }


    public Object getFromDataBase(User user, String paramName) {
        return getFromDataBaseObject(user, paramName);
    }

    private Object getFromDataBase(User user, String paramName, Class<?> itemClass) {
        return getFromDataBase(user, paramName, itemClass, null);
    }

    private Object getFromDataBaseObject(User user, String paramName) {
        Object value=null;
        UsersPref usersPref = userPrefRepo.getFirstByUserIdAndParamName(user.getId(), paramName);
        if (usersPref != null) {
            value = usersPref.getSettings();
            return value;
        } else
            return null;
    }

    public Object get(User user, String paramName, Class<?>  itemClass) {
        return get(user,paramName,itemClass,true);
    }

    public void set(User user, String paramName, Object value, boolean saveToDataBase)
    {
        setToRepository(user, paramName, value);
        if (saveToDataBase) save(user,paramName);
    }

    public void set(User user, String paramName, Object value)
    {
        set(user,paramName,value,true);
    }

    private void setToRepository(User user, String paramName, Object value){
        UserEnvinronmentSettings env=getEnv(user);
        env.put(paramName,value);
    }

    private UserEnvinronmentSettings getEnv(User user) {
        UserEnvinronmentSettings env= settingsCollection.get(user);
        if (env==null) {
            env = new UserEnvinronmentSettings();
            settingsCollection.put(user, env);
        }
        return  env;
    }

    private void save(User user, String paramName) {
        Gson gson = new Gson();
        UsersPref u=new UsersPref();
        UsersPref upref=  userPrefRepo.getFirstByUserIdAndParamName(user.getId(), paramName);
        if (upref!=null)
            u.setId(upref.getId());

        u.setUserId(user.getId());
        u.setObjectname((settingsCollection.get(user).get(paramName)).getClass().getCanonicalName());
        u.setParamName(paramName);

        u.setSettings(gson.toJson(settingsCollection.get(user).get(paramName)));
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
            //value=parceValue(usersPref.getSettings(),itemClass,typeReference);
            value=parceValue(usersPref.getSettings(),itemClass,typeReference);
            setToRepository(user,usersPref.getObjectname(),value);
            return value;
        }
        else
            return null;
    }




    @Autowired
    private void setUserPrefRepo(UserPrefRepo urp){
        this.userPrefRepo=urp;
    }

}
