package ir.saha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ir.saha.web.rest.TestUtil;

public class FileHesabResiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileHesabResi.class);
        FileHesabResi fileHesabResi1 = new FileHesabResi();
        fileHesabResi1.setId(1L);
        FileHesabResi fileHesabResi2 = new FileHesabResi();
        fileHesabResi2.setId(fileHesabResi1.getId());
        assertThat(fileHesabResi1).isEqualTo(fileHesabResi2);
        fileHesabResi2.setId(2L);
        assertThat(fileHesabResi1).isNotEqualTo(fileHesabResi2);
        fileHesabResi1.setId(null);
        assertThat(fileHesabResi1).isNotEqualTo(fileHesabResi2);
    }
}
