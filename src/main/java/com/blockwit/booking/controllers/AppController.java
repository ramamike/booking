package com.blockwit.booking.controllers;

import com.blockwit.booking.entity.Booking;
import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.repository.BookingRepository;
import com.blockwit.booking.repository.HotelRepository;
import com.blockwit.booking.service.BookingService;
import com.blockwit.booking.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class AppController {

	private HotelService hotelService;
	private BookingService bookingService;

	public AppController(HotelService hotelService, BookingService bookingService) {
		this.hotelService = hotelService;
		this.bookingService = bookingService;
	}

	@GetMapping
	public ModelAndView home() {
		return hotelService.showHotels("front/home", "hotels");
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
					"Отель " + hotel.getName() + " успешно создан!");}
			else {
			redirectAttributes.addFlashAttribute("message_error",
					"Ошибка при создании отеля!");
			}
		return new RedirectView("/", true);
	}

	@GetMapping("/edit/{hotelId}")
	public ModelAndView hotelDetails(@PathVariable(value = "hotelId") long hotelId, Model model) {
		return hotelService.showDetail(hotelId);
	}

	@PostMapping("/edit/{hotelId}")
	public RedirectView hotelUpdate( RedirectAttributes redirectAttributes,
										@PathVariable(value = "hotelId") long hotelId,
							  			@RequestParam String name, @RequestParam String description,
									 	Model model) {
		Hotel hotel = Hotel.builder()
				.name(name)
				.description(description)
				.build();
		if(hotelService.hotelUpdate(hotelId, hotel)) {
		redirectAttributes.addFlashAttribute("message_success",
				"Информацию об отеле обновили успешно!");
		}
		else {
			redirectAttributes.addFlashAttribute("message_error",
					"Исправления не применены!");
		}

		return new RedirectView("/", true);
	}

	@PostMapping("/book/{hotelId}")
	public RedirectView bookHotel(RedirectAttributes redirectAttributes,
								  @PathVariable(value = "hotelId") long hotelId) {

		bookingService.bookHotel(hotelId);

		if(bookingService.bookHotel(hotelId)) {
			redirectAttributes.addFlashAttribute("message_success",
					"Отель забранирован!");
		}
		else {
			redirectAttributes.addFlashAttribute("message_error",
					"Отель не забранирован!");
		}

		return new RedirectView("/", true);
	}                  	

}
