package alar0615;

import alar0615.cache.integration.ILeveledCache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class TwoLevelCacheApp {

    public static void main(String[] args) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring-config.xml");
        ILeveledCache<Integer, String> cache = appContext.getBean("iLeveledCache", ILeveledCache.class);
        try {
            StringBuilder sb = new StringBuilder();
            for (int i=1; i<70; i++){
                cache.add(i, "hello" + i);
                for (int j=0; j<i; j++){
                    cache.getObject(i);
                }
                sb.append(cache.getObject(i));
            }
            System.out.println(sb.toString());
            System.out.println(cache.sortedKeys().toString());
            System.out.println(cache.keySet().toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
