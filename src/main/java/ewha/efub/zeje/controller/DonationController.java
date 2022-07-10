package ewha.efub.zeje.controller;

import ewha.efub.zeje.domain.Donation;
import ewha.efub.zeje.dto.DonationResponseDTO;
import ewha.efub.zeje.dto.FruitRequestDTO;
import ewha.efub.zeje.service.DonationService;
import ewha.efub.zeje.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

import static ewha.efub.zeje.dto.DonationResponseDTO.*;

@RestController
@RequiredArgsConstructor
public class DonationController {
    private final DonationService donationService;
    private final UserService userService;

    @Transactional
    @PostMapping("/donations")
    public DonationResponseDTO donationAdd(@RequestBody FruitRequestDTO fruitRequestDTO) {
        Long userId = userService.findSessionUser();
        Donation donation = donationService.buildDonation(userId, fruitRequestDTO);
        userService.modifyFruitBoxSub(userId, fruitRequestDTO);

        return donationService.addDonation(donation);
    }

    @GetMapping("/donations")
    public DonationTotalResponseDTO donationDetails() {
        Long userId = userService.findSessionUser();
        return donationService.findDonations(userId);
    }
}
