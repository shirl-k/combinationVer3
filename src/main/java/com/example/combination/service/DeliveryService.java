package com.example.combination.service;

import com.example.combination.domain.delivery.DeliveryAddressForm;
import com.example.combination.domain.delivery.DeliveryStatus;
import com.example.combination.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public void changeDeliveryStatus(Long deliveryId, DeliveryStatus newStatus) {
        DeliveryAddressForm deliveryAddressForm = DeliveryRepository.findById(deliveryId)


    }

}
