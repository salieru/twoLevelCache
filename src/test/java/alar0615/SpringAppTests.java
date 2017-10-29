package alar0615;

import alar0615.cache.api.ICache;
import alar0615.cache.integration.ILeveledCache;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class SpringAppTests {

    ApplicationContext appContext = new ClassPathXmlApplicationContext("spring-config.xml");
    ILeveledCache<Integer, String> twoLevelCache = appContext.getBean("iLeveledCache", ILeveledCache.class);
    ICache<Integer, String> hdCache = appContext.getBean("fileCache", ICache.class);

    @Test
    public void testCacheOne() throws IOException, ClassNotFoundException {
        twoLevelCache.add(10, "Hello!");
        String object = twoLevelCache.getObject(10);
        Assert.assertEquals("Hello!", object);

    }

    @Test
    public void reCache() throws IOException, ClassNotFoundException {
        int maxMemory = twoLevelCache.maxLevel1size();
        int reCacheSize = maxMemory + 100;
        for (int i=0; i < reCacheSize; i++){
            twoLevelCache.add(i, String.valueOf(i+1));
            for (int j=0; j<i; j++){
                twoLevelCache.getObject(i);
            }
        }
        Set<Integer> actualKeysInMemory = twoLevelCache.level1keys();
        for (int i = reCacheSize-1; i>reCacheSize - maxMemory; i--){
            if (!actualKeysInMemory.contains(i)){
                Assert.fail();
            }
        }

    }

    @Test
    public void testHD() throws IOException, ClassNotFoundException {
        hdCache.cacheObject(10, "hey");
        Assert.assertEquals("hey", hdCache.getObject(10));
    }
}
