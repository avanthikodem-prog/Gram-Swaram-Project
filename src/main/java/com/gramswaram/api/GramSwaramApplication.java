package com.gramswaram.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Collections;

@SpringBootApplication
public class GramSwaramApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GramSwaramApplication.class);
        
        // కింద ఉన్న లైన్ ద్వారా Python URL ని సాఫ్ట్‌వేర్ లెవల్‌లో యాడ్ చేస్తున్నాము
        app.setDefaultProperties(Collections.singletonMap("python.api.base-url", "https://gramswaram-main.onrender.com"));
        
        app.run(args);
    }

}