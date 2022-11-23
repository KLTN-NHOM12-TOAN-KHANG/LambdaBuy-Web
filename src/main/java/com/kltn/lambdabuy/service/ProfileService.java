package com.kltn.lambdabuy.service;

import com.example.kltn.SpringAPILambdaBuy.common.request.profile.UpdateProfileDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProfileResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;

public interface ProfileService {

	ResponseCommon updateProfile(UpdateProfileDto updateProfileDto);

	ProfileResponseDto getProfileById(String id);

}
