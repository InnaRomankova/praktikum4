import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class Mesto1Test {

    String bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2NmNhMmJiYmQ1NmMxNDAwM2Q0Nzk3ZWMiLCJpYXQiOjE3MjU4MTk4OTIsImV4cCI6MTcyNjQyNDY5Mn0.k4a7tHvCJ0lHyDrnCSgO6j3sr0Bz9EumJGEqRu5XHgo";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru";
    }

    @Test
    @DisplayName("Add a new photo")
    @Description("This test is for adding a new photo to Mesto.")
    public void testAddNewPhoto() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(bearerToken)
                .body("{\"name\":\"Москва333\",\"link\":\"https://code.s3.yandex.net/qa-automation-engineer/java/files/paid-track/sprint1/photoSelenide.jpg\"}")
                .post("/api/cards")
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Like the first photo")
    @Description("This test is for liking the first photo on Mesto.")
    public void testLikeTheFirstPhoto() {
        String photoID = getTheFirstPhotoID();

        likePhotoById(photoID);
        deleteLikePhotoById(photoID);
    }

    @Step("Take the first photo from the list")
    private String getTheFirstPhotoID() {
        return  given()
                .auth().oauth2(bearerToken)
                .get("/api/cards")
                .then().extract().body().path("data[0]._id");
    }

    @Step("Like a photo by id")
    private void likePhotoById(String photoId) {
        given()
                .auth().oauth2(bearerToken)
                .put("/api/cards/{photoId}/likes", photoId)
                .then().assertThat().statusCode(200);
    }

    @Step("Delete like from the photo by id")
    private  void deleteLikePhotoById(String photoId) {
        given()
                .auth().oauth2(bearerToken)
                .delete("/api/cards/{photoId}/likes", photoId)
                .then().assertThat().statusCode(200);
    }
}
