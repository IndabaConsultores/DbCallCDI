package es.indaba.jdbc.test.extension;

import javax.enterprise.context.ApplicationScoped;

import es.indaba.jdbc.annotations.api.DatabaseCall;
import es.indaba.jdbc.annotations.api.FieldResult;
import es.indaba.jdbc.annotations.api.StoredProcedure;
import es.indaba.jdbc.annotations.api.StoredProcedureParameter;
import es.indaba.jdbc.annotations.api.StoredProcedureResult;
import es.indaba.jdbc.test.cdi.PostgreSQL;
import es.indaba.jdbc.test.result.ProcedureResult;

@ApplicationScoped
public interface PostgreDBTester {

    @StoredProcedure("{ ? = call echo( ? ) }")
    @StoredProcedureResult({@FieldResult(name = "value", position = 1)})
    @DatabaseCall(qualifier = PostgreSQL.class)
    public ProcedureResult<String> callEchoAsFunction(@StoredProcedureParameter(2) String name) throws Exception;

    @StoredProcedure("{call echoProc(?,?)}")
    @StoredProcedureResult({@FieldResult(name = "value", position = 2)})
    @DatabaseCall(qualifier = PostgreSQL.class)
    public ProcedureResult<String> callEchoAsProcedure(@StoredProcedureParameter(1) String name) throws Exception;

}
