package com.blockwit.booking.service;

import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.exceptions.HotelNotFoundException;
import com.blockwit.booking.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

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
        ModelAndView mav = new ModelAndView();
        mav.setViewName(viewName);
        mav.addObject(attributeName, hotels);
        return mav;
    }

    public boolean saveHotelResponse(Hotel hotel) {
        Hotel result = hotelRepository.save(hotel);
        return result != null;
    }

    public Hotel showDetail(Long hotelId) throws HotelNotFoundException {

        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new HotelNotFoundException());

        return hotel;
    }

    public Hotel hotelUpdate(long hotelId, Hotel hotel) throws HotelNotFoundException {
        Hotel hotelForUpdate = hotelRepository.findById(hotelId).orElseThrow(() -> new HotelNotFoundException());
        hotelForUpdate.setName(hotel.getName());
        hotelForUpdate.setDescription(hotel.getDescription());
        return hotelRepository.save(hotelForUpdate);
    }
}
