package org.highfives.grid.user.command.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.highfives.grid.user.command.aggregate.Employee;
import org.highfives.grid.user.command.aggregate.EmployeeImg;
import org.highfives.grid.user.command.dto.ImgDTO;
import org.highfives.grid.user.command.repository.ImgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ImgServiceImpl implements ImgService {

    private final ImgRepository imgRepository;
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Autowired
    public ImgServiceImpl(ImgRepository imgRepository,
                          AmazonS3Client amazonS3Client) {
        this.imgRepository = imgRepository;
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public Map<String, String> imgS3Upload(MultipartFile multipartFile) throws IOException {

        Map<String, String> result = new HashMap<>();

        String originFileName = multipartFile.getOriginalFilename();
        String extension = originFileName.substring(originFileName.lastIndexOf("."), originFileName.length());

        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + extension;

        long size = multipartFile.getSize(); // 파일 크기

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(multipartFile.getContentType());
        objectMetaData.setContentLength(size);

        try {
            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, newFileName, multipartFile.getInputStream(), objectMetaData)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            String imagePath = amazonS3Client.getUrl(bucketName, newFileName).toString();
            System.out.println("imagePath = " + imagePath);

            result.put("origin",originFileName);
            result.put("new",newFileName);
            result.put("path",imagePath);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        } catch (AmazonClientException e) {
            System.err.println(e.getMessage());
        }

        return result;
    }

    @Override
    public String imgTableUpdate(Map<String, String> result, int userId, int typeId) {

        EmployeeImg oldImg = imgRepository.findByEmployeeIdAndTypeId(userId, typeId);
        System.out.println("oldImg = " + oldImg);

        if(oldImg == null) {
            log.info("해당 유저의 이미지가 없습니다.");
            EmployeeImg inputNewImg = EmployeeImg.builder()
                    .employeeId(userId)
                    .typeId(typeId)
                    .originName(result.get("origin"))
                    .renameName(result.get("new"))
                    .path(result.get("path"))
                    .build();

            imgRepository.save(inputNewImg);
            return "No old img";
        }

        log.info("유저의 이미지가 있습니다.");
        EmployeeImg inputImg = EmployeeImg.builder()
                .Id(oldImg.getId())
                .employeeId(userId)
                .typeId(typeId)
                .originName(result.get("origin"))
                .renameName(result.get("new"))
                .path(result.get("path"))
                .build();

        String oldImgPath = oldImg.getPath();
        System.out.println("inputImg = " + inputImg);
        System.out.println("oldImg = " + oldImg);
        System.out.println("oldImgPath = " + oldImgPath);
        try{
            imgRepository.save(inputImg);

            System.out.println("oldImg = " + oldImg.getPath());
            System.out.println("inputImg = " + inputImg.getPath());
            return oldImgPath;
        } catch (Exception e) {
            log.info("Exception occurred: {}", e.getMessage());
            return "Fail";
        }
    }

    @Override
    public void imgS3Delete(String path) {
        System.out.println("오래된 경로: " + path);
        String splitStr = ".com/";
        String fileName = path.substring(path.lastIndexOf(splitStr) + splitStr.length());

        amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }
}
