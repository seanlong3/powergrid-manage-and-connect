package com.example.testpowmanage.service;

import com.example.testpowmanage.common.vo.ModelFileVO;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface MinioService {
    Boolean uploadFile(MultipartFile file, Map<String, String> tags) throws Exception;

    InputStream downloadFile(String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    List<ModelFileVO> getFileNameByAreaId(Long areaId) throws Exception;
}
