package com.blockwit.booking.controllers;

import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.entity.Room;
import com.blockwit.booking.exceptions.RoomNotFoundException;
import com.blockwit.booking.exceptions.UserNotFoundException;
import com.blockwit.booking.security.SecurityService;
import com.blockwit.booking.service.HotelService;
import com.blockwit.booking.service.PictureService;
import com.blockwit.booking.service.RoomService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


@Controller
@RequestMapping("/hotels")
@Slf4j
public class PicturesController {

    @Value("${upload.path}")
    private String uploadPath;

    private PictureService pictureService;
    private SecurityService securityService;

    private HotelService hotelService;

    private RoomService roomService;

    public PicturesController(PictureService pictureService,
                              SecurityService securityService,
                              HotelService hotelService,
                              RoomService roomService) {
        this.pictureService = pictureService;
        this.securityService = securityService;
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    @GetMapping({"/{hotelId}/picture/add", "/{hotelId}/rooms/{roomId}/picture/add"})
    public ModelAndView addPicture(@PathVariable(value = "hotelId") long hotelId,
                                   @PathVariable(value = "roomId") Optional<Long> roomId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("front/picture-add");
        mav.addObject("hotelId", hotelId);
        mav.addObject("roomId", roomId);
        return mav;
    }

    @PostMapping({"/{hotelId}/picture/add", "/{hotelId}/rooms/{roomId}/picture/add"})
    public RedirectView addPicture(RedirectAttributes redirectAttributes,
                                   @PathVariable(value = "hotelId") long hotelId,
                                   @PathVariable(value = "roomId") Optional<Long> roomId,
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

            File picturesDir = new File(uploadPath + "/pictures");
            if (!picturesDir.exists()) {
                picturesDir.mkdir();
            }

            Date dateNow = new Date();
            String relativePathPerMonth = "/pictures/" + (new SimpleDateFormat("yyyy.MM")).format(dateNow);

            String absolutePathPerMonth = uploadPath + relativePathPerMonth;
            File absolutePathPerMonthDir = new File(absolutePathPerMonth);
            if (!absolutePathPerMonthDir.exists()) {
                absolutePathPerMonthDir.mkdir();
            }
            try {
                if (roomId.isPresent()) {
                    Optional<Room> roomOptional;
                    roomOptional = roomService.getById(roomId.get());
                    if (roomOptional.isEmpty()) {
                        redirectAttributes.addFlashAttribute("message_error",
                                "Не удалось определить комнату, " +
                                        " для корректного добавления изображения");
                        return new RedirectView("/hotels/{hotelId}", true);
                    }
                    pictureService.savePicture(multipartFile, absolutePathPerMonth, relativePathPerMonth,
                            userName, roomOptional.get());

                } else pictureService.savePicture(multipartFile, absolutePathPerMonth, relativePathPerMonth,
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
