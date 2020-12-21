package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class FileGozareshTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileGozaresh.class);
        FileGozaresh fileGozaresh1 = new FileGozaresh();
        fileGozaresh1.setId(1L);
        FileGozaresh fileGozaresh2 = new FileGozaresh();
        fileGozaresh2.setId(fileGozaresh1.getId());
        assertThat(fileGozaresh1).isEqualTo(fileGozaresh2);
        fileGozaresh2.setId(2L);
        assertThat(fileGozaresh1).isNotEqualTo(fileGozaresh2);
        fileGozaresh1.setId(null);
        assertThat(fileGozaresh1).isNotEqualTo(fileGozaresh2);
    }
}
