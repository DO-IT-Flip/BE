package com.DoIt2.Flip.domain.quote.controller;

import com.DoIt2.Flip.domain.quote.dto.QuoteResponse;
import com.DoIt2.Flip.domain.quote.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;

    @GetMapping("/random")
    public ResponseEntity<?> getRandomQuote() {
        try {
            QuoteResponse response = quoteService.getRandomQuote();
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
