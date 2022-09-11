package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.SpotReport;
import ewha.efub.zeje.domain.SpotReportRepository;
import ewha.efub.zeje.dto.ReviewResponseDTO;
import ewha.efub.zeje.dto.SpotReportRequestDTO;
import ewha.efub.zeje.dto.SpotReportResponseDTO;
import ewha.efub.zeje.util.errors.CustomException;
import ewha.efub.zeje.util.errors.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpotReportService {
    private final SpotReportRepository spotReportRepository;
    private final ImageUploadService imageUploadService;

    public String addSpotReport(SpotReportRequestDTO spotReportRequestDTO, MultipartFile imageFile) throws IOException {
        if(imageFile != null) {
            String fileUrl = imageUploadService.uploadImage(2, imageFile);
            if(fileUrl.equals("type error") || fileUrl.equals("null error")) {
                throw new CustomException(ErrorCode.INVALID_IMAGE_FILE);
            }

            return saveSpotReport(spotReportRequestDTO, fileUrl);
        }
        else {
            return saveSpotReport(spotReportRequestDTO);
        }

    }

    public String saveSpotReport(SpotReportRequestDTO spotReportRequestDTO, String fileUrl) {
        SpotReport spotReport = SpotReport.builder()
                .name(spotReportRequestDTO.getName())
                .type(spotReportRequestDTO.getType())
                .image(fileUrl)
                .description(spotReportRequestDTO.getDescription())
                .mapX(spotReportRequestDTO.getMapX())
                .mapY(spotReportRequestDTO.getMapY())
                .location(spotReportRequestDTO.getLocation())
                .build();

        spotReportRepository.save(spotReport);

        return "SAVE WITH THE IMAGE";
    }

    public String saveSpotReport(SpotReportRequestDTO spotReportRequestDTO) {
        SpotReport spotReport = SpotReport.builder()
                .name(spotReportRequestDTO.getName())
                .type(spotReportRequestDTO.getType())
                .description(spotReportRequestDTO.getDescription())
                .mapX(spotReportRequestDTO.getMapX())
                .mapY(spotReportRequestDTO.getMapY())
                .location(spotReportRequestDTO.getLocation())
                .build();

        spotReportRepository.save(spotReport);

        return "SAVE WITHOUT THE IMAGE";
    }

    public List<SpotReportResponseDTO> findAllSpotReports() {
        return spotReportRepository.findAll()
                .stream()
                .map(spotReport -> new SpotReportResponseDTO(spotReport))
                .collect(Collectors.toList());
    }
}
