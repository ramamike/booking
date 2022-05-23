package com.blockwit.booking.service;

import com.blockwit.booking.entity.Booking;
import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.exceptions.HotelNotFoundException;
import com.blockwit.booking.exceptions.UserNotFoundException;
import com.blockwit.booking.repository.BookingRepository;
import com.blockwit.booking.repository.HotelRepository;
import com.blockwit.booking.repository.UserRepository;
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
    private UserRepository userRepository;


    public Hotel bookHotel(long hotelId, String clientName)
            throws HotelNotFoundException, UserNotFoundException {

        //check if hotel is existed
        Hotel hotelForBook = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException());

        Long userId = userRepository.getUserByEmail(clientName)
                .orElseThrow(() -> new UserNotFoundException()).getId();

        Booking booking = Booking.builder()
                .hotelId(hotelId)
                .userId(userId)
                .build();
        bookingRepository.save(booking);

        return hotelForBook;
    }

}
