package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.*;
import ewha.efub.zeje.dto.SpotDTO;
import ewha.efub.zeje.dto.SpotSearchDTO;
import ewha.efub.zeje.dto.SpotUserResponseDTO;
import ewha.efub.zeje.util.errors.CustomException;
import ewha.efub.zeje.util.errors.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotService {
    private final SpotRepository spotRepository;
    private final UserRepository userRepository;
    private final SpotUserRepository spotUserRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public List<SpotSearchDTO> findAllSpots(String category) {
        return spotRepository.findByCategory(category)
                .stream()
                .map(spot -> new SpotSearchDTO(spot))
                .collect(Collectors.toList());
    }

    public List<SpotSearchDTO> findSpotsByKeyword(String category, String keyword) {
        return spotRepository.findByCategoryAndNameContaining(category, keyword)
                .stream()
                .map(spot -> new SpotSearchDTO(spot))
                .collect(Collectors.toList());
    }

    public SpotDTO findSpotDetail(Long spotId) {
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new CustomException(ErrorCode.SPOT_NOT_FOUND));
        SpotDTO spotDTO = modelMapper.map(spot, SpotDTO.class);

        return spotDTO;
    }

    public List<SpotDTO> findTodayFlowerSpot() {
        return spotRepository.findAllByFlowerIsTrue()
                .stream()
                .map(spot -> new SpotDTO(spot))
                .collect(Collectors.toList());
    }

    @Transactional
    public String updateTodayFlowerSpot() {
        spotUserRepository.deleteAll();
        List<Spot> yesterdayFlower = spotRepository.findAllByFlowerIsTrue();
        for(Spot spot : yesterdayFlower) {
            spot.deleteFlowerSpot();
        }

        long count = spotRepository.countByContentIdIsNotAndCategoryEqualsAndMapXIsNotNull(0L, "여행");
        int random = (int) (Math.random() * count / 10);
        int index = random == 0? random : random - 1;

        Page<Spot> spotPage = spotRepository.findAllByContentIdIsNotAndCategoryEqualsAndMapXIsNotNull(0L, "여행", PageRequest.of(index, 10));

        for(Spot spot : spotPage) {
            spot.updateFlowerSpot();
        }

        return "Successful Update";
    }

    public List<SpotUserResponseDTO> findFlowerVisitList(Long userId) {
        List<SpotDTO> spots = findTodayFlowerSpot();
        List<SpotUserResponseDTO> spotUserList = new ArrayList<>();

        for(SpotDTO spot:spots) {
            Boolean todayVisit = findFlowerVisit(userId, spot.getSpotId());
            SpotUserResponseDTO spotUserResponseDTO = SpotUserResponseDTO.builder()
                    .spotId(spot.getSpotId())
                    .name(spot.getName())
                    .location(spot.getLocation())
                    .mapX(spot.getMapX())
                    .mapY(spot.getMapY())
                    .todayVisit(todayVisit)
                    .build();

            spotUserList.add(spotUserResponseDTO);
        }

        return spotUserList;
    }

    public Boolean findFlowerVisit(Long userId, Long spotId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new CustomException(ErrorCode.SPOT_NOT_FOUND));

        if(!spot.getFlower()) {
            throw new CustomException(ErrorCode.NOT_FLOWER_SPOT);
        }

        return spotUserRepository.existsBySpotAndUser(spot, user);
    }

    public String updateFlowerVisit(Long userId, Long spotId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new CustomException(ErrorCode.SPOT_NOT_FOUND));

        if(!spot.getFlower()) {
            throw new CustomException(ErrorCode.NOT_FLOWER_SPOT);
        }

        Boolean flag = findFlowerVisit(userId, spotId);
        if(flag) {
            throw new CustomException(ErrorCode.DUPLICATE_INFORMATION);
        }

        SpotUser spotUser = SpotUser.builder()
                .spot(spot)
                .user(user)
                .build();

        spotUserRepository.save(spotUser);

        return "Successful Today Visit";
    }

    @Value("${api.serviceKey}")
    private String serviceKey;
    public String addSpotApi(String cat1, String cat2, String cat3) {

        String type = selectType(cat1, cat2, cat3);

        String listApiUrl = "http://apis.data.go.kr/B551011/KorService/areaBasedList?numOfRows=400&pageNo=1&MobileOS=ETC&MobileApp=AppTest&ServiceKey="+serviceKey+"&listYN=Y&arrange=A&contentTypeId=12&areaCode=39&sigunguCode=&cat1="+cat1+"&cat2="+cat2+"&cat3="+cat3;

        JSONObject parseItems = readTourApi(listApiUrl);
        JSONArray parseItemList = new JSONArray();
        if(parseItems.get("item") instanceof JSONObject) {
            JSONObject item = parseItems.getJSONObject("item");
            parseItemList.put(item);
        }
        else if (parseItems.get("item") instanceof JSONArray) {
            parseItemList = parseItems.getJSONArray("item");
        }

        int notSaved = 0;
        for(int i=0;i<parseItemList.length();i++) {
            JSONObject item = (JSONObject) parseItemList.get(i);

            Long contentId = Long.parseLong(String.valueOf(item.get("contentid")));
            if(spotRepository.findByContentId(contentId).isPresent()) {
                notSaved++;
                continue;
            }

            String category = cat2.equals("A0203")? "체험" : "여행";
            String name = (String) item.get("title");
            String location = (String) item.get("addr1");

            String detailApiUrl = "http://apis.data.go.kr/B551011/KorService/detailCommon?ServiceKey="+serviceKey+"&contentTypeId=12&contentId="+contentId+"&MobileOS=ETC&MobileApp=AppTest&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y";
            JSONObject parseDetailItems = readTourApi(detailApiUrl);
            JSONObject parseDetailItem = (JSONObject) parseDetailItems.get("item");

            String description = parseDetailItem.has("overview")? (String) parseDetailItem.get("overview") : null;
            String link = parseDetailItem.has("homepage")? (String) parseDetailItem.get("homepage") : null;

            String mapX = parseDetailItem.has("mapx")? String.valueOf(parseDetailItem.get("mapx")) : null;
            String mapY = parseDetailItem.has("mapy")? String.valueOf(parseDetailItem.get("mapy")) : null;

            String image = parseDetailItem.has("firstimage")? String.valueOf(parseDetailItem.get("firstimage")) : null;

            SpotDTO spotDTO = new SpotDTO(contentId, category, type, name, location, description, link, mapX, mapY, image);
            Spot spot = spotDTO.toEntity();
            spotRepository.save(spot);
        }

        Integer count = parseItemList.length() - notSaved;
        String message = count + " Saved";


        return message;
    }


    private JSONObject readTourApi(String apiUrl) {
        StringBuffer result = new StringBuffer();

        JSONObject parseItems = null;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream, "UTF-8"));
            String returnLine;
            while((returnLine = bufferedReader.readLine()) != null) {
                result.append(returnLine);
            }

            JSONObject jsonObject = XML.toJSONObject(result.toString());

            JSONObject parseResponse = (JSONObject) jsonObject.get("response");
            JSONObject parseBody = (JSONObject) parseResponse.get("body");

            parseItems = (JSONObject) parseBody.get("items");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return parseItems;
    }

    private String selectType(String cat1, String cat2, String cat3) {
        StringBuilder typeBuilder = new StringBuilder();
        for(SpotType spotType : SpotType.values()) {
            if(spotType.toString().equals(cat1)) {
                typeBuilder.append(spotType.valueOf(cat1).getName()).append(",");
            }
            if(spotType.toString().equals(cat2)) {
                typeBuilder.append(spotType.valueOf(cat2).getName()).append(",");
            }
            if(spotType.toString().equals(cat3)) {
                typeBuilder.append(spotType.valueOf(cat3).getName()).append(",");
            }
        }

        if(typeBuilder.length()!=0) {
            typeBuilder.deleteCharAt(typeBuilder.length()-1);
        }

        return typeBuilder.toString();
    }

    @Scheduled(cron = "59 59 23 * * *")
    public void runApi() {
        log.info(addSpotApi("A01", "", ""));
        log.info(addSpotApi("A02", "A0202", "A02020700"));
        log.info(addSpotApi("A02", "A0202", "A02020600"));
        log.info(addSpotApi("A02", "A0202", "A02020200"));
        log.info(addSpotApi("A02", "A0203", "A02030400"));
        log.info(addSpotApi("A02", "A0203", "A02030100"));
        log.info(addSpotApi("A02", "A0203", "A02030600"));
    }

}



