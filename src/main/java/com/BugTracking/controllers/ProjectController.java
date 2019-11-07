package com.BugTracking.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.BugTracking.models.cProject;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletResponse;

@RestController
@RequestMapping("/projects")
public class ProjectController {

	@GetMapping
	@CrossOrigin
	public String getProjects(ServletResponse response,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "count") int count) {
		// Преобразуем в json
		try {
			return new ObjectMapper().writeValueAsString(cProject.getProjects(page,count));
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@DeleteMapping("/{id}")	
	@CrossOrigin
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public String removeProject(@PathVariable("id") int id) {
		// Преобразуем в json
		if (cProject.Remove(id))
			return "Deleted successfully";
		else
			return "Server error"; //return Ошибка статус 502 мб
	}

	@PostMapping
	@CrossOrigin
	@ResponseStatus(HttpStatus.OK)
	public String addProject(@RequestParam(value = "name") String name,
								@RequestParam(value = "description") String description,
								@RequestParam(value = "dateCreate") String dateCreate, 
								@RequestParam(value = "dateModify") String dateModify) {
//	public String addProjects(@RequestBody cProject proj) {
//		if (cProject.Add(name, description, dateCreate, dateModify))
//		if (cProject.Add(proj.getName(), proj.getDescription(), proj.getDateCreate(), proj.getDateModify()))
		if (cProject.Add(name, description, new Timestamp(Long.parseLong(dateCreate)), 
							new Timestamp(Long.parseLong(dateModify))))
			return "Added successfully";
		else
			return "Server error"; //return Ошибка статус 502 мб

//		try {
//			return new ObjectMapper().writeValueAsString(cProject.getProjects());
//		} catch (SQLException | IOException e) {
//			e.printStackTrace();
//			return null;
//		}
	}
	
	@PutMapping
	@CrossOrigin
	@ResponseStatus(HttpStatus.OK)
	public String editProject(@RequestParam(value = "id") int id,
								@RequestParam(value = "name") String name,
								@RequestParam(value = "description") String description,
								@RequestParam(value = "dateModify") String dateModify) {
		if (cProject.Update(id,name, description, new Timestamp(Long.parseLong(dateModify))))
			return "Updated successfully";
		else
			return "Server error"; //return Ошибка статус 502 мб
	}
}
