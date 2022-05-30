package com.luminn.driver.service;

import java.io.IOException;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.luminn.driver.model.DriverPhoto;
import com.luminn.driver.repository.DriverPhotoRepository;

@Service
public class DriverPhotoService {
	@Autowired
	private DriverPhotoRepository photoRepository;
	
	public String saveDriverPhoto(String name, MultipartFile image) throws IOException {
		DriverPhoto photo = new DriverPhoto();
		photo.setDriverImage(new Binary(BsonBinarySubType.BINARY, image.getBytes()));
		photo.setDriverName(name);
		this.photoRepository.save(photo);
		return name;
	}
	
	public Binary getDriverPhoto(String name) {
		return this.photoRepository.findByDriverName(name).getDriverImage();
	}
}
