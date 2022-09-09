package com.blockwit.booking.controllers;

import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.entity.Room;
import com.blockwit.booking.entity.User;
import com.blockwit.booking.exceptions.HotelNotFoundException;
import com.blockwit.booking.exceptions.RoomNotFoundException;
import com.blockwit.booking.exceptions.UserNotFoundException;
import com.blockwit.booking.model.Error;
import com.blockwit.booking.security.SecurityService;
import com.blockwit.booking.service.BookingService;
import com.blockwit.booking.service.HotelService;
import com.blockwit.booking.service.RoomService;
import com.blockwit.booking.service.UserService;
import io.vavr.control.Either;
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
@RequestMapping({"/", "/hotels"})
@AllArgsConstructor
@Slf4j
public class RoomsController {

    private RoomService roomService;
    private HotelService hotelService;
    private BookingService bookingService;
    private UserService userService;
    private SecurityService securityService;

    @GetMapping("/{hotelId}/rooms/add")
    public ModelAndView hotelAdd(
            RedirectAttributes redirectAttributes,
            @PathVariable(value = "hotelId") long hotelId,
            Model model
    ) {

        Optional<Hotel> hotelOptional = hotelService.getHotelById(hotelId);
        ModelAndView mav = new ModelAndView();
        if (hotelOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("message_error",
                    "Не удалось определить отель, " +
                            " для корректного добавления комнаты");
            return new ModelAndView("redirect:/hotels");
        }

        mav.setViewName("front/room-add");
        mav.addObject("hotel", hotelOptional.get());

        return mav;
    }

