package com.ltchen.demo.spring.boot.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author: 01139983
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Greeting {

    @NonNull
    private Long id;

    @NonNull
    private String content;
}
