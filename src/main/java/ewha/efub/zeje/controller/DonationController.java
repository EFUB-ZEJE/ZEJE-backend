package ewha.efub.zeje.controller;

import ewha.efub.zeje.domain.Donation;
import ewha.efub.zeje.dto.DonationRequestDTO;
import ewha.efub.zeje.dto.DonationResponseDTO;
import ewha.efub.zeje.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static ewha.efub.zeje.dto.DonationResponseDTO.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/donations")
public class DonationController {
    private final DonationService donationService;

    @PostMapping("/{userId}")
    public DonationResponseDTO donationAdd(@PathVariable Long userId, @RequestBody DonationRequestDTO donationRequestDTO) {
        Donation donation = donationService.buildDonation(userId, donationRequestDTO);
        return donationService.addDonation(donation);
    }

    @GetMapping("/{userId}")
    public DonationTotalResponseDTO donationDetails(@PathVariable long userId) {
        return donationService.findDonations(userId);
    }
}
