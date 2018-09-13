package com.springmvc.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springmvc.api.config.AppConfig;
import com.springmvc.api.controller.CustomerRestURIConstants;
import com.springmvc.api.model.Customer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
public class SpringMvcTestCustomerControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(CustomerRestURIConstants.GET_ALL_CUSTOMERS))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id").value(101))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))

                .andExpect(jsonPath("$[1].id").value(201))
                .andExpect(jsonPath("$[1].firstName").value("Russ"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))

                .andExpect(jsonPath("$[2].id").value(301))
                .andExpect(jsonPath("$[2].firstName").value("Kate"))
                .andExpect(jsonPath("$[2].lastName").value("Williams"))
                .andReturn();

        // read JSON response and assert values returned
        String jsonContent = mvcResult.getResponse().getContentAsString();
        JSONParser parse = new JSONParser();
        JSONArray jArray = (JSONArray) parse.parse(jsonContent);

        Assert.notNull(jArray);
        Assert.isTrue(jArray.size() == 3);
    }

    @Test
    public void testGetCustomerById() throws Exception {
        this.mockMvc.perform(get(CustomerRestURIConstants.GET_CUSTOMER, "101"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("djohn@gmail.com"))
                .andExpect(jsonPath("$.mobile").value("121-232-3435"))
                .andExpect(jsonPath("$.dateOfBirth").value("1990-12-15"));
    }

    @Test
    public void testGetCustomerByIdFail() throws Exception {
        final String id = "102";
        this.mockMvc.perform(get(CustomerRestURIConstants.GET_CUSTOMER, id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(CustomerRestURIConstants.ERROR_MESSAGE + id));
    }

    @Test
    public void testPostCustomer() throws Exception {
        createCustomer();
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        createCustomer();

        this.mockMvc.perform(delete(CustomerRestURIConstants.DELETE_CUSTOMER, 102))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testDeleteCustomerByIdFail() throws Exception {
        final String id = "103";
        this.mockMvc.perform(get(CustomerRestURIConstants.DELETE_CUSTOMER, id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(CustomerRestURIConstants.ERROR_MESSAGE + id));
    }

    @Test
    public void testPutCustomer() throws Exception {
        Customer customer = createCustomer();
        customer.setFirstName("Janie");
        customer.setLastName("Dumb");
        customer.setEmail("something@nothing.com");
        customer.setMobile("111-222-1234");
        customer.setDateOfBirth(new GregorianCalendar(2015, Calendar.MAY, 29).getTime());

        // create JSON object for POST request
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(customer);

        MvcResult mvcResult = this.mockMvc.perform(put(CustomerRestURIConstants.PUT_CUSTOMER, customer.getId()).content(jsonContent).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        // read JSON response and assert values returned
        jsonContent = mvcResult.getResponse().getContentAsString();
        JSONParser parse = new JSONParser();
        JSONObject jObject = (JSONObject) parse.parse(jsonContent);

        Assert.notNull(jObject);
        Assert.isTrue(jObject.get("id").equals(customer.getId()));
        Assert.isTrue(jObject.get("firstName").equals(customer.getFirstName()));
        Assert.isTrue(jObject.get("lastName").equals(customer.getLastName()));
        Assert.isTrue(jObject.get("email").equals(customer.getEmail()));
        Assert.isTrue(jObject.get("mobile").equals(customer.getMobile()));

        SimpleDateFormat dateFormatter = new SimpleDateFormat(CustomerRestURIConstants.DATE_FORMAT);
        String dob = dateFormatter.format(customer.getDateOfBirth());
        Assert.isTrue(jObject.get("dateOfBirth").equals(dob));
    }

    @Test
    public void testPutCustomerByIdFail() throws Exception {
        // create JSON object for POST request
        Customer customer = createCustomer();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(customer);

        final String id = "103";

        this.mockMvc.perform(put(CustomerRestURIConstants.PUT_CUSTOMER, id).content(jsonContent).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(CustomerRestURIConstants.ERROR_MESSAGE + id));
    }

    private Customer createCustomer() throws Exception {
        final String idKey = "id", firstNameKey = "firstName", lastNameKey = "lastName", emailKey = "email", mobileKey = "mobile", dobKey = "dateOfBirth";
        final String firstName = "Jane", lastName = "Doe", email = "djane@gmail.com", mobile = "121-232-3436", dob = "2005-11-15";
        final long id = 102;

        // create JSON object for POST request
        Customer customer = new Customer(id, firstName, lastName, email, mobile, new GregorianCalendar(2005, Calendar.NOVEMBER, 15).getTime());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(customer);

        MvcResult mvcResult = this.mockMvc.perform(post(CustomerRestURIConstants.POST_CUSTOMER).content(jsonContent).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        // read JSON response and assert values returned
        jsonContent = mvcResult.getResponse().getContentAsString();
        JSONParser parse = new JSONParser();
        JSONObject jObject = (JSONObject) parse.parse(jsonContent);

        Assert.notNull(jObject);
        Assert.isTrue(jObject.get(idKey).equals(id));
        Assert.isTrue(jObject.get(firstNameKey).equals(firstName));
        Assert.isTrue(jObject.get(lastNameKey).equals(lastName));
        Assert.isTrue(jObject.get(emailKey).equals(email));
        Assert.isTrue(jObject.get(mobileKey).equals(mobile));
        Assert.isTrue(jObject.get(dobKey).equals(dob));

        return customer;
    }
}