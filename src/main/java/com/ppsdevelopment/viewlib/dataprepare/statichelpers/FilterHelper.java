package com.ppsdevelopment.viewlib.dataprepare.statichelpers;

import com.ppsdevelopment.envinronment.SettingsManager;
import com.ppsdevelopment.service.FilterQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class FilterHelper {

    //private SettingsManager settingsManager;

    public static FilterQuery getFilter(String filterName, SettingsManager settingsManager){
            Object value=settingsManager.getSettingsValue(filterName,FilterQuery.class);
            try{
                return  (FilterQuery) value;
            }
            catch (Exception e){
                return new FilterQuery();
            }
    }

    public static void setFilter(String filterName, FilterQuery filter, SettingsManager settingsManager){ //"pagination"
        settingsManager.setSettingsValue(filterName,filter);//"maintable.pagination"
    }

//    @Autowired
//    public void setSettingsManager(SettingsManager settingsManager) {
//        this.settingsManager = settingsManager;
//    }

}
