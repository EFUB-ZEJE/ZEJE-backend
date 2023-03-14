package ewha.efub.zeje.controller;

import ewha.efub.zeje.config.BatchScheduler;
import ewha.efub.zeje.config.annotation.LogExecutionTime;
import ewha.efub.zeje.domain.SpotRepository;
import ewha.efub.zeje.dto.SpotDTO;
import ewha.efub.zeje.dto.SpotSearchDTO;
import ewha.efub.zeje.dto.SpotUserResponseDTO;
import ewha.efub.zeje.dto.security.SessionUserDTO;
import ewha.efub.zeje.service.SpotService;
import lombok.RequiredArgsConstructor;
import ewha.efub.zeje.service.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/spots")
public class SpotController {
    private final SpotService spotService;
    private final JwtTokenProvider jwtTokenProvider;
    private final BatchScheduler batchScheduler;

    @GetMapping(value="/search/travel/{order}")
    public List<SpotSearchDTO> spotTravelList(HttpServletRequest request, @PathVariable("order") String order){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        return spotService.findAllSpots(sessionUser.getUserId(), "여행", order);
    }

    @GetMapping(value="/search/experience/{order}")
    public List<SpotSearchDTO> spotExperienceList(HttpServletRequest request, @PathVariable("order") String order){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        return spotService.findAllSpots(sessionUser.getUserId(), "체험", order);
    }

    @GetMapping(value="/search/travel/{order}/{keyword}")
    public List<SpotSearchDTO> spotSearchTravelList(HttpServletRequest request, @PathVariable("order") String order, @PathVariable("keyword") String keyword){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        return spotService.findSpotsByKeyword(sessionUser.getUserId(), "여행", order, keyword);
    }

    @GetMapping(value="/search/experience/{order}/{keyword}")
    public List<SpotSearchDTO> spotSearchExperienceList(HttpServletRequest request, @PathVariable("order") String order, @PathVariable("keyword") String keyword){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        return spotService.findSpotsByKeyword(sessionUser.getUserId(), "체험", order, keyword);
    }

    @GetMapping(value="/details/{spotId}")
    public SpotDTO spotDetails(@PathVariable("spotId") Long spotId){
        return spotService.findSpotDetail(spotId);
    }

    @Scheduled(cron = "59 59 23 * * *")
    @GetMapping("/newFlowers")
    public void spotNewFlowerList() {
        log.info(spotService.updateTodayFlowerSpot());
    }

    @GetMapping("/todayFlowers")
    public List<SpotDTO> spotTodayFlowerList() {
        return spotService.findTodayFlowerSpot();
    }

    @GetMapping("/flowers/today-visit")
    public Boolean spotFlowerVisited(HttpServletRequest request, @RequestParam("spot") Long spotId) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        return spotService.findFlowerVisit(sessionUser.getUserId(), spotId);
    }

    @GetMapping("/flowers/today-visit/lists")
    public List<SpotUserResponseDTO> spotFlowerVisitedList(HttpServletRequest request) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        return spotService.findFlowerVisitList(sessionUser.getUserId());
    }

    @PostMapping("/flowers")
    public String spotFlowerVisitUpdate(HttpServletRequest request, @RequestBody Map<String, Long> body) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        Long spotId = body.get("spotId");
        return spotService.updateFlowerVisit(sessionUser.getUserId(), spotId);
    }

    @GetMapping("/testTime")
    @LogExecutionTime
    public void spotInformationUpdateTest() {
        //spotService.runApi();
        batchScheduler.runJob();
    }

}

