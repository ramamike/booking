package com.blockwit.booking.service;

import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Service
@Slf4j
public class HotelService {

    private HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public ModelAndView showHotels(String viewName, String attributeName) {

        Iterable<Hotel> hotels = hotelRepository.findAll();
        ModelAndView mav=new ModelAndView();
        mav.setViewName(viewName);
        mav.addObject(attributeName, hotels);
        return mav;
    }

    public boolean saveHotelResponse(Hotel hotel){
        Hotel result=hotelRepository.save(hotel);
        return result!=null;
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

    public boolean hotelUpdate(long hotelId, Hotel hotel) {

        Hotel hotelForUpdate = hotelRepository.findById(hotelId).orElseThrow();
        hotelForUpdate.setName(hotel.getName());
        hotelForUpdate.setDescription(hotel.getDescription());
        Hotel result = hotelRepository.save(hotelForUpdate);

        return hotelForUpdate!=null && result!=null;
    }
}
