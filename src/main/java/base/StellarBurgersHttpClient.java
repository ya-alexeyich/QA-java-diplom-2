package base;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;

public abstract class StellarBurgersHttpClient {
    protected static RequestSpecification getBaseRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(URL.HOST)
                .setContentType(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new ErrorLoggingFilter())
                .addFilter(new AllureRestAssured())
                .build();
    }
}
