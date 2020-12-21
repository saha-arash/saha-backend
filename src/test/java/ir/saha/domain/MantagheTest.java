package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class MantagheTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mantaghe.class);
        Mantaghe mantaghe1 = new Mantaghe();
        mantaghe1.setId(1L);
        Mantaghe mantaghe2 = new Mantaghe();
        mantaghe2.setId(mantaghe1.getId());
        assertThat(mantaghe1).isEqualTo(mantaghe2);
        mantaghe2.setId(2L);
        assertThat(mantaghe1).isNotEqualTo(mantaghe2);
        mantaghe1.setId(null);
        assertThat(mantaghe1).isNotEqualTo(mantaghe2);
    }
}
