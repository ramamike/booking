package com.blockwit.booking.controllers;

import com.blockwit.booking.entity.Booking;
import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.repository.BookingRepository;
import com.blockwit.booking.repository.HotelRepository;
import com.blockwit.booking.service.ControllerService;
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

	@Autowired
	private ControllerService controllerService;

	@Autowired
	HotelRepository hotelRepo;

	@Autowired
	BookingRepository bookingRepo;

	@GetMapping
	public ModelAndView home() {
		return controllerService.showHotels("front/home", "hotels");
	}

	@GetMapping("/add")
	public String hotelAdd() {
		return "front/hotel-add";
	}

	@PostMapping("/add")
	public RedirectView hotelAddToDB(HttpServletRequest request,
							   RedirectAttributes redirectAttributes,
							   @RequestParam String name,
							   @RequestParam String description,
							   Model model) {
		Hotel hotel = Hotel.builder()
			.name(name)
			.description(description)
			.build();
		hotelRepo.save(hotel);

		redirectAttributes.addFlashAttribute("message_success", "Отель " + hotel.getName() + " успешно создан!");
		return new RedirectView("/", true);
	}

	@GetMapping("/edit/{hotelId}")
	public String hotelDetails(@PathVariable(value = "hotelId") long hotelId, Model model) {
		Optional<Hotel> hotel = hotelRepo.findById(hotelId);
		if (hotel.isPresent()) {
			model.addAttribute("hotel", hotel.get());
			return "front/hotel-edit";
		}
		return "redirect:/";
	}

	@PostMapping("/edit/{hotelId}")
	public String hotelUpdate(@PathVariable(value = "hotelId") long hotelId,
							  @RequestParam String name, @RequestParam String description, Model model) {
		Hotel hotel = hotelRepo.findById(hotelId).orElseThrow();
		hotel.setName(name);
		hotel.setDescription(description);
		hotelRepo.save(hotel);
		return "redirect:/";
	}

	@PostMapping("/book/{hotelId}")
	public String bookHotel(@PathVariable(value = "hotelId") long hotelId, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentName = authentication.getName();

		Booking booking = Booking.builder()
			.nameUser(currentName)
			.build();

		bookingRepo.save(booking);
		return "redirect:/";
	}

}
