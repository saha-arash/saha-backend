package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class NirooCodeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NirooCode.class);
        NirooCode nirooCode1 = new NirooCode();
        nirooCode1.setId(1L);
        NirooCode nirooCode2 = new NirooCode();
        nirooCode2.setId(nirooCode1.getId());
        assertThat(nirooCode1).isEqualTo(nirooCode2);
        nirooCode2.setId(2L);
        assertThat(nirooCode1).isNotEqualTo(nirooCode2);
        nirooCode1.setId(null);
        assertThat(nirooCode1).isNotEqualTo(nirooCode2);
    }
}
