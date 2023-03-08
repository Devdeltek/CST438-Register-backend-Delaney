package com.cst438.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.Enrollment;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class StudentController {
	//use status for hold
	
	@Autowired
	StudentRepository studentRepository;
	
	/*
	 *As an administrator, I can add a student to the system.  I input the student email and name.  The student email must not already exists in the system.
	 */
	@PostMapping("/student")
	@Transactional
	public Student addStudent( @RequestBody Map<String, String> json) { 
		String student_email = json.get("student_email");
		String student_name = json.get("student_name");

		
		Student student = studentRepository.findByEmail(student_email);
		
		if (student== null) {
			Student result = new Student();
			result.setName(student_name);
			result.setEmail(student_email);
			result.setStatus(null);
			result.setStatusCode(0);
			Student ret  = studentRepository.save(result);
			
			return ret;
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student already Exists" );
		}
		
	}
	
	/*
	 * As an administrator, I can put a HOLD on a student's registration.
	 */
	@PostMapping("/addHold/{student_id}")
	@Transactional
	public void addHold(@PathVariable int student_id) {
		Student student = studentRepository.findById( student_id).orElse(null);
		if (student!=null) {
		       student.setStatus("Hold");
		       student.setStatusCode(1);
		} else {
		       System.out.println("Student id not found "+student_id);
		       throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student not found. " );
		}
	}
	
	/*
	 * As an administrator, I can release the HOLD on a student's registration
	 */
	
	@PostMapping("/releaseHold/{student_id}")
	@Transactional
	public void releaseHold(@PathVariable int student_id) {
		Student student = studentRepository.findById( student_id).orElse(null);
		if (student!=null) {
		       student.setStatus(null);
		       student.setStatusCode(0);
		} else {
		       System.out.println("Student id not found "+student_id);
		       throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student not found. " );
		}
	}
	
}
