package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class HesabResiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HesabResi.class);
        HesabResi hesabResi1 = new HesabResi();
        hesabResi1.setId(1L);
        HesabResi hesabResi2 = new HesabResi();
        hesabResi2.setId(hesabResi1.getId());
        assertThat(hesabResi1).isEqualTo(hesabResi2);
        hesabResi2.setId(2L);
        assertThat(hesabResi1).isNotEqualTo(hesabResi2);
        hesabResi1.setId(null);
        assertThat(hesabResi1).isNotEqualTo(hesabResi2);
    }
}
