package br.com.eventhorizon.personaladminsitration.financial.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FinancialStatusConverter implements AttributeConverter<FinancialStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(FinancialStatus financialStatus) {
        if(financialStatus == null) return null;
        return financialStatus.getId();
    }
    @Override
    public FinancialStatus convertToEntityAttribute(Integer dbData) {
        if(dbData == null) return null;
        return FinancialStatus.fromId(dbData);
    }
}
