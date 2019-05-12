package wujiuye.morelove.webapi;


import wujiuye.morelove.MoreLoveApplication;
import wujiuye.morelove.pojo.User;
import wujiuye.morelove.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MoreLoveApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestApp {

    @Autowired
    private UserService userService;


    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testApplction() {
        User user = userService.getUserWithUsername("就业");
        System.out.printf(user.getUsername());
    }

    @Test
    public void testController() {
        ResponseEntity<String> response = this.restTemplate.postForEntity(
                "http://localhost:8080/user/login", String.class, String.class);
        System.out.println("status-code:" + response.getStatusCode() + ",response-body:" + response.getBody());
    }


    @Test
    public void testMovieBoxOffice() {
        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(
                "http://localhost:8080/found/movie", String.class, String.class
        );
        System.out.println(responseEntity);
    }

    @Test
    public void testMovieBoxOffice2() {
        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(
                "http://localhost:8080/found/movie2", String.class, String.class
        );
        System.out.println(responseEntity);
    }

}
