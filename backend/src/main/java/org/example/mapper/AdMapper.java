package org.example.mapper;

import org.example.dto.AdResponse;
import org.example.entity.Ad;

public class AdMapper {
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