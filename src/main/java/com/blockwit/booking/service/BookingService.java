package com.blockwit.booking.service;

import com.blockwit.booking.entity.Booking;
import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.exceptions.HotelNotFoundException;
import com.blockwit.booking.repository.BookingRepository;
import com.blockwit.booking.repository.HotelRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
@Slf4j
@AllArgsConstructor
public class BookingService {

    private BookingRepository bookingRepository;
    private HotelRepository hotelRepository;

    public Hotel bookHotel(long hotelId) throws HotelNotFoundException {

        //check if hotel is existed
        Hotel hotelForBook = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentName = authentication.getName();

        Booking booking = Booking.builder()
                .nameUser(currentName)
                .hotelId(hotelId)
                .build();
        bookingRepository.save(booking);

        return hotelForBook;
    }

}
