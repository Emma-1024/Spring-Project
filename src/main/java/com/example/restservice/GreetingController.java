package com.example.restservice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.tomcat.jni.SSL.getTime;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();



    private LocalDate getEndDate(String startDate, long weeksInterval) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate);
        LocalDate result = start.plusWeeks(weeksInterval);
//        return startDate + weeksInterval;
        return result;

    }

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
//    @GetMapping("/endDate")
//    public String calculateEndDate(@RequestParam(value = "startDate") String startDate){
//        return String.format("The final date is %s!", startDate);
//    }

    @GetMapping("/endDate")
    public LocalDate calculateEndDate(@RequestParam("startDate") String startDate,
                                   @RequestParam("weeksInterval") long weeksInterval) throws ParseException {
//        return "The final date is %s!"+ getEndDate(startDate, weeksInterval);
        return getEndDate(startDate, weeksInterval);

    }


}