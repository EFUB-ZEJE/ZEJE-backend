package ewha.efub.zeje.controller;

import ewha.efub.zeje.domain.SpotRepository;
import ewha.efub.zeje.dto.SpotDTO;
import ewha.efub.zeje.service.SpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping(value="/spots")
public class SpotController {
    private final SpotRepository spotRepository;
    private final SpotService spotService;

    @GetMapping(value="/search/travel")
    public List<SpotDTO> spotTravelList(){
        return spotService.findAllSpots("여행");
    }

    @GetMapping(value="/search/experience")
    public List<SpotDTO> spotExperienceList(){
        return spotService.findAllSpots("체험");
    }

    @GetMapping(value="/search/travel/{keyword}")
    public List<SpotDTO> spotSearchTravelList(@PathVariable("keyword") String keyword){
        return spotService.findSpotsByKeyword("여행", keyword);
    }

    @GetMapping(value="/search/experience/{keyword}")
    public List<SpotDTO> spotSearchExperienceList(@PathVariable("keyword") String keyword){
        return spotService.findSpotsByKeyword("체험", keyword);
    }

    @GetMapping(value="/details/{spotId}")
    public SpotDTO spotDetails(@PathVariable("spotId") Long spotId){
        return spotService.findSpotDetail(spotId);
    }

    @PostMapping("/tourapi")
    public String spotApiData(@RequestBody Map<String, String> body) {
        String cat1 = body.get("cat1");
        String cat2 = body.get("cat2");
        String cat3 = body.get("cat3");

        Integer count = spotService.addSpotApi(cat1, cat2, cat3);
        String message = count + " Saved";

        return message;
    }

    @GetMapping("/flowers")
    public List<SpotDTO> spotFlowerList() {
        return spotService.findFlowerSpot();
    }
}

