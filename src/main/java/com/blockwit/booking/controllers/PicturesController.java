package com.blockwit.booking.controllers;

import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.exceptions.UserNotFoundException;
import com.blockwit.booking.security.SecurityService;
import com.blockwit.booking.service.HotelService;
import com.blockwit.booking.service.PictureService;
import com.blockwit.booking.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Optional;


@Controller
@RequestMapping("/hotels/pictures")
@Slf4j
public class PicturesController {

    @Value("${upload.path}")
    private String uploadPath;

    private PictureService pictureService;
    private SecurityService securityService;

    private HotelService hotelService;

    public PicturesController(PictureService pictureService,
                              SecurityService securityService,
                              HotelService hotelService) {
        this.pictureService = pictureService;
        this.securityService = securityService;
        this.hotelService = hotelService;
    }

    @GetMapping("/add")
    public String addPicture() {
        return "front/picture-add";
    }

    @PostMapping("/add")
    public RedirectView addPicture(RedirectAttributes redirectAttributes,
                                   @RequestParam("file") MultipartFile multipartFile) throws IOException {

        if (multipartFile != null) {

            String uploadPathPerMonth = Utils.getUploadPath(uploadPath);

            String userName = securityService.getUsernameFromSecurityContext();

            Optional<Hotel> hotelOptional = hotelService.getHotelById(1l);
            if (hotelOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("message_error",
                        "Не удалось определить отель, " +
                                " для корректного добавления изображения");
                return new RedirectView("/hotels", true);
            }

            try {
                pictureService.savePicture(multipartFile, uploadPathPerMonth, userName, hotelOptional.get());
            } catch (UserNotFoundException e) {
                redirectAttributes.addFlashAttribute("message_error",
                        "К сожалению, не удалось получить информация для пользователя");
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("message_error",
                        "К сожалению, не удалось сохранить информацию");
            }
            redirectAttributes.addFlashAttribute("message_success",
                    "Избображение добавлено");

        }
        return new RedirectView("/hotels/pictures/add", true);
    }
}
