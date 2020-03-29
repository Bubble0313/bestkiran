package com.example.practice.controller;

import com.example.practice.model.HolidayHistory;
import com.example.practice.model.Visitor;
import com.example.practice.repository.HolidayRepository;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jdk.nashorn.internal.runtime.JSONFunctions;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;

@RestController
public class HolidayController {

    @Autowired
    private HolidayRepository holidayRepository;

    //1st level

    @ApiOperation(value = "Save a piece of holiday history record")
    @PostMapping("/holidayhistory")
    public ResponseEntity<String> saveHistory(@Valid @RequestBody HolidayHistory holidayHistory) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        this.holidayRepository.save(holidayHistory);
        jsonObject.put("status", HttpStatus.OK);
        jsonObject.put("message", "Successfully saved holiday history");
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Find all holiday history records")
    @GetMapping("/holidayhistory")
    public ResponseEntity<String> findAllHistory() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            List<HolidayHistory> holidayHistoryList = Lists.newArrayList(this.holidayRepository.findAll());
            if (holidayHistoryList.size() > 0) {
                JSONArray array = new JSONArray();
                for (HolidayHistory holi : holidayHistoryList) {
                    JSONObject object1 = new JSONObject();
                    JSONArray array1 = new JSONArray();
                    for (Visitor visit : holi.getVisitorList()) {
                        JSONObject obj = new JSONObject();
                        obj.put("vid", visit.getVid());
                        obj.put("firstName", visit.getFirstName());
                        obj.put("lastName", visit.getLastName());
                        array1.put(obj);
                    }
                    object1.put("id", holi.getId());
                    object1.put("date", holi.getDate());
                    object1.put("destination", holi.getDestination());
                    object1.put("visitorList", array1);
                    array.put(object1);
                }
                jsonObject.put("status", HttpStatus.OK);
                jsonObject.put("data", array.toString());
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
            } else {
                jsonObject.put("status", HttpStatus.OK);
                jsonObject.put("data", Collections.emptyList());
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
            }
        } catch (Exception e) {
            jsonObject.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            jsonObject.put("message", "Exception occurred");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiOperation(value = "Find a piece of holiday history record by historyId")
    @GetMapping("/holidayhistory/{historyId}")
    public ResponseEntity<String> findHistoryById(@PathVariable Integer historyId) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            Optional<HolidayHistory> holidayHistory = this.holidayRepository.findById(historyId);
            if (holidayHistory.isPresent()) {
                JSONObject object = new JSONObject();
                JSONArray array = new JSONArray();
                for (Visitor visit : holidayHistory.get().getVisitorList()) {
                    JSONObject obj = new JSONObject();
                    obj.put("firstName", visit.getFirstName());
                    obj.put("lastName", visit.getLastName());
                    obj.put("vid", visit.getVid());
                    array.put(obj);
                }
                object.put("id", holidayHistory.get().getId());
                object.put("visitorList", array);
                object.put("date", holidayHistory.get().getDate());
                object.put("destination", holidayHistory.get().getDestination());
                jsonObject.put("status", HttpStatus.OK);
                jsonObject.put("data", object.toString());
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
            } else {
                jsonObject.put("status", HttpStatus.BAD_REQUEST);
                jsonObject.put("message", "Nothing found for history record " + historyId);
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            jsonObject.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            jsonObject.put("message", "Exception occurred");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //2nd level

    @ApiOperation(value = "Save a piece of visitor information into a specific holiday history record")
    @PostMapping("/holidayhistory/{historyId}/visitor")
    public ResponseEntity<String> saveVisitorByHistoryId(@PathVariable Integer historyId, @Valid @RequestBody Visitor visitor) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        Optional<HolidayHistory> history = this.holidayRepository.findById(historyId);
        if (!history.isPresent()) {
            jsonObject.put("status", HttpStatus.BAD_REQUEST);
            jsonObject.put("message", "Nothing found for history record " + historyId);
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        } else if (history != null && history.get().getId() == historyId) {
            List<Visitor> l = history.get().getVisitorList();
            l.add(visitor);
            HolidayHistory his = history.get();
            his.setVisitorList(l);
            this.holidayRepository.save(his);
            jsonObject.put("status", HttpStatus.OK);
            jsonObject.put("message", "Successfully saved visitor in history record " + historyId);
        }
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Find all visitors information in a specific holiday history record")
    @GetMapping("/holidayhistory/{historyId}/visitor")
    public ResponseEntity<String> findAllVisitorByHistoryId(@PathVariable Integer historyId) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            Optional<HolidayHistory> holidayHistory = this.holidayRepository.findById(historyId);
            if (holidayHistory.isPresent()) {
                JSONObject object = new JSONObject();
                JSONArray array = new JSONArray();
                for (Visitor visit : holidayHistory.get().getVisitorList()) {
                    JSONObject obj = new JSONObject();
                    obj.put("firstName", visit.getFirstName());
                    obj.put("lastName", visit.getLastName());
                    obj.put("vid", visit.getVid());
                    array.put(obj);
                }
                object.put("visitorList", array);
                jsonObject.put("status", HttpStatus.OK);
                jsonObject.put("data", object.toString());
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
            } else {
                jsonObject.put("status", HttpStatus.BAD_REQUEST);
                jsonObject.put("message", "Nothing found for history record " + historyId);
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST); }
        } catch (Exception e) {
            jsonObject.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            jsonObject.put("message", "Exception occurred");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }


    @ApiOperation(value = "Find a piece of visitor information based on holiday historyId and the sequence of the visitor")
    @GetMapping("/holidayhistory/{historyId}/visitor/{order}")
    public ResponseEntity<String> findVisitorByHistoryIdAndOrder(@PathVariable Integer historyId, @PathVariable Integer order) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            Optional<HolidayHistory> holidayHistory = this.holidayRepository.findById(historyId);
            if (holidayHistory.isPresent()) {
                if (order <= holidayHistory.get().getVisitorList().size()) {
                    Visitor visitor = holidayHistory.get().getVisitorList().get(order - 1);
                    JSONObject obj = new JSONObject();
                    obj.put("firstName", visitor.getFirstName());
                    obj.put("lastName", visitor.getLastName());
                    obj.put("vid", visitor.getVid());
                    jsonObject.put("status", HttpStatus.OK);
                    jsonObject.put("data", obj);
                    return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
                } else {
                    jsonObject.put("status", HttpStatus.BAD_REQUEST);
                    jsonObject.put("message", "No visitor " + order + " found for history record " + historyId);
                    return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST); }
            } else {
                jsonObject.put("status", HttpStatus.BAD_REQUEST);
                jsonObject.put("message", "Nothing found for history record " + historyId);
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST); }
        } catch (Exception e) {
            jsonObject.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            jsonObject.put("message", "Exception occurred");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }
}