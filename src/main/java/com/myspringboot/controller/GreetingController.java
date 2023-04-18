package com.myspringboot.controller;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.myspringboot.dto.Greeting;
import com.myspringboot.model.User;
import com.myspringboot.mapper.UserMapper;
import com.myspringboot.service.UserService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/user")
    public User getUsers(@RequestParam(value = "id") long id) {
        User result = userMapper.getById(id);
        return result;
    }

    // ??? password 为null？？？？
    @GetMapping("/allUsers")
    public List<User> getUsers() {
        List<User> result = userMapper.getAll();
        return result;
//        JSONArray jsonArray = new JSONArray();
//
//        for (User u : result) {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("id", u.getId());
//            jsonObject.put("name", u.getName());
//            jsonObject.put("email", u.getEmail());
//            jsonObject.put("password", u.getPassword());
//            jsonObject.put("phone_number", u.getPhone_number());
//
//            jsonArray.add(jsonObject);
//        }
//        return jsonArray;
    }

    @PutMapping("/userUpdate/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        // your code here
        userMapper.updateById(id, "Hebe", "testqazs");
        return new ResponseEntity<>(HttpStatus.OK);
    }


//    @GetMapping("/endDate")
//    public String calculateEndDate(@RequestParam(value = "startDate") String startDate) {
//        return String.format("The final date is %s!", startDate);
//    }


    private LocalDate getEndDate(String startDate, long weeksInterval) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate);
        LocalDate result = start.plusWeeks(weeksInterval);
//        return startDate + weeksInterval;
        return result;

    }

    @GetMapping("/endDate")
    public LocalDate calculateEndDate(@RequestParam("startDate") String startDate,
                                      @RequestParam("weeksInterval") long weeksInterval) throws ParseException {
//        return "The final date is %s!"+ getEndDate(startDate, weeksInterval);
        return getEndDate(startDate, weeksInterval);

    }

}