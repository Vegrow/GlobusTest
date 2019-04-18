package com.litvinov.globustest.util;

import com.litvinov.globustest.data.Valute;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ValuteHelper {
    public static BigDecimal exchange(BigDecimal amount, Valute from, Valute to){
        BigDecimal fromRatio = new BigDecimal(from.getNominal()).divide(new BigDecimal(from.getValue().replaceAll(",", ".")), 10, RoundingMode.HALF_EVEN);
        BigDecimal toRatio = new BigDecimal(to.getNominal()).divide(new BigDecimal(to.getValue().replaceAll(",", ".")), 10, RoundingMode.HALF_EVEN);
        BigDecimal ratio = toRatio.divide(fromRatio,10, RoundingMode.HALF_EVEN);
        return amount.multiply(ratio).setScale(4, RoundingMode.HALF_EVEN);
    }
}
