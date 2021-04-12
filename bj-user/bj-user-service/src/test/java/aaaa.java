import com.bajie.user.UserApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApp.class)
public class aaaa {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void aa() {
        redisTemplate.opsForValue().set("A", "dsad");
    }
}
