package com.develop.Controller;

import com.develop.Model.DictionaryDocument;
import com.develop.Service.DictionaryDocumentService;
import com.develop.dto.DirectoryDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DictionaryDocumentController {
    private final DictionaryDocumentService dictionaryDocumentService;

    @PostMapping("/create")
    public ResponseEntity<DictionaryDocument> create(@RequestBody DictionaryDocument dictionaryDocument){
        return ResponseEntity.ok(dictionaryDocumentService.create(dictionaryDocument));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DictionaryDocument>> all(){
        return ResponseEntity.ok(dictionaryDocumentService.listAll());
    }

    @GetMapping("/getFields/{id}")
    public ResponseEntity<List<Map<String, Object>>> getFields(@PathVariable String id) {
        DictionaryDocument document = dictionaryDocumentService.findById(id);
        return ResponseEntity.ok(document.getFields());
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id){
        dictionaryDocumentService.delete(id);
    }
    @PutMapping("/add-data")
    public ResponseEntity<DictionaryDocument> addData(@RequestBody DirectoryDataDto dto) {
        dictionaryDocumentService.addData(dto);
        return ResponseEntity.ok(dictionaryDocumentService.findById(dto.getDictionaryId()));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<DictionaryDocument> findById(@PathVariable String id){
        DictionaryDocument document = dictionaryDocumentService.findById(id);
        return ResponseEntity.ok(document);
    }

    @DeleteMapping("/deleteData/{id}/{dataId}")
    public ResponseEntity<DictionaryDocument> deleteData(@PathVariable String id,@PathVariable String dataId){
        dictionaryDocumentService.deleteData(id,dataId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/update")
    public ResponseEntity<DictionaryDocument> find(@RequestBody DirectoryDataDto dto){
        return ResponseEntity.ok(dictionaryDocumentService.updateData(dto));
    }
}
