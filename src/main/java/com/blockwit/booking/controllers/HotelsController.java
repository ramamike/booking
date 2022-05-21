package com.blockwit.booking.controllers;

import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.exceptions.HotelNotFoundException;
import com.blockwit.booking.service.BookingService;
import com.blockwit.booking.service.HotelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/hotels")
public class HotelsController {

    private HotelService hotelService;
    private BookingService bookingService;

    public HotelsController(HotelService hotelService, BookingService bookingService) {
        this.hotelService = hotelService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public ModelAndView home() {
        return hotelService.showHotels("front/hotels", "hotels");
    }

    @GetMapping("/add")
    public String hotelAdd() {
        return "front/hotel-add";
    }

    @PostMapping("/add")
    public RedirectView hotelAddToDB(
            RedirectAttributes redirectAttributes,
            @RequestParam String name,
            @RequestParam String description,
            Model model) {

        Hotel hotel = Hotel.builder()
                .name(name)
                .description(description)
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
        } catch (HotelNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error", "К сожалению отель не найден!");
        }

        ModelAndView mav = new ModelAndView();
        if (hotel != null) {
            mav.setViewName("front/hotel-edit");
            mav.addObject("hotel", hotel);
            mav.addObject("hotelId", String.valueOf(hotelId));
            return mav;
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

        try {
            bookingService.bookHotel(hotelId);
        } catch (HotelNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error", "К сожалению отель не найден!");
            return new RedirectView("/", true);
        }

        redirectAttributes.addFlashAttribute("message_success",
                "Отель забранирован!");

        return new RedirectView("/", true);
    }

}
