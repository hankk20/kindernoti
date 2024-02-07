package kr.co.kindernoti.institution.infrastructure.basedata;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("cityCode")
@TypeAlias("cityCode")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CityCode {

    @Id
    private String id;

    @EqualsAndHashCode.Include
    private String code;

    private String name;

    private List<CountyCode> counties = new ArrayList<>();

    public CityCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public void addAll(List<CountyCode> counties) {
        this.counties = new ArrayList<>(counties);
        this.counties.addAll(counties);
    }

    public void addCounty(CountyCode countyCode) {
        if(counties.contains(countyCode)){
            return;
        }
        counties.add(countyCode);
    }

    @Value(staticConstructor = "of")
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class CountyCode {

        @EqualsAndHashCode.Include
        String code;

        String name;
    }

}
