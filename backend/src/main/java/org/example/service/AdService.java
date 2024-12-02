package org.example.service;

import org.example.dto.AdRequest;
import org.example.dto.AdResponse;
import org.example.dto.AdStatisticsResponse;

import java.math.BigDecimal;
import java.util.List;

// Основные методы работы с Объявлениями
public interface AdService {

    AdResponse createAd(AdRequest adRequest, String email);  // Создаю Объявление
    List<AdResponse> getAllAds();  // Получаю все Объявления
    AdResponse getAdById(Long id);  // Получаю Объявление по ID
    AdResponse updateAd(Long id, AdRequest adRequest);  // Переписываю Объявление
    void deleteAd(Long id);  // Удаляю Объявление

    // Фильтрую Объявления по заданным параметрам
    List<AdResponse> filterAds(BigDecimal minPrice, BigDecimal maxPrice, String currency, String status);

    // Создаю запрос на сбор статистики
    List<AdStatisticsResponse> getStatistics(String email);
}