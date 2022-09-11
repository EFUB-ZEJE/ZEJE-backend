package ewha.efub.zeje.controller;

import ewha.efub.zeje.dto.SpotReportRequestDTO;
import ewha.efub.zeje.service.SpotReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spotReports")
public class SpotReportController {
    private final SpotReportService spotReportService;

    @PostMapping
    public String spotReportSave(@RequestParam(value = "name") String name,
                                 @RequestParam(value = "type") String type,
                                 @RequestParam(value = "image", required = false) MultipartFile image,
                                 @RequestParam(value = "description", required = false) String description) throws IOException {
        SpotReportRequestDTO spotReportRequestDTO = SpotReportRequestDTO.builder()
                .name(name)
                .type(type)
                .description(description)
                .build();

        return spotReportService.addSpotReport(spotReportRequestDTO, image);
    }
}
