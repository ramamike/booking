package com.blockwit.booking.service;

import com.blockwit.booking.entity.Booking;
import com.blockwit.booking.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
@Slf4j
public class BookingService {

    private BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public boolean bookHotel(long hotelId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentName = authentication.getName();

        Booking booking = Booking.builder()
                .nameUser(currentName)
                .hotelId(hotelId)
                .build();

        Booking result = bookingRepository.save(booking);

        return result!=null;
    }

}
