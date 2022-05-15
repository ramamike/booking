package com.blockwit.booking.service;

import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.repository.HotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Service
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

    public void saveHotel(Hotel hotel){
        hotelRepository.save(hotel);
    }

    public ModelAndView showDetail(Long hotelId){

        Optional<Hotel> hotel = hotelRepository.findById(hotelId);

        ModelAndView mav=new ModelAndView();
        if (hotel.isPresent()) {
            mav.setViewName("front/hotel-edit");
            mav.addObject("hotel", hotel.get());
            return mav;
        }

        return new ModelAndView("redirect:/");
    }

}
