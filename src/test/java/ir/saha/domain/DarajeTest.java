package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class DarajeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Daraje.class);
        Daraje daraje1 = new Daraje();
        daraje1.setId(1L);
        Daraje daraje2 = new Daraje();
        daraje2.setId(daraje1.getId());
        assertThat(daraje1).isEqualTo(daraje2);
        daraje2.setId(2L);
        assertThat(daraje1).isNotEqualTo(daraje2);
        daraje1.setId(null);
        assertThat(daraje1).isNotEqualTo(daraje2);
    }
}
