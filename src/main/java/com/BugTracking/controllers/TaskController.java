package com.BugTracking.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.BugTracking.models.cTask;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;

@RestController
@RequestMapping("/projects/{idProjects}/tasks")
public class TaskController {

	@GetMapping
	@CrossOrigin
	public String getTasks(@PathVariable(value = "idProjects") String idProject,
							@RequestParam(value = "orderBy") String orderField,
							@RequestParam(value = "way") String way,
							@RequestParam(value = "dateFrom") String dateFrom,
							@RequestParam(value = "dateTo") String dateTo,
							@RequestParam(value = "priorityFrom") int priorityFrom,
							@RequestParam(value = "priorityTo") int priorityTo,
							@RequestParam(value = "states") String states,
							@RequestParam(value = "page") int page,
							@RequestParam(value = "count") int count) {
		// Преобразуем в json
//		return ParseToJSON(cProject.getProjects());
		try {
			return new ObjectMapper().writeValueAsString(cTask.getTasks(idProject,orderField,way,dateFrom,
																		dateTo,priorityFrom,priorityTo,states,
																		page,count));
		} catch (JsonProcessingException | SQLException e) {
			e.printStackTrace();
			return "Не удалось преобразовать объекты. " + e.getMessage().toString();
		}
	}

	@PostMapping
	@CrossOrigin
	@ResponseStatus(HttpStatus.OK)
	public String addTask(@RequestParam(value = "name") String name,
			@RequestParam(value = "description") String description, @RequestParam(value = "priority") int priority,
			@RequestParam(value = "dateCreate") String dateCreate,
			@RequestParam(value = "dateModify") String dateModify, @RequestParam(value = "idProject") int idProject) {
		if (cTask.Add(name, description, priority, new Timestamp(Long.parseLong(dateCreate)),
				new Timestamp(Long.parseLong(dateModify)), idProject))
			return "Added successfully";
		else
			return "Server error"; // return Ошибка статус 502 мб

	}

	@PutMapping
	@CrossOrigin
	@ResponseStatus(HttpStatus.OK)
	public String editTask(@RequestParam(value = "id") int id, @RequestParam(value = "name") String name,
			@RequestParam(value = "description") String description, @RequestParam(value = "priority") int priority,
			@RequestParam(value = "dateModify") String dateModify, @RequestParam(value = "idState") int idState) throws NumberFormatException, SQLException {
		if (cTask.Update(id, name, description, priority, new Timestamp(Long.parseLong(dateModify)), idState))
			return "Updated successfully";
		else
			return "Server error"; // return Ошибка статус 502 мб
	}
	
	@DeleteMapping("/{id}")	
	@CrossOrigin
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public String removeTask(@PathVariable("id") int id) {
		// Преобразуем в json
		if (cTask.Remove(id))
			return "Deleted successfully";
		else
			return "Server error"; //return Ошибка статус 502 мб
	}
}
