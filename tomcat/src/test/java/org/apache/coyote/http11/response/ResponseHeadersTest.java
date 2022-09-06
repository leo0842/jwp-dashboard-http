package org.apache.coyote.http11.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.apache.coyote.http11.request.RequestHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ResponseHeadersTest {

    private RequestHeaders requestHeaders;

    @BeforeEach
    void setUp() {
        requestHeaders = RequestHeaders.of(List.of("eden: king"));
    }

    @ParameterizedTest
    @MethodSource("responseEntityAndContentType")
    void body에_맞게_content_type을_반환한다(String body, String expected) {
        // given
        ResponseEntity responseEntity = ResponseEntity.body(body);

        // when
        ResponseHeaders responseHeaders = ResponseHeaders.of(requestHeaders, responseEntity);

        // then
        assertThat(responseHeaders.asString()).contains(expected);
    }

    public static Stream<Arguments> responseEntityAndContentType() {
        return Stream.of(
                Arguments.of("index.html", "Content-Type: text/" + "html" + ";charset=utf-8 "),
                Arguments.of("styles.css", "Content-Type: text/" + "css" + ";charset=utf-8 "),
                Arguments.of("styles.js", "Content-Type: text/" + "javascript" + ";charset=utf-8 "),
                Arguments.of("Hello world!", "Content-Type: text/" + "html" + ";charset=utf-8 ")
        );
    }

    @Test
    void redirect면_Location_헤더를_가진다() {
        // given
        String body = "redirect:index.html";
        ResponseEntity responseEntity = ResponseEntity.body(body).status(HttpStatus.REDIRECT);

        // when
        ResponseHeaders responseHeaders = ResponseHeaders.of(requestHeaders, responseEntity);

        // then
        assertThat(responseHeaders.asString()).contains("Location: index.html ");
    }

    @Test
    void content_length를_추가한다() {
        // given
        String body = "Hello world!";
        ResponseEntity responseEntity = ResponseEntity.body(body);
        ResponseHeaders responseHeaders = ResponseHeaders.of(requestHeaders, responseEntity);

        // when
        responseHeaders.setContentLength(body.getBytes().length);
        String expected = "Content-Type: text/html;charset=utf-8 \r\n"
                + "Content-Length: " + body.getBytes().length + " ";
        // then
        assertThat(responseHeaders.asString()).contains(expected);
    }

}
