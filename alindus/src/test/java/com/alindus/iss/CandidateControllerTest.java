package com.alindus.iss;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alindus.iss.domain.Address;
import com.alindus.iss.domain.Candidate;
import com.alindus.iss.domain.Phone;
import com.alindus.iss.domain.Role;
import com.alindus.iss.domain.SocialSecurityNumber;
import com.alindus.iss.domain.Technology;
import com.alindus.iss.domain.User;
import com.alindus.iss.domain.Candidate.CandidateStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xml.internal.resolver.readers.TextCatalogReader;

public class CandidateControllerTest extends BaseTest {

	private MockMvc mvc;
	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	//@Test
	public void addCandidateTest() throws Exception {
		
		Phone phone = new Phone(641,345,1212);
		Phone phone1 = new Phone(641,345,1212);
		SocialSecurityNumber ssn = new SocialSecurityNumber(123,34,1234);
		Address address = new Address("1000 N 4th street", "FF", "IA", "52557");
		
		Candidate candidate = new Candidate("amul", "sapkota", "amulsapkota@gmail.com", address, phone,
				ssn, "skypeId", Candidate.CandidateStatus.MARKETING);
		candidate.setSsn(ssn);
		candidate.setPhone(phone);
		candidate.setPhone1(phone1);
		candidate.setEmail1("amul@gmail.com");
		candidate.setSkypeId1("skypeId1");
		candidate.setTechnology(new Technology("JAVA"));
		
		Gson gson = new Gson();
		String json = gson.toJson(candidate);
		//System.out.println("json obj: " + json);
//		MvcResult result =
				mvc
				.perform(MockMvcRequestBuilders.post("/secure/candidate/add")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(json))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
//		System.out.println(result.getResponse().getContentAsString());
//		Candidate can1 = gson.fromJson(result.getResponse().getContentAsString(), Candidate.class);
//		System.out.println("obj: " + can1.getEmail());
	}
	
	@Test
	public void updateCandidateTest() throws Exception {
		
		Gson gson = new Gson();
		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.get("/secure/candidate/findone/18")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		
		/*MvcResult result = mvc.
				perform(MockMvcRequestBuilders.get("/secure/candidate/findone/{id}", "18")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andExpect(content().contentTypeCompatibleWith("application/json"))
        .andReturn();*/
		
		
		System.out.println("response Candidate1 >>>>> " + result.getResponse().getContentAsString());
		Candidate can1 = gson.fromJson(result.getResponse().getContentAsString(), Candidate.class);
		System.out.println("response Candidate2 >>>>> " + can1);
		System.out.println("response ssn "+can1.getSsn());
		can1.setFirstName("Edited Amul");
		can1.setSkypeId("Edited Skype Id");
		Phone phone = new Phone(111,111,2222);
		Phone phone1 = new Phone(222,222,2222);
		SocialSecurityNumber ssn = new SocialSecurityNumber(111,11,1111);
		Address address = new Address("4146 N Belt Line", "Irvin", "Dallas", "75038");
		System.out.println("can1 p1 >>>>>>>>>>>>"+can1);		
		can1.setSsn(ssn);
		can1.setPhone(phone);
		can1.setAddress(address);
		can1.setPhone1(phone1);
		
		System.out.println("can1 p2 >>>>>>>>>>>>"+can1);
		
		mvc.perform(MockMvcRequestBuilders.post("/secure/candidate/update")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.content(gson.toJson(can1)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
	}
	/*
	@Test
	public void viewAllTest() throws Exception {
		Gson gson = new Gson();
		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.get("/secure/user/all").contentType(MediaType.APPLICATION_JSON_VALUE)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		List<User> list = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<User>>() {
		}.getType());
		System.out.println("user list: " + list.toString());
	}*/
}