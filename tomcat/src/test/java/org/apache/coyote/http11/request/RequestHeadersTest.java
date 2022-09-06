package org.apache.coyote.http11.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RequestHeadersTest {

    @Test
    void request_header를_파싱한다() {
        // given
        String contentType = "Content-Type: text/html;charset=utf-8";
        String contentLength = "Content-Length: 10";

        // when
        RequestHeaders requestHeaders = RequestHeaders.of(List.of(contentType, contentLength));

        // then
        Assertions.assertAll(
                () -> assertThat(requestHeaders.getHeader("Content-Type")).isEqualTo("text/html;charset=utf-8"),
                () -> assertThat(requestHeaders.getHeader("Content-Length")).isEqualTo("10"),
                () -> assertThat(requestHeaders.getContentLength()).isEqualTo(10)
        );
    }

    @Test
    void content_length가_없으면_0을_반환한다() {
        // given
        String contentType = "Content-Type: text/html;charset=utf-8";

        // when
        RequestHeaders requestHeaders = RequestHeaders.of(List.of(contentType));

        // then
        assertThat(requestHeaders.getContentLength()).isZero();
    }

    @ParameterizedTest
    @MethodSource("provideJSessionId")
    void JSESSIONID가_있는지_확인한다(List<String> inputHeaders, boolean expected) {
        // given

        //when
        RequestHeaders requestHeaders = RequestHeaders.of(inputHeaders);

        // then
        assertThat(requestHeaders.existsJSessionId()).isEqualTo(expected);
    }

    public static Stream<Arguments> provideJSessionId() {
        return Stream.of(
                Arguments.of(List.of("Eden: eden", "Cookie: JSESSIONID=eden"), true),
                Arguments.of(List.of("Eden: eden", "Cookie: No-JSESSIONID=eden"), false),
                Arguments.of(List.of("Eden: eden"), false),
                Arguments.of(List.of("JSESSIONID: eden"), false)
        );
    }
}
