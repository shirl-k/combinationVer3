package com.example.combination.domain.external;

import com.example.combination.domain.valuetype.HomeAddress;
import com.example.combination.domain.valuetype.MovingServiceAddress;

/**
 * 지도 API 서비스 인터페이스
 * 주소 검증, 거리 계산, 경로 최적화 등의 기능을 제공합니다.
 */
public interface MapApiService {
    
    /**
     * 주소 유효성을 검증합니다.
     * @param address 검증할 주소
     * @return 유효한 주소 여부
     */
    boolean validateAddress(String address);
    
    /**
     * 두 주소 간의 거리를 계산합니다.
     * @param fromAddress 출발지 주소
     * @param toAddress 도착지 주소
     * @return 거리 (km)
     */
    double calculateDistance(String fromAddress, String toAddress);
    
    /**
     * 배송 주소의 상세 정보를 조회합니다.
     * @param address 주소
     * @return 주소 상세 정보
     */
    AddressDetail getAddressDetail(String address);
    
    /**
     * 이사 서비스의 최적 경로를 계산합니다.
     * @param homeAddress 기존 집 주소
     * @param newAddress 새 집 주소
     * @return 최적 경로 정보
     */
    RouteInfo calculateOptimalRoute(HomeAddress homeAddress, MovingServiceAddress newAddress);
    
    /**
     * 배송 가능 지역인지 확인합니다.
     * @param address 확인할 주소
     * @return 배송 가능 여부
     */
    boolean isDeliverableArea(String address);
    
    /**
     * 주소 상세 정보를 담는 클래스
     */
    class AddressDetail {
        private String fullAddress;
        private String roadAddress;
        private String jibunAddress;
        private double latitude;
        private double longitude;
        private String buildingName;
        private String dongName;
        
        // Getters and Setters
        public String getFullAddress() { return fullAddress; }
        public void setFullAddress(String fullAddress) { this.fullAddress = fullAddress; }
        
        public String getRoadAddress() { return roadAddress; }
        public void setRoadAddress(String roadAddress) { this.roadAddress = roadAddress; }
        
        public String getJibunAddress() { return jibunAddress; }
        public void setJibunAddress(String jibunAddress) { this.jibunAddress = jibunAddress; }
        
        public double getLatitude() { return latitude; }
        public void setLatitude(double latitude) { this.latitude = latitude; }
        
        public double getLongitude() { return longitude; }
        public void setLongitude(double longitude) { this.longitude = longitude; }
        
        public String getBuildingName() { return buildingName; }
        public void setBuildingName(String buildingName) { this.buildingName = buildingName; }
        
        public String getDongName() { return dongName; }
        public void setDongName(String dongName) { this.dongName = dongName; }
    }
    
    /**
     * 경로 정보를 담는 클래스
     */
    class RouteInfo {
        private double totalDistance;
        private int estimatedTime; // 분 단위
        private String routeDescription;
        private java.util.List<String> waypoints;
        
        // Getters and Setters
        public double getTotalDistance() { return totalDistance; }
        public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }
        
        public int getEstimatedTime() { return estimatedTime; }
        public void setEstimatedTime(int estimatedTime) { this.estimatedTime = estimatedTime; }
        
        public String getRouteDescription() { return routeDescription; }
        public void setRouteDescription(String routeDescription) { this.routeDescription = routeDescription; }
        
        public java.util.List<String> getWaypoints() { return waypoints; }
        public void setWaypoints(java.util.List<String> waypoints) { this.waypoints = waypoints; }
    }
}
