package com.blockwit.booking.controllers;

import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.entity.User;
import com.blockwit.booking.exceptions.BookingNotFoundException;
import com.blockwit.booking.exceptions.HotelNotFoundException;
import com.blockwit.booking.exceptions.UserNotFoundException;
import com.blockwit.booking.security.SecurityService;
import com.blockwit.booking.service.BookingService;
import com.blockwit.booking.service.HotelService;
import com.blockwit.booking.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;


@Controller
@RequestMapping("/hotels")
@AllArgsConstructor
@Slf4j
public class HotelsController {

    private HotelService hotelService;
    private BookingService bookingService;
    private UserService userService;
    private SecurityService securityService;

    @GetMapping
    public ModelAndView showHotels(RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView();

        String userName = securityService.getUsernameFromSecurityContext();

        Optional<User> userOptional = userService.getUserByUsername(userName);

        if (userName.equals("anonymousUser")) {
            log.info("anonymousUser");
        } else if (userOptional.isPresent()) {
            mav.addObject("userId", userOptional.get().getId());
        } else if (userOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("message_error",
                    "Данные могут отображаться не корректно");
            return new ModelAndView("redirect:/");
        }

        boolean isAdmin = securityService.checkRoleFromSecurityContext("ADMIN");

        mav.setViewName("front/hotels");
        mav.addObject("hotels", hotelService.hotels());
        mav.addObject("isAdmin", isAdmin);
        return mav;
    }

    @GetMapping("/add")
    public String hotelAdd() {
        return "front/hotel-add";
    }

    @PostMapping("/add")
    public RedirectView hotelPost(
            RedirectAttributes redirectAttributes,
            @RequestParam String name,
            @RequestParam String description,
            Model model) {

        String userName = securityService.getUsernameFromSecurityContext();

        Optional<User> userOptional = userService.getUserByUsername(userName);

        if (userOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("message_error",
                    "Не удалось определить пользователя, " +
                            " для корректного добавления отеля");
            return new RedirectView("/hotels", true);
        }

        Hotel hotel = Hotel.builder()
                .name(name)
                .description(description)
                .ownerId(userOptional.get().getId())
                .build();

        if (hotelService.saveHotelResponse(hotel)) {
            redirectAttributes.addFlashAttribute("message_success",
                    "Отель " + hotel.getName() + " успешно создан!");
        } else {
            redirectAttributes.addFlashAttribute("message_error",
                    "Ошибка при создании отеля!");
        }
        return new RedirectView("/hotels", true);
    }

    @GetMapping("/{hotelId}")
    public ModelAndView showHotel(RedirectAttributes redirectAttributes,
                                  @PathVariable(value = "hotelId") long hotelId, Model model) {


        boolean isAdmin = securityService.checkRoleFromSecurityContext("ADMIN");

        Hotel hotel = null;
        try {
            hotel = hotelService.showDetail(hotelId).orElseThrow(() -> new HotelNotFoundException());
            ModelAndView mav = new ModelAndView();
            mav.addObject(hotel);
            mav.addObject("isAdmin", isAdmin);
            mav.setViewName("front/hotel-general");
            return mav;

        } catch (HotelNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error",
                    "К сожалению отель не найден!");
        }

        return new ModelAndView("redirect:/hotels");
    }

    @GetMapping("/{hotelId}/edit")
    public ModelAndView hotelDetails(RedirectAttributes redirectAttributes,
                                     @PathVariable(value = "hotelId") long hotelId, Model model) {

        Hotel hotel = null;
        try {
            hotel = hotelService.showDetail(hotelId).orElseThrow(() -> new HotelNotFoundException());
            ModelAndView mav = new ModelAndView();
            mav.setViewName("front/hotel-edit");
            mav.addObject("hotel", hotel);
            mav.addObject("hotelId", String.valueOf(hotelId));
            return mav;
        } catch (HotelNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error",
                    "К сожалению отель не найден!");
        }

        return new ModelAndView("redirect:/");
    }

    @PostMapping("/{hotelId}/edit")
    public RedirectView hotelUpdate(RedirectAttributes redirectAttributes,
                                    @PathVariable(value = "hotelId") long hotelId,
                                    @RequestParam String name, @RequestParam String description,
                                    Model model) {

        String userName = securityService.getUsernameFromSecurityContext();

        try {
            if (!hotelService.checkEditingPermission(hotelId, userName)) {
                redirectAttributes.addFlashAttribute("message_error",
                        userName + " не может редактировать данную запись");
                return new RedirectView("/hotels", true);
            }
        } catch (HotelNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error",
                    "К сожалению, не удалось получить информация для пользователя");
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error",
                    "К сожалению, не удалось получить информация для пользователя");
        }

        Hotel hotel = Hotel.builder()
                .name(name)
                .description(description)
                .build();

        try {
            hotelService.hotelUpdate(hotelId, hotel);
            redirectAttributes.addFlashAttribute("message_success", "Информацию об отеле обновили успешно!");
        } catch (HotelNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error", "К сожалению, не удалось обновить информацию об отеле!");
        }

        return new RedirectView("/hotels", true);
    }

}
