package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class ShahrTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shahr.class);
        Shahr shahr1 = new Shahr();
        shahr1.setId(1L);
        Shahr shahr2 = new Shahr();
        shahr2.setId(shahr1.getId());
        assertThat(shahr1).isEqualTo(shahr2);
        shahr2.setId(2L);
        assertThat(shahr1).isNotEqualTo(shahr2);
        shahr1.setId(null);
        assertThat(shahr1).isNotEqualTo(shahr2);
    }
}
