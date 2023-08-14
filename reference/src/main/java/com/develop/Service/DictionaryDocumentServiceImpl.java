package com.develop.Service;

import com.develop.Model.DictionaryDocument;
import com.develop.Repository.DictionaryRepository;
import com.develop.dto.DirectoryDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DictionaryDocumentServiceImpl implements DictionaryDocumentService {
    private final DictionaryRepository dictionaryRepository;

    @Override
    public DictionaryDocument create(DictionaryDocument dictionaryDocument) {
        dictionaryDocument = dictionaryRepository.insert(dictionaryDocument);
        String dictionaryId = dictionaryDocument.getId();
        for (int i = 0; i < dictionaryDocument.getData().size(); i++) {
            if(!dictionaryDocument.getData().get(i).containsKey("dictionaryId")){
               dictionaryDocument.getData().get(i).put("dictionaryId", dictionaryId);
            }
        }
        return dictionaryRepository.save(dictionaryDocument);
    }

    @Override
    public void delete(String id) {
        dictionaryRepository.deleteById(id);
    }

    @Override
    public List<DictionaryDocument> listAll() {
        return dictionaryRepository.findAll();
    }

    @Override
    public DictionaryDocument findById(String id) {
        return dictionaryRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public DictionaryDocument addData(DirectoryDataDto dto) {
        DictionaryDocument document = findById(dto.getDictionaryId());
        int dataSize = document.getData().size();
        Map<String, Object> data = new HashMap<>();
        data.put("data" ,dto.getData());
        document.getData().add(data);
        if(!document.getData().get(dataSize).containsKey("dictionaryId")){
            document.getData().get(dataSize).put("dictionaryId", dto.getDictionaryId());
        }if(!document.getData().get(dataSize).containsKey("dataId")){
            document.getData().get(dataSize).put("dataId", dto.getDataId());
        }
         return dictionaryRepository.save(document);
    }

    @Override
    public DictionaryDocument deleteData(String id, String dataId) {
        DictionaryDocument document = findById(id);
        List<Map<String, Object>> newList = document.getData().stream().filter(stringObjectMap -> !stringObjectMap.get("dataId").toString().equals(dataId)).collect(Collectors.toList());
        document.setData(newList);
        return dictionaryRepository.save(document);
    }

    @Override
    public DictionaryDocument updateData(DirectoryDataDto dto) {
        DictionaryDocument document = findById(dto.getDictionaryId());
        List<Map<String, Object>> newList = document.getData().stream().map(stringObjectMap -> {
            if (stringObjectMap.get("dataId").toString().equals(dto.getDataId())) {
                Map<String, Object> newData = new HashMap<>();
                newData.put("dataId", dto.getDataId());
                newData.put("dictionaryId", dto.getDictionaryId());
                newData.put("data", dto.getData());
                return newData;
            }
            return stringObjectMap;
        }).collect(Collectors.toList());
        document.setData(newList);
        return dictionaryRepository.save(document);
    }
}
