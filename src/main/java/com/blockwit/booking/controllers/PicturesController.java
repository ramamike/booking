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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


@Controller
@RequestMapping("/hotels")
@Slf4j
public class PicturesController {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${pictures.path}")
    private String picturesPath;

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

    @GetMapping("/{hotelId}/picture/add")
    public ModelAndView addPicture(@PathVariable(value = "hotelId") long hotelId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("front/picture-add");
        mav.addObject("hotelId", hotelId);
        return mav;
    }

    @PostMapping("/{hotelId}/picture/add")
    public RedirectView addPicture(RedirectAttributes redirectAttributes,
                                   @PathVariable(value = "hotelId") long hotelId,
                                   @RequestParam("file") MultipartFile multipartFile) {

        if (multipartFile != null) {

            String userName = securityService.getUsernameFromSecurityContext();

            Optional<Hotel> hotelOptional = hotelService.getHotelById(hotelId);
            if (hotelOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("message_error",
                        "Не удалось определить отель, " +
                                " для корректного добавления изображения");
                return new RedirectView("/hotels", true);
            }

            File uploadDir = new File(uploadPath + picturesPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String absolutePath = Utils.getPathPerMonth(uploadPath + picturesPath);

            File absoluteDir=new File(absolutePath);
            if(!absoluteDir.exists()) {
                absoluteDir.mkdir();
            }

            String picturesPathPrMonth= Utils.getPathPerMonth(picturesPath);

            try {
                pictureService.savePicture(multipartFile, absolutePath, picturesPathPrMonth,
                        userName, hotelOptional.get());
            } catch (UserNotFoundException e) {
                redirectAttributes.addFlashAttribute("message_error",
                        "К сожалению, не удалось получить информация для пользователя");
                return new RedirectView("/hotels", true);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("message_error",
                        "К сожалению, не удалось сохранить информацию");
                return new RedirectView("/hotels", true);
            }
            redirectAttributes.addFlashAttribute("message_success",
                    "Избображение добавлено");

        }
        return new RedirectView("/hotels", true);
    }
}
