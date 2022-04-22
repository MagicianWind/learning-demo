package com.wind.miniodemo.controller;

import com.wind.miniodemo.util.MinioTemplate;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * minio控制器
 *
 * @author wind
 */
@RestController
@RequestMapping("/file")
@Api(tags = "文件管理")
@RequiredArgsConstructor
public class FileController {

    private final MinioTemplate minioTemplate;

    /**
     * 上传文件-文件流方式
     *
     * @param file 文件
     * @param newFileName 新文件名
     */
    @PostMapping("/upload")
    @ApiOperation("上传文件-文件流")
    public String uploadFile(@RequestPart MultipartFile file, @RequestParam(required = false) String newFileName) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (StringUtils.isEmpty(newFileName)) {
            newFileName = file.getOriginalFilename();
        }
        return minioTemplate.putObject(newFileName, file.getInputStream(), file.getSize());
    }

    /**
     * 上传文件-文件地址方式
     *
     * @param fileUrl 文件地址
     * @param newFileName 新文件名
     */
    @PostMapping("/upload/by-url")
    @ApiOperation("上传文件-文件地址")
    public String uploadFile(String fileUrl, String newFileName) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (StringUtils.isEmpty(newFileName)) {
            newFileName = fileUrl.substring(fileUrl.lastIndexOf(File.separatorChar) + 1);
        }
        return minioTemplate.uploadObject(newFileName, fileUrl);
    }
}
