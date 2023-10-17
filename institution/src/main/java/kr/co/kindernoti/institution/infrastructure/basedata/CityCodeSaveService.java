package kr.co.kindernoti.institution.infrastructure.basedata;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 시군구 코드 기초 데이터 저장
 */
@Slf4j
@RequiredArgsConstructor
@Service
@ConfigurationProperties(prefix = "basedata.citycode")
public class CityCodeSaveService {

    @Setter
    private String path;

    private final CityCodeCsvReaderService cityCodeCsvReaderService;

    private final CityCodeRepository cityCodeRepository;

    private final BaseDataFileRepository baseDataFileRepository;

    /**
     * 파일명과 파일 최종수정일로 저장된 파일 정보를 찿고 해당 정보가 존재 하지 않으면
     * 기존 데이터를 삭제하고 새로 저장한다.
     */
    public void initCitiCode() {
        BaseDataFile baseDataFile = cityCodeCsvReaderService.getBaseDataFile(path);
        existBaseDataFile(baseDataFile)
                .flatMap(exist -> {
                    if(exist) {
                        return Mono.empty();
                    }
                    return deleteAndSave(baseDataFile);
                }).flatMap(baseDataFileRepository::save)
                .subscribe();
    }

    private Mono<BaseDataFile> deleteAndSave(BaseDataFile baseDataFile) {
        return cityCodeRepository.deleteAll()
                .then(Mono.defer(() -> {
                    List<CityCode> cityCodes = cityCodeCsvReaderService.readCityCodeCsv(path);
                    baseDataFile.setStatus(BaseDataFile.BaseDataFileStatus.SUCCESS);
                    return cityCodeRepository.saveAll(cityCodes)
                            .then(Mono.just(baseDataFile));
                }));
    }

    private Mono<Boolean> existBaseDataFile(BaseDataFile data) {
        QBaseDataFile baseDataFile = QBaseDataFile.baseDataFile;
        BooleanExpression predicate = baseDataFile.filename.eq(data.getFilename())
                .and(baseDataFile.lastFileModified.eq(data.getLastFileModified()))
                .and(baseDataFile.status.eq(BaseDataFile.BaseDataFileStatus.SUCCESS));

        return baseDataFileRepository.exists(predicate);
    }
}
