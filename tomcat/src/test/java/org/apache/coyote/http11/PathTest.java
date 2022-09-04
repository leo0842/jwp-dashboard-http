package org.apache.coyote.http11;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PathTest {

    @Test
    void 파일_요청인지_확인한다() {
        // given
        String uri = "/index.html";
        Path path = Path.of(uri);

        // when & then
        assertThat(path.isFileRequest()).isTrue();
    }

    @Test
    void 파일명을_반환한다() {
        // given
        String uri = "/index.html";
        Path path = Path.of(uri);

        // when & then
        assertThat(path.getFileName()).isEqualTo("index.html");
    }
}
