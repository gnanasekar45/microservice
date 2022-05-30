package com.luminn.firebase.model;

import com.luminn.firebase.dto.ConfigVersionDTO;
import com.luminn.firebase.entity.ConfigVersion;
import com.luminn.firebase.repository.ConfigRepository;
import com.luminn.firebase.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ConfigVersionModel {

    @Autowired
    ConfigRepository configRepository;

    @Autowired
    ConfigService configService;
    public ModelStatus create(ConfigVersionDTO configVersionDTO) {
        ConfigVersion configVersion = new ConfigVersion();
        configVersion.setPaymentType(configVersionDTO.getPaymentType());
        configVersion.setAndroidUser(configVersionDTO.getAndroidUser());
        configVersion.setAndroidDriver(configVersionDTO.getAndroidDriver());

        configVersion.setIosDriver(configVersionDTO.getIosDriver());
        configVersion.setIosUser(configVersionDTO.getIosUser());

        configVersion.setMandatoryAndroidDriver(configVersionDTO.isMandatoryAndroidDriver());
        configVersion.setMandatoryAndroidUser(configVersionDTO.isMandatoryAndroidUser());
        configVersion.setMandatoryIOSDriver(configVersionDTO.isMandatoryIOSDriver());
        configVersion.setMandatoryIOSUser(configVersionDTO.isMandatoryIOSDriver());


        configRepository.save(configVersion);
        return ModelStatus.CREATED;
    }
    public ModelStatus createEntity(ConfigVersion configVersion) {
        configRepository.save(configVersion);
        return ModelStatus.CREATED;
    }

    public ConfigVersion getId(){
       List<ConfigVersion> version =  configRepository.findAll();
       if(version != null)
         return  version.get(0);

         return  new ConfigVersion();
    }
    public boolean update(ConfigVersionDTO configVersionDTO)  {

        ConfigVersion configVersion = null;
        Optional<ConfigVersion> configVersionss = configRepository.findById(configVersionDTO.getId());
        if(configVersionss.isPresent())
            configVersion =   configVersionss.get();

        configVersion.setPaymentType(configVersionDTO.getPaymentType());
        configVersion.setAndroidUser(configVersionDTO.getAndroidUser());
        configVersion.setAndroidDriver(configVersionDTO.getAndroidDriver());

        configVersion.setIosDriver(configVersionDTO.getIosDriver());
        configVersion.setIosUser(configVersionDTO.getIosUser());

        configVersion.setMandatoryAndroidDriver(configVersionDTO.isMandatoryAndroidDriver());
        configVersion.setMandatoryAndroidUser(configVersionDTO.isMandatoryAndroidUser());
        configVersion.setMandatoryIOSDriver(configVersionDTO.isMandatoryIOSDriver());
        configVersion.setMandatoryIOSUser(configVersionDTO.isMandatoryIOSDriver());



        configRepository.save(configVersion);

        return true;
    }
}
