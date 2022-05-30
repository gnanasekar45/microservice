package com.luminn.firebase.model;

import com.luminn.firebase.entity.Store;
import com.luminn.firebase.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreModel {

    @Autowired
    StoreService storeService;

    public void create(String key,String value){
        Store store = new Store();
        store.setKey(key);
        store.setValue(value);
        //storeService.create(store);
    }

    public String findByKey(String key){
        //List<Store> listStore =   storeService.list(Store.class);
        List<Store> listStore =  null;
        for(Store store : listStore){
            if(store.getKey().equalsIgnoreCase(key))
                return store.getValue();
        }
        return "";
    }
}
