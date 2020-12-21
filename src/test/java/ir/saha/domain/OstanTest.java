package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class OstanTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ostan.class);
        Ostan ostan1 = new Ostan();
        ostan1.setId(1L);
        Ostan ostan2 = new Ostan();
        ostan2.setId(ostan1.getId());
        assertThat(ostan1).isEqualTo(ostan2);
        ostan2.setId(2L);
        assertThat(ostan1).isNotEqualTo(ostan2);
        ostan1.setId(null);
        assertThat(ostan1).isNotEqualTo(ostan2);
    }
}
