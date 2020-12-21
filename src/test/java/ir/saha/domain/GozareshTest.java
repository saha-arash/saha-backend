package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class GozareshTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gozaresh.class);
        Gozaresh gozaresh1 = new Gozaresh();
        gozaresh1.setId(1L);
        Gozaresh gozaresh2 = new Gozaresh();
        gozaresh2.setId(gozaresh1.getId());
        assertThat(gozaresh1).isEqualTo(gozaresh2);
        gozaresh2.setId(2L);
        assertThat(gozaresh1).isNotEqualTo(gozaresh2);
        gozaresh1.setId(null);
        assertThat(gozaresh1).isNotEqualTo(gozaresh2);
    }
}
