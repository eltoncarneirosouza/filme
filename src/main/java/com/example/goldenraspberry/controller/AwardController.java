package com.example.goldenraspberry.controller;

import com.example.goldenraspberry.service.AwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/awards")
@RequiredArgsConstructor
public class AwardController {

    private final AwardService awardService;

    @PostMapping("/upload")
    public ResponseEntity
            <Map<String, List<Map<String, Object>>>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || file.getSize() == 0) {
            Map<String, List<Map<String, Object>>> errorResponse = new HashMap<>();
            errorResponse.put("error", List.of(Map.of("message", "Arquivo não enviado")));
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        var stringListMap = awardService.parseAndSaveMovies(file);
        return new ResponseEntity<>(stringListMap, HttpStatus.OK);
    }
}