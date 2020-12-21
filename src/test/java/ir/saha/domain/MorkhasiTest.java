package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class MorkhasiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Morkhasi.class);
        Morkhasi morkhasi1 = new Morkhasi();
        morkhasi1.setId(1L);
        Morkhasi morkhasi2 = new Morkhasi();
        morkhasi2.setId(morkhasi1.getId());
        assertThat(morkhasi1).isEqualTo(morkhasi2);
        morkhasi2.setId(2L);
        assertThat(morkhasi1).isNotEqualTo(morkhasi2);
        morkhasi1.setId(null);
        assertThat(morkhasi1).isNotEqualTo(morkhasi2);
    }
}
