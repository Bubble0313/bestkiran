package com.example.practice.controller;

//import com.example.practice.Handler.NotFoundAdvice;
import com.example.practice.model.HolidayHistory;
import com.example.practice.model.Visitor;
import com.example.practice.repository.HolidayRepository;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

//import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HolidayControllerTest {

    @Mock
    private HolidayRepository holidayRepository;

    @InjectMocks
    private HolidayController holidayController = new HolidayController();

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNotNull(){
        Assertions.assertThat(this.holidayController).isNotNull();
    }

    //1st level
    @Test
    public void testSaveValidHistory() throws JSONException {
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "13032020", "Shenyang", Collections.emptyList());
        //when(holidayRepository.save(any(HolidayHistory.class))).thenReturn(holidayHistory1);
        ResponseEntity<String> message = holidayController.saveHistory(holidayHistory1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.OK);
        jsonObject.put("message", "Successfully saved holiday history");
        Assertions.assertThat(message.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }

    @Test
    public void testSaveInvalidHistory() throws JSONException {
        HolidayHistory holidayHistory = new HolidayHistory(1, null, "Shenyang", Collections.emptyList());
        when(this.holidayRepository.save(holidayHistory)).thenThrow(new ValidationException());
        Assertions.assertThatThrownBy(()->this.holidayController.saveHistory(holidayHistory)).isInstanceOf(ValidationException.class);
    }

    @Test
    public void testFindAllHistoryNotEmpty() throws JSONException {
        Visitor visitor1 = new Visitor();
        visitor1.setVid(1);
        visitor1.setFirstName("Amy");
        visitor1.setLastName("Zhang");
        List<Visitor> list1 = new ArrayList<>();
        list1.add(visitor1);
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "13032020", "Shenyang", list1);
        Visitor visitor2 = new Visitor();
        visitor1.setVid(2);
        visitor1.setFirstName("Emma");
        visitor1.setLastName("Wang");
        List<Visitor> list2 = new ArrayList<>();
        list2.add(visitor2);
        HolidayHistory holidayHistory2 = new HolidayHistory(2, "13032020", "London", list2);
        List<HolidayHistory> list = new ArrayList<>();
        list.add(holidayHistory1);
        list.add(holidayHistory2);
        when(holidayRepository.findAll()).thenReturn(list);
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        for(HolidayHistory holi: list){
            JSONObject object1 = new JSONObject();
            JSONArray array1 = new JSONArray();
            for(Visitor visit: holi.getVisitorList()) {
                JSONObject obj = new JSONObject();
                obj.put("firstName",visit.getFirstName());
                obj.put("lastName",visit.getLastName());
                obj.put("vid",visit.getVid());
                array1.put(obj);
            }
            object1.put("id",holi.getId());
            object1.put("visitorList",array1);
            object1.put("date",holi.getDate());
            object1.put("destination",holi.getDestination());
            array.put(object1);
        }
        jsonObject.put("status", HttpStatus.OK);
        jsonObject.put("data", array.toString());
        ResponseEntity<String> allFromHistory = holidayController.findAllHistory();
        Assertions.assertThat(allFromHistory.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }


    @Test
    public void testFindAllHistoryEmpty() throws JSONException {
        when(holidayRepository.findAll()).thenReturn(Collections.emptyList());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.OK);
        jsonObject.put("data", Collections.emptyList());
        ResponseEntity<String> allHistory = holidayController.findAllHistory();
        Assertions.assertThat(allHistory.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }


    @Test
    public void testFindAllHistoryException() throws JSONException {
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "13032020", "Shenyang", null);
        List<HolidayHistory> list = new ArrayList<>();
        list.add(holidayHistory1);
        when(holidayRepository.findAll()).thenReturn(list);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        jsonObject.put("message", "Exception occurred");
        ResponseEntity<String> all = holidayController.findAllHistory();
        Assertions.assertThat(all.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }


    @Test
    public void testFindHistoryByValidId() throws JSONException {
        Visitor visitor1 = new Visitor();
        visitor1.setVid(1);
        visitor1.setFirstName("Amy");
        visitor1.setLastName("Zhang");
        List<Visitor> list1 = new ArrayList<>();
        list1.add(visitor1);
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "13032020", "Shenyang", list1);
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
        when(holidayRepository.findById(1)).thenReturn(Optional.of(holidayHistory1));
        ResponseEntity<String> all = holidayController.findHistoryById(1);
        Assertions.assertThat(all.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }


    @Test
    public void testFindHistoryByInvalidId() throws JSONException {
        Visitor visitor1 = new Visitor(1, "Amy","Zhang");
        List<Visitor> list1 = new ArrayList<>();
        list1.add(visitor1);
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "13032020", "Shenyang", list1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.BAD_REQUEST);
        jsonObject.put("message", "Nothing found for history record 2");
        ResponseEntity<String> all = holidayController.findHistoryById(2);
        Assertions.assertThat(all.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }

    @Test
    public void testFindHistoryByIdException() throws JSONException {
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "13032020", "Shenyang", null);
        when(holidayRepository.findById(1)).thenReturn(Optional.of(holidayHistory1));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        jsonObject.put("message", "Exception occurred");
        ResponseEntity<String> all = holidayController.findHistoryById(1);
        Assertions.assertThat(all.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }


    //2nd level
    @Test
    public void testSaveValidVisitorByHistoryId() throws JSONException {
        Visitor visitor1 = new Visitor();
        visitor1.setVid(1);
        visitor1.setFirstName("Amy");
        visitor1.setLastName("Zhang");
        List<Visitor> list1 = new ArrayList<>();
        list1.add(visitor1);
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "13032020", "Shenyang", list1);
        Visitor visitor2 = new Visitor(1, "Emma","Wang");
        when(holidayRepository.findById(1)).thenReturn(Optional.of(holidayHistory1));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.OK);
        jsonObject.put("message", "Successfully saved visitor in history record 1");
        ResponseEntity<String> message = holidayController.saveVisitorByHistoryId(1,visitor2);
        Assertions.assertThat(message.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }

    @Test
    public void testSaveVisitorByInvalidHistoryId() throws JSONException {
        Visitor visitor1 = new Visitor(1, "Amy","Zhang");
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "13032020", "Shenyang", Collections.emptyList());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.BAD_REQUEST);
        jsonObject.put("message", "Nothing found for history record 2");
        ResponseEntity<String> message = holidayController.saveVisitorByHistoryId(2,visitor1);
        Assertions.assertThat(message.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }

    @Test
    public void testFindAllVisitorByValidHistoryId() throws JSONException {
        Visitor visitor1 = new Visitor();
        visitor1.setVid(1);
        visitor1.setFirstName("Amy");
        visitor1.setLastName("Zhang");
        List<Visitor> list1 = new ArrayList<>();
        list1.add(visitor1);
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "13032020", "Shenyang", list1);
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
        when(holidayRepository.findById(1)).thenReturn(Optional.of(holidayHistory1));
        ResponseEntity<String> all = holidayController.findAllVisitorByHistoryId(1);
        Assertions.assertThat(all.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }


    @Test
    public void testFindAllVisitorByInalidHistoryId() throws JSONException {
        Visitor visitor1 = new Visitor(1, "Amy","Zhang");
        List<Visitor> list1 = new ArrayList<>();
        list1.add(visitor1);
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "13032020", "Shenyang", list1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.BAD_REQUEST);
        jsonObject.put("message", "Nothing found for history record 2");
        ResponseEntity<String> all = holidayController.findAllVisitorByHistoryId(2);
        Assertions.assertThat(all.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }

    @Test
    public void testFindAllVisitorByHistoryIdException() throws JSONException {
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "13032020", "Shenyang", null);
        when(holidayRepository.findById(1)).thenReturn(Optional.of(holidayHistory1));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        jsonObject.put("message", "Exception occurred");
        ResponseEntity<String> all = holidayController.findAllVisitorByHistoryId(1);
        Assertions.assertThat(all.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }



    @Test
    public void testfindVisitorByValidHistoryIdAndValidOrder() throws JSONException {
        Visitor visitor1 = new Visitor(1, "Amy","Zhang");
        List<Visitor> list1 = new ArrayList<>();
        list1.add(visitor1);
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "13032020", "Shenyang", list1);
        JSONObject jsonObject = new JSONObject();
        JSONObject obj = new JSONObject();
        obj.put("firstName", visitor1.getFirstName());
        obj.put("lastName", visitor1.getLastName());
        obj.put("vid", visitor1.getVid());
        jsonObject.put("status", HttpStatus.OK);
        jsonObject.put("data", obj);
        when(holidayRepository.findById(1)).thenReturn(Optional.of(holidayHistory1));
        ResponseEntity<String> message = holidayController.findVisitorByHistoryIdAndOrder(1,1);
        Assertions.assertThat(message.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }


    @Test
    public void testfindVisitorByValidHistoryIdAndInvalidOrder() throws JSONException {
        Visitor visitor1 = new Visitor(1, "Amy","Zhang");
        List<Visitor> list1 = new ArrayList<>();
        list1.add(visitor1);
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "13032020", "Shenyang", list1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.BAD_REQUEST);
        jsonObject.put("message", "No visitor 2 found for history record 1");
        when(holidayRepository.findById(1)).thenReturn(Optional.of(holidayHistory1));
        ResponseEntity<String> message = holidayController.findVisitorByHistoryIdAndOrder(1,2);
        Assertions.assertThat(message.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }

    @Test
    public void testfindVisitorByInValidHistoryIdAndOrder() throws JSONException {
        Visitor visitor1 = new Visitor(1, "Amy","Zhang");
        List<Visitor> list1 = new ArrayList<>();
        list1.add(visitor1);
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "13032020", "Shenyang", list1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.BAD_REQUEST);
        jsonObject.put("message", "Nothing found for history record 2");
        ResponseEntity<String> all = holidayController.findVisitorByHistoryIdAndOrder(2,1);
        Assertions.assertThat(all.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }

    @Test
    public void testfindVisitorByHistoryIdAndOrderException() throws JSONException {
        HolidayHistory holidayHistory1 = new HolidayHistory(1, "13032020", "Shenyang", null);
        when(holidayRepository.findById(1)).thenReturn(Optional.of(holidayHistory1));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        jsonObject.put("message", "Exception occurred");
        ResponseEntity<String> all = holidayController.findVisitorByHistoryIdAndOrder(1,1);
        Assertions.assertThat(all.getBody()).isNotNull().isEqualTo(jsonObject.toString());
    }


}