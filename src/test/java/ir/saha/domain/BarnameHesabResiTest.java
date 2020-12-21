package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class BarnameHesabResiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BarnameHesabResi.class);
        BarnameHesabResi barnameHesabResi1 = new BarnameHesabResi();
        barnameHesabResi1.setId(1L);
        BarnameHesabResi barnameHesabResi2 = new BarnameHesabResi();
        barnameHesabResi2.setId(barnameHesabResi1.getId());
        assertThat(barnameHesabResi1).isEqualTo(barnameHesabResi2);
        barnameHesabResi2.setId(2L);
        assertThat(barnameHesabResi1).isNotEqualTo(barnameHesabResi2);
        barnameHesabResi1.setId(null);
        assertThat(barnameHesabResi1).isNotEqualTo(barnameHesabResi2);
    }
}
