package com.blockwit.booking.service;

import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.entity.User;
import com.blockwit.booking.exceptions.HotelNotFoundException;
import com.blockwit.booking.exceptions.UserNotFoundException;
import com.blockwit.booking.repository.HotelRepository;
import com.blockwit.booking.security.SecurityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class HotelService {

    private HotelRepository hotelRepository;
    private UserService userService;
    private SecurityService securityService;

    public Iterable<Hotel> hotels() {
        return hotelRepository.findAll();
    }

    public boolean saveHotelResponse(Hotel hotel) {
        Hotel result = hotelRepository.save(hotel);
        return result != null;
    }

    public Hotel showDetail(Long hotelId) throws HotelNotFoundException {
        return hotelRepository.findById(hotelId).orElseThrow(() -> new HotelNotFoundException());
    }

    public Hotel hotelUpdate(long hotelId, Hotel hotel) throws HotelNotFoundException {
        Hotel hotelForUpdate = hotelRepository.findById(hotelId).orElseThrow(() -> new HotelNotFoundException());
        hotelForUpdate.setName(hotel.getName());
        hotelForUpdate.setDescription(hotel.getDescription());
        return hotelRepository.save(hotelForUpdate);
    }

    public boolean checkEditingPermission(long hotelId, String userName)
            throws HotelNotFoundException, UserNotFoundException {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new HotelNotFoundException());
        Long userId= userService.getIdByUsername(userName);
        return securityService.checkRoleFromSecurityContext("ADMIN") || hotel.getOwnerId().equals(userId);

    }
}
