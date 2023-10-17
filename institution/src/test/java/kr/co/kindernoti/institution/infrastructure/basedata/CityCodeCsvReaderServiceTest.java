package kr.co.kindernoti.institution.infrastructure.basedata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CityCodeCsvReaderServiceTest {

    CityCodeCsvReaderService cityCodeCsvReaderService;

    String filePath = "basedata/testcitycode.csv";

    @BeforeEach
    void setUp() {
        cityCodeCsvReaderService = new CityCodeCsvReaderService();
    }

    @Test
    @DisplayName("csv 파일 읽기")
    void testRead() throws IOException {
        List<CityCode> cityCodes = cityCodeCsvReaderService.readCityCodeCsv(filePath);

        assertThat(cityCodes)
                .extracting(CityCode::getCode)
                .contains("11", "26", "27")
                .size().isEqualTo(3);
    }

}