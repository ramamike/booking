package com.blockwit.booking.service;

import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.security.auth.Subject;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class HotelService {

    private HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public ModelAndView showHotels(String viewName, String attributeName) {

        log.info("showHotels method is invoked");

        Iterable<Hotel> hotels = hotelRepository.findAll();
        ModelAndView mav=new ModelAndView();
        mav.setViewName(viewName);
        mav.addObject(attributeName, hotels);
        return mav;
    }

    public RedirectView saveHotel(Hotel hotel, RedirectAttributes redirectAttributes){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if("[ROLE_SERVICE_PROVIDER]".equals(authentication.getAuthorities().toString())){
        hotelRepository.save(hotel);
        redirectAttributes.addFlashAttribute("message_success",
                "Отель " + hotel.getName() + " успешно создан!");
        return new RedirectView("/", true);
        }
        redirectAttributes.addFlashAttribute("message_error",
                "Отель не создан!");
        return new RedirectView("/", true);
    }

    public ModelAndView showDetail(Long hotelId){

        Optional<Hotel> hotel = hotelRepository.findById(hotelId);

        ModelAndView mav=new ModelAndView();
        if (hotel.isPresent()) {
            mav.setViewName("front/hotel-edit");
            mav.addObject("hotel", hotel.get());
            mav.addObject("hotelId", String.valueOf(hotelId));
            return mav;
        }

        return new ModelAndView("redirect:/");
    }

    public RedirectView hotelUpdate(RedirectAttributes redirectAttributes, long hotelId, Hotel hotel) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if("[ROLE_SERVICE_PROVIDER]".equals(authentication.getAuthorities().toString())) {
            Hotel hotelForUpdate = hotelRepository.findById(hotelId).orElseThrow();
            hotelForUpdate.setName(hotel.getName());
            hotelForUpdate.setDescription(hotel.getDescription());
            hotelRepository.save(hotelForUpdate);

            redirectAttributes.addFlashAttribute("message_success",
                    "Исправления применены!");
            return new RedirectView("/", true);
        }
        redirectAttributes.addFlashAttribute("message_error",
                "Исправления неприменены!");
        return new RedirectView("/", true);
    }
}
