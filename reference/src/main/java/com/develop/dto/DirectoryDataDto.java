package com.develop.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@RequiredArgsConstructor
public class DirectoryDataDto {
    private Map<String, Object> data;
    private String dictionaryId;
    private String dataId;
}
