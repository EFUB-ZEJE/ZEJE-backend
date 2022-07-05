package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.Spot;
import ewha.efub.zeje.domain.SpotRepository;
import ewha.efub.zeje.domain.SpotType;
import ewha.efub.zeje.dto.SpotDTO;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<SpotDTO> searchSpots(String category, String keyword) {
        return spotRepository.findByCategoryStartingWithAndNameContaining(category, keyword)
                .stream()
                .map(spot -> modelMapper.map(spot, SpotDTO.class))
                .collect(Collectors.toList());
    }

    @Value("${api.serviceKey}")
    private String serviceKey;

    public Integer addSpotApi(String cat1, String cat2, String cat3) {

        String type = selectType(cat1, cat2, cat3);

        JSONObject jsonObject = changeXmlToJson(cat1, cat2, cat3);

        JSONObject parseResponse = (JSONObject) jsonObject.get("response");
        JSONObject parseBody = (JSONObject) parseResponse.get("body");
        JSONObject parseItems = (JSONObject) parseBody.get("items");
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

            SpotDTO spotDTO = new SpotDTO(contentId, category, type, name, location);
            Spot spot = spotDTO.toEntity();
            spotRepository.save(spot);
        }

        Integer count = parseItemList.length() - notSaved;
        return count;
    }

    private JSONObject changeXmlToJson(String cat1, String cat2, String cat3) {
        StringBuffer result = new StringBuffer();

        JSONObject jsonObject = null;
        try {
            String apiUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=" + serviceKey
                    + "&contentTypeId=12&areaCode=39&sigunguCode=&cat1=" + cat1 + "&cat2=" + cat2 + "&cat3=" + cat3 + "&listYN=Y&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=A&numOfRows=263&pageNo=1";
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream, "UTF-8"));
            String returnLine;
            while ((returnLine = bufferedReader.readLine()) != null) {
                result.append(returnLine);
            }

            jsonObject = XML.toJSONObject(result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
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



