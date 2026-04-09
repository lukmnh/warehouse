package com.geli.warehouse.util;

import com.geli.warehouse.repository.ItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductGenerator {
    private final ItemsRepository itemsRepository;

    public String generateDefault() {
        return generateWithPrefix("PRD");
    }

    public String generate(String brandCode, String categoryCode){
        String prefix = brandCode + "-" + categoryCode;
        return generateWithPrefix(prefix);
    }

    public String generateWithPrefix(String prefix){
        String lastCode = itemsRepository.findLastProductCodeByPrefix(prefix);
        int nextNumber = 1;
        if (lastCode != null && lastCode.contains("-")) {
            String[] parts = lastCode.split("-");
            if (parts.length > 0) {
                try {
                    nextNumber = Integer.parseInt(parts[parts.length - 1]) + 1;
                } catch (NumberFormatException e) {
                    nextNumber = 1;
                }
            }
        }
        return String.format("%s-%03d", prefix, nextNumber);
    }
}
