package ewha.efub.zeje.controller;

import ewha.efub.zeje.domain.Donation;
import ewha.efub.zeje.dto.DonationResponseDTO;
import ewha.efub.zeje.dto.FruitRequestDTO;
import ewha.efub.zeje.dto.security.SessionUserDTO;
import ewha.efub.zeje.service.DonationService;
import ewha.efub.zeje.service.JwtTokenProvider;
import ewha.efub.zeje.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import static ewha.efub.zeje.dto.DonationResponseDTO.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/donations")
public class DonationController {
    private final DonationService donationService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @PostMapping
    public DonationResponseDTO donationAdd(HttpServletRequest request, @RequestBody FruitRequestDTO fruitRequestDTO) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        Donation donation = donationService.buildDonation(sessionUser.getUserId(), fruitRequestDTO);
        userService.modifyFruitBoxSub(sessionUser.getUserId(), fruitRequestDTO);

        return donationService.addDonation(donation);
    }

    @GetMapping
    public DonationTotalResponseDTO donationDetails(HttpServletRequest request) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        return donationService.findDonations(sessionUser.getUserId());
    }
}
