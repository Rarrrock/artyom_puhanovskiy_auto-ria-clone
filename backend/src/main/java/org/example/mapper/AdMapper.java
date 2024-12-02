package org.example.mapper;

import org.example.dto.AdRequest;
import org.example.dto.AdResponse;
import org.example.dto.CarRequest;
import org.example.entity.Ad;
import org.example.entity.Car;
import org.example.entity.User;

public class AdMapper {
    public static Ad mapToEntity(AdRequest adRequest, User owner) {
        return Ad.builder()
                .title(adRequest.getTitle())
                .description(adRequest.getDescription())
                .price(adRequest.getPrice())
                .currency(adRequest.getCurrency())
                .owner(owner)
                .build();
    }

    public static AdResponse mapToResponse(Ad ad) {
        return AdResponse.builder()
                .id(ad.getId())
                .title(ad.getTitle())
                .description(ad.getDescription())
                .price(ad.getPrice())
                .currency(ad.getCurrency())
                .status(ad.getStatus())
                .build();
    }
}