package org.highfives.grid.user.query.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
public class ResCheckNameVO {

    private int statusCode;
    private String message;
    private String href;
    private Map<String, Object> result;
}
