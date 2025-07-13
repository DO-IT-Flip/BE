package com.DoIt2.Flip.domain.quote.dto;

import com.DoIt2.Flip.domain.quote.entity.Quote;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuoteResponse {

    private Long id;
    private String krContent;
    private String enContent;

    public static QuoteResponse from(Quote quote) {
        return new QuoteResponse(
                quote.getId(),
                quote.getKrContent(),
                quote.getEnContent()
        );
    }
}
