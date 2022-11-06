package com.kltn.lambdabuy.service;

import java.util.List;

import com.example.kltn.SpringAPILambdaBuy.entities.BrandEntity;


public interface BrandService {
	public List<BrandEntity> findAll();
	
	public BrandEntity findById(String id);
	public void create(BrandEntity entity);
	
	void delete(String id);
}
