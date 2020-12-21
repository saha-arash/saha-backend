package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class FileBargeMamooriatTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileBargeMamooriat.class);
        FileBargeMamooriat fileBargeMamooriat1 = new FileBargeMamooriat();
        fileBargeMamooriat1.setId(1L);
        FileBargeMamooriat fileBargeMamooriat2 = new FileBargeMamooriat();
        fileBargeMamooriat2.setId(fileBargeMamooriat1.getId());
        assertThat(fileBargeMamooriat1).isEqualTo(fileBargeMamooriat2);
        fileBargeMamooriat2.setId(2L);
        assertThat(fileBargeMamooriat1).isNotEqualTo(fileBargeMamooriat2);
        fileBargeMamooriat1.setId(null);
        assertThat(fileBargeMamooriat1).isNotEqualTo(fileBargeMamooriat2);
    }
}
