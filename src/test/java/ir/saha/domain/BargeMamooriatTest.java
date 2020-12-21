package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class BargeMamooriatTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BargeMamooriat.class);
        BargeMamooriat bargeMamooriat1 = new BargeMamooriat();
        bargeMamooriat1.setId(1L);
        BargeMamooriat bargeMamooriat2 = new BargeMamooriat();
        bargeMamooriat2.setId(bargeMamooriat1.getId());
        assertThat(bargeMamooriat1).isEqualTo(bargeMamooriat2);
        bargeMamooriat2.setId(2L);
        assertThat(bargeMamooriat1).isNotEqualTo(bargeMamooriat2);
        bargeMamooriat1.setId(null);
        assertThat(bargeMamooriat1).isNotEqualTo(bargeMamooriat2);
    }
}
