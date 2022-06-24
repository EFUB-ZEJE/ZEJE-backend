package ewha.efub.zeje.controller;

import ewha.efub.zeje.dto.SpotDTO;
import ewha.efub.zeje.service.SpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/spots")
public class SpotController {

    private final SpotService spotService;

    @GetMapping(value="/search/travel/{keyword}")
    public List<SpotDTO> searchTravelSpots(@PathVariable("keyword") String keyword){
        return spotService.searchSpots("여행", keyword);
    }

    @GetMapping(value="/search/experience/{keyword}")
    public List<SpotDTO> searchExperienceSpots(@PathVariable("keyword") String keyword){
        return spotService.searchSpots("체험", keyword);
    }
}
