package com.codedecode.microservices.VaccinationCenter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.codedecode.microservices.VaccinationCenter.Entity.VaccinationCenter;
import com.codedecode.microservices.VaccinationCenter.Model.Citizen;
import com.codedecode.microservices.VaccinationCenter.Model.RequiredResponse;
import com.codedecode.microservices.VaccinationCenter.repositories.CenterRepo;


@RestController
@RequestMapping("/vaccinationcenter")
public class VaccinationCenterController {

	@Autowired
	private CenterRepo centerRepo;

	@Autowired
	private RestTemplate restTemplate;

	@PostMapping(path = "/add")
	public ResponseEntity<VaccinationCenter> addCitizen(@RequestBody VaccinationCenter vaccinationCenter) {

		VaccinationCenter vaccinationCenterAdded = centerRepo.save(vaccinationCenter);
		return new ResponseEntity<>(vaccinationCenterAdded, HttpStatus.OK);
	}

	@GetMapping(path = "/id/{id}")
	//@HystrixCommand(fallbackMethod = "handleCitizenDownTime")
	public ResponseEntity<RequiredResponse> getAllDadaBasedonCenterId(@PathVariable Integer id) {
		RequiredResponse requiredResponse = new RequiredResponse();
		VaccinationCenter center = centerRepo.findById(id).get();
		requiredResponse.setCenter(center);

		List<Citizen> listOfCitizens = restTemplate.getForObject("http://localhost:8083/citizen/id/" + id, List.class);
		requiredResponse.setCitizens(listOfCitizens);
		return new ResponseEntity<RequiredResponse>(requiredResponse, HttpStatus.OK);
	}

	/*
	 * public ResponseEntity<RequiredResponse> handleCitizenDownTime(@PathVariable
	 * Integer id) { RequiredResponse requiredResponse = new RequiredResponse();
	 * VaccinationCenter center = centerRepo.findById(id).get();
	 * requiredResponse.setCenter(center); return new
	 * ResponseEntity<RequiredResponse>(requiredResponse, HttpStatus.OK);
	 * 
	 * }
	 */

}
