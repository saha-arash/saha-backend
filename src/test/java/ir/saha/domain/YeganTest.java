package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class YeganTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Yegan.class);
        Yegan yegan1 = new Yegan();
        yegan1.setId(1L);
        Yegan yegan2 = new Yegan();
        yegan2.setId(yegan1.getId());
        assertThat(yegan1).isEqualTo(yegan2);
        yegan2.setId(2L);
        assertThat(yegan1).isNotEqualTo(yegan2);
        yegan1.setId(null);
        assertThat(yegan1).isNotEqualTo(yegan2);
    }
}
