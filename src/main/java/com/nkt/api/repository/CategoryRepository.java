package com.nkt.api.repository;

import com.nkt.api.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByActiveTrueOrderBySortOrderAsc();
}
