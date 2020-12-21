package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class YeganCodeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(YeganCode.class);
        YeganCode yeganCode1 = new YeganCode();
        yeganCode1.setId(1L);
        YeganCode yeganCode2 = new YeganCode();
        yeganCode2.setId(yeganCode1.getId());
        assertThat(yeganCode1).isEqualTo(yeganCode2);
        yeganCode2.setId(2L);
        assertThat(yeganCode1).isNotEqualTo(yeganCode2);
        yeganCode1.setId(null);
        assertThat(yeganCode1).isNotEqualTo(yeganCode2);
    }
}
