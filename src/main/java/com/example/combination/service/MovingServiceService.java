package com.example.combination.service;

import com.example.combination.domain.movingService.MovingService;
import com.example.combination.domain.movingService.MovingServiceStatus;
import com.example.combination.exception.InvalidMovingDateException;
import com.example.combination.exception.MovingServiceNotFoundExceptiion;
import com.example.combination.repository.MovingServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovingServiceService {

    private final MovingServiceRepository movingServiceRepository;

    //변경 감지
    @Transactional
    public void updateMovingServiceStatus(Long id, MovingServiceStatus newStatus) {
        MovingService movingService = movingServiceRepository.findByMovingId(id)
                .orElseThrow(() -> new MovingServiceNotFoundExceptiion(id));
        movingService.changeMovingServiceStatus(newStatus);
    }
    
    /**
     * 이사 날짜 검증
     */
    public void validateMovingDate(LocalDate movingDate) {
        if (movingDate == null) {
            throw new InvalidMovingDateException(null, "이사 날짜가 비어있습니다.");
        }
        
        LocalDate today = LocalDate.now();
        
        // 과거 날짜 검증
        if (movingDate.isBefore(today)) {
            throw new InvalidMovingDateException(movingDate, "과거 날짜는 선택할 수 없습니다.");
        }
        
        // 너무 먼 미래 검증 (1년 후)
        if (ChronoUnit.DAYS.between(today, movingDate) > 365) {
            throw new InvalidMovingDateException(movingDate, "1년 이내의 날짜만 선택 가능합니다.");
        }
        
        // 휴일 검증 (일요일)
        if (movingDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new InvalidMovingDateException(movingDate, "일요일은 이사 서비스가 불가능합니다.");
        }
        
        // 공휴일 검증 (간단한 예시 - 실제로는 공휴일 API 연동 필요)
        if (isHoliday(movingDate)) {
            throw new InvalidMovingDateException(movingDate, "공휴일은 이사 서비스가 불가능합니다.");
        }
    }
    
    /**
     * 공휴일 여부 확인 (간단한 예시)
     */
    private boolean isHoliday(LocalDate date) {
        // 실제로는 공휴일 API나 데이터베이스에서 조회해야 함
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        
        // 신정, 설날, 추석 등 주요 공휴일 (간단한 예시)
        return (month == 1 && day == 1) || // 신정
               (month == 3 && day == 1) || // 삼일절
               (month == 5 && day == 5) || // 어린이날
               (month == 6 && day == 6) || // 현충일
               (month == 8 && day == 15) || // 광복절
               (month == 10 && day == 3) || // 개천절
               (month == 10 && day == 9) || // 한글날
               (month == 12 && day == 25); // 크리스마스
    }
}
