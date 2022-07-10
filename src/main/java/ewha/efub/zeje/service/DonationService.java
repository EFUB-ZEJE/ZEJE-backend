package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.Donation;
import ewha.efub.zeje.domain.DonationRepository;
import ewha.efub.zeje.domain.User;
import ewha.efub.zeje.domain.UserRepository;
import ewha.efub.zeje.dto.DonationResponseDTO;
import ewha.efub.zeje.dto.FruitRequestDTO;
import ewha.efub.zeje.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

import static ewha.efub.zeje.dto.DonationResponseDTO.*;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;
    private final UserRepository userRepository;

    @Transactional
    public DonationResponseDTO addDonation(Donation donation) {
        donationRepository.save(donation);
        return new DonationResponseDTO(donation);
    }

    @Transactional
    public DonationTotalResponseDTO findDonations(Long userId) {
        Integer fruitTotal = 0;
        List<Donation> donations = donationRepository.findDonationsByUser_UserId(userId);
        for(Donation donation : donations) {
            fruitTotal += donation.getFruit();
        }
        return new DonationTotalResponseDTO(userId, fruitTotal);
    }

    public Donation buildDonation(Long userId, FruitRequestDTO fruitRequestDTO) {
        User user = userRepository.findByUserIdAndDeleteFlagFalse(userId);

        Donation donation = Donation.builder()
                .fruit(fruitRequestDTO.getFruitBox())
                .user(user)
                .build();

        return donation;
    }
}
