package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.Donation;
import ewha.efub.zeje.domain.DonationRepository;
import ewha.efub.zeje.domain.User;
import ewha.efub.zeje.domain.UserRepository;
import ewha.efub.zeje.dto.DonationRequestDTO;
import ewha.efub.zeje.dto.DonationResponseDTO;
import ewha.efub.zeje.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static ewha.efub.zeje.dto.DonationResponseDTO.*;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;
    private final UserRepository userRepository;

    @Transactional
    public DonationResponseDTO addDonation(Long userId, DonationRequestDTO donationRequestDTO) {
        User user = userRepository.findByUserIdAndDeleteFlagFalse(userId);
        Donation donation = Donation.builder()
                                    .fruit(donationRequestDTO.getFruit())
                                    .user(user)
                                    .build();

        donationRepository.save(donation);
        return new DonationResponseDTO(donation);
    }

    @Transactional
    public DonationTotalResponseDTO findDonations(Long userId) {
        User user = userRepository.findByUserIdAndDeleteFlagFalse(userId);

    }
}
