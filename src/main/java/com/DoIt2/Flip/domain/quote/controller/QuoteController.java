package com.DoIt2.Flip.domain.quote.controller;

import com.DoIt2.Flip.domain.quote.entity.Quote;
import com.DoIt2.Flip.domain.quote.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;

    @GetMapping("/random")
    public Quote getRandomQuote() {
        return quoteService.getRandomQuote();
    }
}
