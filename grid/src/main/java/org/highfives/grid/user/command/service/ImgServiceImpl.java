package org.highfives.grid.user.command.service;

import org.highfives.grid.user.command.dto.ImgDTO;
import org.highfives.grid.user.command.repository.ImgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ImgServiceImpl implements ImgService {

    private final ImgRepository imgRepository;

    @Autowired
    public ImgServiceImpl(ImgRepository imgRepository) {
        this.imgRepository = imgRepository;
    }

    @Override
    public boolean uploadImage(Map<String, String> result, int id, int typeId) {




        return false;
    }
}
