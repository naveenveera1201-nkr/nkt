package com.nkt.api.service;

import com.nkt.api.model.Banner;
import com.nkt.api.model.Category;
import com.nkt.api.model.Store;
import com.nkt.api.repository.BannerRepository;
import com.nkt.api.repository.CategoryRepository;
import com.nkt.api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscoverService {

    private final CategoryRepository categoryRepository;
    private final BannerRepository   bannerRepository;
    private final StoreRepository    storeRepository;

    // API 12 – List All Categories
    public List<Category> listCategories() {
        return categoryRepository.findByActiveTrueOrderBySortOrderAsc();
    }

    // API 13 – Get Hero Banner
    public List<Banner> getHeroBanner() {
        return bannerRepository.findByActiveTrueOrderBySortOrderAsc();
    }

    // API 14 – Get Nearby Stores Strip
    public List<Store> getNearbyStores(Double latitude, Double longitude, Double radiusKm) {
        double radius = (radiusKm != null) ? radiusKm : 5.0;
        List<Store> all = storeRepository.findByStatus("ACTIVE");
        return all.stream()
                .filter(s -> s.getAddress() != null
                        && s.getAddress().getLatitude()  != null
                        && s.getAddress().getLongitude() != null
                        && haversine(latitude, longitude,
                                     s.getAddress().getLatitude(),
                                     s.getAddress().getLongitude()) <= radius)
                .sorted(Comparator.comparingDouble(s ->
                        haversine(latitude, longitude,
                                  s.getAddress().getLatitude(),
                                  s.getAddress().getLongitude())))
                .collect(Collectors.toList());
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2)*Math.sin(dLat/2)
                 + Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))
                   *Math.sin(dLon/2)*Math.sin(dLon/2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }
}
