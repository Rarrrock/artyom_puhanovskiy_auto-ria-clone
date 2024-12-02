package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.AdRequest;
import org.example.dto.AdResponse;
import org.example.dto.AdStatisticsResponse;
import org.example.entity.Ad;
import org.example.entity.User;
import org.example.enums.AccountType;
import org.example.mapper.AdMapper;
import org.example.repository.AdRepository;
import org.example.repository.UserRepository;
import org.example.service.AdService;
import org.example.service.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    // Создаю Объявление
    @Override
    public AdResponse createAd(AdRequest adRequest, String email) {
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден."));

        // Проверяю ограничения для BASIC-аккаунта
        if (owner.getAccountType() == AccountType.BASIC) {
            long adCount = adRepository.countByOwnerId(owner.getId()); // Метод должен быть реализован в AdRepository
            if (adCount >= 1) {
                throw new IllegalArgumentException("Пользователи с BASIC аккаунтом могут создать только одно объявление.");
            }
        }

        Ad ad = AdMapper.mapToEntity(adRequest, owner);
        return AdMapper.mapToResponse(adRepository.save(ad));
    }

    // Получаю все Объявления
    @Override
    public List<AdResponse> getAllAds() {
        return adRepository.findAll().stream()
                .map(AdMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    // Получаю Объявление по ID
    @Override
    public AdResponse getAdById(Long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Объявление с ID " + id + " не найдено."));
        return AdMapper.mapToResponse(ad);
    }

    // Переписываю Объявление
    @Override
    public AdResponse updateAd(Long id, AdRequest adRequest) {
        Ad existingAd = adRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Объявление с ID " + id + " не найдено."));

        User owner = existingAd.getOwner();

        if (!userService.isCurrentUserOrAdmin(owner.getEmail(), owner.getId())) {
            throw new SecurityException("Недостаточно прав для выполнения операции.");
        }

        existingAd.setTitle(adRequest.getTitle());
        existingAd.setDescription(adRequest.getDescription());
        existingAd.setPrice(adRequest.getPrice());
        existingAd.setCurrency(adRequest.getCurrency());

        return AdMapper.mapToResponse(adRepository.save(existingAd));
    }

    // Удаляю Объявление
    @Override
    public void deleteAd(Long id) {
        Ad existingAd = adRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Объявление с ID " + id + " не найдено."));

        User owner = existingAd.getOwner();

        if (!userService.isCurrentUserOrAdmin(owner.getEmail(), owner.getId())) {
            throw new SecurityException("Недостаточно прав для выполнения операции.");
        }

        adRepository.delete(existingAd);
    }

    // Выполняю запрос с фильтрацией
    @Override
    public List<AdResponse> filterAds(BigDecimal minPrice, BigDecimal maxPrice, String currency, String status) {

        List<Ad> ads = adRepository.findFilteredAds(minPrice, maxPrice, currency, status);

        return ads.stream()
                .map(AdMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    // Выполняю запрос на сбор статистики
    @Override
    public List<AdStatisticsResponse> getStatistics(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден."));

        // Проверяю, что пользователь является PREMIUM
        if (user.getAccountType() != AccountType.PREMIUM) {
            throw new SecurityException("Доступ к статистике доступен только для PREMIUM аккаунтов.");
        }

        // Собираю статистику через репозиторий
        List<Object[]> rawStats = adRepository.fetchAdStatistics(user.getId());
        return rawStats.stream()
                .map(row -> new AdStatisticsResponse(
                        (Long) row[0], // ID объявления
                        (String) row[1], // Название
                        (Integer) row[2], // Количество просмотров
                        (Double) row[3])) // Средняя цена
                .collect(Collectors.toList());
    }
}