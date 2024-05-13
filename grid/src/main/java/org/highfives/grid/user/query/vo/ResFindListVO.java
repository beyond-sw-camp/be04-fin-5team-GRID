package org.highfives.grid.user.query.vo;



import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ResFindListVO {

    private int statusCode;
    private String message;
    private String href;
    private List<SimpleInfo> result;
}
