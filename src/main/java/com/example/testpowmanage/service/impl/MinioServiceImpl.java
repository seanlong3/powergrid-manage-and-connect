package com.example.testpowmanage.service.impl;

import com.example.testpowmanage.common.vo.ModelFileVO;
import com.example.testpowmanage.service.MinioService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucketName;
    @Autowired
    public MinioServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    private String getMd5(MultipartFile file){
        try{
            return DigestUtils.md5Hex(file.getInputStream());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }



    private boolean isFileWithMd5Exists(String md5) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .build());
        for (Result<Item> result : results){
            Item item = result.get();
            Map<String, String> tags = getFileTags(item.objectName());
            if (md5.equals(tags.get("md5"))){
                return true;
            }
        }
        return false;
    }

    private Map<String, String> getFileTags(String fileName) throws Exception{
        return minioClient.getObjectTags(
                GetObjectTagsArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build())  .get();
    }


    @Override
    public Boolean uploadFile(MultipartFile file, Map<String, String> tags) throws Exception {
        String md5 = getMd5(file);
        if (md5 == null){
            return false;
        }
        tags.put("md5",md5);

        if (isFileWithMd5Exists(md5)){
            return false;
        }

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(file.getOriginalFilename())
                        .stream(file.getInputStream(), file.getSize(),-1)
                        .contentType(file.getContentType())
                        .tags(tags)
                        .build());
        return true;
    }


    @Override
    public InputStream downloadFile(String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );
    }

    @Override
    public List<ModelFileVO> getFileNameByAreaId(Long areaId) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .build());
        List<ModelFileVO> filterFiles = new ArrayList<>();
        for (Result<Item> result : results){
            Item item = result.get();
            Map<String,String> tags = getFileTags(item.objectName());
            if (areaId.toString().equals(tags.get("areaId"))){
                ModelFileVO fileVO = new ModelFileVO();
                fileVO.setFileName(item.objectName());
                fileVO.setMd5(tags.get("md5"));
                filterFiles.add(fileVO);
            }
        }
        return filterFiles;
    }
}
