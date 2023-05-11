package com.myspringboot.service;

import com.myspringboot.annotation.Log;
import com.myspringboot.aspect.LoggingAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    @Log
    public void logCatching(){
        System.out.println("In Service");
    }
}
