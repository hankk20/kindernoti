package kr.co.kindernoti.institution.infrastructure.basedata;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 첫번째열은 도 또는 시의 명칭 두번째는 해당 코드
 * 세번째열은 군 또는 구의 명칭 네번째는 해당 코드를 가진 Csv를 읽어드려서 CityCode 객체로 변환한다.
 * csv ex : 서울특별시,11,중구,11140
 */
@Slf4j
@Service
public class CityCodeCsvReaderService {

    /**
     * path의 CSV 파일을 읽어드려서 시군구 코드 List를 리턴 한다.
     * @param path
     * @return 시군구
     */
    public List<CityCode> readCityCodeCsv(String path) {
        try {
            BufferedReader bufferedReader = reader(path);
            Map<CityCode, List<String>> grouping = grouping(bufferedReader);
            return convertCityCode(grouping);
        }catch (IOException e){
            log.error("[CSV Read Error]", e);
        }
        return Collections.emptyList();
    }

    public BaseDataFile getBaseDataFile(String path){
        ClassPathResource classPathResource = new ClassPathResource(path);
        try {
            return BaseDataFile.from(classPathResource.getFile());
        }catch (IOException e) {
            throw new IllegalArgumentException("파일을 찿지 못하였습니다. ["+path+"]");
        }
    }

    /**
     * Classpath 경로의 파일을 읽어 드린다.
     * @param path
     * @return
     * @throws IOException
     */
    public BufferedReader reader(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        if(classPathResource.exists()) {
            InputStreamReader in = new InputStreamReader(classPathResource.getInputStream());
            return new BufferedReader(in);
        }
        throw new FileNotFoundException("읽어드릴 CSV 파일이 없습니다. ["+path+"]");
    }

    /**
     * 시도 코드 별로 그룹화 한다.
     * @param reader
     * @return
     */
    public Map<CityCode, List<String>> grouping(BufferedReader reader) {
        return reader.lines()
                .collect(Collectors.groupingBy(this::parseCityCode));
    }

    /**
     * 문자열 신군구 코드를 CityCode 객체로 변환한다.
     * @param map
     * @return
     */
    public List<CityCode> convertCityCode(Map<CityCode, List<String>> map) {
        Set<CityCode> cityCodes = map.keySet();
        return cityCodes.stream()
                .peek(c -> {
                    List<String> strings = map.get(c);
                    List<CityCode.CountyCode> countyCodes = strings.stream()
                            .map(this::parseCountyCode)
                            .toList();
                    c.addAll(countyCodes);
                })
                .toList();
    }

    /**
     * CSV 문자열에서 군/구 코드를 가져와 CountyCode 객체로 만든다.
     * @param s
     * @return
     */
    private CityCode.CountyCode parseCountyCode(String s) {
        String[] split = StringUtils.split(s, ",");
        return CityCode.CountyCode.of(split[3], split[2]);
    }

    /**
     * CSV 문자열에서 도/시 코드를 가져와 CityCode 객체로 만든다.
     * @param s
     * @return
     */
    private CityCode parseCityCode(String s) {
        String[] split = StringUtils.split(s, ",");
        return new CityCode(split[1], split[0]);
    }

}
