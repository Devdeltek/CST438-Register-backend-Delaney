package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cst438.controller.StudentController;
import com.cst438.domain.Enrollment;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration(classes = { StudentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestStudent {
	static final String URL = "http://localhost:8080";
	public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
	public static final String TEST_STUDENT_NAME  = "test";

	@MockBean
	StudentRepository studentRepository;

	@Autowired
	private MockMvc mvc;

	
	@Test
	public void addStudent()  throws Exception {
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		student.setStatusCode(0);
		student.setStudent_id(1);
		
	    given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
	    
	    Student test = new Student();
	    
	    test.setName(TEST_STUDENT_NAME);
	    test.setEmail(TEST_STUDENT_EMAIL);
	    
	    
	    response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/student")
			      .content(asJsonString(test))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
	    
		assertEquals(200, response.getStatus());
		
		
		verify(studentRepository).save(any(Student.class));


		
	}
	
	@Test
	public void addHold()  throws Exception {
		MockHttpServletResponse response;
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		student.setStatusCode(0);
		student.setStudent_id(1);
		
		given(studentRepository.findById(1)).willReturn(Optional.of(student));
		
		response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/addHold/1"))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void releaseHold()  throws Exception {
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		student.setStatusCode(1);
		student.setStudent_id(1);
		
		given(studentRepository.findById(1)).willReturn(Optional.of(student));
		
		response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/releaseHold/1"))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
	}
	
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
