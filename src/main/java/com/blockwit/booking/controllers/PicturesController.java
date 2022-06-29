package com.blockwit.booking.controllers;

import com.blockwit.booking.entity.Picture;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Controller
@RequestMapping("/hotels/pictures")
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PicturesController {

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/add")
    public String addPicture() {
        return "front/picture-add";
    }

    @PostMapping("/add")
    public RedirectView addPicture(RedirectAttributes redirectAttributes,
                                   @RequestParam("file") MultipartFile multipartFile) throws IOException {

        if(multipartFile!=null) {
            File uploadDir= new File(uploadPath);
            if(!uploadDir.exists()) {
                uploadDir.mkdir();
                String uuidFile=UUID.randomUUID().toString();
                String resultFileName=uuidFile + "." + multipartFile.getOriginalFilename();
                Picture picture = Picture.builder()
                        .name(resultFileName)
                        .build();
                multipartFile. transferTo(new File(uploadPath + "/" + resultFileName));
            }
        }

        redirectAttributes.addFlashAttribute("message_error",
                "Ups");
        return new RedirectView("/hotels", true);
    }
}
