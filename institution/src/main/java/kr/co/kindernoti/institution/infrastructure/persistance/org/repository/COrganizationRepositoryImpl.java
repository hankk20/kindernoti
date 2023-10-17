package kr.co.kindernoti.institution.infrastructure.persistance.org.repository;

import kr.co.kindernoti.institution.domain.model.org.GradeId;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.GradeData;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.InstitutionData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class COrganizationRepositoryImpl implements COrganizationRepository {

    private final ReactiveMongoOperations operations;

    @Override
    public Mono<InstitutionData> addGrade(InstitutionId id, GradeData gradeData) {
        return operations.findAndModify(Query.query(Criteria.where("id").is(id))
                , new Update().addToSet("grades", gradeData)
                , FindAndModifyOptions.options().returnNew(true)
                , InstitutionData.class);
    }

    @Override
    public Mono<GradeData> findByGradeId(InstitutionId id, GradeId gradeId) {
        Query query = Query.query(Criteria.where("id").is(id)
                .and("grades").elemMatch(Criteria.where("id").is(gradeId)));
        query.fields().include("grades.$")
                .exclude("id")
        ;
        return operations.findOne(query
                , GradeData.class, "grade");
    }

    @Override
    public Mono<InstitutionData> findByOrganizationId(InstitutionId id, GradeId gradeId) {
        Query query = Query.query(Criteria.where("id").is(id)
                .and("grades").elemMatch(Criteria.where("id").is(gradeId)));
        query.fields().include("grades.$").exclude("id");
        return operations.findOne(query
                , InstitutionData.class);
    }

}
