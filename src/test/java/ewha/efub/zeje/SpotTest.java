package ewha.efub.zeje;

import com.sun.istack.NotNull;
import ewha.efub.zeje.domain.Spot;
import ewha.efub.zeje.domain.SpotRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.Column;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpotTest {
    @Autowired
    SpotRepository spotRepository;

    Long contentId;
    String category;
    String type;
    String name;
    String location;
    String description;
    String link;
    Spot spot;

    @Test
    public void 컬럼_정상입력_테스트() {
        contentId = (long)123;
        category = "여행";
        type = "힐링";
        name = "최고 맛집";
        location = "제주시 어디어디구";

        spot = new Spot(contentId, category, type, name, location);
        assertThat(spotRepository.save(spot));
    }

}
