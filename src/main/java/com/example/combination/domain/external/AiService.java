package com.example.combination.domain.external;

import com.example.combination.domain.item.Item;

import java.util.List;

/**
 * 생성형 AI 서비스 인터페이스
 * 인테리어 추천, 상품 추천, 고객 상담 등의 기능을 제공합니다.
 */
public interface AiService {
    
    /**
     * 사용자 선호도에 따른 인테리어 스타일을 추천합니다.
     * @param userPreferences 사용자 선호도 정보
     * @return 추천 인테리어 스타일 목록
     */
    List<InteriorStyle> recommendInteriorStyles(UserPreferences userPreferences);
    
    /**
     * 인테리어 스타일에 맞는 상품을 추천합니다.
     * @param style 인테리어 스타일
     * @param budget 예산
     * @return 추천 상품 목록
     */
    List<Item> recommendItems(InteriorStyle style, int budget);
    
    /**
     * 사용자 질문에 대한 AI 상담 응답을 생성합니다.
     * @param question 사용자 질문
     * @param context 문맥 정보 (주문 내역, 관심 상품 등)
     * @return AI 응답
     */
    String generateConsultationResponse(String question, ConsultationContext context);
    
    /**
     * 상품 설명을 AI로 생성합니다.
     * @param item 상품 정보
     * @return AI 생성 상품 설명
     */
    String generateProductDescription(Item item);
    
    /**
     * 인테리어 이미지를 생성합니다.
     * @param style 인테리어 스타일
     * @param roomType 방 타입
     * @return 생성된 이미지 URL
     */
    String generateInteriorImage(InteriorStyle style, String roomType);
    
    /**
     * 인테리어 스타일 정보
     */
    class InteriorStyle {
        private String styleName;
        private String description;
        private List<String> characteristics;
        private String colorPalette;
        private List<String> recommendedMaterials;
        
        // Getters and Setters
        public String getStyleName() { return styleName; }
        public void setStyleName(String styleName) { this.styleName = styleName; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public List<String> getCharacteristics() { return characteristics; }
        public void setCharacteristics(List<String> characteristics) { this.characteristics = characteristics; }
        
        public String getColorPalette() { return colorPalette; }
        public void setColorPalette(String colorPalette) { this.colorPalette = colorPalette; }
        
        public List<String> getRecommendedMaterials() { return recommendedMaterials; }
        public void setRecommendedMaterials(List<String> recommendedMaterials) { this.recommendedMaterials = recommendedMaterials; }
    }
    
    /**
     * 사용자 선호도 정보
     */
    class UserPreferences {
        private String preferredStyle;
        private String colorPreference;
        private int budget;
        private String roomType;
        private List<String> interests;
        
        // Getters and Setters
        public String getPreferredStyle() { return preferredStyle; }
        public void setPreferredStyle(String preferredStyle) { this.preferredStyle = preferredStyle; }
        
        public String getColorPreference() { return colorPreference; }
        public void setColorPreference(String colorPreference) { this.colorPreference = colorPreference; }
        
        public int getBudget() { return budget; }
        public void setBudget(int budget) { this.budget = budget; }
        
        public String getRoomType() { return roomType; }
        public void setRoomType(String roomType) { this.roomType = roomType; }
        
        public List<String> getInterests() { return interests; }
        public void setInterests(List<String> interests) { this.interests = interests; }
    }
    
    /**
     * 상담 문맥 정보
     */
    class ConsultationContext {
        private List<Item> recentOrders;
        private List<Item> cartItems;
        private String memberGrade;
        private List<String> previousQuestions;
        
        // Getters and Setters
        public List<Item> getRecentOrders() { return recentOrders; }
        public void setRecentOrders(List<Item> recentOrders) { this.recentOrders = recentOrders; }
        
        public List<Item> getCartItems() { return cartItems; }
        public void setCartItems(List<Item> cartItems) { this.cartItems = cartItems; }
        
        public String getMemberGrade() { return memberGrade; }
        public void setMemberGrade(String memberGrade) { this.memberGrade = memberGrade; }
        
        public List<String> getPreviousQuestions() { return previousQuestions; }
        public void setPreviousQuestions(List<String> previousQuestions) { this.previousQuestions = previousQuestions; }
    }
}
