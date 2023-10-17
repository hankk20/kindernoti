package kr.co.kindernoti.institution.infrastructure.persistance.org.repository;

import kr.co.kindernoti.institution.domain.model.org.GradeId;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.AClassData;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.GradeData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CGradeRepositoryImpl implements CGradeRepository{

    private final ReactiveMongoOperations operations;

    @Override
    public Mono<GradeData> addAClass(GradeId gradeId, AClassData aClassData) {
        return operations.findAndModify(Query.query(Criteria.where("id").is(gradeId))
                , new Update().addToSet("aClasses", aClassData)
                , FindAndModifyOptions.options().returnNew(true)
                , GradeData.class);
    }
}
