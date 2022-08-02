package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.Spot;
import ewha.efub.zeje.domain.SpotRepository;
import ewha.efub.zeje.domain.SpotType;
import ewha.efub.zeje.dto.SpotDTO;
import ewha.efub.zeje.util.errors.CustomException;
import ewha.efub.zeje.util.errors.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.simple.parser.JSONParser;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class SpotService {
    private final SpotRepository spotRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final ModelMapper modelMapperSearch = new ModelMapper();

    @Bean
    public ModelMapper setSearchMapper() {
        modelMapperSearch.typeMap(Spot.class, SpotDTO.class).addMappings(mapper -> {
            mapper.skip(SpotDTO::setDescription);
            mapper.skip(SpotDTO::setLink);
        });
        return modelMapperSearch;
    }

    public List<SpotDTO> findAllSpots(String category) {
        return spotRepository.findByCategory(category)
                .stream()
                .map(spot -> new SpotDTO(spot))
                .collect(Collectors.toList());
    }

    public List<SpotDTO> findSpotsByKeyword(String category, String keyword) {
        return spotRepository.findByCategoryAndNameContaining(category, keyword)
                .stream()
                .map(spot -> new SpotDTO(spot))
                .collect(Collectors.toList());
    }

    public SpotDTO findSpotDetail(Long spotId) {
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new CustomException(ErrorCode.SPOT_NOT_FOUND));
        SpotDTO spotDTO = modelMapper.map(spot, SpotDTO.class);

        return spotDTO;
    }

    public List<SpotDTO> findFlowerSpot() {
        long count = spotRepository.countByContentIdIsNotAndCategoryEqualsAndMapXIsNotNull(0L, "여행");
        int random = (int) (Math.random() * count / 10);
        int index = random == 0? random : random - 1;

        Page<Spot> spotPage = spotRepository.findAllByContentIdIsNotAndCategoryEqualsAndMapXIsNotNull(0L, "여행", PageRequest.of(index, 10));

        List<Spot> spotList = spotPage.toList();
        return spotList.stream()
                .map(spot -> new SpotDTO(spot))
                .collect(Collectors.toList());
    }

    @Value("${api.serviceKey}")
    private String serviceKey;
    public Integer addSpotApi(String cat1, String cat2, String cat3) {

        String type = selectType(cat1, cat2, cat3);

        String listApiUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=" + serviceKey
                + "&contentTypeId=12&areaCode=39&sigunguCode=&cat1=" + cat1 + "&cat2=" + cat2 + "&cat3=" + cat3 + "&listYN=Y&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=A&numOfRows=300&pageNo=1";

        JSONObject parseItems = readTourApi(listApiUrl);
        JSONArray parseItemList = (JSONArray) parseItems.get("item");

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

            String detailApiUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?ServiceKey="+ serviceKey + "&contentTypeId=12&contentId="+ contentId
                    + "&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&transGuideYN=Y";
            JSONObject parseDetailItems = readTourApi(detailApiUrl);
            JSONObject parseDetailItem = (JSONObject) parseDetailItems.get("item");

            String description = parseDetailItem.has("overview")? (String) parseDetailItem.get("overview") : null;
            String link = parseDetailItem.has("homepage")? (String) parseDetailItem.get("homepage") : null;

            String mapX = parseDetailItem.has("mapx")? String.valueOf(parseDetailItem.get("mapx")) : null;
            String mapY = parseDetailItem.has("mapy")? String.valueOf(parseDetailItem.get("mapy")) : null;

            SpotDTO spotDTO = new SpotDTO(contentId, category, type, name, location, description, link, mapX, mapY);
            Spot spot = spotDTO.toEntity();
            spotRepository.save(spot);
        }

        Integer count = parseItemList.length() - notSaved;
        return count;
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
}



