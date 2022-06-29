package com.blockwit.booking.repository;

import com.blockwit.booking.entity.Picture;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PictureRepository extends CrudRepository<Picture, Long> {

}
