package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class NegahbaniTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Negahbani.class);
        Negahbani negahbani1 = new Negahbani();
        negahbani1.setId(1L);
        Negahbani negahbani2 = new Negahbani();
        negahbani2.setId(negahbani1.getId());
        assertThat(negahbani1).isEqualTo(negahbani2);
        negahbani2.setId(2L);
        assertThat(negahbani1).isNotEqualTo(negahbani2);
        negahbani1.setId(null);
        assertThat(negahbani1).isNotEqualTo(negahbani2);
    }
}
