package org.highfives.grid.user.command.service;

import org.highfives.grid.user.command.dto.ImgDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface ImgService {
    Map<String, String> imgS3Upload(MultipartFile multipartFile) throws IOException;
    String imgTableUpdate(Map<String, String> result, int id, int typeId);
    void imgS3Delete(String result);
}
