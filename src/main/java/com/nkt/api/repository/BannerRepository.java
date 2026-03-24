package com.nkt.api.repository;

import com.nkt.api.model.Banner;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BannerRepository extends MongoRepository<Banner, String> {
    List<Banner> findByActiveTrueOrderBySortOrderAsc();
}
