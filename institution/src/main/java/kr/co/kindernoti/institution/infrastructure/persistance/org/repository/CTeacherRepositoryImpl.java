package kr.co.kindernoti.institution.infrastructure.persistance.org.repository;

import kr.co.kindernoti.institution.domain.model.teacher.TeacherId;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.TeacherData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CTeacherRepositoryImpl implements CTeacherRepository{

    private final ReactiveMongoOperations operations;

    @Override
    public Mono<TeacherData> updateAccount(TeacherId id, Account account) {
        return operations.findAndModify(Query.query(Criteria.where("id").is(id))
                , new Update().set("account", account)
                , FindAndModifyOptions.options().returnNew(true)
                , TeacherData.class);
    }
}
