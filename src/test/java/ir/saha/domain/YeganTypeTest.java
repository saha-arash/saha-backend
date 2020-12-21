package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class YeganTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(YeganType.class);
        YeganType yeganType1 = new YeganType();
        yeganType1.setId(1L);
        YeganType yeganType2 = new YeganType();
        yeganType2.setId(yeganType1.getId());
        assertThat(yeganType1).isEqualTo(yeganType2);
        yeganType2.setId(2L);
        assertThat(yeganType1).isNotEqualTo(yeganType2);
        yeganType1.setId(null);
        assertThat(yeganType1).isNotEqualTo(yeganType2);
    }
}
