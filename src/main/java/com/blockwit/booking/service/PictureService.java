package com.blockwit.booking.service;

import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.entity.Picture;
import com.blockwit.booking.entity.Room;
import com.blockwit.booking.exceptions.UserNotFoundException;
import com.blockwit.booking.repository.PictureRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class PictureService {

    private PictureRepository pictureRepository;
    private UserService userService;

    public PictureService(PictureRepository pictureRepository, UserService userService) {
        this.pictureRepository = pictureRepository;
        this.userService = userService;
    }

    public Picture savePicture(MultipartFile multipartFile,
                               String absolutePath,
                               String relativePath,
                               String userName,
                               Hotel hotel)
            throws UserNotFoundException, IOException {
        String uuidFile = UUID.randomUUID().toString();
        String resultFileName = uuidFile + "." + multipartFile.getOriginalFilename();
        Long userId = userService.getUserByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException()).getId();
        String path = absolutePath + "/" + resultFileName;

        multipartFile.transferTo(new File(path));
        Picture picture = Picture.builder()
                .name(multipartFile.getOriginalFilename())
                .path(relativePath + "/" + resultFileName)
                .owner_id(userId)
                .format(multipartFile.getContentType())
                .hotel(hotel)
                .build();

        return pictureRepository.save(picture);
    }
    public Picture savePicture(MultipartFile multipartFile,
                               String absolutePath,
                               String relativePath,
                               String userName,
                               Room room)
            throws UserNotFoundException, IOException {
        String uuidFile = UUID.randomUUID().toString();
        String resultFileName = uuidFile + "." + multipartFile.getOriginalFilename();
        Long userId = userService.getUserByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException()).getId();
        String path = absolutePath + "/" + resultFileName;

        multipartFile.transferTo(new File(path));
        Picture picture = Picture.builder()
                .name(multipartFile.getOriginalFilename())
                .path(relativePath + "/" + resultFileName)
                .owner_id(userId)
                .format(multipartFile.getContentType())
                .room(room)
                .build();

        return pictureRepository.save(picture);
    }
}