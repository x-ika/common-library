package com.simplejcode.commons.misc.util;

import lombok.*;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExcelRow {

    private List<ExcelCell> cells = new ArrayList<>();

}
