package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.SpotRepository;
import ewha.efub.zeje.dto.SpotDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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

    public JSONObject callApiWithJson(String cat1, String cat2, String cat3) {
        StringBuffer result = new StringBuffer();
        //String jsonPrintString = null;
        JSONObject jsonObject = null;
        try {
            String apiUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey="+serviceKey
                    + "&contentTypeId=12&areaCode=39&sigunguCode=&cat1="+cat1+"&cat2="+cat2+"&cat3="+cat3+"&listYN=Y&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=A&numOfRows=263&pageNo=1";
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream, "UTF-8"));
            String returnLine;
            while((returnLine = bufferedReader.readLine()) != null) {
                result.append(returnLine);
            }

            jsonObject = XML.toJSONObject(result.toString());
            //jsonPrintString = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
