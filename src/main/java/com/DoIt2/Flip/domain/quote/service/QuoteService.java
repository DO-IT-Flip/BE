package com.DoIt2.Flip.domain.quote.service;

import com.DoIt2.Flip.domain.quote.dto.QuoteResponse;
import com.DoIt2.Flip.domain.quote.entity.Quote;
import com.DoIt2.Flip.domain.quote.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuoteService {

    private final QuoteRepository quoteRepository;

    public QuoteResponse getRandomQuote() {
        Quote quote = quoteRepository.findRandomQuote();
        if (quote == null) {
            throw new IllegalStateException("등록된 명언이 없습니다.");
        }
        return QuoteResponse.from(quote);
    }
}
