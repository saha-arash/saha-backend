package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class KarbarTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Karbar.class);
        Karbar karbar1 = new Karbar();
        karbar1.setId(1L);
        Karbar karbar2 = new Karbar();
        karbar2.setId(karbar1.getId());
        assertThat(karbar1).isEqualTo(karbar2);
        karbar2.setId(2L);
        assertThat(karbar1).isNotEqualTo(karbar2);
        karbar1.setId(null);
        assertThat(karbar1).isNotEqualTo(karbar2);
    }
}
