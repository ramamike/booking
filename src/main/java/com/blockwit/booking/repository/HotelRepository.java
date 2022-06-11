package com.blockwit.booking.repository;

import com.blockwit.booking.entity.Hotel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends CrudRepository<Hotel, Long> {

    @Query(
            value = "SELECT * FROM hotels inner join bookings on hotels.id=bookings.hotel_id \n" +
                    "where bookings.user_id=?1",
            nativeQuery = true
    )
    Iterable<Hotel> getBookedHotel(Long userId);
}
