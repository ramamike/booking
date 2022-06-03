package com.blockwit.booking.controllers;

import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.exceptions.HotelNotFoundException;
import com.blockwit.booking.exceptions.UserNotFoundException;
import com.blockwit.booking.service.BookingService;
import com.blockwit.booking.service.HotelService;
import com.blockwit.booking.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class HotelsController {

    private HotelService hotelService;
    private BookingService bookingService;

    private UserService userService;

    @GetMapping
    public ModelAndView showHotels() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("front/hotels");
        mav.addObject("hotels", hotelService.hotels());
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

        String userName = Utils.getUsernameFromSecurityContext();

        Long userId;
        try {
            userId=userService.getIdByUsername(userName);
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error",
                    "Не удалось найти пользователя в базе данных, " +
                            " для корректного добавления отеля");
            return new RedirectView("/", true);
        }

        Hotel hotel = Hotel.builder()
                .name(name)
                .description(description)
                .ownerId(userId)
                .build();

        if (hotelService.saveHotelResponse(hotel)) {
            redirectAttributes.addFlashAttribute("message_success",
                    "Отель " + hotel.getName() + " успешно создан!");
        } else {
            redirectAttributes.addFlashAttribute("message_error",
                    "Ошибка при создании отеля!");
        }
        return new RedirectView("/", true);
    }

    @GetMapping("/edit/{hotelId}")
    public ModelAndView hotelDetails(RedirectAttributes redirectAttributes,
                                     @PathVariable(value = "hotelId") long hotelId, Model model) {

        Hotel hotel = null;
        try {
            hotel = hotelService.showDetail(hotelId);
            ModelAndView mav = new ModelAndView();
            mav.setViewName("front/hotel-edit");
            mav.addObject("hotel", hotel);
            mav.addObject("hotelId", String.valueOf(hotelId));
            return mav;
        } catch (HotelNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error", "К сожалению отель не найден!");
        }

        return new ModelAndView("redirect:/");
    }

    @PostMapping("/edit/{hotelId}")
    public RedirectView hotelUpdate(RedirectAttributes redirectAttributes,
                                    @PathVariable(value = "hotelId") long hotelId,
                                    @RequestParam String name, @RequestParam String description,
                                    Model model) {
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

        return new RedirectView("/", true);
    }

    @PostMapping("/book/{hotelId}")
    public RedirectView bookHotel(RedirectAttributes redirectAttributes,
                                  @PathVariable(value = "hotelId") long hotelId) {

        String userName = Utils.getUsernameFromSecurityContext();

        try {
            bookingService.bookHotel(hotelId, userName);
        } catch (HotelNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error", "К сожалению отель не найден!");
            return new RedirectView("/", true);
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error", "К сожалению пользователь не найден!");
            return new RedirectView("/", true);
        }

        redirectAttributes.addFlashAttribute("message_success",
                "Отель забранирован!");

        return new RedirectView("/", true);
    }

}
