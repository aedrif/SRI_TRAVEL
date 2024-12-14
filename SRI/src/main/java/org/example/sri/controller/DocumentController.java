package org.example.sri.controller;


import lombok.RequiredArgsConstructor;
import org.example.sri.models.Document;
import org.example.sri.repo.DocumentRepository;
import org.example.sri.service.LuceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "http://localhost:3000")
public class DocumentController {

    private final DocumentRepository documentRepository;

    private final LuceneService luceneService;

    public DocumentController(DocumentRepository documentRepository, LuceneService luceneService) {
        this.documentRepository = documentRepository;
        this.luceneService = luceneService;
    }

    @PostMapping
    public ResponseEntity<Document> addDocument(@RequestBody Document document) throws Exception {
        // Save to MySQL
        Document savedDocument = documentRepository.save(document);

        // Index in Lucene
        luceneService.indexDocument(savedDocument);

        return ResponseEntity.ok(savedDocument);
    }

    @PostMapping("/token")
    public ResponseEntity<Set<String>> addtokenset(@RequestBody HashMap<String,String> token) throws Exception {

        return ResponseEntity.ok(luceneService.tokenizeWithEnglishAnalyzer(token.get("tokens"),token.get("category")));
    }

    @PostMapping("/many")
    public ResponseEntity<List<Document>> addDocuments(@RequestBody List<Document> documents) throws Exception {
        // Save to MySQL
        List<Document> savedDocuments = documentRepository.saveAll(documents);
        for(Document document : documents) {
            // Index in Lucene
            luceneService.indexDocument(document);
        }
        return ResponseEntity.ok(savedDocuments);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Document>> searchDocuments(
            @RequestParam (defaultValue = " ")String query,
            @RequestParam (defaultValue = " ")String dest,
            @RequestParam(defaultValue = "3") int maxResults,
            @RequestParam String algo
    ) throws Exception {
        // Validate the algorithm parameter
        if (!algo.equalsIgnoreCase("tfidf") && !algo.equalsIgnoreCase("bm25")) {
            return ResponseEntity.badRequest().body(null); // Respond with a 400 error for invalid input
        }

        // Call the service method with the algorithm parameter
        List<Document> searchResults = luceneService.searchDocuments2(query, dest, maxResults, algo);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return documentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {
        documentRepository.deleteById(id);
        try {
            luceneService.deleteDocument(id);
            return ResponseEntity.ok("Document deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting document: " + e.getMessage());
        }
    }

}