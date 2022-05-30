package com.luminn.firebase.service;

import com.luminn.firebase.entity.DriverPhoto;
import com.luminn.firebase.repository.DriverPhotoRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DriverPhotoService {
	@Autowired
	private DriverPhotoRepository photoRepository;
	
	public String saveDriverPhoto(String name, MultipartFile image,String userId,String type) throws IOException {
		DriverPhoto photo = new DriverPhoto();
		photo.setDriverImage(new Binary(BsonBinarySubType.BINARY, image.getBytes()));
		photo.setDriverName(name);
		photo.setUserId(userId);
		photo.setType(type);
		this.photoRepository.save(photo);
		return name;
	}

	public String saveCITUDriverPhoto(String name, MultipartFile image,String userId,String type) throws IOException {
		DriverPhoto photo = new DriverPhoto();
		photo.setDriverImage(new Binary(BsonBinarySubType.BINARY, image.getBytes()));
		photo.setDriverName(name);
		photo.setCituUserId(userId);
		photo.setType(type);
		this.photoRepository.save(photo);
		return name;
	}

	
	public Binary getDriverPhoto(String name) {
		return this.photoRepository.findByDriverName(name).getDriverImage();
	}

	public Binary getDriverUserId(String userId) {

		Optional<DriverPhoto> list = this.photoRepository.findById(userId);
		//List<byte[]> ls = new ArrayList<>();
		//for(DriverPhoto dp : list){
		if(list.isPresent())
			return list.get().getDriverImage();
		//}
		//return new Binary();
		return null;
	}

	public DriverPhoto getDriverUserandId(String userId) {

		List<DriverPhoto> list = this.photoRepository.findByUserId(userId);
		//List<byte[]> ls = new ArrayList<>();
		for(DriverPhoto dp : list){
			Optional<DriverPhoto> opt = photoRepository.findById(dp.getId());

			if(opt.isPresent())
				return opt.get();
		}
		return null;
	}

	public DriverPhoto getCITUDriverUserandId(String userId) {

		List<DriverPhoto> list = this.photoRepository.findByCituUserId(userId);
		//List<byte[]> ls = new ArrayList<>();
		for(DriverPhoto dp : list){
			Optional<DriverPhoto> opt = photoRepository.findById(dp.getId());

			if(opt.isPresent())
				return opt.get();
		}
		return null;
	}

	public DriverPhoto getDriverUserandId(String userId,String type) {

		List<DriverPhoto> list = this.photoRepository.findByUserId(userId);
		//List<byte[]> ls = new ArrayList<>();
		boolean status = false;
		for(DriverPhoto dp : list){
			Optional<DriverPhoto> opt = photoRepository.findById(dp.getId());

			if(opt.isPresent()){
				DriverPhoto driverPhoto =  opt.get();
				if(driverPhoto.getType().equalsIgnoreCase(type)){
					status = false;
					return driverPhoto;
				}
			}
		}
		return null;
	}

	public DriverPhoto getDriverCituUserandId(String userId,String type) {

		List<DriverPhoto> list = this.photoRepository.findByCituUserId(userId);
		//List<byte[]> ls = new ArrayList<>();
		boolean status = false;
		for(DriverPhoto dp : list){
			Optional<DriverPhoto> opt = photoRepository.findById(dp.getId());

			if(opt.isPresent()){
				DriverPhoto driverPhoto =  opt.get();
				if(driverPhoto.getType().equalsIgnoreCase(type)){
					status = false;
					return driverPhoto;
				}
			}
		}
		return null;
	}
	public Binary getDriverUserId(String userId,String type) {
		List<DriverPhoto> list = this.photoRepository.findByUserId(userId);
		List<byte[]> ls = new ArrayList<>();
		for(DriverPhoto dp : list){
			if(dp.getType() != null && dp.getType().equalsIgnoreCase(type))
				return dp.getDriverImage();
			else
				return dp.getDriverImage();
		}
		return null;
	}


}
