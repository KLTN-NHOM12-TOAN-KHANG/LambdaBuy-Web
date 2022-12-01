package com.kltn.lambdabuy.service;

import com.example.kltn.SpringAPILambdaBuy.common.request.image.UploadImageDto;
import com.example.kltn.SpringAPILambdaBuy.entities.ImageEntity;

public interface StorageService {
	public ImageEntity upload(UploadImageDto uploadImageDto);
	public ImageEntity findById(String id);
	public byte[] download(String avatarName);
}
