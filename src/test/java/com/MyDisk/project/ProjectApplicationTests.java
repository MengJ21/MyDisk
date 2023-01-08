package com.MyDisk.project;

import com.MyDisk.project.entity.*;
import com.MyDisk.project.mapper.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
class ProjectApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private ShareMapper shareMapper;

    @Test
    void test1() {
        User user = new User();
        user.setUserId(2);
        user.setPassword("123456");
        user.setPhone("123456789010");

        System.out.println(userMapper.update(user));
    }

    @Test
    void testFileMapper1() {
        File file = new File();
        file.setFileName("./home/llp");
        file.setFileSize((double) 10);
        file.setCreateTime(new Date());
        file.setRecycle(0);
        file.setFileStoreId(1);
        file.setFileType("文件夹");
        System.out.println(fileMapper.insertFile(file));
    }
    @Test
    void testFileMapper2() {
        System.out.println(fileMapper.updateRecycle(1, 1));
    }

    @Test
    void testFileMapper3() {
        System.out.println(fileMapper.deleteFile(1));
    }
    @Test
    void testFileMapper4() {
        System.out.println(fileMapper.updateFileName(2, "./home/llp"));
    }

    @Test
    void testShareMapperInsert() {
        Share share = new Share();
        share.setUserId(7);
        share.setFileId(2);
        share.setSharedUserId(25);
        share.setShareTime(new Date());
        share.setLike(0);
        System.out.println(shareMapper.insertShare(share));
    }

    @Test
    void testShareMapperDelete() {
        System.out.println(shareMapper.deleteShare(3));
    }
    @Test
    void test2() {
        System.out.println(fileMapper.queryByFileStoreIdAndRecycle(1, 0));
    }

    @Test
    void test3() {
        System.out.println(shareMapper.queryBySharedUserIdAndLike(1,1));
    }
    @Test
    void test4() {
        System.out.println(shareMapper.queryBySharedUserId(1));
    }

    @Test
    void test5() {
        System.out.println(shareMapper.queryBySharedUserId(1));
    }
}
