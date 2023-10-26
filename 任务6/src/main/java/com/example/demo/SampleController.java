package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SampleController {
    @GetMapping("/api/sample")
    public String sampleApi() {
        // Your API logic here
        return "Sample API Response";
    }
}
