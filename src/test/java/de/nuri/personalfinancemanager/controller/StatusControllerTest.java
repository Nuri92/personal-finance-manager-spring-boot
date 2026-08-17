package de.nuri.personalfinancemanager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatusController.class)
class StatusControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldReturnApplicationStatus() throws Exception {
		mockMvc.perform(get("/api/status"))
		       .andExpect(status().isOk())
		       .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		       .andExpect(jsonPath("$.status")
				       .value("Personal Finance Manager is running"));
	}
}