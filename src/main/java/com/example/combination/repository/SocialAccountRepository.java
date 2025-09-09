package com.example.combination.repository;

import com.example.combination.domain.member.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    
    Optional<SocialAccount> findByProviderAndProviderId(String provider, String providerId);
    
    Optional<SocialAccount> findByProviderAndEmail(String provider, String email);
    
    boolean existsByProviderAndProviderId(String provider, String providerId);
}
