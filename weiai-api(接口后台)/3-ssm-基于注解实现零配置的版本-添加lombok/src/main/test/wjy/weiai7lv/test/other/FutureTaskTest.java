package wjy.weiai7lv.test.other;

import org.junit.Test;
import wjy.weiai7lv.entity.User;

import java.util.concurrent.*;

public class FutureTaskTest {

    private Runnable testRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Test
    public void testFutureTask(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        User user = new User();
        user.setUsername("helllo");
        Future<User> future = executorService.submit(testRunnable,user);
        executorService.shutdown();
        try {
            future.cancel(true);
            User result = future.get();
            System.out.println(result.getUsername());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testFutureTask2(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<User> userFuture = executorService.submit(new Callable<User>() {
            @Override
            public User call() throws Exception {
                User user = new User();
                user.setUsername("你好");
                Thread.sleep(5000);
                return user;
            }
        });
        executorService.shutdown();
        try {
            User user = userFuture.get();
            System.out.println(user.getUsername());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
