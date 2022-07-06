package ewha.efub.zeje.controller;

import ewha.efub.zeje.domain.SpotRepository;
import ewha.efub.zeje.dto.SpotDTO;
import ewha.efub.zeje.service.SpotService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping(value="/spots")
public class SpotController {
    private final SpotRepository spotRepository;
    private final SpotService spotService;

    @GetMapping(value="/search/travel/{keyword}")
    public List<SpotDTO> searchTravelSpots(@PathVariable("keyword") String keyword){
        return spotService.searchSpots("여행", keyword);
    }

    @GetMapping(value="/search/experience/{keyword}")
    public List<SpotDTO> searchExperienceSpots(@PathVariable("keyword") String keyword){
        return spotService.searchSpots("체험", keyword);
    }

    @PostMapping("/tourapi")
    public String insertSpotApiData(@RequestBody Map<String, String> body) {
        String cat1 = body.get("cat1");
        String cat2 = body.get("cat2");
        String cat3 = body.get("cat3");

        Integer count = spotService.addSpotApi(cat1, cat2, cat3);
        String message = count + " Saved";

        return message;
    }


}

