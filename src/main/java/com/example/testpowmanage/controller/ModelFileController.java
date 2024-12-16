package com.example.testpowmanage.controller;

import com.example.testpowmanage.common.dto.EquipDto;
import com.example.testpowmanage.common.vo.ModelFileVO;
import com.example.testpowmanage.service.AnalysisService;
import com.example.testpowmanage.service.MinioService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.errors.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/modelfile")
@Log4j2
public class ModelFileController {

    private final AnalysisService analysisService;
    private final MinioService minioService;
    private final ObjectMapper mapper = new ObjectMapper();


    @Autowired
    public ModelFileController(AnalysisService analysisService, MinioService minioService) {
        this.analysisService = analysisService;
        this.minioService = minioService;
    }


    @PostMapping("/upload")
    public Boolean upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("areaid") Long areaId
    ) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("areaId", String.valueOf(areaId));
            String fileName = file.getOriginalFilename();
            if (fileName == null) {
                return false;
            }
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
                return false;
            }
            if ("json".equals(fileName.substring(dotIndex + 1))) {
                return analysisService.analysisDataFile(
                        mapper.readValue(file.getInputStream(), new TypeReference<Map<String, List<EquipDto>>>() {
                        }),
                        areaId
                );
            } else if ("obj".equalsIgnoreCase(fileName.substring(dotIndex + 1))) {
                return minioService.uploadFile(file, map);
            }
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String fileName) {
        try {
            InputStream inputStream = minioService.downloadFile(fileName);
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    @GetMapping("/getfile")
    public List<ModelFileVO> getFile(@RequestParam("areaid") Long areaId){
        try{
            return minioService.getFileNameByAreaId(areaId);
        } catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }



}
