package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class FileNameTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileName.class);
        FileName fileName1 = new FileName();
        fileName1.setId(1L);
        FileName fileName2 = new FileName();
        fileName2.setId(fileName1.getId());
        assertThat(fileName1).isEqualTo(fileName2);
        fileName2.setId(2L);
        assertThat(fileName1).isNotEqualTo(fileName2);
        fileName1.setId(null);
        assertThat(fileName1).isNotEqualTo(fileName2);
    }
}
