package com.gramswaram.api;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    // RestTemplate ని ఇక్కడే ఇన్షియలైజ్ చేస్తున్నాము
    private final RestTemplate restTemplate = new RestTemplate();

    // ప్రైస్ ని తెలుగు మాటల్లోకి మార్చే లాజిక్
    public String convertToTeluguWords(double price) {
        int p = (int) price;
        
        // ముఖ్యమైన ధరల మ్యాపింగ్
        Map<Integer, String> numbers = new HashMap<>();
        numbers.put(160, "నూట అరవై");
        numbers.put(500, "ఐదు వందలు");
        numbers.put(600, "ఆరు వందలు");
        numbers.put(1000, "వెయ్యి");
        numbers.put(1350, "వెయ్యి మూడు వందల యాభై");
        numbers.put(120, "నూట ఇరవై"); // అదనంగా కొన్ని యాడ్ చేశాను
        numbers.put(200, "రెండు వందలు");

        return numbers.getOrDefault(p, p + " రూపాయలు"); 
    }

    // పైథాన్ సర్వర్ నుండి వాయిస్ డేటా తెచ్చే మెథడ్
    public String startListening() {
        // 127.0.0.1 బదులు localhost వాడటం సేఫ్
        String url = "http://localhost:5000/listen-telugu";
        
        try {
            // పైథాన్ నుండి వచ్చే రెస్పాన్స్ ని మ్యాప్ లాగా తీసుకుంటాము
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && "success".equals(response.get("status"))) {
                return (String) response.get("text");
            } else {
                return "క్షమించండి, మీరు అన్నది అర్థం కాలేదు.";
            }
        } catch (Exception e) {
            // సర్వర్ ఆఫ్ లో ఉంటే ఈ మెసేజ్ వెళ్తుంది
            return "AI సర్వర్ కనెక్ట్ అవ్వలేదు.";
        }
    }
}