package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class SematTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Semat.class);
        Semat semat1 = new Semat();
        semat1.setId(1L);
        Semat semat2 = new Semat();
        semat2.setId(semat1.getId());
        assertThat(semat1).isEqualTo(semat2);
        semat2.setId(2L);
        assertThat(semat1).isNotEqualTo(semat2);
        semat1.setId(null);
        assertThat(semat1).isNotEqualTo(semat2);
    }
}
