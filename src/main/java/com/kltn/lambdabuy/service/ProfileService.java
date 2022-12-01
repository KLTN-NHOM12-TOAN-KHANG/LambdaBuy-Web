package com.kltn.lambdabuy.service;

import com.example.kltn.SpringAPILambdaBuy.common.request.profile.UpdateProfileDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProfileResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.example.kltn.SpringAPILambdaBuy.entities.ProfileEntity;

public interface ProfileService {

	ResponseCommon updateProfile(UpdateProfileDto updateProfileDto);

//	ResponseCommon createProfile(Cr)
	ProfileResponseDto getProfileById(String id);
	ProfileEntity getProfileEntityById(String id);

}
