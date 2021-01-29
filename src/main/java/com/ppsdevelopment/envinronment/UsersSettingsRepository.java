package com.ppsdevelopment.envinronment;

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
        UserEnvinronmentSettings settings=getEnv(user);
        Object value=null;
        if (settings!=null)
            value=settings.get(paramName);
        if ((value==null)&&(dbSearch)) {
            value=getFromDataBase(user, paramName, itemClass);
        }
        return value;
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

    private Object getFromDataBase(User user, String paramName, Class<?> itemClass) {
        Object value;
        UsersPref usersPref=  userPrefRepo.getFirstByUserIdAndParamName(user.getId(),paramName);
        if (usersPref!=null) {
            Gson gson = new Gson();
            value=gson.fromJson(usersPref.getSettings(),itemClass);
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
