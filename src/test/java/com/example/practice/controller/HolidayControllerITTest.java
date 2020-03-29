package com.example.practice.controller;


import com.example.practice.model.HolidayHistory;
import com.example.practice.model.Visitor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HolidayControllerITTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSaveHistory() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.OK);
        jsonObject.put("message", "Successfully saved holiday history");

        this.mockMvc.perform(post("/holidayhistory")
                .content(asJsonString(new HolidayHistory(null, "13032020", "Shenyang", Collections.emptyList())))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(String.valueOf(jsonObject)));
    }

    @Test
    public void testFindAllHistory() throws Exception {
        this.mockMvc.perform(get("/holidayhistory")).
                andDo(print()).andExpect(status().isOk()).
                andExpect(jsonPath("$.data").exists());
    }

    @Test
    public void testFindHistoryById() throws Exception {
        Visitor visitor1 = new Visitor();
        visitor1.setVid(1);
        visitor1.setFirstName("Amy");
        visitor1.setLastName("Zhang");
        List<Visitor> list1 = new ArrayList<>();
        list1.add(visitor1);
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "29032019", "Shenyang", list1);
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("firstName",visitor1.getFirstName());
        obj.put("lastName",visitor1.getLastName());
        obj.put("vid",visitor1.getVid());
        array.put(obj);
        object.put("id",holidayHistory1.getId());
        object.put("visitorList",array);
        object.put("date",holidayHistory1.getDate());
        object.put("destination",holidayHistory1.getDestination());
        jsonObject.put("status", HttpStatus.OK);
        jsonObject.put("data", object.toString());

        this.mockMvc.perform(get("/holidayhistory/{historyId}", 1)).
                andDo(print()).andExpect(status().isOk()).
                andExpect(content().json(String.valueOf(jsonObject)));
    }

    @Test
    public void testFindHistoryByInvalidId() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.BAD_REQUEST);
        jsonObject.put("message", "Nothing found for history record 5");
        this.mockMvc.perform(get("/holidayhistory/{historyId}", 5)).
                andDo(print()).andExpect(status().isBadRequest()).
                andExpect(content().json(String.valueOf(jsonObject)));
    }

    @Test
    public void testSaveVisitorByHistoryId() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.OK);
        jsonObject.put("message", "Successfully saved visitor in history record 2");

        this.mockMvc.perform(post("/holidayhistory/{historyId}/visitor",2).
                content(asJsonString(new Visitor(4, "Jeff","Fang"))).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json(String.valueOf(jsonObject)));
    }

    @Test
    public void testfindAllVisitorByHistoryId() throws Exception {
        Visitor visitor1 = new Visitor();
        visitor1.setVid(1);
        visitor1.setFirstName("Amy");
        visitor1.setLastName("Zhang");
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("firstName",visitor1.getFirstName());
        obj.put("lastName",visitor1.getLastName());
        obj.put("vid",visitor1.getVid());
        array.put(obj);
        object.put("visitorList",array);
        jsonObject.put("status", HttpStatus.OK);
        jsonObject.put("data", object.toString());

        this.mockMvc.perform(get("/holidayhistory/{historyId}/visitor", 1)).
                andDo(print()).andExpect(status().isOk()).
                andExpect(content().json(String.valueOf(jsonObject)));
    }

    @Test
    public void testFindVisitorByHistoryIdAndOrder() throws Exception {
        Visitor visitor1 = new Visitor(1, "Amy","Zhang");
        JSONObject jsonObject = new JSONObject();
        JSONObject obj = new JSONObject();
        obj.put("firstName", visitor1.getFirstName());
        obj.put("lastName", visitor1.getLastName());
        obj.put("vid", visitor1.getVid());
        jsonObject.put("status", HttpStatus.OK);
        jsonObject.put("data", obj);

        this.mockMvc.perform(get("/holidayhistory/{historyId}/visitor/{order}", 1,1)).
                andDo(print()).andExpect(status().isOk()).
                andExpect(content().json(String.valueOf(jsonObject)));
    }

    @Test
    public void testFindVisitorByInvalidHistoryIdAndOrder() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.BAD_REQUEST);
        jsonObject.put("message", "Nothing found for history record 5");

        this.mockMvc.perform(get("/holidayhistory/{historyId}/visitor/{order}", 5,1)).
                andDo(print()).andExpect(status().isBadRequest()).
                andExpect(content().json(String.valueOf(jsonObject)));
    }

    @Test
    public void testFindVisitorByHistoryIdAndInvalidOrder() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.BAD_REQUEST);
        jsonObject.put("message", "No visitor 5 found for history record 1");

        this.mockMvc.perform(get("/holidayhistory/{historyId}/visitor/{order}", 1,5)).
                andDo(print()).andExpect(status().isBadRequest()).
                andExpect(content().json(String.valueOf(jsonObject)));
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