    @PostMapping("/{hotelId}/rooms/add")
    public RedirectView postRoom(
            RedirectAttributes redirectAttributes,
            @PathVariable(value = "hotelId") long hotelId,
            @RequestParam String name,
            @RequestParam String description,
            Model model) {

        String userName = securityService.getUsernameFromSecurityContext();
        boolean isAdmin = securityService.checkRoleFromSecurityContext("ADMIN");

        try {
            if (!isAdmin && !hotelService.checkEditingPermission(hotelId, userName)) {
                redirectAttributes.addFlashAttribute("message_error",
                        userName + " не может добавлять комнату для данного отеля");
                return new RedirectView("/hotels/{hotelId}", true);
            }
        } catch (HotelNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error",
                    "К сожалению, не удалось получить информация для пользователя");
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error",
                    "К сожалению, не удалось получить информация для пользователя");
        }
        Optional<User> userOptional = userService.getUserByUsername(userName);

        if (userOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("message_error",
                    "Не удалось определить пользователя, " +
                            " для корректного добавления комнаты");
            return new RedirectView("/hotels", true);
        }

        Optional<Hotel> hotelOptional = hotelService.getHotelById(hotelId);
        if (hotelOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("message_error",
                    "Не удалось определить отель, " +
                            " для корректного добавления комнаты");
            return new RedirectView("/hotels", true);
        }
        Room room = Room.builder()
                .name(name)
                .description(description)
                .hotel(hotelOptional.get())
                .build();

        if (roomService.saveRoomResponse(room)) {
            redirectAttributes.addFlashAttribute("message_success",
                    "Комната " + room.getName() + " успешно создана!");
        } else {
            redirectAttributes.addFlashAttribute("message_error",
                    "Ошибка при создании комнаты!");
        }
        return new RedirectView("/hotels/{hotelId}", true);
    }

    @GetMapping("/{hotelId}/rooms/{roomId}/edit")
    public ModelAndView hotelDetails(RedirectAttributes redirectAttributes,
                                     @PathVariable(value = "hotelId") long hotelId,
                                     @PathVariable(value = "roomId") long roomId, Model model) {

        Room room = null;
        try {
            room = roomService.getById(roomId).orElseThrow(() -> new RoomNotFoundException());
            ModelAndView mav = new ModelAndView();
            mav.addObject("hotelId", String.valueOf(hotelId));
            mav.setViewName("front/room-edit");
            mav.addObject("room", room);
            mav.addObject("roomId", String.valueOf(roomId));
            return mav;
        } catch (RoomNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error", "К сожалению комната не найдена!");
        }

        return new ModelAndView("redirect:/hotels/{hotelId}");
    }

    @PostMapping("/{hotelId}/rooms/{roomId}/edit")
    public RedirectView roomUpdate(RedirectAttributes redirectAttributes,
                                   @PathVariable(value = "hotelId") long hotelId,
                                   @PathVariable(value = "roomId") long roomId,
                                   @RequestParam String name, @RequestParam String description) {

        String userName = securityService.getUsernameFromSecurityContext();

        try {
            if (!hotelService.checkEditingPermission(hotelId, userName)) {
                redirectAttributes.addFlashAttribute("message_error",
                        userName + " не может редактировать данную запись");
                return new RedirectView("/hotels/{hotelId}", true);
            }
        } catch (HotelNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error",
                    "К сожалению, не удалось получить информация для пользователя");
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error",
                    "К сожалению, не удалось получить информация для пользователя");
        }

        Room room = Room.builder()
                .name(name)
                .description(description)
                .build();

        try {
            roomService.roomUpdate(roomId, room);
            redirectAttributes.addFlashAttribute("message_success",
                    "Информацию о комнате обновили успешно!");
        } catch (RoomNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error",
                    "К сожалению, не удалось обновить информацию о комнате!");
        }

        return new RedirectView("/hotels/{hotelId}", true);
    }

    @PostMapping("/{hotelId}/rooms/{roomId}/book")
    public RedirectView bookHotel(RedirectAttributes redirectAttributes,
                                  @PathVariable(value = "roomId") long roomId) {

        String userName = securityService.getUsernameFromSecurityContext();

        try {
            if (!bookingService.bookRoom(roomId, userName)) {
                redirectAttributes.addFlashAttribute("message_error",
                        "К сожалению комната не забронирована!");
            }
        } catch (RoomNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error",
                    "К сожалению комната не найдена!");
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error",
                    "К сожалению пользователь не найден!");
            return new RedirectView("/hotels/{hotelId}", true);
        }

        redirectAttributes.addFlashAttribute("message_success",
                "Комната забранирована!");

        return new RedirectView("/hotels/{hotelId}", true);
    }


    @GetMapping("/booked")
    public ModelAndView bookedRooms(RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView();

        String userName = securityService.getUsernameFromSecurityContext();

        Optional<User> userOptional = userService.getUserByUsername(userName);

        Iterable<Room> bookedRooms = null;
        if (userOptional.isPresent()) {
            try {
                bookedRooms = roomService.getBookedRooms(userOptional.get().getId());
            } catch (RoomNotFoundException e) {
                redirectAttributes.addFlashAttribute("message_error",
                        "Не удалось определить забронированные комнаты, попробуйте еще раз");
            }

        } else if (userOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("message_error",
                    "Не удалось определить пользователя, " +
                            " для корректного отображения данных");
            return new ModelAndView("redirect:/hotels");
        }
        mav.setViewName("front/rooms-booked");
        mav.addObject("bookedRooms", bookedRooms);
        return mav;
    }

    @PostMapping("/{hotelId}/rooms/{roomId}/delete")
    public RedirectView roomDelete(RedirectAttributes redirectAttributes,
                           @PathVariable(value = "hotelId") long hotelId,
                           @PathVariable(value = "roomId") long roomId) {

        Either <Error, Room> room = roomService.deleteById(roomId);
            redirectAttributes.addFlashAttribute("message_success",
                    room.get().getName() + " удалена! ");
        return new RedirectView("/hotels/{hotelId}", true);
//        String userName = securityService.getUsernameFromSecurityContext();
//        boolean isAdmin = securityService.checkRoleFromSecurityContext("ADMIN");
//
//        try {
//            if (!isAdmin && !hotelService.checkEditingPermission(hotelId, userName)) {
//                redirectAttributes.addFlashAttribute("message_error",
//                        userName + " не может редактировать данную запись");
//                return new RedirectView("/hotels/{hotelId}", true);
//            }
//        } catch (HotelNotFoundException e) {
//            redirectAttributes.addFlashAttribute("message_error",
//                    "К сожалению, не удалось получить информация для пользователя");
//        } catch (UserNotFoundException e) {
//            redirectAttributes.addFlashAttribute("message_error",
//                    "К сожалению, не удалось получить информация для пользователя");
//        }

//        try {
//            roomService.roomUpdate(roomId, room);
//            redirectAttributes.addFlashAttribute("message_success",
//                    "Информацию о комнате обновили успешно!");
//        } catch (RoomNotFoundException e) {
//            redirectAttributes.addFlashAttribute("message_error",
//                    "К сожалению, не удалось обновить информацию о комнате!");
//        }
//
//        return new RedirectView("/hotels/{hotelId}", true);
    }

}
