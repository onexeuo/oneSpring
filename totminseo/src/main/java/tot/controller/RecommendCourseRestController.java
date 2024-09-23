package tot.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tot.domain.CourseDTO;
import tot.domain.GPTMessage;
import tot.domain.Hotel;
import tot.domain.RecommandCourseInputDTO;
import tot.domain.Restaurant;
import tot.domain.Tour;
import tot.domain.Trip;
import tot.service.PlannerService;

import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping(value = "/recommendCourse", produces = "application/json; charset=UTF-8")
public class RecommendCourseRestController {

    @Autowired
    private PlannerService plannerService;
    
    @Autowired
    private HttpSession session;

    ObjectMapper mapper = new ObjectMapper();
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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"; 

    private final HttpClient client = HttpClient.newHttpClient();
    
    private static final Map<String, String> AREA_CODE_MAP = new HashMap<>();
    static {
        AREA_CODE_MAP.put("1", "서울");
        AREA_CODE_MAP.put("2", "인천");
        AREA_CODE_MAP.put("3", "대전");
        AREA_CODE_MAP.put("4", "대구");
        AREA_CODE_MAP.put("5", "광주");
        AREA_CODE_MAP.put("6", "부산");
        AREA_CODE_MAP.put("7", "울산");
        AREA_CODE_MAP.put("8", "세종특별자치시");
        AREA_CODE_MAP.put("31", "경기도");
        AREA_CODE_MAP.put("32", "강원특별자치도");
        AREA_CODE_MAP.put("33", "충청북도");
        AREA_CODE_MAP.put("34", "충청남도");
        AREA_CODE_MAP.put("35", "경상북도");
        AREA_CODE_MAP.put("36", "경상남도");
        AREA_CODE_MAP.put("37", "전북특별자치도");
        AREA_CODE_MAP.put("38", "전라남도");
        AREA_CODE_MAP.put("39", "제주도");
    }
    
    private static final Map<String, String> GET_RESTAURANT_001 = new HashMap<>();
    static {
    	GET_RESTAURANT_001.put("K", "한식");
    	GET_RESTAURANT_001.put("J", "일식");
    	GET_RESTAURANT_001.put("C", "중국식");
    	GET_RESTAURANT_001.put("W", "양식");
    }
    
    private static final Map<String, String> HOTEL_TYPE_MAP = new HashMap<>();
    static {
        HOTEL_TYPE_MAP.put("MOT", "모텔");
        HOTEL_TYPE_MAP.put("RES", "호텔·리조트");
        HOTEL_TYPE_MAP.put("PEN", "펜션");
        HOTEL_TYPE_MAP.put("HOM", "홈&빌라");
        HOTEL_TYPE_MAP.put("CAM", "캠핑·글램핑");
        HOTEL_TYPE_MAP.put("GUE", "게하&한옥");
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> recommendCourse(@RequestBody RecommandCourseInputDTO requestData) {
        try {
            // 받은 데이터 출력
            String json = mapper.writeValueAsString(requestData);
            System.out.println("받은 데이터: " + json);

            // 데이터 출력
            printRequestData(requestData);

            // 데이터 가져오기
            List<Hotel> hotels = plannerService.selectHotel(requestData.getAreacode());
            List<Restaurant> restaurants = plannerService.selectRestaurant(requestData.getAreacode());
            List<Tour> tours = plannerService.selectTour(requestData.getAreacode());

            // 데이터 묶기
            Map<String, Object> allData = new HashMap<>();
            allData.put("hotels", hotels);
            allData.put("restaurants", restaurants);
            allData.put("tours", tours);

            // JSON으로 변환
            String allDataJson = mapper.writeValueAsString(allData);

            // 외부 API로 데이터 전송
            sendToExternalApi(allDataJson, requestData, hotels); // 호텔 리스트 추가 전달
            session.setAttribute("recommendationData", allData);

            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Map.of("error", false));
        }
    } // ResponseEntity

