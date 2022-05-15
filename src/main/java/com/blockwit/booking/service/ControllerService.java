package com.blockwit.booking.service;

import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.repository.HotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class ControllerService {

    private HotelRepository hotelRepository;

    public ControllerService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public ModelAndView showHotels(String viewName, String attributeName) {

        Iterable<Hotel> hotels = hotelRepository.findAll();
        ModelAndView mav=new ModelAndView();
        mav.setViewName(viewName);
        mav.addObject(attributeName, hotels);

        return mav;
    }

}
