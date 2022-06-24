package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.SpotRepository;
import ewha.efub.zeje.dto.SpotDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpotService {
    private final SpotRepository spotRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public List<SpotDTO> searchSpots(String category, String keyword){
        return spotRepository.findByCategoryStartingWithAndNameContaining(category, keyword)
                .stream()
                .map(spot -> modelMapper.map(spot, SpotDTO.class))
                .collect(Collectors.toList());
    }
}
