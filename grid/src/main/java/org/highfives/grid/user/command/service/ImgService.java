package org.highfives.grid.user.command.service;

import org.highfives.grid.user.command.dto.ImgDTO;

import java.util.Map;

public interface ImgService {
    boolean uploadImage(Map<String, String> result, int id, int typeId);
}