    private void sendToExternalApi(String jsonData, RecommandCourseInputDTO requestData, List<Hotel> hotels) throws Exception {
        String restaurant_001 = requestData.getRestaurant_001();
        String restaurantName = GET_RESTAURANT_001.getOrDefault(restaurant_001, "null일 경우 이름을 보고 대충 유형에 맞게 추천");

        String areaCode = requestData.getAreacode();
        String areaName = AREA_CODE_MAP.getOrDefault(areaCode, "대한민국 국내");

        // 호텔 유형 정보 추가
        Map<String, String> hotelTypes = new HashMap<>();
        for (Hotel hotel : hotels) {
            String hotelTypeCode = hotel.getLg_001();
            String hotelType = HOTEL_TYPE_MAP.getOrDefault(hotelTypeCode, "null일 경우 이름을 보고 대충 유형에 맞게 추천");
            hotelTypes.put(hotel.getLg_001(), hotelType);
        }

        
        LocalDate startDate = LocalDate.parse(requestData.getTrstadate());
        LocalDate endDate = LocalDate.parse(requestData.getTrenddate());
        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");

        StringBuilder itineraryMessage = new StringBuilder();
        itineraryMessage.append("아래는 여행 추천을 위해 제공된 사용자의 데이터입니다:\n")
            .append("MBTI: ").append(requestData.getMbti()).append("\n")
            .append("여행 예산: ").append(requestData.getTramt()).append("\n")
            .append("여행 인원 수: ").append(requestData.getTrpeople()).append("\n")
            .append("여행 시작일: ").append(requestData.getTrstadate()).append("\n")
            .append("여행 종료일: ").append(requestData.getTrenddate()).append("\n")
            .append("여행 기간: ").append(requestData.getTrperiod()).append("\n")
            .append("원하는 지역: ").append(areaName).append("\n")
            .append("원하는 식당 종류: ").append(restaurantName).append("\n")
            .append("호텔 유형: ").append(hotelTypes).append("\n")
            .append("결과 유형: ").append(requestData.getResultType()).append("\n\n")
            .append("데이터: ").append(jsonData).append("\n\n");

        for (long i = 1; i <= numberOfDays; i++) {
            itineraryMessage.append(i).append("일차 추천 코스:\n");

            itineraryMessage.append("아침\n")
                            .append("식당 : [아침 식당 정보 입력]\n")  // 아침 식당 정보 추가
                            .append("관광지 : [아침 관광지 정보 입력]\n"); // 아침 관광지 정보 추가

            if (i < numberOfDays) { // 마지막 날이 아닌 경우
                itineraryMessage.append("점심\n")
                                .append("식당 : [점심 식당 정보 입력]\n")  // 점심 식당 정보 추가
                                .append("관광지 : [점심 관광지 정보 입력]\n"); // 점심 관광지 정보 추가
                itineraryMessage.append("저녁\n")
                                .append("식당 : [저녁 식당 정보 입력]\n")  // 저녁 식당 정보 추가
                                .append("호텔 : [저녁 호텔 정보 입력]\n\n");  // 저녁 호텔 정보 추가
            } 
        }

        requestBody.put("messages", new GPTMessage[]{
        	    new GPTMessage("system", "다음 데이터를 기반으로 여행 추천 코스를 상세히 작성해 주세요. 식사, 관광지, 호텔 각 항목의 예상 비용과 각 추천 항목의 고유 ID를 각각 '식당ID', '관광지ID', '호텔ID'로 구분해 주세요. 제공한 데이터 안에서 작성해 주세요. 그리고 호텔만 링크 보내주세요. 그리고 비용들은 전부 예상 비용으로 보내주세요. 호텔의 이미지도 꼭 넣어주세요. 식당과 관광지의 주소도 꼭 넣어주세요. 그리고 결과는 반드시 JSON 형식으로 출력해 주세요.그리고 제공된 여행 일자만큼 꼭 뽑아주세요."),
        	    new GPTMessage("user", itineraryMessage.toString())
        	});

        String requestBodyJson = mapper.writeValueAsString(requestBody);

        // API 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(API_URL))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + API_KEY)
            .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
            .build();

        // API 요청 실행 및 응답 처리
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response Status Code: " + response.statusCode());

        // 응답 본문 처리
        String responseBody = response.body();
        System.out.println("Response Body: " + responseBody);

        try {
            // 응답 본문에서 content 직접 추출
            String content = responseBody;
            
            // JSON 형식의 응답을 확인하여 파싱
            Map<String, Object> jsonResponse = mapper.readValue(content, Map.class);
            System.out.println("Parsed JSON response: " + jsonResponse);
            session.setAttribute("recommendationContent", jsonResponse);
        } catch (Exception e) {
            // JSON 파싱 실패 시 텍스트 응답으로 처리
            System.out.println("Response is not valid JSON. Raw content: " + responseBody);
            session.setAttribute("recommendationContent", responseBody);
        }
    } // sendToExternalApi
    
    @Transactional
    @PostMapping("/create")
    public ResponseEntity<String> createTrip(@RequestBody Trip trip) {
        try {
            // 트립 저장
            plannerService.insertTrip(trip);

            // 저장된 tripid 가져오기
            Integer tripId = plannerService.selectLatestTripId();
            if (tripId == null) {
                throw new Exception("Trip ID 생성 실패");
            }

            System.out.println(tripId);

            // 각 코스에 tripid 설정
            for (CourseDTO courseDTO : trip.getCourses()) {
                courseDTO.setTripid(tripId);
                plannerService.insertCourse(courseDTO);
            }

            return ResponseEntity.ok("저장 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장 실패");
        }
    }
    
    @Transactional
    @PatchMapping("/updateMbti")
    public ResponseEntity<String> updateMemberMbti(@RequestParam String memId, @RequestParam String newMbti) {
        try {
            // 서비스 호출하여 MBTI 업데이트
        	plannerService.updateMemberMbt(memId, newMbti);
            return ResponseEntity.ok("MBTI 업데이트 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("잘못된 요청: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("MBTI 업데이트 중 오류 발생");
        }
    }

	private void printRequestData(RecommandCourseInputDTO requestData) {
        System.out.println("MBTI: " + requestData.getMbti());
        System.out.println("tramt: " + requestData.getTramt());
        System.out.println("trpeople: " + requestData.getTrpeople());
        System.out.println("trstadate: " + requestData.getTrstadate());
        System.out.println("trenddate: " + requestData.getTrenddate());
        System.out.println("trperiod: " + requestData.getTrperiod());
        System.out.println("areacode: " + requestData.getAreacode());
        System.out.println("restaurant_001: " + requestData.getRestaurant_001());
        System.out.println("Result Type: " + requestData.getResultType());
    }
}


