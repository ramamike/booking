package com.blockwit.booking.repository;

import com.blockwit.booking.entity.Hotel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends CrudRepository<Hotel, Long> {

    @Query("SELECT h FROM Hotel h join h.bookings b where b.userId=?1")
    Iterable<Hotel> getBookedHotels(Long userId);
}
