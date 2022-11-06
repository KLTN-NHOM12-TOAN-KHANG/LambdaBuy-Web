package com.kltn.lambdabuy.webcontroller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.kltn.SpringAPILambdaBuy.entities.BrandEntity;
import com.kltn.lambdabuy.service.BrandService;


@Controller
@RequestMapping("/brand")
public class BrandController {
	 private Logger logger = Logger.getLogger(getClass().getName());
		private BrandService service;
		
		public BrandController(BrandService theService) {
			service = theService;
		}
		
	@GetMapping("/list")
	public String listBrands(Model theModel) {
		
		// get employees from db
		List<BrandEntity>  theBrands = service.findAll();
		
		// add to the spring model
		theModel.addAttribute("brands", theBrands);
                //List<Employee> emp=theEmployees.getEmployees();
                String length= String.valueOf(theBrands.size());
                //for(Employee emp :theEmployees.getEmployees() )
                     logger.info(length);
		
		return "views/brand/list-brands";
	}
}
