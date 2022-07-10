package com.blockwit.booking.controllers;

import com.blockwit.booking.entity.Picture;
import com.blockwit.booking.exceptions.UserNotFoundException;
import com.blockwit.booking.security.SecurityService;
import com.blockwit.booking.service.PictureService;
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
import java.text.SimpleDateFormat;
import java.util.Date;


@Controller
@RequestMapping("/hotels/pictures")
@Slf4j
public class PicturesController {

    private PictureService pictureService;
    private SecurityService securityService;

    @Value("${upload.path}")
    private String uploadPath;

    public PicturesController(PictureService pictureService, SecurityService securityService) {
        this.pictureService = pictureService;
        this.securityService = securityService;
    }

    @GetMapping("/add")
    public String addPicture() {
        return "front/picture-add";
    }

    @PostMapping("/add")
    public RedirectView addPicture(RedirectAttributes redirectAttributes,
                                   @RequestParam("file") MultipartFile multipartFile) throws IOException {

        if (multipartFile != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            Date dateNow = new Date();
            String dateForNow = (new SimpleDateFormat("yyyy.MM")).format(dateNow);

            String uploadPathPerMonth=uploadPath +"/" + dateForNow;
            File uploadDirPerMonth=new File(uploadPathPerMonth);
            if(!uploadDirPerMonth.exists()) {
                uploadDirPerMonth.mkdir();
            }

            String userName = securityService.getUsernameFromSecurityContext();

            try {
                pictureService.savePicture(multipartFile, uploadPathPerMonth, userName);
            } catch (UserNotFoundException e) {
                redirectAttributes.addFlashAttribute("message_error",
                        "К сожалению, не удалось получить информация для пользователя");
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("message_error",
                        "К сожалению, не удалось сохранить информацию");
            }
        }
        return new RedirectView("/hotels/pictures/add", true);
    }
}
