package com.elephants.betting.common.constants;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationProperties {
    @Value("${inhouse.cut.percentage}")
    private double inHouseCutPercentage;
}
