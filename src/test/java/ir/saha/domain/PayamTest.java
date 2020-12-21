package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class PayamTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payam.class);
        Payam payam1 = new Payam();
        payam1.setId(1L);
        Payam payam2 = new Payam();
        payam2.setId(payam1.getId());
        assertThat(payam1).isEqualTo(payam2);
        payam2.setId(2L);
        assertThat(payam1).isNotEqualTo(payam2);
        payam1.setId(null);
        assertThat(payam1).isNotEqualTo(payam2);
    }
}
