package com.develop.Model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@Document(collection = "dictionaries")
public class DictionaryDocument {
    @Id
    private String id;
    private Map<String, Object> dictionaryName;
    private List<Map<String, Object>> fields;
    private List<Map<String, Object>> data;
}
