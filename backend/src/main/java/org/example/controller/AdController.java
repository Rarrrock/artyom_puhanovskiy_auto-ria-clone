package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.AdRequest;
import org.example.dto.AdResponse;
import org.example.service.AdService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    //  Создаю Объявление
    @PostMapping
    public ResponseEntity<AdResponse> createAd(@RequestBody AdRequest adRequest, Authentication authentication) {
        AdResponse createdAd = adService.createAd(adRequest, authentication.getName());
        return ResponseEntity.ok(createdAd);
    }

    //  Получаю все Объявления
    @GetMapping
    public ResponseEntity<List<AdResponse>> getAllAds() {
        List<AdResponse> ads = adService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    //  Получаю Объявление по ID
    @GetMapping("/{id}")
    public ResponseEntity<AdResponse> getAdById(@PathVariable Long id) {
        AdResponse ad = adService.getAdById(id);
        return ResponseEntity.ok(ad);
    }

    //  Переписываю Объявление
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AdResponse> updateAd(@PathVariable Long id, @RequestBody AdRequest adRequest) {
        AdResponse updatedAd = adService.updateAd(id, adRequest);
        return ResponseEntity.ok(updatedAd);
    }

    //  Удаляю Объявление
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<AdResponse>> filterAds(@RequestParam(required = false) BigDecimal minPrice,
                                                      @RequestParam(required = false) BigDecimal maxPrice,
                                                      @RequestParam(required = false) String currency,
                                                      @RequestParam(required = false) String status) {
        List<AdResponse> filteredAds = adService.filterAds(minPrice, maxPrice, currency, status);
        return ResponseEntity.ok(filteredAds);
    }
}