package com.ernazar.turismo;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class MessageController {
    private final CohereService service;
    private final ObjectMapper objectMapper;

    @Autowired
    public MessageController(CohereService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }


    @PostMapping
    public String getChatResponse(@RequestBody Message message) {
        try {
            String response = service.getResponse(message.getText());

            return response ;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
